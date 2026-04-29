package it.uninsubria.patientkiosk.scoring

import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.model.ScoreBand
import it.uninsubria.patientkiosk.model.ScoringType

object ScoreCalculator {

    fun calculate(questionnaire: Questionnaire, answers: Map<String, Int>): ScoreResult {
        validateQuestionnaireBeforeCalculation(questionnaire)
        validateAllQuestionsAnswered(questionnaire, answers)
        validateAnswerScores(questionnaire, answers)

        return when (questionnaire.scoringType) {
            ScoringType.SUM -> calculateSum(questionnaire, answers)
            ScoringType.WHO5_PERCENTAGE -> calculateWho5(questionnaire, answers)
            ScoringType.HADS_SUBSCALES -> calculateHads(questionnaire, answers)
        }
    }

    private fun validateQuestionnaireBeforeCalculation(questionnaire: Questionnaire) {
        require(questionnaire.questions.isNotEmpty()) {
            "Impossibile calcolare lo score: il questionario non contiene domande."
        }

        require(questionnaire.maxScore > 0) {
            "Impossibile calcolare lo score: punteggio massimo non valido."
        }

        val expectedMaxScore = questionnaire.expectedMaximumScore()
        require(questionnaire.maxScore == expectedMaxScore) {
            "Configurazione punteggio non coerente: massimo dichiarato ${questionnaire.maxScore}, massimo calcolabile $expectedMaxScore."
        }
    }

    private fun validateAllQuestionsAnswered(
        questionnaire: Questionnaire,
        answers: Map<String, Int>
    ) {
        val missing = questionnaire.questions.filterNot { question ->
            answers.containsKey(question.id)
        }

        require(missing.isEmpty()) {
            "Compilazione incompleta: mancano ${missing.size} risposte."
        }
    }

    private fun validateAnswerScores(
        questionnaire: Questionnaire,
        answers: Map<String, Int>
    ) {
        questionnaire.questions.forEach { question ->
            val selectedScore = answers[question.id]
            val availableScores = question.availableScores()

            require(selectedScore in availableScores) {
                "Risposta non valida per la domanda ${question.id}."
            }
        }
    }

    private fun calculateSum(
        questionnaire: Questionnaire,
        answers: Map<String, Int>
    ): ScoreResult {
        val total = questionnaire.questions.sumOf { question ->
            answers.getValue(question.id)
        }

        validateScoreRange(total, questionnaire.maxScore)

        val band = findBand(
            bands = questionnaire.interpretations,
            category = null,
            score = total
        )

        return ScoreResult(
            mainScore = total,
            maxScore = questionnaire.maxScore,
            label = band.label,
            description = band.description,
            details = listOf(
                "Formula applicata: somma dei punteggi delle risposte.",
                "Domande compilate: ${questionnaire.questionCount}.",
                "Punteggio massimo previsto: ${questionnaire.maxScore}."
            )
        )
    }

    private fun calculateWho5(
        questionnaire: Questionnaire,
        answers: Map<String, Int>
    ): ScoreResult {
        val rawScore = questionnaire.questions.sumOf { question ->
            answers.getValue(question.id)
        }

        val percentageScore = rawScore * WHO5_MULTIPLIER

        validateScoreRange(percentageScore, questionnaire.maxScore)

        val band = findBand(
            bands = questionnaire.interpretations,
            category = null,
            score = percentageScore
        )

        return ScoreResult(
            mainScore = percentageScore,
            maxScore = questionnaire.maxScore,
            label = band.label,
            description = band.description,
            details = listOf(
                "Punteggio grezzo: $rawScore / 25.",
                "Formula applicata: punteggio grezzo × 4.",
                "Punteggio percentuale: $percentageScore / 100."
            )
        )
    }

    private fun calculateHads(
        questionnaire: Questionnaire,
        answers: Map<String, Int>
    ): ScoreResult {
        validateHadsCategories(questionnaire)

        val anxiety = questionnaire.questions
            .filter { question -> question.category == HADS_ANXIETY_CATEGORY }
            .sumOf { question -> answers.getValue(question.id) }

        val depression = questionnaire.questions
            .filter { question -> question.category == HADS_DEPRESSION_CATEGORY }
            .sumOf { question -> answers.getValue(question.id) }

        val total = anxiety + depression

        validateScoreRange(total, questionnaire.maxScore)

        val anxietyBand = findBand(
            bands = questionnaire.interpretations,
            category = HADS_ANXIETY_CATEGORY,
            score = anxiety
        )

        val depressionBand = findBand(
            bands = questionnaire.interpretations,
            category = HADS_DEPRESSION_CATEGORY,
            score = depression
        )

        return ScoreResult(
            mainScore = total,
            maxScore = questionnaire.maxScore,
            label = "Ansia: ${anxietyBand.label} · Depressione: ${depressionBand.label}",
            description = "Risultato di screening con sottoscale separate. Non rappresenta una diagnosi clinica.",
            details = listOf(
                "HADS-A: $anxiety / 21 — ${anxietyBand.description}",
                "HADS-D: $depression / 21 — ${depressionBand.description}",
                "Totale HADS: $total / 42.",
                "Formula applicata: somma separata delle risposte per categoria A e D."
            )
        )
    }

    private fun validateHadsCategories(questionnaire: Questionnaire) {
        val invalidQuestions = questionnaire.questions.filter { question ->
            question.category != HADS_ANXIETY_CATEGORY &&
                question.category != HADS_DEPRESSION_CATEGORY
        }

        require(invalidQuestions.isEmpty()) {
            "Configurazione HADS non valida: alcune domande non hanno categoria A o D."
        }
    }

    private fun validateScoreRange(score: Int, maxScore: Int) {
        require(score in 0..maxScore) {
            "Punteggio calcolato fuori intervallo: $score / $maxScore."
        }
    }

    private fun findBand(
        bands: List<ScoreBand>,
        category: String?,
        score: Int
    ): ScoreBand {
        return bands.firstOrNull { band ->
            band.category == category && band.contains(score)
        } ?: ScoreBand(
            category = category,
            min = score,
            max = score,
            label = "Interpretazione non disponibile",
            description = "Nessuna fascia configurata per il punteggio $score."
        )
    }

    private const val WHO5_MULTIPLIER = 4
    private const val HADS_ANXIETY_CATEGORY = "A"
    private const val HADS_DEPRESSION_CATEGORY = "D"
}