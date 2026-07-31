# كتاب الوصفات — صِيَغ بالأمثلة

هذه الصفحة هي **صفحة التوجيه** في KUnit: عدّة مئات من الحسابات الفيزيائية، كلٌّ منها معروض مرّتين — على اليسار الصيغة
كما تُكتب في الفيزياء أو الهندسة، وعلى اليمين كما تُكتب في Kotlin مع KUnit.

الغاية من العمودين أنّهما *الصيغة نفسها*. لا يطلب منك KUnit أبدًا ترجمة صيغة إلى تمرين في مناورة الأرقام: `v = s / t` هي
فعلًا `distance / time`، والناتج هو `KSpeedUnitInstance` لا مجرّد `Double`.

## كيف تُقرأ الجداول

* العمود **الرياضيات** — الصيغة الكتابية المدرسية. `·` ضرب، و`/` كسر، وتُكتب الأُسّس بالأحرف الفوقية اليونيكودية
  (`²`، `⁻¹`).
* العمود **Kotlin** — تعبير كامل وقابل للتنفيذ يستخدم الفعلين `of` (البناء) و`into` (القراءة).
* العمود **النتيجة** — النوع الذي يعيده KUnit. كل نتيجة محكومة بالنوع هي نسخة وحدة حقيقية، فيمكن تمريرها مباشرةً إلى
  الصيغة التالية.

