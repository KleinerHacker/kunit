# 速度

パッケージ: `org.pcsoft.framework.kunit.speed`
基本単位: **メートル毎秒**(`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

種別: **構成された単位**

速度は最初の**構成された**単位です: 長さや時間とは異なり、単一の「実在する」量ではなく、`length · time⁻¹`
(`m/s`)という合成です。したがって `KSpeedUnitInstance` は、ちょうど2つの項 — 指数 `+1` の `KDistanceUnit.BASE`
(メートル)と指数 `-1` の `KTimeUnit.BASE`(秒) — からなる `KMixedUnitInstance` をラップします。値は、どの
単位や長さ/時間の組み合わせから作成されたかに関係なく、常にメートル毎秒に正規化されて保存されます。

## 速度の作成

速度は**長さ毎時間の式**として作成します。例: `10 of kilo.meters / hours` や
`100 of meters / (10 of seconds)` — どちらも `KSpeedUnitInstance` を生成します。任意の長さ毎時間テンプレートで
読み戻します(`v into (kilo.meters / hours)`)。`metersPerSecond` や `kilometersPerHour` のような綴られた複合
トークンは意図的に**ありません**(それらはまさに `meters / seconds` / `kilo.meters / hours` です)。

真に単一の慣用的な名前を持つ速度のみが値1のトークンとして残ります(`of`/`into` で使用):

| 速度 | 記号 | トークン | m/s 換算(1単位) |
|---|---|---:|---:|
| ノット | `kn` | `knots` | 0.514444(1852/3600) |
| マッハ(ISA 海面) | `Ma` | `mach` | 340.29 |
| 光速 | `c` | `speedOfLight` | 299792458.0 |

> **マッハ**は、海面(15 °C)の国際標準大気における音速です。これは便利な基準点であり、物理定数ではありません
> — 実際の音速は温度と高度によって変化します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888...(m/s に正規化)
v into (kilo.meters / hours)   // 50.0(km/h に戻して読み取り)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408(音速に対する割合)
```

## 中核単位(長さと時間)での計算

これが構成された単位の眼目です。速度*とは*、長さを時間で割ったものです。KUnit では、3つの量 — 長さ、時間、
速度 — の間を単純な `*` と `/` で行き来でき、各結果は**強く型付け**されます。生の `KMixedUnitInstance` を自分で
組み立てたり展開したりする必要は決してありません。

| 式 | 結果の型 | 意味 |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | 速度 = 距離 / 時間 |
| `speed * time` | `KLengthUnitInstance` | 距離 = 速度 × 時間 |
| `time * speed` | `KLengthUnitInstance` | 距離(可換) |
| `length / speed` | `KTimeUnitInstance` | 時間 = 距離 / 速度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- 中核単位 -> 速度 ------------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance(.toSpeed() は不要!)
v.value                    // 10.0(m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// コンパクトなレートのために長さに接頭辞を付ける(括弧不要、`of` は `/` より弱く結合):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- 速度 -> 長さ(時間を掛ける) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // 同じ結果(可換)

// --- 速度 -> 時間(長さをそれで割る) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "速度に割れるのは*純粋な*長さのみ"
    `length / time` と `length / speed` は、長さが指数1であることを要求します。**面積**(`m²`)や**体積**(`m³`)
    は長さではないため、`area / time` は速度ではなく `m²/s` になります — 演算子は誤った値をこっそり返す代わりに
    `IllegalStateException` をスローします。そのような中間結果を意図的に作るには、`toUnit()` で一方のオペランドを
    混合レベルに落とします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance, [METER^2, SECOND^-1]
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : 同じグループ内、異なる速度の式の間の自動変換
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance、20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// 比較(正規化された m/s 値による)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// 2つの速度の間の * / / は KMixedUnitInstance に脱出する(もはや純粋な速度ではない)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance, [m^2, s^-2]
```

## toString フォーマット

基本単位の `toString()` のみが存在します。特定の単位は `into` を使ってフォーマットします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s"(基本単位)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `m/s` | `meters / seconds` | 速度、基本単位（毎秒メートル）— 分数形式 |
| `m·s⁻¹` | `meters * (seconds pow -1)` | 同じ速度を負の指数の積で表現 |
| `km/h` | `kilo.meters / hours` | 毎時キロメートル |
| `mi/h` | `miles / hours` | 毎時マイル |
| `100 m / 10 s` | `(100 of meters) / (10 of seconds)` | 長さ ÷ 時間 で構築 |
