# 距離

パッケージ: `org.pcsoft.framework.kunit.distance`
基本単位: **メートル**(`KDistanceUnit.BASE == KDistanceUnit.METER`)

距離グループは、開いた基底ラッパー `KDistanceUnitInstance`(`KDistanceUnit.BASE` の単一の項、**任意の**指数)
の下で、各指数をそれぞれ独立したコンパイル時安全な型としてモデル化します:

* **`KLengthUnitInstance`** — 指数1(長さ)
* **`KAreaUnitInstance`** — 指数2(面積)
* **`KVolumeUnitInstance`** — 指数3(体積)

値は常にメートル(または平方/立方メートル)に正規化されて保存されます。長さ、面積、体積は異なる型であるため、
`+`/`-`/比較でそれらを混在させると**コンパイルエラー**になります(そのような演算子は存在しません)。一方
`*`/`/` は可能な限り同じ型ファミリー内に留まり(`length * length = area`、`area / length = length`)、
`{1,2,3}` 以外の指数(または指数0の無次元の結果)については `KDistanceUnitInstance`/`KMixedUnitInstance`
にフォールバックします。

すべての値は `number of <トークン>` で作成し、`value into <トークン>` で読み戻します。

## 指数1 - 長さ

| 単位 | Enum 値 | 記号 | トークン | メートル換算(1単位) |
|---|---|---|---:|---:|
| メートル | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| マイル | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| 海里 | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| ヤード | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| フィート | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| インチ | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| ファゾム | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| チェーン | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| ファーロング | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| 天文単位 | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| パーセク | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |
| キュビット | `KDistanceUnit.CUBIT` | `cubit` | `cubits` | 0.4572 |
| ローマフィート (pes) | `KDistanceUnit.ROMAN_FOOT` | `pes` | `romanFeet` | 0.2957 |
| ローマパッスス (passus) | `KDistanceUnit.ROMAN_PACE` | `passus` | `romanPaces` | 1.4787 |
| スタディオン | `KDistanceUnit.STADIUM` | `stadium` | `stadia` | 185.0 |
| ローママイル (mille passus) | `KDistanceUnit.ROMAN_MILE` | `mp` | `romanMiles` | 1481.5 |
| ロッド (perch) | `KDistanceUnit.ROD` | `rod` | `rods` | 5.0292 |
| リーグ | `KDistanceUnit.LEAGUE` | `lea` | `leagues` | 4828.032 |
| ケーブル長 | `KDistanceUnit.CABLE_LENGTH` | `cable` | `cableLengths` | 185.2 |
| ヴェルスタ | `KDistanceUnit.VERST` | `verst` | `versts` | 1066.8 |
| プロイセンマイル | `KDistanceUnit.PRUSSIAN_MILE` | `prussian mi` | `prussianMiles` | 7532.5 |

### 光の到達距離（プレフィックス不可の `light` グループ）

光の到達距離はプレフィックス不可の `light` ビルダーにまとめられ、`5 of light.seconds`、
`3 of light.years` のようにほぼ自然な文として記述できます。これらは SI プレフィックスを一切
受け付けません（`kilo.lightYears` は物理的に無意味なため）。

| 単位 | 列挙値 | 記号 | トークン | 1 単位のメートル値 |
|---|---|---|---:|---:|
| 光秒 | `KDistanceUnit.LIGHT_SECOND` | `ls` | `light.seconds` | 299792458.0 |
| 光分 | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `light.minutes` | 1.798754748e10 |
| 光時 | `KDistanceUnit.LIGHT_HOUR` | `lh` | `light.hours` | 1.0792528488e12 |
| 光日 | `KDistanceUnit.LIGHT_DAY` | `ld` | `light.days` | 2.59020683712e13 |
| 光週 | `KDistanceUnit.LIGHT_WEEK` | `lw` | `light.weeks` | 1.813144785984e14 |
| 光年 | `KDistanceUnit.LIGHT_YEAR` | `ly` | `light.years` | 9.4607304725808e15 |

各 `トークン` は値1の `KLengthUnitInstance` であり、`of`(作成)と `into`(読み取り)の両方で使用します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72(メートルに正規化)
d into miles          // 5.0(マイルに戻して読み取り)
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : 同じグループ内、異なる長さの単位間の自動変換
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance、メートルに正規化
val b = (2 of miles) - (800 of meters)

// 比較
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true(正規化された値が同じ)
// (5 of hectares) > (5 of meters)       // コンパイルされない: 面積と長さは異なる型

// * / / : 両方のオペランドが静的に次元付けされている場合、長さファミリー内に留まる
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0(m²)
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0(m)
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance(無次元)、value=5.0
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は**同じ型**(同じ次元)を持つ2つの値の正規化された `value` を比較します。
異なる次元(例: 長さと面積)を混在させることは、`+`/`-` のルールと同様にコンパイラによって拒否されます —
そのような演算子は存在しません。次元をまたぐ `equals` は単に `false` を返します。

## 指数2 - 面積

