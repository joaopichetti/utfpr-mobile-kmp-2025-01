package br.edu.utfpr.cadastropessoas.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cadastropessoas.composeapp.generated.resources.Res
import cadastropessoas.composeapp.generated.resources.aguarde_um_momento_e_tente_novamente
import cadastropessoas.composeapp.generated.resources.erro_ao_carregar
import cadastropessoas.composeapp.generated.resources.tentar_novamente
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErroCarregar(
    modifier: Modifier = Modifier,
    texto: String,
    onTentarNovamente: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(Res.string.erro_ao_carregar),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
            text = texto,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
            text = stringResource(Res.string.aguarde_um_momento_e_tente_novamente),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        ElevatedButton(
            onClick = onTentarNovamente,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(Res.string.tentar_novamente))
        }
    }
}