تفترض كل الأمثلة أنّ مفردات المجموعات المعنيّة قد استُوردت:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo          // and the other prefix builders
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
// … one import per unit group used
```

!!! tip "أين تُوثَّق الوحدات نفسها"
تعرض هذه الصفحة **كيف تتركّب الكمّيات**. أمّا الرموز والعلامات والبادئات المتاحة لكل مجموعة فمذكورة في صفحتها الخاصّة،
ويمكن الوصول إليها من نظرات الفروع العامّة:
[علم الحركة](units/kinematics/overview.md)، [الميكانيكا](units/mechanics/overview.md)،
[الهندسة الكهربائية](units/electrical/overview.md)، [الديناميكا الحرارية](units/thermodynamics/overview.md)،
[تقنية المعلومات](units/information/overview.md).

---

## 1. علم الحركة — الحركة

### 1.1 المسافة والزمن والسرعة

| الرياضيات      | Kotlin                                        | النتيجة                 |
|----------------|-----------------------------------------------|-------------------------|
| `v = s / t`    | `(120 of kilo.meters) / (1.5 of hours)`       | `KSpeedUnitInstance`    |
| `s = v · t`    | `v * (3 of hours)`                            | `KLengthUnitInstance`   |
| `s = t · v`    | `(3 of hours) * v`                            | `KLengthUnitInstance`   |
| `t = s / v`    | `(240 of kilo.meters) / v`                    | `KTimeUnitInstance`     |
| `v = s · f`    | `(2.5 of meters) * (80 of rpm)`               | `KSpeedUnitInstance`    |
| `v بوحدة km/h` | `v into (kilo.meters / hours)`                | `Double`                |
| `v بالعُقد`     | `v into knots`                                | `Double`                |
| `v كعدد ماخ`   | `v into mach`                                 | `Double`                |
| `v / c`        | `v into speedOfLight`                         | `Double`                |
| `s = s₁ + s₂`  | `(5 of kilo.meters) + (3 of miles)`           | `KLengthUnitInstance`   |
| `s = s₁ − s₂`  | `(5 of kilo.meters) - (800 of meters)`        | `KLengthUnitInstance`   |
| `s̄ = s / n`    | `(120 of kilo.meters) / 4`                    | `KLengthUnitInstance`   |
| `t = t₁ + t₂`  | `(90 of minutes) + (45 of seconds)`           | `KTimeUnitInstance`     |
| `v̄ = Σs / Σt`  | `((120 of kilo.meters) + (30 of kilo.meters)) / ((1.5 of hours) + (0.5 of hours))` | `KSpeedUnitInstance` |

### 1.2 التسارع

| الرياضيات            | Kotlin                                                | النتيجة                     |
|----------------------|-------------------------------------------------------|-----------------------------|
| `a = Δv / t`         | `((10 of meters) / (1 of seconds)) / (2 of seconds)`  | `KAccelerationUnitInstance` |
| `a = Δv / t`         | `(100 of kilo.meters / hours) / (4.6 of seconds)`       | `KAccelerationUnitInstance` |
| `v = a · t`          | `a * (2 of seconds)`                                  | `KSpeedUnitInstance`        |
| `v = t · a`          | `(2 of seconds) * a`                                  | `KSpeedUnitInstance`        |
| `t = Δv / a`         | `(30 of meters / seconds) / (3 of meters / (seconds pow 2)).toAcceleration()` | `KTimeUnitInstance`     |
| `a / g`              | `a into standardGravities`                            | `Double`                    |
| `a بوحدة Gal`        | `a into gals`                                         | `Double`                    |
| `a = g` (سقوط حرّ)    | `1 of standardGravities`                              | `KAccelerationUnitInstance` |
| `v = g · t`          | `(1 of standardGravities) * (3 of seconds)`           | `KSpeedUnitInstance`        |
| `s = ½ · g · t²`     | `((1 of standardGravities) * (3 of seconds)) * (3 of seconds) / 2` | `KLengthUnitInstance` |

### 1.3 التردّد والدور

| الرياضيات      | Kotlin                                  | النتيجة                   |
|----------------|-----------------------------------------|---------------------------|
| `f = 1 / T`    | `1 / (0.02 of seconds)`                 | `KFrequencyUnitInstance`  |
| `T = 1 / f`    | `1 / (50 of hertz)`                     | `KTimeUnitInstance`       |
| `f بوحدة kHz`  | `(50_000 of hertz) into kilo.hertz`     | `Double`                  |
| `n بوحدة rpm`  | `(50 of hertz) into rpm`                | `Double`                  |
| `n بوحدة bpm`  | `(1.2 of hertz) into bpm`               | `Double`                  |
| `f_frame`      | `(60 of fps) into hertz`                | `Double`                  |
| `v = π · d · f`| `(Math.PI * 0.7 of meters) * (900 of rpm)` | `KSpeedUnitInstance`   |

### 1.4 التدفّق الحجمي

| الرياضيات      | Kotlin                                             | النتيجة                     |
|----------------|----------------------------------------------------|-----------------------------|
| `q̇ = V / t`    | `(600 of liters) / (2 of minutes)`                 | `KVolumeFlowUnitInstance`   |
| `V = q̇ · t`    | `q * (15 of minutes)`                              | `KVolumeUnitInstance`       |
| `V = t · q̇`    | `(15 of minutes) * q`                              | `KVolumeUnitInstance`       |
| `t = V / q̇`    | `(1000 of liters) / q`                        | `KTimeUnitInstance`         |
| `q̇ بوحدة l/min`| `q into litersPerMinute`                           | `Double`                    |
| `q̇ بوحدة m³/h` | `q into cubicMetersPerHour`                        | `Double`                    |
| `q̇ بوحدة gpm`  | `q into usGallonsPerMinute`                        | `Double`                    |
| `ṁ = ρ · q̇`    | `(1000 of kilo.grams) / (1000 of liters) * q` | `KMassFlowUnitInstance`     |

---

## 2. الميكانيكا — القوى والطاقة

### 2.1 قانون نيوتن

| الرياضيات      | Kotlin                                                        | النتيجة                     |
|----------------|---------------------------------------------------------------|-----------------------------|
| `F = m · a`    | `(1200 of kilo.grams) * (2.5 of meters / (seconds pow 2)).toAcceleration()`      | `KForceUnitInstance`        |
| `F = a · m`    | `(2.5 of meters / (seconds pow 2)).toAcceleration() * (1200 of kilo.grams)`      | `KForceUnitInstance`        |
| `a = F / m`    | `(3000 of newtons) / (1200 of kilo.grams)`                    | `KAccelerationUnitInstance` |
| `m = F / a`    | `(3000 of newtons) / (2.5 of meters / (seconds pow 2)).toAcceleration()`         | `KMassUnitInstance`         |
| `F_g = m · g`  | `(75 of kilo.grams) * (1 of standardGravities)`               | `KForceUnitInstance`        |
| `F بوحدة kN`   | `f into kilo.newtons`                                         | `Double`                    |
| `F بوحدة lbf`  | `f into poundsForce`                                          | `Double`                    |
| `F بوحدة dyn`  | `f into dynes`                                                | `Double`                    |
| `F بوحدة kp`   | `f into ponds`                                                | `Double`                    |
| `ΣF`           | `(300 of newtons) + (2 of kilo.newtons)`                      | `KForceUnitInstance`        |

### 2.2 الشغل والطاقة والقدرة

| الرياضيات         | Kotlin                                              | النتيجة                |
|-------------------|-----------------------------------------------------|------------------------|
| `W = F · s`       | `(500 of newtons) * (12 of meters)`                 | `KEnergyUnitInstance`  |
| `W = s · F`       | `(12 of meters) * (500 of newtons)`                 | `KEnergyUnitInstance`  |
| `E_pot = m · g · h` | `((80 of kilo.grams) * (1 of standardGravities)) * (10 of meters)` | `KEnergyUnitInstance` |
| `P = W / t`       | `(6000 of joules) / (12 of seconds)`                | `KPowerUnitInstance`   |
| `W = P · t`       | `(2 of kilo.watts) * (3 of hours)`                  | `KEnergyUnitInstance`  |
| `t = W / P`       | `(21.6 of mega.joules) / (2 of kilo.watts)`         | `KTimeUnitInstance`    |
| `P = F · v`       | `(400 of newtons) * (25 of meters / seconds)`        | `KPowerUnitInstance`   |
| `F = P / v`       | `(10 of kilo.watts) / (25 of meters / seconds)`      | `KForceUnitInstance`   |
| `v = P / F`       | `(10 of kilo.watts) / (400 of newtons)`             | `KSpeedUnitInstance`   |
| `W = P / f`       | `(60 of watts) / (50 of hertz)`                     | `KEnergyUnitInstance`  |
| `E بوحدة kWh`     | `e into (kilo.watts * hours)`                       | `Double`               |
| `E بوحدة kcal`    | `e into kilo.calories`                              | `Double`               |
| `E بوحدة eV`      | `e into electronVolts`                              | `Double`               |
| `E بوحدة Btu`     | `e into britishThermalUnits`                        | `Double`               |
| `P بوحدة hp`      | `p into metricHorsePowers`                          | `Double`               |
| `P بوحدة bhp`     | `p into mechanicalHorsePowers`                      | `Double`               |

### 2.3 كمّية الحركة والدفع

| الرياضيات       | Kotlin                                                | النتيجة                 |
|-----------------|-------------------------------------------------------|-------------------------|
| `p = m · v`     | `(1200 of kilo.grams) * (25 of meters / seconds)`      | `KMomentumUnitInstance` |
| `p = v · m`     | `(25 of meters / seconds) * (1200 of kilo.grams)`      | `KMomentumUnitInstance` |
| `v = p / m`     | `(30_000 of kilogramMetersPerSecond) / (1200 of kilo.grams)` | `KSpeedUnitInstance` |
| `m = p / v`     | `(30_000 of kilogramMetersPerSecond) / (25 of meters / seconds)` | `KMassUnitInstance` |
| `J = F · t`     | `(2 of kilo.newtons) * (0.15 of seconds)`             | `KMomentumUnitInstance` |
| `J = t · F`     | `(0.15 of seconds) * (2 of kilo.newtons)`             | `KMomentumUnitInstance` |
| `F = p / t`     | `(300 of newtonSeconds) / (0.15 of seconds)`          | `KForceUnitInstance`    |
| `t = p / F`     | `(300 of newtonSeconds) / (2 of kilo.newtons)`        | `KTimeUnitInstance`     |
| `p بوحدة N·s`   | `p into newtonSeconds`                                | `Double`                |
| `p بوحدة lb·ft/s` | `p into poundFeetPerSecond`                         | `Double`                |

### 2.4 الضغط والإجهاد

| الرياضيات        | Kotlin                                                     | النتيجة                  |
|------------------|------------------------------------------------------------|--------------------------|
| `p = F / A`      | `(2000 of newtons) / ((0.1 of meters) * (0.05 of meters))` | `KPressureUnitInstance`  |
| `F = p · A`      | `(3 of bars) * ((0.2 of meters) * (0.1 of meters))`                   | `KForceUnitInstance`     |
| `A = F / p`      | `(6000 of newtons) / (3 of bars)`                          | `KAreaUnitInstance`      |
| `p بوحدة bar`    | `p into bars`                                              | `Double`                 |
| `p بوحدة psi`    | `p into psis`                                              | `Double`                 |
| `p بوحدة atm`    | `p into atmospheres`                                       | `Double`                 |
| `p بوحدة Torr`   | `p into torrs`                                             | `Double`                 |
| `σ = F / A`      | `(50 of kilo.newtons) / ((0.05 of meters) * (0.01 of meters))`        | `KPressureUnitInstance`  |
| `ε = ΔL / L`     | `((0.6 of milli.meters) / (2 of meters)).toStrain()`       | `KStrainUnitInstance`    |
| `E = σ / ε`      | `(100 of mega.pascals) / (0.0005 of ratio)`                | `KPressureUnitInstance`  |
| `σ = E · ε`      | `(210 of giga.pascals) * (0.0005 of ratio)`                | `KPressureUnitInstance`  |
| `ε بوحدة %`      | `strain into percent`                                      | `Double`                 |
| `ε بوحدة ‰`      | `strain into perMille`                                     | `Double`                 |
| `ε بوحدة µε`     | `strain into microstrain`                                  | `Double`                 |
| `p_hyd = F / A`  | `((10_000 of kilo.grams) * (1 of standardGravities)) / ((1 of meters) * (1 of meters))` | `KPressureUnitInstance` |

### 2.5 الدوران

| الرياضيات      | Kotlin                                                         | النتيجة                            |
|----------------|----------------------------------------------------------------|------------------------------------|
| `ω = φ / t`    | `(1 of turns) / (1 of seconds)`                                | `KAngularVelocityUnitInstance`     |
| `φ = ω · t`    | `(3000 of revolutionsPerMinute) * (2 of seconds)`              | `KAngleUnitInstance`               |
| `t = φ / ω`    | `(10 of turns) / (3000 of revolutionsPerMinute)`               | `KTimeUnitInstance`                |
| `α = Δω / t`   | `(3000 of revolutionsPerMinute) / (5 of seconds)`              | `KAngularAccelerationUnitInstance` |
| `ω = α · t`    | `(2 of radiansPerSecondSquared) * (5 of seconds)`              | `KAngularVelocityUnitInstance`     |
| `t = ω / α`    | `(10 of (radians / seconds)) / (2 of radiansPerSecondSquared)` | `KTimeUnitInstance`                |
| `ω بوحدة rpm`  | `w into revolutionsPerMinute`                                  | `Double`                           |
| `ω بوحدة rad/s`| `w into (radians / seconds)`                                   | `Double`                           |
| `J = m · r²`   | `(2 of kilo.grams) * ((0.3 of meters) * (0.3 of meters))`                  | `KInertiaUnitInstance`             |
| `m = J / r²`   | `(0.18 of kilogramMetersSquared) / ((0.3 of meters) * (0.3 of meters))`    | `KMassUnitInstance`                |
| `L = J · ω`    | `(0.18 of kilogramMetersSquared) * (50 of revolutionsPerSecond)` | `KAngularMomentumUnitInstance`   |
| `J = L / ω`    | `(56 of kilogramMetersSquaredPerSecond) / (50 of revolutionsPerSecond)` | `KInertiaUnitInstance`     |
| `ω = L / J`    | `(56 of kilogramMetersSquaredPerSecond) / (0.18 of kilogramMetersSquared)` | `KAngularVelocityUnitInstance` |
| `L = p · r`    | `(300 of newtonSeconds) * (0.4 of meters)`                     | `KAngularMomentumUnitInstance`     |
| `p = L / r`    | `(120 of jouleSeconds) / (0.4 of meters)`                      | `KMomentumUnitInstance`            |
| `M = F · r`    | `(100 of newtons) * (2 of meters)`                             | `KEnergyUnitInstance` (N·m)        |
| `M = J · α`    | `(0.18 of kilogramMetersSquared) * (2 of radiansPerSecondSquared)` | `KEnergyUnitInstance` (N·m)    |
| `M = P / ω`    | `(15 of kilo.watts) / (3000 of revolutionsPerMinute)`          | `KEnergyUnitInstance` (N·m)        |
| `P = M · ω`    | `(48 of joules) * (3000 of revolutionsPerMinute)`              | `KPowerUnitInstance`               |
| `ω = P / M`    | `(15 of kilo.watts) / (48 of joules)`                          | `KAngularVelocityUnitInstance`     |
| `α = M / J`    | `(48 of joules) / (0.18 of kilogramMetersSquared)`             | `KAngularAccelerationUnitInstance` |
| `J = M / α`    | `(48 of joules) / (2 of radiansPerSecondSquared)`              | `KInertiaUnitInstance`             |
| `Ω = φ · φ`    | `(0.5 of radians) * (0.5 of radians)`                          | `KSolidAngleUnitInstance`          |
| `φ = Ω / φ`    | `(0.25 of steradians) / (0.5 of radians)`                      | `KAngleUnitInstance`               |
| `φ بوحدة °`    | `angle into degrees`                                           | `Double`                           |
| `φ بوحدة gon`  | `angle into gradians`                                          | `Double`                           |
| `Ω بوحدة deg²` | `omega into squareDegrees`                                     | `Double`                           |

### 2.6 الكثافات

| الرياضيات         | Kotlin                                                    | النتيجة                       |
|-------------------|-----------------------------------------------------------|-------------------------------|
| `ρ = m / V`       | `(7850 of kilo.grams) / (1000 of liters)`            | `KDensityUnitInstance`        |
| `m = ρ · V`       | `steel * (250 of liters)`                        | `KMassUnitInstance`           |
| `m = V · ρ`       | `(250 of liters) * steel`                        | `KMassUnitInstance`           |
| `V = m / ρ`       | `(1962.5 of kilo.grams) / steel`                          | `KVolumeUnitInstance`         |
| `ρ بوحدة g/cm³`   | `steel into (grams / (centi.meters * centi.meters * centi.meters))`               | `Double`                      |
| `ρ_A = m / A`     | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))`    | `KAreaDensityUnitInstance`    |
| `m = ρ_A · A`     | `areaDensity * ((4 of meters) * (3 of meters))`                    | `KMassUnitInstance`           |
| `A = m / ρ_A`     | `(60 of kilo.grams) / areaDensity`                        | `KAreaUnitInstance`           |
| `ρ_A = ρ · d`     | `steel * (2 of milli.meters)`                             | `KAreaDensityUnitInstance`    |
| `ρ = ρ_A / d`     | `areaDensity / (2 of milli.meters)`                       | `KDensityUnitInstance`        |
| `ρ_l = m / l`     | `(90 of grams) / (1000 of meters)`                        | `KLinearDensityUnitInstance`  |
| `m = ρ_l · l`     | `(0.09 of gramsPerMeter) * (2500 of meters)`              | `KMassUnitInstance`           |
| `l = m / ρ_l`     | `(225 of grams) / (0.09 of gramsPerMeter)`                | `KLengthUnitInstance`         |
| `ρ_l بوحدة tex`   | `linearDensity into tex`                                  | `Double`                      |
| `ρ_l بوحدة den`   | `linearDensity into denier`                               | `Double`                      |
| `v = V / m`       | `(1000 of liters) / (1000 of kilo.grams)`            | `KSpecificVolumeUnitInstance` |
| `v = 1 / ρ`       | `1 / steel`                                               | `KSpecificVolumeUnitInstance` |
| `ρ = 1 / v`       | `1 / specificVolume`                                      | `KDensityUnitInstance`        |
| `V = v · m`       | `(0.001 of cubicMetersPerKilogram) * (500 of kilo.grams)` | `KVolumeUnitInstance`         |
| `m = V / v`       | `(500 of liters) / (0.001 of cubicMetersPerKilogram)` | `KMassUnitInstance`       |

