# 慣性モーメント

パッケージ: `org.pcsoft.framework.kunit.mechanic.inertia`
基本単位: **キログラムメートル毎秒**(`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

種別: **構成された単位**

慣性モーメント `J` は[質量](mass.md)の回転版です: 物体がその回転の変化にどれだけ強く抵抗するかを表し ます。
**構成された**単位です — 合成 `mass · length²`(`kg·m²`)。

`KInertiaUnitInstance` は、正準の正規形でちょうど2つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KMassUnit.BASE`(グラム)と指数 `+2` の `KDistanceUnit.BASE`(メートル)。このライブラリの
質量成分はグラムに正規化されているため、保存される値は生のグラム基準の成分値で、kg·m² での読み取りは 固定係数で除算されます。

## 名前付き単位

| 単位                     | 記号      |                 トークン | kg·m²での1単位 |
|--------------------------|-----------|-------------------------:|---------------:|
| キログラムメートル毎秒   | `kg*m^2`  |  `kilogramMetersSquared` |            1.0 |
| グラムセンチメートル毎秒 | `g*cm^2`  | `gramCentimetersSquared` |           1e-7 |
| ポンドフィート毎秒       | `lb*ft^2` |       `poundFeetSquared` |    ≈ 0.0421401 |

すべての単位がSI接頭辞の全範囲に対応しています (小型サーボロータ向けの `milli.kilogramMetersSquared`
など)。

## 分解表現

| 形式           | Kotlin                                                  | 結果の型               |
|----------------|---------------------------------------------------------|------------------------|
| 質量 × 面積    | `mass * area`                                           | `KInertiaUnitInstance` |
| ネイティブ表現 | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## 基本単位による計算

| 式                              | 結果の型                       | 意味                                        |
|---------------------------------|--------------------------------|---------------------------------------------|
| `mass * area`, `area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                                |
| `inertia / mass`                | `KAreaUnitInstance`            | 回転半径の二乗 `r² = J / m`                 |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                                |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [角運動量](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [トルク](torque.md) `M = J · α`             |

## 実例: プレス機のはずみ車

ソリッドなはずみ車ディスク (`J = ½ · m · r²`)は質量40 kg、半径0.3 mです。その慣性モーメントはいくつで、 1500
rpmでの角運動量はいくつでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2"(基本単位)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                          | 意味                                       |
|--------------|---------------------------------|--------------------------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | 慣性モーメント、基本単位(名前付きトークン) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | 同じ量を純粋な積で表現                     |
| `J = m · r²` | `mass * area`                   | 型付き分解表現                             |
| `r² = J / m` | `inertia / mass`                | 回転半径の二乗                             |
| `L = J · ω`  | `inertia * angularvelocity`     | 角運動量                                   |
| `M = J · α`  | `inertia * angularacceleration` | トルク                                     |
