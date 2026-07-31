# 動粘度

パッケージ: `org.pcsoft.framework.kunit.common.diffusivity`
基本単位: **平方メートル毎秒**
(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

種別: **構成された単位**

動粘度 `ν = η / ρ` は[動粘性係数](viscosity.md)を[密度](density.md)で割ったもので、流体中を運動量が
どのように拡散するかを支配する量です。その次元は `長さ²・時間⁻¹`(`m²/s`)です。

これはまさに、熱力学の[熱拡散率](../thermodynamics/thermal-diffusivity.md)と共有される **拡散率**
グループの次元および量そのものです。したがってKUnitはこのために2つ目のグループを導入 **しません**:
動粘度は `KDiffusivityUnitInstance` の **読み取り方**であり、そのためこのグループは `common` に
存在します。このページは力学的な読み取り方を文書化しています。

!!! note "1つのグループ、2つの分野"
`KDiffusivityUnit` は両方の語彙を持ちます: 両分野で共有されるメートル法の読み取り方 (m²/s、mm²/s)
と、動粘度の伝統的な表記であるストークスおよびセンチストークスです。

## 名前付き単位

| 単位                 | 記号    |                     トークン | m²/sでの1単位 |
|----------------------|---------|-----------------------------:|--------------:|
| 平方メートル毎秒     | `m²/s`  |      `squareMetersPerSecond` |           1.0 |
| 平方ミリメートル毎秒 | `mm²/s` | `squareMillimetersPerSecond` |          1e-6 |
| ストークス           | `St`    |                     `stokes` |          1e-4 |
| センチストークス     | `cSt`   |                `centistokes` |          1e-6 |
| 平方フィート毎時     | `ft²/h` |          `squareFeetPerHour` |  ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` は厳密に成立します — 20°Cの水は約1 cStです。すべての単位はSI接頭辞の全範囲を 受け付けるため、
`centi.stokes` はセンチストークスの別の表記です。

## 分解表現

| 形式              | Kotlin                                                      | 結果の型                   |
|-------------------|-------------------------------------------------------------|----------------------------|
| 動粘性係数 / 密度 | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| ネイティブ表現    | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - どちらも1e-6 m²/s
typed into centistokes   // 1.0
```

## 中核となる単位での計算

| 式                                               | 結果の型                   | 意味        |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`, `density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## 実例: 油圧オイルの選定

ある油圧オイルはISO VG 46、すなわち40°Cで46 cSt、密度870 kg/m³と規定されています。これはどのような 動粘性係数に相当するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true (同じ値)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s"(基本単位)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                     | 意味                        |
|-------------|----------------------------|-----------------------------|
| `m²/s`      | `squareMetersPerSecond`    | 動粘度、基本単位            |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | 同じ量を基本次元で          |
| `cSt`       | `centistokes`              | センチストークス(= 1 mm²/s) |
| `ν = η / ρ` | `viscosity / density`      | 型付けされた分解表現        |
| `η = ν · ρ` | `diffusivity * density`    | 動粘性係数について解いた形  |
| `ρ = η / ν` | `viscosity / diffusivity`  | 密度について解いた形        |
