package org.example.quiz.data

import org.example.quiz.data.db.QuizDatabase
import org.example.quiz.domain.HighScore

class HighScoreRepository(
    private val database: QuizDatabase
) {
    fun getHighScore(): HighScore? {
        return database.quizDatabaseQueries
            .selectHighScore()
            .executeAsOneOrNull()
            ?.let { entity ->
                HighScore(
                    bestScore = entity.bestScore.toInt(),
                    totalQuestions = entity.totalQuestions.toInt()
                )
            }
    }

    fun saveHighScoreIfBetter(newScore: Int, totalQuestions: Int) {
        val current = getHighScore()
        if (current == null || newScore > current.bestScore) {
            database.quizDatabaseQueries.insertOrReplaceHighScore(
                bestScore = newScore.toLong(),
                totalQuestions = totalQuestions.toLong()
            )
        }
    }
}