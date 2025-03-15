package br.edu.utfpr.cadastropessoas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import br.edu.utfpr.cadastropessoas.ui.lista.ListaPessoasScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        ListaPessoasScreen()
    }
}