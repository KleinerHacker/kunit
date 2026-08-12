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

package org.pcsoft.framework.kunit.thermo.temperature

import org.pcsoft.framework.kunit.of
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The runtime half of the blocked scalar operators on an affine absolute temperature. The operators are a
 * compile error via `@Deprecated(ERROR)`, so they can only be reached through reflection - which makes this
 * a JVM-only test.
 */
class KTemperatureOperatorJvmTest {

    /**
     * All four scalar operators are blocked for an affine absolute temperature. Invoked here through
     * reflection to prove they throw at runtime too.
     */
    @Test
    fun `scalar operators on absolute temperature are blocked`() {
        val t = 20 of celsius
        val cls = KTemperatureUnitInstance::class.java
        val facade = Class.forName("org.pcsoft.framework.kunit.thermo.temperature.KTemperatureUnitInstanceKt")

        val cases = listOf(
            { cls.getMethod("times", Number::class.java).invoke(t, 2) },
            { cls.getMethod("div", Number::class.java).invoke(t, 2) },
            { facade.getMethod("times", Number::class.java, KTemperatureUnitInstance::class.java).invoke(null, 2, t) },
            { facade.getMethod("div", Number::class.java, KTemperatureUnitInstance::class.java).invoke(null, 2, t) },
        )
        cases.forEach { call ->
            val ex = assertFailsWith<InvocationTargetException> { call() }
            assertIs<UnsupportedOperationException>(ex.cause)
        }
    }
}
