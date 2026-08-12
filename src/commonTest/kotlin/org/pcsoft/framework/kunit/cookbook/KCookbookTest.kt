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

package org.pcsoft.framework.kunit.cookbook

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.electric.capacitance.*
import org.pcsoft.framework.kunit.electric.charge.*
import org.pcsoft.framework.kunit.electric.chargedensity.*
import org.pcsoft.framework.kunit.electric.conductance.*
import org.pcsoft.framework.kunit.electric.conductivity.*
import org.pcsoft.framework.kunit.electric.current.*
import org.pcsoft.framework.kunit.electric.currentdensity.*
import org.pcsoft.framework.kunit.electric.dipolemoment.*
import org.pcsoft.framework.kunit.electric.fieldstrength.*
import org.pcsoft.framework.kunit.electric.fluxdensity.*
import org.pcsoft.framework.kunit.electric.inductance.*
import org.pcsoft.framework.kunit.electric.linearchargedensity.*
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*
import org.pcsoft.framework.kunit.electric.magneticflux.*
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*
import org.pcsoft.framework.kunit.electric.mobility.*
import org.pcsoft.framework.kunit.electric.permeability.*
import org.pcsoft.framework.kunit.electric.permittivity.*
import org.pcsoft.framework.kunit.electric.reluctance.*
import org.pcsoft.framework.kunit.electric.resistance.*
import org.pcsoft.framework.kunit.electric.resistivity.*
import org.pcsoft.framework.kunit.electric.voltage.*
import org.pcsoft.framework.kunit.it.datarate.*
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.it.storagedensity.*
import org.pcsoft.framework.kunit.kinematic.acceleration.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.speed.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.kinematic.volumeflow.*
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*
import org.pcsoft.framework.kunit.mechanic.areadensity.*
import org.pcsoft.framework.kunit.mechanic.density.*
import org.pcsoft.framework.kunit.mechanic.force.*
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.mechanic.massflow.*
import org.pcsoft.framework.kunit.mechanic.momentum.*
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.insulance.*
import org.pcsoft.framework.kunit.thermo.molarenergy.*
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.molarmass.*
import org.pcsoft.framework.kunit.thermo.molarvolume.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

/**
 * Executable proof of the cross-field documentation page `docs/docs/cookbook.md`: every formula listed
 * there is evaluated here and checked for the **typed** result and the **numeric** value.
 *
 * The expected values are derived from physics (not read back from the framework), so a regression in
 * any operator, factor or normal form breaks this test rather than silently changing the documentation's
 * promises.
 */
class KCookbookTest {

    // ---------------------------------------------------------------- §1 Kinematics

    /** `v = s / t`, `s = v · t`, `t = s / v` and the readings of a speed. */
    @Test
    fun `speed distance and time`() {
        val v = (120 of kilo.meters) / (1.5 of hours)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(80.0, v into kilo.meters / hours, 1e-9)
        assertEquals(120_000.0 / 5400.0, v into meters / seconds, 1e-9)

        val s = v * (3 of hours)
        val sCommuted = (3 of hours) * v
        assertIs<KLengthUnitInstance>(s)
        assertIs<KLengthUnitInstance>(sCommuted)
        assertEquals(240.0, s into kilo.meters, 1e-9)
        assertEquals(240.0, sCommuted into kilo.meters, 1e-9)

        val t = (240 of kilo.meters) / v
        assertIs<KTimeUnitInstance>(t)
        assertEquals(3.0, t into hours, 1e-9)
    }

    /** `v = s · f` - a circumference times a rotation rate is a speed. */
    @Test
    fun `speed from length times frequency`() {
        val v = (2.5 of meters) * (80 of rpm)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(2.5 * 80.0 / 60.0, v into meters / seconds, 1e-9)
    }

    /** The named speed readings: knots, Mach and the speed of light. */
    @Test
    fun `speed readings`() {
        val v = 100 of kilo.meters / hours
        assertEquals(100_000.0 / 3600.0 / KSpeedUnit.KNOT.baseValue, v into knots, 1e-6)
        assertEquals(100_000.0 / 3600.0 / KSpeedUnit.MACH.baseValue, v into mach, 1e-6)
        assertEquals(100_000.0 / 3600.0 / KSpeedUnit.LIGHT_SPEED.baseValue, v into speedOfLight, 1e-15)
    }

    /** Same-group `+`, `-` and scaling by a plain number, with automatic conversion. */
    @Test
    fun `distance and time arithmetic`() {
        assertEquals(5.0 + 1.609344 * 3.0, ((5 of kilo.meters) + (3 of miles)) into kilo.meters, 1e-9)
        assertEquals(4.2, ((5 of kilo.meters) - (800 of meters)) into kilo.meters, 1e-9)
        assertEquals(30.0, ((120 of kilo.meters) / 4) into kilo.meters, 1e-9)
        assertEquals(5445.0, ((90 of minutes) + (45 of seconds)) into seconds, 1e-9)
    }

    /** The average speed of a two-leg trip: `v̄ = Σs / Σt`. */
    @Test
    fun `average speed of a trip`() {
        val v = ((120 of kilo.meters) + (30 of kilo.meters)) / ((1.5 of hours) + (0.5 of hours))
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(75.0, v into kilo.meters / hours, 1e-9)
    }

    /** `a = Δv / t`, `v = a · t`, `t = Δv / a`. */
    @Test
    fun `acceleration formulas`() {
        val a = ((10 of meters) / (1 of seconds)) / (2 of seconds)
        assertIs<KAccelerationUnitInstance>(a)
        assertEquals(5.0, a into meters / (seconds pow 2), 1e-9)

        val sprint = (100 of kilo.meters / hours) / (4.6 of seconds)
        assertIs<KAccelerationUnitInstance>(sprint)
        assertEquals(100_000.0 / 3600.0 / 4.6, sprint into meters / (seconds pow 2), 1e-9)

        val v = a * (2 of seconds)
        val vCommuted = (2 of seconds) * a
        assertIs<KSpeedUnitInstance>(v)
        assertIs<KSpeedUnitInstance>(vCommuted)
        assertEquals(10.0, v into meters / seconds, 1e-9)
        assertEquals(10.0, vCommuted into meters / seconds, 1e-9)

        val t = (30 of meters / seconds) / (3 of meters / (seconds pow 2)).toAcceleration()
        assertIs<KTimeUnitInstance>(t)
        assertEquals(10.0, t into seconds, 1e-9)
    }

    /** Gravity: the `g` and `Gal` readings and free fall over 3 s. */
    @Test
    fun `gravity readings and free fall`() {
        val a = 5 of meters / (seconds pow 2)
        assertEquals(5.0 / 9.80665, a into standardGravities, 1e-9)
        assertEquals(500.0, a into gals, 1e-9)

        val v = (1 of standardGravities) * (3 of seconds)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(9.80665 * 3.0, v into meters / seconds, 1e-9)

        val s = ((1 of standardGravities) * (3 of seconds)) * (3 of seconds) / 2
        assertIs<KLengthUnitInstance>(s)
        assertEquals(0.5 * 9.80665 * 9.0, s into meters, 1e-9)
    }

    /** `f = 1 / T`, `T = 1 / f` and the rate readings. */
    @Test
    fun `frequency and period`() {
        val f = 1 / (0.02 of seconds)
        assertIs<KFrequencyUnitInstance>(f)
        assertEquals(50.0, f into hertz, 1e-9)

        val t = 1 / (50 of hertz)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(0.02, t into seconds, 1e-9)

        assertEquals(50.0, (50_000 of hertz) into kilo.hertz, 1e-9)
        assertEquals(3000.0, (50 of hertz) into rpm, 1e-9)
        assertEquals(72.0, (1.2 of hertz) into bpm, 1e-9)
        assertEquals(60.0, (60 of fps) into hertz, 1e-9)
    }

    /** Circumferential speed from a rotation rate: `v = π · d · f`. */
    @Test
    fun `circumferential speed`() {
        val v = (PI * 0.7 of meters) * (900 of rpm)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(PI * 0.7 * 15.0, v into meters / seconds, 1e-9)
    }

    /** `q̇ = V / t`, `V = q̇ · t`, `t = V / q̇` and the flow readings. */
    @Test
    fun `volumetric flow`() {
        val q = (600 of liters) / (2 of minutes)
        assertIs<KVolumeFlowUnitInstance>(q)
        assertEquals(300.0, q into litersPerMinute, 1e-9)
        assertEquals(5.0, q into litersPerSecond, 1e-9)
        assertEquals(18.0, q into cubicMetersPerHour, 1e-9)
        assertEquals(0.005 / KVolumeFlowUnit.US_GALLON_PER_MINUTE.baseValue, q into usGallonsPerMinute, 1e-6)

        val v = q * (15 of minutes)
        val vCommuted = (15 of minutes) * q
        assertIs<KVolumeUnitInstance>(v)
        assertIs<KVolumeUnitInstance>(vCommuted)
        assertEquals(4500.0, v into liters, 1e-9)
        assertEquals(4500.0, vCommuted into liters, 1e-9)

        val t = (1000 of liters) / q
        assertIs<KTimeUnitInstance>(t)
        assertEquals(200.0, t into seconds, 1e-9)
    }

    // ---------------------------------------------------------------- §2 Mechanics

    /** `F = m · a`, `a = F / m`, `m = F / a` including the commutative form. */
    @Test
    fun `newtons law`() {
        val f = (1200 of kilo.grams) * (2.5 of meters / (seconds pow 2)).toAcceleration()
        val fCommuted = (2.5 of meters / (seconds pow 2)).toAcceleration() * (1200 of kilo.grams)
        assertIs<KForceUnitInstance>(f)
        assertIs<KForceUnitInstance>(fCommuted)
        assertEquals(3000.0, f into newtons, 1e-9)
        assertEquals(3000.0, fCommuted into newtons, 1e-9)

        val a = (3000 of newtons) / (1200 of kilo.grams)
        assertIs<KAccelerationUnitInstance>(a)
        assertEquals(2.5, a into meters / (seconds pow 2), 1e-9)

        val m = (3000 of newtons) / (2.5 of meters / (seconds pow 2)).toAcceleration()
        assertIs<KMassUnitInstance>(m)
        assertEquals(1200.0, m into kilo.grams, 1e-9)
    }

    /** Weight force and the alternative force readings. */
    @Test
    fun `weight force and force readings`() {
        val g = (75 of kilo.grams) * (1 of standardGravities)
        assertIs<KForceUnitInstance>(g)
        assertEquals(75.0 * 9.80665, g into newtons, 1e-9)

        val f = 1000 of newtons
        assertEquals(1.0, f into kilo.newtons, 1e-9)
        assertEquals(1000.0 / KForceUnit.POUND_FORCE.baseValue, f into poundsForce, 1e-9)
        assertEquals(1e8, f into dynes, 1e-3)
        assertEquals(1000.0 / KForceUnit.POND.baseValue, f into ponds, 1e-9)
        assertEquals(2300.0, ((300 of newtons) + (2 of kilo.newtons)) into newtons, 1e-9)
    }

