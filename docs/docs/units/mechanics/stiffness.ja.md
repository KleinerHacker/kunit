# 剛性 (ばね定数)

パッケージ: `org.pcsoft.framework.kunit.mechanic.lineforce`
基本単位: **ニュートン毎メートル**(`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

種別: **構成された単位**

剛性 (ばね定数)`k = F / s` は、単位変位あたりに必要な力です。その次元は `質量・時間⁻²`(`N/m`)—
まさに[表面張力](surface-tension.md)の次元です。KUnitは両方の読み取り方に対して1つの中立的なグループ
`lineforce` をモデル化しており、剛性はそのうちの1つです。このページはその読み取り方を文書化して います。

!!! note "1つのグループ、2つの読み取り方"
`KLineForceUnitInstance` は共有される型であるため、KUnitにとって剛性と表面張力は同じ単位です。 このグループは中立的な名前
`lineforce` を持っており、どちらの読み取り方も相手の名前を主張しない ようになっています。値に付ける名前で区別してください。

## 名前付き単位

| 単位                     | 記号     |               トークン | N/mでの1単位 |
|--------------------------|----------|-----------------------:|-------------:|
| ニュートン毎メートル     | `N/m`    |      `newtonsPerMeter` |          1.0 |
| ニュートン毎ミリメートル | `N/mm`   | `newtonsPerMillimeter` |       1000.0 |
| キロポンド毎メートル     | `kp/m`   |    `kilopondsPerMeter` |      9.80665 |
| ポンド重毎インチ         | `lbf/in` |   `poundsForcePerInch` |    ≈ 175.127 |
| ダイン毎センチメートル   | `dyn/cm` |   `dynesPerCentimeter` |         1e-3 |

ばねのデータシートはN/mmで表記されます。キロニュートン毎メートルは接頭辞付きの形式
`kilo.newtonsPerMeter` であり、数値的にはN/mmと同じです。

## 中核となる単位での計算

| 式                                         | 結果の型                 | 意味                                       |
|--------------------------------------------|--------------------------|--------------------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                                |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | ばね力 `F = k · s`                         |
| `force / lineforce`                        | `KLengthUnitInstance`    | 変位 `s = F / k`                           |
| `energy / area`                            | `KLineForceUnitInstance` | [表面張力](surface-tension.md)の読み取り方 |

ネイティブ形式は `toLineForce()` で変換されます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - どちらも1 N/m
typed into newtonsPerMeter // 1.0
```

## 実例: サスペンションのコイルスプリング

コイルスプリングは40 N/mmと規定されています。2000 Nのホイール荷重でどれだけ圧縮され、15 mmの変位は どの力を生み出すでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// 並列のばねは単純に加算される
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m"(基本単位)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                          | 意味                         |
|-------------|---------------------------------|------------------------------|
| `N/m`       | `newtonsPerMeter`               | 剛性、基本単位               |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | 同じ量を基本次元で           |
| `N/mm`      | `newtonsPerMillimeter`          | ばねデータシートの読み取り方 |
| `k = F / s` | `force / length`                | 型付けされた分解表現         |
| `F = k · s` | `lineforce * length`            | ばね力                       |
| `s = F / k` | `force / lineforce`             | 変位                         |
