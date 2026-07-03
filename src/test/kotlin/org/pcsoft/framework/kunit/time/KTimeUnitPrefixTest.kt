package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The time-group prefix `infix` functions (e.g. `5 milli seconds`) return a [KTimeUnitInstance]
 * directly, which is backed by a [java.time.Duration] whose whole-seconds part is a `Long`. That wrapper
 * therefore cannot faithfully hold the extreme magnitudes a prefix like `quetta` (1e30) would produce for
 * a base of several seconds. To still cover **every** prefix standalone, the per-prefix tests apply the
 * prefix to a count of `1 / factor`, which always lands at exactly `1.0` second - representable for any
 * prefix - and thus verifies the prefix function multiplies by the correct factor. The prefix × unit
 * matrix is then exercised over the band of prefixes that is representable for all time units (see
 * [representablePrefixFunctions]).
 */
private val representablePrefixFunctions: List<Pair<(Number, KTimeUnit) -> KTimeUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KTimeUnit -> n giga u }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KTimeUnit -> n mega u }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KTimeUnit -> n kilo u }) to KUnitPrefix.KILO,
    ({ n: Number, u: KTimeUnit -> n hecto u }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KTimeUnit -> n deca u }) to KUnitPrefix.DECA,
    ({ n: Number, u: KTimeUnit -> n deci u }) to KUnitPrefix.DECI,
    ({ n: Number, u: KTimeUnit -> n centi u }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KTimeUnit -> n milli u }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KTimeUnit -> n micro u }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KTimeUnit -> n nano u }) to KUnitPrefix.NANO
)

class KTimeUnitPrefixTest {

    /**
     * Applies [prefix] via [apply] to a count of `1 / prefix.factor` seconds; the result must be exactly
     * 1.0 second, which verifies the prefix function scales by [prefix].factor. Using `1 / factor` keeps
     * every prefix - even the most extreme - within the Duration-backed wrapper's representable range.
     */
    private fun assertPrefixScales(prefix: KUnitPrefix, apply: (Number, KTimeUnit) -> KTimeUnitInstance) {
        val actual = apply(1.0 / prefix.factor, KTimeUnit.SECOND).value
        assertEquals(1.0, actual, 1e-9, "prefix $prefix mismatch")
    }

    @Test
    fun `quetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUETTA) { n, u -> n quetta u }

    @Test
    fun `ronna scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONNA) { n, u -> n ronna u }

    @Test
    fun `yotta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOTTA) { n, u -> n yotta u }

    @Test
    fun `zetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZETTA) { n, u -> n zetta u }

    @Test
    fun `exa scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.EXA) { n, u -> n exa u }

    @Test
    fun `peta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PETA) { n, u -> n peta u }

    @Test
    fun `tera scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.TERA) { n, u -> n tera u }

    @Test
    fun `giga scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.GIGA) { n, u -> n giga u }

    @Test
    fun `mega scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MEGA) { n, u -> n mega u }

    @Test
    fun `kilo scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.KILO) { n, u -> n kilo u }

    @Test
    fun `hecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.HECTO) { n, u -> n hecto u }

    @Test
    fun `deca scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECA) { n, u -> n deca u }

    @Test
    fun `deci scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECI) { n, u -> n deci u }

    @Test
    fun `centi scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.CENTI) { n, u -> n centi u }

    @Test
    fun `milli scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MILLI) { n, u -> n milli u }

    @Test
    fun `micro scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MICRO) { n, u -> n micro u }

    @Test
    fun `nano scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.NANO) { n, u -> n nano u }

    @Test
    fun `pico scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PICO) { n, u -> n pico u }

    @Test
    fun `femto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.FEMTO) { n, u -> n femto u }

    @Test
    fun `atto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ATTO) { n, u -> n atto u }

    @Test
    fun `zepto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZEPTO) { n, u -> n zepto u }

    @Test
    fun `yocto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOCTO) { n, u -> n yocto u }

    @Test
    fun `ronto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONTO) { n, u -> n ronto u }

    @Test
    fun `quecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUECTO) { n, u -> n quecto u }

    @Test
    fun `prefix infix returns a KTimeUnitInstance directly`() {
        val ms: KTimeUnitInstance = 5 milli seconds

        assertEquals(0.005, ms.value, 1e-12)
        assertEquals(5.0, ms.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND), 1e-9)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), ms.toKMixedUnitInstance().units)
    }

    @Test
    fun `every time unit combined with every representable prefix scales and reads back`() {
        for ((applyPrefix, prefix) in representablePrefixFunctions) {
            for ((_, unit) in timeUnitGenerators) {
                val result = applyPrefix(5, unit)
                val expectedSeconds = 5.0 * prefix.factor * unit.baseValue

                assertEquals(expectedSeconds, result.value, expectedSeconds.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit mismatch")
                assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit read-back mismatch")
            }
        }
    }
}
