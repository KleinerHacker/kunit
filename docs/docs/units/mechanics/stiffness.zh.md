# 刚度 (弹簧刚度系数)

包: `org.pcsoft.framework.kunit.mechanic.lineforce`
基本单位: **牛顿每米**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

类型： **构造单位**

刚度 (弹簧刚度系数)`k = F / s` 是每单位挠度所需的力。其维度是 `mass · time⁻²`(`N/m`)——恰好是
[表面张力](surface-tension.md)的维度。KUnit 为这两种读法建模了一个中性组 `lineforce`；刚度是其中一种读法。 本页描述该读法。

!!! note "一个组，两种读法"
`KLineForceUnitInstance` 是共享类型，因此就 KUnit 而言，刚度和表面张力是同一个单位。该组使用中性 名称 `lineforce`
，以免任何一种读法占用另一种的名字。请通过为你的值命名来区分它们。

## 命名单位

| 单位       | 符号     |                   令牌 | 1 单位等于多少 N/m |
|------------|----------|-----------------------:|-------------------:|
| 牛顿每米   | `N/m`    |      `newtonsPerMeter` |                1.0 |
| 牛顿每毫米 | `N/mm`   | `newtonsPerMillimeter` |             1000.0 |
| 千克力每米 | `kp/m`   |    `kilopondsPerMeter` |            9.80665 |
| 磅力每英寸 | `lbf/in` |   `poundsForcePerInch` |          ≈ 175.127 |
| 达因每厘米 | `dyn/cm` |   `dynesPerCentimeter` |               1e-3 |

弹簧数据表通常以 N/mm 给出；千牛顿每米是带前缀的写法 `kilo.newtonsPerMeter`，数值上与 N/mm 相同。

## 使用核心单位计算

| 表达式                                     | 结果类型                 | 含义                               |
|--------------------------------------------|--------------------------|------------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                        |
| `lineforce * length`、`length * lineforce` | `KForceUnitInstance`     | 弹簧力 `F = k · s`                 |
| `force / lineforce`                        | `KLengthUnitInstance`    | 挠度 `s = F / k`                   |
| `energy / area`                            | `KLineForceUnitInstance` | [表面张力](surface-tension.md)读法 |

原生形式通过 `toLineForce()` 转换：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - 两者都是 1 N/m
typed into newtonsPerMeter // 1.0
```

## 现实示例：悬架中的螺旋弹簧

一根螺旋弹簧的刚度为 40 N/mm。在 2000 N 的车轮载荷下它会压缩多少？15 mm 的挠度又会产生多大的力？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// 并联弹簧的刚度直接相加
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m"(基本单位)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                          | 含义                 |
|-------------|---------------------------------|----------------------|
| `N/m`       | `newtonsPerMeter`               | 刚度，基本单位       |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 同一量以基础维度表示 |
| `N/mm`      | `newtonsPerMillimeter`          | 弹簧数据表读法       |
| `k = F / s` | `force / length`                | 类型化分解           |
| `F = k · s` | `lineforce * length`            | 弹簧力               |
| `s = F / k` | `force / lineforce`             | 挠度                 |
