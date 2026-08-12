# モル伝導率

パッケージ: `org.pcsoft.framework.kunit.electric.molarconductivity`
基本単位: **シーメンス平方メートル毎モル**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

種別: **構成された単位**

電解質のモル伝導率 `Λ` は、その[電気伝導率](conductivity.ja.md)を[濃度](../thermodynamics/concentration.ja.md)で
正規化したものです: `Λ = κ / c`。濃度を割り出すことで、濃さの異なる溶液を比較可能にします — これは「このビーカーが
どれだけよく電気を通すか」ではなく「この特定のイオンがどれだけよく伝導するか」に答えるものです。

その正規の基本次元標準形は `mass⁻¹ · time³ · current² · substance⁻¹` です。長さの次元は完全に相殺されます:
伝導率が `length⁻³` を寄与し、分母の濃度がさらに `length⁻³` を寄与するためです。

## 名前付き単位

| 単位                             | 記号       |                            トークン | 1単位のS·m²/mol値 |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| シーメンス平方メートル毎モル    | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| シーメンス平方センチメートル毎モル | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

電気化学の表は通常 S·cm²/mol で示されます。SI 形式は通常 milli 接頭辞を付けて書かれます
(`milli.siemensSquareMetersPerMole`)。すべてのトークンはあらゆる SI 接頭辞を受け付けます。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。ネイティブ形式は
**ユニットテンプレート**から組み立てられます。グループが質量項を持つためです: 生のミックス値はグラム基準の積であり、
型付きインスタンスは名前付き単位で値を保持します。

| 形式             | 式                                                          |
|------------------|---------------------------------------------------------------------|
| 型付き演算子     | `conductivity / concentration`                                      |
| ネイティブ (`toX()`) | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## グループでの計算

| 式                                  | 結果の型                      | 意味       |
|-------------------------------------|-----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | コールラウシュの法則 |

コールラウシュの独立イオン移動の法則は、無限希釈においてモル伝導率がイオンの寄与の**総和**であると述べています —
これはまさにグループの同一型どうしの `+` そのものです。

## 実例 — KCl のコールラウシュの法則

極限イオン伝導率は K⁺ が 7.35 mS·m²/mol、Cl⁻ が 7.63 mS·m²/mol です。それらの和が塩化カリウムの極限モル伝導率であり、
濃度を掛けるとメーターが読み取るであろう伝導率が得られます:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // コールラウシュ
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85(表の値)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたS·m²/mol値**を比較するため、
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)` となります。`toString()` は
値を基本単位で表示します: `"0.0126 S*m^2/mol"`。

## 関連項目

* [電気伝導率](conductivity.ja.md) — 分子。
* [物質量濃度](../thermodynamics/concentration.ja.md) — 分母。
* [コンダクタンス](conductance.ja.md) — メーターが測定する正規化されていない量。
* [電気工学の概要](overview.ja.md)
