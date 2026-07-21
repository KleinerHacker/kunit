# 密度

包: `org.pcsoft.framework.kunit.density`
基本单位: **千克每立方米**(`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

密度(质量密度)是一个**构造**单位,即组合 `mass · length⁻³`(`kg/m³`)。`KDensityUnitInstance` 包装两项——
指数为 `+1` 的 `KMassUnit.BASE`(克)和指数为 `-3` 的 `KDistanceUnit.BASE`(米)。存储值为原始的以克为基准的
分量值,以 kg/m³ 读取时除以固定因子。

## 构建密度

密度**没有裸令牌**——每种写法(kg/m³、g/cm³ 等)都是比值。将其构建为表达式,或通过类型化的 `mass / volume`
运算符,并用 `into` 针对这样的表达式读回:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785(= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## 使用核心单位(质量与体积)进行计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | 密度 = m / V |
| `density * volume` | `KMassUnitInstance` | 质量 = ρ · V |
| `volume * density` | `KMassUnitInstance` | 质量(可交换) |
| `mass / density` | `KVolumeUnitInstance` | 体积 = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | 面密度(见面密度) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³"(基本单位)
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `kg/m³` | `kilo.grams / (meters pow 3)` | 密度，基本单位（千克每立方米）— 分数形式 |
| `kg·m⁻³` | `kilo.grams * (meters pow -3)` | 同一密度写成带负指数的乘积 |
| `g/cm³` | `grams / (centi.meters pow 3)` | 克每立方厘米 |
| `6 kg / 2 L` | `(6 of kilo.grams) / (2 of liters)` | 由 质量 ÷ 体积 构造 |
