# 吸収線量（グレイ）

パッケージ: `org.pcsoft.framework.kunit.thermo.specificenergy`
基本単位: **ジュール毎キログラム**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

種別: **構成単位（constructed unit）**

吸収線量 `D` は、電離放射線が単位質量あたりに与えるエネルギーです: `D = E / m`。その単位は
**グレイ**であり、`1 Gy = 1 J/kg` — [比エネルギー](specific-energy.ja.md)と**次元的に同一**です。

## グレイが独自の型を持たない理由

KUnit は意図的に、独立した `KAbsorbedDoseUnitInstance` ではなく `KSpecificEnergyUnitInstance` で吸収線量を
モデル化しています。理由はこのライブラリの形状認識契約にあります:

* 標準化された各グループには **唯一**の正準の基本次元正規形があり、
* `toX()` は正確にその形のみを認識します。

吸収線量と比エネルギーは正規形 `length² · time⁻²` を共有しています。1つの正規形に対して2つの型があると、
ネイティブ表現が曖昧になってしまいます — `toSpecificEnergy()` と仮の `toAbsorbedDose()` はどちらも同じ混合単位に
一致し、どちらの答えがより正しいということもありません。1つの型にすることでラウンドトリップが決定的になります。

したがって、この区別は、ライブラリが渡す型の違いではなく、*変数にどんな名前を付けるか*の問題にすぎません —
これはまさに物理学において、グレイがジュール毎キログラムそのもの **である**のと同じです。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## 実例 — 胸部X線検査

胸部X線撮影ではおよそ **0.1 mGy** が照射されます。体重70kgの人ではこれは合計何ジュールになり、
1年間の自然放射線バックグラウンド（≈ 2.4 mGy）と比べてどうなるでしょうか？

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## 関連項目

* [比エネルギー](specific-energy.ja.md) — 同じ型を、エネルギー密度として読んだもの。
* [等価線量](dose-equivalent.ja.md) — 生物学的影響を重み付けしたシーベルト。
* [線量率](dose-rate.ja.md) — 時間あたりの線量。こちらは独自の型を持ちます。
* [照射線量](exposure.ja.md) — 電荷に基づく電離線量。
