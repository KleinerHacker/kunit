package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm
import java.time.Duration
import java.time.temporal.TemporalUnit
import kotlin.math.floor
import kotlin.math.roundToLong

/**
 * A 100 % wrapper around [java.time.Duration], exposed as the "pure" unit of the time group. The
 * [duration] is the single source of truth: `+`, `-`, comparison and equality operate directly on it
 * (nanosecond-exact), and the full [Duration] API is forwarded (see the `plusXxx`/`minusXxx`/`toXxx`
 * members below). On top of that, it offers the same surface as every other "pure" unit wrapper (e.g.
 * `KLengthUnitInstance`): [value]/[valueIn]/`*`/`/`/[toString]/[toKUnitInstance], so it plugs into the
 * generic mixed-unit engine (e.g. `length / time` = speed).
 *
 * Unlike the other wrappers, a time value always represents exponent 1 (a [Duration] cannot represent a
 * time² or 1/time). Multiplying/dividing therefore "escapes" to a raw [KUnitInstance] (e.g. `s^2`,
 * `s^-1`), just like length's `*`/`/`.
 *
 * Instances are created via the extension functions in `KTimeUnitExtensions.kt` (e.g. `5.seconds()`,
 * `2.hours()`), from a [Duration] via [toKTimeUnit], or by constructing values with the generic,
 * root-level prefix `infix` functions and converting them via [toKTimeUnit] (e.g.
 * `(5 milli seconds).toKUnitInstance().toKTimeUnit()`).
 *
 * Example:
 * ```kotlin
 * val t = 2.hours()
 * t.value               // 7200.0 (normalized to seconds)
 * t.valueIn(KTimeUnit.HOUR) // 2.0 (read back in hours)
 * t.toDuration()        // PT2H
 * ```
 */
class KTimeUnitInstance internal constructor(internal val duration: Duration) {

    /**
     * Value expressed in the base unit ([KTimeUnit.BASE], second), as a [Double]. Computed from the
     * [Duration]'s whole seconds plus its nanosecond part; avoids [Duration.toNanos] to stay safe for
     * very large durations.
     */
    val value: Double get() = duration.seconds.toDouble() + duration.nano / 1e9

    /** The generic single-term (`SECOND^1`) representation this wrapper delegates its engine work to. */
    internal fun toBaseInstance(): KUnitInstance = KUnitInstance(value, listOf(KUnitTerm(KTimeUnit.BASE, 1)))

    /**
     * Converts [value] into the given unit or prefixed unit - see [KUnitInstance.valueAs] for the exact
     * matching rules.
     *
     * @throws IllegalStateException if [target] does not belong to the time group.
     *
     * Example:
     * ```kotlin
     * val t = 2.hours()
     * t.valueIn(KTimeUnit.MINUTE)                        // 120.0
     * t.valueIn(KUnitPrefix.MILLI with KTimeUnit.SECOND) // 7 200 000.0 (ms)
     * ```
     */
    fun valueIn(target: KUnitTarget): Double = toBaseInstance().valueAs(target)

    /**
     * Adds two durations, automatically converting between different [KTimeUnit]s since both operands
     * are always normalized to [KTimeUnit.BASE] internally. Implemented as exact [Duration] addition.
     *
     * Example:
     * ```kotlin
     * (1.hours() + 30.minutes()).value // 5400.0
     * ```
     */
    operator fun plus(other: KTimeUnitInstance): KTimeUnitInstance = KTimeUnitInstance(duration + other.duration)

    /** Subtracts two durations. See [plus] for the automatic unit conversion; implemented as exact [Duration] subtraction. */
    operator fun minus(other: KTimeUnitInstance): KTimeUnitInstance = KTimeUnitInstance(duration - other.duration)

    /**
     * Multiplies two time values, producing a new [KUnitInstance] whose exponent is the sum of both
     * operands' exponents (`SECOND^2`, no longer a "pure" duration).
     */
    operator fun times(other: KTimeUnitInstance): KUnitInstance = toBaseInstance() * other.toBaseInstance()

    /** Divides two time values, producing a new (dimensionless) [KUnitInstance]. */
    operator fun div(other: KTimeUnitInstance): KUnitInstance = toBaseInstance() / other.toBaseInstance()

    /** Multiplies this value with an arbitrary mixed unit, producing a new [KUnitInstance]. */
    operator fun times(other: KUnitInstance): KUnitInstance = toBaseInstance() * other

    /** Divides this value by an arbitrary mixed unit, producing a new [KUnitInstance]. */
    operator fun div(other: KUnitInstance): KUnitInstance = toBaseInstance() / other

    /** Compares two durations chronologically (delegates to [Duration.compareTo]). */
    operator fun compareTo(other: KTimeUnitInstance): Int = duration.compareTo(other.duration)

