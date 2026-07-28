# 透磁率

パッケージ: `org.pcsoft.framework.kunit.electric.permeability`
基本単位: **メートルあたりのヘンリー** (`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`)

種別: **構成単位（constructed unit）**

透磁率は**構成単位**です。組成は `質量 · 長さ · 時間⁻² · 電流⁻²`
（`kg·m·s⁻²·A⁻²` = `H/m`）です。`KPermeabilityUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `+1`、`KDistanceUnit.BASE`（メートル）を `+1`、`KTimeUnit.BASE`（秒）を `-2`、
`KElectricCurrentUnit.BASE`（アンペア）を `-2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってメートルあたりのヘンリーに換算されます。
格納される値は常にメートルあたりのヘンリーに正規化されています。

透磁率 `μ` は材料の磁気定数であり、[磁束密度](magneticfluxdensity.md)を
[磁界強度](magneticfieldstrength.md)と関係付け（`μ = B / H`）、
[インダクタンス](inductance.md)とコイル形状を関係付けます。電気的な対応物は[誘電率](permittivity.md)です。

## 透磁率を組み立てる

名前付きトークンで、または分解表現（下記参照）から透磁率を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 透磁率 | 記号 | トークン | H/mでの1単位 |
|---|---|---:|---:|
| メートルあたりのヘンリー | `H/m` | `henriesPerMeter` | 1.0 |
| センチメートルあたりのヘンリー | `H/cm` | `henriesPerCentimeter` | 100.0 |
| 真空の透磁率 `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`micro.henriesPerMeter`、`milli.henriesPerMeter`
など）。この定数は `KPermeabilityUnit.VACUUM_PERMEABILITY` としても利用できます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## 複数の分解表現

透磁率は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい透磁率を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`、幾何係数は長さとなる |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | `.toPermeability()` 経由 | ネイティブの正準 `kg·m·s⁻²·A⁻²` 表現 |

型付き演算子形式は直接透磁率を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toPermeability()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子はインダクタンス、長さ、2つの磁場の量を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l`（可換） |
| `inductance / permeability` | `KLengthUnitInstance` | 幾何係数 `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H`（可換） |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.electric.permeability.*

// 実例 - 真空中で1000 A/mの磁界は1.257 mTの磁束密度を生じる。
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// 定義式を透磁率について解く:
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// 同じ透磁率をネイティブの kg·m·s⁻²·A⁻² 表現として:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `H/m` | `henriesPerMeter` | 透磁率、基本単位（名前付きトークン、メートルあたりのヘンリー） |
| `μ₀` | `vacuumPermeability` | 真空の透磁率定数、1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | 磁束密度と磁界強度からの透磁率 |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | インダクタンスとコイル形状からの透磁率 |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | 質量・長さ / (時間²・電流²) としての透磁率（分数形式） |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | 純粋な積としての同じ透磁率 |
| `µH/m` | `micro.henriesPerMeter` | 接頭辞付き透磁率（マイクロヘンリー毎メートル） |
