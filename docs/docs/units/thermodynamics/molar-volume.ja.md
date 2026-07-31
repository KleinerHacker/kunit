# モル体積

パッケージ: `org.pcsoft.framework.kunit.thermo.molarvolume`
基本単位: **立方メートル毎モル** (`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`)

種別: **構成単位（constructed unit）**

モル体積は物質量あたりの体積です: `volume / amountOfSubstance`(`m³/mol`)。理想気体では すべての物質で同じ値になります (0
°C・100 kPaで22.711 l/mol)。固体や液体の場合は
[モルマス](molar-mass.md)と密度から求まります。

`KMolarVolumeUnitInstance` は正準の正規形 `distance³ · substance⁻¹`(`m³·mol⁻¹`)にちょうど 2つの項からなる
`KMixedUnitInstance` をラップし、常に m³/mol に正規化されます。両方の成分は それぞれの単位群の基本単位で保存されるため、コンポーネントの生の基本単位はそのまま名前付き
基本単位と一致します。

[周期表](../../periodic-table.md)の各元素は、下記の2番目の分解表現を通じて、そのモルマスと 密度からモル体積を導出しています。

## 名前付き単位

| 単位                     | 記号       |                  トークン | m³/molでの1単位 |
|--------------------------|------------|--------------------------:|----------------:|
| 立方メートル毎モル       | `m^3/mol`  |      `cubicMetersPerMole` |             1.0 |
| リットル毎モル           | `l/mol`    |           `litersPerMole` |           0.001 |
| 立方センチメートル毎モル | `cm^3/mol` | `cubicCentimetersPerMole` |          1.0e-6 |

すべての単位はSI接頭辞の全範囲をサポートします (`milli.cubicMetersPerMole`、
`milli.litersPerMole` など)。さらにこのパッケージは定数 `MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464 (m³/mol)
を公開しています。これは標準状態における理想気体のモル体積です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## 実例 — ヘリウムを満たした風船

標準状態で理想気体2モルはどれだけの空間を占めるでしょうか?また、5リットルの風船には 何モル入るでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// 2モルの体積
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// 5リットルの風船には何モル入るか?
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// そして、満たされた風船から測定されたモル体積:
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## 実例 — 水1モルの体積

水のモルマスは18.015 g/mol、密度は1 kg/lです。したがって1モルはおよそ18 cm³ — 大さじ1杯 程度を占めます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## 中核単位での計算

| 式                                | 結果の型                         | 意味                      |
|-----------------------------------|----------------------------------|---------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | モル体積                  |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | モル体積(2番目の分解表現) |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | 総体積                    |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | 総体積(可換)              |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | 含まれる物質量            |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [モルマス](molar-mass.md) |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | モルマス(可換)            |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | 密度                      |

## 分解表現

どの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現                     | 形式                               | 結果                            |
|------------------------------|------------------------------------|---------------------------------|
| `volume / amountOfSubstance` | 型付き演算子                       | `KMolarVolumeUnitInstance` 直接 |
| `molarMass / density`        | 型付き演算子                       | `KMolarVolumeUnitInstance` 直接 |
| `distance³ · substance⁻¹`    | ネイティブ表現 + `toMolarVolume()` | `KMolarVolumeUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// 型付き演算子形式: volume / amount
val typedVolume = (0.018015 of liters) / (1 of moles)

// 型付き演算子形式: molar mass / density
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// ネイティブの基本次元形式(m³·mol⁻¹)、toMolarVolume() によって認識される
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true - すべて 1.8015e-5 m³/mol
```

`toMolarVolume()` は **唯一**の正準の正規形のみを認識します。誤った形は
`IllegalStateException` を投げます。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学          | Kotlin                               | 意味                                  |
|---------------|--------------------------------------|---------------------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | モル体積、基本単位 — 名前付きトークン |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | 同じ量を基本次元で                    |
| `l/mol`       | `litersPerMole`                      | リットル毎モル                        |
| `cm³/mol`     | `cubicCentimetersPerMole`            | 立方センチメートル毎モル              |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | 体積÷物質量からモル体積               |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | モルマス÷密度からモル体積             |
| `V = V_m · n` | `ideal * (2 of moles)`               | モル体積×物質量から体積               |
| `n = V / V_m` | `(5 of liters) / ideal`              | 体積÷モル体積から物質量               |
| `ρ = M / V_m` | `molarMass / molarVolume`            | モルマス÷モル体積から密度             |
