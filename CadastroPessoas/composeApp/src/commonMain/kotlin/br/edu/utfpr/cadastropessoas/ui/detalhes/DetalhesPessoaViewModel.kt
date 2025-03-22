package br.edu.utfpr.cadastropessoas.ui.detalhes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetalhesPessoaViewModel(
    private val pessoaRepository: PessoaRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "DetalhesPessoaViewModel"
    private val idPessoa: Int = savedStateHandle.get<Int>("id") ?: 0

    var uiState: DetalhesPessoaUiState by mutableStateOf(DetalhesPessoaUiState())
        private set

    init {
        carregarPessoa()
    }

    fun carregarPessoa() {
        if (uiState.carregando) return

        uiState = uiState.copy(
            carregando = true,
            ocorreuErroAoCarregar = false
        )
        viewModelScope.launch {
            delay(1500)
            uiState = try {
                uiState.copy(
                    carregando = false,
                    pessoa = pessoaRepository.carregar(idPessoa)
                )
            } catch (ex: Exception) {
                println("[$tag]: Erro ao carregar dados da pessoa com id $idPessoa")
                ex.printStackTrace()
                uiState.copy(
                    carregando = false,
                    ocorreuErroAoCarregar = true
                )
            }
        }
    }

    fun remover() {
        if (uiState.removendo) return

        uiState = uiState.copy(
            removendo = true,
            ocorreuErroAoRemover = false,
            mostrarDialogConfirmacao = false
        )
        viewModelScope.launch {
            delay(1500)
            uiState = try {
                pessoaRepository.remover(idPessoa)
                uiState.copy(
                    removendo = false,
                    pessoaRemovida = true
                )
            } catch (ex: Exception) {
                println("[$tag]: Erro ao excluir pessoa com id $idPessoa")
                ex.printStackTrace()
                uiState.copy(
                    removendo = false,
                    ocorreuErroAoRemover = true
                )
            }
        }
    }

    fun mostrarDialogConfirmacao() {
        uiState = uiState.copy(
            mostrarDialogConfirmacao = true
        )
    }

    fun ocultarDialogConfirmacao() {
        uiState = uiState.copy(
            mostrarDialogConfirmacao = false
        )
    }
}