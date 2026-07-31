# 静電容量

パッケージ: `org.pcsoft.framework.kunit.electric.capacitance`
基本単位: **ファラド**(`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

種別: **構成された単位**

静電容量は **構成された**単位で、合成 `mass⁻¹ · length⁻² · time⁴ · current²`(`kg⁻¹·m⁻²·s⁴·A²`)です。
`KCapacitanceUnitInstance` は4つの項 — 指数 `-1` の `KMassUnit.BASE`(グラム)、指数 `-2` の
`KDistanceUnit.BASE`(メートル)、指数 `+4` の `KTimeUnit.BASE`(秒)、指数 `+2` の
`KElectricCurrentUnit.BASE`(アンペア) — をラップします。ライブラリの質量成分は **グラム**(キログラムではない)
に正規化され、質量の指数が *負*であるため、ファラドは生の成分基準に対して逆方向に1000倍です。 保存される値はファラドに正規化されます。

## 静電容量の作成

静電容量は名前付きトークンから、または分解 (下記参照)から作成します。名前付き単位は値1のトークンとして残ります (`of`/
`into` で使用):

| 静電容量                  | 記号    |     トークン |   F 換算(1単位) |
|---------------------------|---------|-------------:|----------------:|
| ファラド                  | `F`     |     `farads` |             1.0 |
| アブファラド(CGS-EMU)     | `abF`   |   `abfarads` |           1.0e9 |
| スタットファラド(CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| ジャー(ライデン瓶)        | `jar`   |       `jars` |      1.11265e-9 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します (`micro.farads`、`nano.farads`、`pico.farads` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## 複数の分解

静電容量はいくつかの **等価な分解**から得られ、いずれも同じ値の等しい静電容量を生成します:

| 式                              | 結果の型                   | 意味                                     |
|---------------------------------|----------------------------|------------------------------------------|
| `charge / voltage`              | `KCapacitanceUnitInstance` | 定義 `C = Q / U`                         |
| `current²·time⁴/(mass·length²)` | `.toCapacitance()` 経由    | ネイティブな正規形 `kg⁻¹·m⁻²·s⁴·A²` の式 |

型付き演算子の形式は静電容量を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toCapacitance()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。 両方の経路は値が等しくなります。

逆の演算子は電荷・電圧・静電容量を結び付けます:

| 式                      | 結果の型               | 意味              |
|-------------------------|------------------------|-------------------|
| `capacitance * voltage` | `KChargeUnitInstance`  | `Q = C · U`(可換) |
| `charge / capacitance`  | `KVoltageUnitInstance` | `U = Q / C`       |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.capacitance.*

// 実例 - 充電されたコンデンサ: 470 µF を 12 V まで充電すると 5.64 mC を蓄えます。
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance、0.00564 C

// 静電容量について解いた定義式:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance、2 F

// 同じ静電容量をネイティブな kg⁻¹·m⁻²·s⁴·A² の式で:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.capacitance.*

(470 of farads).toString()     // "470.0 F"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`⁴`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学             | Kotlin                                                                      | 意味                                                    |
|------------------|-----------------------------------------------------------------------------|---------------------------------------------------------|
| `F`              | `farads`                                                                    | 静電容量、基本単位（名前付きトークン、ファラド）        |
| `A²·s⁴/(kg·m²)`  | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))`         | 電流²·時間⁴ / (質量·長さ²) としての静電容量（分数形式） |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | 同じ静電容量を純粋な積で表現                            |
| `µF`             | `micro.farads`                                                              | 接頭辞付きの静電容量（マイクロファラド）                |
