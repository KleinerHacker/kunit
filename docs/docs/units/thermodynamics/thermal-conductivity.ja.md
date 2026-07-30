# 熱伝導率

パッケージ: `org.pcsoft.framework.kunit.thermo.conductivity`
基本単位: **ワット毎メートルケルビン** (`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`)

種別: **構成単位（constructed unit）**

熱伝導率 `λ`(`k` とも表記)は、フーリエの法則における材料特性です: 材料を通る
[熱流束密度](heat-flux-density.md)は、その伝導率に[温度勾配](temperature-gradient.md)を掛けたものに
等しくなります。単位: `W/(m·K)`。

`KThermalConductivityUnitInstance` は正準の正規形 `mass¹ · distance¹ · time⁻³ · temperature⁻¹`
(`kg·m·s⁻³·K⁻¹`)にちょうど4つの項からなる `KMixedUnitInstance` をラップし、常に W/(m·K) に正規化されます。

!!! note "パッケージ名とクラス名"
    パッケージは `thermo.conductivity` であり、`thermo.thermalconductivity` ではありません —
    単位パッケージはその分野パッケージの名前を繰り返してはなりません。**型**は完全な技術用語
    (`KThermalConductivityUnitInstance`)を保持しており、これによって `electric.conductivity` と
    区別されます。

厚さで割ると[熱伝達率](heat-transfer-coefficient.md)になり、厚さをこれで割ると
[熱抵抗](thermal-resistance.md)(R値)になります。

## 名前付き単位

| 単位 | 記号 | トークン | W/(m·K)での1単位 |
|---|---|---:|---:|
| ワット毎メートルケルビン | `W/(m·K)` | `wattsPerMeterKelvin` | 1.0 |
| Btu 毎時間フィート華氏度 | `Btu/(h·ft·°F)` | `btusPerHourFootFahrenheit` | ≈ 1.730735 |
| カロリー毎秒センチメートルケルビン | `cal/(s·cm·K)` | `caloriesPerSecondCentimeterKelvin` | 418.4 |

すべてがSI接頭辞の全範囲をサポートします — 断熱材は自然に `40 of milli.wattsPerMeterKelvin` のように
記述されます。

## 代表的な値

| 材料 | λ |
|---|---:|
| 銅 | 401 W/(m·K) |
| 鋼 | ≈ 50 W/(m·K) |
| ガラス | ≈ 1 W/(m·K) |
| ミネラルウール | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## 実例 — 断熱壁を通る熱損失

30 cmのミネラルウール層(λ = 0.04 W/(m·K))が、21 °Cの室内と −5 °Cの屋外空気を隔てています。壁は
12 m²です。どれだけの熱が失われるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance、≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance(フーリエの法則)
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## 隣接単位での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | `KThermalConductivityUnitInstance` | λについて解いたフーリエの法則 |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance` | フーリエの法則 |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance` | 同じ(可換) |
| `heatFluxDensity / thermalConductivity` | `KTemperatureGradientUnitInstance` | 暗黙の勾配 |

## 分解表現

両方の分解表現が同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | 型付き演算子 | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | ネイティブ + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true - どちらも 1.0 W/(m·K)
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `W/(m·K)` | `wattsPerMeterKelvin` | 熱伝導率、基本単位 |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK` | 同じ量を基本次元で |
| `mW/(m·K)` | `milli.wattsPerMeterKelvin` | ミリワット毎メートルケルビン(断熱材) |
| `q̇ = λ · ∇T` | `wool * gradient` | フーリエの法則 |
| `λ = q̇ / ∇T` | `(80 of wattsPerSquareMeter) / gradient` | 流束÷勾配から伝導率 |
| `∇T = q̇ / λ` | `flux / wool` | 流束÷伝導率から勾配 |
