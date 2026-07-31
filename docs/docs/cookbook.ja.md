# クックブック — 例で見る公式集

このページは KUnit の **道案内ページ**です: 数百におよぶ物理計算を、それぞれ2通りで示します — 左側は物理学や工学で
書かれる形、右側は KUnit を用いて Kotlin で書かれる形です。

2つの列のポイントは、それらが *同じ*公式であるということです。KUnit は公式を数値のお手玉に翻訳することを 決して要求しません:
`v = s / t` はそのまま `distance / time` であり、その結果は素の `Double` ではなく `KSpeedUnitInstance` です。

## 表の読み方

* **数学**列 — 教科書の形式。`·` は乗算、`/` は分数、指数には Unicode の上付き文字 (`²`、`⁻¹`)を使用します。
* **Kotlin**列 — 2つの動詞 `of`(作成)と `into`(読み取り)を使った、完全に実行可能な式。
* **結果**列 — KUnit が返す型。型付きの結果はすべて本物の単位インスタンスなので、そのまま次の公式に 渡すことができます。

すべての例は、関係するグループの語彙がインポート済みであることを前提としています:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo          // and the other prefix builders
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
// … one import per unit group used
```

!!! tip "単位そのものが文書化されている場所"
このページが示すのは **量がどのように組み合わさるか**です。各グループで利用できるトークン、記号、接頭辞は それぞれ専用のページに一覧されており、各分野の概要ページからたどれます:
[運動学](units/kinematics/overview.md)、[力学](units/mechanics/overview.md)、
[電気工学](units/electrical/overview.md)、[熱力学](units/thermodynamics/overview.md)、
[情報技術](units/information/overview.md)。

---

## 1. 運動学 — 運動

### 1.1 距離、時間、速度

| 数学           | Kotlin                                        | 結果                    |
|----------------|-----------------------------------------------|-------------------------|
| `v = s / t`    | `(120 of kilo.meters) / (1.5 of hours)`       | `KSpeedUnitInstance`    |
| `s = v · t`    | `v * (3 of hours)`                            | `KLengthUnitInstance`   |
| `s = t · v`    | `(3 of hours) * v`                            | `KLengthUnitInstance`   |
| `t = s / v`    | `(240 of kilo.meters) / v`                    | `KTimeUnitInstance`     |
| `v = s · f`    | `(2.5 of meters) * (80 of rpm)`               | `KSpeedUnitInstance`    |
| `v in km/h`    | `v into (kilo.meters / hours)`                | `Double`                |
| `v in knots`   | `v into knots`                                | `Double`                |
| `v as Mach`    | `v into mach`                                 | `Double`                |
| `v / c`        | `v into speedOfLight`                         | `Double`                |
| `s = s₁ + s₂`  | `(5 of kilo.meters) + (3 of miles)`           | `KLengthUnitInstance`   |
| `s = s₁ − s₂`  | `(5 of kilo.meters) - (800 of meters)`        | `KLengthUnitInstance`   |
| `s̄ = s / n`    | `(120 of kilo.meters) / 4`                    | `KLengthUnitInstance`   |
| `t = t₁ + t₂`  | `(90 of minutes) + (45 of seconds)`           | `KTimeUnitInstance`     |
| `v̄ = Σs / Σt`  | `((120 of kilo.meters) + (30 of kilo.meters)) / ((1.5 of hours) + (0.5 of hours))` | `KSpeedUnitInstance` |

### 1.2 加速度

| 数学                 | Kotlin                                                | 結果                        |
|----------------------|-------------------------------------------------------|-----------------------------|
| `a = Δv / t`         | `((10 of meters) / (1 of seconds)) / (2 of seconds)`  | `KAccelerationUnitInstance` |
| `a = Δv / t`         | `(100 of kilometersPerHour) / (4.6 of seconds)`       | `KAccelerationUnitInstance` |
| `v = a · t`          | `a * (2 of seconds)`                                  | `KSpeedUnitInstance`        |
| `v = t · a`          | `(2 of seconds) * a`                                  | `KSpeedUnitInstance`        |
| `t = Δv / a`         | `(30 of metersPerSecond) / (3 of metersPerSecondSquared)` | `KTimeUnitInstance`     |
| `a / g`              | `a into standardGravities`                            | `Double`                    |
| `a in Gal`           | `a into gals`                                         | `Double`                    |
| `a = g`(自由落下)    | `1 of standardGravities`                              | `KAccelerationUnitInstance` |
| `v = g · t`          | `(1 of standardGravities) * (3 of seconds)`           | `KSpeedUnitInstance`        |
| `s = ½ · g · t²`     | `((1 of standardGravities) * (3 of seconds)) * (3 of seconds) / 2` | `KLengthUnitInstance` |

### 1.3 周波数と周期

| 数学           | Kotlin                                  | 結果                      |
|----------------|-----------------------------------------|---------------------------|
| `f = 1 / T`    | `1 / (0.02 of seconds)`                 | `KFrequencyUnitInstance`  |
| `T = 1 / f`    | `1 / (50 of hertz)`                     | `KTimeUnitInstance`       |
| `f in kHz`     | `(50_000 of hertz) into kilo.hertz`     | `Double`                  |
| `n in rpm`     | `(50 of hertz) into rpm`                | `Double`                  |
| `n in bpm`     | `(1.2 of hertz) into bpm`               | `Double`                  |
| `f_frame`      | `(60 of fps) into hertz`                | `Double`                  |
| `v = π · d · f`| `(Math.PI * 0.7 of meters) * (900 of rpm)` | `KSpeedUnitInstance`   |

### 1.4 体積流量

| 数学           | Kotlin                                             | 結果                        |
|----------------|----------------------------------------------------|-----------------------------|
| `q̇ = V / t`    | `(600 of liters) / (2 of minutes)`                 | `KVolumeFlowUnitInstance`   |
| `V = q̇ · t`    | `q * (15 of minutes)`                              | `KVolumeUnitInstance`       |
| `V = t · q̇`    | `(15 of minutes) * q`                              | `KVolumeUnitInstance`       |
| `t = V / q̇`    | `(1 of (meters pow 3)) / q`                        | `KTimeUnitInstance`         |
| `q̇ in l/min`   | `q into litersPerMinute`                           | `Double`                    |
| `q̇ in m³/h`    | `q into cubicMetersPerHour`                        | `Double`                    |
| `q̇ in gpm`     | `q into usGallonsPerMinute`                        | `Double`                    |
| `ṁ = ρ · q̇`    | `(1000 of kilo.grams) / (1 of (meters pow 3)) * q` | `KMassFlowUnitInstance`     |

---

## 2. 力学 — 力とエネルギー

### 2.1 ニュートンの法則

| 数学           | Kotlin                                                        | 結果                        |
|----------------|---------------------------------------------------------------|-----------------------------|
| `F = m · a`    | `(1200 of kilo.grams) * (2.5 of metersPerSecondSquared)`      | `KForceUnitInstance`        |
| `F = a · m`    | `(2.5 of metersPerSecondSquared) * (1200 of kilo.grams)`      | `KForceUnitInstance`        |
| `a = F / m`    | `(3000 of newtons) / (1200 of kilo.grams)`                    | `KAccelerationUnitInstance` |
| `m = F / a`    | `(3000 of newtons) / (2.5 of metersPerSecondSquared)`         | `KMassUnitInstance`         |
| `F_g = m · g`  | `(75 of kilo.grams) * (1 of standardGravities)`               | `KForceUnitInstance`        |
| `F in kN`      | `f into kilo.newtons`                                         | `Double`                    |
| `F in lbf`     | `f into poundsForce`                                          | `Double`                    |
| `F in dyn`     | `f into dynes`                                                | `Double`                    |
| `F in kp`      | `f into ponds`                                                | `Double`                    |
| `ΣF`           | `(300 of newtons) + (2 of kilo.newtons)`                      | `KForceUnitInstance`        |

### 2.2 仕事、エネルギー、仕事率

| 数学              | Kotlin                                              | 結果                   |
|-------------------|-----------------------------------------------------|------------------------|
| `W = F · s`       | `(500 of newtons) * (12 of meters)`                 | `KEnergyUnitInstance`  |
| `W = s · F`       | `(12 of meters) * (500 of newtons)`                 | `KEnergyUnitInstance`  |
| `E_pot = m · g · h` | `((80 of kilo.grams) * (1 of standardGravities)) * (10 of meters)` | `KEnergyUnitInstance` |
| `P = W / t`       | `(6000 of joules) / (12 of seconds)`                | `KPowerUnitInstance`   |
| `W = P · t`       | `(2 of kilo.watts) * (3 of hours)`                  | `KEnergyUnitInstance`  |
| `t = W / P`       | `(21.6 of mega.joules) / (2 of kilo.watts)`         | `KTimeUnitInstance`    |
| `P = F · v`       | `(400 of newtons) * (25 of metersPerSecond)`        | `KPowerUnitInstance`   |
| `F = P / v`       | `(10 of kilo.watts) / (25 of metersPerSecond)`      | `KForceUnitInstance`   |
| `v = P / F`       | `(10 of kilo.watts) / (400 of newtons)`             | `KSpeedUnitInstance`   |
| `W = P / f`       | `(60 of watts) / (50 of hertz)`                     | `KEnergyUnitInstance`  |
| `E in kWh`        | `e into (kilo.watts * hours)`                       | `Double`               |
| `E in kcal`       | `e into kilo.calories`                              | `Double`               |
| `E in eV`         | `e into electronVolts`                              | `Double`               |
| `E in Btu`        | `e into britishThermalUnits`                        | `Double`               |
| `P in hp`         | `p into metricHorsePowers`                          | `Double`               |
| `P in bhp`        | `p into mechanicalHorsePowers`                      | `Double`               |

### 2.3 運動量と力積

| 数学            | Kotlin                                                | 結果                    |
|-----------------|-------------------------------------------------------|-------------------------|
| `p = m · v`     | `(1200 of kilo.grams) * (25 of metersPerSecond)`      | `KMomentumUnitInstance` |
| `p = v · m`     | `(25 of metersPerSecond) * (1200 of kilo.grams)`      | `KMomentumUnitInstance` |
| `v = p / m`     | `(30_000 of kilogramMetersPerSecond) / (1200 of kilo.grams)` | `KSpeedUnitInstance` |
| `m = p / v`     | `(30_000 of kilogramMetersPerSecond) / (25 of metersPerSecond)` | `KMassUnitInstance` |
| `J = F · t`     | `(2 of kilo.newtons) * (0.15 of seconds)`             | `KMomentumUnitInstance` |
| `J = t · F`     | `(0.15 of seconds) * (2 of kilo.newtons)`             | `KMomentumUnitInstance` |
| `F = p / t`     | `(300 of newtonSeconds) / (0.15 of seconds)`          | `KForceUnitInstance`    |
| `t = p / F`     | `(300 of newtonSeconds) / (2 of kilo.newtons)`        | `KTimeUnitInstance`     |
| `p in N·s`      | `p into newtonSeconds`                                | `Double`                |
| `p in lb·ft/s`  | `p into poundFeetPerSecond`                           | `Double`                |

### 2.4 圧力と応力

| 数学             | Kotlin                                                     | 結果                     |
|------------------|------------------------------------------------------------|--------------------------|
| `p = F / A`      | `(2000 of newtons) / ((0.1 of meters) * (0.05 of meters))` | `KPressureUnitInstance`  |
| `F = p · A`      | `(3 of bars) * (0.02 of (meters pow 2))`                   | `KForceUnitInstance`     |
| `A = F / p`      | `(6000 of newtons) / (3 of bars)`                          | `KAreaUnitInstance`      |
| `p in bar`       | `p into bars`                                              | `Double`                 |
| `p in psi`       | `p into psis`                                              | `Double`                 |
| `p in atm`       | `p into atmospheres`                                       | `Double`                 |
| `p in Torr`      | `p into torrs`                                             | `Double`                 |
| `σ = F / A`      | `(50 of kilo.newtons) / (0.0005 of (meters pow 2))`        | `KPressureUnitInstance`  |
| `ε = ΔL / L`     | `((0.6 of milli.meters) / (2 of meters)).toStrain()`       | `KStrainUnitInstance`    |
| `E = σ / ε`      | `(100 of mega.pascals) / (0.0005 of ratio)`                | `KPressureUnitInstance`  |
| `σ = E · ε`      | `(210 of giga.pascals) * (0.0005 of ratio)`                | `KPressureUnitInstance`  |
| `ε in %`         | `strain into percent`                                      | `Double`                 |
| `ε in ‰`         | `strain into perMille`                                     | `Double`                 |
| `ε in µε`        | `strain into microstrain`                                  | `Double`                 |
| `p_hyd = ρ · g · h` | `((1000 of kilo.grams) / (1 of (meters pow 3))) * (1 of standardGravities) * (10 of meters)` | `KPressureUnitInstance` |

### 2.5 回転

| 数学           | Kotlin                                                         | 結果                               |
|----------------|----------------------------------------------------------------|------------------------------------|
| `ω = φ / t`    | `(1 of turns) / (1 of seconds)`                                | `KAngularVelocityUnitInstance`     |
| `φ = ω · t`    | `(3000 of revolutionsPerMinute) * (2 of seconds)`              | `KAngleUnitInstance`               |
| `t = φ / ω`    | `(10 of turns) / (3000 of revolutionsPerMinute)`               | `KTimeUnitInstance`                |
| `α = Δω / t`   | `(3000 of revolutionsPerMinute) / (5 of seconds)`              | `KAngularAccelerationUnitInstance` |
| `ω = α · t`    | `(2 of radiansPerSecondSquared) * (5 of seconds)`              | `KAngularVelocityUnitInstance`     |
| `t = ω / α`    | `(10 of (radians / seconds)) / (2 of radiansPerSecondSquared)` | `KTimeUnitInstance`                |
| `ω in rpm`     | `w into revolutionsPerMinute`                                  | `Double`                           |
| `ω in rad/s`   | `w into (radians / seconds)`                                   | `Double`                           |
| `J = m · r²`   | `(2 of kilo.grams) * ((0.3 of meters) pow 2)`                  | `KInertiaUnitInstance`             |
| `m = J / r²`   | `(0.18 of kilogramMetersSquared) / ((0.3 of meters) pow 2)`    | `KMassUnitInstance`                |
| `L = J · ω`    | `(0.18 of kilogramMetersSquared) * (50 of revolutionsPerSecond)` | `KAngularMomentumUnitInstance`   |
| `J = L / ω`    | `(56 of kilogramMetersSquaredPerSecond) / (50 of revolutionsPerSecond)` | `KInertiaUnitInstance`     |
| `ω = L / J`    | `(56 of kilogramMetersSquaredPerSecond) / (0.18 of kilogramMetersSquared)` | `KAngularVelocityUnitInstance` |
| `L = p · r`    | `(300 of newtonSeconds) * (0.4 of meters)`                     | `KAngularMomentumUnitInstance`     |
| `p = L / r`    | `(120 of jouleSeconds) / (0.4 of meters)`                      | `KMomentumUnitInstance`            |
| `M = F · r`    | `(100 of newtons) * (2 of meters)`                             | `KEnergyUnitInstance`(N·m)         |
| `M = J · α`    | `(0.18 of kilogramMetersSquared) * (2 of radiansPerSecondSquared)` | `KEnergyUnitInstance`(N·m)     |
| `M = P / ω`    | `(15 of kilo.watts) / (3000 of revolutionsPerMinute)`          | `KEnergyUnitInstance`(N·m)         |
| `P = M · ω`    | `(48 of joules) * (3000 of revolutionsPerMinute)`              | `KPowerUnitInstance`               |
| `ω = P / M`    | `(15 of kilo.watts) / (48 of joules)`                          | `KAngularVelocityUnitInstance`     |
| `α = M / J`    | `(48 of joules) / (0.18 of kilogramMetersSquared)`             | `KAngularAccelerationUnitInstance` |
| `J = M / α`    | `(48 of joules) / (2 of radiansPerSecondSquared)`              | `KInertiaUnitInstance`             |
| `Ω = φ · φ`    | `(0.5 of radians) * (0.5 of radians)`                          | `KSolidAngleUnitInstance`          |
| `φ = Ω / φ`    | `(0.25 of steradians) / (0.5 of radians)`                      | `KAngleUnitInstance`               |
| `φ in °`       | `angle into degrees`                                           | `Double`                           |
| `φ in gon`     | `angle into gradians`                                          | `Double`                           |
| `Ω in deg²`    | `omega into squareDegrees`                                     | `Double`                           |

### 2.6 密度

| 数学              | Kotlin                                                    | 結果                          |
|-------------------|-----------------------------------------------------------|-------------------------------|
| `ρ = m / V`       | `(7850 of kilo.grams) / (1 of (meters pow 3))`            | `KDensityUnitInstance`        |
| `m = ρ · V`       | `steel * (0.25 of (meters pow 3))`                        | `KMassUnitInstance`           |
| `m = V · ρ`       | `(0.25 of (meters pow 3)) * steel`                        | `KMassUnitInstance`           |
| `V = m / ρ`       | `(1962.5 of kilo.grams) / steel`                          | `KVolumeUnitInstance`         |
| `ρ in g/cm³`      | `steel into (grams / (centi.meters pow 3))`               | `Double`                      |
| `ρ_A = m / A`     | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))`    | `KAreaDensityUnitInstance`    |
| `m = ρ_A · A`     | `areaDensity * (12 of (meters pow 2))`                    | `KMassUnitInstance`           |
| `A = m / ρ_A`     | `(60 of kilo.grams) / areaDensity`                        | `KAreaUnitInstance`           |
| `ρ_A = ρ · d`     | `steel * (2 of milli.meters)`                             | `KAreaDensityUnitInstance`    |
| `ρ = ρ_A / d`     | `areaDensity / (2 of milli.meters)`                       | `KDensityUnitInstance`        |
| `ρ_l = m / l`     | `(90 of grams) / (1000 of meters)`                        | `KLinearDensityUnitInstance`  |
| `m = ρ_l · l`     | `(0.09 of gramsPerMeter) * (2500 of meters)`              | `KMassUnitInstance`           |
| `l = m / ρ_l`     | `(225 of grams) / (0.09 of gramsPerMeter)`                | `KLengthUnitInstance`         |
| `ρ_l in tex`      | `linearDensity into tex`                                  | `Double`                      |
| `ρ_l in den`      | `linearDensity into denier`                               | `Double`                      |
| `v = V / m`       | `(1 of (meters pow 3)) / (1000 of kilo.grams)`            | `KSpecificVolumeUnitInstance` |
| `v = 1 / ρ`       | `1 / steel`                                               | `KSpecificVolumeUnitInstance` |
| `ρ = 1 / v`       | `1 / specificVolume`                                      | `KDensityUnitInstance`        |
| `V = v · m`       | `(0.001 of cubicMetersPerKilogram) * (500 of kilo.grams)` | `KVolumeUnitInstance`         |
| `m = V / v`       | `(0.5 of (meters pow 3)) / (0.001 of cubicMetersPerKilogram)` | `KMassUnitInstance`       |

