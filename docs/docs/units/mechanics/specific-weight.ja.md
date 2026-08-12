# 比重量

パッケージ: `org.pcsoft.framework.kunit.mechanic.specificweight`
基本単位: **ニュートン毎立方メートル**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

種別: **構成された単位**

比重量 `γ` は、単位体積あたりの **重量力** です: `γ = F / V = ρ · g`。これは水理学で用いられる量です
— ある深さでの圧力は単に `p = γ · h` であり、土木工学では土壌や建設材料に対して用いられます。水は約
9.81 kN/m³ です。

その正準の基本次元標準形は `mass · length⁻² · time⁻²` です。

!!! note "質量ではなく重量"
    比重量は現地の重力加速度に依存しますが、[密度](density.ja.md) は依存しません。月では材料は密度を
    保ちますが、比重量はおよそ6分の1になります。

## 名前付き単位

| 単位                       | 記号     |                     トークン | 1単位を N/m³ で |
|----------------------------|------------|--------------------------:|---------------:|
| ニュートン毎立方メートル     | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| キロニュートン毎立方メートル | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| ポンド重毎立方フィート       | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

すべてのトークンは SI 接頭辞を受け入れます。隣接する力、圧力、密度のグループと同様に、インスタンスは
**グラム基準の生の成分値** を保持します。N/m³ での読み取りはこれを1000で除算します。

## 分解

このグループには **2つ** の分解があります。どちらも同じ正規化ファクトリに集約されます:

| 形式               | 式                                                        |
|--------------------|--------------------------------------------------------------|
| 型付き演算子 A     | `force / volume`                                             |
| 型付き演算子 B     | `density * acceleration` (`γ = ρ · g`)                       |
| ネイティブ (`toX()`) | `(1 of kilo.grams / m² / s²).toSpecificWeight()`            |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## グループでの計算

| 式                              | 結果の型                     | 意味                |
|------------------------------------|---------------------------------|-----------------------|
| `force / volume`                  | `KSpecificWeightUnitInstance`  | `γ = F / V`           |
| `density * acceleration`          | `KSpecificWeightUnitInstance`  | `γ = ρ · g`           |
| `specificWeight * volume`         | `KForceUnitInstance`           | 重量力                 |
| `force / specificWeight`          | `KVolumeUnitInstance`          | それを満たす体積        |
| `specificWeight / acceleration`   | `KDensityUnitInstance`         | `ρ` に戻る             |
| `specificWeight / density`        | `KAccelerationUnitInstance`    | `g` に戻る             |

## 実例 — 水タンクの重さ

**300 l** の水タンクと、その内容物が床に及ぼす力:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// そして逆に: 1 kN の重さになるのはどのくらいの体積か？
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化された成分値** を比較するため、
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)` です。`toString()` は基本単位で
値を表示します: `"9807.0 N/m^3"`。

## 関連項目

* [密度](density.ja.md) — 重力に依存しない、質量ベースの対応する量。
* [力](force.ja.md) と [圧力](pressure.ja.md) — 隣接するグループ。
* [力学の概要](overview.ja.md)
