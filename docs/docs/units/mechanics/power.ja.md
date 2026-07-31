# 動力（力学）

パッケージ: `org.pcsoft.framework.kunit.common.power`
基本単位: **ワット** (`KPowerUnit.BASE == KPowerUnit.WATT`)

種別: **構成単位（constructed unit）**

動力は **構成単位**です。組成は `質量 · 長さ² · 時間⁻³`（`kg·m²·s⁻³`）です。
`KPowerUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-3` として保持します。ライブラリの質量成分は **グラム**
（キログラムではない）に正規化されているため、正準積は1000で割ってワットに換算されます。 格納される値は常にワットに正規化されています。

動力は技術的には **一つ**の量ですが、複数の分野に現れます。このページではその *力学的*な解釈 （`P = F · v`
）を説明します。同じKotlinグループは他の分野向けに
[電力（電気）](../electrical/power.md)および[電力（熱力学）](../thermodynamics/power.md)でも文書化されています。

## 動力を組み立てる

名前付きトークンで、または分解表現（下記参照）から動力を組み立てられます。名前付き単位は値1の トークンとして存在します（`of`/
`into` と併用）。

| 動力                    | 記号    |                トークン |        Wでの1単位 |
|-------------------------|---------|------------------------:|------------------:|
| ワット                  | `W`     |                 `watts` |               1.0 |
| メートル法馬力          | `PS`    |     `metricHorsePowers` |         735.49875 |
| 機械的馬力              | `hp`    | `mechanicalHorsePowers` | 745.6998715822702 |
| 秒あたりのエルグ（CGS） | `erg/s` |         `ergsPerSecond` |            1.0e-7 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.watts`、`mega.watts`、`milli.watts` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 100 of metricHorsePowers
p into kilo.watts               // 73.549875
p into mechanicalHorsePowers    // 98.63200706...
```

## 複数の分解表現

動力は複数の **等価な分解表現**を通じて到達でき、いずれも同じ値として等しい動力を生成します。

| 表現                 | 結果の型             | 意味                                                                     |
|----------------------|----------------------|--------------------------------------------------------------------------|
| `force * speed`      | `KPowerUnitInstance` | 機械的な動力 `P = F · v`（可換）                                         |
| `voltage * current`  | `KPowerUnitInstance` | 電気的な電力 `P = U · I`（[電力（電気）](../electrical/power.md)を参照） |
| `energy / time`      | `KPowerUnitInstance` | `P = W / t`（[エネルギー（力学）](energy.md)を参照）                     |
| `mass·length²/time³` | `.toPower()` 経由    | ネイティブの正準 `kg·m²·s⁻³` 表現                                        |

型付き演算子形式は直接動力を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toPower()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

力学的な形式の逆演算子は力、速度、動力を結び付けます。

| 表現            | 結果の型             | 意味        |
|-----------------|----------------------|-------------|
| `power / force` | `KSpeedUnitInstance` | `v = P / F` |
| `power / speed` | `KForceUnitInstance` | `F = P / v` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*

// 実例 - 貨物ウインチ: 100 Nの牽引力を5 m/sで動かすには500 Wが必要。
val p = (100 of newtons) * ((5 of meters) / (1 of seconds))  // KPowerUnitInstance
p into watts                                                 // 500.0

// 定義式を解いて、ある速度における牽引力を求める:
val f = (500 of watts) / ((5 of meters) / (1 of seconds))     // KForceUnitInstance, 100 N

// そして、ある力で到達可能な速度について解く:
val v = (500 of watts) / (100 of newtons)                     // KSpeedUnitInstance, 5 m/s

// 同じ動力をネイティブの kg·m²·s⁻³ 表現として:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (500 of watts)                               // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

(1 of metricHorsePowers).toString()     // "735.49875 W"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`⁻³`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学        | Kotlin                                                | 意味                                         |
|-------------|-------------------------------------------------------|----------------------------------------------|
| `W`         | `watts`                                               | 動力、基本単位（名前付きトークン、ワット）   |
| `F · v`     | `(100 of newtons) * ((5 of meters) / (1 of seconds))` | 力と速度からの機械的な動力                   |
| `kg·m²/s³`  | `(kilo.grams * (meters pow 2)) / (seconds pow 3)`     | 質量・長さ² / 時間³ としての動力（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)`      | 純粋な積としての同じ動力                     |
| `PS`        | `metricHorsePowers`                                   | メートル法馬力（名前付きトークン）           |
