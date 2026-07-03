package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitInstance
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
 * The prefix × unit matrix is verified at the [KUnitInstance] (Double) level, which is where prefix
 * scaling actually happens (the root-level prefix `infix` functions produce a `KPrefixBuilder` ->
 * [KUnitInstance]). It is intentionally not routed through [KTimeUnitInstance] for every combination,
 * because that wrapper is backed by [java.time.Duration] whose whole-seconds part is a `Long` and thus
 * cannot represent the extreme prefixes (e.g. `5 quetta seconds` = 5e30 s far exceeds `Long.MAX`). The
 * lossless round trip through the Duration wrapper is exercised separately for representable magnitudes
 * (see [KTimeUnitInstanceTest]).
 */
private val prefixFunctions: List<Pair<(Number, KTimeUnit) -> KUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KTimeUnit -> (n quetta u).toKUnitInstance() }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KTimeUnit -> (n ronna u).toKUnitInstance() }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KTimeUnit -> (n yotta u).toKUnitInstance() }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KTimeUnit -> (n zetta u).toKUnitInstance() }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KTimeUnit -> (n exa u).toKUnitInstance() }) to KUnitPrefix.EXA,
    ({ n: Number, u: KTimeUnit -> (n peta u).toKUnitInstance() }) to KUnitPrefix.PETA,
    ({ n: Number, u: KTimeUnit -> (n tera u).toKUnitInstance() }) to KUnitPrefix.TERA,
    ({ n: Number, u: KTimeUnit -> (n giga u).toKUnitInstance() }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KTimeUnit -> (n mega u).toKUnitInstance() }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KTimeUnit -> (n kilo u).toKUnitInstance() }) to KUnitPrefix.KILO,
    ({ n: Number, u: KTimeUnit -> (n hecto u).toKUnitInstance() }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KTimeUnit -> (n deca u).toKUnitInstance() }) to KUnitPrefix.DECA,
    ({ n: Number, u: KTimeUnit -> (n deci u).toKUnitInstance() }) to KUnitPrefix.DECI,
    ({ n: Number, u: KTimeUnit -> (n centi u).toKUnitInstance() }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KTimeUnit -> (n milli u).toKUnitInstance() }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KTimeUnit -> (n micro u).toKUnitInstance() }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KTimeUnit -> (n nano u).toKUnitInstance() }) to KUnitPrefix.NANO,
    ({ n: Number, u: KTimeUnit -> (n pico u).toKUnitInstance() }) to KUnitPrefix.PICO,
    ({ n: Number, u: KTimeUnit -> (n femto u).toKUnitInstance() }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KTimeUnit -> (n atto u).toKUnitInstance() }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KTimeUnit -> (n zepto u).toKUnitInstance() }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KTimeUnit -> (n yocto u).toKUnitInstance() }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KTimeUnit -> (n ronto u).toKUnitInstance() }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KTimeUnit -> (n quecto u).toKUnitInstance() }) to KUnitPrefix.QUECTO
)

class KTimeUnitPrefixTest {

    /** Verifies that applying [prefix] via [apply] to 5 seconds matches the equivalent unprefixed value. */
    private fun assertPrefixScales(prefix: KUnitPrefix, apply: (Number, KTimeUnit) -> KUnitInstance) {
        val expected = 5.0 * prefix.factor // seconds (SECOND.baseValue == 1.0)
        val actual = apply(5, KTimeUnit.SECOND).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @Test
    fun `quetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUETTA) { n, u -> (n quetta u).toKUnitInstance() }

    @Test
    fun `ronna scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONNA) { n, u -> (n ronna u).toKUnitInstance() }

