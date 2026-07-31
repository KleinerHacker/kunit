# 熱容量

パッケージ: `org.pcsoft.framework.kunit.thermo.heatcapacity`
基本単位: **ジュール毎ケルビン** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

種別: **構成単位（constructed unit）**

熱容量は、物体が温度上昇の単位あたりに吸収するエネルギーです: `energy / temperature`(`J/K`)。
`KHeatCapacityUnitInstance` は正準の正規形 `mass¹ · distance² · time⁻² · temperature⁻¹`
(`kg·m²·s⁻²·K⁻¹`)にちょうど4つの項からなる `KMixedUnitInstance` をラップし、常に J/K に正規化されます。

!!! note "温度の **差**であり、絶対温度ではない"
温度の次元は **差**のグループ (`KTemperatureDifferenceUnit`、記号 `ΔK`)であり、アフィンな絶対値の
`KTemperatureUnit` ではありません。熱容量はエネルギーを温度の **区間**に関連付けます。オフセットを 持つ絶対尺度 (°C、°F)
は商の中では物理的に誤りとなります。

同じ次元 `J/K` は **エントロピー**も表します — このグループがなぜ独自の型ではなくこの型を共有するかは
[エントロピー](entropy.md)を参照してください。単位質量あたりでは[比熱容量](specific-heat-capacity.md)、
モルあたりでは[モル熱容量](molar-heat-capacity.md)になります。

## 名前付き単位

| 単位               | 記号     |            トークン | J/Kでの1単位 |
|--------------------|----------|--------------------:|-------------:|
| ジュール毎ケルビン | `J/K`    |   `joulesPerKelvin` |          1.0 |
| カロリー毎ケルビン | `cal/K`  | `caloriesPerKelvin` |        4.184 |
| Btu 毎華氏度       | `Btu/°F` | `btusPerFahrenheit` |  ≈ 1899.1005 |

すべてがSI接頭辞の全範囲をサポートします (`kilo.joulesPerKelvin`、`kilo.caloriesPerKelvin` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## 実例 — やかんで水を沸かす

1リットルの水 (4184 J/K)を20 °Cから100 °Cに加熱します。どれだけのエネルギーが必要でしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 1リットルの水
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance、80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ... そして逆方向: 100 kJでどこまで届くか?
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## 中核単位 (エネルギーと温度差)での計算

| 式                                     | 結果の型                             | 意味               |
|----------------------------------------|--------------------------------------|--------------------|
| `energy / temperatureDifference`       | `KHeatCapacityUnitInstance`          | 熱容量             |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance`                | 必要なエネルギー   |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance`                | エネルギー(可換)   |
| `energy / heatCapacity`                | `KTemperatureDifferenceUnitInstance` | 到達可能な温度上昇 |

## 分解表現

どちらの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現                                    | 形式                                | 結果                             |
|---------------------------------------------|-------------------------------------|----------------------------------|
| `energy / temperatureDifference`            | 型付き演算子                        | `KHeatCapacityUnitInstance` 直接 |
| `mass · distance² · time⁻² · temperature⁻¹` | ネイティブ表現 + `toHeatCapacity()` | `KHeatCapacityUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// 型付き演算子形式
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// ネイティブの基本次元形式(kg·m²·s⁻²·K⁻¹)、toHeatCapacity() によって認識される
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true - どちらも 1.0 J/K
```

`toHeatCapacity()` は **唯一**の正準の正規形のみを認識します。等価な表現はどれも自動的にこの形に還元され、 誤った形は
`IllegalStateException` を投げます。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - : 同じグループ、単位・接頭辞間の自動変換
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// 比較(正規化された J/K の値で)
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// 2つの熱容量間の * / / は KMixedUnitInstance に外れる
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学            | Kotlin                                          | 意味                                |
|-----------------|-------------------------------------------------|-------------------------------------|
| `J/K`           | `joulesPerKelvin`                               | 熱容量、基本単位 — 名前付きトークン |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 同じ量を基本次元で                  |
| `kJ/K`          | `kilo.joulesPerKelvin`                          | キロジュール毎ケルビン              |
| `cal/K`         | `caloriesPerKelvin`                             | カロリー毎ケルビン                  |
| `C = Q / ΔT`    | `(4184 of joules) / rise`                       | エネルギー÷温度上昇から熱容量       |
| `Q = C · ΔT`    | `kettle * rise`                                 | 熱容量×温度上昇からエネルギー       |
| `ΔT = Q / C`    | `(100 of kilo.joules) / kettle`                 | エネルギー÷熱容量から温度上昇       |