### 2.7 الجريان واللزوجة والتوتّر السطحي

| الرياضيات      | Kotlin                                                       | النتيجة                     |
|----------------|--------------------------------------------------------------|-----------------------------|
| `ṁ = m / t`    | `(180 of kilo.grams) / (1 of hours)`                         | `KMassFlowUnitInstance`     |
| `m = ṁ · t`    | `(0.05 of kilogramsPerSecond) * (2 of hours)`                | `KMassUnitInstance`         |
| `t = m / ṁ`    | `(360 of kilo.grams) / (0.05 of kilogramsPerSecond)`         | `KTimeUnitInstance`         |
| `ṁ = ρ · q̇`    | `water * (2 of litersPerSecond)`                             | `KMassFlowUnitInstance`     |
| `q̇ = ṁ / ρ`    | `(2 of kilogramsPerSecond) / water`                          | `KVolumeFlowUnitInstance`   |
| `ρ = ṁ / q̇`    | `(2 of kilogramsPerSecond) / (2 of litersPerSecond)`         | `KDensityUnitInstance`      |
| `ṁ بوحدة t/h`  | `massFlow into tonnesPerHour`                                | `Double`                    |
| `η = p · t`    | `(1 of pascals) * (0.001 of seconds)`                        | `KViscosityUnitInstance`    |
| `ν = η / ρ`    | `(0.001 of pascalSeconds) / water`                           | `KDiffusivityUnitInstance`  |
| `η = ν · ρ`    | `(1e-6 of squareMetersPerSecond) * water`                    | `KViscosityUnitInstance`    |
| `ρ = η / ν`    | `(0.001 of pascalSeconds) / (1e-6 of squareMetersPerSecond)` | `KDensityUnitInstance`      |
| `η بوحدة cP`   | `viscosity into centi.poises`                                 | `Double`                    |
| `ν بوحدة cSt`  | `kinematicViscosity into centistokes`                        | `Double`                    |
| `σ = F / l`    | `(0.0728 of newtons) / (1 of meters)`                        | `KLineForceUnitInstance`    |
| `F = σ · l`    | `(72.8 of (milli.newtons / meters)) * (0.05 of meters)`      | `KForceUnitInstance`        |
| `l = F / σ`    | `(0.00364 of newtons) / (72.8 of (milli.newtons / meters))`  | `KLengthUnitInstance`       |
| `W = σ · A`    | `(0.0728 of newtonsPerMeter) * ((1 of meters) * (0.5 of meters))`      | `KEnergyUnitInstance`       |
| `A = W / σ`    | `(0.0364 of joules) / (0.0728 of newtonsPerMeter)`           | `KAreaUnitInstance`         |
| `k = F / s`    | `(500 of newtons) / (0.01 of meters)`                        | `KLineForceUnitInstance`    |
| `F = k · s`    | `(50 of newtonsPerMillimeter) * (0.004 of meters)`           | `KForceUnitInstance`        |

---

## 3. الهندسة الكهربائية

### 3.1 قانون أوم والقدرة

| الرياضيات      | Kotlin                                     | النتيجة                        |
|----------------|--------------------------------------------|--------------------------------|
| `U = R · I`    | `(220 of ohms) * (0.05 of amperes)`        | `KVoltageUnitInstance`         |
| `U = I · R`    | `(0.05 of amperes) * (220 of ohms)`        | `KVoltageUnitInstance`         |
| `I = U / R`    | `(230 of volts) / (46 of ohms)`            | `KElectricCurrentUnitInstance` |
| `R = U / I`    | `(230 of volts) / (5 of amperes)`          | `KResistanceUnitInstance`      |
| `G = 1 / R`    | `1 / (46 of ohms)`                         | `KConductanceUnitInstance`     |
| `R = 1 / G`    | `1 / (0.02 of siemens)`                    | `KResistanceUnitInstance`      |
| `I = G · U`    | `(0.02 of siemens) * (230 of volts)`       | `KElectricCurrentUnitInstance` |
| `G = I / U`    | `(5 of amperes) / (230 of volts)`          | `KConductanceUnitInstance`     |
| `U = I / G`    | `(5 of amperes) / (0.02 of siemens)`       | `KVoltageUnitInstance`         |
| `P = U · I`    | `(230 of volts) * (5 of amperes)`          | `KPowerUnitInstance`           |
| `I = P / U`    | `(1150 of watts) / (230 of volts)`         | `KElectricCurrentUnitInstance` |
| `U = P / I`    | `(1150 of watts) / (5 of amperes)`         | `KVoltageUnitInstance`         |
| `W = P · t`    | `(1150 of watts) * (2 of hours)`           | `KEnergyUnitInstance`          |
| `U بوحدة kV`   | `u into kilo.volts`                        | `Double`                       |
| `U بوحدة mV`   | `u into milli.volts`                       | `Double`                       |
| `R بوحدة kΩ`   | `r into kilo.ohms`                         | `Double`                       |
| `S بوحدة kVA`  | `s into kilo.voltAmperes`                  | `Double`                       |
| `Q بوحدة var`  | `q into vars`                              | `Double`                       |

