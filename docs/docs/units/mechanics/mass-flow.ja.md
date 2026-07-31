# 質量流量

パッケージ: `org.pcsoft.framework.kunit.mechanic.massflow`
基本単位: **キログラム毎秒**(`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

種別: **構成された単位**

質量流量 `ṁ` は単位時間あたりに運ばれる質量です — [体積流量](../kinematics/volume-flow.md)の質量版 です。 **構成された**
単位です — 合成 `mass · time⁻¹`(`kg/s`)。

`KMassFlowUnitInstance` は、正準の正規形でちょうど2つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KMassUnit.BASE`(グラム)と指数 `-1` の `KTimeUnit.BASE`(秒)。このライブラリの質量成分は
グラムに正規化されているため、保存される値は生のグラム基準の成分値で、kg/s での読み取りは固定係数で 除算されます。

## 名前付き単位

| 単位           | 記号   |             トークン |      kg/sでの1単位 |
|----------------|--------|---------------------:|-------------------:|
| キログラム毎秒 | `kg/s` | `kilogramsPerSecond` |                1.0 |
| グラム毎秒     | `g/s`  |     `gramsPerSecond` |               1e-3 |
| キログラム毎時 | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| トン毎時       | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| ポンド毎秒     | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| ポンド毎時     | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

すべての単位がSI接頭辞の全範囲に対応しています (定量ポンプ向けの `milli.gramsPerSecond` など)。

## 分解表現

質量流量には2つの同等な分解表現があります。どちらも同じ正規化ファクトリーに集約されます。

| 形式            | Kotlin                                         | 結果の型                |
|-----------------|------------------------------------------------|-------------------------|
| 質量 / 時間     | `mass / time`                                  | `KMassFlowUnitInstance` |
| 密度 × 体積流量 | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| ネイティブ表現  | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true - どちらも 2000 kg/s
viaMassTime into kilogramsPerSecond    // 2000.0
```

## 基本単位による計算

| 式                                             | 結果の型                  | 意味                     |
|------------------------------------------------|---------------------------|--------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`              |
| `massflow * time`, `time * massflow`           | `KMassUnitInstance`       | 運ばれた質量 `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | 必要な時間 `t = m / ṁ`   |
| `density * volumeflow`, `volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`              |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`              |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`              |

## 実例: ポンプの吐出量

ポンプが水 (ρ = 998 kg/m³)を15 m³/hで移送しています。t/hでの質量流量はいくつで、8時間でどれだけの質量 が通過するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s"(基本単位)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                          | 意味                                 |
|-------------|---------------------------------|--------------------------------------|
| `kg/s`      | `kilogramsPerSecond`            | 質量流量、基本単位(名前付きトークン) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | 同じ量を純粋な積で表現               |
| `t/h`       | `tonnesPerHour`                 | 産業用の処理量読み取り方             |
| `ṁ = m / t` | `mass / time`                   | 分解表現A                            |
| `ṁ = ρ · Q` | `density * volumeflow`          | 分解表現B                            |
| `Q = ṁ / ρ` | `massflow / density`            | 体積流量について解く                 |
| `mg/s`      | `milli.gramsPerSecond`          | 接頭辞付きの質量流量                 |
