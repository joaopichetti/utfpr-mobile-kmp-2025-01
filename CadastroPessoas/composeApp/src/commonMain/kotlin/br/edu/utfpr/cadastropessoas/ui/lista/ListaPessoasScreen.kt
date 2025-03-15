package br.edu.utfpr.cadastropessoas.ui.lista

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoas.data.model.Pessoa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPessoasScreen(
    modifier: Modifier = Modifier
) {
    val carregando = false
    val ocorreuErro = false
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ListaPessoasTopBar(
                mostrarAcaoAtualizar = true,
                onAtualizar = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { innerPadding ->
        if (carregando) {
            CarregandoPessoas(modifier = Modifier.padding(innerPadding))
        } else if (ocorreuErro) {
            ErroCarregarPessoas(
                modifier = Modifier.padding(innerPadding),
                onTentarNovamente = {}
            )
        } else {
            ListaPessoas(
                modifier = Modifier.padding(innerPadding),
                pessoas = listOf(),
                onPessoaSelecionada = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPessoasTopBar(
    modifier: Modifier = Modifier,
    mostrarAcaoAtualizar: Boolean,
    onAtualizar: () -> Unit
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text("Pessoas") },
        actions = {
            if (mostrarAcaoAtualizar) {
                IconButton(onClick = onAtualizar) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Atualizar"
                    )
                }
            }
        }
    )
}

@Composable
fun CarregandoPessoas(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(60.dp)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Carregando Pessoas...",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ErroCarregarPessoas(
    modifier: Modifier = Modifier,
    onTentarNovamente: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Erro ao carregar",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
            text = "Não foi possível carregar as pessoas",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
            text = "Aguarde um momento e tente novamente",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        ElevatedButton(
            onClick = onTentarNovamente,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Tentar novamente")
        }
    }
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
            text = "Nenhuma pessoa encontrada",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Adicione alguma presionando o \"+\"",
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
                contentDescription = "Selecionar"
            )
        }
    )
}