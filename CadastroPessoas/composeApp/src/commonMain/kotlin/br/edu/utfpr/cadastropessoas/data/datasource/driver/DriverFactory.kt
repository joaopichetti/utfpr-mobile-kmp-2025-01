package br.edu.utfpr.cadastropessoas.data.datasource.driver

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver(): SqlDriver
}