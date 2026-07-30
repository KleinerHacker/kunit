# 熱抵抗(R値)

パッケージ: `org.pcsoft.framework.kunit.thermo.resistance`
基本単位: **平方メートルケルビン毎ワット** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`)

種別: **構成単位（constructed unit）**

熱抵抗 — **R値** — は、ある層がどれだけ強く熱の流れに抵抗するかを表します: `m²·K/W`。これは
[熱伝達率](heat-transfer-coefficient.md)(U値)の正確な逆数であり、直列の層のR値が単純に**加算される**
ため、断熱材が実際に販売される際に使われる形式です。

`KThermalResistanceUnitInstance` は正準の正規形 `mass⁻¹ · time³ · temperature¹`(`kg⁻¹·s³·K`)に
ちょうど3つの項からなる `KMixedUnitInstance` をラップし、常に m²·K/W に正規化されます。

!!! note "パッケージ名とクラス名"
    パッケージは `thermo.resistance` であり、`thermo.thermalresistance` ではありません — 単位パッケージ
    はその分野パッケージの名前を繰り返してはなりません。**型**は完全な技術用語
    (`KThermalResistanceUnitInstance`)を保持しており、これによって `electric.resistance` と区別されます。

## 名前付き単位

| 単位 | 記号 | トークン | m²·K/Wでの1単位 |
|---|---|---:|---:|
| 平方メートルケルビン毎ワット(RSI) | `m²·K/W` | `squareMeterKelvinPerWatt` | 1.0 |
| インペリアルR値 | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | ≈ 0.176110 |
| クロ | `clo` | `clo` | 0.155 |
| トグ | `tog` | `tog` | 0.1 |

米国の "R-30" バットは `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W です。ビジネススーツは
約1 cloで、掛け布団はトグで評価されます(1 clo = 1.55 tog)。すべての単位がSI接頭辞の全範囲をサポート
します。

## 実例 — 層ごとの断熱壁

壁は20 cmのミネラルウール(λ = 0.04 W/(m·K))と12 cmのレンガ(λ = 0.8 W/(m·K))から構成されています。
合計のR値、結果としてのU値、そして ΔT = 25 K における熱損失はどうなるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // 直列の層は加算される
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2("R-29"の壁)

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## 隣接単位での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | `KThermalResistanceUnitInstance` | 測定からのR |
| `length / thermalConductivity` | `KThermalResistanceUnitInstance` | 材料+厚さからのR |
| `thermalResistance * heatFluxDensity` | `KTemperatureDifferenceUnitInstance` | 持続する温度差 |
| `heatFluxDensity * thermalResistance` | `KTemperatureDifferenceUnitInstance` | 同じ(可換) |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance` | 結果としての流束 |
| `thermalResistance * thermalConductivity` | `KLengthUnitInstance` | 必要な厚さ |
| `thermalConductivity * thermalResistance` | `KLengthUnitInstance` | 同じ(可換) |
| `length / thermalResistance` | `KThermalConductivityUnitInstance` | 暗黙の伝導率 |
| `1 / heatTransferCoefficient` | `KThermalResistanceUnitInstance` | UからR |
| `1 / thermalResistance` | `KHeatTransferCoefficientUnitInstance` | RからU |

2つの逆数演算子は狭く宣言されているため、`1 / u` と `1 / r` はグループに依存しない `Number.div` が
生成するであろう汎用の混合単位ではなく、**型付き**の値を返します。

## 分解表現

3つの分解表現すべてが同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `temperatureDifference / heatFluxDensity` | 型付き演算子 | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity` | 型付き演算子 | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹` | ネイティブ + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true - すべて 1.0 m²·K/W
```

## 演算子

`+` と `-` はここではまさに物理的に意味のある演算です: 直列の層はそれぞれのR値を加算します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true(5 tog = 0.5 m²·K/W)
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `m²·K/W` | `squareMeterKelvinPerWatt` | 熱抵抗(R値)、基本単位 |
| `kg⁻¹·s³·K` | `(seconds pow 3) * ΔK / grams` | 同じ量を基本次元で |
| `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` | インペリアルR値 |
| `R = d / λ` | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | 厚さ÷伝導率からR |
| `R = ΔT / q̇` | `drop / (4 of wattsPerSquareMeter)` | 温度差÷流束からR |
| `R_total = R₁ + R₂` | `wool + brick` | 直列の層 |
| `U = 1 / R` | `1 / total` | R値からU値 |
| `q̇ = ΔT / R` | `drop / total` | 温度差÷Rから流束 |
