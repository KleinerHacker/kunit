# 導電率

パッケージ: `org.pcsoft.framework.kunit.electric.conductivity`
基本単位: **メートルあたりのジーメンス** (`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`)

種別: **構成単位（constructed unit）**

電気導電率は **構成単位**です。組成は `質量⁻¹ · 長さ⁻³ · 時間³ · 電流²`
（`kg⁻¹·m⁻³·s³·A²`）です。`KConductivityUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `-1`、`KDistanceUnit.BASE`（メートル）を `-3`、`KTimeUnit.BASE`（秒）を `+3`、
`KElectricCurrentUnit.BASE`（アンペア）を `+2` として保持します。ライブラリの質量成分は **グラム**
（キログラムではない）に正規化されており、質量の指数が *負*であるため、正準積は1000を掛けて
メートルあたりのジーメンスに換算されます。格納される値は常にS/mに正規化されています。

導電率はコンダクタンスの背後にある材料特性であり、[抵抗率](resistivity.md)の逆数です（`σ = 1 / ρ`）。

## 導電率を組み立てる

名前付きトークンで、または分解表現（下記参照）から導電率を組み立てられます。名前付き単位は値1の トークンとして存在します（
`of`/`into` と併用）。

| 導電率                                   | 記号    |                    トークン | S/mでの1単位 |
|------------------------------------------|---------|----------------------------:|-------------:|
| メートルあたりのジーメンス               | `S/m`   |           `siemensPerMeter` |          1.0 |
| センチメートルあたりのジーメンス         | `S/cm`  |      `siemensPerCentimeter` |        100.0 |
| センチメートルあたりのマイクロジーメンス | `µS/cm` | `microsiemensPerCentimeter` |       1.0e-4 |
| メートルあたりのメガジーメンス           | `MS/m`  |       `megasiemensPerMeter` |        1.0e6 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`mega.siemensPerMeter`、`milli.siemensPerMeter` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // 銅
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## 複数の分解表現

導電率は複数の **等価な分解表現**を通じて到達でき、いずれも同じ値として等しい導電率を生成します。

| 表現                            | 結果の型                    | 意味                                                               |
|---------------------------------|-----------------------------|--------------------------------------------------------------------|
| `1 / resistivity`               | `KConductivityUnitInstance` | 逆数 `σ = 1 / ρ`                                                   |
| `conductance / length`          | `KConductivityUnitInstance` | `σ = G · l / A`；幾何係数 `l / A` は長さの逆数であるため除算になる |
| `current²·time³/(mass·length³)` | `.toConductivity()` 経由    | ネイティブの正準 `kg⁻¹·m⁻³·s³·A²` 表現                             |

型付き演算子形式は直接導電率を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toConductivity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子はコンダクタンス、長さ、導電率を結び付けます。

| 表現                         | 結果の型                   | 意味                     |
|------------------------------|----------------------------|--------------------------|
| `conductivity * length`      | `KConductanceUnitInstance` | `G = σ · A / l`（可換）  |
| `conductance / conductivity` | `KLengthUnitInstance`      | 幾何係数 `A / l = G / σ` |
| `1 / conductivity`           | `KResistivityUnitInstance` | 抵抗率への回帰           |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.conductance.siemens
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.electric.conductivity.*

// 実例 - 銅: 17 nΩ·mの抵抗率は約58.8 MS/mの導電率になる。
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// コンダクタンスを導体の幾何形状で割る:
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance, 2 S/m

// 同じ導電率をネイティブの kg⁻¹·m⁻³·s³·A² 表現として:
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// 逆数のペアは対称的:
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学             | Kotlin                                                                      | 意味                                                             |
|------------------|-----------------------------------------------------------------------------|------------------------------------------------------------------|
| `S/m`            | `siemensPerMeter`                                                           | 導電率、基本単位（名前付きトークン、メートルあたりのジーメンス） |
| `1 / ρ`          | `1 / (17 of nano.ohmMeters)`                                                | 抵抗率の逆数としての導電率                                       |
| `A²·s³/(kg·m³)`  | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))`       | 電流²・時間³ / (質量・長さ³) としての導電率（分数形式）          |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | 純粋な積としての同じ導電率                                       |
| `MS/m`           | `mega.siemensPerMeter`                                                      | 接頭辞付き導電率（メートルあたりのメガジーメンス）               |
