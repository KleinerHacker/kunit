# 比熱容量

パッケージ: `org.pcsoft.framework.kunit.thermo.specificheatcapacity`
基本単位: **ジュール毎キログラムケルビン** (`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`)

種別: **構成単位（constructed unit）**

比熱容量は、材料の*単位質量あたりの*[熱容量](heat-capacity.md)です: `J/(kg·K)`。あらゆる
「これを加熱するのにどれだけのエネルギーが必要か」という計算の背後にある材料特性です。

`KSpecificHeatCapacityUnitInstance` は正準の正規形 `distance² · time⁻² · temperature⁻¹`
(`m²·s⁻²·K⁻¹`)にちょうど3つの項からなる `KMixedUnitInstance` をラップします — 質量の次元は
[比エネルギー](specific-energy.md)と同様に打ち消されます。温度の次元は**差**のグループ
(`KTemperatureDifferenceUnit`)であり、アフィンな絶対温度ではありません。

## 名前付き単位

| 単位 | 記号 | トークン | J/(kg·K)での1単位 |
|---|---|---:|---:|
| ジュール毎キログラムケルビン | `J/(kg·K)` | `joulesPerKilogramKelvin` | 1.0 |
| カロリー毎グラムケルビン | `cal/(g·K)` | `caloriesPerGramKelvin` | 4184.0 |
| Btu 毎ポンド華氏度 | `Btu/(lb·°F)` | `btusPerPoundFahrenheit` | 4186.8 |

すべてがSI接頭辞の全範囲をサポートします(`kilo.joulesPerKilogramKelvin` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0(カロリーの定義により水は 1 cal/(g·K))
```

## 実例 — 浴槽を加熱する

150リットルの水(150 kg)を12 °Cから40 °Cに加熱します。水の比熱容量は 4184 J/(kg·K) です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val bath = 150 of kilo.grams
val rise = (40 of celsius) - (12 of celsius)  // 28 K

// ルート1: まず浴槽の熱容量を組み立てる
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// ルート2: 代わりに比エネルギー(1キログラムあたりのエネルギー)経由
val perKilogram = water * rise                // KSpecificEnergyUnitInstance, 117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ - 同一
```

## 隣接単位での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `heatCapacity / mass` | `KSpecificHeatCapacityUnitInstance` | 物体からの材料特性 |
| `specificEnergy / temperatureDifference` | `KSpecificHeatCapacityUnitInstance` | 同じ、比エネルギー経由 |
| `specificHeatCapacity * mass` | `KHeatCapacityUnitInstance` | 物体の熱容量 |
| `mass * specificHeatCapacity` | `KHeatCapacityUnitInstance` | 同じ(可換) |
| `heatCapacity / specificHeatCapacity` | `KMassUnitInstance` | 物体の質量 |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance` | キログラムあたりのエネルギー |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance` | 同じ(可換) |
| `specificEnergy / specificHeatCapacity` | `KTemperatureDifferenceUnitInstance` | 到達可能な上昇 |

## 分解表現

3つの分解表現すべてが同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `heatCapacity / mass` | 型付き演算子 | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | 型付き演算子 | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹` | ネイティブ表現 + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity   = (1 of joulesPerKelvin) / (1 of kilo.grams)
val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
val native = (
    ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toSpecificHeatCapacity()

viaHeatCapacity == viaSpecificEnergy // true
viaHeatCapacity == native            // true - すべて 1.0 J/(kg·K)
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J/(kg·K)` | `joulesPerKilogramKelvin` | 比熱容量、基本単位 |
| `m²·s⁻²·K⁻¹` | `(meters pow 2) / (seconds pow 2) / ΔK` | 同じ量を基本次元で |
| `cal/(g·K)` | `caloriesPerGramKelvin` | カロリー毎グラムケルビン |
| `c = C / m` | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | 熱容量÷質量から |
| `c = q / ΔT` | `(8368 of joulesPerKilogram) / rise` | 比エネルギー÷温度上昇から |
| `C = c · m` | `water * bath` | 材料×質量から物体の熱容量 |
| `Q = c · m · ΔT` | `water * bath * rise` | 総エネルギー |
