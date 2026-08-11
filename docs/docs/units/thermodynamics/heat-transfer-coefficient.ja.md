# 熱伝達率

パッケージ: `org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
基本単位: **ワット毎平方メートルケルビン**
(`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`)

種別: **構成単位（constructed unit）**

熱伝達率 — 建築物理学における **U値** — は、構造部材がケルビンあたりの温度差で通過させる熱流束密度です:
`W/(m²·K)`。U値が低いほど断熱性能が高くなります。

`KHeatTransferCoefficientUnitInstance` は正準の正規形 `mass¹ · time⁻³ · temperature⁻¹`
(`kg·s⁻³·K⁻¹`)にちょうど3つの項からなる `KMixedUnitInstance` をラップし、常に W/ (m²·K) に正規化されます。
[熱流束密度](heat-flux-density.md)と同様に、面積がワットの長さ次元を打ち消すため、正規形には距離の項が ありません。

その逆数は[熱抵抗](thermal-insulance.md)(R値)です。厚さを掛けると[熱伝導率](thermal-conductivity.md)
になります。

## 名前付き単位

| 単位                                   | 記号             |                                  トークン | W/(m²·K)での1単位 |
|----------------------------------------|------------------|------------------------------------------:|------------------:|
| ワット毎平方メートルケルビン           | `W/(m²·K)`       |               `wattsPerSquareMeterKelvin` |               1.0 |
| Btu 毎時間平方フィート華氏度           | `Btu/(h·ft²·°F)` |         `btusPerHourSquareFootFahrenheit` |        ≈ 5.678263 |
| カロリー毎秒平方センチメートルケルビン | `cal/(s·cm²·K)`  | `caloriesPerSecondSquareCentimeterKelvin` |           41840.0 |

すべてがSI接頭辞の全範囲をサポートします (`milli.wattsPerSquareMeterKelvin` など)。

## 代表的なU値

| 部材               |                    U |
|--------------------|---------------------:|
| 単板ガラス         |       ≈ 5.8 W/(m²·K) |
| 複層ガラス         |       ≈ 2.8 W/(m²·K) |
| トリプルガラス     | ≈ 0.7 … 1.3 W/(m²·K) |
| パッシブハウスの壁 |      ≈ 0.15 W/(m²·K) |

## 実例 — 窓を通した熱損失

2.4 m²のトリプルガラス窓は U = 1.3 W/ (m²·K) です。室内は21 °C、屋外は1 °Cです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// 単板ガラスならどうなるか?
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W - 4.5倍以上
```

## 隣接単位での計算

| 式                                                | 結果の型                               | 意味               |
|---------------------------------------------------|----------------------------------------|--------------------|
| `heatFluxDensity / temperatureDifference`         | `KHeatTransferCoefficientUnitInstance` | 測定からのU値      |
| `thermalConductivity / length`                    | `KHeatTransferCoefficientUnitInstance` | 材料+厚さからのU値 |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance`         | 部材を通る流束     |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance`         | 同じ(可換)         |
| `heatFluxDensity / heatTransferCoefficient`       | `KTemperatureDifferenceUnitInstance`   | 駆動する差         |
| `heatTransferCoefficient * length`                | `KThermalConductivityUnitInstance`     | 材料の伝導率       |
| `length * heatTransferCoefficient`                | `KThermalConductivityUnitInstance`     | 同じ(可換)         |
| `thermalConductivity / heatTransferCoefficient`   | `KLengthUnitInstance`                  | 必要な厚さ         |

## 分解表現

3つの分解表現すべてが同じ値として等しい型付きインスタンスを生成します。

| 分解表現                                  | 形式                                       | 結果                                   |
|-------------------------------------------|--------------------------------------------|----------------------------------------|
| `heatFluxDensity / temperatureDifference` | 型付き演算子                               | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length`            | 型付き演算子                               | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹`           | ネイティブ + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true - すべて 1.0 W/(m²·K)
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学             | Kotlin                                            | 意味                    |
|------------------|---------------------------------------------------|-------------------------|
| `W/(m²·K)`       | `wattsPerSquareMeterKelvin`                       | 熱伝達率(U値)、基本単位 |
| `kg·s⁻³·K⁻¹`     | `grams / (seconds pow 3) / ΔK`                    | 同じ量を基本次元で      |
| `U = q̇ / ΔT`     | `(26 of wattsPerSquareMeter) / drop`              | 流束÷温度差からU値      |
| `U = λ / d`      | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | 伝導率÷厚さからU値      |
| `q̇ = U · ΔT`     | `window * drop`                                   | U値×温度差から流束      |
| `P = U · A · ΔT` | `(window * drop) * glass`                         | 総熱損失                |
