# 电气工程 — 概述

包：`org.pcsoft.framework.kunit.ec`、`…voltage`、`…resistance`、`…charge`、`…conductance`、
`…magneticfieldstrength`、`…capacitance`、`…inductance`、`…magneticflux`、`…magneticfluxdensity`、
`…currentdensity`、`…chargedensity`、`…resistivity`、`…conductivity`、`…power`、`…energy`

电气工程将流过电路的电流、驱动它的电压,以及阻碍它的电阻联系在一起。这三者由**欧姆定律**相连,
KUnit 将该定律直接表达为类型化的 `*` 与 `/` 运算符:1 个**原生**基本量(电流)以及由基本量纲
**构造**的若干量(电压、电阻、电荷、电导与磁场强度)。

## 本主题的单位

| 单位 | 类型 | 基准单位 | 页面 |
|---|---|---|---|
| 电流 | 原生 | 安培(`A`) | [电流](ec.md) |
| 电压 | 构造 | 伏特(`V`) | [电压](voltage.md) |
| 电阻 | 构造 | 欧姆(`Ω`) | [电阻](resistance.md) |
| 电荷 | 构造 | 库仑(`C`) | [电荷](charge.md) |
| 电导 | 构造 | 西门子(`S`) | [电导](conductance.md) |
| 磁场强度 | 构造 | 安培每米(`A/m`) | [磁场强度](magneticfieldstrength.md) |
| 电容 | 构造 | 法拉(`F`) | [电容](capacitance.md) |
| 电感 | 构造 | 亨利(`H`) | [电感](inductance.md) |
| 磁通量 | 构造 | 韦伯(`Wb`) | [磁通量](magneticflux.md) |
| 磁通密度 | 构造 | 特斯拉(`T`) | [磁通密度](magneticfluxdensity.md) |
| 电流密度 | 构造 | 安培每平方米(`A/m²`) | [电流密度](currentdensity.md) |
| 电荷密度 | 构造 | 库仑每立方米(`C/m³`) | [电荷密度](chargedensity.md) |
| 电阻率 | 构造 | 欧姆米(`Ω·m`) | [电阻率](resistivity.md) |
| 电导率 | 构造 | 西门子每米(`S/m`) | [电导率](conductivity.md) |
| 功率 | 构造 | 瓦特(`W`) | [功率(电气)](power.md) |
| 能量 | 构造 | 焦耳(`J`) | [能量(电气)](energy.md) |

功率与能量在技术上分别是**同一个**量,与其他学科领域共享;它们按领域分别记录并互相交叉引用
([功率(力学)](../mechanics/power.md)、[功率(热力学)](../thermodynamics/power.md)、
[能量(力学)](../mechanics/energy.md)、[能量(热力学)](../thermodynamics/energy.md))。

## 作为类型化运算符的欧姆定律

| 表达式 | 结果 | 公式 |
|---|---|---|
| `resistance * current` | 电压 | `U = R · I` |
| `current * resistance` | 电压 | `U = R · I`(可交换) |
| `voltage / current` | 电阻 | `R = U / I` |
| `voltage / resistance` | 电流 | `I = U / R` |
| `current / voltage` | 电导 | `G = I / U` |
| `1 / resistance` | 电导 | `G = 1 / R` |
| `1 / conductance` | 电阻 | `R = 1 / G` |
| `conductance * voltage` | 电流 | `I = G · U` |
| `current / conductance` | 电压 | `U = I / G` |

## 其他类型化运算符

| 表达式 | 结果 | 公式 |
|---|---|---|
| `current * time` | 电荷 | `Q = I · t` |
| `current / frequency` | 电荷 | `Q = I / f` |
| `charge / time` | 电流 | `I = Q / t` |
| `charge / current` | 时间 | `t = Q / I` |
| `current / length` | 磁场强度 | `H = I / l` |
| `field strength * length` | 电流 | `I = H · l` |
| `charge / voltage` | 电容 | `C = Q / U` |
| `capacitance * voltage` | 电荷 | `Q = C · U` |
| `voltage * time` | 磁通量 | `Φ = U · t` |
| `flux / time` | 电压 | `U = Φ / t` |
| `flux / current` | 电感 | `L = Φ / I` |
| `inductance * current` | 磁通量 | `Φ = L · I` |
| `resistance / frequency` | 电感 | `L = X / ω` |
| `flux / area` | 磁通密度 | `B = Φ / A` |
| `flux density * area` | 磁通量 | `Φ = B · A` |
| `current / area` | 电流密度 | `J = I / A` |
| `current density * area` | 电流 | `I = J · A` |
| `charge / volume` | 电荷密度 | `ρ = Q / V` |
| `charge density * volume` | 电荷 | `Q = ρ · V` |
| `resistance * length` | 电阻率 | `ρ = R · A / l` |
| `1 / resistivity` | 电导率 | `σ = 1 / ρ` |
| `1 / conductivity` | 电阻率 | `ρ = 1 / σ` |
| `conductance / length` | 电导率 | `σ = G · l / A` |
| `conductivity * length` | 电导 | `G = σ · A / l` |
| `voltage * current` | 功率 | `P = U · I` |
| `power / voltage` | 电流 | `I = P / U` |
| `power / current` | 电压 | `U = P / I` |
| `power * time` | 能量 | `W = P · t` |
| `energy / time` | 功率 | `P = W / t` |
| `charge * voltage` | 能量 | `W = Q · U` |
| `energy / charge` | 电压 | `U = W / Q` |

