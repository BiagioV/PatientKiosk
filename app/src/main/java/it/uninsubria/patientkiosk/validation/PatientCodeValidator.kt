package it.uninsubria.patientkiosk.validation

object PatientCodeValidator {

    private val allowedPattern = Regex("^[A-Z0-9_-]+$")

    data class ValidationResult(
        val isValid: Boolean,
        val normalizedCode: String = "",
        val errorMessage: String? = null
    )

    fun validate(rawCode: String): ValidationResult {
        val code = rawCode.trim().uppercase()

        if (code.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Inserisci il codice paziente prima di continuare."
            )
        }

        if (code.length < 3) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Il codice paziente deve contenere almeno 3 caratteri."
            )
        }

        if (code.length > 20) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Il codice paziente non può superare 20 caratteri."
            )
        }

        if (!allowedPattern.matches(code)) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Usa solo lettere, numeri, trattino - o underscore _. Non inserire nome e cognome."
            )
        }

        return ValidationResult(
            isValid = true,
            normalizedCode = code
        )
    }
}