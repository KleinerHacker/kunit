/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

package org.pcsoft.framework.kunit.kinematic.time

import org.pcsoft.framework.kunit.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * A 100 % wrapper around [kotlin.time.Duration], exposed as the "pure" unit of the time group. The
 * [duration] is the single source of truth: `+`, `-`, comparison and equality operate directly on it, and
 * the full [Duration] API is forwarded (see the `plusXxx`/`minusXxx`/`toXxx` members below). On top of that,
 * it offers the same surface as every other "pure" unit wrapper (e.g. `KLengthUnitInstance`):
 * [value]/`*`/`/`/[toString]/[toUnit], so it plugs into the generic mixed-unit engine
 * (e.g. `length / time` = speed).
 *
 * Unlike the other wrappers, a time value always represents exponent 1 (a [Duration] cannot represent a
 * time² or 1/time). Multiplying/dividing therefore "escapes" to a raw [KMixedUnitInstance] (e.g. `s^2`,
 * `s^-1`), just like length's `*`/`/`.
 *
 * Instances are created via the creator extension properties in `KTimeUnitExtensions.kt` (e.g. `5.seconds`,
 * `2.hours`), the SI-prefix `infix` constructors in `KTimeUnitPrefix.kt` (e.g. `5 milli seconds`),
 * or from a [Duration] via [toTime].
 *
 * **Precision:** [kotlin.time.Duration] is nanosecond-exact up to roughly ±146 years and switches to
 * millisecond resolution beyond that. On the JVM the `java.time` interop (`toDuration()`, `plus`, `minus`,
 * `truncatedTo`) is available as extensions from the JVM source set.
 *
 * Example:
 * ```kotlin
 * val t = 2.hours
 * t.value                   // 7200.0 (normalized to seconds)
 * t.valueAs(KTimeUnit.HOUR) // 2.0 (read back in hours)
 * t.toKotlinDuration()      // 2h
 * ```
 */
