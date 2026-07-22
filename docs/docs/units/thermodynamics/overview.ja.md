# 熱力学 — 概要

パッケージ: `org.pcsoft.framework.kunit.temperature`

熱力学は**熱と温度**の物理です。KUnit ではこの分野は現在、温度を中心にしており、温度は**関連する 2 つの
ネイティブなグループ**でモデル化されます — なぜなら、温度の*読み値*と温度の*変化*は物理的に異なる種類の
量であり、それらを区別することが計算を正しくするからです。

## この話題の単位

| 単位 | 種別 | 性質 | 基準単位 | ページ |
|---|---|---|---|---|
| 絶対温度 | ネイティブ | アフィンな**点** | ケルビン(`K`) | [絶対温度](temperature.md) |
| 温度差 | ネイティブ | 線形な**区間** | ケルビン(`ΔK`) | [温度差](temperature-difference.md) |

専用の[温度概要](temperature-overview.md)が点と区間の区別を詳しく説明します。このページは熱力学分野全体の
入口です。

## 点 vs 区間 — 演算子の規則

| 演算 | 結果 |
|---|---|
| `絶対温度 − 絶対温度` | **温度差** |
| `絶対温度 + 差` | 絶対温度 |
| `絶対温度 − 差` | 絶対温度 |
| `差 ± 差` | 温度差 |
| `絶対温度 + 絶対温度` | **コンパイルエラー**(物理的に無意味) |

## 実例 — 加熱の 1 ステップ

水を **10 °C** から **30 °C** へ加熱します。その*変化*は温度**差**(`ΔT`)であり、これは `Q = m · c · ΔT`
のような熱の公式に入る量です。ゼロ点が打ち消されるため、`°C` と `K` はステップの大きさで一致します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0(ケルビン区間)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(ケルビン)で出力します。絶対温度は `K`、差は区別された
`ΔK` 記号で表示されます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()                       // "298.15 K"(絶対、基準単位)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK"(区間)
```

## 記法

下表は温度の関係を数学表記と KUnit の Kotlin 表記で対比します。`Δ` は区間量を表し、絶対的な点とは意図的に
区別されます。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | 2 つの絶対温度からの差 |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | 区間で移動した絶対温度 |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 明示的な温度区間 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 2 つの区間の和 |

## 次に読むもの

* [温度概要](temperature-overview.md) — 点と区間の完全な議論と、それが物理的になぜ重要か
  (熱エネルギー、放射、理想気体の法則)。
* [絶対温度](temperature.md) — ケルビン、摂氏、華氏、ランキンとアフィン演算子。
* [温度差](temperature-difference.md) — 線形なケルビン区間のグループ。