    /** Structural equality by the underlying [Duration] (nanosecond-exact). */
    override fun equals(other: Any?): Boolean = other is KTimeUnitInstance && duration == other.duration

    override fun hashCode(): Int = duration.hashCode()

    /** Base-unit representation, e.g. `"7200.0 s"` for two hours. */
    override fun toString(): String = toBaseInstance().toString()

    /**
     * Representation in the given unit or prefixed unit - see [valueIn] for the matching rules.
     *
     * @throws IllegalStateException under the same conditions as [valueIn].
     *
     * Example:
     * ```kotlin
     * 2.hours().toString(KTimeUnit.HOUR)   // "2.0 h"
     * 2.hours().toString(KTimeUnit.MINUTE) // "120.0 min"
     * ```
     */
    fun toString(target: KUnitTarget): String = toBaseInstance().toString(target)

    /** The underlying generic [KUnitInstance] representation (single term: `SECOND^1`). */
    fun toKUnitInstance(): KUnitInstance = toBaseInstance()

    // --- java.time.Duration facade -------------------------------------------------------------

    /** The wrapped [java.time.Duration] (nanosecond-exact backing store of this value). */
    fun toDuration(): Duration = duration

    /** The whole-seconds part of the duration ([Duration.getSeconds]). */
    fun getSeconds(): Long = duration.seconds

    /** The nanosecond-of-second part of the duration, `0..999_999_999` ([Duration.getNano]). */
    fun getNano(): Int = duration.nano

    /** `true` if the duration is zero length ([Duration.isZero]). */
    fun isZero(): Boolean = duration.isZero

    /** `true` if the duration is negative ([Duration.isNegative]). */
    fun isNegative(): Boolean = duration.isNegative

    /** The total number of whole days ([Duration.toDays]). */
    fun toDays(): Long = duration.toDays()

    /** The total number of whole hours ([Duration.toHours]). */
    fun toHours(): Long = duration.toHours()

    /** The total number of whole minutes ([Duration.toMinutes]). */
    fun toMinutes(): Long = duration.toMinutes()

    /** The total number of whole seconds ([Duration.toSeconds]). */
    fun toSeconds(): Long = duration.toSeconds()

    /** The total number of whole milliseconds ([Duration.toMillis]). */
    fun toMillis(): Long = duration.toMillis()

    /** The total number of nanoseconds ([Duration.toNanos]); may overflow for very large durations. */
    fun toNanos(): Long = duration.toNanos()

    /** The days part of `d:h:m:s` ([Duration.toDaysPart]). */
    fun toDaysPart(): Long = duration.toDaysPart()

    /** The hours part `0..23` ([Duration.toHoursPart]). */
    fun toHoursPart(): Int = duration.toHoursPart()

    /** The minutes part `0..59` ([Duration.toMinutesPart]). */
    fun toMinutesPart(): Int = duration.toMinutesPart()

    /** The seconds part `0..59` ([Duration.toSecondsPart]). */
    fun toSecondsPart(): Int = duration.toSecondsPart()

    /** The milliseconds part `0..999` ([Duration.toMillisPart]). */
    fun toMillisPart(): Int = duration.toMillisPart()

    /** The nanoseconds part `0..999_999_999` ([Duration.toNanosPart]). */
    fun toNanosPart(): Int = duration.toNanosPart()

    /** A copy with the given [Duration] added ([Duration.plus]). */
    fun plus(amount: Duration): KTimeUnitInstance = KTimeUnitInstance(duration.plus(amount))

    /** A copy with the given amount of the given [unit] added ([Duration.plus]). */
    fun plus(amountToAdd: Long, unit: TemporalUnit): KTimeUnitInstance = KTimeUnitInstance(duration.plus(amountToAdd, unit))

    /** A copy with the given number of days added ([Duration.plusDays]). */
    fun plusDays(days: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusDays(days))

    /** A copy with the given number of hours added ([Duration.plusHours]). */
    fun plusHours(hours: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusHours(hours))

    /** A copy with the given number of minutes added ([Duration.plusMinutes]). */
    fun plusMinutes(minutes: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusMinutes(minutes))

    /** A copy with the given number of seconds added ([Duration.plusSeconds]). */
    fun plusSeconds(seconds: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusSeconds(seconds))

    /** A copy with the given number of milliseconds added ([Duration.plusMillis]). */
    fun plusMillis(millis: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusMillis(millis))

    /** A copy with the given number of nanoseconds added ([Duration.plusNanos]). */
    fun plusNanos(nanos: Long): KTimeUnitInstance = KTimeUnitInstance(duration.plusNanos(nanos))

    /** A copy with the given [Duration] subtracted ([Duration.minus]). */
    fun minus(amount: Duration): KTimeUnitInstance = KTimeUnitInstance(duration.minus(amount))

    /** A copy with the given amount of the given [unit] subtracted ([Duration.minus]). */
    fun minus(amountToSubtract: Long, unit: TemporalUnit): KTimeUnitInstance = KTimeUnitInstance(duration.minus(amountToSubtract, unit))

