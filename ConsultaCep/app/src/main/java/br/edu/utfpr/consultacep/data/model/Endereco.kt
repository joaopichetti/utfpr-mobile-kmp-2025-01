package br.edu.utfpr.consultacep.data.model

data class Endereco(
    val cep: String = "",
    val logradouro: String = "",
    val bairro: String = "",
    val localidade: String = "",
    val uf: String = ""
)