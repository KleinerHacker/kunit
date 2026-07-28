# 誘電率

パッケージ: `org.pcsoft.framework.kunit.permittivity`
基本単位: **メートルあたりのファラド** (`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`)

種別: **構成単位（constructed unit）**

誘電率は**構成単位**です。組成は `質量⁻¹ · 長さ⁻³ · 時間⁴ · 電流²`
（`kg⁻¹·m⁻³·s⁴·A²` = `F/m`）です。`KPermittivityUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `-1`、`KDistanceUnit.BASE`（メートル）を `-3`、`KTimeUnit.BASE`（秒）を `+4`、
`KElectricCurrentUnit.BASE`（アンペア）を `+2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されており、質量の指数が*負*であるため、正準積は1000を掛けて
メートルあたりのファラドに換算されます。格納される値は常にF/mに正規化されています。

誘電率 `ε` は材料の電気定数であり、[電束密度](electricfluxdensity.md)を
[電界強度](electricfieldstrength.md)と関係付け（`ε = D / E`）、
[静電容量](capacitance.md)と極板形状を関係付けます。磁気的な対応物は[透磁率](permeability.md)です。

## 誘電率を組み立てる

名前付きトークンで、または分解表現（下記参照）から誘電率を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 誘電率 | 記号 | トークン | F/mでの1単位 |
|---|---|---:|---:|
| メートルあたりのファラド | `F/m` | `faradsPerMeter` | 1.0 |
| センチメートルあたりのファラド | `F/cm` | `faradsPerCentimeter` | 100.0 |
| 真空の誘電率 `ε₀` | `F/m` | `vacuumPermittivity` | 8.8541878188e-12 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`pico.faradsPerMeter`、`nano.faradsPerMeter` など）。
この定数は `KPermittivityUnit.VACUUM_PERMITTIVITY` としても利用できます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## 複数の分解表現

誘電率は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい誘電率を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `capacitance / length` | `KPermittivityUnitInstance` | `ε = C · d / A`、幾何係数 `d / A` は長さとなる |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |
| `(time⁴·current²)/(mass·length³)` | `.toPermittivity()` 経由 | ネイティブの正準 `kg⁻¹·m⁻³·s⁴·A²` 表現 |

型付き演算子形式は直接誘電率を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toPermittivity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は静電容量、長さ、2つの場の量を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `permittivity * length` | `KCapacitanceUnitInstance` | `C = ε · A / d`（可換） |
| `capacitance / permittivity` | `KLengthUnitInstance` | 幾何係数 `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E`（可換） |
| `electricFluxDensity / permittivity` | `KElectricFieldStrengthUnitInstance` | `E = D / ε` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.capacitance.farads
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricfluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.permittivity.*

// 実例 - 真空中で1 MV/mの電界は8.854 µC/m²の束密度を生じる。
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// 定義式を誘電率について解く:
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// 同じ誘電率をネイティブの kg⁻¹·m⁻³·s⁴·A² 表現として:
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`⁴`、`⁻³`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `F/m` | `faradsPerMeter` | 誘電率、基本単位（名前付きトークン、メートルあたりのファラド） |
| `ε₀` | `vacuumPermittivity` | 真空の誘電率定数、8.854 pF/m |
| `D / E` | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)` | 束密度と電界強度からの誘電率 |
| `C · (d/A)` | `(10 of farads) / (5 of meters)` | 静電容量と幾何係数からの誘電率 |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))` | (時間⁴・電流²) / (質量・長さ³) としての誘電率（分数形式） |
| `kg⁻¹·m⁻³·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | 純粋な積としての同じ誘電率 |
| `pF/m` | `pico.faradsPerMeter` | 接頭辞付き誘電率（ピコファラド毎メートル） |
