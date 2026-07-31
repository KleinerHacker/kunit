# 表面張力

パッケージ: `org.pcsoft.framework.kunit.mechanic.lineforce`
基本単位: **ニュートン毎メートル**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

種別: **構成された単位**

表面張力 `σ` は単位面積の新しい表面を作るのに必要なエネルギーであり、同等に接触線に沿って単位長さ あたりに作用する力です:
`1 J/m² = 1 N/m`。その次元は `質量・時間⁻²` です。

これはまさに、[剛性](stiffness.md)が共有する **単位長さあたりの力**の次元です。したがってKUnitは両方 の読み取り方に対して1つの中立的なグループ
`lineforce` をモデル化しており、表面張力はそのうちの1つ です。このページはその読み取り方を文書化しています。

!!! note "1つのグループ、2つの読み取り方"
`KLineForceUnitInstance` は共有される型です。表面張力とばね定数を区別するものは、付けた名前を 除けば何もありません —
このグループは中立的な名前を持ち、どちらの読み取り方も相手の名前を主張 しないようになっています。

## 名前付き単位

| 単位                     | 記号     |               トークン | N/mでの1単位 |
|--------------------------|----------|-----------------------:|-------------:|
| ニュートン毎メートル     | `N/m`    |      `newtonsPerMeter` |          1.0 |
| ダイン毎センチメートル   | `dyn/cm` |   `dynesPerCentimeter` |         1e-3 |
| ニュートン毎ミリメートル | `N/mm`   | `newtonsPerMillimeter` |       1000.0 |
| ポンド重毎インチ         | `lbf/in` |   `poundsForcePerInch` |    ≈ 175.127 |
| キロポンド毎メートル     | `kp/m`   |    `kilopondsPerMeter` |      9.80665 |

表面張力は通常mN/mまたは数値的に同一のdyn/cmで表記されます: 25°Cの水は約72 mN/m = 72 dyn/cmです。 ミリニュートン毎メートルは接頭辞付きの形式
`milli.newtonsPerMeter` です。

## 分解表現

| 形式              | Kotlin                                                  | 結果の型                 |
|-------------------|---------------------------------------------------------|--------------------------|
| エネルギー / 面積 | `energy / area`                                         | `KLineForceUnitInstance` |
| 力 / 長さ         | `force / length`                                        | `KLineForceUnitInstance` |
| ネイティブ表現    | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - どちらも2 N/m
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## 中核となる単位での計算

| 式                                         | 結果の型                 | 意味                       |
|--------------------------------------------|--------------------------|----------------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`                |
| `lineforce * area`, `area * lineforce`     | `KEnergyUnitInstance`    | 表面エネルギー `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`                |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`                |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | `F = σ · l`                |

## 実例: シャボン膜を作るエネルギー

面積0.05 m² (表面2枚、それぞれσ ≈ 25 mN/m)のシャボン膜を作ります。どれだけのエネルギーがかかり、 10
cmのワイヤーに膜が及ぼす力はいくつでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m"(基本単位)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                          | 意味                         |
|-------------|---------------------------------|------------------------------|
| `N/m`       | `newtonsPerMeter`               | 表面張力、基本単位           |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 同じ量を基本次元で           |
| `mN/m`      | `milli.newtonsPerMeter`         | 日常的な表面張力の読み取り方 |
| `dyn/cm`    | `dynesPerCentimeter`            | CGSの読み取り方(= 1 mN/m)    |
| `σ = W / A` | `energy / area`                 | 分解表現A                    |
| `σ = F / l` | `force / length`                | 分解表現B                    |
| `W = σ · A` | `lineforce * area`              | 表面エネルギー               |
