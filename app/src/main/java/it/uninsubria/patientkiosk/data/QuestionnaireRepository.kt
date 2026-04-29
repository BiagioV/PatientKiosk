package it.uninsubria.patientkiosk.data

import android.content.Context
import it.uninsubria.patientkiosk.model.AnswerOption
import it.uninsubria.patientkiosk.model.Question
import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.model.ScoreBand
import it.uninsubria.patientkiosk.model.ScoringType
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class QuestionnaireRepository(private val context: Context) {

    fun loadQuestionnaires(): List<Questionnaire> {
        return try {
            val json = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }
            val root = JSONObject(json)

            if (!root.has("questionnaires")) {
                error("Campo 'questionnaires' mancante nel file JSON.")
            }

            val questionnaires = root.getJSONArray("questionnaires")
                .toList { parseQuestionnaire(it) }

            validateQuestionnaireCollection(questionnaires)

            questionnaires
        } catch (exception: JSONException) {
            throw IllegalStateException(
                "Il file dei questionari non è un JSON valido. Dettaglio: ${exception.message}",
                exception
            )
        } catch (exception: Exception) {
            throw IllegalStateException(
                "Impossibile caricare i questionari. Dettaglio: ${exception.message}",
                exception
            )
        }
    }

    private fun parseQuestionnaire(json: JSONObject): Questionnaire {
        val id = json.requiredString("id", "questionario")
        val title = json.requiredString("title", "questionario $id")
        val description = json.requiredString("description", "questionario $id")
        val source = json.requiredString("source", "questionario $id")
        val scoringType = ScoringType.fromJson(
            json.requiredString("scoringType", "questionario $id")
        )
        val maxScore = json.requiredInt("maxScore", "questionario $id")

        val questions = json.requiredArray("questions", "questionario $id")
            .toList { parseQuestion(it, id) }

        val interpretations = json.requiredArray("interpretations", "questionario $id")
            .toList { parseBand(it, id) }

        val questionnaire = Questionnaire(
            id = id,
            title = title,
            description = description,
            source = source,
            scoringType = scoringType,
            maxScore = maxScore,
            questions = questions,
            interpretations = interpretations
        )

        validateQuestionnaire(questionnaire)

        return questionnaire
    }

    private fun parseQuestion(json: JSONObject, questionnaireId: String): Question {
        val id = json.requiredString("id", "domanda del questionario $questionnaireId")
        val text = json.requiredString("text", "domanda $id")
        val category = json.optString("category").takeIf { it.isNotBlank() }

        val answers = json.requiredArray("answers", "domanda $id")
            .toList { parseAnswer(it, id) }

        val question = Question(
            id = id,
            text = text,
            category = category,
            answers = answers
        )

        validateQuestion(question, questionnaireId)

        return question
    }

    private fun parseAnswer(json: JSONObject, questionId: String): AnswerOption {
        val id = json.requiredString("id", "risposta della domanda $questionId")
        val label = json.requiredString("label", "risposta $id della domanda $questionId")
        val score = json.requiredInt("score", "risposta $id della domanda $questionId")

        return AnswerOption(
            id = id,
            label = label,
            score = score
        )
    }

    private fun parseBand(json: JSONObject, questionnaireId: String): ScoreBand {
        val category = json.optString("category").takeIf { it.isNotBlank() }

        return ScoreBand(
            category = category,
            min = json.requiredInt("min", "interpretazione del questionario $questionnaireId"),
            max = json.requiredInt("max", "interpretazione del questionario $questionnaireId"),
            label = json.requiredString("label", "interpretazione del questionario $questionnaireId"),
            description = json.requiredString("description", "interpretazione del questionario $questionnaireId")
        )
    }

    private fun validateQuestionnaireCollection(questionnaires: List<Questionnaire>) {
        require(questionnaires.isNotEmpty()) {
            "Il file JSON non contiene questionari."
        }

        val duplicatedIds = questionnaires
            .groupBy { it.id }
            .filterValues { it.size > 1 }
            .keys

        require(duplicatedIds.isEmpty()) {
            "Sono presenti questionari duplicati: ${duplicatedIds.joinToString()}."
        }
    }

    private fun validateQuestionnaire(questionnaire: Questionnaire) {
        require(questionnaire.id.isNotBlank()) {
            "Questionario con id vuoto."
        }

        require(questionnaire.title.isNotBlank()) {
            "Questionario ${questionnaire.id} senza titolo."
        }

        require(questionnaire.description.isNotBlank()) {
            "Questionario ${questionnaire.id} senza descrizione."
        }

        require(questionnaire.maxScore > 0) {
            "Questionario ${questionnaire.id} con punteggio massimo non valido."
        }

        require(questionnaire.questions.isNotEmpty()) {
            "Questionario ${questionnaire.id} senza domande."
        }

        require(questionnaire.interpretations.isNotEmpty()) {
            "Questionario ${questionnaire.id} senza fasce di interpretazione."
        }

        val duplicatedQuestionIds = questionnaire.questions
            .groupBy { it.id }
            .filterValues { it.size > 1 }
            .keys

        require(duplicatedQuestionIds.isEmpty()) {
            "Questionario ${questionnaire.id} con domande duplicate: ${duplicatedQuestionIds.joinToString()}."
        }

        val expectedMaxScore = questionnaire.expectedMaximumScore()
        require(questionnaire.maxScore == expectedMaxScore) {
            "Questionario ${questionnaire.id}: maxScore=${questionnaire.maxScore}, ma il massimo calcolabile è $expectedMaxScore."
        }

        validateInterpretationBands(questionnaire)

        if (questionnaire.scoringType == ScoringType.HADS_SUBSCALES) {
            validateHadsStructure(questionnaire)
        }
    }

    private fun validateQuestion(question: Question, questionnaireId: String) {
        require(question.id.isNotBlank()) {
            "Questionario $questionnaireId con domanda senza id."
        }

        require(question.text.isNotBlank()) {
            "Domanda ${question.id} senza testo."
        }

        require(question.answers.isNotEmpty()) {
            "Domanda ${question.id} senza risposte."
        }

        val duplicatedAnswerIds = question.answers
            .groupBy { it.id }
            .filterValues { it.size > 1 }
            .keys

        require(duplicatedAnswerIds.isEmpty()) {
            "Domanda ${question.id} con risposte duplicate: ${duplicatedAnswerIds.joinToString()}."
        }

        question.answers.forEach { answer ->
            require(answer.label.isNotBlank()) {
                "Risposta ${answer.id} della domanda ${question.id} senza testo."
            }

            require(answer.score >= 0) {
                "Risposta ${answer.id} della domanda ${question.id} con punteggio negativo."
            }
        }
    }

    private fun validateInterpretationBands(questionnaire: Questionnaire) {
        questionnaire.interpretations.forEach { band ->
            require(band.min <= band.max) {
                "Questionario ${questionnaire.id} con fascia di interpretazione non valida: ${band.label}."
            }

            require(band.label.isNotBlank()) {
                "Questionario ${questionnaire.id} con fascia senza etichetta."
            }

            require(band.description.isNotBlank()) {
                "Questionario ${questionnaire.id} con fascia senza descrizione."
            }
        }
    }

    private fun validateHadsStructure(questionnaire: Questionnaire) {
        val invalidCategories = questionnaire.questions
            .mapNotNull { it.category }
            .filterNot { it == "A" || it == "D" }
            .toSet()

        require(invalidCategories.isEmpty()) {
            "Questionario ${questionnaire.id}: categorie HADS non valide: ${invalidCategories.joinToString()}."
        }

        require(questionnaire.questions.any { it.category == "A" }) {
            "Questionario ${questionnaire.id}: manca la sottoscala HADS-A."
        }

        require(questionnaire.questions.any { it.category == "D" }) {
            "Questionario ${questionnaire.id}: manca la sottoscala HADS-D."
        }

        require(questionnaire.interpretations.any { it.category == "A" }) {
            "Questionario ${questionnaire.id}: mancano interpretazioni per HADS-A."
        }

        require(questionnaire.interpretations.any { it.category == "D" }) {
            "Questionario ${questionnaire.id}: mancano interpretazioni per HADS-D."
        }
    }

    private fun JSONObject.requiredString(fieldName: String, contextName: String): String {
        require(has(fieldName)) {
            "Campo '$fieldName' mancante in $contextName."
        }

        val value = getString(fieldName).trim()

        require(value.isNotBlank()) {
            "Campo '$fieldName' vuoto in $contextName."
        }

        return value
    }

    private fun JSONObject.requiredInt(fieldName: String, contextName: String): Int {
        require(has(fieldName)) {
            "Campo '$fieldName' mancante in $contextName."
        }

        return getInt(fieldName)
    }

    private fun JSONObject.requiredArray(fieldName: String, contextName: String): JSONArray {
        require(has(fieldName)) {
            "Campo '$fieldName' mancante in $contextName."
        }

        return getJSONArray(fieldName)
    }

    private inline fun <T> JSONArray.toList(parser: (JSONObject) -> T): List<T> {
        val result = mutableListOf<T>()

        for (index in 0 until length()) {
            result += parser(getJSONObject(index))
        }

        return result
    }

    companion object {
        private const val FILE_NAME = "questionnaires.json"
    }
}