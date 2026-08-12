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

import java.time.Duration
import java.time.temporal.TemporalUnit
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

/**
 * The wrapped duration as a [java.time.Duration] - the JVM counterpart of
 * [KTimeUnitInstance.toKotlinDuration].
 *
 * Example:
 * ```kotlin
 * 2.hours.toDuration() // PT2H
 * ```
 */
fun KTimeUnitInstance.toDuration(): Duration = duration.toJavaDuration()

/**
 * A copy with the given [java.time.Duration] added - the JVM counterpart of
 * [KTimeUnitInstance.plus].
 */
fun KTimeUnitInstance.plus(amount: Duration): KTimeUnitInstance =
    KTimeUnitInstance(duration + amount.toKotlinDuration())

/** A copy with the given amount of the given `java.time` [unit] added. */
fun KTimeUnitInstance.plus(amountToAdd: Long, unit: TemporalUnit): KTimeUnitInstance =
    KTimeUnitInstance(duration + unit.duration.multipliedBy(amountToAdd).toKotlinDuration())

/**
 * A copy with the given [java.time.Duration] subtracted - the JVM counterpart of
 * [KTimeUnitInstance.minus].
 */
fun KTimeUnitInstance.minus(amount: Duration): KTimeUnitInstance =
    KTimeUnitInstance(duration - amount.toKotlinDuration())

/** A copy with the given amount of the given `java.time` [unit] subtracted. */
fun KTimeUnitInstance.minus(amountToSubtract: Long, unit: TemporalUnit): KTimeUnitInstance =
    KTimeUnitInstance(duration - unit.duration.multipliedBy(amountToSubtract).toKotlinDuration())

/** A copy truncated to whole multiples of the given `java.time` [unit] ([Duration.truncatedTo]). */
fun KTimeUnitInstance.truncatedTo(unit: TemporalUnit): KTimeUnitInstance =
    KTimeUnitInstance(toDuration().truncatedTo(unit).toKotlinDuration())

/**
 * Wraps this [java.time.Duration] as a [KTimeUnitInstance], the entry point for interoperating with
 * `java.time`.
 *
 * Example:
 * ```kotlin
 * java.time.Duration.ofMinutes(90).toTime().valueAs(KTimeUnit.HOUR) // 1.5
 * ```
 */
fun Duration.toTime(): KTimeUnitInstance = KTimeUnitInstance(toKotlinDuration())