    /** `W = F · s` (both orders), potential energy and the energy readings. */
    @Test
    fun `work and energy`() {
        val w = (500 of newtons) * (12 of meters)
        val wCommuted = (12 of meters) * (500 of newtons)
        assertIs<KEnergyUnitInstance>(w)
        assertIs<KEnergyUnitInstance>(wCommuted)
        assertEquals(6000.0, w into joules, 1e-9)
        assertEquals(6000.0, wCommuted into joules, 1e-9)

        val ePot = ((80 of kilo.grams) * (1 of standardGravities)) * (10 of meters)
        assertIs<KEnergyUnitInstance>(ePot)
        assertEquals(80.0 * 9.80665 * 10.0, ePot into joules, 1e-6)

        val e = 3.6 of mega.joules
        assertEquals(1.0, e into kilo.watts * hours, 1e-9)
        assertEquals(3.6e6 / (1000.0 * KEnergyUnit.CALORIE.baseValue), e into kilo.calories, 1e-6)
        assertEquals(3.6e6 / KEnergyUnit.ELECTRON_VOLT.baseValue, e into electronVolts, 1e10)
        assertEquals(3.6e6 / KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue, e into britishThermalUnits, 1e-6)
    }

    /** `P = W / t`, `W = P · t`, `t = W / P`, `P = F · v` and their inversions. */
    @Test
    fun `power formulas`() {
        val p = (6000 of joules) / (12 of seconds)
        assertIs<KPowerUnitInstance>(p)
        assertEquals(500.0, p into watts, 1e-9)

        val w = (2 of kilo.watts) * (3 of hours)
        assertIs<KEnergyUnitInstance>(w)
        assertEquals(21.6, w into mega.joules, 1e-9)

        val t = (21.6 of mega.joules) / (2 of kilo.watts)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(3.0, t into hours, 1e-9)

        val mechanical = (400 of newtons) * (25 of meters / seconds)
        assertIs<KPowerUnitInstance>(mechanical)
        assertEquals(10_000.0, mechanical into watts, 1e-9)

        val force = (10 of kilo.watts) / (25 of meters / seconds)
        assertIs<KForceUnitInstance>(force)
        assertEquals(400.0, force into newtons, 1e-9)

        val speed = (10 of kilo.watts) / (400 of newtons)
        assertIs<KSpeedUnitInstance>(speed)
        assertEquals(25.0, speed into meters / seconds, 1e-9)

        val perCycle = (60 of watts) / (50 of hertz)
        assertIs<KEnergyUnitInstance>(perCycle)
        assertEquals(1.2, perCycle into joules, 1e-9)
    }

    /** The horsepower readings of a power. */
    @Test
    fun `power readings`() {
        val p = 100 of kilo.watts
        assertEquals(100_000.0 / KPowerUnit.METRIC_HORSE_POWER.baseValue, p into metricHorsePowers, 1e-6)
        assertEquals(100_000.0 / KPowerUnit.MECHANICAL_HORSE_POWER.baseValue, p into mechanicalHorsePowers, 1e-6)
    }

    /** `p = m · v`, `J = F · t` and every inversion of the momentum group. */
    @Test
    fun `momentum and impulse`() {
        val p = (1200 of kilo.grams) * (25 of meters / seconds)
        val pCommuted = (25 of meters / seconds) * (1200 of kilo.grams)
        assertIs<KMomentumUnitInstance>(p)
        assertIs<KMomentumUnitInstance>(pCommuted)
        assertEquals(30_000.0, p into kilogramMetersPerSecond, 1e-9)
        assertEquals(30_000.0, pCommuted into kilogramMetersPerSecond, 1e-9)

        val v = (30_000 of kilogramMetersPerSecond) / (1200 of kilo.grams)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(25.0, v into meters / seconds, 1e-9)

        val m = (30_000 of kilogramMetersPerSecond) / (25 of meters / seconds)
        assertIs<KMassUnitInstance>(m)
        assertEquals(1200.0, m into kilo.grams, 1e-9)

        val impulse = (2 of kilo.newtons) * (0.15 of seconds)
        val impulseCommuted = (0.15 of seconds) * (2 of kilo.newtons)
        assertIs<KMomentumUnitInstance>(impulse)
        assertIs<KMomentumUnitInstance>(impulseCommuted)
        assertEquals(300.0, impulse into newtonSeconds, 1e-9)
        assertEquals(300.0, impulseCommuted into newtonSeconds, 1e-9)

        val force = (300 of newtonSeconds) / (0.15 of seconds)
        assertIs<KForceUnitInstance>(force)
        assertEquals(2000.0, force into newtons, 1e-9)

        val time = (300 of newtonSeconds) / (2 of kilo.newtons)
        assertIs<KTimeUnitInstance>(time)
        assertEquals(0.15, time into seconds, 1e-9)

        assertEquals(
            300.0 / KMomentumUnit.POUND_FEET_PER_SECOND.baseValue,
            (300 of newtonSeconds) into poundFeetPerSecond,
            1e-6,
        )
    }

    /** `p = F / A`, `F = p · A`, `A = F / p` and the pressure readings. */
    @Test
    fun `pressure formulas`() {
        val p = (2000 of newtons) / ((0.1 of meters) * (0.05 of meters))
        assertIs<KPressureUnitInstance>(p)
        assertEquals(400_000.0, p into pascals, 1e-6)

        val f = (3 of bars) * ((0.2 of meters) * (0.1 of meters))
        assertIs<KForceUnitInstance>(f)
        assertEquals(6000.0, f into newtons, 1e-6)

        val a = (6000 of newtons) / (3 of bars)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(0.02, a into (meters pow 2), 1e-9)

        val reading = 100_000 of pascals
        assertEquals(1.0, reading into bars, 1e-9)
        assertEquals(100_000.0 / KPressureUnit.PSI.baseValue, reading into psis, 1e-6)
        assertEquals(100_000.0 / KPressureUnit.ATMOSPHERE.baseValue, reading into atmospheres, 1e-9)
        assertEquals(100_000.0 / KPressureUnit.TORR.baseValue, reading into torrs, 1e-6)
    }

    /** Hydrostatic pressure `p = ρ · g · h`. */
    @Test
    fun `hydrostatic pressure`() {
        val water = (1000 of kilo.grams) / (1000 of liters)
        val column = water * ((1 of meters) * (1 of meters) * (10 of meters)) // 10 m of water above 1 m²
        val p = (column * (1 of standardGravities)) / ((1 of meters) * (1 of meters))
        assertIs<KPressureUnitInstance>(p)
        assertEquals(1000.0 * 9.80665 * 10.0, p into pascals, 1e-3)
    }

    /** Strain, stress and Hooke's law `σ = E · ε`. */
    @Test
    fun `strain and elastic modulus`() {
        val strain = ((0.6 of milli.meters) / (2 of meters)).toStrain()
        assertIs<KStrainUnitInstance>(strain)
        assertEquals(0.0003, strain into ratio, 1e-12)
        assertEquals(0.03, strain into percent, 1e-12)
        assertEquals(0.3, strain into perMille, 1e-12)
        assertEquals(300.0, strain into microstrain, 1e-9)

        val stress = (50 of kilo.newtons) / ((0.05 of meters) * (0.01 of meters))
        assertIs<KPressureUnitInstance>(stress)
        assertEquals(100.0, stress into mega.pascals, 1e-6)

        val modulus = (100 of mega.pascals) / (0.0005 of ratio)
        assertIs<KPressureUnitInstance>(modulus)
        assertEquals(200.0, modulus into giga.pascals, 1e-9)

        val hooke = (210 of giga.pascals) * (0.0005 of ratio)
        assertIs<KPressureUnitInstance>(hooke)
        assertEquals(105.0, hooke into mega.pascals, 1e-6)
    }

    /** `ω = φ / t`, `φ = ω · t`, `α = Δω / t` and their inversions. */
    @Test
    fun `angular motion`() {
        val w = (1 of turns) / (1 of seconds)
        assertIs<KAngularVelocityUnitInstance>(w)
        assertEquals(60.0, w into revolutionsPerMinute, 1e-9)
        assertEquals(2.0 * PI, w into radians / seconds, 1e-9)

        val phi = (3000 of revolutionsPerMinute) * (2 of seconds)
        assertIs<KAngleUnitInstance>(phi)
        assertEquals(100.0, phi into turns, 1e-9)

        val t = (10 of turns) / (3000 of revolutionsPerMinute)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(0.2, t into seconds, 1e-9)

        val alpha = (3000 of revolutionsPerMinute) / (5 of seconds)
        assertIs<KAngularAccelerationUnitInstance>(alpha)
        assertEquals(100.0 * PI / 5.0, alpha into radiansPerSecondSquared, 1e-9)

        val omega = (2 of radiansPerSecondSquared) * (5 of seconds)
        assertIs<KAngularVelocityUnitInstance>(omega)
        assertEquals(10.0, omega into radians / seconds, 1e-9)

        val rampTime = (10 of radians / seconds) / (2 of radiansPerSecondSquared)
        assertIs<KTimeUnitInstance>(rampTime)
        assertEquals(5.0, rampTime into seconds, 1e-9)
    }

    /** Moment of inertia and angular momentum: `J = m · r²`, `L = J · ω`, `L = p · r`. */
    @Test
    fun `inertia and angular momentum`() {
        val j = (2 of kilo.grams) * ((0.3 of meters) * (0.3 of meters))
        assertIs<KInertiaUnitInstance>(j)
        assertEquals(0.18, j into kilogramMetersSquared, 1e-9)

        val m = (0.18 of kilogramMetersSquared) / ((0.3 of meters) * (0.3 of meters))
        assertIs<KMassUnitInstance>(m)
        assertEquals(2.0, m into kilo.grams, 1e-9)

        val l = (0.18 of kilogramMetersSquared) * (50 of revolutionsPerSecond)
        assertIs<KAngularMomentumUnitInstance>(l)
        assertEquals(0.18 * 100.0 * PI, l into kilogramMetersSquaredPerSecond, 1e-9)

        val inertia = (56 of kilogramMetersSquaredPerSecond) / (50 of revolutionsPerSecond)
        assertIs<KInertiaUnitInstance>(inertia)
        assertEquals(56.0 / (100.0 * PI), inertia into kilogramMetersSquared, 1e-9)

        val omega = (56 of kilogramMetersSquaredPerSecond) / (0.18 of kilogramMetersSquared)
        assertIs<KAngularVelocityUnitInstance>(omega)
        assertEquals(56.0 / 0.18, omega into radians / seconds, 1e-9)

        val fromMomentum = (300 of newtonSeconds) * (0.4 of meters)
        assertIs<KAngularMomentumUnitInstance>(fromMomentum)
        assertEquals(120.0, fromMomentum into kilogramMetersSquaredPerSecond, 1e-9)

        val momentum = (120 of jouleSeconds) / (0.4 of meters)
        assertIs<KMomentumUnitInstance>(momentum)
        assertEquals(300.0, momentum into newtonSeconds, 1e-9)
    }

