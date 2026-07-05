# 距離

パッケージ: `org.pcsoft.framework.kunit.distance`
基本単位: **メートル**(`KDistanceUnit.BASE == KDistanceUnit.METER`)

距離グループは、開いた基底ラッパー `KDistanceUnitInstance`(`KDistanceUnit.BASE` の単一の項、**任意の**指数)
の下で、各指数をそれぞれ独立したコンパイル時安全な型としてモデル化します:

* **`KLengthUnitInstance`** — 指数1(長さ)
* **`KAreaUnitInstance`** — 指数2(面積)
* **`KVolumeUnitInstance`** — 指数3(体積)

値は作成に使用された単位に関係なく、常にメートル(または平方/立方メートル)に正規化されて保存されます。長さ、
面積、体積は異なる型であるため、`+`/`-`/比較でそれらを混在させると**コンパイルエラー**になります(そのような
演算子は存在しません)。一方 `*`/`/` は可能な限り同じ型ファミリー内に留まり(`length * length = area`、
`area / length = length`)、`{1,2,3}` 以外の指数(または指数0の無次元の結果)については
`KDistanceUnitInstance`/`KMixedUnitInstance` にフォールバックします。

## 指数1 - 長さ

| 単位 | Enum 値 | 記号 | コンストラクタ | メートル換算(1単位) |
|---|---|---|---:|---:|
| メートル | `KDistanceUnit.METER` | `m` | `Number.meters` | 1.0 |
| マイル | `KDistanceUnit.MILE` | `mi` | `Number.miles` | 1609.344 |
| 海里 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles` | 1852.0 |
| ヤード | `KDistanceUnit.YARD` | `yd` | `Number.yards` | 0.9144 |
| フィート | `KDistanceUnit.FOOT` | `ft` | `Number.feet` | 0.3048 |
| インチ | `KDistanceUnit.INCH` | `in` | `Number.inches` | 0.0254 |
| ファゾム | `KDistanceUnit.FATHOM` | `ftm` | `Number.fathoms` | 1.8288 |
| チェーン | `KDistanceUnit.CHAIN` | `ch` | `Number.chains` | 20.1168 |
| ファーロング | `KDistanceUnit.FURLONG` | `fur` | `Number.furlongs` | 201.168 |
| 天文単位 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits` | 1.495978707e11 |
| 光秒 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds` | 299792458.0 |
| 光分 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes` | 1.798754748e10 |
| 光時 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `Number.lightHours` | 1.0792528488e12 |
| 光日 | `KDistanceUnit.LIGHT_DAY` | `ld` | `Number.lightDays` | 2.59020683712e13 |
| 光週 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks` | 1.813144785984e14 |
| 光年 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `Number.lightYears` | 9.4607304725808e15 |
| パーセク | `KDistanceUnit.PARSEC` | `pc` | `Number.parsecs` | 3.0856775814913673e16 |

上記のすべての単位には、`valueAs`/`toString` のターゲットや接頭辞 infix 関数の `unit` 引数として
使用できる bare な `val` エイリアスがあります: `meters`、`miles`、`nauticalMiles`、`yards`、
`feet`、`inches`、`fathoms`、`chains`、`furlongs`、`astronomicalUnits`、`lightSeconds`、
`lightMinutes`、`lightHours`、`lightDays`、`lightWeeks`、`lightYears`、`parsecs`。

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5.miles
d.value                        // 8046.72(メートルに正規化)
d.valueAs(KDistanceUnit.MILE)    // 5.0(マイルに戻して読み取り)
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452(海里として読み取り)
```

### 演算子

