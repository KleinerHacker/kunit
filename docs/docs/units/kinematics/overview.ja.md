# 運動学 — 概要

パッケージ: `org.pcsoft.framework.kunit.distance`、`…time`、`…speed`、`…acceleration`、`…frequency`

運動学は**運動**の記述です — どれだけ遠く、どれだけ長く、どれだけ速く、そして運動の割合そのものが
どう変化するか — 背後にある力はまだ問いません(それは[力学](../mechanics/overview.md)の話題です)。
KUnit はこの分野を 2 つの**ネイティブ**基本量と、それらから**構成された** 3 つの量でモデル化するため、
古典的な運動の公式が、強く型付けされたまま通常の `*` と `/` の式になります。

## この話題の単位

| 単位 | 種別 | 基準単位 | ページ |
|---|---|---|---|
| 距離 | ネイティブ | メートル(`m`) | [距離](distance.md) |
| 時間 | ネイティブ | 秒(`s`) | [時間](time.md) |
| 周波数 | ネイティブ | ヘルツ(`Hz`) | [周波数](frequency.md) |
| 速度 | 構成 | メートル毎秒(`m/s`) | [速度](speed.md) |
| 加速度 | 構成 | メートル毎秒毎秒(`m/s²`) | [加速度](acceleration.md) |

## 量どうしの関係

速度は距離÷時間、加速度は速度÷時間、周波数は時間の逆数です。KUnit は各組み合わせに対して正しい**型付き**の
量を返します — 生の混合単位を手作業で組み立てる必要はありません。

| 式 | 結果 | 公式 |
|---|---|---|
| `distance / time` | 速度 | `v = s / t` |
| `speed * time` | 距離 | `s = v · t` |
| `speed / time` | 加速度 | `a = Δv / t` |
| `acceleration * time` | 速度 | `v = a · t` |
| `distance * frequency` | 速度 | `v = s · f` |

## 実例 — 移動の平均速度

自動車が **120 km** を **1.5 h** で走ります。平均速度は `v = s / t` であり、その速度に所要時間を掛けると
再び走行距離が得られます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0(km/h)
v.value                                          // ≈ 22.22(m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0(3 h での km)
```

## 実例 — 短距離走者の加速度

短距離走者が静止状態から **2 s** で **10 m/s** に達します。加速度は `a = Δv / t` です:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance、5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance、10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51(g に対する割合)
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(値 + 記号)で出力します。他の単位には `into` を文字列
テンプレート内で使い、記号を自分で付け足します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s"(基準単位)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## 記法

下表は、この分野の中核的な関係を数学表記と KUnit の Kotlin 表記で対比します。指数は Unicode 上付き文字
(`²`、`⁻¹`)、`·` は乗算、`/` は分数を表します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | 距離÷時間から速度 |
| `s = v · t` | `v * (3 of hours)` | 速度×時間から距離 |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | 速度÷時間から加速度 |
| `v = a · t` | `a * (2 of seconds)` | 加速度×時間から速度 |
| `f = 1 / T` | `1 / (2 of hertz)` | 周期↔周波数(時間の逆数) |

## 次に読むもの

* [距離](distance.md) — 長さ・面積・体積を 1 つのグループに。
* [時間](time.md) — `Duration` を基盤とする継続時間。
* [速度](speed.md) と [加速度](acceleration.md) — 構成された運動の割合。
* [周波数](frequency.md) — 時間の逆数と、その相互演算子。