### 2.7 流れ、粘度、表面張力

| 数学           | Kotlin                                                       | 結果                        |
|----------------|--------------------------------------------------------------|-----------------------------|
| `ṁ = m / t`    | `(180 of kilo.grams) / (1 of hours)`                         | `KMassFlowUnitInstance`     |
| `m = ṁ · t`    | `(0.05 of kilogramsPerSecond) * (2 of hours)`                | `KMassUnitInstance`         |
| `t = m / ṁ`    | `(360 of kilo.grams) / (0.05 of kilogramsPerSecond)`         | `KTimeUnitInstance`         |
| `ṁ = ρ · q̇`    | `water * (2 of litersPerSecond)`                             | `KMassFlowUnitInstance`     |
| `q̇ = ṁ / ρ`    | `(2 of kilogramsPerSecond) / water`                          | `KVolumeFlowUnitInstance`   |
| `ρ = ṁ / q̇`    | `(2 of kilogramsPerSecond) / (2 of litersPerSecond)`         | `KDensityUnitInstance`      |
| `ṁ in t/h`     | `massFlow into tonnesPerHour`                                | `Double`                    |
| `η = p · t`    | `(1 of pascals) * (0.001 of seconds)`                        | `KViscosityUnitInstance`    |
| `ν = η / ρ`    | `(0.001 of pascalSeconds) / water`                           | `KDiffusivityUnitInstance`  |
| `η = ν · ρ`    | `(1e-6 of squareMetersPerSecond) * water`                    | `KViscosityUnitInstance`    |
| `ρ = η / ν`    | `(0.001 of pascalSeconds) / (1e-6 of squareMetersPerSecond)` | `KDensityUnitInstance`      |
| `η in cP`      | `viscosity into centipoises`                                 | `Double`                    |
| `ν in cSt`     | `kinematicViscosity into centistokes`                        | `Double`                    |
| `σ = F / l`    | `(0.0728 of newtons) / (1 of meters)`                        | `KLineForceUnitInstance`    |
| `F = σ · l`    | `(72.8 of (milli.newtons / meters)) * (0.05 of meters)`      | `KForceUnitInstance`        |
| `l = F / σ`    | `(0.00364 of newtons) / (72.8 of (milli.newtons / meters))`  | `KLengthUnitInstance`       |
| `W = σ · A`    | `(0.0728 of newtonsPerMeter) * (0.5 of (meters pow 2))`      | `KEnergyUnitInstance`       |
| `A = W / σ`    | `(0.0364 of joules) / (0.0728 of newtonsPerMeter)`           | `KAreaUnitInstance`         |
| `k = F / s`    | `(500 of newtons) / (0.01 of meters)`                        | `KLineForceUnitInstance`    |
| `F = k · s`    | `(50 of newtonsPerMillimeter) * (0.004 of meters)`           | `KForceUnitInstance`        |

