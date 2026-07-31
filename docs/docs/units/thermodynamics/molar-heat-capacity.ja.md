# モル熱容量

パッケージ: `org.pcsoft.framework.kunit.thermo.molarheatcapacity`
基本単位: **ジュール毎モルケルビン** (`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`)

種別: **構成単位（constructed unit）**

モル熱容量は、物質の *モルあたり*の[熱容量](heat-capacity.md)です: `J/(mol·K)`。これは気体や化学熱力学の
自然な形式であり、そこでは物質量はキログラムではなくモルで数えられます (キログラムあたりは
[比熱容量](specific-heat-capacity.md)です)。

`KMolarHeatCapacityUnitInstance` は正準の正規形
`mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹`(`kg·m²·s⁻²·mol⁻¹·K⁻¹`)にちょうど5つの項 からなる
`KMixedUnitInstance` をラップします。温度の次元は **差**のグループであり、アフィンな絶対温度 ではありません。

## 名前付き単位

| 単位                   | 記号          |                トークン | J/(mol·K)での1単位 |
|------------------------|---------------|------------------------:|-------------------:|
| ジュール毎モルケルビン | `J/(mol·K)`   |   `joulesPerMoleKelvin` |                1.0 |
| カロリー毎モルケルビン | `cal/(mol·K)` | `caloriesPerMoleKelvin` |              4.184 |

どちらもSI接頭辞の全範囲をサポートします (`kilo.joulesPerMoleKelvin`、`milli.joulesPerMoleKelvin` など)。

## 気体定数

このグループはモル気体定数の正確なSI値を `GAS_CONSTANT`(8.31446261815324 J/ (mol·K))として公開します — 単純な `Double`
なので、係数としても読み値としても使えます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## 実例 — 窒素を加熱する (デュロン・プティの法則による検証)

二原子窒素は `c_p ≈ 29.1 J/(mol·K)` です。3モルを50 K加熱するにはどれだけのエネルギーが必要で、 モルあたりではどうなるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val nitrogen = 29.1 of joulesPerMoleKelvin
val sample = 3 of moles
val rise = KTemperatureDifference.ofKelvin(50)

// ルート1: まずサンプルの熱容量、次にエネルギー
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// ルート2: まずモルあたり
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J - 同一
```

## 隣接単位での計算

| 式                                          | 結果の型                             | 意味                     |
|---------------------------------------------|--------------------------------------|--------------------------|
| `heatCapacity / amountOfSubstance`          | `KMolarHeatCapacityUnitInstance`     | サンプルからの物質特性   |
| `molarEnergy / temperatureDifference`       | `KMolarHeatCapacityUnitInstance`     | 同じ、モルエネルギー経由 |
| `molarHeatCapacity * amountOfSubstance`     | `KHeatCapacityUnitInstance`          | サンプルの熱容量         |
| `amountOfSubstance * molarHeatCapacity`     | `KHeatCapacityUnitInstance`          | 同じ(可換)               |
| `heatCapacity / molarHeatCapacity`          | `KAmountOfSubstanceUnitInstance`     | 物質量                   |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance`           | モルあたりのエネルギー   |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance`           | 同じ(可換)               |
| `molarEnergy / molarHeatCapacity`           | `KTemperatureDifferenceUnitInstance` | 到達可能な上昇           |

## 分解表現

3つの分解表現すべてが同じ値として等しい型付きインスタンスを生成します。

| 分解表現                                                  | 形式                                 | 結果                             |
|-----------------------------------------------------------|--------------------------------------|----------------------------------|
| `heatCapacity / amountOfSubstance`                        | 型付き演算子                         | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference`                     | 型付き演算子                         | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | ネイティブ + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
val viaMolarEnergy  = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit() /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toMolarHeatCapacity()

viaHeatCapacity == viaMolarEnergy // true
viaHeatCapacity == native         // true - すべて 1.0 J/(mol·K)
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学                  | Kotlin                                                  | 意味                           |
|-----------------------|---------------------------------------------------------|--------------------------------|
| `J/(mol·K)`           | `joulesPerMoleKelvin`                                   | モル熱容量、基本単位           |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | 基本次元                       |
| `cal/(mol·K)`         | `caloriesPerMoleKelvin`                                 | カロリー毎モルケルビン         |
| `R`                   | `GAS_CONSTANT of joulesPerMoleKelvin`                   | モル気体定数、8.3145 J/(mol·K) |
| `C_m = C / n`         | `(58.2 of joulesPerKelvin) / (2 of moles)`              | 熱容量÷物質量から              |
| `C_m = ΔH_m / ΔT`     | `(58.2 of joulesPerMole) / rise`                        | モルエネルギー÷温度上昇から    |
| `Q = C_m · n · ΔT`    | `nitrogen * sample * rise`                              | 総エネルギー                   |
