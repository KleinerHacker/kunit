# 角運動量

パッケージ: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
基本単位: **キログラムメートル毎秒毎秒**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

種別: **構成された単位**

角運動量 `L` は[運動量](momentum.md)の回転版であり、回転系の保存量です。 **構成された**単位です — 合成
`mass · length² · time⁻¹`(`kg·m²/s`)。

`KAngularMomentumUnitInstance` は、正準の正規形でちょうど3つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+2` の `KDistanceUnit.BASE`(メートル)、指数 `-1` の
`KTimeUnit.BASE`(秒)。ラジアンは正規形には **現れません** — それは無次元の比だからです。

!!! note "作用量は同じ量です"
**作用量**(エネルギー × 時間)はちょうどこの次元を共有します。そのためジュール秒 (`jouleSeconds`、プランク定数の単位)は
*この*グループのトークンです: `1 J·s = 1 kg·m²/s`。

## 名前付き単位

| 単位                         | 記号       |                          トークン | kg·m²/sでの1単位 |
|------------------------------|------------|----------------------------------:|-----------------:|
| キログラムメートル毎秒毎秒   | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |              1.0 |
| ニュートンメートル秒         | `N*m*s`    |              `newtonMeterSeconds` |              1.0 |
| ジュール秒                   | `J*s`      |                    `jouleSeconds` |              1.0 |
| グラムセンチメートル毎秒毎秒 | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |             1e-7 |

すべての単位がSI接頭辞の全範囲に対応しています (`femto.jouleSeconds`、`milli.jouleSeconds`)。

## 分解表現

角運動量には2つの同等な分解表現があります。どちらも同じ正規化ファクトリーに集約されます。

| 形式                    | Kotlin                                                                          | 結果の型                       |
|-------------------------|---------------------------------------------------------------------------------|--------------------------------|
| 慣性モーメント × 角速度 | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| 運動量 × 腕の長さ       | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| ネイティブ表現          | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true - どちらも 6 kg·m²/s
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## 基本単位による計算

| 式                                       | 結果の型                       | 意味        |
|------------------------------------------|--------------------------------|-------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω` |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | 同じ、可換  |
| `momentum * length`, `length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r` |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J` |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω` |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r` |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p` |

## 実例: 腕を引き込むフィギュアスケーター

スケーターが慣性モーメント4 kg·m²で2 rev/sで回転しています。腕を引き込むと1.6 kg·m²に減少します。 角運動量は保存されるため、新しい回転速度は
`ω = L / J` から求まります。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true(同じ次元)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s"(基本単位)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                                           | 意味                                 |
|-------------|--------------------------------------------------|--------------------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | 角運動量、基本単位(名前付きトークン) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | 同じ量を純粋な積で表現               |
| `J·s`       | `jouleSeconds`                                   | 同じ次元の作用量表記                 |
| `L = J · ω` | `inertia * angularvelocity`                      | 分解表現A                            |
| `L = p · r` | `momentum * length`                              | 分解表現B                            |
| `ω = L / J` | `angularmomentum / inertia`                      | 角速度について解く                   |
| `r = L / p` | `angularmomentum / momentum`                     | 腕の長さについて解く                 |