### 3.2 الشحنة والسعة

| الرياضيات      | Kotlin                                         | النتيجة                         |
|----------------|------------------------------------------------|---------------------------------|
| `Q = I · t`    | `(2 of amperes) * (30 of minutes)`             | `KChargeUnitInstance`           |
| `Q = t · I`    | `(30 of minutes) * (2 of amperes)`             | `KChargeUnitInstance`           |
| `I = Q / t`    | `(3600 of coulombs) / (30 of minutes)`         | `KElectricCurrentUnitInstance`  |
| `t = Q / I`    | `(3600 of coulombs) / (2 of amperes)`          | `KTimeUnitInstance`             |
| `I = Q · f`    | `(1 of milli.coulombs) * (50 of hertz)`        | `KElectricCurrentUnitInstance`  |
| `Q = I / f`    | `(0.05 of amperes) / (50 of hertz)`            | `KChargeUnitInstance`           |
| `Q بوحدة Ah`   | `charge into ampereHours`                      | `Double`                        |
| `Q بوحدة mAh`  | `charge into milli.ampereHours`                | `Double`                        |
| `Q / e`        | `charge into elementaryCharges`                | `Double`                        |
| `W = Q · U`    | `(3600 of coulombs) * (12 of volts)`           | `KEnergyUnitInstance`           |
| `U = W / Q`    | `(43_200 of joules) / (3600 of coulombs)`      | `KVoltageUnitInstance`          |
| `C = Q / U`    | `(0.01 of coulombs) / (10 of volts)`           | `KCapacitanceUnitInstance`      |
| `Q = C · U`    | `(1000 of micro.farads) * (10 of volts)`       | `KChargeUnitInstance`           |
| `U = Q / C`    | `(0.01 of coulombs) / (1000 of micro.farads)`  | `KVoltageUnitInstance`          |
| `C بوحدة µF`   | `c into micro.farads`                          | `Double`                        |
| `τ = R · C`    | `(10 of kilo.ohms) * (100 of micro.farads)`    | `KMixedUnitInstance` (s)        |

### 3.3 المجال وكثافة التيار وخواصّ المواد

| الرياضيات      | Kotlin                                                     | النتيجة                               |
|----------------|------------------------------------------------------------|---------------------------------------|
| `E = U / d`    | `(230 of volts) / (2 of milli.meters)`                     | `KElectricFieldStrengthUnitInstance`  |
| `U = E · d`    | `(115 of kilo.voltsPerMeter) * (2 of milli.meters)`        | `KVoltageUnitInstance`                |
| `d = U / E`    | `(230 of volts) / (115 of kilo.voltsPerMeter)`             | `KLengthUnitInstance`                 |
| `F = E · Q`    | `(1000 of voltsPerMeter) * (1 of micro.coulombs)`          | `KForceUnitInstance`                  |
| `E = F / Q`    | `(0.001 of newtons) / (1 of micro.coulombs)`               | `KElectricFieldStrengthUnitInstance`  |
| `Q = F / E`    | `(0.001 of newtons) / (1000 of voltsPerMeter)`             | `KChargeUnitInstance`                 |
| `J = I / A`    | `(16 of amperes) / ((1.5 of milli.meters) * (1 of milli.meters))`          | `KCurrentDensityUnitInstance`         |
| `I = J · A`    | `currentDensity * ((2.5 of milli.meters) * (1 of milli.meters))`           | `KElectricCurrentUnitInstance`        |
| `A = I / J`    | `(16 of amperes) / currentDensity`                         | `KAreaUnitInstance`                   |
| `ρ_Q = Q / V`  | `(12 of milli.coulombs) / (4 of liters)`                   | `KChargeDensityUnitInstance`          |
| `Q = ρ_Q · V`  | `chargeDensity * (2 of liters)`                            | `KChargeUnitInstance`                 |
| `V = Q / ρ_Q`  | `(12 of milli.coulombs) / chargeDensity`                   | `KVolumeUnitInstance`                 |
| `λ = Q / l`    | `(1 of micro.coulombs) / (2 of meters)`                    | `KLinearChargeDensityUnitInstance`    |
| `Q = λ · l`    | `linearChargeDensity * (5 of meters)`                      | `KChargeUnitInstance`                 |
| `l = Q / λ`    | `(1 of micro.coulombs) / linearChargeDensity`              | `KLengthUnitInstance`                 |
| `D = Q / A`    | `(1 of micro.coulombs) / ((1 of meters) * (0.5 of meters))`          | `KElectricFluxDensityUnitInstance`    |
| `Q = D · A`    | `(2 of micro.coulombsPerSquareMeter) * ((1 of meters) * (0.5 of meters))` | `KChargeUnitInstance`            |
| `A = Q / D`    | `(1 of micro.coulombs) / (2 of micro.coulombsPerSquareMeter)` | `KAreaUnitInstance`                |
| `D = ε · E`    | `(1 of vacuumPermittivity) * (1000 of voltsPerMeter)`      | `KElectricFluxDensityUnitInstance`    |
| `ε = D / E`    | `electricFluxDensity / (1000 of voltsPerMeter)`            | `KPermittivityUnitInstance`           |
| `E = D / ε`    | `electricFluxDensity / (1 of vacuumPermittivity)`          | `KElectricFieldStrengthUnitInstance`  |
| `C = ε · l`    | `(1 of vacuumPermittivity) * (0.05 of meters)`             | `KCapacitanceUnitInstance`            |
| `ε = C / l`    | `(1000 of micro.farads) / (0.05 of meters)`                | `KPermittivityUnitInstance`           |
| `l = C / ε`    | `(1000 of micro.farads) / (1 of vacuumPermittivity)`       | `KLengthUnitInstance`                 |
| `ρ = R · l`    | `(0.017 of ohms) * (1 of meters)`                          | `KResistivityUnitInstance`            |
| `R = ρ / l`    | `(17 of nano.ohmMeters) / (0.001 of meters)`               | `KResistanceUnitInstance`             |
| `l = ρ / R`    | `(17 of nano.ohmMeters) / (0.017 of ohms)`                 | `KLengthUnitInstance`                 |
| `σ = 1 / ρ`    | `1 / (17 of nano.ohmMeters)`                               | `KConductivityUnitInstance`           |
| `ρ = 1 / σ`    | `1 / (58 of megasiemensPerMeter)`                          | `KResistivityUnitInstance`            |
| `G = σ · l`    | `(58 of megasiemensPerMeter) * (0.001 of meters)`          | `KConductanceUnitInstance`            |
| `σ = G / l`    | `(0.02 of siemens) / (0.001 of meters)`                    | `KConductivityUnitInstance`           |
| `l = G / σ`    | `(0.02 of siemens) / (58 of megasiemensPerMeter)`          | `KLengthUnitInstance`                 |
| `σ بوحدة µS/cm`| `conductivity into microsiemensPerCentimeter`              | `Double`                              |
| `µ = v / E`    | `(0.01 of meters / seconds) / (1000 of voltsPerMeter)`      | `KElectricMobilityUnitInstance`       |
| `v = µ · E`    | `(1e-5 of squareMetersPerVoltSecond) * (1000 of voltsPerMeter)` | `KSpeedUnitInstance`             |
| `E = v / µ`    | `(0.01 of meters / seconds) / (1e-5 of squareMetersPerVoltSecond)` | `KElectricFieldStrengthUnitInstance` |
| `p = Q · l`    | `(1 of nano.coulombs) * (1 of milli.meters)`               | `KElectricDipoleMomentUnitInstance`   |
| `Q = p / l`    | `(1e-12 of coulombMeters) / (1 of milli.meters)`           | `KChargeUnitInstance`                 |
| `l = p / Q`    | `(1e-12 of coulombMeters) / (1 of nano.coulombs)`          | `KLengthUnitInstance`                 |
| `p بوحدة D`    | `dipoleMoment into debyes`                                 | `Double`                              |

