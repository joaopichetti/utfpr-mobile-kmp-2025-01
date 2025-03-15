package br.edu.utfpr.cadastropessoas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform