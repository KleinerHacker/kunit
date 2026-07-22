# 抵抗

パッケージ: `org.pcsoft.framework.kunit.resistance`
基本単位: **オーム**(`KResistanceUnit.BASE == KResistanceUnit.OHM`)

種別: **構成された単位**

電気抵抗は**構成された**単位で、合成 `mass · length² · time⁻³ · current⁻²`(`kg·m²·s⁻³·A⁻²`)です。
`KResistanceUnitInstance` は4つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+2` の
`KDistanceUnit.BASE`(メートル)、指数 `-3` の `KTimeUnit.BASE`(秒)、指数 `-2` の
`KElectricCurrentUnit.BASE`(アンペア) — をラップします。ライブラリの質量成分は**グラム**(キログラムではない)
に正規化されるため、オームは生の成分基準の1000倍です。保存される値はオームに正規化されます。

## 抵抗の作成

抵抗は名前付きトークンから、または分解(下記参照)から作成します。名前付き単位は値1のトークンとして残ります
(`of`/`into` で使用):

| 抵抗 | 記号 | トークン | Ω 換算(1単位) |
|---|---|---:|---:|
| オーム | `Ω` | `ohms` | 1.0 |
| スタットオーム(CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| アブオーム(CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| 国際オーム | `Ω_int` | `internationalOhms` | 1.000049 |
| 法定オーム(1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| ジーメンス水銀単位 | `Ω_S` | `siemensUnits` | 0.9534 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`kilo.ohms`、`mega.ohms`、`milli.ohms` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## 複数の分解

抵抗はいくつかの**等価な分解**から得られ、いずれも同じ値の等しい抵抗を生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | オームの法則 `R = U / I` |
| `mass·length²/(time³·current²)` | `.toResistance()` 経由 | ネイティブな正規形 `kg·m²·s⁻³·A⁻²` の式 |

型付き演算子の形式は抵抗を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toResistance()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。
両方の経路は値が等しくなります。

逆のオームの法則の演算子は電圧・抵抗・電流を結び付けます:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I`(可換) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// 実例 - オームの法則: 負荷に 230 V が加わり 2 A が流れると、抵抗は 115 Ω です。
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance、115 Ω

// 同じ抵抗をネイティブな kg·m²·s⁻³·A⁻² の式で:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `Ω` | `ohms` | 抵抗、基本単位（名前付きトークン、オーム） |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | 質量·長さ² / (時間³·電流²) としての抵抗（分数形式） |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | 同じ抵抗を純粋な積で表現 |
| `kΩ` | `kilo.ohms` | 接頭辞付きの抵抗（キロオーム） |
