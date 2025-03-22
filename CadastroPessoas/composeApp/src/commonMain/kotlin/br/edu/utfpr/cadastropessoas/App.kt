package br.edu.utfpr.cadastropessoas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.cadastropessoas.ui.detalhes.DetalhesPessoaScreen
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
                    },
                    onPessoaSelecionada = { pessoa ->
                        navController.navigate("details/${pessoa.id}")
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
                    pessoaSalvaComSucesso = {
                        navegarParaLista(navController)
                    },
                    onVoltar = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "details/{id}",
                arguments = listOf(
                    navArgument(name = "id") {
                        type = NavType.IntType
                    }
                )
            ) { navBackStackEntry ->
                DetalhesPessoaScreen(
                    onPessoaRemovida = {
                        navegarParaLista(navController)
                    },
                    onVoltar = {
                        navController.popBackStack()
                    },
                    onAlterar = {
                        val idPessoa = navBackStackEntry.arguments?.getInt("id") ?: 0
                        navController.navigate("form?id=$idPessoa")
                    }
                )
            }
        }
    }
}

fun navegarParaLista(navController: NavHostController) {
    navController.navigate("list") {
        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}
