package br.edu.utfpr.cadastropessoas.data.datasource.driver

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope

interface DriverFactory {
    fun createDriver(): SqlDriver
}

expect fun getDriverFactory(scope: Scope): DriverFactory