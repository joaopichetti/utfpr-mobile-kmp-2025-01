package br.edu.utfpr.consultacep.data.service

import br.edu.utfpr.consultacep.data.model.Endereco
import retrofit2.http.GET
import retrofit2.http.Path

interface CepApiService {
    @GET("{cep}/json/")
    suspend fun buscarCep(@Path("cep") cep: String): Endereco
}