    /** Torque as a reading of the energy group: `M = F · r`, `M = J · α`, `M = P / ω`, `P = M · ω`. */
    @Test
    fun `torque formulas`() {
        val lever = (100 of newtons) * (2 of meters)
        assertIs<KEnergyUnitInstance>(lever)
        assertEquals(200.0, lever into joules, 1e-9)

        val rotational = (0.18 of kilogramMetersSquared) * (2 of radiansPerSecondSquared)
        assertIs<KEnergyUnitInstance>(rotational)
        assertEquals(0.36, rotational into joules, 1e-9)

        val fromPower = (15 of kilo.watts) / (3000 of revolutionsPerMinute)
        assertIs<KEnergyUnitInstance>(fromPower)
        assertEquals(15_000.0 / (100.0 * PI), fromPower into joules, 1e-9)

        val power = (48 of joules) * (3000 of revolutionsPerMinute)
        assertIs<KPowerUnitInstance>(power)
        assertEquals(48.0 * 100.0 * PI, power into watts, 1e-6)

        val omega = (15 of kilo.watts) / (48 of joules)
        assertIs<KAngularVelocityUnitInstance>(omega)
        assertEquals(15_000.0 / 48.0, omega into radians / seconds, 1e-9)

        val alpha = (48 of joules) / (0.18 of kilogramMetersSquared)
        assertIs<KAngularAccelerationUnitInstance>(alpha)
        assertEquals(48.0 / 0.18, alpha into radiansPerSecondSquared, 1e-9)

        val inertia = (48 of joules) / (2 of radiansPerSecondSquared)
        assertIs<KInertiaUnitInstance>(inertia)
        assertEquals(24.0, inertia into kilogramMetersSquared, 1e-9)
    }

    /** Angles and solid angles, including the readings. */
    @Test
    fun `angles and solid angles`() {
        val omega = (0.5 of radians) * (0.5 of radians)
        assertIs<KSolidAngleUnitInstance>(omega)
        assertEquals(0.25, omega into steradians, 1e-9)

        val angle = (0.25 of steradians) / (0.5 of radians)
        assertIs<KAngleUnitInstance>(angle)
        assertEquals(0.5, angle into radians, 1e-9)

        assertEquals(90.0, (0.25 of turns) into degrees, 1e-9)
        assertEquals(100.0, (0.25 of turns) into gradians, 1e-9)
        assertEquals(0.25 / ((PI / 180.0) * (PI / 180.0)), (0.25 of steradians) into squareDegrees, 1e-6)
    }

    /** Mass density `ρ = m / V` with both inversions and a g/cm³ reading. */
    @Test
    fun `mass density`() {
        val steel = (7850 of kilo.grams) / (1000 of liters)
        assertIs<KDensityUnitInstance>(steel)
        assertEquals(7850.0, steel into kilo.grams / (meters * meters * meters), 1e-9)
        assertEquals(7.85, steel into grams / (centi.meters * centi.meters * centi.meters), 1e-9)

        val m = steel * (250 of liters)
        val mCommuted = (250 of liters) * steel
        assertIs<KMassUnitInstance>(m)
        assertIs<KMassUnitInstance>(mCommuted)
        assertEquals(1962.5, m into kilo.grams, 1e-9)
        assertEquals(1962.5, mCommuted into kilo.grams, 1e-9)

        val v = (1962.5 of kilo.grams) / steel
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(0.25, v into (meters pow 3), 1e-9)
    }

    /** Areal density `ρ_A = m / A`, its inversions and the link to the volume density. */
    @Test
    fun `areal density`() {
        val rho = (25 of kilo.grams) / ((5 of meters) * (1 of meters))
        assertIs<KAreaDensityUnitInstance>(rho)
        assertEquals(5.0, rho into kilo.grams / (meters * meters), 1e-9)

        val m = rho * ((4 of meters) * (3 of meters))
        val mCommuted = ((4 of meters) * (3 of meters)) * rho
        assertIs<KMassUnitInstance>(m)
        assertIs<KMassUnitInstance>(mCommuted)
        assertEquals(60.0, m into kilo.grams, 1e-9)
        assertEquals(60.0, mCommuted into kilo.grams, 1e-9)

        val a = (60 of kilo.grams) / rho
        assertIs<KAreaUnitInstance>(a)
        assertEquals(12.0, a into (meters pow 2), 1e-9)

        val steel = (7850 of kilo.grams) / (1000 of liters)
        val sheet = steel * (2 of milli.meters)
        assertIs<KAreaDensityUnitInstance>(sheet)
        assertEquals(15.7, sheet into kilo.grams / (meters * meters), 1e-9)

        val back = sheet / (2 of milli.meters)
        assertIs<KDensityUnitInstance>(back)
        assertEquals(7850.0, back into kilo.grams / (meters * meters * meters), 1e-6)
    }

    /** Linear density `ρ_l = m / l` with the textile readings tex and denier. */
    @Test
    fun `linear density`() {
        val rho = (90 of grams) / (1000 of meters)
        assertIs<KLinearDensityUnitInstance>(rho)
        assertEquals(0.09, rho into gramsPerMeter, 1e-9)
        assertEquals(9e-5 / KLinearDensityUnit.TEX.baseValue, rho into tex, 1e-6)
        assertEquals(9e-5 / KLinearDensityUnit.DENIER.baseValue, rho into denier, 1e-6)

        val m = (0.09 of gramsPerMeter) * (2500 of meters)
        val mCommuted = (2500 of meters) * (0.09 of gramsPerMeter)
        assertIs<KMassUnitInstance>(m)
        assertIs<KMassUnitInstance>(mCommuted)
        assertEquals(225.0, m into grams, 1e-9)
        assertEquals(225.0, mCommuted into grams, 1e-9)

        val l = (225 of grams) / (0.09 of gramsPerMeter)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(2500.0, l into meters, 1e-9)
    }

    /** Specific volume as the reciprocal of density, in both directions. */
    @Test
    fun `specific volume`() {
        val v = (1000 of liters) / (1000 of kilo.grams)
        assertIs<KSpecificVolumeUnitInstance>(v)
        assertEquals(0.001, v into cubicMetersPerKilogram, 1e-12)

        val steel = (7850 of kilo.grams) / (1000 of liters)
        val reciprocal = 1 / steel
        assertIs<KSpecificVolumeUnitInstance>(reciprocal)
        assertEquals(1.0 / 7850.0, reciprocal into cubicMetersPerKilogram, 1e-12)

        val density = 1 / (0.001 of cubicMetersPerKilogram)
        assertIs<KDensityUnitInstance>(density)
        assertEquals(1000.0, density into kilo.grams / (meters * meters * meters), 1e-6)

        val volume = (0.001 of cubicMetersPerKilogram) * (500 of kilo.grams)
        val volumeCommuted = (500 of kilo.grams) * (0.001 of cubicMetersPerKilogram)
        assertIs<KVolumeUnitInstance>(volume)
        assertIs<KVolumeUnitInstance>(volumeCommuted)
        assertEquals(0.5, volume into (meters pow 3), 1e-9)
        assertEquals(0.5, volumeCommuted into (meters pow 3), 1e-9)

        val mass = (500 of liters) / (0.001 of cubicMetersPerKilogram)
        assertIs<KMassUnitInstance>(mass)
        assertEquals(500.0, mass into kilo.grams, 1e-9)
    }

    /** Mass flow `ṁ = m / t`, `ṁ = ρ · q̇` and every inversion. */
    @Test
    fun `mass flow`() {
        val flow = (180 of kilo.grams) / (1 of hours)
        assertIs<KMassFlowUnitInstance>(flow)
        assertEquals(0.05, flow into kilogramsPerSecond, 1e-9)
        assertEquals(0.18, flow into tonnesPerHour, 1e-9)

        val m = (0.05 of kilogramsPerSecond) * (2 of hours)
        val mCommuted = (2 of hours) * (0.05 of kilogramsPerSecond)
        assertIs<KMassUnitInstance>(m)
        assertIs<KMassUnitInstance>(mCommuted)
        assertEquals(360.0, m into kilo.grams, 1e-9)
        assertEquals(360.0, mCommuted into kilo.grams, 1e-9)

        val t = (360 of kilo.grams) / (0.05 of kilogramsPerSecond)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(2.0, t into hours, 1e-9)

        val water = (1000 of kilo.grams) / (1000 of liters)
        val fromFlow = water * (2 of litersPerSecond)
        val fromFlowCommuted = (2 of litersPerSecond) * water
        assertIs<KMassFlowUnitInstance>(fromFlow)
        assertIs<KMassFlowUnitInstance>(fromFlowCommuted)
        assertEquals(2.0, fromFlow into kilogramsPerSecond, 1e-9)
        assertEquals(2.0, fromFlowCommuted into kilogramsPerSecond, 1e-9)

        val volumeFlow = (2 of kilogramsPerSecond) / water
        assertIs<KVolumeFlowUnitInstance>(volumeFlow)
        assertEquals(2.0, volumeFlow into litersPerSecond, 1e-9)

        val density = (2 of kilogramsPerSecond) / (2 of litersPerSecond)
        assertIs<KDensityUnitInstance>(density)
        assertEquals(1000.0, density into kilo.grams / (meters * meters * meters), 1e-6)
    }

    /** Dynamic and kinematic viscosity: `η = p · t`, `ν = η / ρ` and back. */
    @Test
    fun viscosity() {
        val eta = (1 of pascals) * (0.001 of seconds)
        val etaCommuted = (0.001 of seconds) * (1 of pascals)
        assertIs<KViscosityUnitInstance>(eta)
        assertIs<KViscosityUnitInstance>(etaCommuted)
        assertEquals(0.001, eta into pascalSeconds, 1e-12)
        assertEquals(1.0, eta into centi.poises, 1e-9)

        val water = (1000 of kilo.grams) / (1000 of liters)
        val nu = (0.001 of pascalSeconds) / water
        assertIs<KDiffusivityUnitInstance>(nu)
        assertEquals(1e-6, nu into squareMetersPerSecond, 1e-15)

        val back = (1e-6 of squareMetersPerSecond) * water
        assertIs<KViscosityUnitInstance>(back)
        assertEquals(0.001, back into pascalSeconds, 1e-12)

        val density = (0.001 of pascalSeconds) / (1e-6 of squareMetersPerSecond)
        assertIs<KDensityUnitInstance>(density)
        assertEquals(1000.0, density into kilo.grams / (meters * meters * meters), 1e-6)
    }