### 3.4 المغناطيسية

| الرياضيات      | Kotlin                                                        | النتيجة                               |
|----------------|---------------------------------------------------------------|---------------------------------------|
| `Φ = B · A`    | `(1.2 of teslas) * ((0.1 of meters) * (0.1 of meters))`                  | `KMagneticFluxUnitInstance`           |
| `B = Φ / A`    | `(12 of milli.webers) / ((0.1 of meters) * (0.1 of meters))`             | `KMagneticFluxDensityUnitInstance`    |
| `A = Φ / B`    | `(12 of milli.webers) / (1.2 of teslas)`                      | `KAreaUnitInstance`                   |
| `Φ = U · t`    | `(230 of volts) * (1 of milli.seconds)`                       | `KMagneticFluxUnitInstance`           |
| `U = Φ / t`    | `(0.23 of webers) / (1 of milli.seconds)`                     | `KVoltageUnitInstance`                |
| `U = Φ · f`    | `(12 of milli.webers) * (50 of hertz)`                        | `KVoltageUnitInstance`                |
| `Φ = U / f`    | `(230 of volts) / (50 of hertz)`                              | `KMagneticFluxUnitInstance`           |
| `t = Φ / U`    | `(0.23 of webers) / (230 of volts)`                           | `KTimeUnitInstance`                   |
| `L = Φ / I`    | `(12 of milli.webers) / (2 of amperes)`                       | `KInductanceUnitInstance`             |
| `Φ = L · I`    | `(6 of milli.henries) * (2 of amperes)`                       | `KMagneticFluxUnitInstance`           |
| `I = Φ / L`    | `(12 of milli.webers) / (6 of milli.henries)`                 | `KElectricCurrentUnitInstance`        |
| `X_L = ω · L`  | `(6 of milli.henries) * (50 of hertz)`                        | `KResistanceUnitInstance`             |
| `L = X_L / f`  | `(0.3 of ohms) / (50 of hertz)`                               | `KInductanceUnitInstance`             |
| `H = I / l`    | `(2 of amperes) / (0.1 of meters)`                            | `KMagneticFieldStrengthUnitInstance`  |
| `I = H · l`    | `(20 of amperesPerMeter) * (0.1 of meters)`                   | `KElectricCurrentUnitInstance`        |
| `I = l · H`    | `(0.1 of meters) * (20 of amperesPerMeter)`                   | `KElectricCurrentUnitInstance`        |
| `B = µ · H`    | `(1 of vacuumPermeability) * (20 of amperesPerMeter)`         | `KMagneticFluxDensityUnitInstance`    |
| `µ = B / H`    | `(1.2 of teslas) / (20 of amperesPerMeter)`                   | `KPermeabilityUnitInstance`           |
| `H = B / µ`    | `(1.2 of teslas) / (1 of vacuumPermeability)`                 | `KMagneticFieldStrengthUnitInstance`  |
| `L = µ · l`    | `(1 of vacuumPermeability) * (0.1 of meters)`                 | `KInductanceUnitInstance`             |
| `µ = L / l`    | `(6 of milli.henries) / (0.1 of meters)`                      | `KPermeabilityUnitInstance`           |
| `l = L / µ`    | `(6 of milli.henries) / (1 of vacuumPermeability)`            | `KLengthUnitInstance`                 |
| `R_m = 1 / L`  | `1 / (6 of milli.henries)`                                    | `KReluctanceUnitInstance`             |
| `L = 1 / R_m`  | `1 / (166 of inverseHenries)`                                 | `KInductanceUnitInstance`             |
| `R_m = I / Φ`  | `(2 of amperes) / (12 of milli.webers)`                       | `KReluctanceUnitInstance`             |
| `Θ = R_m · Φ`  | `(166 of ampereTurnsPerWeber) * (12 of milli.webers)`         | `KElectricCurrentUnitInstance`        |
| `Φ = I / R_m`  | `(2 of amperes) / (166 of ampereTurnsPerWeber)`               | `KMagneticFluxUnitInstance`           |
| `B بوحدة G`    | `b into gauss`                                                | `Double`                              |
| `Φ بوحدة Mx`   | `flux into maxwells`                                          | `Double`                              |
| `H بوحدة Oe`   | `h into oersteds`                                             | `Double`                              |

---

## 4. الديناميكا الحرارية

### 4.1 درجة الحرارة — نقطة مقابل فرق

| الرياضيات         | Kotlin                                             | النتيجة                              |
|-------------------|----------------------------------------------------|--------------------------------------|
| `T = 20 °C`       | `20 of celsius`                                    | `KTemperatureUnitInstance`           |
| `T بوحدة K`       | `(20 of celsius) into kelvin`                      | `Double` (293.15)                    |
| `T بوحدة °F`      | `(20 of celsius) into fahrenheit`                  | `Double` (68.0)                      |
| `T بوحدة °R`      | `(20 of celsius) into rankine`                     | `Double`                             |
| `ΔT = T₂ − T₁`    | `(40 of celsius) - (12 of celsius)`                | `KTemperatureDifferenceUnitInstance` |
| `ΔT = 20 K`       | `KTemperatureDifference.ofKelvin(20)`              | `KTemperatureDifferenceUnitInstance` |
| `T₂ = T₁ + ΔT`    | `(20 of celsius) + KTemperatureDifference.ofKelvin(5)` | `KTemperatureUnitInstance`       |
| `T₂ = T₁ − ΔT`    | `(20 of celsius) - KTemperatureDifference.ofKelvin(5)` | `KTemperatureUnitInstance`       |
| `ΔT₁ + ΔT₂`       | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | `KTemperatureDifferenceUnitInstance` |
| `2 · ΔT`          | `KTemperatureDifference.ofKelvin(20) * 2`          | `KTemperatureDifferenceUnitInstance` |
| `T₁ + T₂`         | *مرفوض عند التصريف* (جمع نقطة إلى نقطة بلا معنى)   | —                                    |
| `∇T = ΔT / d`     | `KTemperatureDifference.ofKelvin(21) / (0.3 of meters)` | `KTemperatureGradientUnitInstance` |
| `ΔT = ∇T · d`     | `(70 of kelvinPerMeter) * (0.3 of meters)`         | `KTemperatureDifferenceUnitInstance` |
| `d = ΔT / ∇T`     | `KTemperatureDifference.ofKelvin(21) / (70 of kelvinPerMeter)` | `KLengthUnitInstance`     |
| `∇T بوحدة K/km`   | `gradient into kelvinPerKilometer`                 | `Double`                             |
| `α = 1 / ΔT`      | `1 / KTemperatureDifference.ofKelvin(1)`           | `KThermalExpansionUnitInstance`      |
| `ΔT = 1 / α`      | `1 / (12e-6 of perKelvin)`                         | `KTemperatureDifferenceUnitInstance` |
| `ΔL = α · L · ΔT` | `(12e-6 of perKelvin).elongationOf(10 of meters, KTemperatureDifference.ofKelvin(30))` | `KLengthUnitInstance` |
| `α بوحدة ppm/K`   | `expansion into ppmPerKelvin`                      | `Double`                             |

### 4.2 الحرارة والسعة الحرارية والتدفّق الحراري

