# 质量

包: `org.pcsoft.framework.kunit.mass`
基准单位: **克** (`KMassUnit.BASE == KMassUnit.GRAM`)

质量组用于建模质量的量。它是一个 **简单的一维** 组（没有像距离组那样的指数特化子类型，也没有像时间组那样的
`Duration` 支撑）：`KMassUnitInstance` 包装单个 `KMassUnit.GRAM` 项，始终以克归一化存储。

基准单位刻意选为 **克而非千克**。千克并不是一个专门的单位——它就是 `kilo.grams`，即把 SI 前缀 `kilo`
应用于克。克的任何十进制量级（毫克、千克等）都通过 SI 前缀以相同的通用方式获得。

## 单位

| 体系 | 单位 | 枚举值 | 符号 | 令牌 | 1 单位对应的克 |
|---|---|---|---|---:|---:|
| 公制 | 克 | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| 公制 | 吨（公吨） | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| 公制 | 克拉（公制） | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| 常衡 | 格令 | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| 常衡 | 打兰 | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| 常衡 | 盎司 | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| 常衡 | 磅 | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| 常衡 | 英石 | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| 常衡 | 英担 US（短） | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| 常衡 | 英担 UK（长） | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| 常衡 | 短吨（US） | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| 常衡 | 长吨（UK） | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| 常衡 | 斯勒格 | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| 金衡 | 本尼威特 | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| 金衡 | 金衡盎司 | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| 金衡 | 金衡磅 | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| 历史 | 德国磅 | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| 历史 | 岑特纳 | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| 历史 | 罗特 | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| 地区 | 斤 | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| 地区 | 两 | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| 地区 | 匁 | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| 地区 | 贯 | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| 科学 | 道尔顿（u） | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

每个 `令牌` 都是一个值为 1 的 `KMassUnitInstance`，用于 `of`（构建）和 `into`（读取）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g（千克即 `kilo.grams`）
m.value                      // 2000.0（归一化为克）
m into pounds                // ≈ 4.409（以磅读取）
(1 of pounds) into grams     // 453.59237
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : 同组，单位间自动转换
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// 比较
(1 of kilo.grams) == (1000 of grams)         // true（归一化量相同）
(1 of kilo.grams) > (500 of grams)           // true
```

### 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个 `KMassUnitInstance` 的归一化 `value`（克）。`equals` 按归一化量比较，
因此 `(1 of kilo.grams) == (1000 of grams)`。

## 使用 `pow` 求幂

用中缀运算符 `pow` 进行整数次幂（Kotlin 没有可重载的 `^`）。对于质量组，`pow` 返回通用的
`KMixedUnitInstance`（质量没有带量纲的幂类型）:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## SI 前缀

质量接受 **任意** 量级，因此可以通过属性访问将每个 SI 前缀构建器（`quetta` … `quecto`）与任意质量单位组合。
千克即 `kilo.grams`，毫克即 `milli.grams`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     （千克）
(1 of milli.grams).value   // 0.001      （毫克）

(2500 of grams) into kilo.grams  // 2.5
```

## toString 格式化

只存在基准单位的 `toString()`；要以特定单位格式化，请使用 `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g"（基准单位表示）
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```
