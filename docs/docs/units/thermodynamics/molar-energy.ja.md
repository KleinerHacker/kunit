# モルエネルギー

パッケージ: `org.pcsoft.framework.kunit.thermo.molarenergy`
基本単位: **ジュール毎モル** (`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`)

種別: **構成単位（constructed unit）**

モルエネルギーは物質量あたりのエネルギーです: `energy / amountOfSubstance`(`J/mol`)。文脈によって
同じ量は*モルエンタルピー*、*反応エンタルピー*、*結合エネルギー*とも呼ばれます。

`KMolarEnergyUnitInstance` は正準の正規形 `mass¹ · distance² · time⁻² · substance⁻¹`
(`kg·m²·s⁻²·mol⁻¹`)にちょうど4つの項からなる `KMixedUnitInstance` をラップし、常に J/mol に正規化されます。

温度あたりでは[モル熱容量](molar-heat-capacity.md)になり、モルの代わりにキログラムあたりでは
[比エネルギー](specific-energy.md)になります。

## 名前付き単位

| 単位 | 記号 | トークン | J/molでの1単位 |
|---|---|---:|---:|
| ジュール毎モル | `J/mol` | `joulesPerMole` | 1.0 |
| カロリー毎モル | `cal/mol` | `caloriesPerMole` | 4.184 |
| エンティティあたりの電子ボルト | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

エンティティあたりの電子ボルトトークンは*粒子あたり*のエネルギーを*モルあたり*のエネルギーに変換します —
その係数はファラデー定数です。すべての単位はSI接頭辞の全範囲をサポートします
(`kilo.joulesPerMole`、`kilo.caloriesPerMole`、`milli.electronVoltsPerEntity` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV(1分子あたり)
```

## 実例 — 水素の燃焼

液体の水の生成エンタルピーは −286 kJ/mol です。4モルの水素が燃焼するとどれだけのエネルギーが
放出され、1分子あたりではどれだけになるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// 分子あたり、化学者の単位で
formation into electronVoltsPerEntity // ≈ -2.964 eV

// 逆方向: 1 MJはどれだけの物質量に相当するか?
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## 中核単位(エネルギーと物質量)での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | モルエネルギー |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | 総エネルギー |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | 総エネルギー(可換) |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | 関与する物質量 |

## 分解表現

どちらの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `energy / amountOfSubstance` | 型付き演算子 | `KMolarEnergyUnitInstance` 直接 |
| `mass · distance² · time⁻² · substance⁻¹` | ネイティブ表現 + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// 型付き演算子形式
val typed = (1 of joules) / (1 of moles)

// ネイティブの基本次元形式(kg·m²·s⁻²·mol⁻¹)、toMolarEnergy() によって認識される
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true - どちらも 1.0 J/mol
```

`toMolarEnergy()` は**唯一**の正準の正規形のみを認識します。誤った形は
`IllegalStateException` を投げます。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J/mol` | `joulesPerMole` | モルエネルギー、基本単位 — 名前付きトークン |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | 同じ量を基本次元で |
| `kJ/mol` | `kilo.joulesPerMole` | キロジュール毎モル |
| `eV`(粒子あたり) | `electronVoltsPerEntity` | 基本エンティティあたりの電子ボルト |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | エネルギー÷物質量からモルエネルギー |
| `Q = ΔH_m · n` | `formation * hydrogen` | モルエネルギー×物質量からエネルギー |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | エネルギー÷モルエネルギーから物質量 |
