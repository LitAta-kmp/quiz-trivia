package org.example.quiz.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.quiz.domain.QuizRules
import org.example.quiz.presentation.QuizState

@Composable
fun PlayingScreen(
    state: QuizState.Playing,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Question ${state.questionNumber} / ${state.totalQuestions}")
        Text("Score: ${state.score}")
        Text("Time left: ${state.timeLeftSeconds}s")

        LinearProgressIndicator(
            progress = { state.timeLeftSeconds / QuizRules.SECONDS_PER_QUESTION.toFloat() },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = state.currentQuestion.text)

        state.currentQuestion.answers.forEachIndexed { index, answer ->
            AnswerButton(
                text = answer,
                onClick = { onAnswerSelected(index) }
            )
        }
    }
}

@Composable
private fun AnswerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text)
    }
}