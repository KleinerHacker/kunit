# 面密度

包: `org.pcsoft.framework.kunit.areadensity`
基本单位: **千克每平方米**(`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

面密度(面质量 / 面荷载,常见于结构力学)是一个**构造**单位,即组合 `mass · length⁻²`(`kg/m²`)。
`KAreaDensityUnitInstance` 包装两项——指数为 `+1` 的 `KMassUnit.BASE`(克)和指数为 `-2` 的
`KDistanceUnit.BASE`(米)。存储值为原始的以克为基准的分量值,以 kg/m² 读取时除以固定因子。

## 构建面密度

与密度一样,面密度**没有裸令牌**——每种写法(kg/m²、g/mm² 等)都是比值。将其构建为表达式,或通过类型化的
`mass / area` 运算符,并用 `into` 针对这样的表达式读回:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005(每 mm²)
```

## 使用核心单位(质量、面积与密度)进行计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | 面密度 = m / A |
| `area density * area` | `KMassUnitInstance` | 质量 = q · A |
| `area * area density` | `KMassUnitInstance` | 质量(可交换) |
| `mass / area density` | `KAreaUnitInstance` | 面积 = m / q |
| `density * length` | `KAreaDensityUnitInstance` | 给定材料与厚度的板 |
| `area density / length` | `KDensityUnitInstance` | 返回体积密度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// 厚度 3 m 的板: 密度 × 厚度 = 面质量
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance, 2000 kg/m³
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²"(基本单位)
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `kg/m²` | `kilo.grams / (meters pow 2)` | 面密度，基本单位（千克每平方米）— 分数形式 |
| `kg·m⁻²` | `kilo.grams * (meters pow -2)` | 同一面密度写成带负指数的乘积 |
| `g/mm²` | `grams / (milli.meters pow 2)` | 克每平方毫米 |
| `25 kg / (5 m · 1 m)` | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))` | 由 质量 ÷ 面积 构造 |
