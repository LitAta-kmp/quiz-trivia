package org.example.quiz.presentation

import org.example.quiz.domain.Question
import kotlin.test.Test
import kotlin.test.assertEquals

class QuizReducerTest {

    private val sampleQuestion = Question(
        text = "What is the capital of France?",
        answers = listOf("Berlin", "Paris", "Madrid", "Rome"),
        correctAnswerIndex = 1
    )

    @Test
    fun `correct_answer_increases_score`() {
        val initialState = QuizState.Playing(
            currentQuestion = sampleQuestion,
            questionNumber = 1,
            totalQuestions = 5,
            timeLeftSeconds = 15,
            score = 0
        )
        val nextQuestion = sampleQuestion.copy(text = "Second question")

        val result = QuizReducer.reduce(
            initialState,
            QuizIntent.AnswerSelected(answerIndex = 1, nextQuestion = nextQuestion)
        )

        assertEquals(true, result is QuizState.Playing)
        assertEquals(1, (result as QuizState.Playing).score)
    }

    @Test
    fun `wrong_answer_does_not_increase_score`() {
        val initialState = QuizState.Playing(
            currentQuestion = sampleQuestion,
            questionNumber = 1,
            totalQuestions = 5,
            timeLeftSeconds = 15,
            score = 3
        )
        val nextQuestion = sampleQuestion.copy(text = "Second question")

        val result = QuizReducer.reduce(
            initialState,
            QuizIntent.AnswerSelected(answerIndex = 0, nextQuestion = nextQuestion)
        )

        assertEquals(3, (result as QuizState.Playing).score)
    }

    @Test
    fun `last_question_with_no_next_question_finishes_the_quiz`() {
        val initialState = QuizState.Playing(
            currentQuestion = sampleQuestion,
            questionNumber = 5,
            totalQuestions = 5,
            timeLeftSeconds = 15,
            score = 4
        )

        val result = QuizReducer.reduce(
            initialState,
            QuizIntent.AnswerSelected(answerIndex = 1, nextQuestion = null)
        )

        assertEquals(true, result is QuizState.Finished)
        assertEquals(5, (result as QuizState.Finished).finalScore)
    }
}