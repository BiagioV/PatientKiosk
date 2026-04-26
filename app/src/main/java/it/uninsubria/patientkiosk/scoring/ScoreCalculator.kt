package it.uninsubria.patientkiosk.scoring

import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.model.ScoreBand
import it.uninsubria.patientkiosk.model.ScoringType

object ScoreCalculator {

    fun calculate(questionnaire: Questionnaire, answers: Map<String, Int>): ScoreResult {
        validateAllQuestionsAnswered(questionnaire, answers)

        return when (questionnaire.scoringType) {
            ScoringType.SUM -> calculateSum(questionnaire, answers)
            ScoringType.WHO5_PERCENTAGE -> calculateWho5(questionnaire, answers)
            ScoringType.HADS_SUBSCALES -> calculateHads(questionnaire, answers)
        }
    }

    private fun validateAllQuestionsAnswered(questionnaire: Questionnaire, answers: Map<String, Int>) {
        val missing = questionnaire.questions.filterNot { answers.containsKey(it.id) }
        require(missing.isEmpty()) {
            "Compilazione incompleta: mancano ${missing.size} risposte."
        }
    }

    private fun calculateSum(questionnaire: Questionnaire, answers: Map<String, Int>): ScoreResult {
        val total = questionnaire.questions.sumOf { question -> answers[question.id] ?: 0 }
        val band = findBand(questionnaire.interpretations, null, total)
        return ScoreResult(
            mainScore = total,
            maxScore = questionnaire.maxScore,
            label = band.label,
            description = band.description,
            details = listOf("Formula: somma dei punteggi delle risposte.")
        )
    }

    private fun calculateWho5(questionnaire: Questionnaire, answers: Map<String, Int>): ScoreResult {
        val rawScore = questionnaire.questions.sumOf { question -> answers[question.id] ?: 0 }
        val percentageScore = rawScore * 4
        val band = findBand(questionnaire.interpretations, null, percentageScore)
        return ScoreResult(
            mainScore = percentageScore,
            maxScore = questionnaire.maxScore,
            label = band.label,
            description = band.description,
            details = listOf(
                "Punteggio grezzo: $rawScore / 25",
                "Formula: punteggio grezzo × 4 = punteggio percentuale."
            )
        )
    }

    private fun calculateHads(questionnaire: Questionnaire, answers: Map<String, Int>): ScoreResult {
        val anxiety = questionnaire.questions
            .filter { it.category == "A" }
            .sumOf { answers[it.id] ?: 0 }
        val depression = questionnaire.questions
            .filter { it.category == "D" }
            .sumOf { answers[it.id] ?: 0 }
        val total = anxiety + depression

        val anxietyBand = findBand(questionnaire.interpretations, "A", anxiety)
        val depressionBand = findBand(questionnaire.interpretations, "D", depression)

        return ScoreResult(
            mainScore = total,
            maxScore = questionnaire.maxScore,
            label = "Ansia: ${anxietyBand.label} · Depressione: ${depressionBand.label}",
            description = "Risultato di screening, non diagnosi clinica.",
            details = listOf(
                "HADS-A: $anxiety / 21 — ${anxietyBand.description}",
                "HADS-D: $depression / 21 — ${depressionBand.description}",
                "Totale HADS: $total / 42"
            )
        )
    }

    private fun findBand(bands: List<ScoreBand>, category: String?, score: Int): ScoreBand {
        return bands.firstOrNull { band ->
            band.category == category && score in band.min..band.max
        } ?: ScoreBand(
            category = category,
            min = score,
            max = score,
            label = "Interpretazione non disponibile",
            description = "Nessuna fascia configurata per il punteggio $score."
        )
    }
}
