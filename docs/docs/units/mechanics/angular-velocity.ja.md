# 角速度

パッケージ: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
基本単位: **ラジアン毎秒**(`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

種別: **構成された単位**

角速度 `ω` は[速さ](../kinematics/speed.md)の回転版です: 単位時間あたりに掃引される角度です。 **構成された**単位です — 合成
`angle · time⁻¹`(`rad/s`)。

`KAngularVelocityUnitInstance` は、正準の正規形でちょうど2つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KAngleUnit.BASE`(ラジアン)と、指数 `-1` の `KTimeUnit.BASE`(秒)。値は常に rad/s に 正規化されます。

## 角速度の作成

`angle / time` から、または慣用的な回転速度トークンの1つで作成します。単純に合成された表記には意図的に 専用トークンが
**ありません**: `rad/s` は `radians / seconds` で、`°/s` は `degrees / seconds` です。 接頭辞は成分に適用されるため
(`kilo.radians / seconds`)、このグループには独自の接頭辞ビルダーは ありません。

| 単位         | 記号    |               トークン |  rad/sでの1単位 |
|--------------|---------|-----------------------:|----------------:|
| ラジアン毎秒 | `rad/s` |    `radians / seconds` |             1.0 |
| 度毎秒       | `°/s`   |    `degrees / seconds` |           π/180 |
| 毎分回転数   | `rpm`   | `revolutionsPerMinute` | 2π/60 ≈ 0.10472 |
| 毎秒回転数   | `rps`   | `revolutionsPerSecond` |     2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## 基本単位 (角度と時間)による計算

| 式                          | 結果の型                           | 意味                                |
|-----------------------------|------------------------------------|-------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                         |
| `angularvelocity * time`    | `KAngleUnitInstance`               | 掃引角 `φ = ω · t`                  |
| `time * angularvelocity`    | `KAngleUnitInstance`               | 同じ、可換                          |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | 必要な時間 `t = φ / ω`              |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [角加速度](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [角運動量](angular-momentum.md)     |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | 回転動力、[トルク](torque.md)を参照 |

ネイティブ形式も利用できます: 一般エンジンで構築された `angle / time` 式は `toAngularVelocity()` で 変換されます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true - どちらも 0.5 rad/s
```

## 実例: スピンドル回転速度

フライス盤のスピンドルが12,000 rpmで回転しています。工具外周上の点は1秒あたり角度換算でどれだけ移動
し、1回転にはどれだけの時間がかかるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                       | 意味                         |
|-------------|------------------------------|------------------------------|
| `rad/s`     | `radians / seconds`          | 角速度、基本単位(分数形式)   |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | 同じ量を純粋な積で表現       |
| `rpm`       | `revolutionsPerMinute`       | 毎分回転数(名前付きトークン) |
| `ω = φ / t` | `angle / time`               | 型付き分解表現               |
| `φ = ω · t` | `angularvelocity * time`     | 角度について解く             |
| `t = φ / ω` | `angle / angularvelocity`    | 時間について解く             |
