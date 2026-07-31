# 力積

パッケージ: `org.pcsoft.framework.kunit.mechanic.momentum`
基本単位: **キログラムメートル毎秒**(`KMomentumUnit.BASE`)、 **ニュートン秒**として読み取り
(`KMomentumUnit.NEWTON_SECOND`)

種別: **構成された単位**

力積 `J = F · t` は、力がその作用時間にわたって物体に与える運動量です。次元的には[運動量](momentum.md)
*そのもの*です: `1 N·s = 1 kg·m/s`。したがってKUnitはこのために2つ目の単位グループを導入 **しません**
— 力積は運動量グループの **読み取り方**であり、`newtonSeconds` トークンで表現されます。このページはその
読み取り方を文書化しています。グループ自体については[運動量](momentum.md)のページで説明されています。

!!! note "同じグループ、2つの読み取り方"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` は `true` です。トークンの選択は値の
読み方を変えるだけで、それが何であるかは変わりません。「力 × 時間」で考えるときは `newtonSeconds`
を、「質量 × 速度」で考えるときは `kilogramMetersPerSecond` を使用してください。

## 名前付き単位

| 単位                     | 記号      |                   トークン | kg·m/sでの1単位 |
|--------------------------|-----------|---------------------------:|----------------:|
| ニュートン秒             | `N*s`     |            `newtonSeconds` |             1.0 |
| キログラムメートル毎秒   | `kg*m/s`  |  `kilogramMetersPerSecond` |             1.0 |
| グラムセンチメートル毎秒 | `g*cm/s`  | `gramCentimetersPerSecond` |            1e-5 |
| ポンドフィート毎秒       | `lb*ft/s` |       `poundFeetPerSecond` |      ≈ 0.138255 |

すべてのトークンに接頭辞付き形式が存在します (`kilo.newtonSeconds` = kN·s、`milli.newtonSeconds` = mN·s)。

## 力積の計算

| 式                | 結果の型                | 意味                  |
|-------------------|-------------------------|-----------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`           |
| `time * force`    | `KMomentumUnitInstance` | 同じ、可換            |
| `impulse / time`  | `KForceUnitInstance`    | 平均力 `F = J / t`    |
| `impulse / force` | `KTimeUnitInstance`     | 作用時間 `t = J / F`  |
| `impulse / mass`  | `KSpeedUnitInstance`    | 速度変化 `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0(同一の次元)
```

## 実例: ロケット段の燃焼

模型ロケットモーターは平均12 Nの推力を1.6秒間出力します。生成される全力積はいくつで、0.8 kgのロケット にどのような速度変化を与えるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s"(グループの基本単位)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                                   | 意味                                   |
|--------------|------------------------------------------|----------------------------------------|
| `N·s`        | `newtonSeconds`                          | 力積(運動量グループの名前付きトークン) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | 同じ量を基本次元で                     |
| `J = F · t`  | `force * time`                           | 型付き分解表現                         |
| `F = J / t`  | `impulse / time`                         | 平均力について解く                     |
| `Δv = J / m` | `impulse / mass`                         | 質量の速度変化                         |
| `kN·s`       | `kilo.newtonSeconds`                     | 接頭辞付きの力積                       |
