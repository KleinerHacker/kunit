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

package org.pcsoft.framework.kunit.electric.dipolemoment

// Bare, value-1 electric dipole moment tokens (each = 1 unit, normalized to coulomb meters). Vocabulary for
// building (`1.85 of debyes`) and reading (`p into coulombMeters`); combine with the prefix builders
// (`pico.coulombMeters`). Prefixed forms live in KElectricDipoleMomentUnitExtensions.kt.

/** 1 coulomb meter ([KElectricDipoleMomentUnit.COULOMB_METER]). */
val coulombMeters: KElectricDipoleMomentUnitInstance =
    electricDipoleMomentInstanceOf(KElectricDipoleMomentUnit.COULOMB_METER.baseValue)

/** 1 debye ([KElectricDipoleMomentUnit.DEBYE], CGS, 3.335 640 952e-30 C·m). */
val debyes: KElectricDipoleMomentUnitInstance =
    electricDipoleMomentInstanceOf(KElectricDipoleMomentUnit.DEBYE.baseValue)
