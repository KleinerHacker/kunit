# 热导（Thermal Conductance）

包：`org.pcsoft.framework.kunit.thermo.conductance`
基本单位： **瓦特每开尔文**（`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`）

类型： **构造单位**

某个部件的热导 `G` 表示每单位温差下有多少热量流过它：`G = P / ΔT`，单位为 `W/K`。它正好是
[绝对热阻](thermal-resistance.md)的倒数，当多条热传导路径处于**并联**时，这是更方便的表达形式——
并联的热导可以直接相加。

其规范的基础维度正规形式为 `mass · length² · time⁻³ · temperature⁻¹`。

## 命名单位

| 单位                | 符号         |                   令牌 | 1 单位相当于多少 W/K |
|---------------------|--------------|------------------------:|----------------------:|
| 瓦特每开尔文        | `W/K`        |         `wattsPerKelvin` |                    1.0 |
| 英热单位每小时-华氏度 | `Btu/(h*°F)` | `btusPerHourFahrenheit` |             ≈ 0.52753 |

所有单位均支持全部 SI 前缀（`milli.wattsPerKelvin`，……）。

## 分解方式

该组只有一种分解方式，其两种形式都会生成相同的类型化、值相等的实例。由于该组包含质量项，原生形式由
**单位模板**组装而成。

| 形式                | 表达式                                                          |
|---------------------|-------------------------------------------------------------------|
| 类型化操作符        | `power / temperatureDifference`                                    |
| 原生（`toX()`）     | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`         |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## 用该组进行计算

| 表达式                                      | 结果类型                              | 含义                     |
|----------------------------------------------|----------------------------------------|--------------------------|
| `power / temperatureDifference`               | `KThermalConductanceUnitInstance`     | `G = P / ΔT`             |
| `thermalConductance * temperatureDifference`  | `KPowerUnitInstance`                  | `P = G · ΔT`             |
| `power / thermalConductance`                  | `KTemperatureDifferenceUnitInstance`  | 所需的温差               |
| `thermalConductance + …`                      | `KThermalConductanceUnitInstance`     | 并联的热传导路径         |
| `1 / thermalConductance`                      | `KThermalResistanceUnitInstance`      | `R = 1 / G`              |
| `1 / thermalResistance`                       | `KThermalConductanceUnitInstance`     | `G = 1 / R`              |

## 现实示例：两条并联的散热路径

某模块通过底板（0.4 W/K）和外壳（0.1 W/K）散热。并联时热导相加，倒数即为总热阻：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // 在 ΔT = 30 K 时带走 15.0 W
```

## 值语义

`equals`/`hashCode` 比较**归一化的 W/K 值**，因此
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`。`toString()` 以基本单位表示该值：
`"0.4 W/K"`。

## 另请参阅

* [绝对热阻](thermal-resistance.zh.md) —— 其倒数量。
* [热绝缘性](thermal-insulance.zh.md) —— 热阻的单位面积形式。
* [传热系数](heat-transfer-coefficient.zh.md) —— 该量的单位面积形式。
* [热力学概述](overview.zh.md)
