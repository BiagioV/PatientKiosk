package it.uninsubria.patientkiosk.scoring

data class ScoreResult(
    val mainScore: Int,
    val maxScore: Int,
    val label: String,
    val description: String,
    val details: List<String>
) {
    fun formattedScore(): String = "$mainScore / $maxScore"
}
