package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of time. [baseValue] is the factor to convert into the group's base
 * unit ([BASE], second): `1 unit = baseValue * second`.
 *
 * Sub-second scales (millisecond, microsecond, nanosecond, ...) are intentionally **not** modeled as
 * dedicated enum entries - they are reached generically through the SI [org.pcsoft.framework.kunit.KUnitPrefix]
 * table applied to [SECOND] (e.g. `5 milli seconds`).
 *
 * Example:
 * ```kotlin
 * KTimeUnit.HOUR.baseValue // 3600.0 (1 hour = 3600 seconds)
 * ```
 */
enum class KTimeUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Second, the SI base unit of time; [baseValue] = 1.0 by definition. */
    SECOND("s", 1.0),

    /** Minute, 1 min = 60 s. */
    MINUTE("min", 60.0),

    /** Hour, 1 h = 3600 s (= 60 min). */
    HOUR("h", 3600.0),

    /** Day, 1 d = 86 400 s (= 24 h). */
    DAY("d", 86_400.0);

    companion object {
        /**
         * The base unit of the time group; all internal values of [KTimeUnitInstance] are
         * normalized to this unit (second).
         */
        val BASE: KTimeUnit = SECOND
    }
}
