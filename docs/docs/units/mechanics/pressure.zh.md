# 压力

包: `org.pcsoft.framework.kunit.pressure`
基本单位: **帕斯卡**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

类型：**构造单位**

压力是一个**构造**单位,即组合 `mass · length⁻¹ · time⁻²`(`kg/(m·s²)` = `N/m²`)。`KPressureUnitInstance`
包装三项——指数为 `+1` 的 `KMassUnit.BASE`(克)、指数为 `-1` 的 `KDistanceUnit.BASE`(米)和指数为 `-2` 的
`KTimeUnit.BASE`(秒)。与力一样,存储值为原始的以克为基准的分量值,以帕斯卡读取时除以固定因子。

## 构建压力

由 `force / area` 构建压力,或使用命名令牌。命名单位作为值为 1 的令牌保留(与 `of`/`into` 一起使用):

| 压力 | 符号 | 令牌 | 1 单位换算为 Pa |
|---|---|---:|---:|
| 帕斯卡 | `Pa` | `pascals` | 1.0 |
| 巴 | `bar` | `bars` | 100000.0 |
| 标准大气压 | `atm` | `atmospheres` | 101325.0 |
| 磅力每平方英寸 | `psi` | `psis` | 6894.757 |
| 托(mmHg) | `Torr` | `torrs` | 133.322 |

可由前缀导出的写法不是专用令牌: **hPa** = `hecto.pascals`,**kPa** = `kilo.pascals`,结构力学单位
**N/mm² = MPa** = `mega.pascals`(或表达式 `newtons / (milli.meters pow 2)`)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0(= 1 N/mm²)
```

## 使用核心单位(力与面积)进行计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | 压力 = F / A |
| `pressure * area` | `KForceUnitInstance` | 力 = p · A |
| `area * pressure` | `KForceUnitInstance` | 力(可交换) |
| `force / pressure` | `KAreaUnitInstance` | 面积 = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance, 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance, 50 Pa
val f = p * area                           // KForceUnitInstance, 100 N
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance(逃逸出组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa"(基本单位)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `Pa` | `pascals` | 压强，基本单位（命名标记，帕斯卡） |
| `N/m²` | `newtons / (meters pow 2)` | 作为 力 / 面积 的压强（分数形式） |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | 同一压强写成纯乘积 |
| `kPa` | `kilo.pascals` | 带前缀的压强（千帕） |
