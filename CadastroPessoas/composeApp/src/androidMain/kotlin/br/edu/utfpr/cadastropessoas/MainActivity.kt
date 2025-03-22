package br.edu.utfpr.cadastropessoas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import br.edu.utfpr.cadastropessoas.data.datasource.driver.DriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                driverFactory = DriverFactory(
                    context = LocalContext.current
                )
            )
        }
    }
}