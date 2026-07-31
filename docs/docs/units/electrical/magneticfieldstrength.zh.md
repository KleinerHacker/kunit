# 磁场强度

包: `org.pcsoft.framework.kunit.electric.magneticfieldstrength`
基本单位: **安培每米**(`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

类型: **构成单位**

磁场强度 (磁化场 `H`)是一个 **构成**单位: 组合 `current · length⁻¹`(`A/m`)。
`KMagneticFieldStrengthUnitInstance` 包装了一个含两项的 `KMixedUnitInstance` —— 指数 `+1` 的
`KElectricCurrentUnit.BASE`(安培)和指数 `-1` 的 `KDistanceUnit.BASE`(米)。存储的值始终归一化为安培每米。

相关页面: [电流](ec.md) 和 [距离](../kinematics/distance.md) 是该单位的两个组成分组。

## 构建磁场强度

可以用命名标记构建磁场强度，或从分解式构建 (见下文)。命名单位保留为值为 1 的标记 (与 `of`/`into` 一起使用):

| 磁场强度        | 符号    |                    标记 | 1 单位等于多少 A/m |
|-----------------|---------|------------------------:|-------------------:|
| 安培每米        | `A/m`   |       `amperesPerMeter` |                1.0 |
| 奥斯特(CGS-EMU) | `Oe`    |              `oersteds` |  79.57747154594767 |
| 吉伯每厘米      | `Gb/cm` | `gilbertsPerCentimeter` |  79.57747154594767 |
| 安匝每英寸      | `At/in` |    `ampereTurnsPerInch` |  39.37007874015748 |

命名单位通过 `KPrefixBuilder` 支持 SI 词头 (`kilo.amperesPerMeter`、`milli.oersteds` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## 多种分解

磁场强度可以通过多种 **等价分解**得到，它们都产生数值相等的磁场强度:

| 表达式             | 结果类型                             | 含义                        |
|--------------------|--------------------------------------|-----------------------------|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | 定义式 `H = I / l`          |
| `current·length⁻¹` | 通过 `.toMagneticFieldStrength()`    | 原生规范形式 `A·m⁻¹` 表达式 |

带类型的运算符形式直接返回磁场强度。完全原生的表达式仍是通用的 `KMixedUnitInstance`，需要用
`toMagneticFieldStrength()` 收窄 (它只识别规范范式，否则抛出 `IllegalStateException`)。两条路径的值相等。

反向运算符把电流、长度和磁场强度联系起来:

| 表达式                   | 结果类型                       | 含义        |
|--------------------------|--------------------------------|-------------|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l` |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | 交换形式    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

// 实际示例 —— 一个 500 匝、通过 2 A 电流、长度 0.25 m 的线圈:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance，4000 A/m

// 同一磁场强度的原生 A·m⁻¹ 表达式:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance(脱离该分组)
```

## toString 格式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m"(基本单位)
```

## 记法

下表展示了该单位及其分量在数学上的写法与在 Kotlin 中使用 KUnit 的写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，
`/` 表示分数。对于既可写成分数又可写成负指数乘积的量，两种等价的 Kotlin 形式都会列出。

| 数学    | Kotlin                              | 含义                                    |
|---------|-------------------------------------|-----------------------------------------|
| `A/m`   | `amperesPerMeter`                   | 磁场强度，基本单位（命名标记）          |
| `A/m`   | `(amperes pow 1) / (meters pow 1)`  | 作为 电流 / 长度 的磁场强度（分数形式） |
| `A·m⁻¹` | `(amperes pow 1) * (meters pow -1)` | 同一磁场强度的纯乘积形式                |
| `kA/m`  | `kilo.amperesPerMeter`              | 带词头的磁场强度（千安每米）            |
