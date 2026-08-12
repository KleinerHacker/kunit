# エラスタンス

パッケージ: `org.pcsoft.framework.kunit.electric.elastance`
基本単位: **逆ファラド**(`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

種別: **構成された単位**

エラスタンス `S = U / Q = 1 / C` は [静電容量](capacitance.ja.md)の正確な逆数です。コンデンサが**直列**に接続される場合に便利な形式であり、
直列のエラスタンスは直列抵抗と同様に単純に加算されます。その単位である逆ファラドは、古くから **daraf**(「farad」を逆から綴ったもの)と呼ばれます。

その正規の基本次元標準形は `mass · length² · time⁻⁴ · current⁻²` です。

## 名前付き単位

| 単位              | 記号    |              トークン | 1単位のF⁻¹値 |
|-------------------|---------|-------------------:|--------------:|
| 逆ファラド        | `1/F`   | `reciprocalFarads` |           1.0 |
| daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` は基本単位の別綴りであり、独自の単位ではありません。すべてのトークンはあらゆる SI 接頭辞を受け付けます
(`mega.reciprocalFarads` など)。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。ネイティブ形式は
**ユニットテンプレート**から組み立てられます。グループが質量項を持つためです。

| 形式             | 式                                                    |
|------------------|----------------------------------------------------------------|
| 型付き演算子     | `voltage / charge`                                            |
| ネイティブ (`toX()`) | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## グループでの計算

| 式                     | 結果の型                     | 意味                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | 蓄えられた電荷          |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | 直列のコンデンサ       |

## 実例 — 直列接続された2つのコンデンサ

直列接続された1mFのコンデンサ2つは、単一の0.5mFのコンデンサのように振る舞います。エラスタンスの観点では、これは単純な加算です:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — 等価な静電容量
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたF⁻¹値**を比較するため、`(1 of reciprocalFarads) == (1 of darafs)` となります。
`toString()` は値を基本単位で表示します: `"1000.0 1/F"`。

## 関連項目

* [静電容量](capacitance.ja.md) — 逆数の量。
* [電圧](voltage.ja.md) と [電荷](charge.ja.md) — 分解の2つのオペランド。
* [電気工学の概要](overview.ja.md)
