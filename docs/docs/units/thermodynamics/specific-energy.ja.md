# 比エネルギー

パッケージ: `org.pcsoft.framework.kunit.thermo.specificenergy`
基本単位: **ジュール毎キログラム** (`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

種別: **構成単位（constructed unit）**

比エネルギーは単位質量あたりのエネルギーです: `energy / mass`(`J/kg`)。文脈によって同じ量は
*比エンタルピー*、*比潜熱*、*発熱量*とも呼ばれます — いずれもこの単位グループを共有します。

`KSpecificEnergyUnitInstance` は正準の正規形 `distance² · time⁻²`(`m²·s⁻²`)にちょうど2つの項からなる
`KMixedUnitInstance` をラップし、常に J/kg に正規化されます。

!!! note "質量の次元は打ち消される"
    `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`。したがって正準の正規形には質量の項がまったく**存在しません**。
    `KMassUnitInstance` に対する演算子のみが、質量グループのグラム基準とこのグループの
    キログラムあたりの定義を橋渡しします。

温度あたりでは[比熱容量](specific-heat-capacity.md)になり、キログラムの代わりにモルあたりでは
[モルエネルギー](molar-energy.md)になります。

## 名前付き単位

| 単位 | 記号 | トークン | J/kgでの1単位 |
|---|---|---:|---:|
| ジュール毎キログラム | `J/kg` | `joulesPerKilogram` | 1.0 |
| カロリー毎グラム | `cal/g` | `caloriesPerGram` | 4184.0 |
| ワット時毎キログラム | `Wh/kg` | `wattHoursPerKilogram` | 3600.0 |
| Btu 毎ポンド | `Btu/lb` | `btusPerPound` | 2326.0 |

すべてがSI接頭辞の全範囲をサポートします(`kilo.joulesPerKilogram`、`mega.joulesPerKilogram`、
`kilo.wattHoursPerKilogram` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val h = 334 of kilo.joulesPerKilogram
h into joulesPerKilogram      // 334_000.0
h into caloriesPerGram        // ≈ 79.83
h into wattHoursPerKilogram   // ≈ 92.78
```

## 実例 — 氷を融かす

水の融解潜熱は334 kJ/kgです。2.5 kgの氷の塊を融かすにはどれだけのエネルギーが必要でしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val latentHeat = 334 of kilo.joulesPerKilogram
val block = 2.5 of kilo.grams

val energy = latentHeat * block     // KEnergyUnitInstance
energy into kilo.joules             // 835.0 kJ
energy into joules                  // 835_000.0 J

// 逆方向: 1 MJでどれだけの氷を融かせるか?
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## 中核単位(エネルギーと質量)での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `energy / mass` | `KSpecificEnergyUnitInstance` | 比エネルギー |
| `specificEnergy * mass` | `KEnergyUnitInstance` | 総エネルギー |
| `mass * specificEnergy` | `KEnergyUnitInstance` | 総エネルギー(可換) |
| `energy / specificEnergy` | `KMassUnitInstance` | 関与する質量 |

## 分解表現

どちらの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `energy / mass` | 型付き演算子 | `KSpecificEnergyUnitInstance` 直接 |
| `distance² · time⁻²` | ネイティブ表現 + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// 型付き演算子形式
val typed = (1 of joules) / (1 of kilo.grams)

// ネイティブの基本次元形式(m²·s⁻²)、toSpecificEnergy() によって認識される
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true - どちらも 1.0 J/kg
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J/kg` | `joulesPerKilogram` | 比エネルギー、基本単位 — 名前付きトークン |
| `m²·s⁻²` | `(meters pow 2) / (seconds pow 2)` | 同じ量を基本次元で |
| `kJ/kg` | `kilo.joulesPerKilogram` | キロジュール毎キログラム |
| `Wh/kg` | `wattHoursPerKilogram` | ワット時毎キログラム(電池のエネルギー密度) |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | エネルギー÷質量から比エネルギー |
| `Q = q · m` | `latentHeat * block` | 比エネルギー×質量からエネルギー |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat` | エネルギー÷比エネルギーから質量 |
