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

所有值都用 `number of <令牌>` 构建，用 `value into <令牌>` 读回。

## 指数 1 - 长度

| 单位 | 枚举值 | 符号 | 令牌 | 1 单位对应的米数 |
|---|---|---|---:|---:|
| 米 | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| 英里 | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| 海里 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| 码 | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| 英尺 | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| 英寸 | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| 英寻 | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| 链 | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| 弗隆 | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| 天文单位 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| 光秒 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `lightSeconds` | 299792458.0 |
| 光分 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `lightMinutes` | 1.798754748e10 |
| 光时 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `lightHours` | 1.0792528488e12 |
| 光日 | `KDistanceUnit.LIGHT_DAY` | `ld` | `lightDays` | 2.59020683712e13 |
| 光周 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `lightWeeks` | 1.813144785984e14 |
| 光年 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `lightYears` | 9.4607304725808e15 |
| 秒差距 | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |

每个`令牌`都是值为 1 的 `KLengthUnitInstance`，同时用于 `of`（构建）和 `into`（读取）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72（归一化为米）
d into miles          // 5.0（换算回英里读取）
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : 同一组内，不同长度单位之间自动换算
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance，归一化为米
val b = (2 of miles) - (800 of meters)

// 比较
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true（归一化后的数值相同）
// (5 of hectares) > (5 of meters)       // 无法编译：面积与长度是不同的类型

// * / / : 当两个操作数都是静态定维时，保持在长度类型族内
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0（m²）
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0（m）
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance（无量纲），value=5.0
```

### 比较与相等性

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个**相同类型**（相同维度）值的归一化 `value`。
混用不同维度（例如长度与面积）会被编译器拒绝——不存在这样的运算符——与 `+`/`-` 的规则一致。
跨维度的 `equals` 直接返回 `false`。

## 指数 2 - 面积

`KAreaUnitInstance` 表示面积，例如 `length * length` 的结果，或用 infix `pow` 运算符对长度求平方
（`(2 of meters) pow 2` == `(2 m)²` == 4 m²，`(2 of kilo.meters) pow 2` == 4 000 000 m²）。没有 `squareXxx`
令牌——`pow` 是唯一的幂运算语法（参见下文“用 `pow` 求幂”一节）。还可以使用以下命名的特殊单位令牌：

| 特殊单位 | 符号 | 令牌 | 1 单位对应的 m² |
|---|---:|---:|---:|
| 公亩 | `a` | `ares` | 100.0 |
| 公顷 | `ha` | `hectares` | 10 000.0 |
| 英亩 | `ac` | `acres` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0（平方米）
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance（10 000 m²）
computed into hectares                           // 1.0

plot + computed   // 允许：两者都是面积 -> KAreaUnitInstance
// plot + (5 of meters)  // 无法编译：面积与长度
```

## 指数 3 - 体积

`KVolumeUnitInstance` 表示体积，例如 `length * length * length`、`area * length`，或对长度求三次方
（`(2 of meters) pow 3` == 8 m³）。与面积一样，没有 `cubicXxx` 令牌，请使用 `pow`（参见下文“用 `pow` 求幂”
一节）。还可以使用以下命名的特殊单位令牌：

| 特殊单位 | 符号 | 令牌 | 1 单位对应的 m³ |
|---|---:|---:|---:|
| 升 | `L` | `liters` | 0.001 |
| 美制加仑 | `gal (US)` | `usGallons` | 0.003785411784 |
| 英制加仑 | `gal (UK)` | `imperialGallons` | 0.00454609 |
| 美制液量盎司 | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| 石油桶 | `bbl` | `oilBarrels` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2（立方米）
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance（8 m³）
cube into liters                                          // 8000.0

tank + cube         // 允许：两者都是体积 -> KVolumeUnitInstance
```

## 用 `pow` 求幂

用 infix `pow` 运算符对值进行整数次幂运算。Kotlin 没有可重载的 `^` 运算符（也没有 `^=`），因此 `pow`
是所有分组中唯一的幂运算语法——不存在 `squareXxx`/`cubicXxx` 令牌。

`pow` 会对值求幂，**并**将每个指数乘以 `n`，因此 `(2 of meters) pow 2` 是 `(2 m)² = 4 m²`（是对值求幂，而不
仅仅是指数）。对于距离分组，结果带有量纲：`pow 2` 得到 `KAreaUnitInstance`，`pow 3` 得到
`KVolumeUnitInstance`，其他指数得到通用的 `KDistanceUnitInstance`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

val area = (2 of meters) pow 2         // KAreaUnitInstance: 4.0 m²
val big = (2 of kilo.meters) pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = (2 of meters) pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = (2 of meters) pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = (2 of meters) pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow` 的结合优先级**弱于** `* / + -`；在混合表达式中请加括号（`(a * b) pow 2`）。它可用于每个单位分组——
例如 `(2 of hours) pow 2`（由于时间没有带量纲的幂类型，得到通用的 `KMixedUnitInstance`）。

## SI 前缀

任何长度单位都可以与 24 个 SI 前缀**构建器**（`kilo`、`milli`、…；根包）通过属性访问组合，生成用于
`of`/`into` 的值为 1 的模板：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// 构造："5 of kilo.meters" -> KLengthUnitInstance（== 5000 m）
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// 用带前缀的单位读回数值
val d = 5 of miles
d into kilo.meters  // 8.04672（千米）

// 前缀也可以与命名的面积/体积令牌组合
val tank = 200 of liters
tank into milli.liters  // 200000.0（毫升）
```

## toString 格式化

只存在基础单位 `toString()`；用 `into` 格式化特定单位：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m"（基础单位表示）
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```
