# 速度

パッケージ: `org.pcsoft.framework.kunit.speed`
基本単位: **メートル毎秒** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

速度は最初の**構成された(constructed)**単位です。長さや時間とは異なり、単一の「実在する」物理量では
なく、`長さ · 時間⁻¹` (`m/s`) の組み合わせです。そのため `KSpeedUnitInstance` は、ちょうど 2 つの項 -
指数 `+1` の `KLengthUnit.BASE`(メートル)と指数 `-1` の `KTimeUnit.BASE`(秒)- からなる
`KMixedUnitInstance` をラップします。値は、どの単位・接頭辞・長さ/時間の組み合わせで生成されても、常に
メートル毎秒に正規化されて保存されます。

## 単位

| 単位 | Enum 値 | 記号 | 生成関数 | 1 単位 (m/s) |
|---|---|---|---:|---:|
| メートル毎秒 | `KSpeedUnit.METERS_PER_SECOND` | `m/s` | `Number.metersPerSecond` | 1.0 |
| キロメートル毎時 | `KSpeedUnit.KILOMETERS_PER_HOUR` | `km/h` | `Number.kilometersPerHour` | 0.277778 (1000/3600) |
| マイル毎時 | `KSpeedUnit.MILES_PER_HOUR` | `mph` | `Number.milesPerHour` | 0.44704 (1609.344/3600) |
| ノット | `KSpeedUnit.KNOT` | `kn` | `Number.knots` | 0.514444 (1852/3600) |
| フィート毎秒 | `KSpeedUnit.FEET_PER_SECOND` | `ft/s` | `Number.feetPerSecond` | 0.3048 |
| マッハ (ISA 海面) | `KSpeedUnit.MACH` | `Ma` | `Number.mach` | 340.29 |
| 光速 | `KSpeedUnit.LIGHT_SPEED` | `c` | `Number.speedOfLight` | 299792458.0 |

上記のすべての単位には、`valueAs`/`toString` のターゲットや接頭辞 infix 関数の `unit` 引数として使える
bare な `val` エイリアスがあります: `metersPerSecond`, `kilometersPerHour`, `milesPerHour`, `knots`,
`feetPerSecond`, `mach`, `speedOfLight`。

> **マッハ**は国際標準大気の海面(15 °C)における音速です。物理定数ではなく便利な基準点であり、実際の
> 音速は温度と高度によって変化します。

```kotlin
import org.pcsoft.framework.kunit.speed.*

val v = 50.kilometersPerHour
v.value                                    // 13.888... (m/s に正規化)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)  // 50.0 (km/h で読み戻し)
v.valueAs(milesPerHour)                     // ≈ 31.07
v.valueAs(knots)                            // ≈ 26.998
v.valueAs(mach)                             // ≈ 0.0408 (音速に対する割合)
```

## 核心単位(長さ & 時間)で計算する

これこそが構成された単位の要点であり、直感的で**ない**部分です - この節を注意深く読んでください。

**メンタルモデル:** 速度とは長さを時間で割ったものです。KUnit では、通常の `*` と `/` で 3 つの物理量 -
長さ・時間・速度 - の間を行き来でき、各結果は**強く型付け**されています。生の
`KMixedUnitInstance` を自分で組み立てたり取り出したりする必要は一切ありません。

4 つの有効な組み合わせとその結果型:

| 式 | 結果型 | 意味 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 速度 = 距離 / 時間 |
| `speed * time` | `KLengthUnitInstance` | 距離 = 速度 × 時間 |
| `time * speed` | `KLengthUnitInstance` | 距離(可換) |
| `length / speed` | `KTimeUnitInstance` | 時間 = 距離 / 速度 |

```kotlin
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 核心単位 -> 速度 ---------------------------------------------------
val v = 100.meters / 10.seconds          // KSpeedUnitInstance (.toKSpeedUnit() 不要!)
v.value                                     // 10.0 (m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)   // 36.0
v.valueAs(KSpeedUnit.MILES_PER_HOUR)        // ≈ 22.37
v.valueAs(KSpeedUnit.KNOT)                  // ≈ 19.44
v.valueAs(KSpeedUnit.MACH)                  // ≈ 0.0294
v.valueAs(KSpeedUnit.LIGHT_SPEED)           // ≈ 3.336e-8

// 代入先の型は何も変換しません - 演算子がすでに
// KSpeedUnitInstance を返します。Kotlin に暗黙の変換はありません。
val explicit: KSpeedUnitInstance = 100.meters / 10.seconds

// --- 速度 -> 長さ (時間を掛ける) ----------------------------------------
val distance = v * 60.seconds             // KLengthUnitInstance
distance.value                              // 600.0 (m)
distance.valueAs(KLengthUnit.METER)         // 600.0
distance.valueAs(feet)                      // ≈ 1968.5 (任意の長さ単位で読み戻し)
distance.valueAs(miles)                     // ≈ 0.373
60.seconds * v                            // 同じ結果(可換)

// --- 速度 -> 時間 (長さを割る) ------------------------------------------
val time = 600.meters / v                 // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
time.valueAs(KTimeUnit.HOUR)                // ≈ 0.0167
```