    /** Surface tension and stiffness share the line-force group (`N/m`). */
    @Test
    fun `line force surface tension and stiffness`() {
        val sigma = (0.0728 of newtons) / (1 of meters)
        assertIs<KLineForceUnitInstance>(sigma)
        assertEquals(0.0728, sigma into newtonsPerMeter, 1e-12)

        val f = (72.8 of milli.newtons / meters) * (0.05 of meters)
        val fCommuted = (0.05 of meters) * (72.8 of milli.newtons / meters)
        assertIs<KForceUnitInstance>(f)
        assertIs<KForceUnitInstance>(fCommuted)
        assertEquals(0.00364, f into newtons, 1e-12)
        assertEquals(0.00364, fCommuted into newtons, 1e-12)

        val l = (0.00364 of newtons) / (72.8 of milli.newtons / meters)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(0.05, l into meters, 1e-9)

        val work = (0.0728 of newtonsPerMeter) * ((1 of meters) * (0.5 of meters))
        val workCommuted = ((1 of meters) * (0.5 of meters)) * (0.0728 of newtonsPerMeter)
        assertIs<KEnergyUnitInstance>(work)
        assertIs<KEnergyUnitInstance>(workCommuted)
        assertEquals(0.0364, work into joules, 1e-12)
        assertEquals(0.0364, workCommuted into joules, 1e-12)

        val area = (0.0364 of joules) / (0.0728 of newtonsPerMeter)
        assertIs<KAreaUnitInstance>(area)
        assertEquals(0.5, area into (meters pow 2), 1e-9)

        val k = (500 of newtons) / (0.01 of meters)
        assertIs<KLineForceUnitInstance>(k)
        assertEquals(50.0, k into newtonsPerMillimeter, 1e-9)

        val springForce = (50 of newtonsPerMillimeter) * (0.004 of meters)
        assertIs<KForceUnitInstance>(springForce)
        assertEquals(200.0, springForce into newtons, 1e-9)
    }

    // ---------------------------------------------------------------- §3 Electrical engineering

    /** Ohm's law in all three forms, plus the conductance reciprocals. */
    @Test
    fun `ohms law`() {
        val u = (220 of ohms) * (0.05 of amperes)
        val uCommuted = (0.05 of amperes) * (220 of ohms)
        assertIs<KVoltageUnitInstance>(u)
        assertIs<KVoltageUnitInstance>(uCommuted)
        assertEquals(11.0, u into volts, 1e-9)
        assertEquals(11.0, uCommuted into volts, 1e-9)

        val i = (230 of volts) / (46 of ohms)
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(5.0, i into amperes, 1e-9)

        val r = (230 of volts) / (5 of amperes)
        assertIs<KResistanceUnitInstance>(r)
        assertEquals(46.0, r into ohms, 1e-9)

        val g = 1 / (46 of ohms)
        assertIs<KConductanceUnitInstance>(g)
        assertEquals(1.0 / 46.0, g into siemens, 1e-12)

        val backToR = 1 / (0.02 of siemens)
        assertIs<KResistanceUnitInstance>(backToR)
        assertEquals(50.0, backToR into ohms, 1e-9)

        val viaConductance = (0.02 of siemens) * (230 of volts)
        assertIs<KElectricCurrentUnitInstance>(viaConductance)
        assertEquals(4.6, viaConductance into amperes, 1e-9)

        val conductance = (5 of amperes) / (230 of volts)
        assertIs<KConductanceUnitInstance>(conductance)
        assertEquals(5.0 / 230.0, conductance into siemens, 1e-12)

        val voltage = (5 of amperes) / (0.02 of siemens)
        assertIs<KVoltageUnitInstance>(voltage)
        assertEquals(250.0, voltage into volts, 1e-9)
    }

    /** Electrical power `P = U · I` with its inversions and the energy over time. */
    @Test
    fun `electrical power`() {
        val p = (230 of volts) * (5 of amperes)
        assertIs<KPowerUnitInstance>(p)
        assertEquals(1150.0, p into watts, 1e-9)

        val i = (1150 of watts) / (230 of volts)
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(5.0, i into amperes, 1e-9)

        val u = (1150 of watts) / (5 of amperes)
        assertIs<KVoltageUnitInstance>(u)
        assertEquals(230.0, u into volts, 1e-9)

        val w = (1150 of watts) * (2 of hours)
        assertIs<KEnergyUnitInstance>(w)
        assertEquals(2.3, w into kilo.watts * hours, 1e-9)

        assertEquals(1.15, (1150 of voltAmperes) into kilo.voltAmperes, 1e-9)
        assertEquals(11.5, (11_500 of volts) into kilo.volts, 1e-9)
        assertEquals(230_000.0, (230 of volts) into milli.volts, 1e-6)
        assertEquals(4.7, (4700 of ohms) into kilo.ohms, 1e-9)
    }

    /** Charge: `Q = I · t`, `Q = C · U`, `W = Q · U` and every inversion. */
    @Test
    fun `charge and capacitance`() {
        val q = (2 of amperes) * (30 of minutes)
        val qCommuted = (30 of minutes) * (2 of amperes)
        assertIs<KChargeUnitInstance>(q)
        assertIs<KChargeUnitInstance>(qCommuted)
        assertEquals(3600.0, q into coulombs, 1e-9)
        assertEquals(3600.0, qCommuted into coulombs, 1e-9)
        assertEquals(1.0, q into ampereHours, 1e-9)
        assertEquals(1000.0, q into milli.ampereHours, 1e-6)
        assertEquals(3600.0 / KChargeUnit.ELEMENTARY_CHARGE.baseValue, q into elementaryCharges, 1e6)

        val i = (3600 of coulombs) / (30 of minutes)
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(2.0, i into amperes, 1e-9)

        val t = (3600 of coulombs) / (2 of amperes)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(1800.0, t into seconds, 1e-9)

        val pulsed = (1 of milli.coulombs) * (50 of hertz)
        assertIs<KElectricCurrentUnitInstance>(pulsed)
        assertEquals(0.05, pulsed into amperes, 1e-12)

        val perCycle = (0.05 of amperes) / (50 of hertz)
        assertIs<KChargeUnitInstance>(perCycle)
        assertEquals(0.001, perCycle into coulombs, 1e-12)

        val energy = (3600 of coulombs) * (12 of volts)
        assertIs<KEnergyUnitInstance>(energy)
        assertEquals(43_200.0, energy into joules, 1e-9)

        val voltage = (43_200 of joules) / (3600 of coulombs)
        assertIs<KVoltageUnitInstance>(voltage)
        assertEquals(12.0, voltage into volts, 1e-9)

        val c = (0.01 of coulombs) / (10 of volts)
        assertIs<KCapacitanceUnitInstance>(c)
        assertEquals(1000.0, c into micro.farads, 1e-6)

        val charge = (1000 of micro.farads) * (10 of volts)
        assertIs<KChargeUnitInstance>(charge)
        assertEquals(0.01, charge into coulombs, 1e-12)

        val capVoltage = (0.01 of coulombs) / (1000 of micro.farads)
        assertIs<KVoltageUnitInstance>(capVoltage)
        assertEquals(10.0, capVoltage into volts, 1e-9)

        assertEquals(1e-6, (1 of micro.farads) into farads, 1e-15)
    }

    /** The RC time constant `τ = R · C` - a generic mixed unit that reads as seconds. */
    @Test
    fun `rc time constant`() {
        val tau = (10 of kilo.ohms) * (100 of micro.farads)
        assertEquals(1.0, tau into seconds, 1e-9)
    }

    /** Electric field strength `E = U / d`, `F = E · Q` and their inversions. */
    @Test
    fun `electric field strength`() {
        val e = (230 of volts) / (2 of milli.meters)
        assertIs<KElectricFieldStrengthUnitInstance>(e)
        assertEquals(115_000.0, e into voltsPerMeter, 1e-6)

        val u = (115 of kilo.voltsPerMeter) * (2 of milli.meters)
        assertIs<KVoltageUnitInstance>(u)
        assertEquals(230.0, u into volts, 1e-9)

        val d = (230 of volts) / (115 of kilo.voltsPerMeter)
        assertIs<KLengthUnitInstance>(d)
        assertEquals(0.002, d into meters, 1e-12)

        val f = (1000 of voltsPerMeter) * (1 of micro.coulombs)
        assertIs<KForceUnitInstance>(f)
        assertEquals(0.001, f into newtons, 1e-12)

        val field = (0.001 of newtons) / (1 of micro.coulombs)
        assertIs<KElectricFieldStrengthUnitInstance>(field)
        assertEquals(1000.0, field into voltsPerMeter, 1e-9)

        val q = (0.001 of newtons) / (1000 of voltsPerMeter)
        assertIs<KChargeUnitInstance>(q)
        assertEquals(1e-6, q into coulombs, 1e-15)
    }

    /** Current density, charge density and linear charge density with their inversions. */
    @Test
    fun `current and charge densities`() {
        val j = (16 of amperes) / ((1.5 of milli.meters) * (1 of milli.meters))
        assertIs<KCurrentDensityUnitInstance>(j)
        assertEquals(16.0 / 1.5e-6, j into amperes / (meters * meters), 1e-3)

        val i = j * ((2.5 of milli.meters) * (1 of milli.meters))
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(16.0 / 1.5 * 2.5, i into amperes, 1e-9)

        val a = (16 of amperes) / j
        assertIs<KAreaUnitInstance>(a)
        assertEquals(1.5e-6, a into (meters pow 2), 1e-15)

        val rho = (12 of milli.coulombs) / (4 of liters)
        assertIs<KChargeDensityUnitInstance>(rho)
        assertEquals(3.0, rho into coulombs / (meters * meters * meters), 1e-9)

        val q = rho * (2 of liters)
        val qCommuted = (2 of liters) * rho
        assertIs<KChargeUnitInstance>(q)
        assertIs<KChargeUnitInstance>(qCommuted)
        assertEquals(0.006, q into coulombs, 1e-12)
        assertEquals(0.006, qCommuted into coulombs, 1e-12)

        val v = (12 of milli.coulombs) / rho
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(4.0, v into liters, 1e-9)

        val lambda = (1 of micro.coulombs) / (2 of meters)
        assertIs<KLinearChargeDensityUnitInstance>(lambda)
        assertEquals(5e-7, lambda into coulombs / meters, 1e-15)

        val lineCharge = lambda * (5 of meters)
        assertIs<KChargeUnitInstance>(lineCharge)
        assertEquals(2.5e-6, lineCharge into coulombs, 1e-15)

        val length = (1 of micro.coulombs) / lambda
        assertIs<KLengthUnitInstance>(length)
        assertEquals(2.0, length into meters, 1e-9)
    }

