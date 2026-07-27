# 熱力学 — 概要

パッケージ: `org.pcsoft.framework.kunit.temperature`、`…energy`、`…power`

熱力学は**熱と温度**の物理です。KUnit ではこの分野は温度を中心にしており、温度は**関連する 2 つの
ネイティブなグループ**でモデル化されます — なぜなら、温度の*読み値*と温度の*変化*は物理的に異なる種類の
量であり、それらを区別することが計算を正しくするからです。その周りには、あらゆる熱収支に登場する 2 つの
**構成された**量、すなわち熱そのもの(エネルギー)とその流れる速さ(電力)があります。

## この話題の単位

| 単位 | 種別 | 性質 | 基準単位 | ページ |
|---|---|---|---|---|
| 絶対温度 | ネイティブ | アフィンな**点** | ケルビン(`K`) | [絶対温度](temperature.md) |
| 温度差 | ネイティブ | 線形な**区間** | ケルビン(`ΔK`) | [温度差](temperature-difference.md) |
| エネルギー | 構成 | 線形量 | ジュール(`J`) | [エネルギー(熱力学)](energy.md) |
| 電力 | 構成 | 線形量 | ワット(`W`) | [電力(熱力学)](power.md) |

エネルギー(熱)と電力(熱流量)は技術的にはそれぞれ**単一の**量であり、他の分野とも共有されています。
それらは分野ごとに文書化され、互いに相互参照します([エネルギー(電気)](../electrical/energy.md)、
[エネルギー(力学)](../mechanics/energy.md)、[電力(電気)](../electrical/power.md)、
[電力(力学)](../mechanics/power.md))。

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

## 熱と熱流量の型付き演算子

| 式 | 結果 | 公式 |
|---|---|---|
| `power * time` | エネルギー(熱) | `Q = Φ · t` |
| `energy / time` | 電力(熱流量) | `Φ = Q / t` |
| `energy / power` | 時間 | `t = Q / Φ` |
| `power / frequency` | エネルギー | `Q = Φ / f` |

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

## 実例 — ボイラーの熱量と加熱時間

**2 kW** のボイラーを **10 分間**稼働させます。供給される熱量は `Q = Φ · t` です。これを熱流量で割り戻すと
加熱時間が得られます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8(kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
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
| `Q = Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | 熱流量×時間から熱量 |
| `Φ = Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | 熱量÷時間から熱流量 |

## 次に読むもの

* [温度概要](temperature-overview.md) — 点と区間の完全な議論と、それが物理的になぜ重要か
  (熱エネルギー、放射、理想気体の法則)。
* [絶対温度](temperature.md) — ケルビン、摂氏、華氏、ランキンとアフィン演算子。
* [温度差](temperature-difference.md) — 線形なケルビン区間のグループ。
* [エネルギー(熱力学)](energy.md) — 熱としてのジュール、およびカロリーと BTU。
* [電力(熱力学)](power.md) — 熱流量としてのワット、`Q / t`。