`KAreaUnitInstance` は面積を表します(例: `length * length` の結果、または長さを infix `pow` 演算子で
2乗した結果: `(2 of meters) pow 2` == `(2 m)²` == 4 m²、`(2 of kilo.meters) pow 2` == 4 000 000 m²)。
`squareXxx` トークンはありません — `pow` が唯一のべき乗構文です(下記「[`pow` によるべき乗](#pow)」の節を
参照)。次の名前付き特殊単位トークンが利用できます。

| 特殊単位 | 記号 | トークン | m²換算(1単位) |
|---|---:|---:|---:|
| アール | `a` | `ares` | 100.0 |
| ヘクタール | `ha` | `hectares` | 10 000.0 |
| エーカー | `ac` | `acres` | 4046.8564224 |
| ルード | `ro` | `roods` | 1011.7141056 |
| 平方パーチ（平方ロッド） | `perch²` | `squarePerches` | 25.29285264 |
| モルゲン（プロイセン） | `Mg` | `morgens` | 2553.22 |
| ヨッホ（オーストリア） | `Joch` | `jochs` | 5754.642 |
| タークヴェルク（バイエルン） | `Tw` | `tagwerks` | 3407.27 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0(m²)
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance(10 000 m²)
computed into hectares                           // 1.0

plot + computed   // 許可される: 両方とも面積 -> KAreaUnitInstance
// plot + (5 of meters)  // コンパイルされない: 面積と長さ
```

## 指数3 - 体積

`KVolumeUnitInstance` は体積を表します(例: `length * length * length`、`area * length`、または長さを
3乗した結果: `(2 of meters) pow 3` == 8 m³)。面積と同様に `cubicXxx` トークンはなく、`pow` を使用します
(下記「[`pow` によるべき乗](#pow)」の節を参照)。次の名前付き特殊単位トークンが利用できます。

| 特殊単位 | 記号 | トークン | m³換算(1単位) |
|---|---:|---:|---:|
| リットル | `L` | `liters` | 0.001 |
| 米国液量ガロン | `gal (US)` | `usGallons` | 0.003785411784 |
| 英国ガロン | `gal (UK)` | `imperialGallons` | 0.00454609 |
| 米国液量オンス | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| オイルバレル | `bbl` | `oilBarrels` | 0.158987294928 |
| 英ブッシェル | `bu (UK)` | `imperialBushels` | 0.03636872 |
| 英ホッグズヘッド | `hhd` | `hogsheads` | 0.32731785 |
| 英パイント | `pt (UK)` | `imperialPints` | 0.00056826125 |
| 英クォート | `qt (UK)` | `imperialQuarts` | 0.0011365225 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2(m³)
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance(8 m³)
cube into liters                                          // 8000.0

tank + cube         // 許可される: 両方とも体積 -> KVolumeUnitInstance
```

## <a name="pow"></a>`pow` によるべき乗

infix `pow` 演算子で値を整数乗します。Kotlin にはオーバーロード可能な `^` 演算子(および `^=`)がないため、
`pow` がグループ全体で唯一のべき乗構文です — `squareXxx`/`cubicXxx` トークンは存在しません。

`pow` は値をべき乗し、**さらに**すべての指数に `n` を掛けます。したがって `(2 of meters) pow 2` は
`(2 m)² = 4 m²` です(指数だけでなく値もべき乗されます)。距離グループでは結果が次元を持ちます: `pow 2`
は `KAreaUnitInstance`、`pow 3` は `KVolumeUnitInstance`、その他の指数は一般の `KDistanceUnitInstance`
になります。

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

`pow` は `* / + -` よりも**弱く**結合します。混合式では括弧を付けてください(`(a * b) pow 2`)。すべての単位
グループで使用できます — 例: `(2 of hours) pow 2`(時間には次元を持つべき乗型がないため、一般の
`KMixedUnitInstance` になります)。

## SI 接頭辞

任意の長さ単位は、24種類の SI 接頭辞**ビルダー**(`kilo`、`milli` など、ルートパッケージ)のいずれとも
プロパティアクセスで組み合わせることができ、`of`/`into` 用の値1テンプレートを生成します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// 構築: "5 of kilo.meters" -> KLengthUnitInstance(== 5000 m)
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// 接頭辞付きの単位で値を読み戻す
val d = 5 of miles
d into kilo.meters  // 8.04672(km)

// 接頭辞は名前付きの面積/体積トークンとも組み合わせ可能
val tank = 200 of liters
tank into milli.liters  // 200000.0(mL)
```

## toString フォーマット

基本単位の `toString()` のみが存在します。特定の単位は `into` を使ってフォーマットします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m"(基本単位表現)
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `m` | `meters` | 長さ、基本単位（メートル） |
| `km` | `kilo.meters` | 接頭辞付きの長さ（キロメートル） |
| `m²` | `meters pow 2` | 面積（メートルの2乗） |
| `m³` | `meters pow 3` | 体積（メートルの3乗） |
| `m⁻¹` | `meters pow -1` | 長さの逆数 |
| `2 m · 2 m` | `(2 of meters) * (2 of meters)` | 長さ×長さで作る面積 |
