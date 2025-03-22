package br.edu.utfpr.cadastropessoas.data.datasource

import br.edu.utfpr.cadastropessoas.PessoaQueries
import br.edu.utfpr.cadastropessoas.data.datasource.driver.DriverFactory
import br.edu.utfpr.cadastropessoas.data.model.Endereco
import br.edu.utfpr.cadastropessoas.data.model.Pessoa

class PessoaDatasource(driverFactory: DriverFactory) {
    private val database: AppDatabase = AppDatabase(driverFactory.createDriver())
    private val pessoaQueries: PessoaQueries = database.pessoaQueries

    fun carregarPessoas(): List<Pessoa> {
        return pessoaQueries.carregarPessoas(::mapearPessoa).executeAsList()
    }

    fun carregarPessoa(id: Int): Pessoa {
        return pessoaQueries.carregarPessoa(id.toLong(), ::mapearPessoa).executeAsOne()
    }

    fun adicionar(pessoa: Pessoa) {
        pessoaQueries.adicionar(
            nome = pessoa.nome,
            cpf = pessoa.cpf,
            telefone = pessoa.telefone,
            cep = pessoa.endereco.cep,
            logradouro = pessoa.endereco.logradouro,
            numero = pessoa.endereco.numero.toLong(),
            complemento = pessoa.endereco.complemento,
            bairro = pessoa.endereco.bairro,
            cidade = pessoa.endereco.cidade
        )
    }

    fun atualizar(pessoa: Pessoa) {
        pessoaQueries.atualizar(
            nome = pessoa.nome,
            cpf = pessoa.cpf,
            telefone = pessoa.telefone,
            cep = pessoa.endereco.cep,
            logradouro = pessoa.endereco.logradouro,
            numero = pessoa.endereco.numero.toLong(),
            complemento = pessoa.endereco.complemento,
            bairro = pessoa.endereco.bairro,
            cidade = pessoa.endereco.cidade,
            id = pessoa.id.toLong()
        )
    }

    fun remover(id: Int) {
        pessoaQueries.remover(id.toLong())
    }

    private fun mapearPessoa(
        id: Long,
        nome: String,
        cpf: String,
        telefone: String,
        cep: String,
        logradouro: String,
        numero: Long,
        complemento: String?,
        bairro: String,
        cidade: String
    ): Pessoa = Pessoa(
        id = id.toInt(),
        nome = nome,
        cpf = cpf,
        telefone = telefone,
        endereco = Endereco(
            cep = cep,
            logradouro = logradouro,
            numero = numero.toInt(),
            complemento = complemento ?: "",
            bairro = bairro,
            cidade = cidade
        )
    )
}