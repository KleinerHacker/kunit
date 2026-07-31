# トルク

パッケージ: `org.pcsoft.framework.kunit.common.energy`
基本単位: **ジュール**(`KEnergyUnit.BASE == KEnergyUnit.JOULE`)、 **ニュートンメートル**(`N·m`)として 読み取り

種別: **構成された単位**

トルク `M = F · r` は、腕の長さで作用する力の回転効果です。次元的には[エネルギー](energy.md)*そのもの*
です: `1 N·m = 1 J`。したがってKUnitはこのために2つ目の単位グループを導入 **しません** — トルクは エネルギーグループの
**読み取り方**です。このページはその読み取り方を文書化しています。グループ自体に ついては[エネルギー (力学)](energy.md)
のページで説明されています。

!!! note "同じ次元、異なる物理量"
トルクと仕事は物理的には異なります (トルクは軸性ベクトル、仕事はスカラー)が、次元 `kg·m²·s⁻²` を 正確に共有します。KUnitは
*単位*をモデル化するのであってベクトル性をモデル化するわけではないため、
両方とも1つのグループに属します。命名で区別してください:
`val torque = (100 of newtons) * (2 of meters)` はN·mとして読み、経路に沿った
`val work = force * distance` はJとして読みます。

## トルクの作成

| 式                                 | 結果の型                           | 意味                                |
|------------------------------------|------------------------------------|-------------------------------------|
| `force * length`, `length * force` | `KEnergyUnitInstance`              | `M = F · r`(腕の長さ)               |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α`(回転版ニュートンの法則) |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω`(駆動系の公式)           |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`                         |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`                         |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`                         |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`                         |

3つの作成形式はすべてエネルギーグループの単一のファクトリーに集約されるため、値として等価です:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## 名前付き単位

トルクはエネルギーグループのトークンを使用します。`newtons * meters` が慣用的なN·m表記で、接頭辞付きの
読み取り方はエネルギーのトークンから得られます (`kilo.joules` = kN·m)。

| 読み取り方             | 記号   | Kotlin                           |
|------------------------|--------|----------------------------------|
| ニュートンメートル     | `N*m`  | `(1 of newtons) * (1 of meters)` |
| ジュール(同じ次元)     | `J`    | `joules`                         |
| キロニュートンメートル | `kN*m` | `kilo.joules`                    |

## 実例: エンジンのトルクと出力

エンジンが3000 rpmで62.83 kWを出力します。トルクはいくつでしょうか? また、同じトルクが6000 rpmで維持 された場合、出力はいくつになるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0(N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J"(グループの基本単位)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                                           | 意味                         |
|-------------|--------------------------------------------------|------------------------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | トルク、腕の長さ形式         |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 同じ量を基本次元で           |
| `M = F · r` | `force * length`                                 | 分解表現A                    |
| `M = J · α` | `inertia * angularacceleration`                  | 分解表現B                    |
| `M = P / ω` | `power / angularvelocity`                        | 分解表現C(駆動系)            |
| `P = M · ω` | `torque * angularvelocity`                       | 回転動力                     |
| `kN·m`      | `kilo.joules`                                    | 接頭辞付きのトルク読み取り方 |
