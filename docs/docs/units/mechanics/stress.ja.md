# 力学的応力と弾性係数

パッケージ: `org.pcsoft.framework.kunit.mechanic.pressure`
基本単位: **パスカル**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

種別: **構成された単位**

力学的応力 `σ = F / A` および弾性 (ヤング)係数 `E = σ / ε` は、まさに[圧力](pressure.md)の次元、
`質量・長さ⁻¹・時間⁻²` を持ちます。したがってKUnitはこれらのために単位グループを導入 **しません**:
両方とも圧力グループの **読み取り方**であり、その接頭辞エイリアスを通じて表現されます。このページは
その読み取り方を文書化しています。グループ自体については[圧力](pressure.md)のページで説明されて います。

!!! note "MPa、N/mm²、GPaは接頭辞エイリアスである"
静力学の単位は専用のトークンでは **ありません**。なぜなら正確に到達できるからです:
**MPa = N/mm² = `mega.pascals`**、 **GPa = `giga.pascals`**。`(1 of newtons) / ((1 of milli.meters) *
    (1 of milli.meters))` は `1 of mega.pascals` とまったく同じ値を生みます。

## 読み取り方の表

| 読み取り方           | 記号   | Kotlin         | Paでの1単位 |
|----------------------|--------|----------------|------------:|
| パスカル             | `Pa`   | `pascals`      |         1.0 |
| キロパスカル         | `kPa`  | `kilo.pascals` |         1e3 |
| メガパスカル = N/mm² | `MPa`  | `mega.pascals` |         1e6 |
| ギガパスカル(係数)   | `GPa`  | `giga.pascals` |         1e9 |
| 単位面積あたりの力   | `N/m²` | `force / area` |         1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0(N/mm²はメガパスカルである)
```

## フックの法則

[ひずみ](strain.md)グループと合わせて、圧力グループはフックの法則の両辺を保持します:

| 式                                       | 結果の型                | 意味                   |
|------------------------------------------|-------------------------|------------------------|
| `force / area`                           | `KPressureUnitInstance` | 応力 `σ = F / A`       |
| `stress / strain`                        | `KPressureUnitInstance` | 弾性係数 `E = σ / ε`   |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | 応力 `σ = E · ε`       |
| `pressure * area`                        | `KForceUnitInstance`    | 作用する力 `F = σ · A` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0(鋼)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## 実例: 荷重を受けるタイロッド

直径20 mm (A ≈ 314 mm²)の鋼製タイロッドが60 kNを支えています。応力はいくつで、S235鋼の降伏強度 235 MPaを下回っているでしょうか。また3
mのロッドはどれだけ伸びるでしょうか (E = 210 GPa)?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - 降伏強度以内

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E を無次元比として
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa"(グループの基本単位)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                                            | 意味                                     |
|--------------|---------------------------------------------------|------------------------------------------|
| `MPa`        | `mega.pascals`                                    | 応力の読み取り方(= N/mm²)                |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | 単位面積あたりの力としての同じ読み取り方 |
| `GPa`        | `giga.pascals`                                    | 弾性係数の読み取り方                     |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | 同じ量を基本次元で                       |
| `σ = F / A`  | `force / area`                                    | 力と面積からの応力                       |
| `E = σ / ε`  | `stress / strain`                                 | フックの法則を係数について解いた形       |
| `σ = E · ε`  | `pressure * strain`                               | フックの法則を応力について解いた形       |
