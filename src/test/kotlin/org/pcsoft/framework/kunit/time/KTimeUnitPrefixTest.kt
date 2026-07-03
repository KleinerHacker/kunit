package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.atto
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.deca
import org.pcsoft.framework.kunit.deci
import org.pcsoft.framework.kunit.exa
import org.pcsoft.framework.kunit.femto
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.peta
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.quecto
import org.pcsoft.framework.kunit.quetta
import org.pcsoft.framework.kunit.ronna
import org.pcsoft.framework.kunit.ronto
import org.pcsoft.framework.kunit.tera
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.yocto
import org.pcsoft.framework.kunit.yotta
import org.pcsoft.framework.kunit.zepto
import org.pcsoft.framework.kunit.zetta
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The prefix × unit matrix is verified at the [KMixedUnitInstance] (Double) level, which is where prefix
 * scaling actually happens (the root-level prefix `infix` functions produce a `KPrefixBuilder` ->
 * [KMixedUnitInstance]). It is intentionally not routed through [KTimeUnitInstance] for every combination,
 * because that wrapper is backed by [java.time.Duration] whose whole-seconds part is a `Long` and thus
 * cannot represent the extreme prefixes (e.g. `5 quetta seconds` = 5e30 s far exceeds `Long.MAX`). The
 * lossless round trip through the Duration wrapper is exercised separately for representable magnitudes
 * (see [KTimeUnitInstanceTest]).
 */
private val prefixFunctions: List<Pair<(Number, KTimeUnit) -> KMixedUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KTimeUnit -> (n quetta u).toKMixedUnitInstance() }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KTimeUnit -> (n ronna u).toKMixedUnitInstance() }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KTimeUnit -> (n yotta u).toKMixedUnitInstance() }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KTimeUnit -> (n zetta u).toKMixedUnitInstance() }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KTimeUnit -> (n exa u).toKMixedUnitInstance() }) to KUnitPrefix.EXA,
    ({ n: Number, u: KTimeUnit -> (n peta u).toKMixedUnitInstance() }) to KUnitPrefix.PETA,
    ({ n: Number, u: KTimeUnit -> (n tera u).toKMixedUnitInstance() }) to KUnitPrefix.TERA,
    ({ n: Number, u: KTimeUnit -> (n giga u).toKMixedUnitInstance() }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KTimeUnit -> (n mega u).toKMixedUnitInstance() }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KTimeUnit -> (n kilo u).toKMixedUnitInstance() }) to KUnitPrefix.KILO,
    ({ n: Number, u: KTimeUnit -> (n hecto u).toKMixedUnitInstance() }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KTimeUnit -> (n deca u).toKMixedUnitInstance() }) to KUnitPrefix.DECA,
    ({ n: Number, u: KTimeUnit -> (n deci u).toKMixedUnitInstance() }) to KUnitPrefix.DECI,
    ({ n: Number, u: KTimeUnit -> (n centi u).toKMixedUnitInstance() }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KTimeUnit -> (n milli u).toKMixedUnitInstance() }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KTimeUnit -> (n micro u).toKMixedUnitInstance() }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KTimeUnit -> (n nano u).toKMixedUnitInstance() }) to KUnitPrefix.NANO,
    ({ n: Number, u: KTimeUnit -> (n pico u).toKMixedUnitInstance() }) to KUnitPrefix.PICO,
    ({ n: Number, u: KTimeUnit -> (n femto u).toKMixedUnitInstance() }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KTimeUnit -> (n atto u).toKMixedUnitInstance() }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KTimeUnit -> (n zepto u).toKMixedUnitInstance() }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KTimeUnit -> (n yocto u).toKMixedUnitInstance() }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KTimeUnit -> (n ronto u).toKMixedUnitInstance() }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KTimeUnit -> (n quecto u).toKMixedUnitInstance() }) to KUnitPrefix.QUECTO
)

class KTimeUnitPrefixTest {

    /** Verifies that applying [prefix] via [apply] to 5 seconds matches the equivalent unprefixed value. */
    private fun assertPrefixScales(prefix: KUnitPrefix, apply: (Number, KTimeUnit) -> KMixedUnitInstance) {
        val expected = 5.0 * prefix.factor // seconds (SECOND.baseValue == 1.0)
        val actual = apply(5, KTimeUnit.SECOND).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @Test
    fun `quetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUETTA) { n, u -> (n quetta u).toKMixedUnitInstance() }

    @Test
    fun `ronna scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONNA) { n, u -> (n ronna u).toKMixedUnitInstance() }

