# 热扩散率

包：`org.pcsoft.framework.kunit.thermo.diffusivity`
基本单位：**平方米每秒**（`KThermalDiffusivityUnit.BASE == KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND`）

类型：**构造单位**

热扩散率 `α` 描述温度变化在材料中传播的*速度* —— 与
[热导率](thermal-conductivity.md)不同，后者描述的是稳态下*流过多少*热量。
单位：`m²/s`。其定义为

```
α = λ / (ρ · c_p)
```

`KThermalDiffusivityUnitInstance` 包装了一个恰好由两项组成的
`KMixedUnitInstance`，处于规范正规形式 `distance² · time⁻¹`（`m²·s⁻¹`），
始终以 m²/s 归一化。

!!! note "包名与类名的区别"
    包名是 `thermo.diffusivity`，而非 `thermo.thermaldiffusivity` ——
    单位包不得重复其所属领域包的名称。类型保留完整的技术术语
    （`KThermalDiffusivityUnitInstance`）。`m²/s` 这一维度与运动粘度和
    质量扩散率共享；本组建模的是热学量。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 m²/s |
|---|---|---:|---:|
| 平方米每秒 | `m²/s` | `squareMetersPerSecond` | 1.0 |
| 平方毫米每秒 | `mm²/s` | `squareMillimetersPerSecond` | 1e-6 |
| 平方英尺每小时 | `ft²/h` | `squareFeetPerHour` | ≈ 2.58064e-5 |

材料手册中通常以 mm²/s 列出 `α`，这恰好等于 `micro.squareMetersPerSecond`。
所有单位均支持完整的 SI 前缀范围。

## 典型数值

| 材料 | α |
|---|---:|
| 铜 | ≈ 116 mm²/s |
| 钢 | ≈ 14 mm²/s |
| 玻璃 | ≈ 0.34 mm²/s |
| 水 | ≈ 0.14 mm²/s |
| 矿棉 | ≈ 1.2 mm²/s |

## 现实示例：铜的热平衡速度

铜的 λ = 401 W/(m·K)，ρ = 8960 kg/m³，c_p = 385 J/(kg·K)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// 反过来：由扩散率还原导热率
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## 用相邻单位计算

其定义关系是**三元**的（`α = λ / (ρ · c_p)`），因此与本页其他每个组不同，
它无法用单一的二元操作符表示，除非为体积热容 `ρ · c_p`（J/(m³·K)）
发明一个中间类型，而本库并未建模该类型。因此该关系以命名的、强类型的
函数形式暴露：

| 函数 | 结果类型 | 含义 |
|---|---|---|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KThermalDiffusivityUnitInstance` | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance` | `λ = α · ρ · c_p` |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance` | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

这四个函数都汇入与本库其他分解方式相同的归一化工厂。

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `λ / (ρ · c_p)` | 类型化函数 `diffusivityWith` | `KThermalDiffusivityUnitInstance` |
| `distance² · time⁻¹` | 原生表达式 + `toThermalDiffusivity()` | `KThermalDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K)，ρ = 1 kg/m³，c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toThermalDiffusivity()

typed == native // true —— 两者都是 1.0 m²/s
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `m²/s` | `squareMetersPerSecond` | 热扩散率，基本单位 |
| `m²·s⁻¹` | `(meters pow 2) / seconds` | 相同的量以基础维度表示 |
| `mm²/s` | `squareMillimetersPerSecond` | 平方毫米每秒（材料手册） |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)` | 定义关系 |
| `λ = α · ρ · c_p` | `alpha.conductivityWith(density, heat)` | 由扩散率得到导热率 |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)` | 由扩散率得到密度 |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | 由扩散率得到比热容 |
