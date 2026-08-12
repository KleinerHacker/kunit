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

package org.pcsoft.framework.kunit

import kotlin.test.fail

/**
 * Runs [block] for every entry of [cases] - the multiplatform stand-in for JUnit Jupiter's
 * `@ParameterizedTest`/`@MethodSource`, which exists on the JVM only.
 *
 * A failing case does not lose its identity: the [AssertionError] is rethrown with the case's index and
 * its rendered content prepended, so a failure reads like
 * `case 3 of 8 [kibi, 1024.0] failed: expected <1024.0> ...` instead of pointing at a bare loop.
 */
internal inline fun <T> forEachCase(cases: List<T>, block: (T) -> Unit) {
    cases.forEachIndexed { index, case ->
        try {
            block(case)
        } catch (error: AssertionError) {
            val rendered = if (case is Array<*>) case.contentToString() else case.toString()
            fail("case ${index + 1} of ${cases.size} $rendered failed: ${error.message}", error)
        }
    }
}
