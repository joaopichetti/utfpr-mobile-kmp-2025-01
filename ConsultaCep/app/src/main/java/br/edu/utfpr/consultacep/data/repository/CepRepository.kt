package br.edu.utfpr.consultacep.data.repository

import br.edu.utfpr.consultacep.data.model.Endereco
import br.edu.utfpr.consultacep.data.service.CepApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CepRepository {
    private val api: CepApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(CepApiService::class.java)
    }

    suspend fun buscarCep(cep: String): Endereco = api.buscarCep(cep)
}