package br.edu.utfpr.cadastropessoas.ui.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.cadastropessoas.data.model.Cep
import br.edu.utfpr.cadastropessoas.data.model.Endereco
import br.edu.utfpr.cadastropessoas.data.model.Pessoa
import br.edu.utfpr.cadastropessoas.data.repository.CepRepository
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import cadastropessoas.composeapp.generated.resources.Res
import cadastropessoas.composeapp.generated.resources.cep_invalido
import cadastropessoas.composeapp.generated.resources.cep_obrigatorio
import cadastropessoas.composeapp.generated.resources.cpf_invalido
import cadastropessoas.composeapp.generated.resources.cpf_obrigatorio
import cadastropessoas.composeapp.generated.resources.erro_ao_carregar_cep
import cadastropessoas.composeapp.generated.resources.nome_obrigatorio
import cadastropessoas.composeapp.generated.resources.numero_obrigatorio
import cadastropessoas.composeapp.generated.resources.telefone_invalido
import cadastropessoas.composeapp.generated.resources.telefone_obrigatorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class FormPessoaViewModel(
    private val pessoaRepository: PessoaRepository,
    private val cepRepository: CepRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val tag: String = "FormPessoaViewModel"
    private val idPessoa: Int = savedStateHandle.get<String>("id")?.toIntOrNull() ?: 0

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
                        errorStringResource = validarNome(novoNome)
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
                        errorStringResource = validarCpf(novoCpf)
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
                        errorStringResource = validarTelefone(novoTelefone)
                    )
                )
            )
        }
    }

    fun onCepAlterado(valor: String) {
        val novoCep = valor.replace("\\D".toRegex(), "")
        if (novoCep.length <= 8 && uiState.formState.cep.value != novoCep) {
            val mensagemValidacao = validarCep(novoCep)
            val podeBuscar = mensagemValidacao == null && novoCep.length == 8
            if (podeBuscar) {
                buscarCep(novoCep)
            } else {
                uiState = uiState.copy(
                    formState = uiState.formState.copy(
                        cep = FormField(
                            value = novoCep,
                            errorStringResource = mensagemValidacao
                        )
                    )
                )
            }
        }
    }

    fun onNumeroAlterado(valor: String) {
        val novoNumero = valor.replace("\\D".toRegex(), "")
        if (uiState.formState.numero.value != novoNumero) {
            uiState = uiState.copy(
                formState = uiState.formState.copy(
                    numero = FormField(
                        value = novoNumero,
                        errorStringResource = validarNumero(novoNumero)
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

    private fun validarNome(nome: String): StringResource? = if (nome.isBlank()) {
        Res.string.nome_obrigatorio
    } else {
        null
    }

    private fun validarCpf(cpf: String): StringResource? = if (cpf.isBlank()) {
        Res.string.cpf_obrigatorio
    } else if (cpf.length != 11) {
        Res.string.cpf_invalido
    } else {
        null
    }

    private fun validarTelefone(telefone: String): StringResource? = if (telefone.isBlank()) {
        Res.string.telefone_obrigatorio
    } else if (telefone.length < 10 || telefone.length > 11) {
        Res.string.telefone_invalido
    } else {
        null
    }

    private fun validarCep(cep: String): StringResource? = if (cep.isBlank()) {
        Res.string.cep_obrigatorio
    } else if (cep.length != 8) {
        Res.string.cep_invalido
    } else {
        null
    }

    private fun validarNumero(numero: String): StringResource? = if (numero.isBlank()) {
        Res.string.numero_obrigatorio
    } else {
        null
    }

    private fun formularioValido(): Boolean {
        uiState = uiState.copy(
            formState = uiState.formState.copy(
                nome = uiState.formState.nome.copy(
                    errorStringResource = validarNome(uiState.formState.nome.value)
                ),
                cpf = uiState.formState.cpf.copy(
                    errorStringResource = validarCpf(uiState.formState.cpf.value)
                ),
                telefone = uiState.formState.telefone.copy(
                    errorStringResource = validarTelefone(uiState.formState.telefone.value)
                ),
                cep = uiState.formState.cep.copy(
                    errorStringResource = validarCep(uiState.formState.cep.value)
                ),
                numero = uiState.formState.numero.copy(
                    errorStringResource = validarNumero(uiState.formState.numero.value)
                )
            )
        )
        return uiState.formState.isValid
    }

    private fun buscarCep(cep: String) {
        if (uiState.formState.buscandoCep) return

        uiState = uiState.copy(
            formState = uiState.formState.copy(
                buscandoCep = true,
                cep = FormField(
                    value = cep,
                    errorStringResource = null
                ),
                logradouro = FormField(""),
                bairro = FormField(""),
                cidade = FormField("")
            )
        )
        viewModelScope.launch {
            delay(1500)
            uiState = try {
                val cepRetornado: Cep = cepRepository.buscarCep(cep)
                uiState.copy(
                    formState = uiState.formState.copy(
                        logradouro = FormField(cepRetornado.logradouro),
                        bairro = FormField(cepRetornado.bairro),
                        cidade = FormField(cepRetornado.cidadeUf),
                        buscandoCep = false
                    )
                )
            } catch (ex: Exception) {
                println("[$tag]: Erro ao consultar o CEP $cep")
                ex.printStackTrace()
                uiState.copy(
                    formState = uiState.formState.copy(
                        buscandoCep = false,
                        cep = uiState.formState.cep.copy(
                            errorStringResource = Res.string.erro_ao_carregar_cep
                        )
                    )
                )
            }
        }
    }
}