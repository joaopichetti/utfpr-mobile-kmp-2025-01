package br.edu.utfpr.cadastropessoas.ui.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoas.data.model.Endereco
import br.edu.utfpr.cadastropessoas.data.model.Pessoa
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class FormPessoaViewModel(
    private val idPessoa: Int
) : ViewModel() {
    private val tag: String = "FormPessoaViewModel"
    private val pessoaRepository: PessoaRepository = PessoaRepository()

    var uiState: FormPessoaUiState by mutableStateOf(FormPessoaUiState(idPessoa = idPessoa))
        private set

    init {
        if (!uiState.novaPessoa) {
            carregarPessoa()
        }
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
                val ocorreuErro = Random.nextBoolean()
                if (ocorreuErro) {
                    throw Exception("Erro gerado para teste")
                }
                val pessoa: Pessoa = pessoaRepository.carregar(idPessoa)
                uiState.copy(
                    carregando = false,
                    formState = FormState(
                        nome = FormField(pessoa.nome),
                        cpf = FormField(pessoa.cpf),
                        telefone = FormField(pessoa.telefone),
                        cep = FormField(pessoa.endereco.cep),
                        logradouro = FormField(pessoa.endereco.logradouro),
                        numero = FormField(pessoa.endereco.numero.toString()),
                        complemento = FormField(pessoa.endereco.complemento),
                        bairro = FormField(pessoa.endereco.bairro),
                        cidade = FormField(pessoa.endereco.cidade)
                    )
                )
            } catch (ex: Exception) {
                println("[$tag]: Erro ao carregar dados da pessoa com código $idPessoa")
                ex.printStackTrace()
                uiState.copy(
                    carregando = false,
                    ocorreuErroAoCarregar = true
                )
            }
        }
    }

    fun salvar() {
        if (uiState.salvando || !formularioValido()) return

        uiState = uiState.copy(
            salvando = true,
            ocorreuErroAoSalvar = false
        )
        viewModelScope.launch {
            delay(1500)
            val pessoa = Pessoa(
                id = idPessoa,
                nome = uiState.formState.nome.value,
                cpf = uiState.formState.cpf.value,
                telefone = uiState.formState.telefone.value,
                endereco = Endereco(
                    cep = uiState.formState.cep.value,
                    logradouro = uiState.formState.logradouro.value,
                    numero = uiState.formState.numero.value.toIntOrNull() ?: 0,
                    complemento = uiState.formState.complemento.value,
                    bairro = uiState.formState.bairro.value,
                    cidade = uiState.formState.cidade.value
                )
            )
            uiState = try {
                val ocorreuErro = Random.nextBoolean()
                if (ocorreuErro) {
                    throw Exception("Erro gerado para teste")
                }
                pessoaRepository.salvar(pessoa)
                uiState.copy(
                    salvando = false,
                    salvoComSucesso = true
                )
            } catch (ex: Exception) {
                println("[$tag]: Erro ao salvar a pessoa com id $idPessoa")
                ex.printStackTrace()
                uiState.copy(
                    salvando = false,
                    ocorreuErroAoSalvar = true
                )
            }
        }
    }

    fun onNomeAlterado(novoNome: String) {
        if (uiState.formState.nome.value != novoNome) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    nome = FormField(
                        value = novoNome,
                        errorMessage = validarNome(novoNome)
                    )
                )
            )
        }
    }

    fun onCpfAlterado(valor: String) {
        val novoCpf = valor.replace("\\D".toRegex(), "")
        if (novoCpf.length <= 11 && uiState.formState.cpf.value != novoCpf) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    cpf = FormField(
                        value = novoCpf,
                        errorMessage = validarCpf(novoCpf)
                    )
                )
            )
        }
    }

    fun onTelefoneAlterado(valor: String) {
        val novoTelefone = valor.replace("\\D".toRegex(), "")
        if (novoTelefone.length <= 11 && uiState.formState.telefone.value != novoTelefone) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    telefone = FormField(
                        value = novoTelefone,
                        errorMessage = validarTelefone(novoTelefone)
                    )
                )
            )
        }
    }

    fun onCepAlterado(valor: String) {
        val novoCep = valor.replace("\\D".toRegex(), "")
        if (novoCep.length <= 8 && uiState.formState.cep.value != novoCep) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    cep = FormField(
                        value = novoCep,
                        errorMessage = validarCep(novoCep)
                    )
                )
            )
        }
    }

    fun onNumeroAlterado(valor: String) {
        val novoNumero = valor.replace("\\D".toRegex(), "")
        if (uiState.formState.numero.value != novoNumero) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    numero = FormField(
                        value = novoNumero,
                        errorMessage = validarNumero(novoNumero)
                    )
                )
            )
        }
    }

    fun onComplementoAlterado(novoComplemento: String) {
        if (uiState.formState.complemento.value != novoComplemento) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    complemento = uiState.formState.complemento.copy(
                        value = novoComplemento
                    )
                )
            )
        }
    }

    private fun validarNome(nome: String): String? = if (nome.isBlank()) {
        "O nome é obrigatório"
    } else {
        null
    }

    private fun validarCpf(cpf: String): String? = if (cpf.isBlank()) {
        "O CPF é obrigatório"
    } else if (cpf.length != 11) {
        "Informe um CPF válido"
    } else {
        null
    }

    private fun validarTelefone(telefone: String): String? = if (telefone.isBlank()) {
        "O telefone é obrigatório"
    } else if (telefone.length < 10 || telefone.length > 11) {
        "Informe um telefone válido"
    } else {
        null
    }

    private fun validarCep(cep: String): String? = if (cep.isBlank()) {
        "O CEP é obrigatório"
    } else if (cep.length != 8) {
        "Informe um CEP válido"
    } else {
        null
    }

    private fun validarNumero(numero: String): String? = if (numero.isBlank()) {
        "O número é obrigatório"
    } else {
        null
    }

    private fun formularioValido(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                nome = uiState.formState.nome.copy(
                    errorMessage = validarNome(uiState.formState.nome.value)
                ),
                cpf = uiState.formState.cpf.copy(
                    errorMessage = validarCpf(uiState.formState.cpf.value)
                ),
                telefone = uiState.formState.telefone.copy(
                    errorMessage = validarTelefone(uiState.formState.telefone.value)
                ),
                cep = uiState.formState.cep.copy(
                    errorMessage = validarCep(uiState.formState.cep.value)
                ),
                numero = uiState.formState.numero.copy(
                    errorMessage = validarNumero(uiState.formState.numero.value)
                )
            )
        )
        return uiState.formState.isValid
    }
}