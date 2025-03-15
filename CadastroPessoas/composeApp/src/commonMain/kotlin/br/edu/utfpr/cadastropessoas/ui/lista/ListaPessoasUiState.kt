package br.edu.utfpr.cadastropessoas.ui.lista

import br.edu.utfpr.cadastropessoas.data.model.Pessoa

data class ListaPessoasUiState(
    val carregando: Boolean = false,
    val ocorreuErro: Boolean = false,
    val pessoas: List<Pessoa> = listOf()
) {
    val sucesso get(): Boolean = !carregando && !ocorreuErro
}