    /** Electric flux density `D = Q / A`, `D = ε · E` and the permittivity links. */
    @Test
    fun `electric flux density and permittivity`() {
        val d = (1 of micro.coulombs) / ((1 of meters) * (0.5 of meters))
        assertIs<KElectricFluxDensityUnitInstance>(d)
        assertEquals(2e-6, d into coulombsPerSquareMeter, 1e-15)

        val q = (2 of micro.coulombsPerSquareMeter) * ((1 of meters) * (0.5 of meters))
        assertIs<KChargeUnitInstance>(q)
        assertEquals(1e-6, q into coulombs, 1e-15)

        val a = (1 of micro.coulombs) / (2 of micro.coulombsPerSquareMeter)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(0.5, a into (meters pow 2), 1e-9)

        val flux = (1 of vacuumPermittivity) * (1000 of voltsPerMeter)
        assertIs<KElectricFluxDensityUnitInstance>(flux)
        assertEquals(KPermittivityUnit.VACUUM_PERMITTIVITY * 1000.0, flux into coulombsPerSquareMeter, 1e-18)

        val epsilon = flux / (1000 of voltsPerMeter)
        assertIs<KPermittivityUnitInstance>(epsilon)
        assertEquals(1.0, epsilon into vacuumPermittivity, 1e-9)

        val field = flux / (1 of vacuumPermittivity)
        assertIs<KElectricFieldStrengthUnitInstance>(field)
        assertEquals(1000.0, field into voltsPerMeter, 1e-6)

        val c = (1 of vacuumPermittivity) * (0.05 of meters)
        assertIs<KCapacitanceUnitInstance>(c)
        assertEquals(KPermittivityUnit.VACUUM_PERMITTIVITY * 0.05, c into farads, 1e-18)

        val perLength = (1000 of micro.farads) / (0.05 of meters)
        assertIs<KPermittivityUnitInstance>(perLength)
        assertEquals(0.02, perLength into farads / meters, 1e-9)

        val len = (1000 of micro.farads) / (1 of vacuumPermittivity)
        assertIs<KLengthUnitInstance>(len)
        assertEquals(0.001 / KPermittivityUnit.VACUUM_PERMITTIVITY, len into meters, 1e-3)
    }

    /** Resistivity and conductivity, their reciprocity and the length links. */
    @Test
    fun `resistivity and conductivity`() {
        val rho = (0.017 of ohms) * (1 of meters)
        assertIs<KResistivityUnitInstance>(rho)
        assertEquals(0.017, rho into ohmMeters, 1e-12)

        val r = (17 of nano.ohmMeters) / (0.001 of meters)
        assertIs<KResistanceUnitInstance>(r)
        assertEquals(1.7e-5, r into ohms, 1e-15)

        val l = (17 of nano.ohmMeters) / (0.017 of ohms)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(1e-6, l into meters, 1e-15)

        val sigma = 1 / (17 of nano.ohmMeters)
        assertIs<KConductivityUnitInstance>(sigma)
        assertEquals(1.0 / 17e-9 / 1e6, sigma into megasiemensPerMeter, 1e-6)

        val back = 1 / (58 of megasiemensPerMeter)
        assertIs<KResistivityUnitInstance>(back)
        assertEquals(1.0 / 58e6, back into ohmMeters, 1e-15)

        val g = (58 of megasiemensPerMeter) * (0.001 of meters)
        assertIs<KConductanceUnitInstance>(g)
        assertEquals(58_000.0, g into siemens, 1e-6)

        val conductivity = (0.02 of siemens) / (0.001 of meters)
        assertIs<KConductivityUnitInstance>(conductivity)
        assertEquals(20.0, conductivity into siemens / meters, 1e-9)

        val len = (0.02 of siemens) / (58 of megasiemensPerMeter)
        assertIs<KLengthUnitInstance>(len)
        assertEquals(0.02 / 58e6, len into meters, 1e-15)

        assertEquals(1000.0, (0.1 of siemens / meters) into microsiemensPerCentimeter, 1e-6)
    }

    /** Electric mobility `µ = v / E` and the dipole moment `p = Q · l`. */
    @Test
    fun `mobility and dipole moment`() {
        val mu = (0.01 of meters / seconds) / (1000 of voltsPerMeter)
        assertIs<KElectricMobilityUnitInstance>(mu)
        assertEquals(1e-5, mu into squareMetersPerVoltSecond, 1e-15)

        val v = (1e-5 of squareMetersPerVoltSecond) * (1000 of voltsPerMeter)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(0.01, v into meters / seconds, 1e-12)

        val e = (0.01 of meters / seconds) / (1e-5 of squareMetersPerVoltSecond)
        assertIs<KElectricFieldStrengthUnitInstance>(e)
        assertEquals(1000.0, e into voltsPerMeter, 1e-6)

        val p = (1 of nano.coulombs) * (1 of milli.meters)
        assertIs<KElectricDipoleMomentUnitInstance>(p)
        assertEquals(1e-12, p into coulombMeters, 1e-21)
        assertEquals(1e-12 / KElectricDipoleMomentUnit.DEBYE.baseValue, p into debyes, 1e6)

        val q = (1e-12 of coulombMeters) / (1 of milli.meters)
        assertIs<KChargeUnitInstance>(q)
        assertEquals(1e-9, q into coulombs, 1e-18)

        val l = (1e-12 of coulombMeters) / (1 of nano.coulombs)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(0.001, l into meters, 1e-12)
    }

    /** Magnetic flux and flux density with every inversion of the group. */
    @Test
    fun `magnetic flux`() {
        val phi = (1.2 of teslas) * ((0.1 of meters) * (0.1 of meters))
        assertIs<KMagneticFluxUnitInstance>(phi)
        assertEquals(12.0, phi into milli.webers, 1e-9)

        val b = (12 of milli.webers) / ((0.1 of meters) * (0.1 of meters))
        assertIs<KMagneticFluxDensityUnitInstance>(b)
        assertEquals(1.2, b into teslas, 1e-9)

        val a = (12 of milli.webers) / (1.2 of teslas)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(0.01, a into (meters pow 2), 1e-12)

        val induced = (230 of volts) * (1 of milli.seconds)
        assertIs<KMagneticFluxUnitInstance>(induced)
        assertEquals(0.23, induced into webers, 1e-12)

        val u = (0.23 of webers) / (1 of milli.seconds)
        assertIs<KVoltageUnitInstance>(u)
        assertEquals(230.0, u into volts, 1e-9)

        val acVoltage = (12 of milli.webers) * (50 of hertz)
        assertIs<KVoltageUnitInstance>(acVoltage)
        assertEquals(0.6, acVoltage into volts, 1e-12)

        val fluxFromVoltage = (230 of volts) / (50 of hertz)
        assertIs<KMagneticFluxUnitInstance>(fluxFromVoltage)
        assertEquals(4.6, fluxFromVoltage into webers, 1e-12)

        val t = (0.23 of webers) / (230 of volts)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(0.001, t into seconds, 1e-12)

        assertEquals(1.2 / KMagneticFluxDensityUnit.GAUSS.baseValue, (1.2 of teslas) into gauss, 1e-6)
        assertEquals(0.23 / KMagneticFluxUnit.MAXWELL.baseValue, (0.23 of webers) into maxwells, 1e-3)
    }

    /** Inductance, reactance and reluctance. */
    @Test
    fun `inductance and reluctance`() {
        val l = (12 of milli.webers) / (2 of amperes)
        assertIs<KInductanceUnitInstance>(l)
        assertEquals(6.0, l into milli.henries, 1e-9)

        val phi = (6 of milli.henries) * (2 of amperes)
        assertIs<KMagneticFluxUnitInstance>(phi)
        assertEquals(12.0, phi into milli.webers, 1e-9)

        val i = (12 of milli.webers) / (6 of milli.henries)
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(2.0, i into amperes, 1e-9)

        val reactance = (6 of milli.henries) * (50 of hertz)
        assertIs<KResistanceUnitInstance>(reactance)
        assertEquals(0.3, reactance into ohms, 1e-12)

        val inductance = (0.3 of ohms) / (50 of hertz)
        assertIs<KInductanceUnitInstance>(inductance)
        assertEquals(6.0, inductance into milli.henries, 1e-9)

        val reluctance = 1 / (6 of milli.henries)
        assertIs<KReluctanceUnitInstance>(reluctance)
        assertEquals(1.0 / 0.006, reluctance into inverseHenries, 1e-6)

        val back = 1 / (166 of inverseHenries)
        assertIs<KInductanceUnitInstance>(back)
        assertEquals(1.0 / 166.0, back into henries, 1e-12)

        val fromCurrent = (2 of amperes) / (12 of milli.webers)
        assertIs<KReluctanceUnitInstance>(fromCurrent)
        assertEquals(2.0 / 0.012, fromCurrent into ampereTurnsPerWeber, 1e-6)

        val mmf = (166 of ampereTurnsPerWeber) * (12 of milli.webers)
        assertIs<KElectricCurrentUnitInstance>(mmf)
        assertEquals(1.992, mmf into amperes, 1e-9)

        val flux = (2 of amperes) / (166 of ampereTurnsPerWeber)
        assertIs<KMagneticFluxUnitInstance>(flux)
        assertEquals(2.0 / 166.0, flux into webers, 1e-12)
    }

    /** Magnetic field strength, permeability and their links. */
    @Test
    fun `magnetic field strength and permeability`() {
        val h = (2 of amperes) / (0.1 of meters)
        assertIs<KMagneticFieldStrengthUnitInstance>(h)
        assertEquals(20.0, h into amperesPerMeter, 1e-9)

        val i = (20 of amperesPerMeter) * (0.1 of meters)
        val iCommuted = (0.1 of meters) * (20 of amperesPerMeter)
        assertIs<KElectricCurrentUnitInstance>(i)
        assertIs<KElectricCurrentUnitInstance>(iCommuted)
        assertEquals(2.0, i into amperes, 1e-9)
        assertEquals(2.0, iCommuted into amperes, 1e-9)

        val b = (1 of vacuumPermeability) * (20 of amperesPerMeter)
        assertIs<KMagneticFluxDensityUnitInstance>(b)
        assertEquals(KPermeabilityUnit.VACUUM_PERMEABILITY * 20.0, b into teslas, 1e-12)

        val mu = (1.2 of teslas) / (20 of amperesPerMeter)
        assertIs<KPermeabilityUnitInstance>(mu)
        assertEquals(0.06, mu into henries / meters, 1e-9)

        val field = (1.2 of teslas) / (1 of vacuumPermeability)
        assertIs<KMagneticFieldStrengthUnitInstance>(field)
        assertEquals(1.2 / KPermeabilityUnit.VACUUM_PERMEABILITY, field into amperesPerMeter, 1e-3)

        val inductance = (1 of vacuumPermeability) * (0.1 of meters)
        assertIs<KInductanceUnitInstance>(inductance)
        assertEquals(KPermeabilityUnit.VACUUM_PERMEABILITY * 0.1, inductance into henries, 1e-15)

        val perLength = (6 of milli.henries) / (0.1 of meters)
        assertIs<KPermeabilityUnitInstance>(perLength)
        assertEquals(0.06, perLength into henries / meters, 1e-9)

        val len = (6 of milli.henries) / (1 of vacuumPermeability)
        assertIs<KLengthUnitInstance>(len)
        assertEquals(0.006 / KPermeabilityUnit.VACUUM_PERMEABILITY, len into meters, 1e-3)

        assertEquals(
            20.0 / KMagneticFieldStrengthUnit.OERSTED.baseValue,
            (20 of amperesPerMeter) into oersteds,
            1e-9,
        )
    }

