# 热容

包：`org.pcsoft.framework.kunit.thermo.heatcapacity`
基本单位： **焦耳每开尔文**（`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`）

类型： **构造单位**

热容是物体每升高单位温度所吸收的能量：`energy / temperature`（`J/K`）。
`KHeatCapacityUnitInstance` 包装了一个恰好由四项组成的 `KMixedUnitInstance`， 处于规范正规形式
`mass¹ · distance² · time⁻² · temperature⁻¹`（`kg·m²·s⁻²·K⁻¹`）， 始终以 J/K 归一化。

!!! note "温度 *差*，而非绝对温度"
温度维度是 **差**组（`KTemperatureDifferenceUnit`，符号 `ΔK`），永远不是仿射的 绝对量 `KTemperatureUnit`。热容将能量与温度
*区间*相关联；带偏移量的绝对标度 （°C、°F）出现在商运算中在物理上是错误的。

相同的维度 `J/K` 也描述 **熵** —— 参见[熵](entropy.md)，了解为什么该量共享此类型
而非拥有自己的类型。每单位质量得到[比热容](specific-heat-capacity.md)，每摩尔得到
[摩尔热容](molar-heat-capacity.md)。

## 命名单位

| 单位             | 符号     |                令牌 | 1 单位相当于多少 J/K |
|------------------|----------|--------------------:|---------------------:|
| 焦耳每开尔文     | `J/K`    |   `joulesPerKelvin` |                  1.0 |
| 卡路里每开尔文   | `cal/K`  | `caloriesPerKelvin` |                4.184 |
| 英热单位每华氏度 | `Btu/°F` | `btusPerFahrenheit` |          ≈ 1899.1005 |

以上单位均支持完整的 SI 前缀范围（`kilo.joulesPerKelvin`、`kilo.caloriesPerKelvin` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## 现实示例：给水壶加热

将 1 升水（4184 J/K）从 20 °C 加热到 100 °C。这需要多少能量？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 1 升水
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance，80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ……反过来：100 kJ 能达到多高的温升？
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## 用核心单位（能量与温度差）计算

| 表达式                                 | 结果类型                             | 含义           |
|----------------------------------------|--------------------------------------|----------------|
| `energy / temperatureDifference`       | `KHeatCapacityUnitInstance`          | 热容           |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance`                | 所需能量       |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance`                | 能量（可交换） |
| `energy / heatCapacity`                | `KTemperatureDifferenceUnitInstance` | 可达到的温升   |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式                                    | 形式                            | 结果                                 |
|---------------------------------------------|---------------------------------|--------------------------------------|
| `energy / temperatureDifference`            | 类型化操作符                    | 直接得到 `KHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · temperature⁻¹` | 原生表达式 + `toHeatCapacity()` | `KHeatCapacityUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// 类型化操作符形式
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// 原生基础维度形式（kg·m²·s⁻²·K⁻¹），由 toHeatCapacity() 识别
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true —— 两者都是 1.0 J/K
```

`toHeatCapacity()` 只识别规范正规形式；任何等价表达式都会自动归约到该形式上， 错误的形状会抛出 `IllegalStateException`。

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - ：同组，单位与前缀之间自动转换
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// 比较（按归一化的 J/K 值）
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// 两个热容之间的 * / / 会脱离到 KMixedUnitInstance
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示        | Kotlin                                          | 含义                       |
|-----------------|-------------------------------------------------|----------------------------|
| `J/K`           | `joulesPerKelvin`                               | 热容，基本单位 —— 命名令牌 |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 相同的量以基础维度表示     |
| `kJ/K`          | `kilo.joulesPerKelvin`                          | 千焦耳每开尔文             |
| `cal/K`         | `caloriesPerKelvin`                             | 卡路里每开尔文             |
| `C = Q / ΔT`    | `(4184 of joules) / rise`                       | 由能量 ÷ 温升得到热容      |
| `Q = C · ΔT`    | `kettle * rise`                                 | 由热容 × 温升得到能量      |
| `ΔT = Q / C`    | `(100 of kilo.joules) / kettle`                 | 由能量 ÷ 热容得到温升      |
