package br.edu.utfpr.cadastropessoas.data.repository

import br.edu.utfpr.cadastropessoas.data.datasource.PessoaDatasource
import br.edu.utfpr.cadastropessoas.data.model.Pessoa

class PessoaRepository(private val pessoaDatasource: PessoaDatasource) {
    fun listar(): List<Pessoa> {
        return pessoaDatasource.carregarPessoas()
    }

    fun carregar(id: Int): Pessoa {
        return pessoaDatasource.carregarPessoa(id)
    }

    fun remover(id: Int) {
        pessoaDatasource.remover(id)
    }

    fun salvar(pessoa: Pessoa) {
        if (pessoa.id > 0) {
            pessoaDatasource.atualizar(pessoa)
        } else {
            pessoaDatasource.adicionar(pessoa)
        }
    }
}