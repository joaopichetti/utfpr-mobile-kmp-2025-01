package br.edu.utfpr.cadastropessoas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.cadastropessoas.ui.form.FormPessoaScreen
import br.edu.utfpr.cadastropessoas.ui.lista.ListaPessoasScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = modifier
        ) {
            composable(route = "list") {
                ListaPessoasScreen(
                    onAdicionarPessoa = {
                        navController.navigate("form")
                    }
                )
            }
            composable(
                route = "form?id={id}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ) {
                FormPessoaScreen(
                    idPessoa = 0,
                    pessoaSalvaComSucesso = {
                        navController.popBackStack()
                    },
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}