package org.example.quiz.data.db

import app.cash.sqldelight.db.SqlDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidDatabaseModule = module {
    single<SqlDriver> { DatabaseDriverFactory(androidContext()).createDriver() }
}