| الرياضيات         | Kotlin                                                        | النتيجة                             |
|-------------------|---------------------------------------------------------------|-------------------------------------|
| `C = Q / ΔT`      | `(627_600 of joules) / KTemperatureDifference.ofKelvin(1)`    | `KHeatCapacityUnitInstance`         |
| `Q = C · ΔT`      | `(627_600 of joulesPerKelvin) * KTemperatureDifference.ofKelvin(28)` | `KEnergyUnitInstance`         |
| `Q = ΔT · C`      | `KTemperatureDifference.ofKelvin(28) * (627_600 of joulesPerKelvin)` | `KEnergyUnitInstance`        |
| `ΔT = Q / C`      | `(17.57 of mega.joules) / (627_600 of joulesPerKelvin)`       | `KTemperatureDifferenceUnitInstance`|
| `c = C / m`       | `(627_600 of joulesPerKelvin) / (150 of kilo.grams)`          | `KSpecificHeatCapacityUnitInstance` |
| `C = c · m`       | `(4184 of joulesPerKilogramKelvin) * (150 of kilo.grams)`     | `KHeatCapacityUnitInstance`         |
| `C = m · c`       | `(150 of kilo.grams) * (4184 of joulesPerKilogramKelvin)`     | `KHeatCapacityUnitInstance`         |
| `m = C / c`       | `(627_600 of joulesPerKelvin) / (4184 of joulesPerKilogramKelvin)` | `KMassUnitInstance`            |
| `q = c · ΔT`      | `(4184 of joulesPerKilogramKelvin) * KTemperatureDifference.ofKelvin(28)` | `KSpecificEnergyUnitInstance` |
| `Q = q · m`       | `(117_152 of joulesPerKilogram) * (150 of kilo.grams)`        | `KEnergyUnitInstance`               |
| `q = Q / m`       | `(17.57 of mega.joules) / (150 of kilo.grams)`                | `KSpecificEnergyUnitInstance`       |
| `m = Q / q`       | `(17.57 of mega.joules) / (117_152 of joulesPerKilogram)`     | `KMassUnitInstance`                 |
| `ΔT = q / c`      | `(117_152 of joulesPerKilogram) / (4184 of joulesPerKilogramKelvin)` | `KTemperatureDifferenceUnitInstance` |
| `c = q / ΔT`      | `(117_152 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(28)` | `KSpecificHeatCapacityUnitInstance` |
| `c بوحدة cal/(g·K)` | `heatCapacity into caloriesPerGramKelvin`                   | `Double`                            |
| `q بوحدة Wh/kg`   | `specificEnergy into wattHoursPerKilogram`                    | `Double`                            |
| `Φ = Q / t`       | `(17.57 of mega.joules) / (30 of minutes)`                    | `KPowerUnitInstance`                |
| `Q = Φ · t`       | `(9.76 of kilo.watts) * (30 of minutes)`                      | `KEnergyUnitInstance`               |
| `t = Q / Φ`       | `(17.57 of mega.joules) / (9.76 of kilo.watts)`               | `KTimeUnitInstance`                 |
| `S = Q / T`       | `(1000 of joules) / KTemperatureDifference.ofKelvin(300)`     | `KHeatCapacityUnitInstance` (J/K)   |
| `S بوحدة J/K`     | `entropy into joulesPerKelvin`                                | `Double`                            |

### 4.3 انتقال الحرارة

| الرياضيات         | Kotlin                                                            | النتيجة                                 |
|-------------------|-------------------------------------------------------------------|-----------------------------------------|
| `q̇ = Φ / A`       | `(1200 of watts) / ((5 of meters) * (3 of meters))`                        | `KHeatFluxDensityUnitInstance`          |
| `Φ = q̇ · A`       | `(80 of wattsPerSquareMeter) * ((5 of meters) * (3 of meters))`            | `KPowerUnitInstance`                    |
| `A = Φ / q̇`       | `(1200 of watts) / (80 of wattsPerSquareMeter)`                   | `KAreaUnitInstance`                     |
| `q̇ = λ · ∇T`      | `(0.035 of wattsPerMeterKelvin) * (70 of kelvinPerMeter)`         | `KHeatFluxDensityUnitInstance`          |
| `∇T = q̇ / λ`      | `(2.45 of wattsPerSquareMeter) / (0.035 of wattsPerMeterKelvin)`  | `KTemperatureGradientUnitInstance`      |
| `λ = q̇ / ∇T`      | `(2.45 of wattsPerSquareMeter) / (70 of kelvinPerMeter)`          | `KThermalConductivityUnitInstance`      |
| `U = λ / d`       | `(0.035 of wattsPerMeterKelvin) / (0.2 of meters)`                | `KHeatTransferCoefficientUnitInstance`  |
| `λ = U · d`       | `(0.175 of wattsPerSquareMeterKelvin) * (0.2 of meters)`          | `KThermalConductivityUnitInstance`      |
| `d = λ / U`       | `(0.035 of wattsPerMeterKelvin) / (0.175 of wattsPerSquareMeterKelvin)` | `KLengthUnitInstance`              |
| `q̇ = U · ΔT`      | `(0.175 of wattsPerSquareMeterKelvin) * KTemperatureDifference.ofKelvin(21)` | `KHeatFluxDensityUnitInstance` |
| `U = q̇ / ΔT`      | `(3.675 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(21)` | `KHeatTransferCoefficientUnitInstance` |
| `ΔT = q̇ / U`      | `(3.675 of wattsPerSquareMeter) / (0.175 of wattsPerSquareMeterKelvin)` | `KTemperatureDifferenceUnitInstance` |
| `R = d / λ`       | `(0.2 of meters) / (0.035 of wattsPerMeterKelvin)`                | `KThermalResistanceUnitInstance`        |
| `R = 1 / U`       | `1 / (0.175 of wattsPerSquareMeterKelvin)`                        | `KThermalResistanceUnitInstance`        |
| `U = 1 / R`       | `1 / (5.71 of squareMeterKelvinPerWatt)`                          | `KHeatTransferCoefficientUnitInstance`  |
| `d = R · λ`       | `(5.71 of squareMeterKelvinPerWatt) * (0.035 of wattsPerMeterKelvin)` | `KLengthUnitInstance`                |
| `λ = d / R`       | `(0.2 of meters) / (5.71 of squareMeterKelvinPerWatt)`            | `KThermalConductivityUnitInstance`      |
| `ΔT = R · q̇`      | `(5.71 of squareMeterKelvinPerWatt) * (3.675 of wattsPerSquareMeter)` | `KTemperatureDifferenceUnitInstance` |
| `R = ΔT / q̇`      | `KTemperatureDifference.ofKelvin(21) / (3.675 of wattsPerSquareMeter)` | `KThermalResistanceUnitInstance`    |
| `q̇ = ΔT / R`      | `KTemperatureDifference.ofKelvin(21) / (5.71 of squareMeterKelvinPerWatt)` | `KHeatFluxDensityUnitInstance`  |
| `R بوحدة tog`     | `thermalResistance into tog`                                      | `Double`                                |
| `R بوحدة clo`     | `thermalResistance into clo`                                      | `Double`                                |
| `a = λ / (ρ · c)` | `(0.6 of wattsPerMeterKelvin).diffusivityWith(water, (4184 of joulesPerKilogramKelvin))` | `KDiffusivityUnitInstance` |
| `ρ = λ / (a · c)` | `diffusivity.densityWith(conductivity, specificHeatCapacity)`     | `KDensityUnitInstance`                  |
| `c = λ / (a · ρ)` | `diffusivity.specificHeatCapacityWith(conductivity, density)`     | `KSpecificHeatCapacityUnitInstance`     |
| `q̇ بوحدة Btu/(h·ft²)`| `heatFluxDensity into btusPerHourSquareFoot`                   | `Double`                                |
| `λ بوحدة Btu/(h·ft·°F)` | `conductivity into btusPerHourFootFahrenheit`               | `Double`                                |

### 4.4 كمّية المادة والكمّيات المولية

