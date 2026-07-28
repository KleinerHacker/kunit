# 電束密度

パッケージ: `org.pcsoft.framework.kunit.electric.electricfluxdensity`
基本単位: **平方メートルあたりのクーロン**
(`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`)

種別: **構成単位（constructed unit）**

電束密度は**構成単位**です。組成は `電流 · 時間 · 長さ⁻²`
（`A·s·m⁻²` = `C/m²`）です。`KElectricFluxDensityUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします —
`KElectricCurrentUnit.BASE`（アンペア）を `+1`、`KTimeUnit.BASE`（秒）を `+1`、`KDistanceUnit.BASE`
（メートル）を `-2` として保持します。このグループには質量次元が含まれないため、グラム／キログラムの橋渡しは不要です。
格納される値は常に平方メートルあたりのクーロンに正規化されています。

束密度 `D`（電気変位とも呼ばれる）は単位面積あたりの電荷です。**表面電荷密度** `σ` は次元的に同じ量であるため、
別グループではなくこのグループそのもので表現されます。`D` は[誘電率](permittivity.md)を介して
[電界強度](electricfieldstrength.md)と関係します（`D = ε · E`）。一次元の対応物は
[線電荷密度](linearchargedensity.md)、三次元の対応物は[電荷密度](chargedensity.md)です。

## 電束密度を組み立てる

名前付きトークンで、または分解表現（下記参照）から電束密度を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 電束密度 | 記号 | トークン | C/m²での1単位 |
|---|---|---:|---:|
| 平方メートルあたりのクーロン | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| 平方センチメートルあたりのクーロン | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`micro.coulombsPerSquareMeter`、
`milli.coulombsPerSquareMeter` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.fluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // 帯電したコンデンサ板
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## 複数の分解表現

電束密度は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい密度を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`、面積に広がる電荷 |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E`（可換、[誘電率](permittivity.md)参照） |
| `current·time/length²` | `.toElectricFluxDensity()` 経由 | ネイティブの正準 `A·s·m⁻²` 表現 |

型付き演算子形式は直接電束密度を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toElectricFluxDensity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は電荷、面積、束密度を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A`（可換） |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.ares
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electric.fluxdensity.*

// 実例 - 20 µCが4 m²のコンデンサ板に広がると5 µC/m²になる。
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// 同じ束密度をネイティブの A·s·m⁻² 表現として:
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | 電束密度、基本単位（名前付きトークン） |
| `Q / A` | `(20 of micro.coulombs) / plate` | 面積に広がる電荷からの束密度 |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | 誘電率と電界強度からの束密度 |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | 電流・時間 / 長さ² としての束密度（分数形式） |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | 純粋な積としての同じ束密度 |
| `µC/m²` | `micro.coulombsPerSquareMeter` | 接頭辞付き束密度（マイクロクーロン毎平方メートル） |
