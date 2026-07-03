# 長さ

パッケージ: `org.pcsoft.framework.kunit.length`
基本単位: **メートル**(`KLengthUnit.BASE == KLengthUnit.METER`)

`KLengthUnitInstance` は `KLengthUnit.BASE` の単一の項に限定された `KMixedUnitInstance` を、任意の指数で
ラップします - 純粋な長さは指数1、面積は2、体積は3です。値は作成に使用された単位に関係なく、常にメートル
(または平方/立方メートル)に正規化されて保存されます。

## 指数1 - 長さ

| 単位 | Enum 値 | 記号 | コンストラクタ | メートル換算(1単位) |
|---|---|---|---:|---:|
| メートル | `KLengthUnit.METER` | `m` | `Number.meters()` | 1.0 |
| マイル | `KLengthUnit.MILE` | `mi` | `Number.miles()` | 1609.344 |
| 海里 | `KLengthUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles()` | 1852.0 |
| ヤード | `KLengthUnit.YARD` | `yd` | `Number.yards()` | 0.9144 |
| フィート | `KLengthUnit.FOOT` | `ft` | `Number.feet()` | 0.3048 |
| インチ | `KLengthUnit.INCH` | `in` | `Number.inches()` | 0.0254 |
| ファゾム | `KLengthUnit.FATHOM` | `ftm` | `Number.fathoms()` | 1.8288 |
| チェーン | `KLengthUnit.CHAIN` | `ch` | `Number.chains()` | 20.1168 |
| ファーロング | `KLengthUnit.FURLONG` | `fur` | `Number.furlongs()` | 201.168 |
| 天文単位 | `KLengthUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits()` | 1.495978707e11 |
| 光秒 | `KLengthUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds()` | 299792458.0 |
| 光分 | `KLengthUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes()` | 1.798754748e10 |
| 光時 | `KLengthUnit.LIGHT_HOUR` | `lh` | `Number.lightHours()` | 1.0792528488e12 |
| 光日 | `KLengthUnit.LIGHT_DAY` | `ld` | `Number.lightDays()` | 2.59020683712e13 |
| 光週 | `KLengthUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks()` | 1.813144785984e14 |
| 光年 | `KLengthUnit.LIGHT_YEAR` | `ly` | `Number.lightYears()` | 9.4607304725808e15 |
| パーセク | `KLengthUnit.PARSEC` | `pc` | `Number.parsecs()` | 3.0856775814913673e16 |

上記のすべての単位には、`valueAs`/`toString` のターゲットや接頭辞 infix 関数の `unit` 引数として
使用できる bare な `val` エイリアスがあります: `meters`、`miles`、`nauticalMiles`、`yards`、
`feet`、`inches`、`fathoms`、`chains`、`furlongs`、`astronomicalUnits`、`lightSeconds`、
`lightMinutes`、`lightHours`、`lightDays`、`lightWeeks`、`lightYears`、`parsecs`。

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.miles()
d.value                        // 8046.72(メートルに正規化)
d.valueAs(KLengthUnit.MILE)    // 5.0(マイルに戻して読み取り)
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452(海里として読み取り)
```

### 演算子

```kotlin
import org.pcsoft.framework.kunit.length.*

// + / - : 同じグループ内、異なる長さの単位間の自動変換
val a = 1.miles() + 500.meters()   // KLengthUnitInstance、メートルに正規化
val b = 2.miles() - 800.meters()

// 比較
2.miles() > 1.miles()               // true
1.miles() == 1609.344.meters()      // true(正規化された値が同じ)
5.hectares() > 5.meters()           // IllegalStateException が発生(面積 vs 長さ、指数が異なる)

// * / / : 常に許可され、新しい指数を持つ KMixedUnitInstance を生成
val area = 200.meters() * 50.meters()   // KMixedUnitInstance: value=10000.0, units=[METER^2]
val lengthAgain = area / 50.meters().toKMixedUnitInstance() // KMixedUnitInstance: value=200.0, units=[METER^1]
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は**同じ指数**を持つ2つの `KLengthUnitInstance` の正規化された `value`
を比較します。異なる指数間(例: 長さと面積)の比較は、`+`/`-` のルールと同様に `IllegalStateException`
を投げます。

