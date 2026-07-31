# 热膨胀系数

包：`org.pcsoft.framework.kunit.thermo.expansion`
基本单位： **每开尔文**（`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`）

类型： **构造单位**

热膨胀系数 `α` 是长度（或面积、体积）每开尔文的 *相对*变化：`1/K`。它是 温度差的倒数。

`KThermalExpansionUnitInstance` 包装了一个恰好由一项组成的
`KMixedUnitInstance`，处于规范正规形式 `temperature⁻¹`（`K⁻¹`），始终以 1/K 归一化。温度维度是 **差**组 —— 该系数描述的是每温度
*区间*的变化。

!!! note "包名与类名的区别"
包名是 `thermo.expansion`，而非 `thermo.thermalexpansion` —— 单位包 不得重复其所属领域包的名称。类型保留完整的技术术语 （
`KThermalExpansionUnitInstance`）。

## 命名单位

| 单位               | 符号    |            令牌 | 1 单位相当于多少 1/K |
|--------------------|---------|----------------:|---------------------:|
| 每开尔文           | `1/K`   |     `perKelvin` |                  1.0 |
| 每华氏度           | `1/°F`  | `perFahrenheit` |                  1.8 |
| 百万分之一每开尔文 | `ppm/K` |  `ppmPerKelvin` |                 1e-6 |

材料手册中通常以 ppm/K 列出 `α`，这恰好等于 `micro.perKelvin`。所有单位均 支持完整的 SI 前缀范围。

## 典型数值

| 材料     |           α |
|----------|------------:|
| 钢       |  ≈ 12 ppm/K |
| 混凝土   |  ≈ 12 ppm/K |
| 铝       |  ≈ 23 ppm/K |
| 硼硅玻璃 | ≈ 3.3 ppm/K |

## 现实示例：夏日中的钢梁

一根 10 m 长的钢梁（α = 12 ppm/K）从 0 °C 升温到 50 °C。它会伸长多少？ 这正是桥梁需要设置伸缩缝的原因。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// 无量纲的相对变化
val strain = steel * rise                      // 6.0e-4

// 类型化的绝对变化
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// 同样的温差摆幅下，一座 100 m 的桥面
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## 操作符

| 表达式                                                         | 结果类型                             | 含义                   |
|----------------------------------------------------------------|--------------------------------------|------------------------|
| `1 / temperatureDifference`                                    | `KThermalExpansionUnitInstance`      | 由区间得到系数         |
| `1 / thermalExpansion`                                         | `KTemperatureDifferenceUnitInstance` | 由系数得到区间         |
| `thermalExpansion * temperatureDifference`                     | `Double`                             | **相对**变化（无量纲） |
| `temperatureDifference * thermalExpansion`                     | `Double`                             | 相同（可交换）         |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance`                | **绝对**变化           |

这两个倒数操作符被窄化声明，因此 `1 / d` 与 `1 / α` 返回的是 **类型化**值， 而非通用的 `Number.div` 与分组无关时会产生的混合单位。

!!! warning "用 `elongationOf` 而非链式 `*`"
`α · ΔT` 有意设计为一个纯 `Double` —— 相对变化是无量纲的。将该 `Double`
乘到一个长度上需要根包中的通用标量 `times`，而显式导入它会 **遮蔽**本组的
`times` 操作符。`elongationOf` 正是一个不会被遮蔽的普通函数；若想得到 绝对变化，请优先使用它。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                    | 形式                                | 结果                            |
|-----------------------------|-------------------------------------|---------------------------------|
| `1 / temperatureDifference` | 类型化操作符                        | `KThermalExpansionUnitInstance` |
| `temperature⁻¹`             | 原生表达式 + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true —— 两者都是 1.0 1/K
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示          | Kotlin                                   | 含义                           |
|-------------------|------------------------------------------|--------------------------------|
| `1/K`             | `perKelvin`                              | 热膨胀系数，基本单位           |
| `K⁻¹`             | `ΔK pow -1`                              | 相同的量以负指数表示           |
| `ppm/K`           | `ppmPerKelvin`                           | 百万分之一每开尔文（材料手册） |
| `α = 1 / ΔT`      | `1 / KTemperatureDifference.ofKelvin(2)` | 由区间得到系数                 |
| `ε = α · ΔT`      | `steel * rise`                           | 相对变化（无量纲）             |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)`         | 绝对长度变化                   |