---

## 3. 電気工学

### 3.1 オームの法則と電力

| 数学           | Kotlin                                     | 結果                           |
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
| `U in kV`      | `u into kilo.volts`                        | `Double`                       |
| `U in mV`      | `u into milli.volts`                       | `Double`                       |
| `R in kΩ`      | `r into kilo.ohms`                         | `Double`                       |
| `S in kVA`     | `s into kilo.voltAmperes`                  | `Double`                       |
| `Q in var`     | `q into vars`                              | `Double`                       |

### 3.2 電荷と静電容量

| 数学           | Kotlin                                         | 結果                            |
|----------------|------------------------------------------------|---------------------------------|
| `Q = I · t`    | `(2 of amperes) * (30 of minutes)`             | `KChargeUnitInstance`           |
| `Q = t · I`    | `(30 of minutes) * (2 of amperes)`             | `KChargeUnitInstance`           |
| `I = Q / t`    | `(3600 of coulombs) / (30 of minutes)`         | `KElectricCurrentUnitInstance`  |
| `t = Q / I`    | `(3600 of coulombs) / (2 of amperes)`          | `KTimeUnitInstance`             |
| `I = Q · f`    | `(1 of milli.coulombs) * (50 of hertz)`        | `KElectricCurrentUnitInstance`  |
| `Q = I / f`    | `(0.05 of amperes) / (50 of hertz)`            | `KChargeUnitInstance`           |
| `Q in Ah`      | `charge into ampereHours`                      | `Double`                        |
| `Q in mAh`     | `charge into milli.ampereHours`                | `Double`                        |
| `Q / e`        | `charge into elementaryCharges`                | `Double`                        |
| `W = Q · U`    | `(3600 of coulombs) * (12 of volts)`           | `KEnergyUnitInstance`           |
| `U = W / Q`    | `(43_200 of joules) / (3600 of coulombs)`      | `KVoltageUnitInstance`          |
| `C = Q / U`    | `(0.01 of coulombs) / (10 of volts)`           | `KCapacitanceUnitInstance`      |
| `Q = C · U`    | `(1000 of micro.farads) * (10 of volts)`       | `KChargeUnitInstance`           |
| `U = Q / C`    | `(0.01 of coulombs) / (1000 of micro.farads)`  | `KVoltageUnitInstance`          |
| `C in µF`      | `c into micro.farads`                          | `Double`                        |
| `τ = R · C`    | `(10 of kilo.ohms) * (100 of micro.farads)`    | `KMixedUnitInstance`(s)         |

