package br.edu.utfpr.cadastropessoas.ui.form.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarTelefone

class TelefoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val telefoneFormatado = text.text.formatarTelefone()

        return TransformedText(
            AnnotatedString(telefoneFormatado),
            TelefoneOffsetMapping
        )
    }

    object TelefoneOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 6 -> offset + 4
                offset > 2 -> offset + 3
                offset > 0 -> offset + 1
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 6 -> offset - 4
                offset > 2 -> offset - 3
                offset > 0 -> offset -1
                else -> offset
            }
        }
    }
}