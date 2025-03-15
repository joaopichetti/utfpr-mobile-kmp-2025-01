package br.edu.utfpr.cadastropessoas.ui.form.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarCep

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val cepFormatado = text.text.formatarCep()

        return TransformedText(
            AnnotatedString(cepFormatado),
            CepOffsetMapping
        )
    }

    object CepOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 5 -> offset + 1
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 5 -> offset - 1
                else -> offset
            }
        }
    }
}