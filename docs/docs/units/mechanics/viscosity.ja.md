# 動粘性係数

パッケージ: `org.pcsoft.framework.kunit.mechanic.viscosity`
基本単位: **パスカル秒**(`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

種別: **構成された単位**

動粘性係数 `η` は流体のせん断に対する抵抗を表します。これは **構成された**単位であり、
`圧力・時間` すなわち `質量・長さ⁻¹・時間⁻¹`(`Pa·s`)の組み合わせです。

`KViscosityUnitInstance` は、正規化された形式でちょうど3つの項からなる `KMixedUnitInstance` を ラップします:
`KMassUnit.BASE`(グラム)を指数`+1`で、`KDistanceUnit.BASE`(メートル)を指数`-1`で、
`KTimeUnit.BASE`(秒)を指数`-1`で。このライブラリの質量成分はグラムに正規化されているため、格納
される値は生のグラム基準の成分値であり、Pa·sでの読み取り方は固定係数で割ることで得られます。

!!! note "動粘性係数と動粘度の違い"
**動粘度** `ν = η / ρ`(`m²/s`)は異なる量であり、拡散率グループに属します —
[動粘度](kinematic-viscosity.md)を参照してください。

## 名前付き単位

| 単位                     | 記号         |                         トークン | Pa·sでの1単位 |
|--------------------------|--------------|---------------------------------:|--------------:|
| パスカル秒               | `Pa*s`       |                  `pascalSeconds` |           1.0 |
| ポアズ                   | `P`          |                         `poises` |           0.1 |
| ポンド重秒毎平方フィート | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |     ≈ 47.8803 |
| レイン(lbf·s/in²)        | `reyn`       |                          `reyns` |    ≈ 6894.757 |

水のような流体に対する日常的な2つの表記は接頭辞付きの形式であり、独自のトークンではありません:
**ミリパスカル秒**は `milli.pascalSeconds`、 **センチポイズ**は `centi.poises` であり、両者は等しい です (`1 mPa·s = 1 cP`
、20°Cの水)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## 中核となる単位での計算 (圧力と時間)

| 式                                   | 結果の型                   | 意味               |
|--------------------------------------|----------------------------|--------------------|
| `pressure * time`, `time * pressure` | `KViscosityUnitInstance`   | `η = p · t`        |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`        |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`        |
| `viscosity / density`                | `KDiffusivityUnitInstance` | 動粘度 `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`        |

ネイティブ形式は `toViscosity()` で変換されます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - どちらも6 Pa·s
typed into pascalSeconds   // 6.0
```

## 実例: 運転温度でのエンジンオイル

SAE 30オイルは100°Cで9.3 cP、密度850 kg/m³と測定されます。これはPa·sでいくつで、どの動粘度に相当 するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s"(基本単位)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                                            | 意味                                   |
|--------------|---------------------------------------------------|----------------------------------------|
| `Pa·s`       | `pascalSeconds`                                   | 動粘性係数、基本単位(名前付きトークン) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | 同じ量を純粋な積として                 |
| `cP`         | `centi.poises`                                    | センチポイズ(= 1 mPa·s)                |
| `η = p · t`  | `pressure * time`                                 | 型付けされた分解表現                   |
| `ν = η / ρ`  | `viscosity / density`                             | 動粘度                                 |
| `mPa·s`      | `milli.pascalSeconds`                             | 接頭辞付きの動粘性係数                 |
