# 电荷

包: `org.pcsoft.framework.kunit.electric.charge`
基本单位: **库仑**(`KChargeUnit.BASE == KChargeUnit.COULOMB`)

类型: **构造单位**

电荷是**构造**单位,由 `current · time`(`A·s`)组合而成。`KChargeUnitInstance` 包装一个含两个项的
`KMixedUnitInstance` —— 指数为 `+1` 的 `KElectricCurrentUnit.BASE`(安培)和指数为 `+1` 的
`KTimeUnit.BASE`(秒)。无论由哪个命名单位、SI 前缀或电流/时间组合构建,存储的值始终归一化为库仑。

## 创建电荷

可以用命名令牌创建电荷,或者通过分解(见下文)。命名单位以值为 1 的令牌形式保留(配合 `of`/`into` 使用):

| 电荷 | 符号 | 令牌 | 1 单位 = ? C |
|---|---|---:|---:|
| 库仑 | `C` | `coulombs` | 1.0 |
| 安培秒 | `As` | `ampereSeconds` | 1.0 |
| 安培小时 | `Ah` | `ampereHours` | 3600.0 |
| 电磁库仑(CGS-EMU) | `abC` | `abcoulombs` | 10.0 |
| 静电库仑(CGS-ESU) | `statC` | `statcoulombs` | 3.335641e-10 |
| 法拉第 | `F_c` | `faradays` | 96485.332 |
| 元电荷 | `e` | `elementaryCharges` | 1.602176634e-19 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀(`kilo.coulombs`、`milli.coulombs`、`milli.ampereHours` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## 多种分解

电荷可以通过多种**等价分解**得到,它们都产生数值相等的电荷:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `current * time` | `KChargeUnitInstance` | 定义 `Q = I · t` |
| `time * current` | `KChargeUnitInstance` | `Q = I · t` 的交换形式 |
| `current / frequency` | `KChargeUnitInstance` | 逆时间形式 `Q = I / f`(`1/Hz = s`) |
| `current·time` | 通过 `.toCharge()` | 原生规范表达式 `A·s` |

带类型的运算符形式直接返回电荷。完全原生的表达式仍然是通用的 `KMixedUnitInstance`,需要用 `toCharge()`
收窄(它只识别规范形式 —— 一个指数为 `+1` 的 `KElectricCurrentUnit` 项和一个指数为 `+1` 的 `KTimeUnit`
项,否则抛出 `IllegalStateException`)。所有路径的数值都相等。

逆运算符把电荷、电流和时间联系起来:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `charge / time` | `KElectricCurrentUnitInstance` | `I = Q / t` |
| `charge / current` | `KTimeUnitInstance` | `t = Q / I` |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f`(逆时间形式) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.*

// 实际例子 —— 电池容量: 一节 2000 mAh 的电池存储 7200 C。
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance,7200 C

// 以 250 mA 恒定放电能持续多久?
battery / (0.25 of amperes)               // KTimeUnitInstance,28800 s(8 小时)

// 同一电荷的带类型分解形式与原生 A·s 表达式:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance,7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## 电通量

穿过一个闭合曲面的**电通量** `Ψ` 等于该曲面所包围的电荷（高斯定律,`Ψ = Q`）。因此它与电荷在
**量纲上相同**,同样以库仑为单位。KUnit 用该组和符号 `C` 来表示它;没有单独的令牌,也没有单独的类型:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.*

// 一个包围 2 µC 电荷的球面,其电通量为 2 µC。
val psi = 2 of micro.coulombs
psi into micro.coulombs        // 2.0
```

用面积除以通量得到[电通量密度](electricfluxdensity.md) `D = Ψ / A`。

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance(脱离该组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

(470 of coulombs).toString()   // "470.0 C"(基本单位)
(1 of ampereHours).toString()  // "3600.0 C"(基本单位)
```

## 记法

下表展示该单位及其组成部分在数学上的写法与在 KUnit 中的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）,`·` 表示乘法,`/` 表示分数。若某个量既可写成分数也可写成带负指数的乘积,则同时列出两种等价的 Kotlin 形式。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `C` | `coulombs` | 电荷,基本单位(命名令牌,库仑) |
| `A·s` | `amperes * seconds` | 电荷 = 电流·时间(乘积形式) |
| `A/Hz` | `amperes / hertz` | 同一电荷写成电流除以频率(`1/Hz = s`) |
| `mAh` | `milli.ampereHours` | 带前缀的电荷(毫安时,电池容量) |

## 参见

- [电流](ec.md) —— 电荷组合中的电流因子
- [电压](voltage.md) —— 电位差
- [电阻](resistance.md) —— 欧姆定律使电学单位组完整
