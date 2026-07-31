# 温度勾配

パッケージ: `org.pcsoft.framework.kunit.thermo.temperaturegradient`
基本単位: **ケルビン毎メートル** (`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`)

種別: **構成単位（constructed unit）**

温度勾配は長さあたりの温度変化です: `温度差 / 長さ`（`K/m`）。これは伝導の駆動量であり、
[熱伝導率](thermal-conductivity.md)を掛けると[熱流束密度](heat-flux-density.md)になります。

`KTemperatureGradientUnitInstance` は正準の正規形 `temperature¹ · distance⁻¹`（`K·m⁻¹`）にちょうど 2つの項からなる
`KMixedUnitInstance` をラップし、常に K/m に正規化されます。

!!! note "勾配は長さあたりの *変化*である"
温度の次元は **差**グループ (`KTemperatureDifferenceUnit`)です。オフセットを持つ絶対スケール (°C、°F)
は勾配において意味を持ちません — 区間のみが意味を持ちます。そのため `°F/ft` はファーレンハイト の **区間**係数 5/9
で変換され、−32のオフセットでは変換されません。

## 名前付き単位

| 単位                   | 記号    |             トークン | K/mでの1単位 |
|------------------------|---------|---------------------:|-------------:|
| ケルビン毎メートル     | `K/m`   |     `kelvinPerMeter` |          1.0 |
| ケルビン毎キロメートル | `K/km`  | `kelvinPerKilometer` |        0.001 |
| 華氏度毎フィート       | `°F/ft` |  `fahrenheitPerFoot` |   ≈ 1.822689 |

すべてがSI接頭辞の全範囲をサポートします (`milli.kelvinPerMeter` など)。

## 実例 — 地熱勾配

地殻は深さ1キロメートルごとにおよそ25 Kずつ暖まります。ボーリング孔は3.5 kmに達します。孔の底では 岩石はどれだけ暖かくなり、100
Kの上昇には何メートル掘る必要があるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // 底では87.5 K暖かい

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## 中核単位 (温度差と長さ)での計算

| 式                                            | 結果の型                             | 意味                 |
|-----------------------------------------------|--------------------------------------|----------------------|
| `temperatureDifference / length`              | `KTemperatureGradientUnitInstance`   | 勾配                 |
| `temperatureGradient * length`                | `KTemperatureDifferenceUnitInstance` | 長さ全体にわたる上昇 |
| `length * temperatureGradient`                | `KTemperatureDifferenceUnitInstance` | 上昇(可換)           |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance`                | 及ぶ長さ             |

## 分解表現

両方の分解表現が同じ値として等しい型付きインスタンスを生成します。

| 分解表現                         | 形式                                       | 結果                               |
|----------------------------------|--------------------------------------------|------------------------------------|
| `temperatureDifference / length` | 型付き演算子                               | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹`       | ネイティブ表現 + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true - どちらも 1.0 K/m
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学          | Kotlin                                                     | 意味                             |
|---------------|------------------------------------------------------------|----------------------------------|
| `K/m`         | `kelvinPerMeter`                                           | 温度勾配、基本単位               |
| `K·m⁻¹`       | `ΔK / meters`                                              | 同じ量を基本次元で               |
| `K/km`        | `kelvinPerKilometer`                                       | ケルビン毎キロメートル(地熱勾配) |
| `°F/ft`       | `fahrenheitPerFoot`                                        | 華氏度毎フィート                 |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | 上昇÷長さから勾配                |
| `ΔT = ∇T · L` | `geothermal * borehole`                                    | 勾配×長さから上昇                |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal`        | 上昇÷勾配から長さ                |
