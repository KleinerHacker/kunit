# モルマス

パッケージ: `org.pcsoft.framework.kunit.thermo.molarmass`
基本単位: **グラム毎モル** (`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`)

種別: **構成単位（constructed unit）**

モルマスは物質量あたりの質量です: `mass / amountOfSubstance`(`g/mol`)。これはマクロな世界
（天秤上のグラム）と粒子の世界（モル）をつなぐ橋渡しであり、数値的にはある物質の相対原子量 または相対分子量に等しくなります。

`KMolarMassUnitInstance` は正準の正規形 `mass¹ · substance⁻¹`(`g·mol⁻¹`)にちょうど2つの項 からなる `KMixedUnitInstance`
をラップし、常に g/mol に正規化されます。このライブラリは質量を グラムに正規化するため、コンポーネントの生の基本単位はそのまま名前付き基本単位と一致し、
橋渡し係数は不要です。

密度で割ると[モル体積](molar-volume.md)になります。[周期表](../../periodic-table.md)の 各元素はそのモルマスをこの単位群の値として公開しています。

## 名前付き単位

| 単位                   | 記号       |             トークン | g/molでの1単位 |
|------------------------|------------|---------------------:|---------------:|
| グラム毎モル           | `g/mol`    |       `gramsPerMole` |            1.0 |
| キログラム毎モル       | `kg/mol`   |   `kilogramsPerMole` |         1000.0 |
| ポンド毎ポンドモル     | `lb/lbmol` | `poundsPerPoundMole` |            1.0 |
| ダルトン毎エンティティ | `Da`       |   `daltonsPerEntity` |  1.00000000105 |

ポンドモルは、ポンド単位での質量がモルマスに等しくなるよう定義されているため、`lb/lbmol` は 数値的に `g/mol`
と同一になります。2019年のSI再定義以降、モルマス定数は厳密には1 g/molでは なくなったため、ダルトンの係数が生じています。すべての単位はSI接頭辞の全範囲をサポートします
(`kilo.gramsPerMole`、`milli.kilogramsPerMole` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 18.015 Da(1分子あたり)
```

## 実例 — 1モルを計量する

レシピでは0.25 molの食塩 (NaCl、58.44 g/mol)が必要です。何グラム計量すればよいでしょうか? また、500gのパッケージには何モル入っているでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// 0.25 molは何グラムか?
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// 500gのパッケージには何モル入っているか?
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// そしてモルマス自体を、計量したサンプルから求める:
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## 中核単位 (質量と物質量)での計算

| 式                              | 結果の型                         | 意味                        |
|---------------------------------|----------------------------------|-----------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | モルマス                    |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | 総質量                      |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | 総質量(可換)                |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | 含まれる物質量              |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [モル体積](molar-volume.md) |

## 分解表現

どちらの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現                   | 形式                             | 結果                          |
|----------------------------|----------------------------------|-------------------------------|
| `mass / amountOfSubstance` | 型付き演算子                     | `KMolarMassUnitInstance` 直接 |
| `mass · substance⁻¹`       | ネイティブ表現 + `toMolarMass()` | `KMolarMassUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// 型付き演算子形式
val typed = (18.015 of grams) / (1 of moles)

// ネイティブの基本次元形式(g·mol⁻¹)、toMolarMass() によって認識される
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true - どちらも 18.015 g/mol
```

`toMolarMass()` は **唯一**の正準の正規形のみを認識します。誤った形は
`IllegalStateException` を投げます。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学          | Kotlin                               | 意味                                  |
|---------------|--------------------------------------|---------------------------------------|
| `g/mol`       | `gramsPerMole`                       | モルマス、基本単位 — 名前付きトークン |
| `g·mol⁻¹`     | `grams / moles`                      | 同じ量を基本次元で                    |
| `kg/mol`      | `kilogramsPerMole`                   | キログラム毎モル                      |
| `Da`          | `daltonsPerEntity`                   | 基本エンティティあたりのダルトン      |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | 質量÷物質量からモルマス               |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | モルマス×物質量から質量               |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | 質量÷モルマスから物質量               |
| `V_m = M / ρ` | `molarMass / density`                | モルマス÷密度からモル体積             |
