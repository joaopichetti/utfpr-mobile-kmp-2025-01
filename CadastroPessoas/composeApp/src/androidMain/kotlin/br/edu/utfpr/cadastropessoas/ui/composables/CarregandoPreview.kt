package br.edu.utfpr.cadastropessoas.ui.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, heightDp = 400)
@Composable
private fun CarregandoPreview() {
    MaterialTheme {
        Carregando(
            texto = "Carregando..."
        )
    }
}