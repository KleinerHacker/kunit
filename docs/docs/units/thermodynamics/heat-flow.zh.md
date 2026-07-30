# 热流

包：`org.pcsoft.framework.kunit.common.power`
基本单位：**瓦特**（`KPowerUnit.BASE == KPowerUnit.WATT`）

类型：**构造单位**

热流 `Q̇`（也称为热功率，或热流率）是单位时间内传递的热量：`W`。它与
[功率](power.md)在**维度和物理上完全相同** —— 都是能量除以时间 —— 因此
KUnit 用 `KPowerUnitInstance` 来建模它。

## 为什么热流没有自己的类型

热流不是一个独立的量，只是恰好是热学性质的功率。它只有一个规范正规形式
`mass¹ · distance² · time⁻³`，若在其之上再有第二个类型，会使 `toPower()` 产生歧义，
却没有增加任何物理意义。一瓦特描述的是电动机、激光器还是散热器，取决于语境，而非维度。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // 机械功率
val radiator = 1500 of watts    // 热流
// 两者都是 KPowerUnitInstance
```

## 现实示例：一台散热器

一台额定功率 1500 W 的散热器运行 4 小时。它输出多少能量，在其 0.6 m² 表面上
产生的热流密度是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ（= 6 kWh）

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## 热流在本领域中的位置

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | 由热量 ÷ 时长得到热流 |
| `power * time` | `KEnergyUnitInstance` | 一段时长内输出的热量 |
| `power / area` | `KHeatFluxDensityUnitInstance` | [热流密度](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance` | 通过表面的总热流 |

一堵墙的热损失是经典的链式关系：[传热系数](heat-transfer-coefficient.md)乘以温度差
得到[热流密度](heat-flux-density.md)，再乘以面积就得到以瓦特计的热流。

## 另请参阅

* [功率](power.md) —— 热流与之共享的类型，包含完整的单位表、所有分解方式以及
  完整的操作符集合
* [热流密度](heat-flux-density.md) —— 每单位面积的热流
* [传热系数](heat-transfer-coefficient.md) —— 每开尔文的热流密度
* [能量](energy.md) —— 热流对时间的积分

## 记法

下表展示了该量在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `W` | `watts` | 热流，基本单位（与功率共享） |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | 相同的量以基础维度表示 |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime` | 由热量 ÷ 时长得到热流 |
| `Q = Q̇ · t` | `radiator * runtime` | 由热流 × 时长得到热量 |
| `q̇ = Q̇ / A` | `radiator / surface` | 由热流 ÷ 面积得到热流密度 |
