package org.example.quiz

import android.app.Application
import org.example.quiz.data.dataModule
import org.example.quiz.data.db.androidDatabaseModule
import org.example.quiz.data.db.databaseModule
import org.example.quiz.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuizApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuizApplication)
            modules(dataModule, databaseModule, androidDatabaseModule, presentationModule)
        }
    }
}