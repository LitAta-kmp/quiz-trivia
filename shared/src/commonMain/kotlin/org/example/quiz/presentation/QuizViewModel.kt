package org.example.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.quiz.data.HighScoreRepository
import org.example.quiz.data.QuizRepository
import org.example.quiz.domain.Question
import org.example.quiz.domain.QuizRules

// presentation layer
class QuizViewModel(
    private val repository: QuizRepository,
    private val highScoreRepository: HighScoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow<QuizState>(QuizState.Idle)
    val state: StateFlow<QuizState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var questions: List<Question> = emptyList()
    private var currentQuestionIndex = 0

    fun onIntent(intent: QuizIntent) {
        when (intent) {
            is QuizIntent.StartQuiz -> loadQuestions()
            is QuizIntent.Restart -> loadQuestions()
            is QuizIntent.AnswerSelected -> handleAnswer(intent.answerIndex)
            is QuizIntent.TimerTick -> handleTick()
            is QuizIntent.QuestionsLoaded -> Unit // сюда ViewModel сама не шлёт, см. loadQuestions()
            is QuizIntent.QuestionsLoadFailed -> Unit
            is QuizIntent.HighScoreLoaded -> Unit
        }
    }
    fun onStartQuiz() {
        onIntent(QuizIntent.StartQuiz)
    }

    fun onAnswerSelected(answerIndex: Int) {
        handleAnswer(answerIndex)
    }

    fun onRestart() {
        onIntent(QuizIntent.Restart)
    }

    private fun loadQuestions() {
        timerJob?.cancel()
        _state.value = QuizState.Loading

        viewModelScope.launch {
            repository.getQuestions(amount = QuizRules.QUESTIONS_PER_ROUND)
                .onSuccess { loaded ->
                    questions = loaded
                    currentQuestionIndex = 0
                    _state.value = QuizReducer.reduce(
                        _state.value,
                        QuizIntent.QuestionsLoaded(
                            firstQuestion = loaded.first(),
                            totalQuestions = loaded.size
                        )
                    )
                    startTimer()
                }
                .onFailure { error ->
                    _state.value = QuizReducer.reduce(
                        _state.value,
                        QuizIntent.QuestionsLoadFailed(
                            message = error.message ?: "Unknown error"
                        )
                    )
                }
        }
    }

    private fun handleAnswer(answerIndex: Int) {
        val current = _state.value
        if (current !is QuizState.Playing) return

        timerJob?.cancel()
        currentQuestionIndex++
        val nextQuestion = questions.getOrNull(currentQuestionIndex)

        val newState = QuizReducer.reduce(
            current,
            QuizIntent.AnswerSelected(answerIndex, nextQuestion)
        )
        _state.value = newState

        if (newState is QuizState.Finished) {
            saveAndLoadHighScore(newState.finalScore, newState.totalQuestions)   // ← новое
        } else {
            startTimer()
        }
    }

    private fun saveAndLoadHighScore(finalScore: Int, totalQuestions: Int) {
        viewModelScope.launch {
            val best = withContext(Dispatchers.IO) {
                highScoreRepository.saveHighScoreIfBetter(finalScore, totalQuestions)
                highScoreRepository.getHighScore()
            }
            _state.value = QuizReducer.reduce(
                _state.value,
                QuizIntent.HighScoreLoaded(bestScore = best?.bestScore ?: finalScore)
            )
        }
    }

    private fun handleTick() {
        val current = _state.value
        if (current !is QuizState.Playing) return

        if (current.timeLeftSeconds <= 1) {
            handleAnswer(answerIndex = -1) // время вышло = засчитать как неверный ответ
        } else {
            _state.value = QuizReducer.reduce(current, QuizIntent.TimerTick)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                onIntent(QuizIntent.TimerTick)
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}