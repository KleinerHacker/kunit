# 距离

包：`org.pcsoft.framework.kunit.distance`
基础单位：**米**（`KDistanceUnit.BASE == KDistanceUnit.METER`）

距离组在开放基础包装类型 `KDistanceUnitInstance`（`KDistanceUnit.BASE` 的单个项，指数可为**任意**值）之下，
将各个指数建模为各自独立、编译期安全的类型：

* **`KLengthUnitInstance`** —— 指数 1（长度）
* **`KAreaUnitInstance`** —— 指数 2（面积）
* **`KVolumeUnitInstance`** —— 指数 3（体积）

数值始终以米（或平方米/立方米）归一化存储。由于长度、面积和体积是不同的类型，在 `+`/`-`/比较中混用它们会导致
**编译错误**（不存在这样的运算符），而 `*`/`/` 会尽可能保持在同一类型族内（`length * length = area`，
`area / length = length`），对于 `{1,2,3}` 之外的指数（或指数为 0 的无量纲结果）则回退到
`KDistanceUnitInstance`/`KMixedUnitInstance`。

## 指数 1 - 长度

| 单位 | 枚举值 | 符号 | 创建函数 | 1 单位对应的米数 |
|---|---|---|---:|---:|
| 米 | `KDistanceUnit.METER` | `m` | `Number.meters` | 1.0 |
| 英里 | `KDistanceUnit.MILE` | `mi` | `Number.miles` | 1609.344 |
| 海里 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles` | 1852.0 |
| 码 | `KDistanceUnit.YARD` | `yd` | `Number.yards` | 0.9144 |
| 英尺 | `KDistanceUnit.FOOT` | `ft` | `Number.feet` | 0.3048 |
| 英寸 | `KDistanceUnit.INCH` | `in` | `Number.inches` | 0.0254 |
| 英寻 | `KDistanceUnit.FATHOM` | `ftm` | `Number.fathoms` | 1.8288 |
| 链 | `KDistanceUnit.CHAIN` | `ch` | `Number.chains` | 20.1168 |
| 弗隆 | `KDistanceUnit.FURLONG` | `fur` | `Number.furlongs` | 201.168 |
| 天文单位 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits` | 1.495978707e11 |
| 光秒 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds` | 299792458.0 |
| 光分 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes` | 1.798754748e10 |
| 光时 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `Number.lightHours` | 1.0792528488e12 |
| 光日 | `KDistanceUnit.LIGHT_DAY` | `ld` | `Number.lightDays` | 2.59020683712e13 |
| 光周 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks` | 1.813144785984e14 |
| 光年 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `Number.lightYears` | 9.4607304725808e15 |
| 秒差距 | `KDistanceUnit.PARSEC` | `pc` | `Number.parsecs` | 3.0856775814913673e16 |

以上每个单位都有对应的裸 `val` 别名，可用作 `valueAs`/`toString` 的目标，或作为前缀 infix 函数的 `unit`
参数：`meters`、`miles`、`nauticalMiles`、`yards`、`feet`、`inches`、`fathoms`、`chains`、
`furlongs`、`astronomicalUnits`、`lightSeconds`、`lightMinutes`、`lightHours`、`lightDays`、
`lightWeeks`、`lightYears`、`parsecs`。

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5.miles
d.value                        // 8046.72（归一化为米）
d.valueAs(KDistanceUnit.MILE)    // 5.0（换算回英里读取）
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452（换算为海里读取）
```

### 运算符

```kotlin
import org.pcsoft.framework.kunit.distance.*

// + / - : 同一组内，不同长度单位之间自动换算
val a = 1.miles + 500.meters   // KLengthUnitInstance，归一化为米
val b = 2.miles - 800.meters

// 比较
2.miles > 1.miles               // true
1.miles == 1609.344.meters      // true（归一化后的数值相同）
// 5.hectares > 5.meters        // 无法编译：面积与长度是不同的类型

// * / / : 当两个操作数都是静态定维时，保持在长度类型族内
val area = 200.meters * 50.meters   // KAreaUnitInstance: value=10000.0（m²）
val lengthAgain = area / 50.meters  // KLengthUnitInstance: value=200.0（m）
val ratio = 10.meters / 2.meters    // KMixedUnitInstance（无量纲），value=5.0
```

### 比较与相等性

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个**相同类型**（相同维度）值的归一化 `value`。
混用不同维度（例如长度与面积）会被编译器拒绝——不存在这样的运算符——与 `+`/`-` 的规则一致。
跨维度的 `equals` 直接返回 `false`。

## 指数 2 - 面积

`KAreaUnitInstance` 表示面积，例如 `length * length` 的结果，或用 infix `pow` 运算符对长度求平方
（`2.meters pow 2` == `(2 m)²` == 4 m²，`2 kilo meters pow 2` == 4 000 000 m²）。没有 `squareXxx`
创建函数——`pow` 是唯一的幂运算语法（参见下文"用 `pow` 求幂"一节）。还可以使用以下命名的特殊单位
（`KDistanceDerivedUnit`）作为转换/格式化目标：

