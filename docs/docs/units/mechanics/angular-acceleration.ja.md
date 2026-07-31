# 角加速度

パッケージ: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
基本単位: **ラジアン毎秒毎秒**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

種別: **構成された単位**

角加速度 `α` は[加速度](../kinematics/acceleration.md)の回転版です: 単位時間あたりの
[角速度](angular-velocity.md)の変化です。 **構成された**単位です — 合成 `angle · time⁻²`(`rad/s²`)。

`KAngularAccelerationUnitInstance` は、正準の正規形でちょうど2つの項をラップする `KMixedUnitInstance`
です: 指数 `+1` の `KAngleUnit.BASE`(ラジアン)と、指数 `-2` の `KTimeUnit.BASE`(秒)。値は常に rad/s² に正規化されます。

## 名前付き単位

| 単位             | 記号      |                        トークン | rad/s²での1単位 |
|------------------|-----------|--------------------------------:|----------------:|
| ラジアン毎秒毎秒 | `rad/s^2` |       `radiansPerSecondSquared` |             1.0 |
| 度毎秒毎秒       | `°/s^2`   |       `degreesPerSecondSquared` |           π/180 |
| 回転毎秒毎秒     | `rps^2`   |   `revolutionsPerSecondSquared` |              2π |
| 毎分回転数毎秒   | `rpm/s`   | `revolutionsPerMinutePerSecond` |           2π/60 |

接頭辞は成分に適用されるため (`kilo.radians / (seconds pow 2)`)、このグループには独自の接頭辞ビルダー はありません。

## 分解表現

角加速度には2つの同等な分解表現があります。どちらも同じ正準値に還元されます。

| 形式           | Kotlin                                                             | 結果の型                           |
|----------------|--------------------------------------------------------------------|------------------------------------|
| 型付き演算子   | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| ネイティブ表現 | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true - どちらも 2 rad/s²
typed into radiansPerSecondSquared     // 2.0
```

## 基本単位による計算

| 式                                      | 結果の型                           | 意味                                          |
|-----------------------------------------|------------------------------------|-----------------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                                   |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | 得られた速度 `ω = α · t`                      |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | 同じ、可換                                    |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | 加速時間 `t = ω / α`                          |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | トルク `M = J · α`、[トルク](torque.md)を参照 |

## 実例: モーターの立ち上がり

サーボモーターは0.4秒で3000 rpmに達します。その角加速度はいくつで、静止状態から0.2秒間加速したときに どれだけ回転しているでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                                                                  | 意味                                 |
|-------------|-------------------------------------------------------------------------|--------------------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | 角加速度、基本単位(名前付きトークン) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | 同じ量を純粋な積で表現               |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | ネイティブの分解表現                 |
| `α = ω / t` | `angularvelocity / time`                                                | 型付き分解表現                       |
| `ω = α · t` | `angularacceleration * time`                                            | 角速度について解く                   |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | 機械の立ち上がり速度                 |