### 3.3 電界、電流密度、材料特性

| 数学           | Kotlin                                                     | 結果                                  |
|----------------|------------------------------------------------------------|---------------------------------------|
| `E = U / d`    | `(230 of volts) / (2 of milli.meters)`                     | `KElectricFieldStrengthUnitInstance`  |
| `U = E · d`    | `(115 of kilo.voltsPerMeter) * (2 of milli.meters)`        | `KVoltageUnitInstance`                |
| `d = U / E`    | `(230 of volts) / (115 of kilo.voltsPerMeter)`             | `KLengthUnitInstance`                 |
| `F = E · Q`    | `(1000 of voltsPerMeter) * (1 of micro.coulombs)`          | `KForceUnitInstance`                  |
| `E = F / Q`    | `(0.001 of newtons) / (1 of micro.coulombs)`               | `KElectricFieldStrengthUnitInstance`  |
| `Q = F / E`    | `(0.001 of newtons) / (1000 of voltsPerMeter)`             | `KChargeUnitInstance`                 |
| `J = I / A`    | `(16 of amperes) / (1.5 of (milli.meters pow 2))`          | `KCurrentDensityUnitInstance`         |
| `I = J · A`    | `currentDensity * (2.5 of (milli.meters pow 2))`           | `KElectricCurrentUnitInstance`        |
| `A = I / J`    | `(16 of amperes) / currentDensity`                         | `KAreaUnitInstance`                   |
| `ρ_Q = Q / V`  | `(12 of milli.coulombs) / (4 of liters)`                   | `KChargeDensityUnitInstance`          |
| `Q = ρ_Q · V`  | `chargeDensity * (2 of liters)`                            | `KChargeUnitInstance`                 |
| `V = Q / ρ_Q`  | `(12 of milli.coulombs) / chargeDensity`                   | `KVolumeUnitInstance`                 |
| `λ = Q / l`    | `(1 of micro.coulombs) / (2 of meters)`                    | `KLinearChargeDensityUnitInstance`    |
| `Q = λ · l`    | `linearChargeDensity * (5 of meters)`                      | `KChargeUnitInstance`                 |
| `l = Q / λ`    | `(1 of micro.coulombs) / linearChargeDensity`              | `KLengthUnitInstance`                 |
| `D = Q / A`    | `(1 of micro.coulombs) / (0.5 of (meters pow 2))`          | `KElectricFluxDensityUnitInstance`    |
| `Q = D · A`    | `(2 of micro.coulombsPerSquareMeter) * (0.5 of (meters pow 2))` | `KChargeUnitInstance`            |
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
| `σ in µS/cm`   | `conductivity into microsiemensPerCentimeter`              | `Double`                              |
| `µ = v / E`    | `(0.01 of metersPerSecond) / (1000 of voltsPerMeter)`      | `KElectricMobilityUnitInstance`       |
| `v = µ · E`    | `(1e-5 of squareMetersPerVoltSecond) * (1000 of voltsPerMeter)` | `KSpeedUnitInstance`             |
| `E = v / µ`    | `(0.01 of metersPerSecond) / (1e-5 of squareMetersPerVoltSecond)` | `KElectricFieldStrengthUnitInstance` |
| `p = Q · l`    | `(1 of nano.coulombs) * (1 of milli.meters)`               | `KElectricDipoleMomentUnitInstance`   |
| `Q = p / l`    | `(1e-12 of coulombMeters) / (1 of milli.meters)`           | `KChargeUnitInstance`                 |
| `l = p / Q`    | `(1e-12 of coulombMeters) / (1 of nano.coulombs)`          | `KLengthUnitInstance`                 |
| `p in D`       | `dipoleMoment into debyes`                                 | `Double`                              |

