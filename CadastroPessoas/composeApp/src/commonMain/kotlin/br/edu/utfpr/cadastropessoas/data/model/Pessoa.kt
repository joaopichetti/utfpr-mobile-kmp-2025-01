package br.edu.utfpr.cadastropessoas.data.model

data class Pessoa(
    val id: Int = 0,
    val nome: String = "",
    val cpf: String = "",
    val telefone: String = "",
    val endereco: Endereco = Endereco()
)
