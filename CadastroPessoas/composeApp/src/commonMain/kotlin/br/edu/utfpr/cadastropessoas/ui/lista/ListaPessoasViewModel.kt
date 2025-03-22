package br.edu.utfpr.cadastropessoas.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListaPessoasViewModel(
    private val pessoaRepository: PessoaRepository
) : ViewModel() {
    private val tag: String = "ListaPessoasViewModel"

    var uiState: ListaPessoasUiState by mutableStateOf(ListaPessoasUiState())
        private set

    init {
        carregarPessoas()
    }

    fun carregarPessoas() {
        if (uiState.carregando) return

        uiState = uiState.copy(
            carregando = true,
            ocorreuErro = false
        )
        viewModelScope.launch {
            delay(1500)
            uiState = try {
                uiState.copy(
                    carregando = false,
                    pessoas = pessoaRepository.listar()
                )
            } catch (ex: Exception) {
                println("[${tag}]: Erro ao carregar pessoas")
                ex.printStackTrace()
                uiState.copy(
                    carregando = false,
                    ocorreuErro = true
                )
            }
        }
    }
}