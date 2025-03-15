package br.edu.utfpr.cadastropessoas.ui.lista

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.cadastropessoas.data.model.Endereco
import br.edu.utfpr.cadastropessoas.data.model.Pessoa

@Preview(showBackground = true)
@Composable
private fun ListaPessoasTopBarPreview() {
    MaterialTheme {
        ListaPessoasTopBar(
            mostrarAcaoAtualizar = true,
            onAtualizar = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun ListaPessoasVaziaPreview() {
    MaterialTheme {
        ListaPessoas(
            pessoas = listOf(),
            onPessoaSelecionada = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaPessoasPreenchidaPreview() {
    MaterialTheme {
        ListaPessoas(
            pessoas = listOf(
                Pessoa(
                    id = 1,
                    nome = "João",
                    endereco = Endereco(
                        logradouro = "Tocantins",
                        numero = 1000,
                        cidade = "Pato Branco - PR"
                    )
                ),
                Pessoa(
                    id = 2,
                    nome = "José",
                    endereco = Endereco(
                        logradouro = "Av. Brasil",
                        numero = 10,
                        cidade = "Curitiba - PR"
                    )
                )
            ),
            onPessoaSelecionada = {}
        )
    }
}