    /** A copy with the given number of days subtracted ([Duration.minusDays]). */
    fun minusDays(days: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusDays(days))

    /** A copy with the given number of hours subtracted ([Duration.minusHours]). */
    fun minusHours(hours: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusHours(hours))

    /** A copy with the given number of minutes subtracted ([Duration.minusMinutes]). */
    fun minusMinutes(minutes: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusMinutes(minutes))

    /** A copy with the given number of seconds subtracted ([Duration.minusSeconds]). */
    fun minusSeconds(seconds: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusSeconds(seconds))

    /** A copy with the given number of milliseconds subtracted ([Duration.minusMillis]). */
    fun minusMillis(millis: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusMillis(millis))

    /** A copy with the given number of nanoseconds subtracted ([Duration.minusNanos]). */
    fun minusNanos(nanos: Long): KTimeUnitInstance = KTimeUnitInstance(duration.minusNanos(nanos))

    /** A copy multiplied by the given scalar ([Duration.multipliedBy]). */
    fun multipliedBy(multiplicand: Long): KTimeUnitInstance = KTimeUnitInstance(duration.multipliedBy(multiplicand))

    /** A copy divided by the given scalar ([Duration.dividedBy]). */
    fun dividedBy(divisor: Long): KTimeUnitInstance = KTimeUnitInstance(duration.dividedBy(divisor))

    /** How many times the given [divisor] fits into this duration ([Duration.dividedBy]). */
    fun dividedBy(divisor: KTimeUnitInstance): Long = duration.dividedBy(divisor.duration)

    /** A copy with the length negated ([Duration.negated]). */
    fun negated(): KTimeUnitInstance = KTimeUnitInstance(duration.negated())

    /** A copy with an absolute (non-negative) length ([Duration.abs]). */
    fun abs(): KTimeUnitInstance = KTimeUnitInstance(duration.abs())

    /** A copy truncated to the given [unit] ([Duration.truncatedTo]). */
    fun truncatedTo(unit: TemporalUnit): KTimeUnitInstance = KTimeUnitInstance(duration.truncatedTo(unit))

    /** A copy with the whole-seconds part replaced ([Duration.withSeconds]). */
    fun withSeconds(seconds: Long): KTimeUnitInstance = KTimeUnitInstance(duration.withSeconds(seconds))

    /** A copy with the nanosecond-of-second part replaced ([Duration.withNanos]). */
    fun withNanos(nanoOfSecond: Int): KTimeUnitInstance = KTimeUnitInstance(duration.withNanos(nanoOfSecond))
}

/**
 * Converts this mixed unit to a "pure" time value, as long as it consists of exactly one term of any
 * [KTimeUnit] **at exponent 1** - normalizing it to [KTimeUnit.BASE] (second) if it isn't already
 * (e.g. a term tagged with [KTimeUnit.HOUR] is converted to the equivalent number of seconds). Only
 * exponent 1 is accepted, since a [Duration] cannot represent a time² or 1/time. This is what lets
 * [org.pcsoft.framework.kunit.KPrefixBuilder] (which, being generic, tags terms with whichever
 * [org.pcsoft.framework.kunit.KUnit] it was given) be converted into a [KTimeUnitInstance].
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a [KTimeUnit]
 * at exponent 1 (i.e. it is not a pure duration).
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters() / 2.seconds()
 * val time = speed.toKUnitInstance() // NOT a pure time -> would throw
 *
 * (5 milli KTimeUnit.SECOND).toKUnitInstance().toKTimeUnit().value // 0.005
 * ```
 */
fun KUnitInstance.toKTimeUnit(): KTimeUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KTimeUnit) {
        "KUnitInstance $this does not represent a pure time value (expected exactly one term of a KTimeUnit at exponent 1)"
    }
    return timeUnitInstanceOf(value * unit.baseValue)
}

/**
 * Wraps this [java.time.Duration] as a [KTimeUnitInstance], the entry point for interoperating with
 * `java.time`.
 *
 * Example:
 * ```kotlin
 * java.time.Duration.ofMinutes(90).toKTimeUnit().valueIn(KTimeUnit.HOUR) // 1.5
 * ```
 */
fun Duration.toKTimeUnit(): KTimeUnitInstance = KTimeUnitInstance(this)

internal fun timeUnitInstanceOf(seconds: Double): KTimeUnitInstance = KTimeUnitInstance(secondsToDuration(seconds))

/** Builds a nanosecond-exact [Duration] from a fractional number of [seconds] (rounded to the nearest nanosecond). */
private fun secondsToDuration(seconds: Double): Duration {
    val whole = floor(seconds).toLong()
    val nanos = ((seconds - whole) * 1e9).roundToLong()
    return Duration.ofSeconds(whole, nanos)
}
