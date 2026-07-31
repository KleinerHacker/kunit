# 转动惯量

包: `org.pcsoft.framework.kunit.mechanic.inertia`
基本单位: **千克平方米**(`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

类型： **构造单位**

转动惯量 `J` 是[质量](mass.md)在旋转运动中的对应量：它描述物体抵抗其转动状态变化的程度。它是一个 **构造**单位——组合
`mass · length²`(`kg·m²`)。

`KInertiaUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的 `KMassUnit.BASE`
(克)和指数为 `+2` 的 `KDistanceUnit.BASE`(米)。由于本库的质量分量 归一化为克，存储值为原始的以克为基准的分量值，以 kg·m²
读取时除以固定因子。

## 命名单位

| 单位       | 符号      |                     令牌 | 1 单位换算为 kg·m² |
|------------|-----------|-------------------------:|-------------------:|
| 千克平方米 | `kg*m^2`  |  `kilogramMetersSquared` |                1.0 |
| 克平方厘米 | `g*cm^2`  | `gramCentimetersSquared` |               1e-7 |
| 磅平方英尺 | `lb*ft^2` |       `poundFeetSquared` |        ≈ 0.0421401 |

所有单位均支持完整的 SI 前缀范围 (小型伺服转子用的 `milli.kilogramMetersSquared`)。

## 分解方式

| 形式        | Kotlin                                                  | 结果类型               |
|-------------|---------------------------------------------------------|------------------------|
| 质量 × 面积 | `mass * area`                                           | `KInertiaUnitInstance` |
| 原生表达式  | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## 使用核心单位进行计算

| 表达式                          | 结果类型                       | 含义                                      |
|---------------------------------|--------------------------------|-------------------------------------------|
| `mass * area`、`area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                              |
| `inertia / mass`                | `KAreaUnitInstance`            | 回转半径的平方 `r² = J / m`               |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                              |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [角动量](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [转矩](torque.md) `M = J · α`             |

## 现实示例:压力机的飞轮

一个实心飞轮盘 (`J = ½ · m · r²`)质量为 40 kg，半径为 0.3 m。它的转动惯量是多少？ 在 1500 rpm 下它携带的角动量是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2"(基本单位)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学         | Kotlin                          | 含义                         |
|--------------|---------------------------------|------------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | 转动惯量，基本单位(命名令牌) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | 同一量写成纯乘积             |
| `J = m · r²` | `mass * area`                   | 类型化分解方式               |
| `r² = J / m` | `inertia / mass`                | 回转半径的平方               |
| `L = J · ω`  | `inertia * angularvelocity`     | 角动量                       |
| `M = J · α`  | `inertia * angularacceleration` | 转矩                         |
