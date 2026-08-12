# 重量モル濃度

パッケージ: `org.pcsoft.framework.kunit.thermo.molality`
基本単位: **モル毎キログラム** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

種別: **構成単位（constructed unit）**

重量モル濃度 `b` は、**溶媒の質量あたり**にどれだけの物質が溶けているかを表します: `b = n / m`。
体積を基準とする[物質量濃度](concentration.ja.md)とは異なり、重量モル濃度は溶液を加熱しても変化
しません — 溶媒の質量は熱膨張の影響を受けないためです。これにより、凝固点降下や沸点上昇などの
束一的性質にとって、重量モル濃度が最適な量となります。

その正準の基本次元正規形は `substance¹ · mass⁻¹` です。

## 名前付き単位

| 単位                   | 記号      |                    トークン | mol/kgにおける1単位 |
|------------------------|-----------|-------------------------:|-----------------:|
| モル毎キログラム           | `mol/kg`  |       `molesPerKilogram` |              1.0 |
| ミリモル毎キログラム        | `mmol/kg` | `millimolesPerKilogram`  |            0.001 |

すべてのトークンはSI接頭辞を受け付けます（`milli.molesPerKilogram` など）。

## 分解

このグループには1つの分解表現があり、両方の形式は同じ型で値が等しいインスタンスを生成します。
ネイティブ形式は**単位テンプレート**から組み立てられることに注意してください: 質量項を持つ
グループでは、生の混合値はグラム基準の積であり、型付きインスタンスはその値を名前付き単位で
保持します。

| 形式                | 表現                                              |
|--------------------|-----------------------------------------------------|
| 型付き演算子          | `amountOfSubstance / mass`                          |
| ネイティブ（`toX()`）  | `(0.25 of moles / kilo.grams).toMolality()`         |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## グループでの計算

| 式                              | 結果の型                          | 意味                          |
|----------------------------------|-------------------------------------|--------------------------------|
| `amountOfSubstance / mass`      | `KMolalityUnitInstance`             | `b = n / m`                    |
| `molality * mass`               | `KAmountOfSubstanceUnitInstance`    | `n = b · m`                    |
| `amountOfSubstance / molality`  | `KMassUnitInstance`                 | 必要な溶媒の質量                  |
| `1 / molarMass`                 | `KMolalityUnitInstance`             | 純物質の重量モル濃度               |
| `1 / molality`                  | `KMolarMassUnitInstance`            | モル質量に戻る                    |

最後の2つの関係は、重量モル濃度と[モル質量](molar-mass.ja.md)が互いに逆数であることを反映
しています。

## 実例 — 1キログラムの水に何モル入っているか？

水のモル質量は18.015 g/molであるため、1キログラムにはおよそ55.5 molが含まれます — これは
逆数関係が働いている例です:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## 値の意味論

`equals`/`hashCode` は**正規化されたmol/kg値**を比較するため、
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)` となります。`toString()` は
基本単位での値を表示します: `"0.25 mol/kg"`。

## 関連項目

* [物質量濃度](concentration.ja.md) — 同じ考え方を体積あたりで表したもの。
* [モル質量](molar-mass.ja.md) — 逆数の量。
* [熱力学の概要](overview.ja.md)
