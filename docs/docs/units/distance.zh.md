# 长度

包：`org.pcsoft.framework.kunit.distance`
基础单位：**米**（`KDistanceUnit.BASE == KDistanceUnit.METER`）

`KLengthUnitInstance` 将 `KMixedUnitInstance` 限定为 `KDistanceUnit.BASE` 的单个项进行包装，指数可以是任意值——
指数 1 表示普通长度，2 表示面积，3 表示体积。无论创建时使用的是哪个单位，数值始终以米（或平方米/立方米）
归一化存储。

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
5.hectares > 5.meters           // 抛出 IllegalStateException（面积 vs 长度，指数不同）

// * / / : 始终允许，生成一个具有新指数的 KMixedUnitInstance
val area = 200.meters * 50.meters   // KMixedUnitInstance: value=10000.0, units=[METER^2]
val lengthAgain = area / 50.meters.toUnit() // KMixedUnitInstance: value=200.0, units=[METER^1]
```

### 比较与相等性

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个具有**相同指数**的 `KLengthUnitInstance` 的归一化 `value`。
在不同指数之间进行比较（例如长度与面积）会抛出 `IllegalStateException`，与 `+`/`-` 的规则一致。

## 指数 2 - 面积

指数为 2 的 `KLengthUnitInstance` 表示面积，例如两个长度相乘的结果。除了原始的
`KDistanceUnit.BASE^2`（平方米）表示外，还可以使用以下命名的特殊单位（`KDistanceDerivedUnit`）作为
转换/格式化目标：

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

val computed = 200.meters * 50.meters     // KMixedUnitInstance, units=[METER^2]
computed.toDistance().valueAs(KDistanceDerivedUnit.HECTARE) // 1.0

plot + computed.toDistance()                // 允许：两者都是指数 2（面积）
plot + 5.meters                              // 抛出 IllegalStateException（面积 vs 长度）
```

## 指数 3 - 体积

指数为 3 的 `KLengthUnitInstance` 表示体积。除了原始的 `KDistanceUnit.BASE^3`（立方米）表示外，还可以
使用以下命名的特殊单位：

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

val cube = 2.meters * 2.meters * 2.meters   // KMixedUnitInstance, units=[METER^3]
cube.toDistance().valueAs(KDistanceDerivedUnit.LITER) // 8000.0

tank + cube.toDistance()                        // 允许：两者都是指数 3（体积）
```

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
(200.meters * 50.meters).toDistance().toString(KDistanceDerivedUnit.HECTARE) // "1.0 ha"
```
