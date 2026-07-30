# 熱拡散率

パッケージ: `org.pcsoft.framework.kunit.thermo.diffusivity`
基本単位: **平方メートル毎秒** (`KThermalDiffusivityUnit.BASE == KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND`)

種別: **構成単位（constructed unit）**

熱拡散率 `α` は、温度変化がどれだけ*速く*材料中を伝播するかを表します — 定常状態でどれだけの熱が
流れるかを表す[熱伝導率](thermal-conductivity.md)とは対照的です。単位: `m²/s`。次のように定義されます:

```
α = λ / (ρ · c_p)
```

`KThermalDiffusivityUnitInstance` は正準の正規形 `distance² · time⁻¹`(`m²·s⁻¹`)にちょうど2つの項
からなる `KMixedUnitInstance` をラップし、常に m²/s に正規化されます。

!!! note "パッケージ名とクラス名"
    パッケージは `thermo.diffusivity` であり、`thermo.thermaldiffusivity` ではありません — 単位
    パッケージはその分野パッケージの名前を繰り返してはなりません。型は完全な技術用語
    (`KThermalDiffusivityUnitInstance`)を保持します。次元 `m²/s` は動粘度や質量拡散率とも共有されて
    いますが、このグループは熱的な量をモデル化します。

## 名前付き単位

| 単位 | 記号 | トークン | m²/sでの1単位 |
|---|---|---:|---:|
| 平方メートル毎秒 | `m²/s` | `squareMetersPerSecond` | 1.0 |
| 平方ミリメートル毎秒 | `mm²/s` | `squareMillimetersPerSecond` | 1e-6 |
| 平方フィート毎時間 | `ft²/h` | `squareFeetPerHour` | ≈ 2.58064e-5 |

材料表では `α` を mm²/s で記載しますが、これはちょうど `micro.squareMetersPerSecond` です。すべての
単位がSI接頭辞の全範囲をサポートします。

## 代表的な値

| 材料 | α |
|---|---:|
| 銅 | ≈ 116 mm²/s |
| 鋼 | ≈ 14 mm²/s |
| ガラス | ≈ 0.34 mm²/s |
| 水 | ≈ 0.14 mm²/s |
| ミネラルウール | ≈ 1.2 mm²/s |

## 実例 — 銅がどれだけ速く均一化するか

銅は λ = 401 W/(m·K)、ρ = 8960 kg/m³、c_p = 385 J/(kg·K) です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// 逆演算: 拡散率から伝導率を求める
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## 隣接単位での計算

定義関係は**三項**(`α = λ / (ρ · c_p)`)であるため、ここで扱う他のすべてのグループとは異なり、
容積熱容量 `ρ · c_p`(J/(m³·K))という中間型 — このライブラリではモデル化されていません — を発明せずに
単一の二項演算子にすることはできません。そのため、この関係は名前付きの強く型付けされた関数として
公開されます:

| 関数 | 結果の型 | 意味 |
|---|---|---|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KThermalDiffusivityUnitInstance` | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance` | `λ = α · ρ · c_p` |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance` | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

これら4つの関数はすべて、他のすべての分解表現と同じ正規化ファクトリーに集約されます。

## 分解表現

両方の分解表現が同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `λ / (ρ · c_p)` | 型付き関数 `diffusivityWith` | `KThermalDiffusivityUnitInstance` |
| `distance² · time⁻¹` | ネイティブ表現 + `toThermalDiffusivity()` | `KThermalDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K)、ρ = 1 kg/m³、c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toThermalDiffusivity()

typed == native // true - どちらも 1.0 m²/s
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `m²/s` | `squareMetersPerSecond` | 熱拡散率、基本単位 |
| `m²·s⁻¹` | `(meters pow 2) / seconds` | 同じ量を基本次元で |
| `mm²/s` | `squareMillimetersPerSecond` | 平方ミリメートル毎秒(材料表) |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)` | 定義関係 |
| `λ = α · ρ · c_p` | `alpha.conductivityWith(density, heat)` | 拡散率から伝導率 |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)` | 拡散率から密度 |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | 拡散率から比熱 |