class KTimeUnitInstance internal constructor(
    internal val duration: Duration,
    internal val display: KUnitDisplay? = null,
) : KUnitInstance<KTimeUnitInstance>, KUnitMeasurable by baseInstanceOf(duration, display) {

    /**
     * Returns a new time value with [value] (seconds) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.seconds`).
     */
    override fun scaledBy(factor: Double): KTimeUnitInstance = timeUnitInstanceOf(value * factor)

    /**
     * Adds two durations, automatically converting between different [KTimeUnit]s since both operands
     * are always normalized to [KTimeUnit.BASE] internally. Implemented as exact [Duration] addition.
     *
     * Example:
     * ```kotlin
     * (1.hours + 30.minutes).value // 5400.0
     * ```
     */
    override operator fun plus(other: KTimeUnitInstance): KTimeUnitInstance =
        KTimeUnitInstance(duration + other.duration)

    /** Subtracts two durations. See [plus] for the automatic unit conversion; implemented as exact [Duration] subtraction. */
    override operator fun minus(other: KTimeUnitInstance): KTimeUnitInstance =
        KTimeUnitInstance(duration - other.duration)

    /**
     * Multiplies two time values, producing a new [KMixedUnitInstance] whose exponent is the sum of both
     * operands' exponents (`SECOND^2`, no longer a "pure" duration).
     */
    operator fun times(other: KTimeUnitInstance): KMixedUnitInstance = toUnit() * other.toUnit()

    /** Divides two time values, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KTimeUnitInstance): KMixedUnitInstance = toUnit() / other.toUnit()

    /** Compares two durations chronologically (delegates to [Duration.compareTo]). */
    override operator fun compareTo(other: KTimeUnitInstance): Int = duration.compareTo(other.duration)

    /** Structural equality by the underlying [Duration]. */
    override fun equals(other: Any?): Boolean = other is KTimeUnitInstance && duration == other.duration

    override fun hashCode(): Int = duration.hashCode()

    /** Base-unit representation, e.g. `"7200.0 s"` for two hours. */
    override fun toString(): String = toUnit().toString()

    // --- kotlin.time.Duration facade -----------------------------------------------------------

    /** The wrapped [kotlin.time.Duration] (backing store of this value). */
    fun toKotlinDuration(): Duration = duration

    /**
     * The whole-seconds part of the duration, floored towards negative infinity, so that
     * `getSeconds() + getNano() / 1e9` always reconstructs the value.
     */
    fun getSeconds(): Long = floorParts().first

    /** The nanosecond-of-second part of the duration, `0..999_999_999`. */
    fun getNano(): Int = floorParts().second

    /** `true` if the duration is zero length. */
    fun isZero(): Boolean = duration == Duration.ZERO

    /** `true` if the duration is negative. */
    fun isNegative(): Boolean = duration < Duration.ZERO

    /** The total number of whole days ([Duration.inWholeDays]). */
    fun toDays(): Long = duration.inWholeDays

    /** The total number of whole hours ([Duration.inWholeHours]). */
    fun toHours(): Long = duration.inWholeHours

    /** The total number of whole minutes ([Duration.inWholeMinutes]). */
    fun toMinutes(): Long = duration.inWholeMinutes

    /** The total number of whole seconds ([Duration.inWholeSeconds]). */
    fun toSeconds(): Long = duration.inWholeSeconds

    /** The total number of whole milliseconds ([Duration.inWholeMilliseconds]). */
    fun toMillis(): Long = duration.inWholeMilliseconds

    /** The total number of nanoseconds ([Duration.inWholeNanoseconds]); saturates for very large durations. */
    fun toNanos(): Long = duration.inWholeNanoseconds

    /** The days part of `d:h:m:s`. */
    fun toDaysPart(): Long = duration.inWholeDays

    /** The hours part `0..23`. */
    fun toHoursPart(): Int = (duration.inWholeHours % 24).toInt()

    /** The minutes part `0..59`. */
    fun toMinutesPart(): Int = (duration.inWholeMinutes % 60).toInt()

    /** The seconds part `0..59`. */
    fun toSecondsPart(): Int = (duration.inWholeSeconds % 60).toInt()

    /** The milliseconds part `0..999`. */
    fun toMillisPart(): Int = (duration.inWholeMilliseconds % 1_000).toInt()

    /** The nanoseconds part `0..999_999_999`. */
    fun toNanosPart(): Int = (duration.inWholeNanoseconds % 1_000_000_000).toInt()

    /** A copy with the given [Duration] added. */
    fun plus(amount: Duration): KTimeUnitInstance = KTimeUnitInstance(duration + amount)

    /** A copy with the given amount of the given [unit] added. */
    fun plus(amountToAdd: Long, unit: DurationUnit): KTimeUnitInstance =
        KTimeUnitInstance(duration + amountToAdd.toDuration(unit))

    /** A copy with the given number of days added. */
    fun plusDays(days: Long): KTimeUnitInstance = KTimeUnitInstance(duration + days.days)

    /** A copy with the given number of hours added. */
    fun plusHours(hours: Long): KTimeUnitInstance = KTimeUnitInstance(duration + hours.hours)

    /** A copy with the given number of minutes added. */
    fun plusMinutes(minutes: Long): KTimeUnitInstance = KTimeUnitInstance(duration + minutes.minutes)

    /** A copy with the given number of seconds added. */
    fun plusSeconds(seconds: Long): KTimeUnitInstance = KTimeUnitInstance(duration + seconds.seconds)

    /** A copy with the given number of milliseconds added. */
    fun plusMillis(millis: Long): KTimeUnitInstance = KTimeUnitInstance(duration + millis.milliseconds)

    /** A copy with the given number of nanoseconds added. */
    fun plusNanos(nanos: Long): KTimeUnitInstance = KTimeUnitInstance(duration + nanos.nanoseconds)

    /** A copy with the given [Duration] subtracted. */
    fun minus(amount: Duration): KTimeUnitInstance = KTimeUnitInstance(duration - amount)

    /** A copy with the given amount of the given [unit] subtracted. */
    fun minus(amountToSubtract: Long, unit: DurationUnit): KTimeUnitInstance =
        KTimeUnitInstance(duration - amountToSubtract.toDuration(unit))

    /** A copy with the given number of days subtracted. */
    fun minusDays(days: Long): KTimeUnitInstance = KTimeUnitInstance(duration - days.days)

    /** A copy with the given number of hours subtracted. */
    fun minusHours(hours: Long): KTimeUnitInstance = KTimeUnitInstance(duration - hours.hours)

    /** A copy with the given number of minutes subtracted. */
    fun minusMinutes(minutes: Long): KTimeUnitInstance = KTimeUnitInstance(duration - minutes.minutes)

    /** A copy with the given number of seconds subtracted. */
    fun minusSeconds(seconds: Long): KTimeUnitInstance = KTimeUnitInstance(duration - seconds.seconds)

    /** A copy with the given number of milliseconds subtracted. */
    fun minusMillis(millis: Long): KTimeUnitInstance = KTimeUnitInstance(duration - millis.milliseconds)

    /** A copy with the given number of nanoseconds subtracted. */
    fun minusNanos(nanos: Long): KTimeUnitInstance = KTimeUnitInstance(duration - nanos.nanoseconds)

    /** A copy multiplied by the given scalar. */
    fun multipliedBy(multiplicand: Long): KTimeUnitInstance =
        KTimeUnitInstance(duration * multiplicand.toDouble())

    /** A copy divided by the given scalar. */
    fun dividedBy(divisor: Long): KTimeUnitInstance = KTimeUnitInstance(duration / divisor.toDouble())

    /** How many times the given [divisor] fits into this duration (whole times, truncated). */
    fun dividedBy(divisor: KTimeUnitInstance): Long = (duration / divisor.duration).toLong()

    /** A copy with the length negated. */
    fun negated(): KTimeUnitInstance = KTimeUnitInstance(-duration)

    /** A copy with an absolute (non-negative) length. */
    fun abs(): KTimeUnitInstance = KTimeUnitInstance(duration.absoluteValue)

    /** A copy truncated (towards zero) to whole multiples of the given [unit]. */
    fun truncatedTo(unit: DurationUnit): KTimeUnitInstance =
        KTimeUnitInstance(duration.toLong(unit).toDuration(unit))

    /** A copy with the whole-seconds part replaced. */
    fun withSeconds(seconds: Long): KTimeUnitInstance =
        KTimeUnitInstance(seconds.seconds + floorParts().second.nanoseconds)

    /** A copy with the nanosecond-of-second part replaced. */
    fun withNanos(nanoOfSecond: Int): KTimeUnitInstance =
        KTimeUnitInstance(floorParts().first.seconds + nanoOfSecond.nanoseconds)

    /**
     * Splits the duration into whole seconds (floored towards negative infinity) and a non-negative
     * nanosecond-of-second part, mirroring the `java.time.Duration` component semantics.
     */
    private fun floorParts(): Pair<Long, Int> = duration.toComponents { seconds, nanoseconds ->
        if (nanoseconds < 0) (seconds - 1) to (nanoseconds + 1_000_000_000) else seconds to nanoseconds
    }
}