```kotlin
import org.pcsoft.framework.kunit.distance.*

// + / - : 同じグループ内、異なる長さの単位間の自動変換
val a = 1.miles + 500.meters   // KLengthUnitInstance、メートルに正規化
val b = 2.miles - 800.meters

// 比較
2.miles > 1.miles               // true
1.miles == 1609.344.meters      // true(正規化された値が同じ)
// 5.hectares > 5.meters        // コンパイルされない: 面積と長さは異なる型

// * / / : 両方のオペランドが静的に次元付けされている場合、長さファミリー内に留まる
val area = 200.meters * 50.meters   // KAreaUnitInstance: value=10000.0(m²)
val lengthAgain = area / 50.meters  // KLengthUnitInstance: value=200.0(m)
val ratio = 10.meters / 2.meters    // KMixedUnitInstance(無次元)、value=5.0
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は**同じ型**(同じ次元)を持つ2つの値の正規化された `value`
を比較します。異なる次元(例: 長さと面積)を混在させることは、`+`/`-` のルールと同様にコンパイラによって
拒否されます — そのような演算子は存在しません。次元をまたぐ `equals` は単に `false` を返します。

## 指数2 - 面積

`KAreaUnitInstance` は面積を表します(例: `length * length` の結果)。各単位に対する `square…`
コンストラクタ(`200.squareMeters`、`5.squareMiles`、… および接頭辞 infix 形式 `5 kilo squareMeters`
== 5平方キロメートル == 5 000 000 m²)に加えて、次の名前付き特殊単位(`KDistanceDerivedUnit`)を
変換・フォーマットのターゲットとして使用できます。

| 特殊単位 | Enum 値 | 記号 | コンストラクタ | m²換算(1単位) |
|---|---:|---:|---:|---:|
| アール | `KDistanceDerivedUnit.ARE` | `a` | `Number.ares` | 100.0 |
| ヘクタール | `KDistanceDerivedUnit.HECTARE` | `ha` | `Number.hectares` | 10 000.0 |
| エーカー | `KDistanceDerivedUnit.ACRE` | `ac` | `Number.acres` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val plot = 3.hectares
plot.value                                   // 30000.0(m²)
plot.valueAs(KDistanceDerivedUnit.ARE)          // 300.0
plot.valueAs(KDistanceDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters * 50.meters     // KAreaUnitInstance(10 000 m²)
computed.valueAs(KDistanceDerivedUnit.HECTARE) // 1.0

plot + computed                              // 許可される: 両方とも面積 -> KAreaUnitInstance
// plot + 5.meters                           // コンパイルされない: 面積と長さ
```

## 指数3 - 体積

`KVolumeUnitInstance` は体積を表します(例: `length * length * length` または `area * length`)。各単位に
対する `cubic…` コンストラクタ(`2.cubicMeters`、`3.cubicMiles`、… および接頭辞 infix 形式
`5 kilo cubicMeters`)に加えて、次の名前付き特殊単位が利用できます。

| 特殊単位 | Enum 値 | 記号 | コンストラクタ | m³換算(1単位) |
|---|---:|---:|---:|---:|
| リットル | `KDistanceDerivedUnit.LITER` | `L` | `Number.liters` | 0.001 |
| 米国液量ガロン | `KDistanceDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons` | 0.003785411784 |
| 英国ガロン | `KDistanceDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons` | 0.00454609 |
| 米国液量オンス | `KDistanceDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces` | 2.95735295625e-5 |
| オイルバレル | `KDistanceDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val tank = 200.liters
tank.value                                        // 0.2(m³)
tank.valueAs(KDistanceDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters * 2.meters * 2.meters   // KVolumeUnitInstance(8 m³)
cube.valueAs(KDistanceDerivedUnit.LITER)    // 8000.0

tank + cube                                  // 許可される: 両方とも体積 -> KVolumeUnitInstance
```

## SI 接頭辞

任意の `KDistanceUnit` は、24種類の SI 接頭辞(`KUnitPrefix`、ルートパッケージ、Quetta/Q から
Quecto/q まで)のいずれとも、グループごとの infix 構築関数（具体単位を直接返す）と `with`(valueAs/toString ターゲット用)を使って
組み合わせることができます。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*

// 構築: "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
fiveKm.value // 5000.0

// 接頭辞付きのターゲットを使って値を読み戻す
val d = 5.miles
d.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER)  // 8.04672(km)
d.toString(KUnitPrefix.KILO with KDistanceUnit.METER) // "8.04672 km"

// 接頭辞は派生単位(面積/体積)とも組み合わせ可能
val tank = 200.liters
tank.valueAs(KUnitPrefix.MILLI with KDistanceDerivedUnit.LITER) // 200000.0(mL)
```

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.distance.*

5.meters.toString()                        // "5.0 m"(基本単位表現)
5.miles.toString(KDistanceUnit.MILE)          // "5.0 mi"
(200.meters * 50.meters).toString(KDistanceDerivedUnit.HECTARE) // "1.0 ha"
```