### 3.4 磁気

| 数学           | Kotlin                                                        | 結果                                  |
|----------------|---------------------------------------------------------------|---------------------------------------|
| `Φ = B · A`    | `(1.2 of teslas) * (0.01 of (meters pow 2))`                  | `KMagneticFluxUnitInstance`           |
| `B = Φ / A`    | `(12 of milli.webers) / (0.01 of (meters pow 2))`             | `KMagneticFluxDensityUnitInstance`    |
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
| `B in G`       | `b into gauss`                                                | `Double`                              |
| `Φ in Mx`      | `flux into maxwells`                                          | `Double`                              |
| `H in Oe`      | `h into oersteds`                                             | `Double`                              |

---

## 4. 熱力学

### 4.1 温度 — 点としての温度と差分としての温度

| 数学              | Kotlin                                             | 結果                                 |
|-------------------|----------------------------------------------------|--------------------------------------|
| `T = 20 °C`       | `20 of celsius`                                    | `KTemperatureUnitInstance`           |
| `T in K`          | `(20 of celsius) into kelvin`                      | `Double`(293.15)                     |
| `T in °F`         | `(20 of celsius) into fahrenheit`                  | `Double`(68.0)                       |
| `T in °R`         | `(20 of celsius) into rankine`                     | `Double`                             |
| `ΔT = T₂ − T₁`    | `(40 of celsius) - (12 of celsius)`                | `KTemperatureDifferenceUnitInstance` |
| `ΔT = 20 K`       | `KTemperatureDifference.ofKelvin(20)`              | `KTemperatureDifferenceUnitInstance` |
| `T₂ = T₁ + ΔT`    | `(20 of celsius) + KTemperatureDifference.ofKelvin(5)` | `KTemperatureUnitInstance`       |
| `T₂ = T₁ − ΔT`    | `(20 of celsius) - KTemperatureDifference.ofKelvin(5)` | `KTemperatureUnitInstance`       |
| `ΔT₁ + ΔT₂`       | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | `KTemperatureDifferenceUnitInstance` |
| `2 · ΔT`          | `KTemperatureDifference.ofKelvin(20) * 2`          | `KTemperatureDifferenceUnitInstance` |
| `T₁ + T₂`         | *コンパイル時に拒否される*(点と点の加算は意味を持たない) | —                                 |
| `∇T = ΔT / d`     | `KTemperatureDifference.ofKelvin(21) / (0.3 of meters)` | `KTemperatureGradientUnitInstance` |
| `ΔT = ∇T · d`     | `(70 of kelvinPerMeter) * (0.3 of meters)`         | `KTemperatureDifferenceUnitInstance` |
| `d = ΔT / ∇T`     | `KTemperatureDifference.ofKelvin(21) / (70 of kelvinPerMeter)` | `KLengthUnitInstance`     |
| `∇T in K/km`      | `gradient into kelvinPerKilometer`                 | `Double`                             |
| `α = 1 / ΔT`      | `1 / KTemperatureDifference.ofKelvin(1)`           | `KThermalExpansionUnitInstance`      |
| `ΔT = 1 / α`      | `1 / (12e-6 of perKelvin)`                         | `KTemperatureDifferenceUnitInstance` |
| `ΔL = α · L · ΔT` | `(12e-6 of perKelvin).elongationOf(10 of meters, KTemperatureDifference.ofKelvin(30))` | `KLengthUnitInstance` |
| `α in ppm/K`      | `expansion into ppmPerKelvin`                      | `Double`                             |

