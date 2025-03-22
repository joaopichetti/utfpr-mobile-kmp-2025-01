package br.edu.utfpr.cadastropessoas.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cep(
    val cep: String = "",
    val logradouro: String = "",
    val bairro: String = "",
    @SerialName("localidade")
    val cidade: String = "",
    val uf: String = ""
) {
    val cidadeUf get(): String = "$cidade - $uf"
}