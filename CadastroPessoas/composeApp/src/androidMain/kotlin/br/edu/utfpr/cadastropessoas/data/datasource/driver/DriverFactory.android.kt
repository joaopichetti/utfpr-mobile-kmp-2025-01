package br.edu.utfpr.cadastropessoas.data.datasource.driver

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.edu.utfpr.cadastropessoas.data.datasource.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope

class AndroidDriverFactory(private val context: Context) : DriverFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema,
            context,
            "app.db"
        )
    }
}

actual fun getDriverFactory(scope: Scope): DriverFactory =
    AndroidDriverFactory(scope.androidContext())