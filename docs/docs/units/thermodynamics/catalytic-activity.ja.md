# 触媒活性

パッケージ: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
基本単位: **カタール** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

種別: **構成単位（constructed unit）**

酵素製剤の触媒活性 `z` は、**時間あたり**にどれだけの基質を変換するかを表します:
`z = n / t`。そのSI単位は**カタール**（1 kat = 1 mol/s）ですが、これは非常に大きな単位のため、
実務ではマイクロカタール、あるいは伝統的な**酵素単位** `U`（1分あたり1マイクロモル）が使われます。

その正準の基本次元正規形は `substance¹ · time⁻¹` です。

## 名前付き単位

| 単位   | 記号  |         トークン |          1単位あたりのkat |
|-------|--------|--------------:|-----------------------:|
| カタール  | `kat`  |      `katals` |                    1.0 |
| 酵素単位 | `U`    | `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min であり、1 kat = 60,000,000 U、1 U ≈ 16.67 nkat です。すべてのトークンはSI接頭辞を
受け付けます（`micro.katals`、`nano.katals` など）。

## 分解

このグループには1つの分解表現があり、両方の形式は同じ型で値が等しいインスタンスを生成します:

| 形式                | 表現                                                                   |
|--------------------|------------------------------------------------------------------------|
| 型付き演算子          | `amountOfSubstance / time`                                             |
| ネイティブ（`toX()`）  | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## グループでの計算

| 式                                        | 結果の型                          | 意味                  |
|-------------------------------------------|----------------------------------|-----------------------|
| `amountOfSubstance / time`                | `KCatalyticActivityUnitInstance` | `z = n / t`           |
| `catalyticActivity * time`                | `KAmountOfSubstanceUnitInstance` | `n = z · t`           |
| `amountOfSubstance / catalyticActivity`   | `KTimeUnitInstance`              | かかる時間              |

## 実例 — 酵素アッセイ

あるアッセイは**10秒**で**0.5 mmol**の基質を変換します。両方の書き方で表現し、より少量のバッチに
かかる時間も求めます:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## 値の意味論

`equals`/`hashCode` は**正規化されたkat値**を比較するため、`(1 of katals) == (1000 of milli.katals)` と
なります。`toString()` は基本単位での値を表示します: `"5.0E-5 kat"`。

## 関連項目

* [物質量](amount-of-substance.ja.md) — 分子。
* [物質量濃度](concentration.ja.md) — アッセイが通常測定するもの。
* [熱力学の概要](overview.ja.md)
