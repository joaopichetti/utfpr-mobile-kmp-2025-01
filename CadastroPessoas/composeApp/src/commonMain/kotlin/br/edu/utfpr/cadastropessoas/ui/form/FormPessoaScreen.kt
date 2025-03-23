package br.edu.utfpr.cadastropessoas.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import br.edu.utfpr.cadastropessoas.ui.composables.AppBarPadrao
import br.edu.utfpr.cadastropessoas.ui.composables.Carregando
import br.edu.utfpr.cadastropessoas.ui.composables.ErroCarregar
import br.edu.utfpr.cadastropessoas.ui.form.visualtransformation.CepVisualTransformation
import br.edu.utfpr.cadastropessoas.ui.form.visualtransformation.CpfVisualTransformation
import br.edu.utfpr.cadastropessoas.ui.form.visualtransformation.TelefoneVisualTransformation
import cadastropessoas.composeapp.generated.resources.Res
import cadastropessoas.composeapp.generated.resources.a_definir
import cadastropessoas.composeapp.generated.resources.bairro
import cadastropessoas.composeapp.generated.resources.carregando_pessoa
import cadastropessoas.composeapp.generated.resources.cep
import cadastropessoas.composeapp.generated.resources.cidade
import cadastropessoas.composeapp.generated.resources.codigo
import cadastropessoas.composeapp.generated.resources.complemento
import cadastropessoas.composeapp.generated.resources.cpf
import cadastropessoas.composeapp.generated.resources.editar_pessoa
import cadastropessoas.composeapp.generated.resources.endereco
import cadastropessoas.composeapp.generated.resources.erro_ao_carregar_pessoa
import cadastropessoas.composeapp.generated.resources.erro_ao_salvar_pessoa
import cadastropessoas.composeapp.generated.resources.logradouro
import cadastropessoas.composeapp.generated.resources.nome
import cadastropessoas.composeapp.generated.resources.nova_pessoa
import cadastropessoas.composeapp.generated.resources.numero
import cadastropessoas.composeapp.generated.resources.salvar
import cadastropessoas.composeapp.generated.resources.telefone
import cadastropessoas.composeapp.generated.resources.voltar
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPessoaScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    pessoaSalvaComSucesso: () -> Unit,
    onVoltar: () -> Unit,
    viewModel: FormPessoaViewModel = koinViewModel<FormPessoaViewModel>()
) {
    LaunchedEffect(viewModel.uiState.salvoComSucesso) {
        if (viewModel.uiState.salvoComSucesso) {
            pessoaSalvaComSucesso()
        }
    }
    LaunchedEffect(snackbarHostState, viewModel.uiState.ocorreuErroAoSalvar) {
        if (viewModel.uiState.ocorreuErroAoSalvar) {
            snackbarHostState.showSnackbar(
                message = getString(Res.string.erro_ao_salvar_pessoa)
            )
        }
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            FormPessoaTopBar(
                mostrarAcoes = viewModel.uiState.sucessoAoCarregar,
                salvando = viewModel.uiState.salvando,
                novaPessoa = viewModel.uiState.novaPessoa,
                onSalvar = viewModel::salvar,
                onVoltar = onVoltar
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
            Formulario(
                modifier = Modifier.padding(innerPadding),
                idPessoa = viewModel.uiState.idPessoa,
                formState = viewModel.uiState.formState,
                onNomeAlterado = viewModel::onNomeAlterado,
                onCpfAlterado = viewModel::onCpfAlterado,
                onTelefoneAlterado = viewModel::onTelefoneAlterado,
                onCepAlterado = viewModel::onCepAlterado,
                onNumeroAlterado = viewModel::onNumeroAlterado,
                onComplementoAlterado = viewModel::onComplementoAlterado
            )
        }
    }
}

