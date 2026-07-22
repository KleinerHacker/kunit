# 電圧

パッケージ: `org.pcsoft.framework.kunit.voltage`
基本単位: **ボルト**(`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

電圧(電位差)は**構成された**単位で、合成 `mass · length² · time⁻³ · current⁻¹`(`kg·m²·s⁻³·A⁻¹`)です。
`KVoltageUnitInstance` は4つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+2` の
`KDistanceUnit.BASE`(メートル)、指数 `-3` の `KTimeUnit.BASE`(秒)、指数 `-1` の
`KElectricCurrentUnit.BASE`(アンペア) — をラップします。ライブラリの質量成分は**グラム**(キログラムではない)
に正規化されるため、ボルトは生の成分基準の1000倍です。保存される値はボルトに正規化されます。

## 電圧の作成

電圧は名前付きトークンから、または分解(下記参照)から作成します。名前付き単位は値1のトークンとして残ります
(`of`/`into` で使用):

| 電圧 | 記号 | トークン | V 換算(1単位) |
|---|---|---:|---:|
| ボルト | `V` | `volts` | 1.0 |
| スタットボルト(CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| アブボルト(CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| ウェストン電池 | `V_W` | `westonCells` | 1.0183 |
| ダニエル電池 | `V_Da` | `daniells` | 1.1 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`kilo.volts`、`mega.volts`、`milli.volts` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## 複数の分解

電圧はいくつかの**等価な分解**から得られ、いずれも同じ値の等しい電圧を生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | オームの法則 `U = R · I`(抵抗を参照) |
| `current * resistance` | `KVoltageUnitInstance` | オームの法則(可換) |
| `mass·length²/(time³·current)` | `.toVoltage()` 経由 | ネイティブな正規形 `kg·m²·s⁻³·A⁻¹` の式 |

型付き演算子の形式は電圧を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toVoltage()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。
両方の経路は値が等しくなります。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// 実例 - オームの法則: 2 A を流す 115 Ω の抵抗は 230 V の電圧降下を生じます。
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance、230 V

// 同じ電圧をネイティブな kg·m²·s⁻³·A⁻¹ の式で:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `V` | `volts` | 電圧、基本単位（名前付きトークン、ボルト） |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | 質量·長さ² / (時間³·電流) としての電圧（分数形式） |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | 同じ電圧を純粋な積で表現 |
| `kV` | `kilo.volts` | 接頭辞付きの電圧（キロボルト） |
