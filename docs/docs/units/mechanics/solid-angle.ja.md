# 立体角

パッケージ: `org.pcsoft.framework.kunit.mechanic.solidangle`
基本単位: **ステラジアン**(`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

種別: **構成された単位**

立体角は二次元の角度です: 円錐が球の表面から切り取る部分の割合です。 **構成された**単位です — `1 sr = 1 rad²`
— しかし、ステラジアンは独自の語彙 (平方度、スパット)を持つ独立した名前付きSI単位であるため、単一項の
ラッパーを持つ独自のグループとしてモデル化されています。

`KSolidAngleUnitInstance` は単一の `KSolidAngleUnit.BASE` 項 (指数1)をラップする `KMixedUnitInstance` で、
常にステラジアンに正規化されます。[角度](angle.md)グループへの橋渡しは、型付き演算子 `angle * angle` と、 ネイティブの
`rad²` 形式も受け付ける形式認識フック `toSolidAngle()` です。

## 名前付き単位

| 単位           | 記号   |        トークン |           srでの1単位 |
|----------------|--------|----------------:|----------------------:|
| ステラジアン   | `sr`   |    `steradians` |                   1.0 |
| 平方度         | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| スパット(全球) | `sp`   |         `spats` |          4π ≈ 12.5664 |

すべての単位がSI接頭辞の全範囲に対応しています (`milli.steradians`、`micro.steradians`)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96(全天)
```

## 分解表現

立体角は2つの同等な方法で到達できます。どちらも同じ正準値に還元されます。

| 形式           | Kotlin                                  | 結果の型                  |
|----------------|-----------------------------------------|---------------------------|
| 型付き演算子   | `angle * angle`                         | `KSolidAngleUnitInstance` |
| ネイティブ表現 | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true - どちらも (π/2)² sr ≈ 2.4674 sr
typed into steradians      // ≈ 2.4674
```

## 平面角による計算

| 式                        | 結果の型                  | 意味            |
|---------------------------|---------------------------|-----------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | 立体角 `Ω = φ²` |
| `solidangle / angle`      | `KAngleUnitInstance`      | 残りの平面角    |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | 同一型の演算    |

## 実例: LEDのビーム角度

LEDが30° × 30°の正方形ビームで発光します。どの立体角を照らすか、そしてそれは全球のどのくらいの割合 でしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218(球全体の約2.2%)
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr"(基本単位)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学          | Kotlin                                    | 意味                                       |
|---------------|-------------------------------------------|--------------------------------------------|
| `sr`          | `steradians`                              | 立体角、基本単位                           |
| `deg²`        | `squareDegrees`                           | 平方度                                     |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | 平面角の二乗としての立体角(ネイティブ形式) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | 型付き分解表現                             |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | 平面角について解く                         |
| `msr`         | `milli.steradians`                        | 接頭辞付きの立体角                         |
