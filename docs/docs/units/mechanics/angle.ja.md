# 角度

パッケージ: `org.pcsoft.framework.kunit.mechanic.angle`
基本単位: **ラジアン**(`KAngleUnit.BASE == KAngleUnit.RADIAN`)

種別: **ネイティブ単位**

平面角はKUnitの **ネイティブ**単位です — 独自の単位語彙を持つ、直接測定可能な基本量であり、合成では ありません。
`KAngleUnitInstance` は単一の `KAngleUnit.BASE` 項 (指数1)をラップする `KMixedUnitInstance`
で、常にラジアンに正規化されます。

角度は力学の回転部分全体の基礎です: [角速度](angular-velocity.md)、[角加速度](angular-acceleration.md)、
[角運動量](angular-momentum.md)、そして[立体角](solid-angle.md)はすべてこれを基に構築されています。

## 名前付き単位

| 単位          | 記号  |     トークン |      radでの1単位 |
|---------------|-------|-------------:|------------------:|
| ラジアン      | `rad` |    `radians` |               1.0 |
| 度            | `°`   |    `degrees` | π/180 ≈ 0.0174533 |
| 分(角度)      | `'`   | `arcminutes` |           π/10800 |
| 秒(角度)      | `"`   | `arcseconds` |          π/648000 |
| グラード(gon) | `gon` |   `gradians` |             π/200 |
| 回転(turn)    | `tr`  |      `turns` |       2π ≈ 6.2832 |

すべての単位がSI接頭辞の全範囲に対応しています (`milli.radians`、天体測定用の `micro.arcseconds` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## 角度での計算

| 式                               | 結果の型                       | 意味                |
|----------------------------------|--------------------------------|---------------------|
| `angle + angle`, `angle - angle` | `KAngleUnitInstance`           | 同一型の演算        |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | 立体角(`rad² = sr`) |
| `angle / time`                   | `KAngularVelocityUnitInstance` | 角速度 `ω = φ / t`  |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | 回転にかかる時間    |
| `angle / angle`                  | `KMixedUnitInstance`           | 無次元比            |

三角関数は値に対して直接利用できます。これはラジアンでの読み取り値を使用するためです:
`angle.sin()`、`angle.cos()`、`angle.tan()`。

## 実例: 減速機の出力角度

モーター軸が3回転します。減速比5:1のギアペアがこれを減速します。度で表した出力角度はいくつで、 600 rpm
でこの動きにどれだけの時間がかかるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance, 0.6 turns
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true(値ベースの等価性)
(90 of degrees).sin()                       // 1.0
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad"(基本単位)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学            | Kotlin                      | 意味                           |
|-----------------|-----------------------------|--------------------------------|
| `rad`           | `radians`                   | 平面角、基本単位               |
| `°`             | `degrees`                   | 度                             |
| `mrad`          | `milli.radians`             | 接頭辞付きの角度(ミリラジアン) |
| `1 tr = 2π rad` | `(1 of turns) into radians` | 1回転をラジアンで              |
| `ω = φ / t`     | `angle / time`              | 角度からの角速度               |
| `Ω = φ²`        | `angle * angle`             | 2つの平面角からの立体角        |
