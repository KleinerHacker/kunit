# 体積熱容量

パッケージ: `org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
基本単位: **ジュール毎立方メートルケルビン**
(`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`)

種別: **構成単位（constructed unit）**

体積熱容量 `c_v` は、材料の**体積**あたりケルビンごとにどれだけの熱を蓄えるかを表します:
`c_v = C / V = c · ρ`。これは、建物や貯湯タンク、ヒートシンクが実際にどれだけの熱質量を持つかを決める量です —
密度が異なれば、比熱容量が同じでも2つの材料が蓄える熱量は大きく異なります。

その正準の正規形は `mass · length⁻¹ · time⁻² · temperature⁻¹` です。

## 名前付き単位

| 単位                                | 記号           |                              トークン | J/(m³·K)での1単位 |
|-------------------------------------|----------------|-----------------------------------:|-------------------:|
| ジュール毎立方メートルケルビン       | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |                1.0 |
| カロリー毎立方センチメートルケルビン | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |            4.184e6 |

値が大きいため、実用上はメガジュール形式が便利です: 水はおよそ 4.18 MJ/(m³·K) です。すべてのトークンは
あらゆるSI接頭辞を受け付けます (`mega.joulesPerCubicMeterKelvin` など)。

## 分解表現

このグループには**2つ**の分解表現があります。両方とも同じ正規化ファクトリに集約されるため、
同じ値として等しい型付きインスタンスを生成します:

| 形式                | 式                                                                 |
|---------------------|---------------------------------------------------------------------|
| 型付き演算子 A      | `heatCapacity / volume`                                          |
| 型付き演算子 B      | `specificHeatCapacity * density`                                 |
| ネイティブ (`toX()`) | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## グループでの計算

| 式                                              | 結果の型                                | 意味                    |
|--------------------------------------------------|------------------------------------------|-------------------------|
| `heatCapacity / volume`                          | `KVolumetricHeatCapacityUnitInstance`     | `c_v = C / V`           |
| `specificHeatCapacity * density`                 | `KVolumetricHeatCapacityUnitInstance`     | `c_v = c · ρ`           |
| `volumetricHeatCapacity * volume`                | `KHeatCapacityUnitInstance`               | `C = c_v · V`           |
| `heatCapacity / volumetricHeatCapacity`          | `KVolumeUnitInstance`                     | 対応する体積            |
| `volumetricHeatCapacity / density`               | `KSpecificHeatCapacityUnitInstance`       | `c` に戻る              |
| `volumetricHeatCapacity / specificHeatCapacity`  | `KDensityUnitInstance`                    | `ρ` に戻る              |

## 実例 — 貯湯タンクの熱質量

**300 l** の水の貯湯タンク: 1 K 上げるのにどれだけのエネルギーが必要で、同じ体積のコンクリート
(≈ 2.0 MJ/(m³·K)) と比べてどうなるでしょうか。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09倍の熱質量
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたJ/(m³·K)の値**を比較するため、
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)` となります。`toString()` は
基本単位での値を表示します: `"4184000.0 J/(m^3*K)"`。

## 関連項目

* [熱容量](heat-capacity.ja.md) — 正規化されていない量。
* [比熱容量](specific-heat-capacity.ja.md) — 体積ではなく**質量**あたりの同じ考え方。
* [熱力学の概要](overview.ja.md)