    @Test
    fun `yotta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOTTA) { n, u -> (n yotta u).toKUnitInstance() }

    @Test
    fun `zetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZETTA) { n, u -> (n zetta u).toKUnitInstance() }

    @Test
    fun `exa scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.EXA) { n, u -> (n exa u).toKUnitInstance() }

    @Test
    fun `peta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PETA) { n, u -> (n peta u).toKUnitInstance() }

    @Test
    fun `tera scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.TERA) { n, u -> (n tera u).toKUnitInstance() }

    @Test
    fun `giga scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.GIGA) { n, u -> (n giga u).toKUnitInstance() }

    @Test
    fun `mega scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MEGA) { n, u -> (n mega u).toKUnitInstance() }

    @Test
    fun `kilo scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.KILO) { n, u -> (n kilo u).toKUnitInstance() }

    @Test
    fun `hecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.HECTO) { n, u -> (n hecto u).toKUnitInstance() }

    @Test
    fun `deca scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECA) { n, u -> (n deca u).toKUnitInstance() }

    @Test
    fun `deci scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECI) { n, u -> (n deci u).toKUnitInstance() }

    @Test
    fun `centi scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.CENTI) { n, u -> (n centi u).toKUnitInstance() }

    @Test
    fun `milli scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MILLI) { n, u -> (n milli u).toKUnitInstance() }

    @Test
    fun `micro scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MICRO) { n, u -> (n micro u).toKUnitInstance() }

    @Test
    fun `nano scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.NANO) { n, u -> (n nano u).toKUnitInstance() }

    @Test
    fun `pico scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PICO) { n, u -> (n pico u).toKUnitInstance() }

    @Test
    fun `femto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.FEMTO) { n, u -> (n femto u).toKUnitInstance() }

    @Test
    fun `atto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ATTO) { n, u -> (n atto u).toKUnitInstance() }

    @Test
    fun `zepto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZEPTO) { n, u -> (n zepto u).toKUnitInstance() }

    @Test
    fun `yocto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOCTO) { n, u -> (n yocto u).toKUnitInstance() }

    @Test
    fun `ronto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONTO) { n, u -> (n ronto u).toKUnitInstance() }

    @Test
    fun `quecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUECTO) { n, u -> (n quecto u).toKUnitInstance() }

    @Test
    fun `KPrefixBuilder is an intermediate type, not yet a KTimeUnitInstance`() {
        val builder = 5 milli KTimeUnit.SECOND

        val mixed = builder.toKUnitInstance()

        assertEquals(0.005, mixed.value, 1e-12)
        assertEquals(listOf(KUnitTerm(KTimeUnit.SECOND, 1)), mixed.units)
    }

    @Test
    fun `every time unit combined with every prefix normalizes to seconds and reads back`() {
        for ((applyPrefix, prefix) in prefixFunctions) {
            for ((_, unit) in timeUnitGenerators) {
                // The generic builder keeps the value in the unit's own scale (term tagged `unit^1`),
                // so normalizing to the base unit means multiplying by unit.baseValue - exactly what the
                // library does internally (cf. KUnitInstance.toKTimeUnit). This is checked in pure Double
                // to avoid java.time.Duration's Long-seconds range limit for the extreme prefixes.
                val builder = applyPrefix(5, unit)
                assertEquals(5.0 * prefix.factor, builder.value, (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix (raw scaling) mismatch")

                val normalizedSeconds = builder.value * unit.baseValue
                val expectedSeconds = 5.0 * prefix.factor * unit.baseValue
                assertEquals(expectedSeconds, normalizedSeconds, expectedSeconds.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit normalized-seconds mismatch")

                val base = KUnitInstance(normalizedSeconds, listOf(KUnitTerm(KTimeUnit.BASE, 1)))
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
            KUnitPrefix.KILO to { n: Number, u: KTimeUnit -> (n kilo u).toKUnitInstance().toKTimeUnit() },
            KUnitPrefix.MILLI to { n: Number, u: KTimeUnit -> (n milli u).toKUnitInstance().toKTimeUnit() },
            KUnitPrefix.MICRO to { n: Number, u: KTimeUnit -> (n micro u).toKUnitInstance().toKTimeUnit() },
            KUnitPrefix.NANO to { n: Number, u: KTimeUnit -> (n nano u).toKUnitInstance().toKTimeUnit() }
        )
        for ((prefix, apply) in representable) {
            val result = apply(5, KTimeUnit.SECOND)
            assertEquals(5.0 * prefix.factor, result.value, (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                "representable prefix $prefix mismatch")
            assertEquals(5.0 * prefix.factor, result.valueIn(KUnitPrefix.MILLI with KTimeUnit.SECOND) / 1000.0,
                (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9, "representable prefix $prefix read-back mismatch")
        }
    }
}
