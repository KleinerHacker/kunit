# 物質量

パッケージ: `org.pcsoft.framework.kunit.thermo.amountofsubstance`
基本単位: **モル** (`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`)

種別: **ネイティブ単位（native unit）**

物質量はSIの7つの基本量の1つです — 直接測定可能で構成されていない量であり、そのため **ネイティブ単位**
です。`KAmountOfSubstanceUnitInstance` は単純な一次元のラッパーです — `KAmountOfSubstanceUnit.BASE`
(モル)の項を指数1として1つだけ持ち、常にモルに正規化されます。

これは熱力学分野におけるあらゆる *モル*量の基礎です ([モルエネルギー](molar-energy.md)、
[モル熱容量](molar-heat-capacity.md))。

## 名前付き単位

| 単位       | 記号    |     トークン | molでの1単位 |
|------------|---------|-------------:|-------------:|
| モル       | `mol`   |      `moles` |          1.0 |
| ポンドモル | `lbmol` | `poundMoles` |    453.59237 |

どちらもSI接頭辞の全範囲をサポートします (`milli.moles`、`micro.moles`、`kilo.moles` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0(モルに正規化)
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## アボガドロ定数

このグループはアボガドロ定数の正確なSI値を `AVOGADRO_CONSTANT`(6.02214076e23 mol⁻¹)として公開し、 インスタンスの便利メソッド
`particleCount()` も提供します。どちらも単純な `Double` を返します。 粒子数は無次元だからです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24 個の粒子
```

## 実例 — 食塩を溶かす

塩化ナトリウム (モル質量 58.44 g/mol)25 gには何モルが含まれ、それは何個の式量単位に相当するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // NaClの g/mol
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23 個の式量単位
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - : 同じグループ、異なる単位・接頭辞間の自動変換
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// 比較(正規化されたモル値で)
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

物質量を他の量と乗算/除算すると、型付きの結果が存在しない限り汎用の混合単位エンジンに外れます — 例えば
`energy / amountOfSubstance` は型付きの[モルエネルギー](molar-energy.md)です。

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## 記法

以下の表は、この単位が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、
`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。

| 数学          | Kotlin                                | 意味                            |
|---------------|---------------------------------------|---------------------------------|
| `mol`         | `moles`                               | 物質量、基本単位                |
| `mmol`        | `milli.moles`                         | ミリモル                        |
| `kmol`        | `kilo.moles`                          | キロモル                        |
| `lbmol`       | `poundMoles`                          | ポンドモル(帝国工学単位)        |
| `n = m / M`   | `(sample.value / molarMass) of moles` | 質量÷モル質量から物質量         |
| `N = n · N_A` | `n.particleCount()`                   | 物質量×アボガドロ定数から粒子数 |
