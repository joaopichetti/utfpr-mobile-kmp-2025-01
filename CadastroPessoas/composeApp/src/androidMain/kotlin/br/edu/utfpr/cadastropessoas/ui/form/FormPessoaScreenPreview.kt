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
            formState = FormState(),
            onNomeAlterado = {},
            onCpfAlterado = {},
            onTelefoneAlterado = {},
            onCepAlterado = {},
            onNumeroAlterado = {},
            onComplementoAlterado = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormPessoaExistenteFormularioPreview() {
    MaterialTheme {
        Formulario(
            idPessoa = 0,
            formState = FormState(
                nome = FormField(value = "João"),
                cpf = FormField(
                    errorMessage = "CPF obrigatório"
                ),
                telefone = FormField(
                    value = "(99) 9999-99",
                    errorMessage = "Telefone inválido"
                )
            ),
            onNomeAlterado = {},
            onCpfAlterado = {},
            onTelefoneAlterado = {},
            onCepAlterado = {},
            onNumeroAlterado = {},
            onComplementoAlterado = {}
        )
    }
}