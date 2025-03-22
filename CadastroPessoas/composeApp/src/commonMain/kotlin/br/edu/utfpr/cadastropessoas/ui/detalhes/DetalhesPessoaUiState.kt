package br.edu.utfpr.cadastropessoas.ui.detalhes

import br.edu.utfpr.cadastropessoas.data.model.Pessoa

data class DetalhesPessoaUiState(
    val carregando: Boolean = false,
    val ocorreuErroAoCarregar: Boolean = false,
    val pessoa: Pessoa = Pessoa(),
    val removendo: Boolean = false,
    val ocorreuErroAoRemover: Boolean = false,
    val pessoaRemovida: Boolean = false,
    val mostrarDialogConfirmacao: Boolean = false
) {
    val sucessoAoCarregar get(): Boolean = !carregando && !ocorreuErroAoCarregar
}