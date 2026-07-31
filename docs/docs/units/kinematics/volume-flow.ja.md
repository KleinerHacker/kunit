# 体積流量

パッケージ: `org.pcsoft.framework.kunit.kinematic.volumeflow`
基本単位: **立方メートル毎秒** (`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`)

種別: **構成単位（constructed unit）**

体積流量 (容積流量)は、単位時間あたりに断面を通過する体積を表します:
`距離³ · 時間⁻¹`(`m³/s`)。`KVolumeFlowUnitInstance` はちょうど2つの項からなる `KMixedUnitInstance` を ラップします —
`KDistanceUnit.BASE`(メートル)を指数 `+3`、`KTimeUnit.BASE`(秒)を指数 `-1` として保持します。
値は常に立方メートル毎秒に正規化され、どの単位や体積/時間の組み合わせから作られたかに関わらず一貫します。

エネルギーや電力と異なり、体積流量には質量の次元が **ありません**。したがって格納される値は
`m³/s` での読み値そのものであり、グラム/キログラムへの橋渡しは関与しません。

## 名前付き単位

| 単位             | 記号    |               トークン |       m³/sでの1単位 |
|------------------|---------|-----------------------:|--------------------:|
| 立方メートル毎秒 | `m³/s`  | `cubicMetersPerSecond` |                 1.0 |
| 立方メートル毎時 | `m³/h`  |   `cubicMetersPerHour` |   1/3600 ≈ 2.778e-4 |
| リットル毎秒     | `l/s`   |      `litersPerSecond` |               0.001 |
| リットル毎分     | `l/min` |      `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| US ガロン毎分    | `gpm`   |   `usGallonsPerMinute` |          ≈ 6.309e-5 |

これらすべてはSI接頭辞の全範囲もサポートします (`milli.litersPerSecond`、`kilo.cubicMetersPerHour` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005(m³/s に正規化)
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## 実例 — 雨水タンクへの注水

庭用ポンプが 300 l/min を 5 m³ のタンクに送り込みます。タンクが満杯になるまでどのくらいかかるか、
またポンプのデータシートで使われる単位での流量はどうなるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67 分

pump into cubicMetersPerHour        // 18.0 m³/h(データシートの単位)
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// 逆方向: 15分間でどれだけの水が流れるか?
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## 中核単位 (体積と時間)での計算

| 式                    | 結果の型                  | 意味                   |
|-----------------------|---------------------------|------------------------|
| `volume / time`       | `KVolumeFlowUnitInstance` | 流量 = 体積 / 継続時間 |
| `volumeFlow * time`   | `KVolumeUnitInstance`     | 体積 = 流量 × 継続時間 |
| `time * volumeFlow`   | `KVolumeUnitInstance`     | 体積(可換)             |
| `volume / volumeFlow` | `KTimeUnitInstance`       | 継続時間 = 体積 / 流量 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## 分解表現

体積流量は2通りの方法で到達でき、いずれも同じ値として等しい型付きインスタンスを生成します。

| 分解表現             | 形式                              | 結果                           |
|----------------------|-----------------------------------|--------------------------------|
| `volume / time`      | 型付き演算子                      | `KVolumeFlowUnitInstance` 直接 |
| `distance³ · time⁻¹` | ネイティブ表現 + `toVolumeFlow()` | `KVolumeFlowUnitInstance`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// 型付き演算子形式
val typed = (8000 of liters) / (4 of seconds)

// ネイティブの基本次元形式(m³ · s⁻¹)、toVolumeFlow() によって認識される
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true - どちらも 2.0 m³/s
```

`toVolumeFlow()` は **唯一**の正準の正規形 (`KDistanceUnit` の項を指数 `+3`、`KTimeUnit` の項を指数 `-1`
とする形)のみを認識します。等価な表現はどれも自動的にこの形に還元されます。誤った形は値を静かに 誤って返すのではなく
`IllegalStateException` を投げます。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - : 同じグループ、異なる流量単位間の自動変換
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// 比較(正規化された m³/s の値で)
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// 2つの流量間の * / / は KMixedUnitInstance に外れる
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## toString によるフォーマット

`toString()` は値を基本単位で出力します。他の単位には `into` を使います:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学        | Kotlin                             | 意味                                  |
|-------------|------------------------------------|---------------------------------------|
| `m³/s`      | `cubicMetersPerSecond`             | 体積流量、基本単位 — 名前付きトークン |
| `m³·s⁻¹`    | `(meters pow 3) / seconds`         | 同じ流量を基本次元式として            |
| `l/s`       | `litersPerSecond`                  | リットル毎秒                          |
| `l/min`     | `litersPerMinute`                  | リットル毎分                          |
| `m³/h`      | `cubicMetersPerHour`               | 立方メートル毎時                      |
| `V / t`     | `(600 of liters) / (2 of minutes)` | 体積÷時間から組み立て                 |
| `V = q̇ · t` | `q * (60 of seconds)`              | 流量×継続時間から体積                 |
| `t = V / q̇` | `(600 of liters) / q`              | 体積÷流量から継続時間                 |
