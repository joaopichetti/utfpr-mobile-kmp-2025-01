package br.edu.utfpr.cadastropessoas.ui.form

data class FormField(
    val value: String = "",
    val errorMessage: String? = null
)

data class FormState(
    val nome: FormField = FormField(),
    val cpf: FormField = FormField(),
    val telefone: FormField = FormField(),
    val cep: FormField = FormField(),
    val logradouro: FormField = FormField(),
    val numero: FormField = FormField(),
    val complemento: FormField = FormField(),
    val bairro: FormField = FormField(),
    val cidade: FormField = FormField(),
    val buscandoCep: Boolean = false
) {
    val isValid get(): Boolean = nome.errorMessage == null &&
            cpf.errorMessage == null &&
            telefone.errorMessage == null &&
            cep.errorMessage == null &&
            logradouro.errorMessage == null &&
            numero.errorMessage == null &&
            complemento.errorMessage == null &&
            bairro.errorMessage == null &&
            cidade.errorMessage == null
}

data class FormPessoaUiState(
    val idPessoa: Int = 0,
    val carregando: Boolean = false,
    val ocorreuErroAoCarregar: Boolean = false,
    val formState: FormState = FormState(),
    val salvando: Boolean = false,
    val ocorreuErroAoSalvar: Boolean = false,
    val salvoComSucesso: Boolean = false
) {
    val novaPessoa get(): Boolean = idPessoa <= 0
    val sucessoAoCarregar get(): Boolean = !carregando && !ocorreuErroAoCarregar
}