!!! warning "**純粋な**長さだけが速度に割れます"
    `length / time` と `length / speed` は、長さの指数が 1 である必要があります。**面積**(`m²`、例:
    `2.hectares`)や**体積**(`m³`)は長さではないため、`area / time` は速度ではなく `m²/s` になります -
    演算子は誤った値を黙って返す代わりに `IllegalStateException` をスローします。同様に、
    `length * time`(`m·s`、速度ではない)や `length + speed`(次元が異なる)も有効な速度の構成では
    ありません。

### 速度でない中間結果(例: m²/s)を意図的に計算する

Kotlin の演算子はコンパイル時に単一の戻り値型を持つため、`KLengthUnitInstance / KTimeUnitInstance` は
型付きの速度を構築するために**予約**されており、代わりに `m²/s` を生むことはできません。ただしその中間
結果が**失われるわけではありません** - 一方のオペランドを `toKMixedUnitInstance()` で混合レベルに落とすと、
汎用の `KMixedUnitInstance` の `/` 演算子(任意の指数、速度チェックなし)が選ばれます。この明示的な
`toKMixedUnitInstance()` こそ、強く型付けされた経路から離れるという意図的な合図です。

```kotlin
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.time.*

val area = 2.hectares                 // KLengthUnitInstance、指数 2 (20 000 m²)

// area / 2.seconds                   // ❌ IllegalStateException をスロー(m²/s であって速度でない)

// ✅ 意図的な m²/s の中間結果: 一方のオペランドを混合レベルへ
val areaPerTime = area.toKMixedUnitInstance() / 2.seconds.toKMixedUnitInstance()
areaPerTime.value                       // 10000.0
areaPerTime.units                       // [METER^2, SECOND^-1]

// ...そして任意の KMixedUnitInstance と同様に連鎖します
val backToArea = areaPerTime * 4.seconds.toKMixedUnitInstance() // units=[METER^2], value=40000.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.speed.*

// + / - : 同グループ、異なる速度単位間の自動変換
val a = 36.kilometersPerHour + 10.metersPerSecond  // KSpeedUnitInstance, 20 m/s
val b = 20.metersPerSecond - 36.kilometersPerHour  // 10 m/s

// 比較(正規化された m/s 値による)
50.kilometersPerHour > 10.metersPerSecond   // true  (13.89 m/s > 10 m/s)
36.kilometersPerHour == 10.metersPerSecond  // true  (同じ正規化値)

// 2 つの速度間の * / / は KMixedUnitInstance へ「エスケープ」します(もはや純粋な速度ではない)
val squared = 10.metersPerSecond * 2.metersPerSecond // KMixedUnitInstance, units=[m^2, s^-2]
```

## 比較と等価性

`==`, `!=`, `<`, `<=`, `>`, `>=` は 2 つの `KSpeedUnitInstance` の正規化された `value`(メートル毎秒)を
比較します。速度は常に同じ次元を持つため、指数チェックは不要です(面積と長さを比較できない長さとは
異なります)。

## SI 接頭辞

任意の `KSpeedUnit` は、24 個の SI 接頭辞(`KUnitPrefix`、Quetta/Q から Quecto/q)と組み合わせられます。
速度グループの `infix` 構築関数(直接 `KSpeedUnitInstance` を返す)と `with`(`valueAs`/`toString` の
ターゲット用)を使います:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.speed.*

// 構築: "5 kilo metersPerSecond" -> KSpeedUnitInstance (直接、== 5000.metersPerSecond)
val fast = 5 kilo metersPerSecond
fast.value // 5000.0

// 接頭辞ターゲットで値を読み戻す
val v = 5.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KSpeedUnit.METERS_PER_SECOND)  // 0.005
```

速度を明示的な**長さ/時間のペア**(2 つのターゲット)として読み戻すこともできます。これが「km/h」を
長さと時間の部分から表現する方法です:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.speed.*

val v = 10.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR)   // 36.0 (km per h)
v.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR)  // "36.0 km*h^-1"
```

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.speed.*

10.metersPerSecond.toString()                            // "10.0 m/s" (基本単位)
(100.meters / 10.seconds).toString(KSpeedUnit.KILOMETERS_PER_HOUR) // "36.0 km/h"
1.mach.toString(KSpeedUnit.MACH)                          // "1.0 Ma"
```
