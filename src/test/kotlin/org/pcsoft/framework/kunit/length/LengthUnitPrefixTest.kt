package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals

private val prefixFunctions: List<Pair<(Number, LengthUnit) -> LengthUnitInstance, KUnitPrefix>> = listOf(
    ({ n: Number, u: LengthUnit -> n kilo u }) to KUnitPrefix.KILO,
    ({ n: Number, u: LengthUnit -> n hecto u }) to KUnitPrefix.HECTO,
    ({ n: Number, u: LengthUnit -> n deca u }) to KUnitPrefix.DECA,
    ({ n: Number, u: LengthUnit -> n deci u }) to KUnitPrefix.DECI,
    ({ n: Number, u: LengthUnit -> n centi u }) to KUnitPrefix.CENTI,
    ({ n: Number, u: LengthUnit -> n milli u }) to KUnitPrefix.MILLI
)

private val derivedUnits = listOf(
    LengthDerivedUnit.ARE, LengthDerivedUnit.HECTARE, LengthDerivedUnit.ACRE,
    LengthDerivedUnit.LITER, LengthDerivedUnit.US_GALLON, LengthDerivedUnit.IMPERIAL_GALLON,
    LengthDerivedUnit.US_FLUID_OUNCE, LengthDerivedUnit.OIL_BARREL
)

class LengthUnitPrefixTest {

    @Test
    fun `kilo scales by 1000 against the equivalent unprefixed calculation`() {
        assertEquals(5000.meters().value, (5 kilo LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `hecto scales by 100 against the equivalent unprefixed calculation`() {
        assertEquals(500.meters().value, (5 hecto LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `deca scales by 10 against the equivalent unprefixed calculation`() {
        assertEquals(50.meters().value, (5 deca LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `deci scales by 0-1 against the equivalent unprefixed calculation`() {
        assertEquals(0.5.meters().value, (5 deci LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `centi scales by 0-01 against the equivalent unprefixed calculation`() {
        assertEquals(0.05.meters().value, (5 centi LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `milli scales by 0-001 against the equivalent unprefixed calculation`() {
        assertEquals(0.005.meters().value, (5 milli LengthUnit.METER).value, 1e-9)
    }

    @Test
    fun `every length unit combined with every prefix round trips`() {
        for ((applyPrefix, prefix) in prefixFunctions) {
            for ((_, unit) in lengthUnitGenerators) {
                val result = applyPrefix(5, unit)
                val expectedBase = 5.0 * prefix.factor * unit.baseValue

                assertEquals(expectedBase, result.value, expectedBase.coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit mismatch")
                assertEquals(5.0 * prefix.factor, result.valueIn(unit), (5.0 * prefix.factor).coerceAtLeast(1.0) * 1e-9,
                    "prefix $prefix with unit $unit valueIn mismatch")
            }
        }
    }

    @Test
    fun `every derived unit combined with every prefix round trips`() {
        for (prefix in KUnitPrefix.entries) {
            for (derived in derivedUnits) {
                val scaled = prefix with derived
                // A KUnitInstance whose value is exactly 1 scaled-derived-unit, expressed in the base unit.
                val instance = KUnitInstance(scaled.baseValue, listOf(KUnitTerm(LengthUnit.BASE, derived.exponent)))

                val convertedBack = instance.valueAs(scaled)

                assertEquals(1.0, convertedBack, 1e-6, "prefix $prefix with derived unit $derived mismatch")
            }
        }
    }
}