### 4.2 熱、熱容量、熱流

| 数学              | Kotlin                                                        | 結果                                |
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
| `c in cal/(g·K)`  | `heatCapacity into caloriesPerGramKelvin`                     | `Double`                            |
| `q in Wh/kg`      | `specificEnergy into wattHoursPerKilogram`                    | `Double`                            |
| `Φ = Q / t`       | `(17.57 of mega.joules) / (30 of minutes)`                    | `KPowerUnitInstance`                |
| `Q = Φ · t`       | `(9.76 of kilo.watts) * (30 of minutes)`                      | `KEnergyUnitInstance`               |
| `t = Q / Φ`       | `(17.57 of mega.joules) / (9.76 of kilo.watts)`               | `KTimeUnitInstance`                 |
| `S = Q / T`       | `(1000 of joules) / KTemperatureDifference.ofKelvin(300)`     | `KHeatCapacityUnitInstance`(J/K)    |
| `S in J/K`        | `entropy into joulesPerKelvin`                                | `Double`                            |

### 4.3 熱伝達

| 数学              | Kotlin                                                            | 結果                                    |
|-------------------|-------------------------------------------------------------------|-----------------------------------------|
| `q̇ = Φ / A`       | `(1200 of watts) / (15 of (meters pow 2))`                        | `KHeatFluxDensityUnitInstance`          |
| `Φ = q̇ · A`       | `(80 of wattsPerSquareMeter) * (15 of (meters pow 2))`            | `KPowerUnitInstance`                    |
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
| `R in tog`        | `thermalResistance into tog`                                      | `Double`                                |
| `R in clo`        | `thermalResistance into clo`                                      | `Double`                                |
| `a = λ / (ρ · c)` | `(0.6 of wattsPerMeterKelvin).diffusivityWith(water, (4184 of joulesPerKilogramKelvin))` | `KDiffusivityUnitInstance` |
| `ρ = λ / (a · c)` | `diffusivity.densityWith(conductivity, specificHeatCapacity)`     | `KDensityUnitInstance`                  |
| `c = λ / (a · ρ)` | `diffusivity.specificHeatCapacityWith(conductivity, density)`     | `KSpecificHeatCapacityUnitInstance`     |
| `q̇ in Btu/(h·ft²)`| `heatFluxDensity into btusPerHourSquareFoot`                       | `Double`                                |
| `λ in Btu/(h·ft·°F)` | `conductivity into btusPerHourFootFahrenheit`                  | `Double`                                |

