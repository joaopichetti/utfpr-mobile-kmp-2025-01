package br.edu.utfpr.cadastropessoas.ui.form.visualtransformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import br.edu.utfpr.cadastropessoas.utils.extensions.formatarCpf

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val cpfFormatado = text.text.formatarCpf()

        return TransformedText(
            AnnotatedString(cpfFormatado),
            CpfOffsetMapping
        )
    }

    object CpfOffsetMapping : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return when {
                offset > 9 -> offset + 3
                offset > 6 -> offset + 2
                offset > 3 -> offset + 1
                else -> offset
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return when {
                offset > 9 -> offset - 3
                offset > 6 -> offset - 2
                offset > 3 -> offset - 1
                else -> offset
            }
        }
    }
}