# 绝对热阻

包：`org.pcsoft.framework.kunit.thermo.resistance`
基本单位： **开尔文每瓦特**（`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`）

类型： **构造单位**

某个部件的绝对热阻 `R` 是每单位通过它的热流所维持的温差：`R = ΔT / P`，以 `K/W` 计量。它描述的是
**整个物体** —— 这块散热片、这个晶体管封装、这面特定尺寸的墙。

其规范正规形式为 `mass⁻¹ · length⁻² · time³ · temperature`。

!!! warning "与热阻（保温性）不同"
    不要将这一分组与 [热阻（R 值）](thermal-insulance.md) `m²·K/W` 混淆，后者是同一概念**按单位
    面积**归一化后的结果。两者相差一个面积因子，具有不同的正规形式，因此也是不同的类型。截至并包
    括 0.8.0 版本，名称 `thermo.resistance` / `KThermalResistanceUnit` 指的是该按面积计的保温性；
    现在它指的是本分组。

## 命名单位

| 单位                       | 符号       |                             令牌 | 1 单位相当于多少 K/W |
|----------------------------|------------|------------------------:|--------------:|
| 开尔文每瓦特            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| 摄氏度每瓦特    | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| 小时华氏度每 Btu            | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

1 °C 的温**差**等于 1 K，因此半导体和散热片数据手册上常用的写法
`degreesCelsiusPerWatt` 在数值上与 `kelvinsPerWatt` 完全相同。所有令牌都支持完整的 SI 前缀范围。

## 分解方式

该分组只有一种分解方式，其两种形式都会生成相同的类型化、值相等的实例。由于该分组含有质量项，原生
形式是由**单位模板**组装而成的：原始的混合值是以克为基础的乘积，而类型化实例则以命名单位存储其值。

| 形式             | 表达式                                                            |
|------------------|------------------------------------------------------------------------|
| 类型化操作符   | `temperatureDifference / power`                                        |
| 原生（`toX()`） | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## 用该分组计算

| 表达式                                | 结果类型                            | 含义              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | 由此驱动的热流 |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | 串联的热阻 |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

热阻在**串联时相加** —— 这正是该分组的同型运算符 `+` 所做的事情。

## 现实示例——散热片预算

某功率晶体管耗散 **12 W**。其热路径为：结到壳 0.5 K/W，壳到散热片 0.2 °C/W，散热片到空气
1.8 K/W。结点温度会比环境温度高出多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // 比环境高出 30.0 K

// 若限值为 25 K，它最多可耗散多少功率？
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## 值语义

`equals`/`hashCode` 比较**归一化后的 K/W 值**，因此
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`。`toString()` 以基本单位渲染该值：
`"2.5 K/W"`。

## 另请参见

* [热阻（R 值）](thermal-insulance.zh.md) —— 按单位面积表达的同一概念（R 值）。
* [热导](thermal-conductance.zh.md) —— 其倒数量。
* [热力学概览](overview.zh.md)