| الرياضيات         | Kotlin                                                       | النتيجة                            |
|-------------------|--------------------------------------------------------------|------------------------------------|
| `M = m / n`       | `(18 of grams) / (1 of moles)`                               | `KMolarMassUnitInstance`           |
| `m = M · n`       | `(18 of gramsPerMole) * (2.5 of moles)`                      | `KMassUnitInstance`                |
| `m = n · M`       | `(2.5 of moles) * (18 of gramsPerMole)`                      | `KMassUnitInstance`                |
| `n = m / M`       | `(45 of grams) / (18 of gramsPerMole)`                       | `KAmountOfSubstanceUnitInstance`   |
| `M بوحدة kg/mol`  | `molarMass into kilogramsPerMole`                            | `Double`                           |
| `V_m = V / n`     | `(22.4 of liters) / (1 of moles)`                            | `KMolarVolumeUnitInstance`         |
| `V = V_m · n`     | `(22.4 of litersPerMole) * (3 of moles)`                     | `KVolumeUnitInstance`              |
| `n = V / V_m`     | `(67.2 of liters) / (22.4 of litersPerMole)`                 | `KAmountOfSubstanceUnitInstance`   |
| `V_m = M / ρ`     | `(18 of gramsPerMole) / water`                               | `KMolarVolumeUnitInstance`         |
| `M = V_m · ρ`     | `(18 of cubicCentimetersPerMole) * water`                    | `KMolarMassUnitInstance`           |
| `ρ = M / V_m`     | `(18 of gramsPerMole) / (18 of cubicCentimetersPerMole)`     | `KDensityUnitInstance`             |
| `E_m = E / n`     | `(286 of kilo.joules) / (1 of moles)`                        | `KMolarEnergyUnitInstance`         |
| `E = E_m · n`     | `(286 of kilo.joulesPerMole) * (2 of moles)`                 | `KEnergyUnitInstance`              |
| `n = E / E_m`     | `(572 of kilo.joules) / (286 of kilo.joulesPerMole)`         | `KAmountOfSubstanceUnitInstance`   |
| `C_m = C / n`     | `(75.3 of joulesPerKelvin) / (1 of moles)`                   | `KMolarHeatCapacityUnitInstance`   |
| `C = C_m · n`     | `(75.3 of joulesPerMoleKelvin) * (4 of moles)`               | `KHeatCapacityUnitInstance`        |
| `n = C / C_m`     | `(301.2 of joulesPerKelvin) / (75.3 of joulesPerMoleKelvin)` | `KAmountOfSubstanceUnitInstance`   |
| `E_m = C_m · ΔT`  | `(75.3 of joulesPerMoleKelvin) * KTemperatureDifference.ofKelvin(20)` | `KMolarEnergyUnitInstance` |
| `C_m = E_m / ΔT`  | `(1506 of joulesPerMole) / KTemperatureDifference.ofKelvin(20)` | `KMolarHeatCapacityUnitInstance` |
| `ΔT = E_m / C_m`  | `(1506 of joulesPerMole) / (75.3 of joulesPerMoleKelvin)`    | `KTemperatureDifferenceUnitInstance` |
| `n بوحدة mmol`    | `amount into milli.moles`                                    | `Double`                           |
| `n بوحدة lbmol`   | `amount into poundMoles`                                     | `Double`                           |
| `E_m بوحدة eV`    | `molarEnergy into electronVoltsPerEntity`                    | `Double`                           |

---

## 5. تقنية المعلومات

| الرياضيات         | Kotlin                                              | النتيجة                         |
|-------------------|-----------------------------------------------------|---------------------------------|
| `1 B = 8 bit`     | `(1 of bytes) == (8 of bits)`                       | `true`                          |
| `S بوحدة MiB`     | `(1_048_576 of bytes) into mebi.bytes`              | `Double` (1.0)                  |
| `S بوحدة MB`      | `(1_048_576 of bytes) into mega.bytes`              | `Double` (≈ 1.049)              |
| `S بوحدة GiB`     | `(8 of giga.bytes) into gibi.bytes`                 | `Double`                        |
| `Ṡ = S / t`       | `(100 of mega.bytes) / (10 of seconds)`             | `KDataRateUnitInstance`         |
| `S = Ṡ · t`       | `(10 of (mega.bytes / seconds)) * (30 of seconds)`  | `KStorageUnitInstance`          |
| `S = t · Ṡ`       | `(30 of seconds) * (10 of (mega.bytes / seconds))`  | `KStorageUnitInstance`          |
| `t = S / Ṡ`       | `(4.7 of giga.bytes) / (10 of (mega.bytes / seconds))` | `KTimeUnitInstance`          |
| `Ṡ بوحدة Mbit/s`  | `rate into (mega.bits / seconds)`                   | `Double`                        |
| `Ṡ بوحدة MB/s`    | `rate into (mega.bytes / seconds)`                  | `Double`                        |
| `D = S / A`       | `(1 of tera.bytes) / ((10 of centi.meters) * (10 of centi.meters))` | `KStorageDensityUnitInstance`   |
| `S = D · A`       | `density * ((10 of centi.meters) * (5 of centi.meters))`            | `KStorageUnitInstance`          |
| `A = S / D`       | `(1 of tera.bytes) / density`                       | `KAreaUnitInstance`             |
| `S₁ + S₂`         | `(700 of mega.bytes) + (1.4 of giga.bytes)`         | `KStorageUnitInstance`          |
| `S₁ − S₂`         | `(2 of tera.bytes) - (350 of giga.bytes)`           | `KStorageUnitInstance`          |
| `S / n`           | `(1 of tera.bytes) / 4`                             | `KStorageUnitInstance`          |

---

## 6. البادئات والقراءات والقوى

| الرياضيات         | Kotlin                                    | النتيجة                   |
|-------------------|-------------------------------------------|---------------------------|
| `1 km`            | `1 of kilo.meters`                        | `KLengthUnitInstance`     |
| `1 mm`            | `1 of milli.meters`                       | `KLengthUnitInstance`     |
| `1 µs`            | `1 of micro.seconds`                      | `KTimeUnitInstance`       |
| `1 nF`            | `1 of nano.farads`                        | `KCapacitanceUnitInstance`|
| `1 MW`            | `1 of mega.watts`                         | `KPowerUnitInstance`      |
| `1 GΩ`            | `1 of giga.ohms`                          | `KResistanceUnitInstance` |
| `1 Qm`            | `1 of quetta.meters`                      | `KLengthUnitInstance`     |
| `1 qm`            | `1 of quecto.meters`                      | `KLengthUnitInstance`     |
| `1 KiB`           | `1 of kibi.bytes`                         | `KStorageUnitInstance`    |
| `1 m بوحدة mm`    | `(1 of meters) into milli.meters`         | `Double` (1000.0)         |
| `1 mi بوحدة km`   | `(1 of miles) into kilo.meters`           | `Double` (≈ 1.609)        |
| `1 lb بوحدة kg`   | `(1 of pounds) into kilo.grams`           | `Double` (≈ 0.454)        |
| `A = a²`          | `(2 of kilo.meters) pow 2`                | `KAreaUnitInstance`       |
| `V = a³`          | `(2 of meters) pow 3`                     | `KVolumeUnitInstance`     |
| `A = l · w`       | `(5 of meters) * (3 of meters)`           | `KAreaUnitInstance`       |
| `V = A · h`       | `((5 of meters) * (3 of meters)) * (2 of meters)` | `KVolumeUnitInstance` |
| `l = A / w`       | `((5 of meters) * (3 of meters)) / (3 of meters)`| `KLengthUnitInstance`     |
| `A بوحدة ha`      | `area into hectares`                      | `Double`                  |
| `A بوحدة ac`      | `area into acres`                         | `Double`                  |
| `V بوحدة l`       | `volume into liters`                      | `Double`                  |
| `V بوحدة gal`     | `volume into usGallons`                   | `Double`                  |
| `x^-1`            | `(1 of seconds) pow -1`                   | `KMixedUnitInstance`      |

!!! warning "`pow` كمُعامَل"
تم تعريف `pow` على النوع المفتوح `KDistanceUnitInstance`، لذلك فإن `(2 of meters) pow 2` هو **مساحة أثناء التشغيل**
لكنه ثابتًا مجرد مسافة. هذا يكفي *لقراءة* قيمة (`into (meters pow 2)`) — لكن مُعاملًا ذا نوع محدد مثل
`pressure * area` يحتاج إلى مساحة ذات نوع ثابت. أنشئها بدلًا من ذلك عبر مُعاملات الطول ذات النوع المحدد:
`(0.2 of meters) * (0.1 of meters)` هو `KAreaUnitInstance`، و`liters` هو `KVolumeUnitInstance` جاهز.
| `طباعة`           | `(5 of kilo.meters).toString()`           | `"5000.0 m"` (الوحدة الأساسية) |
| `طباعة (مخصّصة)`   | `"${v into (kilo.meters / hours)} km/h"`  | `"80.0 km/h"`             |

---

## 7. ثوابت الجدول الدوري

