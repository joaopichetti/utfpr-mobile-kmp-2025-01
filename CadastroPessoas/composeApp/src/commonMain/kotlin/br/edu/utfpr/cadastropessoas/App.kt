package br.edu.utfpr.cadastropessoas

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.edu.utfpr.cadastropessoas.data.datasource.PessoaDatasource
import br.edu.utfpr.cadastropessoas.data.datasource.driver.DriverFactory
import br.edu.utfpr.cadastropessoas.data.repository.CepRepository
import br.edu.utfpr.cadastropessoas.data.repository.PessoaRepository
import br.edu.utfpr.cadastropessoas.ui.form.FormPessoaScreen
import br.edu.utfpr.cadastropessoas.ui.form.FormPessoaViewModel
import br.edu.utfpr.cadastropessoas.ui.lista.ListaPessoasScreen
import br.edu.utfpr.cadastropessoas.ui.lista.ListaPessoasViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    driverFactory: DriverFactory
) {
    val pessoaDatasource = PessoaDatasource(driverFactory)
    val pessoaRepository = PessoaRepository(pessoaDatasource)
    val cepRepository = CepRepository()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = modifier
        ) {
            composable(route = "list") {
                val viewModel: ListaPessoasViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            ListaPessoasViewModel(
                                pessoaRepository = pessoaRepository
                            )
                        }
                    }
                )
                ListaPessoasScreen(
                    viewModel = viewModel,
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
            ) { navBackStackEntry ->
                val idPessoa = navBackStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
                val viewModel: FormPessoaViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            FormPessoaViewModel(
                                idPessoa = idPessoa,
                                pessoaRepository = pessoaRepository,
                                cepRepository = cepRepository
                            )
                        }
                    }
                )
                FormPessoaScreen(
                    viewModel = viewModel,
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