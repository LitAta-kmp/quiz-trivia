package org.example.quiz.data.db

import org.example.quiz.data.HighScoreRepository
import org.koin.dsl.module

val databaseModule = module {
    single { QuizDatabase(driver = get()) }
    single { HighScoreRepository(database = get()) }
}