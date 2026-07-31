# 運動量

パッケージ: `org.pcsoft.framework.kunit.mechanic.momentum`
基本単位: **キログラムメートル毎秒**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

種別: **構成された単位**

運動量 `p = m · v` は物体の「運動の量」です。 **構成された**単位です — 合成
`mass · length · time⁻¹`(`kg·m/s`)。

`KMomentumUnitInstance` は、正準の正規形でちょうど3つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+1` の `KDistanceUnit.BASE`(メートル)、指数 `-1` の
`KTimeUnit.BASE`(秒)。このライブラリの質量成分はグラムに正規化されているため、保存される値は生の グラム基準の成分値で、kg·m/s
での読み取りは固定係数で除算されます。

!!! note "力積は同じ量です"
**力積** `F · t` はちょうどこの次元 (`1 N·s = 1 kg·m/s`)を持つため、独自のグループではなく *この*
グループに属します — [力積](impulse.md)のページを参照してください。

## 名前付き単位

| 単位                     | 記号      |                   トークン | kg·m/sでの1単位 |
|--------------------------|-----------|---------------------------:|----------------:|
| キログラムメートル毎秒   | `kg*m/s`  |  `kilogramMetersPerSecond` |             1.0 |
| ニュートン秒             | `N*s`     |            `newtonSeconds` |             1.0 |
| グラムセンチメートル毎秒 | `g*cm/s`  | `gramCentimetersPerSecond` |            1e-5 |
| ポンドフィート毎秒       | `lb*ft/s` |       `poundFeetPerSecond` |      ≈ 0.138255 |

すべての単位がSI接頭辞の全範囲に対応しています (`kilo.newtonSeconds`、`milli.kilogramMetersPerSecond`)。

## 分解表現

運動量には2つの同等な分解表現があります。すべて同じ正規化ファクトリーに集約されるため、同じ型付きの 値として等価な結果を生成します。

| 形式            | Kotlin                                                           | 結果の型                |
|-----------------|------------------------------------------------------------------|-------------------------|
| 質量 × 速度     | `mass * speed`                                                   | `KMomentumUnitInstance` |
| 力 × 時間(力積) | `force * time`                                                   | `KMomentumUnitInstance` |
| ネイティブ表現  | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## 基本単位による計算

| 式                             | 結果の型                       | 意味                            |
|--------------------------------|--------------------------------|---------------------------------|
| `mass * speed`, `speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                     |
| `force * time`, `time * force` | `KMomentumUnitInstance`        | 力積 `p = F · t`                |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                     |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                     |
| `momentum / time`              | `KForceUnitInstance`           | 平均力 `F = p / t`              |
| `momentum / force`             | `KTimeUnitInstance`            | 作用時間 `t = p / F`            |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [角運動量](angular-momentum.md) |

## 実例: 自動車の制動

1200 kgの自動車が20 m/sで走行しています。その運動量はいくつで、5秒間で停止させるにはどんな一定の力が 必要でしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true(同じ次元)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s"(基本単位)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                                   | 意味                               |
|-------------|------------------------------------------|------------------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | 運動量、基本単位(名前付きトークン) |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | 同じ量を純粋な積で表現             |
| `N·s`       | `newtonSeconds`                          | 同じ次元の力積表記                 |
| `p = m · v` | `mass * speed`                           | 分解表現A                          |
| `p = F · t` | `force * time`                           | 分解表現B(力積)                    |
| `v = p / m` | `momentum / mass`                        | 速度について解く                   |
| `F = p / t` | `momentum / time`                        | 平均力について解く                 |