### 4.4 物質量とモル量

| 数学              | Kotlin                                                       | 結果                               |
|-------------------|--------------------------------------------------------------|------------------------------------|
| `M = m / n`       | `(18 of grams) / (1 of moles)`                               | `KMolarMassUnitInstance`           |
| `m = M · n`       | `(18 of gramsPerMole) * (2.5 of moles)`                      | `KMassUnitInstance`                |
| `m = n · M`       | `(2.5 of moles) * (18 of gramsPerMole)`                      | `KMassUnitInstance`                |
| `n = m / M`       | `(45 of grams) / (18 of gramsPerMole)`                       | `KAmountOfSubstanceUnitInstance`   |
| `M in kg/mol`     | `molarMass into kilogramsPerMole`                            | `Double`                           |
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
| `n in mmol`       | `amount into milli.moles`                                    | `Double`                           |
| `n in lbmol`      | `amount into poundMoles`                                     | `Double`                           |
| `E_m in eV`       | `molarEnergy into electronVoltsPerEntity`                    | `Double`                           |

---

## 5. 情報技術

| 数学              | Kotlin                                              | 結果                            |
|-------------------|-----------------------------------------------------|---------------------------------|
| `1 B = 8 bit`     | `(1 of bytes) == (8 of bits)`                       | `true`                          |
| `S in MiB`        | `(1_048_576 of bytes) into mebi.bytes`              | `Double`(1.0)                   |
| `S in MB`         | `(1_048_576 of bytes) into mega.bytes`              | `Double`(≈ 1.049)               |
| `S in GiB`        | `(8 of giga.bytes) into gibi.bytes`                 | `Double`                        |
| `Ṡ = S / t`       | `(100 of mega.bytes) / (10 of seconds)`             | `KDataRateUnitInstance`         |
| `S = Ṡ · t`       | `(10 of (mega.bytes / seconds)) * (30 of seconds)`  | `KStorageUnitInstance`          |
| `S = t · Ṡ`       | `(30 of seconds) * (10 of (mega.bytes / seconds))`  | `KStorageUnitInstance`          |
| `t = S / Ṡ`       | `(4.7 of giga.bytes) / (10 of (mega.bytes / seconds))` | `KTimeUnitInstance`          |
| `Ṡ in Mbit/s`     | `rate into (mega.bits / seconds)`                   | `Double`                        |
| `Ṡ in MB/s`       | `rate into (mega.bytes / seconds)`                  | `Double`                        |
| `D = S / A`       | `(1 of tera.bytes) / (100 of (centi.meters pow 2))` | `KStorageDensityUnitInstance`   |
| `S = D · A`       | `density * (50 of (centi.meters pow 2))`            | `KStorageUnitInstance`          |
| `A = S / D`       | `(1 of tera.bytes) / density`                       | `KAreaUnitInstance`             |
| `S₁ + S₂`         | `(700 of mega.bytes) + (1.4 of giga.bytes)`         | `KStorageUnitInstance`          |
| `S₁ − S₂`         | `(2 of tera.bytes) - (350 of giga.bytes)`           | `KStorageUnitInstance`          |
| `S / n`           | `(1 of tera.bytes) / 4`                             | `KStorageUnitInstance`          |

