package it.uninsubria.patientkiosk.viewmodel

import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.scoring.ScoreResult

sealed class KioskUiState {
    data object PatientIdentification : KioskUiState()

    data class QuestionnaireSelection(
        val patientCode: String,
        val questionnaires: List<Questionnaire>
    ) : KioskUiState()

    data class QuestionForm(
        val patientCode: String,
        val questionnaire: Questionnaire,
        val currentIndex: Int,
        val selectedAnswerScore: Int?
    ) : KioskUiState()

    data class Result(
        val patientCode: String,
        val questionnaire: Questionnaire,
        val result: ScoreResult
    ) : KioskUiState()

    data class Error(
        val message: String
    ) : KioskUiState()
}
