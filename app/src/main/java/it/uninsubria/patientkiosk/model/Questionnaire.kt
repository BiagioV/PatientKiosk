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
) {
    val questionCount: Int
        get() = questions.size

    val rawMaximumScore: Int
        get() = questions.sumOf { question ->
            question.answers.maxOfOrNull { answer -> answer.score } ?: 0
        }

    fun expectedMaximumScore(): Int {
        return when (scoringType) {
            ScoringType.SUM -> rawMaximumScore
            ScoringType.WHO5_PERCENTAGE -> rawMaximumScore * 4
            ScoringType.HADS_SUBSCALES -> rawMaximumScore
        }
    }
}

data class Question(
    val id: String,
    val text: String,
    val category: String?,
    val answers: List<AnswerOption>
) {
    fun availableScores(): Set<Int> = answers.map { it.score }.toSet()
}

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
) {
    fun contains(score: Int): Boolean = score in min..max
}

enum class ScoringType {
    SUM,
    WHO5_PERCENTAGE,
    HADS_SUBSCALES;

    companion object {
        fun fromJson(value: String): ScoringType {
            return when (value.trim().uppercase()) {
                "SUM" -> SUM
                "WHO5_PERCENTAGE" -> WHO5_PERCENTAGE
                "HADS_SUBSCALES" -> HADS_SUBSCALES
                else -> error("Tipo di scoring non supportato: $value")
            }
        }
    }
}