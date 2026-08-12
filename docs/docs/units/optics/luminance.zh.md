# 亮度

包：`org.pcsoft.framework.kunit.optic.luminance`
基本单位：**坎德拉每平方米**（`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`）

类型：**构造单位**

亮度`L`是**每单位发光面积**的发光强度：`L = I / A`，即`1 cd/m² = 1 nit`。这是人眼实际感知为表面"亮度"的量，
也是每一份显示器规格表中给出的数值——一台普通办公显示器约为250–350尼特，一台HDR电视则可达1000尼特或更高。

其规范的基础量纲标准形式是`luminousIntensity¹ · distance⁻²`。

## 单位

| 单位                     | 枚举值                                | 符号   |                    令牌 | 1单位对应cd/m² |
|--------------------------|---------------------------------------------|----------|-------------------------:|----------------:|
| 坎德拉每平方米 | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` | `candelasPerSquareMeter` |             1.0 |
| 尼特                      | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` |                   `nits` |             1.0 |
| 熙提                    | `KLuminanceUnit.STILB`                    | `sb`     |                 `stilbs` |          10 000 |
| 阿熙提                 | `KLuminanceUnit.APOSTILB`                 | `asb`    |              `apostilbs` |           1 / π |
| 朗伯                  | `KLuminanceUnit.LAMBERT`                  | `L`      |               `lamberts` |        10⁴ / π  |
| 英尺朗伯             | `KLuminanceUnit.FOOT_LAMBERT`             | `fL`     |           `footLamberts` |      ≈ 3.426259 |

`nits`是基本单位的另一种写法，不是独立的单位——它是显示行业对坎德拉每平方米的称呼。
阿熙提、朗伯和英尺朗伯属于*朗伯体*家族，携带因子`1/π`，用于将理想漫射光源的照度转换为亮度。
所有令牌都支持所有SI词头。

## 分解

该组有**两种**分解方式。二者都汇入同一个归一化工厂，因此产生相同的、类型化且值相等的实例：

| 形式                   | 表达式                                                     |
|------------------------|------------------------------------------------------------------------|
| 类型化运算符A       | `luminousIntensity / area`                                     |
| 类型化运算符B       | `illuminance / solidAngle`                                     |
| 原生形式（`toX()`）       | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## 使用该组进行计算

| 表达式                     | 结果类型                      | 含义                    |
|--------------------------------|-----------------------------------|-----------------------------|
| `luminousIntensity / area`     | `KLuminanceUnitInstance`         | `L = I / A`                |
| `illuminance / solidAngle`     | `KLuminanceUnitInstance`         | `L = E / Ω`                |
| `luminance * area`             | `KLuminousIntensityUnitInstance` | `I = L · A`                |
| `luminance * solidAngle`       | `KIlluminanceUnitInstance`       | `E = L · Ω`                |
| `luminousIntensity / luminance` | `KAreaUnitInstance`             | 发光面积          |
| `illuminance / luminance`      | `KSolidAngleUnitInstance`        | 光所分布的立体角 |

## 实例——显示器的尼特额定值

一台27英寸显示器，面板面积**0.21 m²**，额定亮度**300尼特**。这对应的轴向总发光强度为：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6（英制读数）
```

## 值语义

`equals`/`hashCode`比较**归一化后的cd/m²值**，因此`(1 of stilbs) == (10000 of candelasPerSquareMeter)`。
`toString()`以基本单位渲染该值：`"250.0 cd/m^2"`。

## 另请参阅

* [发光强度](luminous-intensity.zh.md) —— 亮度的分子。
* [照度](illuminance.zh.md) —— 到达表面而非离开表面的光。
* [辐亮度](radiance.zh.md) —— 对应的辐射度学量。
* [光学概述](overview.zh.md)
</content>
