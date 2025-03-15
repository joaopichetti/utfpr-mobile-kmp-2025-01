package br.edu.utfpr.cadastropessoas.data.model

data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val numero: Int = 0,
    val complemento: String = "",
    val bairro: String = "",
    val cidade: String = ""
) {
    val descricao get(): String = "$logradouro, $numero, $cidade"
}
