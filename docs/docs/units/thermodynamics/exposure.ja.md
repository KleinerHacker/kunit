# 照射線量（電離線量）

パッケージ: `org.pcsoft.framework.kunit.electric.specificcharge`
基本単位: **クーロン毎キログラム**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

種別: **構成単位（constructed unit）**

照射線量 `X` — 古典的な**電離線量**は、単位空気質量あたりに解放される電荷によって電離放射線を
測定します: `X = Q / m`、単位は `C/kg`。その歴史的な単位は**レントゲン**です
（1 R = 2.58 × 10⁻⁴ C/kg）。

その次元は `current · time · mass⁻¹` であり — 粒子の[比電荷](../electrical/specificcharge.ja.md)
と**同じ**です。KUnitは両方の読み方に対して1つのグループをモデル化しており、照射線量はその
1つです。このページはその読み方を説明します。

## 照射線量が独自の型を持たない理由

KUnit は意図的に、独立した `KExposureUnitInstance` ではなく `KSpecificChargeUnitInstance` で
照射線量をモデル化しています。理由はこのライブラリの形状認識契約にあります:

* 標準化された各グループには **唯一**の正準の基本次元正規形があり、
* `toX()` は正確にその形のみを認識します。

照射線量と比電荷は正規形 `current¹ · time¹ · mass⁻¹` を共有しています。1つの正規形に対して2つの
型があると、ネイティブ表現が曖昧になってしまいます — `toSpecificCharge()` と仮の
`toExposure()` はどちらも同じ混合単位に一致し、どちらの答えがより正しいということもありません。
1つの型にすることでラウンドトリップが決定的になります。

したがって、この区別は、ライブラリが渡す型の違いではなく、*変数にどんな名前を付けるか*の問題に
すぎません — これはまさに物理学において、両方ともC/kgと書かれるのと同じです。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## 実例 — 古い線量計の読み取り

あるペン型線量計は、勤務後に **200 mR** を示しています。SI単位に換算し、線量計が校正されている
1kgの空気中に解放される電荷に換算すると:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## 関連項目

* [比電荷](../electrical/specificcharge.ja.md) — 同じ型を、粒子の性質として読んだもの。
* [吸収線量](absorbed-dose.ja.md)と[等価線量](dose-equivalent.ja.md) — エネルギーに基づく線量。
* [線量率](dose-rate.ja.md) — 時間あたりの線量。