    // ---------------------------------------------------------------- §4 Thermodynamics

    /** The affine temperature group: readings, differences and point arithmetic. */
    @Test
    fun `temperature point and interval`() {
        val t = 20 of celsius
        assertIs<KTemperatureUnitInstance>(t)
        assertEquals(293.15, t into kelvin, 1e-9)
        assertEquals(68.0, t into fahrenheit, 1e-9)
        assertEquals(527.67, t into rankine, 1e-9)

        val delta = (40 of celsius) - (12 of celsius)
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(28.0, delta.value, 1e-9)

        val explicit = KTemperatureDifference.ofKelvin(20)
        assertEquals(20.0, explicit.value, 1e-9)

        val warmer = (20 of celsius) + KTemperatureDifference.ofKelvin(5)
        assertIs<KTemperatureUnitInstance>(warmer)
        assertEquals(25.0, warmer into celsius, 1e-9)

        val cooler = (20 of celsius) - KTemperatureDifference.ofKelvin(5)
        assertIs<KTemperatureUnitInstance>(cooler)
        assertEquals(15.0, cooler into celsius, 1e-9)

        val sum = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)
        assertEquals(30.0, sum.value, 1e-9)

        val scaled = KTemperatureDifference.ofKelvin(20) * 2
        assertIs<KTemperatureDifferenceUnitInstance>(scaled)
        assertEquals(40.0, scaled.value, 1e-9)
    }

    /** Temperature gradient `∇T = ΔT / d` and back. */
    @Test
    fun `temperature gradient`() {
        val gradient = KTemperatureDifference.ofKelvin(21) / (0.3 of meters)
        assertIs<KTemperatureGradientUnitInstance>(gradient)
        assertEquals(70.0, gradient into kelvinPerMeter, 1e-9)
        assertEquals(70_000.0, gradient into kelvinPerKilometer, 1e-6)

        val delta = (70 of kelvinPerMeter) * (0.3 of meters)
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(21.0, delta.value, 1e-9)

        val d = KTemperatureDifference.ofKelvin(21) / (70 of kelvinPerMeter)
        assertIs<KLengthUnitInstance>(d)
        assertEquals(0.3, d into meters, 1e-9)
    }

    /** Thermal expansion: the reciprocal relation and the elongation helper. */
    @Test
    fun `thermal expansion`() {
        val alpha = 1 / KTemperatureDifference.ofKelvin(1)
        assertIs<KThermalExpansionUnitInstance>(alpha)
        assertEquals(1.0, alpha into perKelvin, 1e-12)

        val delta = 1 / (12e-6 of perKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(1.0 / 12e-6, delta.value, 1e-6)

        val growth = (12e-6 of perKelvin).elongationOf(10 of meters, KTemperatureDifference.ofKelvin(30))
        assertIs<KLengthUnitInstance>(growth)
        assertEquals(0.0036, growth into meters, 1e-12)

        assertEquals(12.0, (12e-6 of perKelvin) into ppmPerKelvin, 1e-9)
    }

    /** Heat capacity, specific heat capacity and specific energy in every direction. */
    @Test
    fun `heat capacity family`() {
        val c = (627_600 of joules) / KTemperatureDifference.ofKelvin(1)
        assertIs<KHeatCapacityUnitInstance>(c)
        assertEquals(627_600.0, c into joulesPerKelvin, 1e-6)

        val q = (627_600 of joulesPerKelvin) * KTemperatureDifference.ofKelvin(28)
        val qCommuted = KTemperatureDifference.ofKelvin(28) * (627_600 of joulesPerKelvin)
        assertIs<KEnergyUnitInstance>(q)
        assertIs<KEnergyUnitInstance>(qCommuted)
        assertEquals(17_572_800.0, q into joules, 1e-3)
        assertEquals(17_572_800.0, qCommuted into joules, 1e-3)

        val delta = (17_572_800 of joules) / (627_600 of joulesPerKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(28.0, delta.value, 1e-9)

        val specific = (627_600 of joulesPerKelvin) / (150 of kilo.grams)
        assertIs<KSpecificHeatCapacityUnitInstance>(specific)
        assertEquals(4184.0, specific into joulesPerKilogramKelvin, 1e-6)
        assertEquals(1.0, specific into caloriesPerGramKelvin, 1e-9)

        val capacity = (4184 of joulesPerKilogramKelvin) * (150 of kilo.grams)
        val capacityCommuted = (150 of kilo.grams) * (4184 of joulesPerKilogramKelvin)
        assertIs<KHeatCapacityUnitInstance>(capacity)
        assertIs<KHeatCapacityUnitInstance>(capacityCommuted)
        assertEquals(627_600.0, capacity into joulesPerKelvin, 1e-6)
        assertEquals(627_600.0, capacityCommuted into joulesPerKelvin, 1e-6)

        val mass = (627_600 of joulesPerKelvin) / (4184 of joulesPerKilogramKelvin)
        assertIs<KMassUnitInstance>(mass)
        assertEquals(150.0, mass into kilo.grams, 1e-9)

        val perKilogram = (4184 of joulesPerKilogramKelvin) * KTemperatureDifference.ofKelvin(28)
        val perKilogramCommuted = KTemperatureDifference.ofKelvin(28) * (4184 of joulesPerKilogramKelvin)
        assertIs<KSpecificEnergyUnitInstance>(perKilogram)
        assertIs<KSpecificEnergyUnitInstance>(perKilogramCommuted)
        assertEquals(117_152.0, perKilogram into joulesPerKilogram, 1e-6)
        assertEquals(117_152.0, perKilogramCommuted into joulesPerKilogram, 1e-6)

        val energy = (117_152 of joulesPerKilogram) * (150 of kilo.grams)
        assertIs<KEnergyUnitInstance>(energy)
        assertEquals(17_572_800.0, energy into joules, 1e-3)

        val specificEnergy = (17_572_800 of joules) / (150 of kilo.grams)
        assertIs<KSpecificEnergyUnitInstance>(specificEnergy)
        assertEquals(117_152.0, specificEnergy into joulesPerKilogram, 1e-6)

        val fromSpecific = (17_572_800 of joules) / (117_152 of joulesPerKilogram)
        assertIs<KMassUnitInstance>(fromSpecific)
        assertEquals(150.0, fromSpecific into kilo.grams, 1e-9)

        val rise = (117_152 of joulesPerKilogram) / (4184 of joulesPerKilogramKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(rise)
        assertEquals(28.0, rise.value, 1e-9)

        val material = (117_152 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(28)
        assertIs<KSpecificHeatCapacityUnitInstance>(material)
        assertEquals(4184.0, material into joulesPerKilogramKelvin, 1e-6)

        assertEquals(117_152.0 / 3600.0, (117_152 of joulesPerKilogram) into wattHoursPerKilogram, 1e-6)
    }

    /** Heat flow as the power group: `Φ = Q / t`, `Q = Φ · t`, `t = Q / Φ`. */
    @Test
    fun `heat flow`() {
        val phi = (17_572_800 of joules) / (30 of minutes)
        assertIs<KPowerUnitInstance>(phi)
        assertEquals(9762.666666666666, phi into watts, 1e-6)

        val q = (9.76 of kilo.watts) * (30 of minutes)
        assertIs<KEnergyUnitInstance>(q)
        assertEquals(17_568_000.0, q into joules, 1e-3)

        val t = (17_572_800 of joules) / (9.76 of kilo.watts)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(17_572_800.0 / 9760.0, t into seconds, 1e-6)
    }

    /** The heat-transfer chain from material property to total loss. */
    @Test
    fun `heat transfer chain`() {
        val fluxDensity = (1200 of watts) / ((5 of meters) * (3 of meters))
        assertIs<KHeatFluxDensityUnitInstance>(fluxDensity)
        assertEquals(80.0, fluxDensity into wattsPerSquareMeter, 1e-9)

        val power = (80 of wattsPerSquareMeter) * ((5 of meters) * (3 of meters))
        assertIs<KPowerUnitInstance>(power)
        assertEquals(1200.0, power into watts, 1e-9)

        val area = (1200 of watts) / (80 of wattsPerSquareMeter)
        assertIs<KAreaUnitInstance>(area)
        assertEquals(15.0, area into (meters pow 2), 1e-9)

        val fourier = (0.035 of wattsPerMeterKelvin) * (70 of kelvinPerMeter)
        assertIs<KHeatFluxDensityUnitInstance>(fourier)
        assertEquals(2.45, fourier into wattsPerSquareMeter, 1e-9)

        val gradient = (2.45 of wattsPerSquareMeter) / (0.035 of wattsPerMeterKelvin)
        assertIs<KTemperatureGradientUnitInstance>(gradient)
        assertEquals(70.0, gradient into kelvinPerMeter, 1e-9)

        val lambda = (2.45 of wattsPerSquareMeter) / (70 of kelvinPerMeter)
        assertIs<KThermalConductivityUnitInstance>(lambda)
        assertEquals(0.035, lambda into wattsPerMeterKelvin, 1e-12)

        val u = (0.035 of wattsPerMeterKelvin) / (0.2 of meters)
        assertIs<KHeatTransferCoefficientUnitInstance>(u)
        assertEquals(0.175, u into wattsPerSquareMeterKelvin, 1e-12)

        val conductivity = (0.175 of wattsPerSquareMeterKelvin) * (0.2 of meters)
        assertIs<KThermalConductivityUnitInstance>(conductivity)
        assertEquals(0.035, conductivity into wattsPerMeterKelvin, 1e-12)

        val thickness = (0.035 of wattsPerMeterKelvin) / (0.175 of wattsPerSquareMeterKelvin)
        assertIs<KLengthUnitInstance>(thickness)
        assertEquals(0.2, thickness into meters, 1e-9)

        val loss = (0.175 of wattsPerSquareMeterKelvin) * KTemperatureDifference.ofKelvin(21)
        val lossCommuted = KTemperatureDifference.ofKelvin(21) * (0.175 of wattsPerSquareMeterKelvin)
        assertIs<KHeatFluxDensityUnitInstance>(loss)
        assertIs<KHeatFluxDensityUnitInstance>(lossCommuted)
        assertEquals(3.675, loss into wattsPerSquareMeter, 1e-9)
        assertEquals(3.675, lossCommuted into wattsPerSquareMeter, 1e-9)

        val coefficient = (3.675 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(21)
        assertIs<KHeatTransferCoefficientUnitInstance>(coefficient)
        assertEquals(0.175, coefficient into wattsPerSquareMeterKelvin, 1e-12)

        val deltaT = (3.675 of wattsPerSquareMeter) / (0.175 of wattsPerSquareMeterKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(deltaT)
        assertEquals(21.0, deltaT.value, 1e-9)
    }

    /** Thermal resistance in all its forms. */
    @Test
    fun `thermal insulance R-value`() {
        val r = (0.2 of meters) / (0.035 of wattsPerMeterKelvin)
        assertIs<KThermalInsulanceUnitInstance>(r)
        assertEquals(0.2 / 0.035, r into squareMeterKelvinPerWatt, 1e-9)

        val fromU = 1 / (0.175 of wattsPerSquareMeterKelvin)
        assertIs<KThermalInsulanceUnitInstance>(fromU)
        assertEquals(1.0 / 0.175, fromU into squareMeterKelvinPerWatt, 1e-9)

        val u = 1 / (5.714285714285714 of squareMeterKelvinPerWatt)
        assertIs<KHeatTransferCoefficientUnitInstance>(u)
        assertEquals(0.175, u into wattsPerSquareMeterKelvin, 1e-12)

        val thickness = (5.714285714285714 of squareMeterKelvinPerWatt) * (0.035 of wattsPerMeterKelvin)
        assertIs<KLengthUnitInstance>(thickness)
        assertEquals(0.2, thickness into meters, 1e-9)

        val lambda = (0.2 of meters) / (5.714285714285714 of squareMeterKelvinPerWatt)
        assertIs<KThermalConductivityUnitInstance>(lambda)
        assertEquals(0.035, lambda into wattsPerMeterKelvin, 1e-12)

        val delta = (5.714285714285714 of squareMeterKelvinPerWatt) * (3.675 of wattsPerSquareMeter)
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(21.0, delta.value, 1e-9)

        val resistance = KTemperatureDifference.ofKelvin(21) / (3.675 of wattsPerSquareMeter)
        assertIs<KThermalInsulanceUnitInstance>(resistance)
        assertEquals(5.714285714285714, resistance into squareMeterKelvinPerWatt, 1e-9)

        val flux = KTemperatureDifference.ofKelvin(21) / (5.714285714285714 of squareMeterKelvinPerWatt)
        assertIs<KHeatFluxDensityUnitInstance>(flux)
        assertEquals(3.675, flux into wattsPerSquareMeter, 1e-9)

        assertEquals(
            5.714285714285714 / KThermalInsulanceUnit.TOG.baseValue,
            (5.714285714285714 of squareMeterKelvinPerWatt) into tog,
            1e-9,
        )
        assertEquals(
            5.714285714285714 / KThermalInsulanceUnit.CLO.baseValue,
            (5.714285714285714 of squareMeterKelvinPerWatt) into clo,
            1e-9,
        )
    }

    /** Thermal diffusivity `a = λ / (ρ · c)` and its two inverse helpers. */
    @Test
    fun `thermal diffusivity`() {
        val water = (1000 of kilo.grams) / (1000 of liters)
        val cp = 4184 of joulesPerKilogramKelvin
        val lambda = 0.6 of wattsPerMeterKelvin

        val a = lambda.diffusivityWith(water, cp)
        assertIs<KDiffusivityUnitInstance>(a)
        assertEquals(0.6 / (1000.0 * 4184.0), a into squareMetersPerSecond, 1e-15)
    }

    /** The alternative (imperial) readings of the heat-transfer groups. */
    @Test
    fun `heat transfer readings`() {
        assertEquals(
            80.0 / KHeatFluxDensityUnit.BTU_PER_HOUR_SQUARE_FOOT.baseValue,
            (80 of wattsPerSquareMeter) into btusPerHourSquareFoot,
            1e-6,
        )
        assertEquals(
            0.6 / KThermalConductivityUnit.BTU_PER_HOUR_FOOT_FAHRENHEIT.baseValue,
            (0.6 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit,
            1e-9,
        )
    }

    /** Molar mass, molar volume, molar energy and molar heat capacity. */
    @Test
    fun `molar quantities`() {
        val m = (18 of grams) / (1 of moles)
        assertIs<KMolarMassUnitInstance>(m)
        assertEquals(18.0, m into gramsPerMole, 1e-9)
        assertEquals(0.018, m into kilogramsPerMole, 1e-12)

        val mass = (18 of gramsPerMole) * (2.5 of moles)
        val massCommuted = (2.5 of moles) * (18 of gramsPerMole)
        assertIs<KMassUnitInstance>(mass)
        assertIs<KMassUnitInstance>(massCommuted)
        assertEquals(45.0, mass into grams, 1e-9)
        assertEquals(45.0, massCommuted into grams, 1e-9)

        val n = (45 of grams) / (18 of gramsPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(2.5, n into moles, 1e-9)

        val vm = (22.4 of liters) / (1 of moles)
        assertIs<KMolarVolumeUnitInstance>(vm)
        assertEquals(22.4, vm into litersPerMole, 1e-9)

        val volume = (22.4 of litersPerMole) * (3 of moles)
        assertIs<KVolumeUnitInstance>(volume)
        assertEquals(67.2, volume into liters, 1e-9)

        val amount = (67.2 of liters) / (22.4 of litersPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(amount)
        assertEquals(3.0, amount into moles, 1e-9)

        val water = (1000 of kilo.grams) / (1000 of liters)
        val molarVolume = (18 of gramsPerMole) / water
        assertIs<KMolarVolumeUnitInstance>(molarVolume)
        assertEquals(18.0, molarVolume into cubicCentimetersPerMole, 1e-9)

        val molarMass = (18 of cubicCentimetersPerMole) * water
        assertIs<KMolarMassUnitInstance>(molarMass)
        assertEquals(18.0, molarMass into gramsPerMole, 1e-9)

        val density = (18 of gramsPerMole) / (18 of cubicCentimetersPerMole)
        assertIs<KDensityUnitInstance>(density)
        assertEquals(1000.0, density into kilo.grams / (meters * meters * meters), 1e-6)

        val em = (286 of kilo.joules) / (1 of moles)
        assertIs<KMolarEnergyUnitInstance>(em)
        assertEquals(286_000.0, em into joulesPerMole, 1e-6)

        val energy = (286 of kilo.joulesPerMole) * (2 of moles)
        assertIs<KEnergyUnitInstance>(energy)
        assertEquals(572_000.0, energy into joules, 1e-6)

        val moles2 = (572 of kilo.joules) / (286 of kilo.joulesPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(moles2)
        assertEquals(2.0, moles2 into moles, 1e-9)

        val cm = (75.3 of joulesPerKelvin) / (1 of moles)
        assertIs<KMolarHeatCapacityUnitInstance>(cm)
        assertEquals(75.3, cm into joulesPerMoleKelvin, 1e-9)

        val capacity = (75.3 of joulesPerMoleKelvin) * (4 of moles)
        assertIs<KHeatCapacityUnitInstance>(capacity)
        assertEquals(301.2, capacity into joulesPerKelvin, 1e-9)

        val amount2 = (301.2 of joulesPerKelvin) / (75.3 of joulesPerMoleKelvin)
        assertIs<KAmountOfSubstanceUnitInstance>(amount2)
        assertEquals(4.0, amount2 into moles, 1e-9)

        val molarEnergy = (75.3 of joulesPerMoleKelvin) * KTemperatureDifference.ofKelvin(20)
        assertIs<KMolarEnergyUnitInstance>(molarEnergy)
        assertEquals(1506.0, molarEnergy into joulesPerMole, 1e-9)

        val molarCapacity = (1506 of joulesPerMole) / KTemperatureDifference.ofKelvin(20)
        assertIs<KMolarHeatCapacityUnitInstance>(molarCapacity)
        assertEquals(75.3, molarCapacity into joulesPerMoleKelvin, 1e-9)

        val rise = (1506 of joulesPerMole) / (75.3 of joulesPerMoleKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(rise)
        assertEquals(20.0, rise.value, 1e-9)

        assertEquals(2500.0, (2.5 of moles) into milli.moles, 1e-6)
        assertEquals(2.5 / KAmountOfSubstanceUnit.POUND_MOLE.baseValue, (2.5 of moles) into poundMoles, 1e-12)
        assertEquals(
            286_000.0 / KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY.baseValue,
            (286 of kilo.joulesPerMole) into electronVoltsPerEntity,
            1e-3,
        )
    }

    // ---------------------------------------------------------------- §5 Information technology

    /** Storage equality across bit/byte and the binary vs. decimal prefixes. */
    @Test
    fun `storage units and prefixes`() {
        assertTrue((1 of bytes) == (8 of bits))
        assertEquals(1.0, (1_048_576 of bytes) into mebi.bytes, 1e-12)
        assertEquals(1.048576, (1_048_576 of bytes) into mega.bytes, 1e-9)
        assertEquals(8e9 / 1_073_741_824.0, (8 of giga.bytes) into gibi.bytes, 1e-9)
        assertEquals(1024.0, (1 of kibi.bytes) into bytes, 1e-9)

        assertIs<KStorageUnitInstance>((700 of mega.bytes) + (1.4 of giga.bytes))
        assertEquals(2.1e9, ((700 of mega.bytes) + (1.4 of giga.bytes)) into bytes, 1e-3)
        assertEquals(1.65e12, ((2 of tera.bytes) - (350 of giga.bytes)) into bytes, 1e-3)
        assertEquals(2.5e11, ((1 of tera.bytes) / 4) into bytes, 1e-3)
    }

    /** Data rate `Ṡ = S / t`, `S = Ṡ · t`, `t = S / Ṡ` and the storage density. */
    @Test
    fun `data rate and storage density`() {
        val rate = (100 of mega.bytes) / (10 of seconds)
        assertIs<KDataRateUnitInstance>(rate)
        assertEquals(10.0, rate into mega.bytes / seconds, 1e-9)
        assertEquals(80.0, rate into mega.bits / seconds, 1e-9)

        val storage = (10 of mega.bytes / seconds) * (30 of seconds)
        val storageCommuted = (30 of seconds) * (10 of mega.bytes / seconds)
        assertIs<KStorageUnitInstance>(storage)
        assertIs<KStorageUnitInstance>(storageCommuted)
        assertEquals(300e6, storage into bytes, 1e-3)
        assertEquals(300e6, storageCommuted into bytes, 1e-3)

        val time = (4.7 of giga.bytes) / (10 of mega.bytes / seconds)
        assertIs<KTimeUnitInstance>(time)
        assertEquals(470.0, time into seconds, 1e-6)

        val density = (1 of tera.bytes) / ((10 of centi.meters) * (10 of centi.meters))
        assertIs<KStorageDensityUnitInstance>(density)
        assertEquals(1e12 / 0.01, density into bytes / (meters * meters), 1e3)

        val onArea = density * ((10 of centi.meters) * (5 of centi.meters))
        assertIs<KStorageUnitInstance>(onArea)
        assertEquals(5e11, onArea into bytes, 1e-3)

        val area = (1 of tera.bytes) / density
        assertIs<KAreaUnitInstance>(area)
        assertEquals(0.01, area into (meters pow 2), 1e-12)
    }

    // ---------------------------------------------------------------- §6 Prefixes, readings, powers

    /** The prefix builders across the whole SI range, including the binary ones. */
    @Test
    fun `prefix builders`() {
        assertEquals(1000.0, (1 of kilo.meters) into meters, 1e-9)
        assertEquals(0.001, (1 of milli.meters) into meters, 1e-12)
        assertEquals(1e-6, (1 of micro.seconds) into seconds, 1e-15)
        assertEquals(1e-9, (1 of nano.farads) into farads, 1e-18)
        assertEquals(1e6, (1 of mega.watts) into watts, 1e-3)
        assertEquals(1e9, (1 of giga.ohms) into ohms, 1.0)
        assertEquals(1e30, (1 of quetta.meters) into meters, 1e15)
        assertEquals(1e-30, (1 of quecto.meters) into meters, 1e-45)
    }

    /** Conversions between unit systems. */
    @Test
    fun `cross system readings`() {
        assertEquals(1000.0, (1 of meters) into milli.meters, 1e-9)
        assertEquals(1.609344, (1 of miles) into kilo.meters, 1e-9)
        assertEquals(0.45359237, (1 of pounds) into kilo.grams, 1e-12)
    }

    /** Powers: area, volume and the negative exponent. */
    @Test
    fun `powers and geometry`() {
        val area = (2 of kilo.meters) pow 2
        assertIs<KAreaUnitInstance>(area)
        assertEquals(4e6, area into (meters pow 2), 1e-3)

        val volume = (2 of meters) pow 3
        assertIs<KVolumeUnitInstance>(volume)
        assertEquals(8.0, volume into (meters pow 3), 1e-9)

        val rectangle = (5 of meters) * (3 of meters)
        assertIs<KAreaUnitInstance>(rectangle)
        assertEquals(15.0, rectangle into (meters pow 2), 1e-9)

        val box = ((5 of meters) * (3 of meters)) * (2 of meters)
        assertIs<KVolumeUnitInstance>(box)
        assertEquals(30.0, box into (meters pow 3), 1e-9)

        val side = ((5 of meters) * (3 of meters)) / (3 of meters)
        assertIs<KLengthUnitInstance>(side)
        assertEquals(5.0, side into meters, 1e-9)

        assertEquals(1.0, (10_000 of (meters pow 2)) into hectares, 1e-9)
        assertEquals(10_000.0 / 4046.8564224, (10_000 of (meters pow 2)) into acres, 1e-9)
        assertEquals(1000.0, (1000 of liters) into liters, 1e-9)
        assertEquals(1.0 / 0.003785411784, (1000 of liters) into usGallons, 1e-9)
    }

    /** `toString` renders the group's base unit; other units are read with `into`. */
    @Test
    fun `string rendering`() {
        assertEquals("5000.0 m", (5 of kilo.meters).toString())
        val v = (120 of kilo.meters) / (1.5 of hours)
        assertEquals(80.0, v into kilo.meters / hours, 1e-9)
        // A typed speed target carries no written-down display metadata, so `format` reads the value into
        // km/h but renders the group's base dimension.
        assertEquals("80.0 m/s", v format kilo.meters / hours)
    }

    // ---------------------------------------------------------------- §7 Periodic table

    /** The element constants are typed instances and compose with every formula. */
    @Test
    fun `periodic table constants in formulas`() {
        val gold = KChemicalElement.GOLD
        assertIs<KMolarMassUnitInstance>(gold.molarMass)
        assertIs<KDensityUnitInstance>(gold.density!!)

        val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters)
        val mass = gold.density * volume
        assertIs<KMassUnitInstance>(mass)
        assertEquals(19.32 * 56.0 / 1000.0, mass into kilo.grams, 0.1)

        val amount = mass / gold.molarMass
        assertIs<KAmountOfSubstanceUnitInstance>(amount)
        assertTrue((amount into moles) > 0.0)

        val iron = KChemicalElement.IRON
        assertIs<KMolarVolumeUnitInstance>(iron.molarVolume!!)
        assertIs<KTemperatureUnitInstance>(iron.meltingPoint!!)
        assertIs<KTemperatureUnitInstance>(iron.boilingPoint!!)

        val span = iron.boilingPoint - iron.meltingPoint
        assertIs<KTemperatureDifferenceUnitInstance>(span)
        assertTrue(span.value > 0.0)

        val copper = KChemicalElement.COPPER
        val capacity = copper.specificHeatCapacity!! * (5 of kilo.grams)
        assertIs<KHeatCapacityUnitInstance>(capacity)
        assertEquals((copper.specificHeatCapacity into joulesPerKilogramKelvin) * 5.0, capacity into joulesPerKelvin, 1e-6)

        val resistance = (2 of milli.meters) / copper.thermalConductivity!!
        assertIs<KThermalInsulanceUnitInstance>(resistance)
        assertTrue((resistance into squareMeterKelvinPerWatt) > 0.0)

        val wire = copper.electricalResistivity!! / (1 of milli.meters)
        assertIs<KResistanceUnitInstance>(wire)
        assertTrue((wire into ohms) > 0.0)

        assertIs<KEnergyUnitInstance>(KChemicalElement.HYDROGEN.ionizationEnergy!!)
        assertIs<KLengthUnitInstance>(KChemicalElement.CARBON.atomicRadius!!)
        assertEquals(82, KChemicalElement.LEAD.ordinalNumber)
    }

    // ---------------------------------------------------------------- §8 Worked examples

    /** §8.1 - isochoric pressure rise (Gay-Lussac): `T₂ = T₁ · p₂ / p₁`, `ΔT = T₂ − T₁`. */
    @Test
    fun `worked example gay lussac`() {
        val p1 = 8.0 of bars
        val p2 = 9.2 of bars
        val t1 = 20 of celsius

        val ratio = (p2 into bars) / (p1 into bars)
        assertEquals(1.15, ratio, 1e-12)

        val t2 = ((t1 into kelvin) * ratio) of kelvin
        assertIs<KTemperatureUnitInstance>(t2)
        assertEquals(293.15 * 1.15, t2 into kelvin, 1e-9)
        assertEquals(293.15 * 1.15 - 273.15, t2 into celsius, 1e-9)

        val delta = t2 - t1
        assertIs<KTemperatureDifferenceUnitInstance>(delta)
        assertEquals(293.15 * 0.15, delta.value, 1e-9)
    }

    /** §8.2 - heating a bathtub and the boiler time. */
    @Test
    fun `worked example bathtub`() {
        val water = 4184 of joulesPerKilogramKelvin
        val heat = (water * (150 of kilo.grams)) * ((40 of celsius) - (12 of celsius))
        assertIs<KEnergyUnitInstance>(heat)
        assertEquals(4184.0 * 150.0 * 28.0, heat into joules, 1e-3)
        assertEquals(17.5728, heat into mega.joules, 1e-6)

        val boiler = 9 of kilo.watts
        assertEquals(4184.0 * 150.0 * 28.0 / 9000.0 / 3600.0, (heat / boiler) into hours, 1e-9)
    }

    /** §8.3 - heat loss through an insulated wall. */
    @Test
    fun `worked example wall heat loss`() {
        val u = (0.035 of wattsPerMeterKelvin) / (20 of centi.meters)
        val flux = u * KTemperatureDifference.ofKelvin(21)
        val loss = flux * ((5 of meters) * (3 of meters))
        assertIs<KPowerUnitInstance>(loss)
        assertEquals(0.035 / 0.2 * 21.0 * 15.0, loss into watts, 1e-9)
        assertEquals(55.125, loss into watts, 1e-9)
    }

    /** §8.4 - voltage drop on a copper cable. */
    @Test
    fun `worked example cable voltage drop`() {
        val rho = 17 of nano.ohmMeters
        val length = 10 of meters
        val area = 1.5 of (milli.meters pow 2)

        val equivalent = (area.toUnit() / length.toUnit()).toLength() // A / l
        val r = rho / equivalent
        assertIs<KResistanceUnitInstance>(r)
        assertEquals(17e-9 * 10.0 / 1.5e-6, r into ohms, 1e-12)

        val drop = r * (16 of amperes)
        assertIs<KVoltageUnitInstance>(drop)
        assertEquals(17e-9 * 10.0 / 1.5e-6 * 16.0, drop into volts, 1e-12)
    }

    /** §8.5 - kinetic energy and the resulting braking distance. */
    @Test
    fun `worked example braking distance`() {
        val v = 100 of kilo.meters / hours
        val m = 1200 of kilo.grams
        val energy = (((m * v).toUnit() * v.toUnit()) / 2).toEnergy() // momentum * speed is not typed
        assertIs<KEnergyUnitInstance>(energy)
        val expectedEnergy = 0.5 * 1200.0 * (100_000.0 / 3600.0) * (100_000.0 / 3600.0)
        assertEquals(expectedEnergy, energy into joules, 1e-6)

        // energy / force is not a typed operator - read both sides and rebuild the length
        val distance = ((energy into joules) / ((8 of kilo.newtons) into newtons)) of meters
        assertIs<KLengthUnitInstance>(distance)
        assertEquals(expectedEnergy / 8000.0, distance into meters, 1e-9)
    }

    /** §8.6 - download time of a DVD image over a 50 Mbit/s line. */
    @Test
    fun `worked example download time`() {
        val file = 4.7 of giga.bytes
        val line = (50 of mega.bits) / (1 of seconds)
        assertIs<KDataRateUnitInstance>(line)

        val time = file / line
        assertIs<KTimeUnitInstance>(time)
        assertEquals(4.7e9 * 8.0 / 50e6 / 60.0, time into minutes, 1e-9)
    }

    /** §8.7 - motor torque from power and speed, and the way back. */
    @Test
    fun `worked example motor`() {
        val speed = 3000 of revolutionsPerMinute
        val torque = (15 of kilo.watts) / speed
        assertIs<KEnergyUnitInstance>(torque)
        assertEquals(15_000.0 / (100.0 * PI), torque into joules, 1e-9)

        val power = torque * speed
        assertIs<KPowerUnitInstance>(power)
        assertEquals(15.0, power into kilo.watts, 1e-9)
    }

    /** §8.8 - mass and amount of substance of a gold bar. */
    @Test
    fun `worked example gold bar`() {
        val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters)
        assertIs<KVolumeUnitInstance>(volume)
        assertEquals(56e-6, volume into (meters pow 3), 1e-15)

        val mass = KChemicalElement.GOLD.density!! * volume
        assertIs<KMassUnitInstance>(mass)

        val amount = mass / KChemicalElement.GOLD.molarMass
        assertIs<KAmountOfSubstanceUnitInstance>(amount)

        val expectedMass = (KChemicalElement.GOLD.density into kilo.grams / (meters * meters * meters)) * 56e-6
        assertEquals(expectedMass, mass into kilo.grams, 1e-9)
        assertEquals(
            (mass into grams) / (KChemicalElement.GOLD.molarMass into gramsPerMole),
            amount into moles,
            1e-9,
        )
    }
}