/**
 * Converts this mixed unit to a "pure" time value, as long as it consists of exactly one term of any
 * [KTimeUnit] - normalizing it to [KTimeUnit.BASE] (second) if it isn't already (e.g. a term tagged
 * with [KTimeUnit.HOUR] is converted to the equivalent number of seconds). The term's exponent is
 * irrelevant to this conversion: a `KTimeUnit` term is a time-typed unit regardless of exponent (even
 * a "square second"), and the resulting [KTimeUnitInstance] simply wraps the numeric value as a
 * [Duration]. This is what lets an arbitrary single-[KTimeUnit] mixed instance (whose term may be
 * tagged with any [KTimeUnit]) be converted into a [KTimeUnitInstance].
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a [KTimeUnit]
 * (i.e. it is not a single time-typed term).
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters / 2.seconds
 * val time = speed.toUnit() // NOT a single time term -> would throw
 *
 * (5 milli seconds).value // 0.005
 * ```
 */
fun KMixedUnitInstance.toTime(): KTimeUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KTimeUnit) {
        "KMixedUnitInstance $this does not represent a pure time value (expected exactly one term of a KTimeUnit)"
    }
    return timeUnitInstanceOf(value * unit.baseValue)
}

/**
 * Wraps this [kotlin.time.Duration] as a [KTimeUnitInstance], the entry point for interoperating with
 * `kotlin.time`.
 *
 * Example:
 * ```kotlin
 * 90.minutes.toTime().valueAs(KTimeUnit.HOUR) // 1.5
 * ```
 */
fun Duration.toTime(): KTimeUnitInstance = KTimeUnitInstance(this)

internal fun timeUnitInstanceOf(seconds: Double, display: KUnitDisplay? = null): KTimeUnitInstance =
    KTimeUnitInstance(seconds.toDuration(DurationUnit.SECONDS), display)

/**
 * Builds the generic single-term (`SECOND^1`) [KMixedUnitInstance] representation for a [duration],
 * the delegate backing [KTimeUnitInstance]'s [KUnitMeasurable] surface. [display] is the (cosmetic)
 * written-down unit carried onto the term for formatting.
 */
private fun baseInstanceOf(duration: Duration, display: KUnitDisplay?): KMixedUnitInstance =
    KMixedUnitInstance(duration.toDouble(DurationUnit.SECONDS), listOf(KUnitTerm(KTimeUnit.BASE, 1, display)))
