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

package org.pcsoft.framework.kunit.mechanic.angle

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 angle tokens (each = 1 unit, normalized to radians). Vocabulary for building
// (`90 of degrees`) and reading (`a into turns`); combine with the prefix builders
// (`milli.radians`) and operators (`degrees / seconds`). Prefixed forms live in KAngleUnitExtensions.kt.

// Each token carries its own [KUnitDisplay] so that formatting renders the written-down symbol
// (`"°"`, `"gon"`) rather than the base symbol `"rad"`; the value stays normalized to radians.
private fun bareAngle(unit: KAngleUnit): KAngleUnitInstance = angleOf(unit.baseValue, KUnitDisplay(unit))

/** 1 radian ([KAngleUnit.RADIAN]), the group's base unit. */
val radians: KAngleUnitInstance = bareAngle(KAngleUnit.RADIAN)

/** 1 degree ([KAngleUnit.DEGREE], π/180 rad). */
val degrees: KAngleUnitInstance = bareAngle(KAngleUnit.DEGREE)

/** 1 arcminute ([KAngleUnit.ARCMINUTE], 1/60 °). */
val arcminutes: KAngleUnitInstance = bareAngle(KAngleUnit.ARCMINUTE)

/** 1 arcsecond ([KAngleUnit.ARCSECOND], 1/3600 °). */
val arcseconds: KAngleUnitInstance = bareAngle(KAngleUnit.ARCSECOND)

/** 1 gradian / gon ([KAngleUnit.GRADIAN], π/200 rad). */
val gradians: KAngleUnitInstance = bareAngle(KAngleUnit.GRADIAN)

/** 1 full turn ([KAngleUnit.TURN], 2π rad = 360°). */
val turns: KAngleUnitInstance = bareAngle(KAngleUnit.TURN)
