# 比電荷

パッケージ: `org.pcsoft.framework.kunit.electric.specificcharge`
基本単位: **クーロン毎キログラム**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

種別: **構成された単位**

比電荷 `q/m` は、物体が単位質量あたりに帯びる電荷です。これは J. J. トムソンが電子を特定するために測定した量であり、
質量分析法が粒子を分離する際に用いる量でもあります。

その正規の基本次元標準形は `current · time · mass⁻¹` です。

!!! note "1つのグループ、2つの読み方"
    同じ次元は放射線防護における**照射線量**(exposure)も表し、歴史的にはレントゲンで測定されてきました —
    [照射線量](../thermodynamics/exposure.ja.md)を参照してください。1つの標準形は1つの型に対応するため、
    両方の読み方はこのグループを共有します。レントゲンはその名前付き単位の1つです。値の命名によってそれらを区別してください。

## 名前付き単位

| 単位                 | 記号 |                 トークン | 1単位のC/kg値 |
|----------------------|--------|----------------------:|---------------:|
| クーロン毎キログラム | `C/kg` | `coulombsPerKilogram` |            1.0 |
| レントゲン           | `R`    |            `roentgens` |        2.58e-4 |

すべてのトークンはあらゆる SI 接頭辞を受け付けます(`milli.roentgens` など)。

## 定数

| 定数                         | 値                  | 意味                                     |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | 電子の電荷質量比                     |

符号は省略されています: 電子の電荷は負ですが、この比は大きさとして示されます。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。ネイティブ形式は
**ユニットテンプレート**から組み立てられます。グループが質量項を持つためです。

| 形式             | 式                                               |
|------------------|----------------------------------------------------------|
| 型付き演算子     | `charge / mass`                                         |
| ネイティブ (`toX()`) | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## グループでの計算

| 式                          | 結果の型                     | 意味              |
|-----------------------------|----------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | 全電荷     |
| `charge / specificCharge`   | `KMassUnitInstance`             | 電荷を帯びる質量    |

## 実例 — 電子と、ある照射線量の読み

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// トムソンの比
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// サーベイメーターの照射線量の読み、および1kgの空気中で解放される電荷
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたC/kg値**を比較するため、
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)` となります。`toString()` は値を基本単位で表示します:
`"1.0 C/kg"`。

## 関連項目

* [電荷](charge.ja.md) と [質量](../mechanics/mass.ja.md) — 2つのオペランド。
* [照射線量](../thermodynamics/exposure.ja.md) — 同じ型を照射線量として読んだもの。
* [電気工学の概要](overview.ja.md)