| 特殊单位 | 枚举值 | 符号 | 创建函数 | 1 单位对应的 m² |
|---|---:|---:|---:|---:|
| 公亩 | `KDistanceDerivedUnit.ARE` | `a` | `Number.ares` | 100.0 |
| 公顷 | `KDistanceDerivedUnit.HECTARE` | `ha` | `Number.hectares` | 10 000.0 |
| 英亩 | `KDistanceDerivedUnit.ACRE` | `ac` | `Number.acres` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val plot = 3.hectares
plot.value                                   // 30000.0（平方米）
plot.valueAs(KDistanceDerivedUnit.ARE)          // 300.0
plot.valueAs(KDistanceDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters * 50.meters     // KAreaUnitInstance（10 000 m²）
computed.valueAs(KDistanceDerivedUnit.HECTARE) // 1.0

plot + computed                              // 允许：两者都是面积 -> KAreaUnitInstance
// plot + 5.meters                           // 无法编译：面积与长度
```

## 指数 3 - 体积

`KVolumeUnitInstance` 表示体积，例如 `length * length * length`、`area * length`，或对长度求三次方
（`2.meters pow 3` == 8 m³，`2 kilo meters pow 3`）。与面积一样，没有 `cubicXxx` 创建函数，请使用 `pow`
（参见下文"用 `pow` 求幂"一节）。还可以使用以下命名的特殊单位：

| 特殊单位 | 枚举值 | 符号 | 创建函数 | 1 单位对应的 m³ |
|---|---:|---:|---:|---:|
| 升 | `KDistanceDerivedUnit.LITER` | `L` | `Number.liters` | 0.001 |
| 美制加仑 | `KDistanceDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons` | 0.003785411784 |
| 英制加仑 | `KDistanceDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons` | 0.00454609 |
| 美制液量盎司 | `KDistanceDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces` | 2.95735295625e-5 |
| 石油桶 | `KDistanceDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val tank = 200.liters
tank.value                                        // 0.2（立方米）
tank.valueAs(KDistanceDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters * 2.meters * 2.meters   // KVolumeUnitInstance（8 m³）
cube.valueAs(KDistanceDerivedUnit.LITER)    // 8000.0

tank + cube                                  // 允许：两者都是体积 -> KVolumeUnitInstance
```

## 用 `pow` 求幂

用 infix `pow` 运算符对值进行整数次幂运算。Kotlin 没有可重载的 `^` 运算符（也没有 `^=`），因此 `pow`
是所有分组中唯一的幂运算语法——不存在 `squareXxx`/`cubicXxx` 创建函数。

`pow` 会对值求幂，**并**将每个指数乘以 `n`，因此 `2.meters pow 2` 是 `(2 m)² = 4 m²`（是对值求幂，而不
仅仅是指数）。对于距离分组，结果带有量纲：`pow 2` 得到 `KAreaUnitInstance`，`pow 3` 得到
`KVolumeUnitInstance`，其他指数得到通用的 `KDistanceUnitInstance`。

```kotlin
import org.pcsoft.framework.kunit.distance.*

val area = 2.meters pow 2         // KAreaUnitInstance: 4.0 m²
val big = 2 kilo meters pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = 2.meters pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = 2.meters pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = 2.meters pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow` 是命名的 infix 函数，因此其结合优先级**弱于** `* / + -`；在混合表达式中请加括号
（`(a * b) pow 2`）。它可用于每个单位分组——例如 `2.hours pow 2`（由于时间没有带量纲的幂类型，得到通用的
`KMixedUnitInstance`）。

## SI 前缀

任何 `KDistanceUnit` 都可以与 24 个 SI 前缀（`KUnitPrefix`，根包，从 Quetta/Q 到 Quecto/q）中的任意一个
组合，使用每组各自的 infix 构造函数（直接返回具体单位）以及 `with`（用于 valueAs/toString 目标）：

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*

// 构造："5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
fiveKm.value // 5000.0

// 使用带前缀的目标读取数值
val d = 5.miles
d.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER)  // 8.04672（千米）
d.toString(KUnitPrefix.KILO with KDistanceUnit.METER) // "8.04672 km"

// 前缀也可以与派生单位（面积/体积）组合
val tank = 200.liters
tank.valueAs(KUnitPrefix.MILLI with KDistanceDerivedUnit.LITER) // 200000.0（毫升）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.distance.*

5.meters.toString()                        // "5.0 m"（基础单位表示）
5.miles.toString(KDistanceUnit.MILE)          // "5.0 mi"
(200.meters * 50.meters).toString(KDistanceDerivedUnit.HECTARE) // "1.0 ha"
```
