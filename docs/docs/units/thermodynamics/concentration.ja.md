# 物質量濃度（モル濃度）

パッケージ: `org.pcsoft.framework.kunit.thermo.concentration`
基本単位: **モル毎立方メートル** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

種別: **構成単位（constructed unit）**

物質量濃度 `c` は、**溶液の体積あたり**にどれだけの物質が溶けているかを表します: `c = n / V`。
化学ではほぼ常にモル毎リットルで表され、**モル濃度**と呼ばれて `M` と書かれます。臨床検査では
ミリモル毎リットルが使われます。

その正準の基本次元正規形は `substance¹ · length⁻³` です。

## 名前付き単位

| 単位                    | 記号      |                    トークン | mol/m³における1単位 |
|-------------------------|-----------|------------------------:|-----------------:|
| モル毎立方メートル          | `mol/m^3` |    `molesPerCubicMeter` |              1.0 |
| モル毎リットル（モル濃度）   | `mol/l`   |         `molesPerLiter` |             1000 |
| モーラー（`M`）            | `mol/l`   |                 `molar` |             1000 |
| ミリモル毎リットル          | `mmol/l`  |    `millimolesPerLiter` |              1.0 |

`molar` は `molesPerLiter` の別表記であり、独自の単位ではありません。ミリモル毎リットルは
数値的にモル毎立方メートルと同じであることに注意してください — SI基本単位は臨床単位そのもの
です。すべてのトークンはSI接頭辞を受け付けます（`milli.molesPerLiter`、`micro.molar` など）。

## 分解

このグループには1つの分解表現があり、両方の形式は同じ型で値が等しいインスタンスを生成します:

| 形式                | 表現                                                                    |
|--------------------|----------------------------------------------------------------------------|
| 型付き演算子          | `amountOfSubstance / volume`                                               |
| ネイティブ（`toX()`）  | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## グループでの計算

| 式                                      | 結果の型                          | 意味                       |
|------------------------------------------|-----------------------------------|-----------------------------|
| `amountOfSubstance / volume`            | `KConcentrationUnitInstance`      | `c = n / V`                 |
| `concentration * volume`                | `KAmountOfSubstanceUnitInstance`  | `n = c · V`                 |
| `amountOfSubstance / concentration`     | `KVolumeUnitInstance`             | 必要な体積                    |
| `conductivity / concentration`          | `KMolarConductivityUnitInstance`  | `Λ = κ / c`                 |

## 実例 — 血糖値

血液約5 lにおける空腹時血糖 **5.5 mmol/l** は次に相当します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## 値の意味論

`equals`/`hashCode` は**正規化されたmol/m³値**を比較するため、
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)` となります。`toString()` は基本単位での値を
表示します: `"1000.0 mol/m^3"`。

## 関連項目

* [重量モル濃度](molality.ja.md) — 同じ考え方を**質量**あたりで、熱膨張の影響を受けません。
* [物質量](amount-of-substance.ja.md) — 分子。
* [モル体積](molar-volume.ja.md) — 純物質に対する逆数の量。
* [熱力学の概要](overview.ja.md)
