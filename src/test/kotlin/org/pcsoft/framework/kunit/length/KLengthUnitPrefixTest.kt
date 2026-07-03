package org.pcsoft.framework.kunit.length

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
 * Applies the generic, root-level prefix `infix` function for \[prefix] and converts the resulting
 * [org.pcsoft.framework.kunit.KPrefixBuilder] all the way to a [KLengthUnitInstance].
 */
private val prefixFunctions: List<Pair<(Number, KLengthUnit) -> KLengthUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: KLengthUnit -> (n quetta u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.QUETTA,
    ({ n: Number, u: KLengthUnit -> (n ronna u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.RONNA,
    ({ n: Number, u: KLengthUnit -> (n yotta u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.YOTTA,
    ({ n: Number, u: KLengthUnit -> (n zetta u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.ZETTA,
    ({ n: Number, u: KLengthUnit -> (n exa u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.EXA,
    ({ n: Number, u: KLengthUnit -> (n peta u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.PETA,
    ({ n: Number, u: KLengthUnit -> (n tera u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.TERA,
    ({ n: Number, u: KLengthUnit -> (n giga u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.GIGA,
    ({ n: Number, u: KLengthUnit -> (n mega u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.MEGA,
    ({ n: Number, u: KLengthUnit -> (n kilo u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.KILO,
    ({ n: Number, u: KLengthUnit -> (n hecto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.HECTO,
    ({ n: Number, u: KLengthUnit -> (n deca u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.DECA,
    ({ n: Number, u: KLengthUnit -> (n deci u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.DECI,
    ({ n: Number, u: KLengthUnit -> (n centi u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.CENTI,
    ({ n: Number, u: KLengthUnit -> (n milli u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.MILLI,
    ({ n: Number, u: KLengthUnit -> (n micro u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.MICRO,
    ({ n: Number, u: KLengthUnit -> (n nano u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.NANO,
    ({ n: Number, u: KLengthUnit -> (n pico u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.PICO,
    ({ n: Number, u: KLengthUnit -> (n femto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.FEMTO,
    ({ n: Number, u: KLengthUnit -> (n atto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.ATTO,
    ({ n: Number, u: KLengthUnit -> (n zepto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.ZEPTO,
    ({ n: Number, u: KLengthUnit -> (n yocto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.YOCTO,
    ({ n: Number, u: KLengthUnit -> (n ronto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.RONTO,
    ({ n: Number, u: KLengthUnit -> (n quecto u).toKMixedUnitInstance().toKLengthUnit() }) to KUnitPrefix.QUECTO
)

class KLengthUnitPrefixTest {

    /** Verifies that applying [prefix] via [apply] to 5 meters matches the equivalent unprefixed calculation. */
    private fun assertPrefixScales(prefix: KUnitPrefix, apply: (Number, KLengthUnit) -> KLengthUnitInstance) {
        val expected = (5.0 * prefix.factor).meters().value
        val actual = apply(5, KLengthUnit.METER).value
        assertEquals(expected, actual, expected.coerceAtLeast(1.0) * 1e-9, "prefix $prefix mismatch")
    }

    @Test
    fun `quetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUETTA) { n, u -> (n quetta u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `ronna scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONNA) { n, u -> (n ronna u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `yotta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOTTA) { n, u -> (n yotta u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `zetta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZETTA) { n, u -> (n zetta u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `exa scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.EXA) { n, u -> (n exa u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `peta scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PETA) { n, u -> (n peta u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `tera scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.TERA) { n, u -> (n tera u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `giga scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.GIGA) { n, u -> (n giga u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `mega scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MEGA) { n, u -> (n mega u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `kilo scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.KILO) { n, u -> (n kilo u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `hecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.HECTO) { n, u -> (n hecto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `deca scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECA) { n, u -> (n deca u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `deci scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.DECI) { n, u -> (n deci u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `centi scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.CENTI) { n, u -> (n centi u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `milli scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MILLI) { n, u -> (n milli u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `micro scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.MICRO) { n, u -> (n micro u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `nano scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.NANO) { n, u -> (n nano u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `pico scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.PICO) { n, u -> (n pico u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `femto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.FEMTO) { n, u -> (n femto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `atto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ATTO) { n, u -> (n atto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `zepto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.ZEPTO) { n, u -> (n zepto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `yocto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.YOCTO) { n, u -> (n yocto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `ronto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.RONTO) { n, u -> (n ronto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `quecto scales against the equivalent unprefixed calculation`() = assertPrefixScales(KUnitPrefix.QUECTO) { n, u -> (n quecto u).toKMixedUnitInstance().toKLengthUnit() }

    @Test
    fun `KPrefixBuilder is an intermediate type, not yet a KLengthUnitInstance`() {
        val builder = 5 kilo KLengthUnit.METER

        val mixed = builder.toKMixedUnitInstance()

        assertEquals(5000.0, mixed.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KLengthUnit.METER, 1)), mixed.units)
    }

    @Test
    fun `every length unit combined with every prefix round trips`() {
        for ((applyPrefix, prefix) in prefixFunctions) {
            for ((_, unit) in lengthUnitGenerators) {
                val result = applyPrefix(5, unit)
                val expectedBase = 5.0 * prefix.factor * unit.baseValue

                assertEquals(expectedBase, result.value, expectedBase.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit mismatch")
                assertEquals(5.0 * prefix.factor, result.valueAs(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit valueAs mismatch")
            }
        }
    }

    @Test
    fun `every derived unit combined with every prefix round trips`() {
        for (prefix in KUnitPrefix.entries) {
            for ((_, derived) in lengthDerivedUnitGenerators) {
                val scaled = prefix with derived
                // A KMixedUnitInstance whose value is exactly 1 scaled-derived-unit, expressed in the base unit.
                val instance = KMixedUnitInstance(scaled.baseValue, listOf(KUnitTerm(KLengthUnit.BASE, derived.exponent)))

                val convertedBack = instance.valueAs(scaled)

                assertEquals(1.0, convertedBack, 1e-6, "prefix $prefix with derived unit $derived mismatch")
            }
        }
    }
}