    @Test
    fun `yotta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOTTA) { n, u -> (n yotta u).toKMixedUnitInstance() }

    @Test
    fun `zetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZETTA) { n, u -> (n zetta u).toKMixedUnitInstance() }

    @Test
    fun `exa scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.EXA) { n, u -> (n exa u).toKMixedUnitInstance() }

    @Test
    fun `peta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PETA) { n, u -> (n peta u).toKMixedUnitInstance() }

    @Test
    fun `tera scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.TERA) { n, u -> (n tera u).toKMixedUnitInstance() }

    @Test
    fun `giga scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.GIGA) { n, u -> (n giga u).toKMixedUnitInstance() }

    @Test
    fun `mega scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MEGA) { n, u -> (n mega u).toKMixedUnitInstance() }

    @Test
    fun `kilo scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.KILO) { n, u -> (n kilo u).toKMixedUnitInstance() }

    @Test
    fun `hecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.HECTO) { n, u -> (n hecto u).toKMixedUnitInstance() }

    @Test
    fun `deca scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECA) { n, u -> (n deca u).toKMixedUnitInstance() }

    @Test
    fun `deci scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECI) { n, u -> (n deci u).toKMixedUnitInstance() }

    @Test
    fun `centi scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.CENTI) { n, u -> (n centi u).toKMixedUnitInstance() }

    @Test
    fun `milli scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MILLI) { n, u -> (n milli u).toKMixedUnitInstance() }

    @Test
    fun `micro scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MICRO) { n, u -> (n micro u).toKMixedUnitInstance() }

    @Test
    fun `nano scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.NANO) { n, u -> (n nano u).toKMixedUnitInstance() }

    @Test
    fun `pico scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PICO) { n, u -> (n pico u).toKMixedUnitInstance() }

    @Test
    fun `femto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.FEMTO) { n, u -> (n femto u).toKMixedUnitInstance() }

    @Test
    fun `atto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ATTO) { n, u -> (n atto u).toKMixedUnitInstance() }

    @Test
    fun `zepto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZEPTO) { n, u -> (n zepto u).toKMixedUnitInstance() }

    @Test
    fun `yocto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOCTO) { n, u -> (n yocto u).toKMixedUnitInstance() }

    @Test
    fun `ronto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONTO) { n, u -> (n ronto u).toKMixedUnitInstance() }

    @Test
    fun `quecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUECTO) { n, u -> (n quecto u).toKMixedUnitInstance() }

    @Test
    fun `KPrefixBuilder is an intermediate type, not yet a KTimeUnitInstance`() {
        val builder = 5 milli KTimeUnit.SECOND

        val mixed = builder.toKMixedUnitInstance()

        assertEquals(0.005, mixed.value, 1e-12)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, 1)), mixed.units)
    }

    @Test
    fun `every time unit combined with every prefix normalizes to seconds and reads back`() {
        for ((applyPrefix, prefix) in prefixFunctions) {
            for ((_, unit) in timeUnitGenerators) {
                // The generic builder keeps the value in the unit's own scale (term tagged `unit^1`),
                // so normalizing to the base unit means multiplying by unit.baseValue - exactly what the
                // library does internally (cf. KMixedUnitInstance.toKTimeUnit). This is checked in pure Double
                // to avoid java.time.Duration's Long-seconds range limit for the extreme prefixes.
                val builder = applyPrefix(5, unit)
                assertEquals(5.0 * prefix.factor, builder.value, (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix (raw scaling) mismatch")

                val normalizedSeconds = builder.value * unit.baseValue
                val expectedSeconds = 5.0 * prefix.factor * unit.baseValue
                assertEquals(expectedSeconds, normalizedSeconds, expectedSeconds.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit normalized-seconds mismatch")

                val base = KMixedUnitInstance(normalizedSeconds, listOf(KUnitTerm(KTimeUnit.BASE, 1)))
                assertEquals(5.0 * prefix.factor, base.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit read-back mismatch")
            }
        }
    }

    @Test
    fun `representable prefixes round trip through the Duration-backed wrapper`() {
        // The Duration-backed KTimeUnitInstance can only faithfully represent magnitudes within roughly
        // [1 ns, Long.MAX seconds]. For those, construction + back-conversion round trips exactly.
        val representable = listOf(
            KUnitPrefix.KILO to { n: Number, u: KTimeUnit -> (n kilo u).toKMixedUnitInstance().toKTimeUnit() },
            KUnitPrefix.MILLI to { n: Number, u: KTimeUnit -> (n milli u).toKMixedUnitInstance().toKTimeUnit() },
            KUnitPrefix.MICRO to { n: Number, u: KTimeUnit -> (n micro u).toKMixedUnitInstance().toKTimeUnit() },
            KUnitPrefix.NANO to { n: Number, u: KTimeUnit -> (n nano u).toKMixedUnitInstance().toKTimeUnit() }
        )
        for ((prefix, apply) in representable) {
            val result = apply(5, KTimeUnit.SECOND)
            assertEquals(5.0 * prefix.factor, result.value, (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                "representable prefix $prefix mismatch")
            assertEquals(5.0 * prefix.factor, result.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND) / 1000.0,
                (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9, "representable prefix $prefix read-back mismatch")
        }
    }
}