## 指数2 - 面積

指数が2の `KLengthUnitInstance` は面積を表します(例: 2つの長さを掛けた結果)。生の
`KLengthUnit.BASE^2`(平方メートル)表現に加えて、次の名前付き特殊単位(`KLengthDerivedUnit`)を
変換・フォーマットのターゲットとして使用できます。

| 特殊単位 | Enum 値 | 記号 | コンストラクタ | m²換算(1単位) |
|---|---:|---:|---:|---:|
| アール | `KLengthDerivedUnit.ARE` | `a` | `Number.ares()` | 100.0 |
| ヘクタール | `KLengthDerivedUnit.HECTARE` | `ha` | `Number.hectares()` | 10 000.0 |
| エーカー | `KLengthDerivedUnit.ACRE` | `ac` | `Number.acres()` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val plot = 3.hectares()
plot.value                                   // 30000.0(m²)
plot.valueAs(KLengthDerivedUnit.ARE)          // 300.0
plot.valueAs(KLengthDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters() * 50.meters()     // KMixedUnitInstance, units=[METER^2]
computed.toKLengthUnit().valueAs(KLengthDerivedUnit.HECTARE) // 1.0

plot + computed.toKLengthUnit()                // 許可される: 両方とも指数2(面積)
plot + 5.meters()                              // IllegalStateException が発生(面積 vs 長さ)
```

## 指数3 - 体積

指数が3の `KLengthUnitInstance` は体積を表します。生の `KLengthUnit.BASE^3`(立方メートル)表現に
加えて、次の名前付き特殊単位が利用できます。

| 特殊単位 | Enum 値 | 記号 | コンストラクタ | m³換算(1単位) |
|---|---:|---:|---:|---:|
| リットル | `KLengthDerivedUnit.LITER` | `L` | `Number.liters()` | 0.001 |
| 米国液量ガロン | `KLengthDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons()` | 0.003785411784 |
| 英国ガロン | `KLengthDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons()` | 0.00454609 |
| 米国液量オンス | `KLengthDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces()` | 2.95735295625e-5 |
| オイルバレル | `KLengthDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels()` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val tank = 200.liters()
tank.value                                        // 0.2(m³)
tank.valueAs(KLengthDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters() * 2.meters() * 2.meters()   // KMixedUnitInstance, units=[METER^3]
cube.toKLengthUnit().valueAs(KLengthDerivedUnit.LITER) // 8000.0

tank + cube.toKLengthUnit()                        // 許可される: 両方とも指数3(体積)
```

## SI 接頭辞

任意の `KLengthUnit` は、24種類の SI 接頭辞(`KUnitPrefix`、ルートパッケージ、Quetta/Q から
Quecto/q まで)のいずれとも、汎用の infix 構築関数と `with`(valueAs/toString ターゲット用)を使って
組み合わせることができます。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 構築: "5 kilo meters" -> KPrefixBuilder -> KMixedUnitInstance -> KLengthUnitInstance
val fiveKm = (5 kilo meters).toKMixedUnitInstance().toKLengthUnit()
fiveKm.value // 5000.0

// 接頭辞付きのターゲットを使って値を読み戻す
val d = 5.miles()
d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER)  // 8.04672(km)
d.toString(KUnitPrefix.KILO with KLengthUnit.METER) // "8.04672 km"

// 接頭辞は派生単位(面積/体積)とも組み合わせ可能
val tank = 200.liters()
tank.valueAs(KUnitPrefix.MILLI with KLengthDerivedUnit.LITER) // 200000.0(mL)
```

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.length.*

5.meters().toString()                        // "5.0 m"(基本単位表現)
5.miles().toString(KLengthUnit.MILE)          // "5.0 mi"
(200.meters() * 50.meters()).toKLengthUnit().toString(KLengthDerivedUnit.HECTARE) // "1.0 ha"
```
