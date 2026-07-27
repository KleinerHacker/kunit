# 電荷

パッケージ: `org.pcsoft.framework.kunit.charge`
基本単位: **クーロン**(`KChargeUnit.BASE == KChargeUnit.COULOMB`)

種別: **構成された単位**

電荷は**構成された**単位で、合成 `current · time`(`A·s`)です。`KChargeUnitInstance` は2つの項 —
指数 `+1` の `KElectricCurrentUnit.BASE`(アンペア)、指数 `+1` の `KTimeUnit.BASE`(秒) — をラップします。
どの名前付き単位、SI 接頭辞、電流·時間の組み合わせから作っても、保存される値は常にクーロンに正規化されます。

## 電荷の作成

電荷は名前付きトークンから、または分解(下記参照)から作成します。名前付き単位は値1のトークンとして残ります
(`of`/`into` で使用):

| 電荷 | 記号 | トークン | C 換算(1単位) |
|---|---|---:|---:|
| クーロン | `C` | `coulombs` | 1.0 |
| アンペア秒 | `As` | `ampereSeconds` | 1.0 |
| アンペア時 | `Ah` | `ampereHours` | 3600.0 |
| アブクーロン(CGS-EMU) | `abC` | `abcoulombs` | 10.0 |
| スタットクーロン(CGS-ESU) | `statC` | `statcoulombs` | 3.335641e-10 |
| ファラデー | `F_c` | `faradays` | 96485.332 |
| 電気素量 | `e` | `elementaryCharges` | 1.602176634e-19 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`kilo.coulombs`、`milli.coulombs`、
`milli.ampereHours` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## 複数の分解

電荷はいくつかの**等価な分解**から得られ、いずれも同じ値の等しい電荷を生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `current * time` | `KChargeUnitInstance` | 定義 `Q = I · t` |
| `time * current` | `KChargeUnitInstance` | `Q = I · t` の可換形式 |
| `current / frequency` | `KChargeUnitInstance` | 逆時間形式 `Q = I / f`(`1/Hz = s`) |
| `current·time` | `.toCharge()` 経由 | ネイティブな正規形 `A·s` の式 |

型付き演算子の形式は電荷を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toCharge()`(正規形 — 指数 `+1` の `KElectricCurrentUnit` 項1つと指数 `+1` の `KTimeUnit` 項1つ — のみを
認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。すべての経路は値が等しくなります。

逆の演算子は電荷·電流·時間を結び付けます:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `charge / time` | `KElectricCurrentUnitInstance` | `I = Q / t` |
| `charge / current` | `KTimeUnitInstance` | `t = Q / I` |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f`(逆時間形式) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.charge.*

// 実例 - バッテリー容量: 2000 mAh のセルは 7200 C を蓄えます。
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance、7200 C

// 250 mA を一定に流すと何秒もつか?
battery / (0.25 of amperes)               // KTimeUnitInstance、28800 s(8 時間)

// 同じ電荷を型付き分解とネイティブな A·s の式で:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance、7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.charge.*

(470 of coulombs).toString()   // "470.0 C"(基本単位)
(1 of ampereHours).toString()  // "3600.0 C"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `C` | `coulombs` | 電荷、基本単位（名前付きトークン、クーロン） |
| `A·s` | `amperes * seconds` | 電流·時間としての電荷（積の形式） |
| `A/Hz` | `amperes / hertz` | 電流を周波数で割った同じ電荷（`1/Hz = s`) |
| `mAh` | `milli.ampereHours` | 接頭辞付きの電荷（ミリアンペア時、バッテリー容量） |

## 関連項目

- [電流](ec.md) — 電荷の合成における電流成分
- [電圧](voltage.md) — 電位差
- [抵抗](resistance.md) — オームの法則が電気グループを完成させます
