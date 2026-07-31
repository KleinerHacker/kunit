# 周期表

パッケージ: `org.pcsoft.framework.kunit`
型: `KChemicalElement`, `KChemicalElementCategory`

`KChemicalElement` は化学元素の中心的な置き場所です。これは単純なKotlinのenumであるため、
すべての元素はコンパイル時定数であり、それが保持する物理定数もすべてこのライブラリの **型付き単位インスタンス**
であり、他のあらゆるものと組み合わせる準備ができています。

## 対象範囲

このenumは学校で扱う標準的な周期表、すなわち **f-blockを除く第1〜6周期**の主族・副族を カバーしています。したがってランタノイド
(57〜71)は含まれておらず、原子番号はバリウム (56)
からハフニウム (72)へ飛びます。アクチノイドや超アクチノイドも含まれません。合計71エントリと なります。

## 位置に関するデータ

| プロパティ      | 型                         | 意味                                           |
|-----------------|----------------------------|------------------------------------------------|
| `ordinalNumber` | `Int`                      | 原子番号Z、周期表におけるインデックス          |
| `symbol`        | `String`                   | 元素記号、例: `"Pb"`                           |
| `fullName`      | `String`                   | 英語名、例: `"Lead"`(enumエントリは `LEAD`)    |
| `period`        | `Int`                      | 周期(行)、1〜6                                 |
| `mainGroup`     | `Int?`                     | s/pブロック元素の主族1〜8、遷移金属では `null` |
| `subGroup`      | `Int?`                     | dブロック元素の副族1〜8、それ以外は `null`     |
| `category`      | `KChemicalElementCategory` | 化学ファミリー                                 |

`mainGroup` と `subGroup` のどちらか一方だけが設定されます。副族は古典的な番号付けを 使用します (Cu = 1、Zn = 2、Sc = 3 …
Fe/Co/Ni = 8)。

`KChemicalElementCategory` には `HYDROGEN`、`ALKALI_METAL`、`ALKALINE_EARTH_METAL`、
`TRANSITION_METAL`、`POST_TRANSITION_METAL`、`METALLOID`、`NONMETAL`、`HALOGEN`、
`NOBLE_GAS` のエントリがあります。

## 単位データ

| プロパティ              | 型                                    | 利用可否フラグ             |
|-------------------------|---------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`              | 常に存在                   |
| `molarVolume`           | `KMolarVolumeUnitInstance?`           | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`                | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`                | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`               | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`           | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`           | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?`  | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`   | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`                | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`           | `hasElectricalResistivity` |
| `electronegativity`     | `Double?`(ポーリングスケール、無次元) | `hasElectronegativity`     |

意味を持つ形で定義されない定数は `null` になります — ヘリウムは常圧下で融点を持たず、
ヒ素は沸騰せず昇華し、アスタチンは希少すぎて密度が測定されていません。対応する
`has...` プロパティは、null処理を行わずに同じ問いに答えます。

`molarVolume` は `molarMass / density` から導出されます。つまり、
[モル体積](units/thermodynamics/molar-volume.md)群の2番目の分解表現を使用しています。

## 実例 — 金の延べ棒はどれくらい重いか?

標準的な金の延べ棒は7 cm × 4 cm × 2 cmです。その重さは?また、それは金何モルに 相当するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## 実例 — 銅のフライパンを加熱する

1.2 kgの銅のフライパンを20 °Cから200 °Cまで加熱するのにどれだけのエネルギーが 必要でしょうか?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## 検索

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON(大文字小文字を区別しない)
KChemicalElement.ofFullName("iron")    // IRON(大文字小文字を区別しない)
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null - ランタノイドはこの表に含まれない
KChemicalElement.ofMainGroup(4, 6)     // LEAD(主族4、周期6)
KChemicalElement.ofSubGroup(8, 4)      // IRON(副族8、周期4 — Fe/Co/Niの最初の元素)
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

副族8は1周期あたり3つの元素を保持します。`ofSubGroup` は最初の1つ (Fe、Ru、Os)を 返します — すべてを取得するには `ofPeriod`
を使ってフィルタリングしてください。

## 記法

| 数学          | Kotlin                                         | 意味                    |
|---------------|------------------------------------------------|-------------------------|
| `Z`           | `element.ordinalNumber`                        | 原子番号                |
| `M`           | `element.molarMass`                            | モルマス、`g/mol`       |
| `V_m = M / ρ` | `element.molarVolume`                          | モル体積、`m³/mol`      |
| `ρ`           | `element.density`                              | 密度                    |
| `T_m`、`T_b`  | `element.meltingPoint`、`element.boilingPoint` | 融点・沸点(K)           |
| `m = ρ · V`   | `gold.density!! * volume`                      | 密度×体積から質量       |
| `n = m / M`   | `mass / gold.molarMass`                        | 質量÷モルマスから物質量 |
