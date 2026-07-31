# 线密度

包: `org.pcsoft.framework.kunit.mechanic.lineardensity`
基本单位: **千克每米**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

类型： **构造单位**

线密度是单位长度的质量——[面密度](areadensity.md)(`kg/m²`)与[密度](density.md)(`kg/m³`)在 一维上的对应量。它是一个
**构造**单位——组合 `mass · length⁻¹`(`kg/m`)。

`KLinearDensityUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KMassUnit.BASE`(克)和指数为 `-1` 的 `KDistanceUnit.BASE`(米)。由于本库的质量分量 归一化为克，存储值为原始的以克为基准的分量值，以
kg/m 读取时除以固定因子。

## 命名单位

| 单位         | 符号    |                 令牌 | 1 单位换算为 kg/m |
|--------------|---------|---------------------:|------------------:|
| 千克每米     | `kg/m`  |  `kilogramsPerMeter` |               1.0 |
| 克每米       | `g/m`   |      `gramsPerMeter` |              1e-3 |
| 克每厘米     | `g/cm`  | `gramsPerCentimeter` |               0.1 |
| 特克斯(纺织) | `tex`   |                `tex` |              1e-6 |
| 旦尼尔(纺织) | `den`   |             `denier` |       ≈ 1.1111e-7 |
| 磅每英尺     | `lb/ft` |      `poundsPerFoot` |         ≈ 1.48816 |

所有单位均支持完整的 SI 前缀范围；纺织业的分特克斯是 `deci.tex`。

## 使用核心单位进行计算

| 表达式                                             | 结果类型                     | 含义          |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`、`length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

原生形式同样可用：任何通过通用引擎构建的克每米表达式都可以通过 `toLinearDensity()` 进行转换。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true —— 两者都是 0.5 kg/m
typed into gramsPerMeter        // 500.0
```

## 现实示例:卷筒上的钢缆

一根钢缆重 2.6 kg/m。45 m 长的钢缆质量是多少？在 500 kg 载荷限制下允许多长的钢缆？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true(纺织关系)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m"(基本单位)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学          | Kotlin                         | 含义                       |
|---------------|--------------------------------|----------------------------|
| `kg/m`        | `kilogramsPerMeter`            | 线密度，基本单位(命名令牌) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | 同一量写成纯乘积           |
| `tex`         | `tex`                          | 纺织线密度(1 g/km)         |
| `ρ_l = m / l` | `mass / length`                | 类型化分解方式             |
| `m = ρ_l · l` | `lineardensity * length`       | 求解质量                   |
| `dtex`        | `deci.tex`                     | 带前缀的纺织读法           |
