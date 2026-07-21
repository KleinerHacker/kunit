# 圧力

パッケージ: `org.pcsoft.framework.kunit.pressure`
基本単位: **パスカル**(`KPressureUnit.BASE == KPressureUnit.PASCAL`)

圧力は**構成された**単位で、合成 `mass · length⁻¹ · time⁻²`(`kg/(m·s²)` = `N/m²`)です。
`KPressureUnitInstance` は3つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `-1` の `KDistanceUnit.BASE`
(メートル)、指数 `-2` の `KTimeUnit.BASE`(秒) — をラップします。力と同様に、保存される値は生のグラム基準の
成分値で、パスカルでの読み取りは固定係数で除算されます。

## 圧力の作成

圧力は `force / area` から、または名前付きトークンで作成します。名前付き単位は値1のトークンとして残ります
(`of`/`into` で使用):

| 圧力 | 記号 | トークン | Pa 換算(1単位) |
|---|---|---:|---:|
| パスカル | `Pa` | `pascals` | 1.0 |
| バール | `bar` | `bars` | 100000.0 |
| 気圧 | `atm` | `atmospheres` | 101325.0 |
| 重量ポンド毎平方インチ | `psi` | `psis` | 6894.757 |
| トル(mmHg) | `Torr` | `torrs` | 133.322 |

接頭辞で導出できる表記は専用トークンではありません: **hPa** = `hecto.pascals`、**kPa** = `kilo.pascals`、
構造力学の単位 **N/mm² = MPa** = `mega.pascals`(または式 `newtons / (milli.meters pow 2)`)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0(= 1 N/mm²)
```

## 基本単位(力と面積)による計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | 圧力 = F / A |
| `pressure * area` | `KForceUnitInstance` | 力 = p · A |
| `area * pressure` | `KForceUnitInstance` | 力(可換) |
| `force / pressure` | `KAreaUnitInstance` | 面積 = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance, 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance, 50 Pa
val f = p * area                           // KForceUnitInstance, 100 N
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa"(基本単位)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `Pa` | `pascals` | 圧力、基本単位（名前付きトークン、パスカル） |
| `N/m²` | `newtons / (meters pow 2)` | 力 / 面積 としての圧力（分数形式） |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | 同じ圧力を純粋な積で表現 |
| `kPa` | `kilo.pascals` | 接頭辞付きの圧力（キロパスカル） |