كل ثابت في [`KChemicalElement`](periodic-table.md) هو نسخة وحدة محكومة بالنوع، ويمكن إدراجه مباشرةً في أي صيغة من
الصيغ السابقة.

| الرياضيات               | Kotlin                                                                 | النتيجة                             |
|-------------------------|------------------------------------------------------------------------|-------------------------------------|
| `M(Au)`                 | `KChemicalElement.GOLD.molarMass`                                      | `KMolarMassUnitInstance`            |
| `ρ(Au)`                 | `KChemicalElement.GOLD.density`                                        | `KDensityUnitInstance?`             |
| `m = ρ · V`             | `KChemicalElement.GOLD.density!! * ((7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters))`       | `KMassUnitInstance`                 |
| `n = m / M`             | `(1081 of grams) / KChemicalElement.GOLD.molarMass`                    | `KAmountOfSubstanceUnitInstance`    |
| `V_m(Fe)`               | `KChemicalElement.IRON.molarVolume`                                    | `KMolarVolumeUnitInstance?`         |
| `T_melt(Fe)`            | `KChemicalElement.IRON.meltingPoint`                                   | `KTemperatureUnitInstance?`         |
| `T_boil(Fe)`            | `KChemicalElement.IRON.boilingPoint`                                   | `KTemperatureUnitInstance?`         |
| `ΔT = T_b − T_m`        | `KChemicalElement.IRON.boilingPoint!! - KChemicalElement.IRON.meltingPoint!!` | `KTemperatureDifferenceUnitInstance` |
| `c(Cu)`                 | `KChemicalElement.COPPER.specificHeatCapacity`                         | `KSpecificHeatCapacityUnitInstance?`|
| `C = c · m`             | `KChemicalElement.COPPER.specificHeatCapacity!! * (5 of kilo.grams)`   | `KHeatCapacityUnitInstance`         |
| `λ(Cu)`                 | `KChemicalElement.COPPER.thermalConductivity`                          | `KThermalConductivityUnitInstance?` |
| `R = d / λ`             | `(2 of milli.meters) / KChemicalElement.COPPER.thermalConductivity!!`  | `KThermalResistanceUnitInstance`    |
| `ρ_el(Cu)`              | `KChemicalElement.COPPER.electricalResistivity`                        | `KResistivityUnitInstance?`         |
| `R = ρ_el / l`          | `KChemicalElement.COPPER.electricalResistivity!! / (1 of milli.meters)` | `KResistanceUnitInstance` |
| `E_ion(H)`              | `KChemicalElement.HYDROGEN.ionizationEnergy`                           | `KEnergyUnitInstance?`              |
| `r_at(C)`               | `KChemicalElement.CARBON.atomicRadius`                                 | `KLengthUnitInstance?`              |
| `Z`                     | `KChemicalElement.LEAD.ordinalNumber`                                  | `Int` (82)                          |

---

## 8. أمثلة محلولة

### 8.1 ارتفاع درجة الحرارة نتيجة ارتفاع الضغط (ثبات الحجم، غاي-لوساك)

تحتوي قنّينة فولاذية محكمة الإغلاق هواءً عند **20 °C** و**8 bar** (مطلق). يرفع ضوء الشمس الضغط إلى **9.2 bar**.
عند ثبات الحجم يكون `p / T = const`، ومن ثمّ `T₂ = T₁ · p₂ / p₁` و`ΔT = T₂ − T₁`.

لاحظ الكمّيتين المختلفتين العاملتين هنا: **نسبة** الضغط عديمة البُعد (تُقرأ عبر `into`)، بينما تبقى حسابات درجة الحرارة
محكومة بالنوع — فطرح درجتَي حرارة مطلقتين يعطي *فرق درجة حرارة*، لا درجة حرارة مطلقة أخرى أبدًا.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val p1 = 8.0 of bars
val p2 = 9.2 of bars
val t1 = 20 of celsius

val ratio = (p2 into bars) / (p1 into bars)        // 1.15 (dimensionless)
val t2 = ((t1 into kelvin) * ratio) of kelvin      // KTemperatureUnitInstance
val deltaT = t2 - t1                               // KTemperatureDifferenceUnitInstance

t2 into celsius        // ≈ 63.9 °C
deltaT.value           // ≈ 43.97 (ΔK)
```

### 8.2 كم من الطاقة يلزم لتسخين حوض استحمام؟

`Q = c · m · ΔT` — ‏150 لترًا من الماء من 12 °C إلى 40 °C.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val heat = (water * (150 of kilo.grams)) * ((40 of celsius) - (12 of celsius))

heat into mega.joules            // ≈ 17.57 MJ
val boiler = 9 of kilo.watts
(heat / boiler) into hours       // ≈ 0.54 h — how long the boiler needs
```

### 8.3 فقد الحرارة عبر جدار

`Φ = U · A · ΔT`، مع `U = λ / d` لطبقة عزل سماكتها 20 cm (`λ = 0.035 W/(m·K)`)، وجدار مساحته 15 m²، وفرق 21 K.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val u = (0.035 of wattsPerMeterKelvin) / (20 of centi.meters)   // W/(m²·K)
val flux = u * KTemperatureDifference.ofKelvin(21)              // W/m²
val loss = flux * ((5 of meters) * (3 of meters))               // KPowerUnitInstance

loss into watts    // ≈ 55 W
```

### 8.4 هبوط الجهد على كابل نحاسي

`R = ρ · l / A`، ثم `U = R · I` — ‏10 m من نحاس مقطعه 1.5 mm² يحمل 16 A.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.toLength

val rho = 17 of nano.ohmMeters
val length = 10 of meters
val area = (1.5 of milli.meters) * (1 of milli.meters)

// A / l is reduced explicitly - `area / length` has no typed operator
val equivalent = (area.toUnit() / length.toUnit()).toLength()
val r = rho / equivalent               // KResistanceUnitInstance, ≈ 0.113 Ω
val drop = r * (16 of amperes)         // KVoltageUnitInstance

drop into volts                        // ≈ 1.8 V
```

### 8.5 الطاقة الحركية ومسافة الكبح

`E = ½ · m · v²` و`s = E / F` لسيارة كتلتها 1200 kg تسير بسرعة 100 km/h وتكبح بقوّة 8 kN.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.energy.toEnergy

val v = 100 of kilo.meters / hours
val m = 1200 of kilo.grams
// neither `momentum * speed` nor `energy / force` is a typed operator:
// the first is typed by the form-recognition hook, the second is read out and rebuilt
val energy = (((m * v).toUnit() * v.toUnit()) / 2).toEnergy()
val distance = ((energy into joules) / ((8 of kilo.newtons) into newtons)) of meters

distance into meters                    // ≈ 58 m
```

### 8.6 زمن التنزيل

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.it.datarate.*
import org.pcsoft.framework.kunit.kinematic.time.*

val file = 4.7 of giga.bytes
val line = (50 of mega.bits) / (1 of seconds)   // 50 Mbit/s

(file / line) into minutes                       // ≈ 12.5 min
```

### 8.7 محرّك: عزم الدوران والسرعة والقدرة

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val speed = 3000 of revolutionsPerMinute
val torque = (15 of kilo.watts) / speed      // KEnergyUnitInstance, read as N·m
torque into joules                            // ≈ 47.7 N·m

val power = torque * speed                    // back to KPowerUnitInstance
power into kilo.watts                         // 15.0
```

### 8.8 سبيكة ذهب

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters)
val mass = KChemicalElement.GOLD.density!! * volume
val amount = mass / KChemicalElement.GOLD.molarMass

mass into kilo.grams   // ≈ 10.8 kg
amount into moles      // ≈ 55 mol
```

---

## إلى أين بعد ذلك

* [الوحدات المركبة](mixed-units.md) — ماذا يحدث حين لا يكون للتركيب نوع مقنّن.
* [إضافة وحدات مخصصة](custom-units.md) — أضِف كمّيتك الخاصّة واحصل على كل ما سبق لها.
* [المنسّق](formatter/formatting.md) — اعرض أيًّا من هذه النتائج كنصّ أو LaTeX أو MathML أو Typst.
* [الجدول الدوري](periodic-table.md) — ثوابت العناصر المستخدمة في §7 و§8.8.
</content>
</invoke>
