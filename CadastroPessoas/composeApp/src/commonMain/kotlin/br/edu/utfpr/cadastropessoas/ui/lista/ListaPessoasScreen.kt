package br.edu.utfpr.cadastropessoas.ui.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoas.data.model.Pessoa
import br.edu.utfpr.cadastropessoas.ui.composables.AppBarPadrao
import br.edu.utfpr.cadastropessoas.ui.composables.Carregando
import br.edu.utfpr.cadastropessoas.ui.composables.ErroCarregar
import cadastropessoas.composeapp.generated.resources.Res
import cadastropessoas.composeapp.generated.resources.adicionar
import cadastropessoas.composeapp.generated.resources.adicione_alguma_pessoa
import cadastropessoas.composeapp.generated.resources.atualizar
import cadastropessoas.composeapp.generated.resources.carregando_pessoas
import cadastropessoas.composeapp.generated.resources.erro_ao_carregar_pessoas
import cadastropessoas.composeapp.generated.resources.nenhuma_pessoa_encontrada
import cadastropessoas.composeapp.generated.resources.pessoas
import cadastropessoas.composeapp.generated.resources.selecionar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPessoasScreen(
    modifier: Modifier = Modifier,
    viewModel: ListaPessoasViewModel = koinViewModel<ListaPessoasViewModel>(),
    onAdicionarPessoa: () -> Unit,
    onPessoaSelecionada: (Pessoa) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ListaPessoasTopBar(
                mostrarAcaoAtualizar = viewModel.uiState.sucesso,
                onAtualizar = viewModel::carregarPessoas
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdicionarPessoa) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.adicionar)
                )
            }
        }
    ) { innerPadding ->
        if (viewModel.uiState.carregando) {
            Carregando(
                modifier = Modifier.padding(innerPadding),
                texto = "${stringResource(Res.string.carregando_pessoas)}..."
            )
        } else if (viewModel.uiState.ocorreuErro) {
            ErroCarregar(
                modifier = Modifier.padding(innerPadding),
                texto = stringResource(Res.string.erro_ao_carregar_pessoas),
                onTentarNovamente = viewModel::carregarPessoas
            )
        } else {
            ListaPessoas(
                modifier = Modifier.padding(innerPadding),
                pessoas = viewModel.uiState.pessoas,
                onPessoaSelecionada = onPessoaSelecionada
            )
        }
    }
}

@Composable
fun ListaPessoasTopBar(
    modifier: Modifier = Modifier,
    mostrarAcaoAtualizar: Boolean,
    onAtualizar: () -> Unit
) {
    AppBarPadrao(
        modifier = modifier,
        titulo = stringResource(Res.string.pessoas),
        actions = {
            if (mostrarAcaoAtualizar) {
                IconButton(onClick = onAtualizar) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(Res.string.atualizar)
                    )
                }
            }
        }
    )
}

@Composable
fun ListaPessoas(
    modifier: Modifier = Modifier,
    pessoas: List<Pessoa>,
    onPessoaSelecionada: (Pessoa) -> Unit
) {
    if (pessoas.isEmpty()) {
        ListaVazia()
    } else {
        LazyColumn(
            modifier = modifier.padding(top = 4.dp)
        ) {
            itemsIndexed(pessoas) { index, pessoa ->
                ItemListaPessoas(
                    pessoa = pessoa,
                    onPessoaSelecionada = onPessoaSelecionada
                )
                if (index < pessoas.lastIndex) {
                    Divider(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ListaVazia(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(Res.string.nenhuma_pessoa_encontrada),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(Res.string.adicione_alguma_pessoa),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListaPessoas(
    modifier: Modifier = Modifier,
    pessoa: Pessoa,
    onPessoaSelecionada: (Pessoa) -> Unit
) {
    ListItem(
        modifier = modifier
            .padding(8.dp)
            .clickable { onPessoaSelecionada(pessoa) },
        headlineText = {
            Text("${pessoa.id} - ${pessoa.nome}")
        },
        supportingText = {
            Text(pessoa.endereco.descricao)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(Res.string.selecionar)
            )
        }
    )
}