package br.edu.utfpr.cadastropessoas.ui.lista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ListaPessoasViewModel : ViewModel() {
    private val tag: String = "ListaPessoasViewModel"
    private val pessoaRepository: PessoaRepository = PessoaRepository()

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
                val ocorreuErro = Random.nextBoolean()
                if (ocorreuErro) {
                    throw Exception("Erro gerado para teste")
                }
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