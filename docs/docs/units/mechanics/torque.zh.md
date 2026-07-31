# 转矩

包: `org.pcsoft.framework.kunit.common.energy`
基本单位: **焦耳**(`KEnergyUnit.BASE == KEnergyUnit.JOULE`)，读作 **牛顿米**(`N·m`)

类型： **构造单位**

转矩 `M = F · r` 是作用在力臂上的力所产生的旋转效应。从维度上看，它 *就是*一个
[能量](energy.md)：`1 N·m = 1 J`。因此 KUnit 并 **不**为其引入第二个单位组——转矩是能量组的一种 **读法**
。本页描述该读法；该组本身在[能量 (力学)](energy.md)页面中描述。

!!! note "相同的维度，不同的物理意义"
转矩与功在物理上是不同的量 (转矩是轴向矢量，功是标量)，但它们恰好共享维度
`kg·m²·s⁻²`。由于 KUnit 建模的是 *单位*而非矢量特性，两者存在于同一个组中。可以通过命名将它们区分开：
`val torque = (100 of newtons) * (2 of meters)` 读作 N·m，`val work = force * distance`
沿路径读作 J。

## 构建转矩

| 表达式                             | 结果类型                           | 含义                        |
|------------------------------------|------------------------------------|-----------------------------|
| `force * length`、`length * force` | `KEnergyUnitInstance`              | `M = F · r`(力臂)           |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α`(旋转的牛顿定律) |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω`(传动系公式)     |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`                 |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`                 |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`                 |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`                 |

所有三种构建形式都汇入能量组的单一工厂，因此它们的值相等：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## 命名单位

转矩使用能量组的令牌；`newtons * meters` 是惯用的 N·m 写法，带前缀的读法来自能量令牌 (`kilo.joules` = kN·m)。

| 读法           | 符号   | Kotlin                           |
|----------------|--------|----------------------------------|
| 牛顿米         | `N*m`  | `(1 of newtons) * (1 of meters)` |
| 焦耳(相同维度) | `J`    | `joules`                         |
| 千牛顿米       | `kN*m` | `kilo.joules`                    |

## 现实示例:发动机转矩与功率

一台发动机在 3000 rpm 下输出 62.83 kW。这相当于多大的转矩？如果保持相同转矩在 6000 rpm 下运转， 输出功率是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0(N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J"(组的基本单位)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                                           | 含义                 |
|-------------|--------------------------------------------------|----------------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | 转矩，力臂形式       |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 同一量以基础维度表示 |
| `M = F · r` | `force * length`                                 | 分解方式 A           |
| `M = J · α` | `inertia * angularacceleration`                  | 分解方式 B           |
| `M = P / ω` | `power / angularvelocity`                        | 分解方式 C(传动系)   |
| `P = M · ω` | `torque * angularvelocity`                       | 旋转功率             |
| `kN·m`      | `kilo.joules`                                    | 带前缀的转矩读法     |
