package br.edu.utfpr.cadastropessoas.ui.detalhes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoas.data.model.Pessoa
import br.edu.utfpr.cadastropessoas.ui.composables.AppBarPadrao
import br.edu.utfpr.cadastropessoas.ui.composables.Carregando
import br.edu.utfpr.cadastropessoas.ui.composables.ErroCarregar
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarCep
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarCpf
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarTelefone
import cadastropessoas.composeapp.generated.resources.Res
import cadastropessoas.composeapp.generated.resources.alterar
import cadastropessoas.composeapp.generated.resources.atencao
import cadastropessoas.composeapp.generated.resources.bairro
import cadastropessoas.composeapp.generated.resources.cancelar
import cadastropessoas.composeapp.generated.resources.carregando_pessoa
import cadastropessoas.composeapp.generated.resources.cep
import cadastropessoas.composeapp.generated.resources.cidade
import cadastropessoas.composeapp.generated.resources.codigo
import cadastropessoas.composeapp.generated.resources.complemento
import cadastropessoas.composeapp.generated.resources.confirmar
import cadastropessoas.composeapp.generated.resources.confirmar_remover_pessoa
import cadastropessoas.composeapp.generated.resources.cpf
import cadastropessoas.composeapp.generated.resources.detalhes_da_pessoa
import cadastropessoas.composeapp.generated.resources.endereco
import cadastropessoas.composeapp.generated.resources.erro_ao_carregar_pessoa
import cadastropessoas.composeapp.generated.resources.erro_ao_remover_pessoa
import cadastropessoas.composeapp.generated.resources.logradouro
import cadastropessoas.composeapp.generated.resources.nao_informado
import cadastropessoas.composeapp.generated.resources.nome
import cadastropessoas.composeapp.generated.resources.numero
import cadastropessoas.composeapp.generated.resources.remover
import cadastropessoas.composeapp.generated.resources.telefone
import cadastropessoas.composeapp.generated.resources.voltar
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesPessoaScreen(
    modifier: Modifier = Modifier,
    viewModel: DetalhesPessoaViewModel = koinViewModel<DetalhesPessoaViewModel>(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onPessoaRemovida: () -> Unit,
    onVoltar: () -> Unit,
    onAlterar: () -> Unit
) {
    LaunchedEffect(viewModel.uiState.pessoaRemovida) {
        if (viewModel.uiState.pessoaRemovida) {
            onPessoaRemovida()
        }
    }

    LaunchedEffect(snackbarHostState, viewModel.uiState.ocorreuErroAoRemover) {
        if (viewModel.uiState.ocorreuErroAoRemover) {
            snackbarHostState.showSnackbar(
                message = getString(Res.string.erro_ao_remover_pessoa)
            )
        }
    }

    if (viewModel.uiState.mostrarDialogConfirmacao) {
        DialogConfirmacao(
            onCancelar = viewModel::ocultarDialogConfirmacao,
            onConfirmar = viewModel::remover
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            DetalhesPessoaTopBar(
                removendo = viewModel.uiState.removendo,
                mostrarAcoes = viewModel.uiState.sucessoAoCarregar,
                onVoltar = onVoltar,
                onAlterar = onAlterar,
                onRemover = viewModel::mostrarDialogConfirmacao
            )
        }
    ) { innerPadding ->
        if (viewModel.uiState.carregando) {
            Carregando(
                modifier = Modifier.padding(innerPadding),
                texto = "${stringResource(Res.string.carregando_pessoa)}..."
            )
        } else if (viewModel.uiState.ocorreuErroAoCarregar) {
            ErroCarregar(
                modifier = Modifier.padding(innerPadding),
                texto = stringResource(Res.string.erro_ao_carregar_pessoa),
                onTentarNovamente = viewModel::carregarPessoa
            )
        } else {
            DetalhesPessoa(
                modifier = Modifier.padding(innerPadding),
                pessoa = viewModel.uiState.pessoa
            )
        }
    }
}

@Composable
fun DialogConfirmacao(
    modifier: Modifier = Modifier,
    onCancelar: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        title = { Text(stringResource(Res.string.atencao)) },
        text = {
            Text(stringResource(Res.string.confirmar_remover_pessoa))
        },
        onDismissRequest = onCancelar,
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(stringResource(Res.string.confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(stringResource(Res.string.cancelar))
            }
        }
    )
}

@Composable
fun DetalhesPessoaTopBar(
    modifier: Modifier = Modifier,
    mostrarAcoes: Boolean,
    removendo: Boolean,
    onVoltar: () -> Unit,
    onRemover: () -> Unit,
    onAlterar: () -> Unit
) {
    AppBarPadrao(
        modifier = modifier.fillMaxWidth(),
        titulo = stringResource(Res.string.detalhes_da_pessoa),
        navigationIcon = {
            IconButton(onClick = onVoltar) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.voltar)
                )
            }
        },
        actions = {
            if (mostrarAcoes) {
                if (removendo) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onAlterar) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(Res.string.alterar)
                        )
                    }
                    IconButton(onClick = onRemover) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(Res.string.remover)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DetalhesPessoa(
    modifier: Modifier,
    pessoa: Pessoa
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TituloPessoa(
            texto = "${stringResource(Res.string.codigo)} - ${pessoa.id}"
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.nome),
            valorAtributo = pessoa.nome
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.cpf),
            valorAtributo = pessoa.cpf.formatarCpf()
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.telefone),
            valorAtributo = pessoa.telefone.formatarTelefone()
        )
        Divider(modifier = Modifier.padding(top = 8.dp))
        TituloPessoa(
            texto = stringResource(Res.string.endereco)
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.cep),
            valorAtributo = pessoa.endereco.cep.formatarCep()
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.logradouro),
            valorAtributo = pessoa.endereco.logradouro
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.numero),
            valorAtributo = pessoa.endereco.numero.toString()
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.complemento),
            valorAtributo = pessoa.endereco.complemento
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.bairro),
            valorAtributo = pessoa.endereco.bairro
        )
        AtributoPessoa(
            nomeAtributo = stringResource(Res.string.cidade),
            valorAtributo = pessoa.endereco.cidade
        )
    }
}

@Composable
fun TituloPessoa(
    modifier: Modifier = Modifier,
    texto: String
) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(16.dp)
    )
}

@Composable
fun AtributoPessoa(
    modifier: Modifier = Modifier,
    nomeAtributo: String,
    valorAtributo: String
) {
    Column(
        modifier = modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        Text(
            text = nomeAtributo,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        val textStyle: TextStyle
        val texto: String
        if (valorAtributo.isNotEmpty()) {
            texto = valorAtributo
            textStyle = MaterialTheme.typography.labelLarge
        } else {
            texto = stringResource(Res.string.nao_informado)
            textStyle = MaterialTheme.typography.labelSmall.copy(
                fontStyle = FontStyle.Italic
            )
        }
        Text(
            text = texto,
            style = textStyle,
            color = MaterialTheme.colorScheme.primary
        )
    }
}