@Composable
fun FormPessoaTopBar(
    modifier: Modifier = Modifier,
    mostrarAcoes: Boolean,
    novaPessoa: Boolean,
    salvando: Boolean,
    onSalvar: () -> Unit,
    onVoltar: () -> Unit
) {
    AppBarPadrao(
        modifier = modifier,
        titulo = if (novaPessoa) {
            stringResource(Res.string.nova_pessoa)
        } else {
            stringResource(Res.string.editar_pessoa)
        },
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
                if (salvando) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(60.dp)
                            .padding(all = 16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(onClick = onSalvar) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(Res.string.salvar)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun Formulario(
    modifier: Modifier = Modifier,
    idPessoa: Int,
    formState: FormState,
    onNomeAlterado: (String) -> Unit,
    onCpfAlterado: (String) -> Unit,
    onTelefoneAlterado: (String) -> Unit,
    onCepAlterado: (String) -> Unit,
    onNumeroAlterado: (String) -> Unit,
    onComplementoAlterado: (String) -> Unit
) {
   Column(
       modifier = modifier
           .fillMaxSize()
           .padding(vertical = 16.dp)
           .verticalScroll(rememberScrollState())
   ) {
       IdPessoa(idPessoa = idPessoa)
       FormTextField(
           label = stringResource(Res.string.nome),
           value = formState.nome.value,
           onValueChanged = onNomeAlterado,
           errorStringResource = formState.nome.errorStringResource,
           keyboardCapitalization = KeyboardCapitalization.Words
       )
       FormTextField(
           label = stringResource(Res.string.cpf),
           value = formState.cpf.value,
           onValueChanged = onCpfAlterado,
           errorStringResource = formState.cpf.errorStringResource,
           keyboardType = KeyboardType.Number,
           visualTransformation = CpfVisualTransformation()
       )
       FormTextField(
           label = stringResource(Res.string.telefone),
           value = formState.telefone.value,
           onValueChanged = onTelefoneAlterado,
           errorStringResource = formState.telefone.errorStringResource,
           keyboardType = KeyboardType.Phone,
           visualTransformation = TelefoneVisualTransformation()
       )
       Divider(modifier = Modifier.padding(vertical = 16.dp))
       FormTitle(text = stringResource(Res.string.endereco))
       FormTextField(
           label = stringResource(Res.string.cep),
           value = formState.cep.value,
           onValueChanged = onCepAlterado,
           errorStringResource = formState.cep.errorStringResource,
           keyboardType = KeyboardType.Number,
           visualTransformation = CepVisualTransformation(),
           enabled = !formState.buscandoCep,
           trailingIcon = {
               if (formState.buscandoCep) {
                   CircularProgressIndicator(
                       modifier = Modifier
                           .size(60.dp)
                           .padding(16.dp),
                       strokeWidth = 2.dp
                   )
               }
           }
       )
       FormTextField(
           label = stringResource(Res.string.logradouro),
           value = formState.logradouro.value,
           enabled = false,
           onValueChanged = {}
       )
       FormTextField(
           label = stringResource(Res.string.numero),
           value = formState.numero.value,
           onValueChanged = onNumeroAlterado,
           errorStringResource = formState.numero.errorStringResource,
           keyboardType = KeyboardType.Number
       )
       FormTextField(
           label = stringResource(Res.string.complemento),
           value = formState.complemento.value,
           onValueChanged = onComplementoAlterado
       )
       FormTextField(
           label = stringResource(Res.string.bairro),
           value = formState.bairro.value,
           enabled = false,
           onValueChanged = {}
       )
       FormTextField(
           label = stringResource(Res.string.cidade),
           value = formState.cidade.value,
           enabled = false,
           onValueChanged = {},
           keyboardImeAction = ImeAction.Done
       )
   }
}

@Composable
fun IdPessoa(
    modifier: Modifier = Modifier,
    idPessoa: Int
) {
    if (idPessoa > 0) {
        FormTitle(
            text = "${stringResource(Res.string.codigo)} - $idPessoa"
        )
    } else {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormTitle(
                text = "${stringResource(Res.string.codigo)} - "
            )
            Text(
                text = stringResource(Res.string.a_definir),
                color = MaterialTheme.colorScheme.primaryContainer,
                style = MaterialTheme.typography.titleSmall,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun FormTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(start = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    enabled: Boolean = true,
    errorStringResource: StringResource? = null,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.Sentences,
    keyboardImeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onValueChange = onValueChanged,
        label = { Text(label) },
        maxLines = 1,
        enabled = enabled,
        isError = errorStringResource != null,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            capitalization = keyboardCapitalization,
            imeAction = keyboardImeAction,
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation
    )
    errorStringResource?.let {
        Text(
            text = stringResource(errorStringResource),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}