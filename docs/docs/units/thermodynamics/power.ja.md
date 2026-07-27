# 動力（熱力学）

パッケージ: `org.pcsoft.framework.kunit.power`
基本単位: **ワット** (`KPowerUnit.BASE == KPowerUnit.WATT`)

種別: **構成単位（constructed unit）**

動力は**構成単位**です。組成は `質量 · 長さ² · 時間⁻³`（`kg·m²·s⁻³`）です。
`KPowerUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-3` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってワットに換算されます。
格納される値は常にワットに正規化されています。

動力は技術的には**一つ**の量ですが、複数の分野に現れます。このページではその*熱力学的*な解釈 —
**熱流量**、`Φ = Q / t`、すなわち時間あたりの熱エネルギー — を説明します。同じKotlinグループは他の分野向けに
[電力（電気）](../electrical/power.md)および[電力（力学）](../mechanics/power.md)でも文書化されています。

## 動力を組み立てる

名前付きトークンで、または分解表現（下記参照）から動力を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 動力 | 記号 | トークン | Wでの1単位 |
|---|---|---:|---:|
| ワット | `W` | `watts` | 1.0 |
| メートル法馬力 | `PS` | `metricHorsePowers` | 735.49875 |
| 機械的馬力 | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| 秒あたりのエルグ（CGS） | `erg/s` | `ergsPerSecond` | 1.0e-7 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.watts`、`mega.watts`、`milli.watts` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

val heatFlow = 9 of kilo.watts   // 部屋のヒーター
heatFlow into kilo.watts         // 9.0
heatFlow into watts              // 9000.0
```

## 複数の分解表現

動力は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい動力を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | 熱流量 `Φ = Q / t`（[エネルギー（熱力学）](energy.md)を参照） |
| `voltage * current` | `KPowerUnitInstance` | 電気的な電力 `P = U · I`（[電力（電気）](../electrical/power.md)を参照） |
| `force * speed` | `KPowerUnitInstance` | 機械的な動力 `P = F · v`（[電力（力学）](../mechanics/power.md)を参照） |
| `mass·length²/time³` | `.toPower()` 経由 | ネイティブの正準 `kg·m²·s⁻³` 表現 |

型付き演算子形式は直接動力を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toPower()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

熱流量形式の逆演算子はエネルギー、時間、動力を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | 供給された熱、`Q = Φ · t`（可換） |
| `energy / power` | `KTimeUnitInstance` | 必要な時間、`t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.energy.*
import org.pcsoft.framework.kunit.power.*

// 実例 - 給湯器: 10分以内に1200 kJの熱が供給されるのは2 kWの熱流量である。
val heatFlow = (1200 of kilo.joules) / (10 of minutes)   // KPowerUnitInstance
heatFlow into kilo.watts                                 // 2.0

// その熱流量を解いて、1時間で供給される熱量を求める:
val heat = (2 of kilo.watts) * (60 of minutes)           // KEnergyUnitInstance, 7.2 MJ

// 同じ熱流量をネイティブの kg·m²·s⁻³ 表現として:
val raw = 2000 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2 of kilo.watts)                       // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

(9 of kilo.watts).toString()     // "9000.0 W"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻³`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `W` | `watts` | 動力（熱流量）、基本単位（名前付きトークン、ワット） |
| `Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | 熱と時間からの熱流量 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 質量・長さ² / 時間³ としての動力（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 純粋な積としての同じ動力 |
| `kW` | `kilo.watts` | 接頭辞付き動力（キロワット） |
