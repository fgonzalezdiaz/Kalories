package com.example.myapplication.helpers

/*
 * Estimació d'energia i CO₂ equivalent a partir del temps d'ús (ACT09).
 * Valors orientatius documentables per al lliurament; no son mesures reals del dispositiu.
 */
object UsageCo2Estimator {

    private const val KWH_PER_HOUR_PHONE = 0.005
    private const val GRAMS_CO2_PER_KWH = 350.0

    fun hoursOfUse(totalForegroundMs: Long): Double =
        totalForegroundMs / 3_600_000.0

    fun estimatedKwh(totalForegroundMs: Long): Double =
        hoursOfUse(totalForegroundMs) * KWH_PER_HOUR_PHONE

    /* Resultat en kg CO₂e (equivalent). */
    fun estimatedCo2Kg(totalForegroundMs: Long): Double =
        estimatedKwh(totalForegroundMs) * GRAMS_CO2_PER_KWH / 1000.0
}
