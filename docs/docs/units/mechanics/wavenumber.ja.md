# 波数

パッケージ: `org.pcsoft.framework.kunit.common.reciprocallength`
基本単位: **メートルの逆数** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

種別: **構成された単位**

波の波数 `ṽ` は波長の逆数です: `ṽ = 1 / λ` — 単位長さあたりの波の周期数です。分光学では波長の代わりに
これを用います。光子エネルギーに比例するためです。ほぼ常に **毎センチメートル** (`cm⁻¹`、歴史的には
*カイザー* と呼ばれます) で表され、可視光はおよそ 14,000〜25,000 cm⁻¹、赤外指紋領域は 400〜1500 cm⁻¹
です。

その次元は `distance⁻¹` であり、レンズの屈折力である [ディオプター](../optics/dioptre.ja.md) と
**同じ** です。KUnit は両方の読み方に対して1つの中立的なグループ `reciprocallength` をモデル化しており、
波数はそのうちの1つです。このページはその読み方について記述します。

!!! note "1つのグループ、2つの読み方"
    `KReciprocalLengthUnitInstance` は共有される型であるため、波数と屈折力は KUnit の観点からは同じ単位
    です。このグループは中立的な名前 `reciprocallength` を持ち、どちらの読み方もその名前を独占しません。
    値に名前を付けることでそれらを区別してください。

## 名前付き単位

| 単位                    | 記号 |                   トークン | 1単位を m⁻¹ で |
|-----------------------|--------|------------------------:|--------------:|
| メートルの逆数           | `1/m`  |      `reciprocalMeters` |           1.0 |
| センチメートルの逆数      | `1/cm` | `reciprocalCentimeters` |         100.0 |
| カイザー                 | `1/cm` |                `kaysers` |         100.0 |
| ディオプター              | `dpt`  |               `dioptres` |           1.0 |

すべてのトークンは SI 接頭辞を受け入れます (`kilo.reciprocalCentimeters` など)。

## グループでの計算

| 式                          | 結果の型                         | 意味                             |
|-----------------------------|-------------------------------------|-------------------------------------|
| `1 / length`                | `KReciprocalLengthUnitInstance`   | `ṽ = 1 / λ`                         |
| `1 / reciprocalLength`      | `KLengthUnitInstance`             | 波長に戻る                          |
| `reciprocalLength * length` | `Double`                          | 無次元の周期数                      |
| `reciprocalLength + …`      | `KReciprocalLengthUnitInstance`   | 同じ型どうしの加算                   |

ネイティブ形式は `toReciprocalLength()` で変換します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## 実例 — 緑色レーザー光

500 nm のレーザー光線は波数 20,000 cm⁻¹ に変換され、1 mm の経路に収まる周期数もそこから直接求められます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0 (同じ単位、古典的な名前)

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 — 1ミリメートルあたりの波の周期数

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化された m⁻¹ の値** を比較するため、
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)` です。`toString()` は基本単位で値を
表示します: `"2000000.0 1/m"`。

## 関連項目

* [ディオプター](../optics/dioptre.ja.md) — 屈折力として読む同じ型。
* [周波数](../kinematics/frequency.ja.md) — 時間の逆数であり、このグループの時間的な類似物。
* [力学の概要](overview.ja.md)
