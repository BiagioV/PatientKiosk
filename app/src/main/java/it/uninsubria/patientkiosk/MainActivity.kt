package it.uninsubria.patientkiosk

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import it.uninsubria.patientkiosk.data.QuestionnaireRepository
import it.uninsubria.patientkiosk.model.AnswerOption
import it.uninsubria.patientkiosk.model.Questionnaire
import it.uninsubria.patientkiosk.ui.QuestionnaireAdapter
import it.uninsubria.patientkiosk.viewmodel.KioskUiState
import it.uninsubria.patientkiosk.viewmodel.KioskViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: KioskViewModel
    private lateinit var adapter: QuestionnaireAdapter

    private lateinit var errorText: TextView
    private lateinit var patientSection: LinearLayout
    private lateinit var selectionSection: LinearLayout
    private lateinit var questionSection: LinearLayout
    private lateinit var resultSection: LinearLayout

    private lateinit var patientCodeInput: EditText
    private lateinit var startButton: Button

    private lateinit var patientSummaryText: TextView
    private lateinit var questionnaireList: ListView

    private lateinit var questionnaireTitleText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var questionText: TextView
    private lateinit var answersGroup: RadioGroup
    private lateinit var answerHelpText: TextView
    private lateinit var backButton: Button
    private lateinit var nextButton: Button

    private lateinit var resultScoreText: TextView
    private lateinit var resultInterpretationText: TextView
    private lateinit var resultDetailsText: TextView
    private lateinit var changeQuestionnaireButton: Button
    private lateinit var restartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        viewModel = ViewModelProvider(this)[KioskViewModel::class.java]
        adapter = QuestionnaireAdapter(this, emptyList())
        questionnaireList.adapter = adapter

        loadQuestionnaires()
        configureListeners()
        observeState()
    }

    private fun bindViews() {
        errorText = findViewById(R.id.errorText)

        patientSection = findViewById(R.id.patientSection)
        selectionSection = findViewById(R.id.selectionSection)
        questionSection = findViewById(R.id.questionSection)
        resultSection = findViewById(R.id.resultSection)

        patientCodeInput = findViewById(R.id.patientCodeInput)
        startButton = findViewById(R.id.startButton)

        patientSummaryText = findViewById(R.id.patientSummaryText)
        questionnaireList = findViewById(R.id.questionnaireList)

        questionnaireTitleText = findViewById(R.id.questionnaireTitleText)
        progressBar = findViewById(R.id.progressBar)
        progressText = findViewById(R.id.progressText)
        questionText = findViewById(R.id.questionText)
        answersGroup = findViewById(R.id.answersGroup)
        answerHelpText = findViewById(R.id.answerHelpText)
        backButton = findViewById(R.id.backButton)
        nextButton = findViewById(R.id.nextButton)

        resultScoreText = findViewById(R.id.resultScoreText)
        resultInterpretationText = findViewById(R.id.resultInterpretationText)
        resultDetailsText = findViewById(R.id.resultDetailsText)
        changeQuestionnaireButton = findViewById(R.id.changeQuestionnaireButton)
        restartButton = findViewById(R.id.restartButton)
    }

    private fun loadQuestionnaires() {
        runCatching {
            QuestionnaireRepository(this).loadQuestionnaires()
        }.onSuccess { questionnaires ->
            viewModel.setQuestionnaires(questionnaires)
        }.onFailure { error ->
            showError("Errore nel caricamento dei questionari: ${error.message}")
        }
    }

    private fun configureListeners() {
        startButton.setOnClickListener {
            hideKeyboard()
            clearError()
            viewModel.submitPatientCode(patientCodeInput.text.toString())
        }

        questionnaireList.setOnItemClickListener { _, _, position, _ ->
            clearError()
            viewModel.selectQuestionnaire(adapter.getItem(position))
        }

        answersGroup.setOnCheckedChangeListener { _, _ ->
            clearError()
            answerHelpText.gone()
            updateNextButtonState()
        }

        backButton.setOnClickListener {
            clearError()
            saveSelectedAnswerIfPresent()
            viewModel.previousQuestion()
        }

        nextButton.setOnClickListener {
            clearError()

            val selectedScore = selectedAnswerScore()
            if (selectedScore == null) {
                answerHelpText.visible()
                showError(getString(R.string.answer_required_error))
                updateNextButtonState()
                return@setOnClickListener
            }

            viewModel.saveCurrentAnswer(selectedScore)
            viewModel.nextQuestion()
        }

        changeQuestionnaireButton.setOnClickListener {
            clearError()
            viewModel.backToSelection()
        }

        restartButton.setOnClickListener {
            clearError()
            patientCodeInput.text?.clear()
            viewModel.restart()
        }
    }

    private fun observeState() {
        viewModel.state.observe(this) { state ->
            when (state) {
                KioskUiState.PatientIdentification -> renderPatientIdentification()
                is KioskUiState.QuestionnaireSelection -> renderSelection(
                    patientCode = state.patientCode,
                    questionnaires = state.questionnaires
                )
                is KioskUiState.QuestionForm -> renderQuestion(state)
                is KioskUiState.Result -> renderResult(state)
                is KioskUiState.Error -> showError(state.message)
            }
        }
    }

    private fun renderPatientIdentification() {
        patientSection.visible()
        selectionSection.gone()
        questionSection.gone()
        resultSection.gone()
    }

    private fun renderSelection(patientCode: String, questionnaires: List<Questionnaire>) {
        patientSection.gone()
        selectionSection.visible()
        questionSection.gone()
        resultSection.gone()

        patientSummaryText.text = getString(R.string.patient_summary, patientCode)
        adapter.update(questionnaires)
    }

    private fun renderQuestion(state: KioskUiState.QuestionForm) {
        patientSection.gone()
        selectionSection.gone()
        questionSection.visible()
        resultSection.gone()

        val questionnaire = state.questionnaire
        val question = questionnaire.questions[state.currentIndex]

        val currentQuestionNumber = state.currentIndex + 1
        val totalQuestions = questionnaire.questions.size
        val progressPercentage = ((currentQuestionNumber.toFloat() / totalQuestions) * 100).toInt()

        questionnaireTitleText.text = getString(
            R.string.question_form_title_with_name,
            questionnaire.title
        )

        progressBar.max = totalQuestions
        progressBar.progress = currentQuestionNumber

        progressText.text = getString(
            R.string.question_progress,
            currentQuestionNumber,
            totalQuestions,
            progressPercentage
        )

        progressText.contentDescription = getString(
            R.string.question_progress_accessibility,
            currentQuestionNumber,
            totalQuestions,
            progressPercentage
        )

        questionText.text = question.text

        backButton.isEnabled = state.currentIndex > 0

        nextButton.text = if (state.currentIndex == questionnaire.questions.lastIndex) {
            getString(R.string.calculate_result)
        } else {
            getString(R.string.next_question)
        }

        renderAnswers(question.answers, state.selectedAnswerScore)
        answerHelpText.visible()
        updateNextButtonState()
    }

    private fun renderAnswers(options: List<AnswerOption>, selectedScore: Int?) {
        answersGroup.setOnCheckedChangeListener(null)
        answersGroup.removeAllViews()

        options.forEach { option ->
            val radioButton = RadioButton(this).apply {
                id = View.generateViewId()
                text = option.label
                tag = option.score
                textSize = 20f
                minHeight = resources.getDimensionPixelSize(R.dimen.answer_min_height)
                setPadding(8, 12, 8, 12)
                isChecked = selectedScore == option.score
            }

            answersGroup.addView(radioButton)
        }

        answersGroup.setOnCheckedChangeListener { _, _ ->
            clearError()
            answerHelpText.gone()
            updateNextButtonState()
        }

        if (selectedScore != null) {
            answerHelpText.gone()
        }
    }

    private fun renderResult(state: KioskUiState.Result) {
        patientSection.gone()
        selectionSection.gone()
        questionSection.gone()
        resultSection.visible()

        resultScoreText.text = state.result.formattedScore()
        resultInterpretationText.text = state.result.label
        resultDetailsText.text = buildString {
            appendLine("Questionario: ${state.questionnaire.title}")
            appendLine("Paziente: ${state.patientCode}")
            appendLine()
            appendLine(state.result.description)
            state.result.details.forEach {
                appendLine("• $it")
            }
            appendLine()
            append("Nota: risultato informativo per supportare il colloquio clinico, non sostituisce la valutazione medica.")
        }
    }

    private fun saveSelectedAnswerIfPresent() {
        viewModel.saveCurrentAnswer(selectedAnswerScore())
    }

    private fun selectedAnswerScore(): Int? {
        val checkedId = answersGroup.checkedRadioButtonId
        if (checkedId == -1) return null

        return answersGroup.findViewById<RadioButton>(checkedId)?.tag as? Int
    }

    private fun updateNextButtonState() {
        val hasSelectedAnswer = selectedAnswerScore() != null
        nextButton.isEnabled = hasSelectedAnswer
        nextButton.alpha = if (hasSelectedAnswer) 1.0f else 0.55f
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visible()
    }

    private fun clearError() {
        errorText.text = ""
        errorText.gone()
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(patientCodeInput.windowToken, 0)
    }

    private fun View.visible() {
        visibility = View.VISIBLE
    }

    private fun View.gone() {
        visibility = View.GONE
    }
}