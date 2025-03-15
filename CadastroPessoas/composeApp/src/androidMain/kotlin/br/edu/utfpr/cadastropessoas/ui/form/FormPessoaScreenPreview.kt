package br.edu.utfpr.cadastropessoas.ui.form

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun FormPessoaTopBarPreview() {
    MaterialTheme {
        FormPessoaTopBar(
            mostrarAcoes = true,
            novaPessoa = false,
            salvando = false,
            onSalvar = {},
            onVoltar = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormPessoaTopBarSalvandoPreview() {
    MaterialTheme {
        FormPessoaTopBar(
            mostrarAcoes = true,
            novaPessoa = true,
            salvando = true,
            onSalvar = {},
            onVoltar = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun FormPessoaNovaFormularioPreview() {
    MaterialTheme {
        Formulario(
            idPessoa = 0,
            onNomeAlterado = {},
            onCpfAlterado = {},
            onTelefoneAlterado = {},
            onCepAlterado = {},
            onNumeroAlterado = {},
            onComplementoAlterado = {}
        )
    }
}