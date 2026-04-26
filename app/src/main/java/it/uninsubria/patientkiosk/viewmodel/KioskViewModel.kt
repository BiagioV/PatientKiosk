package it.uninsubria.patientkiosk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.scoring.ScoreCalculator
import it.uninsubria.patientkiosk.validation.PatientCodeValidator

class KioskViewModel : ViewModel() {

    private val _state = MutableLiveData<KioskUiState>(KioskUiState.PatientIdentification)
    val state: LiveData<KioskUiState> = _state

    private var questionnaires: List<Questionnaire> = emptyList()
    private var patientCode: String = ""
    private var selectedQuestionnaire: Questionnaire? = null
    private var currentQuestionIndex: Int = 0
    private val answers: MutableMap<String, Int> = mutableMapOf()

    fun setQuestionnaires(items: List<Questionnaire>) {
        questionnaires = items
    }

    fun submitPatientCode(rawCode: String) {
        val validationResult = PatientCodeValidator.validate(rawCode)

        if (!validationResult.isValid) {
            _state.value = KioskUiState.Error(
                validationResult.errorMessage ?: "Codice paziente non valido."
            )
            return
        }

        patientCode = validationResult.normalizedCode
        selectedQuestionnaire = null
        currentQuestionIndex = 0
        answers.clear()

        _state.value = KioskUiState.QuestionnaireSelection(
            patientCode = patientCode,
            questionnaires = questionnaires
        )
    }

    fun selectQuestionnaire(questionnaire: Questionnaire) {
        selectedQuestionnaire = questionnaire
        currentQuestionIndex = 0
        answers.clear()
        emitQuestionForm()
    }

    fun saveCurrentAnswer(score: Int?) {
        val questionnaire = selectedQuestionnaire ?: return
        val question = questionnaire.questions[currentQuestionIndex]

        if (score == null) {
            answers.remove(question.id)
        } else {
            answers[question.id] = score
        }
    }

    fun nextQuestion() {
        val questionnaire = selectedQuestionnaire ?: return
        val question = questionnaire.questions[currentQuestionIndex]

        if (!answers.containsKey(question.id)) {
            _state.value = KioskUiState.Error("Seleziona una risposta prima di proseguire.")
            return
        }

        if (currentQuestionIndex < questionnaire.questions.lastIndex) {
            currentQuestionIndex++
            emitQuestionForm()
        } else {
            runCatching {
                ScoreCalculator.calculate(questionnaire, answers)
            }.onSuccess { result ->
                _state.value = KioskUiState.Result(
                    patientCode = patientCode,
                    questionnaire = questionnaire,
                    result = result
                )
            }.onFailure { error ->
                _state.value = KioskUiState.Error(
                    error.message ?: "Errore nel calcolo del risultato."
                )
            }
        }
    }

    fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            emitQuestionForm()
        }
    }

    fun backToSelection() {
        selectedQuestionnaire = null
        currentQuestionIndex = 0
        answers.clear()

        _state.value = KioskUiState.QuestionnaireSelection(
            patientCode = patientCode,
            questionnaires = questionnaires
        )
    }

    fun restart() {
        patientCode = ""
        selectedQuestionnaire = null
        currentQuestionIndex = 0
        answers.clear()

        _state.value = KioskUiState.PatientIdentification
    }

    fun clearErrorAndRefresh() {
        val questionnaire = selectedQuestionnaire

        when {
            questionnaire != null -> emitQuestionForm()
            patientCode.isNotBlank() -> {
                _state.value = KioskUiState.QuestionnaireSelection(
                    patientCode = patientCode,
                    questionnaires = questionnaires
                )
            }
            else -> _state.value = KioskUiState.PatientIdentification
        }
    }

    private fun emitQuestionForm() {
        val questionnaire = selectedQuestionnaire ?: return
        val currentQuestion = questionnaire.questions[currentQuestionIndex]

        _state.value = KioskUiState.QuestionForm(
            patientCode = patientCode,
            questionnaire = questionnaire,
            currentIndex = currentQuestionIndex,
            selectedAnswerScore = answers[currentQuestion.id]
        )
    }
}