每个结果都是正确的类型化量 —— 无需手工组装原始混合单位。此外,电压、电阻、电荷、电导与磁场强度通过
`toVoltage()` / `toResistance()` / `toCharge()` / `toConductance()` / `toMagneticFieldStrength()`
识别其完全**原生**的分解式(`kg·m²·s⁻³·A⁻¹`、`kg·m²·s⁻³·A⁻²`、`A·s`、`kg⁻¹·m⁻²·s³·A²`、`A·m⁻¹`)。
较新的组同样适用:`toCapacitance()`(`kg⁻¹·m⁻²·s⁴·A²`)、`toInductance()`(`kg·m²·s⁻²·A⁻²`)、
`toMagneticFlux()`(`kg·m²·s⁻²·A⁻¹`)、`toMagneticFluxDensity()`(`kg·s⁻²·A⁻¹`)、
`toCurrentDensity()`(`A·m⁻²`)、`toChargeDensity()`(`A·s·m⁻³`)、`toResistivity()`
(`kg·m³·s⁻³·A⁻²`)、`toConductivity()`(`kg⁻¹·m⁻³·s³·A²`)、`toPower()`(`kg·m²·s⁻³`)以及
`toEnergy()`(`kg·m²·s⁻²`)。

## 实例 —— 单个回路中的欧姆定律

负载在吸取 **2 A** 电流时产生 **230 V** 压降。电阻为 `R = U / I`,再将该电阻与电流相乘即可重现电压
`U = R · I`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance,115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## 实例 —— 从市电功率到消耗的能量

一个 **230 V** 插座为 **10 A** 的负载供电,产生 `P = U · I`;运行三小时消耗 `W = P · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(值 + 符号)输出值;对于其他单位,在字符串模板中用 `into` 读取并自行
附加符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V"(基准单位)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## 记法

下表以数学表记与 KUnit 的 Kotlin 表记对照欧姆定律。指数使用 Unicode 上标(`²`、`⁻¹`),`·` 表示乘法,
`/` 表示分数。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | 电压÷电流得电阻 |
| `U = R · I` | `r * (2 of amperes)` | 电阻×电流得电压 |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | 电压÷电阻得电流 |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | 作为原生正规形的电阻 |

## 后续阅读

* [电流](ec.md) —— 原生安培组(以及 CGS 的毕奥与静电安培)。
* [电压](voltage.md) —— 伏特及其分解式 `R · I` 与原生形式。
* [电阻](resistance.md) —— 欧姆、`U / I`,以及逆欧姆定律运算符。
* [电荷](charge.md) —— 库仑、`I · t`,以及电池容量的安时。
* [电导](conductance.md) —— 西门子、`1 / R`,以及 `I / U`。
* [磁场强度](magneticfieldstrength.md) —— 安培每米、`I / l`,以及奥斯特。
* [电容](capacitance.md) —— 法拉、`Q / U`,以及 CGS 的绝对法拉/静电法拉。
* [电感](inductance.md) —— 亨利、`Φ / I`,以及电抗形式 `X / ω`。
* [磁通量](magneticflux.md) —— 韦伯、`U · t`,以及麦克斯韦。
* [磁通密度](magneticfluxdensity.md) —— 特斯拉、`Φ / A`,以及高斯。
* [电流密度](currentdensity.md) —— 安培每平方米、`I / A`,用于导线选型。
* [电荷密度](chargedensity.md) —— 库仑每立方米、`Q / V`。
* [电阻率](resistivity.md) —— 欧姆米、`R · A / l`,即电阻背后的材料属性。
* [电导率](conductivity.md) —— 西门子每米、`1 / ρ`,以及 `G · l / A`。
* [功率(电气)](power.md) —— 瓦特、`U · I`,以及马力单位。
* [能量(电气)](energy.md) —— 焦耳、`Q · U`、`P · t`,以及作为 `kilo.watts * hours` 的千瓦时。
