package br.edu.utfpr.cadastropessoas.utils.extensions

fun String.formatarCep(): String = mapIndexed { index, char ->
    when (index) {
        5 -> "-$char"
        else -> char
    }
}.joinToString("")

fun String.formatarCpf(): String = mapIndexed { index, char ->
    when (index) {
        3, 6 -> ".$char"
        9 -> "-$char"
        else -> char
    }
}.joinToString("")

fun String.formatarTelefone(): String = mapIndexed { index, char ->
    when {
        index == 0 -> "($char"
        index == 2 -> ") $char"
        (index == 6 && length < 11) ||
                (index == 7 && length == 11) -> "-$char"
        else -> char
    }
}.joinToString("")