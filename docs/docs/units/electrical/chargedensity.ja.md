# 電荷密度

パッケージ: `org.pcsoft.framework.kunit.chargedensity`
基本単位: **クーロン毎立方メートル**(`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

種別: **構成された単位**

(体積)電荷密度は**構成された**単位で、合成 `current¹ · time¹ · length⁻³`(`A·s·m⁻³` = `C/m³`)です。
`KChargeDensityUnitInstance` は3つの項 — 指数 `+1` の `KElectricCurrentUnit.BASE`(アンペア)、指数 `+1` の
`KTimeUnit.BASE`(秒)、指数 `-3` の `KDistanceUnit.BASE`(メートル) — をラップします。すべての成分が各グループの
基本単位で保存されるため、保存値はそのまま C/m³ での読み値になります。

## 電荷密度の作成

電荷密度には**素のトークンも接頭辞ビルダーもありません** — すべての表記(C/m³、mC/cm³ など)は比です。式として、
または型付きの `charge / volume` 演算子で作成し、そのような式に対して `into` で読み戻します。接頭辞は成分の
トークン(`milli.coulombs`、`centi.meters`)から得られます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance、3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003(= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## 複数の分解

電荷密度は**同等な複数の分解**から得られ、いずれも値の等しい電荷密度を生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `charge / volume` | `KChargeDensityUnitInstance` | 定義 `ρ = Q / V` |
| `current·time/length³` | `.toChargeDensity()` 経由 | ネイティブな標準形 `A·s·m⁻³` の式 |

型付き演算子形式は電荷密度を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toChargeDensity()` で絞り込みます(標準形のみを認識し、それ以外は `IllegalStateException` を投げます)。
どちらの経路も値は等しくなります。

逆演算子は電荷・体積・電荷密度を結び付けます:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V` |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ`(可換) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.chargedensity.*

// 実例 - 電解液中の空間電荷: 4 リットルの電解液に溶けた正味 12 mC の電荷は、電荷密度 3 C/m³ を与えます。
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance、3 C/m³

// 同じ電荷密度をネイティブな A·s·m⁻³ の式で:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// 4 リットルに含まれる電荷へ、そして 12 mC を保持する体積へ戻す:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance(グループから外れる)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `C/m³` | `coulombs / (meters pow 3)` | 電荷密度、基本単位（立方メートルあたりクーロン）— 分数形式 |
| `C·m⁻³` | `coulombs * (meters pow -3)` | 同じ電荷密度を負の指数の積で表現 |
| `A·s/m³` | `amperes * seconds / (meters pow 3)` | ネイティブな標準形（電流·時間 / 長さ³） |
| `mC/cm³` | `milli.coulombs / (centi.meters pow 3)` | 立方センチメートルあたりミリクーロン |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | 電荷 ÷ 体積 で構築 |
