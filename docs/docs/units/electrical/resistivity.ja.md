# 抵抗率

パッケージ: `org.pcsoft.framework.kunit.resistivity`
基本単位: **オーム・メートル** (`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`)

種別: **構成単位（constructed unit）**

電気抵抗率は**構成単位**です。組成は `質量 · 長さ³ · 時間⁻³ · 電流⁻²`
（`kg·m³·s⁻³·A⁻²`）です。`KResistivityUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `+1`、`KDistanceUnit.BASE`（メートル）を `+3`、`KTimeUnit.BASE`（秒）を `-3`、
`KElectricCurrentUnit.BASE`（アンペア）を `-2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってオーム・メートルに換算されます。
格納される値は常にオーム・メートルに正規化されています。

抵抗率は抵抗の背後にある材料特性であり、[導電率](conductivity.md)の逆数です（`ρ = 1 / σ`）。

## 抵抗率を組み立てる

名前付きトークンで、または分解表現（下記参照）から抵抗率を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 抵抗率 | 記号 | トークン | Ω·mでの1単位 |
|---|---|---:|---:|
| オーム・メートル | `Ω·m` | `ohmMeters` | 1.0 |
| オーム・センチメートル | `Ω·cm` | `ohmCentimeters` | 0.01 |
| スタットオーム・センチメートル（CGS-ESU） | `statΩ·cm` | `statohmCentimeters` | 8.98755179e9 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`nano.ohmMeters`、`micro.ohmMeters`、
`milli.ohmCentimeters` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.resistivity.*

val rho = 17 of nano.ohmMeters     // 銅
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## 複数の分解表現

抵抗率は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい抵抗率を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `resistance * length` | `KResistivityUnitInstance` | `ρ = R · A / l`、幾何係数 `A / l` は長さとなる（可換） |
| `1 / conductivity` | `KResistivityUnitInstance` | 逆数 `ρ = 1 / σ` |
| `mass·length³/(time³·current²)` | `.toResistivity()` 経由 | ネイティブの正準 `kg·m³·s⁻³·A⁻²` 表現 |

型付き演算子形式は直接抵抗率を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toResistivity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は抵抗、長さ、抵抗率を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `resistivity / length` | `KResistanceUnitInstance` | `R = ρ · l / A` |
| `resistivity / resistance` | `KLengthUnitInstance` | 幾何係数 `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.resistivity.*

// 実例 - 銅の配線: 17 nΩ·mの抵抗率が1 mmの幾何係数にわたると1.7 µΩになる。
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance, 1.7e-5 Ω

// 定義式を抵抗率について解く:
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance, 2 Ω·m

// 同じ抵抗率をネイティブの kg·m³·s⁻³·A⁻² 表現として:
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`³`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `Ω·m` | `ohmMeters` | 抵抗率、基本単位（名前付きトークン、オーム・メートル） |
| `R · (A/l)` | `(5 of ohms) * (0.4 of meters)` | 抵抗と幾何係数からの抵抗率 |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | 質量・長さ³ / (時間³・電流²) としての抵抗率（分数形式） |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)` | 純粋な積としての同じ抵抗率 |
| `nΩ·m` | `nano.ohmMeters` | 接頭辞付き抵抗率（ナノオーム・メートル） |
