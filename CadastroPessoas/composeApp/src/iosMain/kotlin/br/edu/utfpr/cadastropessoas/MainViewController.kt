package br.edu.utfpr.cadastropessoas

import androidx.compose.ui.window.ComposeUIViewController
import br.edu.utfpr.cadastropessoas.data.datasource.driver.DriverFactory

fun MainViewController() = ComposeUIViewController {
    App(
        driverFactory = DriverFactory()
    )
}