---

## 6. 接頭辞、読み取り、べき乗

| 数学              | Kotlin                                    | 結果                      |
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
| `1 m in mm`       | `(1 of meters) into milli.meters`         | `Double`(1000.0)          |
| `1 mi in km`      | `(1 of miles) into kilo.meters`           | `Double`(≈ 1.609)         |
| `1 lb in kg`      | `(1 of pounds) into kilo.grams`           | `Double`(≈ 0.454)         |
| `A = a²`          | `(2 of kilo.meters) pow 2`                | `KAreaUnitInstance`       |
| `V = a³`          | `(2 of meters) pow 3`                     | `KVolumeUnitInstance`     |
| `A = l · w`       | `(5 of meters) * (3 of meters)`           | `KAreaUnitInstance`       |
| `V = A · h`       | `((5 of meters) * (3 of meters)) * (2 of meters)` | `KVolumeUnitInstance` |
| `l = A / w`       | `((15 of (meters pow 2))) / (3 of meters)`| `KLengthUnitInstance`     |
| `A in ha`         | `area into hectares`                      | `Double`                  |
| `A in ac`         | `area into acres`                         | `Double`                  |
| `V in l`          | `volume into liters`                      | `Double`                  |
| `V in gal`        | `volume into usGallons`                   | `Double`                  |
| `x^-1`            | `(1 of seconds) pow -1`                   | `KMixedUnitInstance`      |
| `出力`            | `(5 of kilo.meters).toString()`           | `"5000.0 m"`(基本単位)    |
| `出力(カスタム)`  | `"${v into (kilo.meters / hours)} km/h"`  | `"80.0 km/h"`             |

---

## 7. 周期表の定数

[`KChemicalElement`](periodic-table.md) のすべての定数は型付きの単位インスタンスであり、上記のどの公式にも そのまま投入できます。

| 数学                    | Kotlin                                                                 | 結果                                |
|-------------------------|------------------------------------------------------------------------|-------------------------------------|
| `M(Au)`                 | `KChemicalElement.GOLD.molarMass`                                      | `KMolarMassUnitInstance`            |
| `ρ(Au)`                 | `KChemicalElement.GOLD.density`                                        | `KDensityUnitInstance?`             |
| `m = ρ · V`             | `KChemicalElement.GOLD.density!! * (56 of (centi.meters pow 3))`       | `KMassUnitInstance`                 |
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
| `Z`                     | `KChemicalElement.LEAD.ordinalNumber`                                  | `Int`(82)                           |

---

## 8. 実例集

### 8.1 圧力上昇から求める温度上昇(定容、ゲイ=リュサックの法則)

密閉された鋼製ボンベに **20 °C**、**8 bar**(絶対圧)の空気が入っています。日光により圧力が **9.2 bar** まで上昇します。
体積が一定であれば `p / T = const` なので、`T₂ = T₁ · p₂ / p₁` および `ΔT = T₂ − T₁` となります。

ここで働いている2つの異なる量に注目してください: 圧力の **比**は無次元であり (`into` で読み出します)、一方で 温度の計算は型付きのままです —
2つの絶対温度の減算が生むのは *温度差*であって、決して別の 絶対温度ではありません。

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

### 8.2 浴槽を温めるにはどれだけのエネルギーが必要か?

`Q = c · m · ΔT` — 150 l の水を 12 °C から 40 °C へ。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.kilo
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

### 8.3 壁を通しての熱損失

`Φ = U · A · ΔT`、ここで 20 cm の断熱層 (`λ = 0.035 W/(m·K)`)に対して `U = λ / d`、壁面積 15 m²、温度差 21 K です。

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
val loss = flux * (15 of (meters pow 2))                        // KPowerUnitInstance

loss into watts    // ≈ 55 W
```

### 8.4 銅ケーブルの電圧降下

`R = ρ · l / A`、続いて `U = R · I` — 1.5 mm² の銅線 10 m に 16 A を流した場合。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.ec.amperes
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.electric.voltage.volts

val rho = 17 of nano.ohmMeters
val length = 10 of meters
val area = 1.5 of (milli.meters pow 2)

val r = (rho / area) * length          // KResistanceUnitInstance, ≈ 0.113 Ω
val drop = r * (16 of amperes)         // KVoltageUnitInstance

drop into volts                        // ≈ 1.8 V
```

### 8.5 運動エネルギーと制動距離

1200 kg の自動車が 100 km/h で走行し 8 kN で制動する場合の `E = ½ · m · v²` と `s = E / F`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.kilometersPerHour
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams

val v = 100 of kilometersPerHour
val m = 1200 of kilo.grams
val energy = ((m * v) * v) / 2          // momentum * speed = energy
val distance = energy / (8 of kilo.newtons)

distance into meters                    // ≈ 58 m
```

### 8.6 ダウンロード時間

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

### 8.7 モーター: トルク、回転数、出力

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

### 8.8 金の延べ棒

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

## 次に読むもの

* [混合単位](mixed-units.md) — 組み合わせに標準化された型が存在しない場合に何が起こるか。
* [カスタム単位の追加](custom-units.md) — 独自の物理量を追加し、上記すべてをそれに対して利用する。
* [フォーマッター](formatter/formatting.md) — これらの結果をテキスト、LaTeX、MathML、Typst として描画する。
* [周期表](periodic-table.md) — §7 と §8.8 で使用した元素定数。
