# 力学 — 概要

パッケージ: `org.pcsoft.framework.kunit.mass`、`…force`、`…pressure`、`…density`、`…areadensity`

力学(動力学)は、物体が**なぜ**動くのか、そして物質がどう分布するのかを問います — 質量、それに働く力、
力が面積に及ぼす圧力、そして体積や表面にどれだけの質量が詰まっているか、の相互作用です。
[運動学](../kinematics/overview.md)の割合の上に、この話題は 1 つの**ネイティブ**基本量(質量)と、
質量・長さ・時間から**構成された** 4 つの量を加えます。

## この話題の単位

| 単位 | 種別 | 基準単位 | ページ |
|---|---|---|---|
| 質量 | ネイティブ | グラム(`g`) | [質量](mass.md) |
| 力 | 構成 | ニュートン(`N`) | [力](force.md) |
| 圧力 | 構成 | パスカル(`Pa`) | [圧力](pressure.md) |
| 密度 | 構成 | キログラム毎立方メートル(`kg/m³`) | [密度](density.md) |
| 面密度 | 構成 | キログラム毎平方メートル(`kg/m²`) | [面密度](areadensity.md) |

## 量どうしの関係

| 式 | 結果 | 公式 |
|---|---|---|
| `mass * acceleration` | 力 | `F = m · a` |
| `force / area` | 圧力 | `p = F / A` |
| `pressure * area` | 力 | `F = p · A` |
| `mass / volume` | 密度 | `ρ = m / V` |
| `density * length` | 面密度 | `ρ_A = ρ · d` |

## 実例 — ニュートンの運動第 2 法則と接地圧

**2 kg** の物体を標準重力で加速し、その結果生じる重力を **0.5 m²** の設置面に分散させます。力は
`F = m · a`、圧力は `p = F / A` です:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*
import org.pcsoft.framework.kunit.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61(N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance、0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23(Pa)
```

## 実例 — 密度から鋼部品の質量

鋼の密度は **7850 kg/m³** です。**2 L** の部品の質量は `m = ρ · V` です:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance、7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7(2 L あたりの kg)
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(値 + 記号)で出力します。他の単位には `into` を文字列
テンプレート内で使い、記号を自分で付け足します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N"(基準単位)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## 記法

下表は、この分野の中核的な関係を数学表記と KUnit の Kotlin 表記で対比します。指数は Unicode 上付き文字
(`²`、`³`、`⁻¹`)、`·` は乗算、`/` は分数を表します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)` | 質量×加速度から力 |
| `p = F / A` | `f / area` | 力÷面積から圧力 |
| `F = p · A` | `p * area` | 圧力×面積から力 |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)` | 質量÷体積から密度 |
| `m = ρ · V` | `steel * (2 of liters)` | 密度×体積から質量 |

## 次に読むもの

* [質量](mass.md) — ネイティブな基本量(グラム正規化)。
* [力](force.md) と [圧力](pressure.md) — ニュートンの法則と、面積あたりの力。
* [密度](density.md) と [面密度](areadensity.md) — 体積あたり・表面あたりの質量。
