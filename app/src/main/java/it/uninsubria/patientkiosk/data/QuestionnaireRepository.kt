package it.uninsubria.patientkiosk.data

import android.content.Context
import it.uninsubria.patientkiosk.model.AnswerOption
import it.uninsubria.patientkiosk.model.Question
import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.model.ScoreBand
import it.uninsubria.patientkiosk.model.ScoringType
import org.json.JSONArray
import org.json.JSONObject

class QuestionnaireRepository(private val context: Context) {

    fun loadQuestionnaires(): List<Questionnaire> {
        val json = context.assets.open(FILE_NAME).bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val questionnaires = root.getJSONArray("questionnaires")
        return questionnaires.toList { parseQuestionnaire(it) }
    }

    private fun parseQuestionnaire(json: JSONObject): Questionnaire {
        val questions = json.getJSONArray("questions").toList { parseQuestion(it) }
        val interpretations = json.getJSONArray("interpretations").toList { parseBand(it) }

        require(questions.isNotEmpty()) { "Questionario senza domande: ${json.optString("id")}" }

        return Questionnaire(
            id = json.getString("id"),
            title = json.getString("title"),
            description = json.getString("description"),
            source = json.getString("source"),
            scoringType = ScoringType.fromJson(json.getString("scoringType")),
            maxScore = json.getInt("maxScore"),
            questions = questions,
            interpretations = interpretations
        )
    }

    private fun parseQuestion(json: JSONObject): Question {
        val answers = json.getJSONArray("answers").toList { parseAnswer(it) }
        require(answers.isNotEmpty()) { "Domanda senza risposte: ${json.optString("id")}" }
        return Question(
            id = json.getString("id"),
            text = json.getString("text"),
            category = json.optString("category").takeIf { it.isNotBlank() },
            answers = answers
        )
    }

    private fun parseAnswer(json: JSONObject): AnswerOption = AnswerOption(
        id = json.getString("id"),
        label = json.getString("label"),
        score = json.getInt("score")
    )

    private fun parseBand(json: JSONObject): ScoreBand = ScoreBand(
        category = json.optString("category").takeIf { it.isNotBlank() },
        min = json.getInt("min"),
        max = json.getInt("max"),
        label = json.getString("label"),
        description = json.getString("description")
    )

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
