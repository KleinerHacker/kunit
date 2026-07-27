# 电气工程 — 概述

包：`org.pcsoft.framework.kunit.ec`、`…voltage`、`…resistance`、`…charge`、`…conductance`、
`…magneticfieldstrength`

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

每个结果都是正确的类型化量 —— 无需手工组装原始混合单位。此外,电压、电阻、电荷、电导与磁场强度通过
`toVoltage()` / `toResistance()` / `toCharge()` / `toConductance()` / `toMagneticFieldStrength()`
识别其完全**原生**的分解式(`kg·m²·s⁻³·A⁻¹`、`kg·m²·s⁻³·A⁻²`、`A·s`、`kg⁻¹·m⁻²·s³·A²`、`A·m⁻¹`)。

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
