# ディオプター（屈折力）

パッケージ: `org.pcsoft.framework.kunit.common.reciprocallength`
基本単位: **毎メートル**（`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`）

種別: **構成単位**

レンズの屈折力 `D` は焦点距離の逆数です: `D = 1 / f`。その単位は **ディオプター** であり、これはまさに毎メートルに一致します —
1 mで焦点を結ぶレンズは1 dpt、0.5 mで焦点を結ぶレンズは2 dptとなります。

その次元は `distance⁻¹` であり、分光学における [波数](../mechanics/wavenumber.md) と **同一** です。KUnitはこの2つの読み方に対して
`reciprocallength` という中立的な1つのグループをモデル化しており、屈折力はその読み方の1つです。このページはその読み方を扱います。

!!! note "1つのグループ、2つの読み方"
    `KReciprocalLengthUnitInstance` は共有される型であるため、KUnitにとっては屈折力と波数は同じ単位です。このグループは
    どちらの読み方の名前も名乗らないよう、中立的な名前 `reciprocallength` を持ちます。値に名前を付けることで区別してください。

## 名前付き単位

| 単位                  | 記号 |                  トークン | 1単位 (m⁻¹) |
|-----------------------|--------|-----------------------:|--------------:|
| 毎メートル      | `1/m`  |     `reciprocalMeters` |           1.0 |
| ディオプター               | `dpt`  |             `dioptres` |           1.0 |
| 毎センチメートル | `1/cm` | `reciprocalCentimeters` |         100.0 |
| ケイザー                | `1/cm` |               `kaysers` |         100.0 |

`dioptres` と `kaysers` はそれぞれ毎メートルと毎センチメートルの別表記であり、独自の単位ではありません。すべてのトークンは
あらゆるSI接頭辞を受け付けます（`milli.dioptres` など）。

## グループでの計算

| 式                       | 結果型                      | 意味                          |
|----------------------------------|-----------------------------------|-----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | 焦点距離への逆変換         |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | 密着させた薄いレンズは屈折力が加算される |
| `reciprocalLength * length`      | `Double`                         | 無次元の値（`m⁻¹ · m`）  |

ネイティブ形式は `toReciprocalLength()` で変換します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## 実例 — 老眼鏡

焦点距離**40 cm**のレンズは `D = 1 / 0.4 m = 2.5 dpt` となります。2枚目の弱いレンズを密着させると屈折力が単純に加算されます —
これはまさに同一型の `+` が行う操作です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // 密着させたレンズ
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 — 合成焦点距離
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたm⁻¹値** を比較するため、
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)` となります。`toString()` は値を基本単位で表示します:
`"2.5 1/m"`。

## 関連項目

* [波数](../mechanics/wavenumber.md) — 分光量として読まれる同じ型。
* [距離](../kinematics/distance.md) — このグループの逆数の元になるグループ。
* [光学の概要](overview.ja.md)
</content>
