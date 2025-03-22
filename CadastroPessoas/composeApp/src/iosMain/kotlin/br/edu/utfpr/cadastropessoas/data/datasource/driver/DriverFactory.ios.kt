package br.edu.utfpr.cadastropessoas.data.datasource.driver

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import br.edu.utfpr.cadastropessoas.data.datasource.AppDatabase
import org.koin.core.scope.Scope

class IosDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "app.db")
    }
}

actual fun getDriverFactory(scope: Scope): DriverFactory = IosDriverFactory()