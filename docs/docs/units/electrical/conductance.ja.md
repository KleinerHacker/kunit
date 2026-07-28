# コンダクタンス

パッケージ: `org.pcsoft.framework.kunit.electric.conductance`
基本単位: **ジーメンス**(`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`)

種別: **構成された単位**

電気コンダクタンスは**構成された**単位で、合成 `mass⁻¹ · length⁻² · time³ · current²`(`kg⁻¹·m⁻²·s³·A²`)です。
`KConductanceUnitInstance` は4つの項 — 指数 `-1` の `KMassUnit.BASE`(グラム)、指数 `-2` の
`KDistanceUnit.BASE`(メートル)、指数 `+3` の `KTimeUnit.BASE`(秒)、指数 `+2` の
`KElectricCurrentUnit.BASE`(アンペア) — をラップします。ライブラリの質量成分は**グラム**(キログラムではない)
に正規化され、質量の指数が負であるため、ジーメンスは生の成分基準の 1/1000 倍です。保存される値はジーメンスに
正規化されます。

コンダクタンスは[抵抗](resistance.md)の逆数(`G = 1 / R`)であり、オームの法則を通じて
[電圧](voltage.md)と[電流](ec.md)を結び付けます。

## コンダクタンスの作成

コンダクタンスは名前付きトークンから、または分解(下記参照)から作成します。名前付き単位は値1のトークンとして
残ります(`of`/`into` で使用):

| コンダクタンス | 記号 | トークン | S 換算(1単位) |
|---|---|---:|---:|
| ジーメンス | `S` | `siemens` | 1.0 |
| モー(伝統的な名称) | `℧` | `mhos` | 1.0 |
| アブモー(CGS-EMU) | `ab℧` | `abmhos` | 1.0e9 |
| スタットモー(CGS-ESU) | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` と `siemensUnits`"
    `siemens`(本パッケージ)は**コンダクタンス**の SI 単位です。名前の似た
    `org.pcsoft.framework.kunit.electric.resistance` の `siemensUnits` は歴史的な**ジーメンス水銀単位**で、
    0.9534 Ω の*抵抗*です。両者は別パッケージの無関係な量です。

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`milli.siemens`、`micro.siemens`、
`kilo.siemens` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## 複数の分解

コンダクタンスはいくつかの**等価な分解**から得られ、いずれも同じ値の等しいコンダクタンスを生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `current / voltage` | `KConductanceUnitInstance` | オームの法則 `G = I / U` |
| `1 / resistance` | `KConductanceUnitInstance` | 抵抗の逆数 `G = 1 / R` |
| `time³·current²/(mass·length²)` | `.toConductance()` 経由 | ネイティブな正規形 `kg⁻¹·m⁻²·s³·A²` の式 |

型付き演算子の形式はコンダクタンスを直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toConductance()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。
すべての経路は値が等しくなります。

逆の演算子はコンダクタンス・電圧・電流を結び付けます:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U`(可換) |
| `current / conductance` | `KVoltageUnitInstance` | `U = I / G` |
| `1 / conductance` | `KResistanceUnitInstance` | `R = 1 / G` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// 実例 - 給電ケーブルのコンダクタンス: 2 A が流れ、電圧降下が 1 V のケーブルの
// コンダクタンスは 2 S です(つまり抵抗は 0.5 Ω)。
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance、2 S
val r = 1 / g                            // KResistanceUnitInstance、0.5 Ω

// 抵抗との逆数関係:
1 / (1 of ohms) == (1 of siemens)        // true

// 同じコンダクタンスをネイティブな kg⁻¹·m⁻²·s³·A² の式で:
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `S` | `siemens` | コンダクタンス、基本単位（名前付きトークン、ジーメンス） |
| `s³·A²/(kg·m²)` | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | 時間³·電流² / (質量·長さ²) としてのコンダクタンス（分数形式） |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | 同じコンダクタンスを純粋な積で表現 |
| `mS` | `milli.siemens` | 接頭辞付きのコンダクタンス（ミリジーメンス） |
