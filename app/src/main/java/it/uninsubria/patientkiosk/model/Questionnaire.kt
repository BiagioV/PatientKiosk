package it.uninsubria.patientkiosk.model

data class Questionnaire(
    val id: String,
    val title: String,
    val description: String,
    val source: String,
    val scoringType: ScoringType,
    val maxScore: Int,
    val questions: List<Question>,
    val interpretations: List<ScoreBand>
)

data class Question(
    val id: String,
    val text: String,
    val category: String?,
    val answers: List<AnswerOption>
)

data class AnswerOption(
    val id: String,
    val label: String,
    val score: Int
)

data class ScoreBand(
    val category: String?,
    val min: Int,
    val max: Int,
    val label: String,
    val description: String
)

enum class ScoringType {
    SUM,
    WHO5_PERCENTAGE,
    HADS_SUBSCALES;

    companion object {
        fun fromJson(value: String): ScoringType = when (value.trim().uppercase()) {
            "SUM" -> SUM
            "WHO5_PERCENTAGE" -> WHO5_PERCENTAGE
            "HADS_SUBSCALES" -> HADS_SUBSCALES
            else -> error("Tipo di scoring non supportato: $value")
        }
    }
}
