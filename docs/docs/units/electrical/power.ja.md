# 電力（電気）

パッケージ: `org.pcsoft.framework.kunit.power`
基本単位: **ワット** (`KPowerUnit.BASE == KPowerUnit.WATT`)

種別: **構成単位（constructed unit）**

電力は**構成単位**です。組成は `質量 · 長さ² · 時間⁻³`（`kg·m²·s⁻³`）です。
`KPowerUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-3` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってワットに換算されます。
格納される値は常にワットに正規化されています。

電力は技術的には**一つ**の量ですが、複数の分野に現れます。このページではその*電気的*な解釈
（`P = U · I`）を説明します。同じKotlinグループは他の分野向けに
[電力（力学）](../mechanics/power.md)および[電力（熱力学）](../thermodynamics/power.md)でも文書化されています。

## 電力を組み立てる

名前付きトークンで、または分解表現（下記参照）から電力を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 電力 | 記号 | トークン | Wでの1単位 |
|---|---|---:|---:|
| ワット | `W` | `watts` | 1.0 |
| メートル法馬力 | `PS` | `metricHorsePowers` | 735.49875 |
| 機械的馬力 | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| 秒あたりのエルグ（CGS） | `erg/s` | `ergsPerSecond` | 1.0e-7 |
| ボルトアンペア（皮相電力） | `VA` | `voltAmperes` | 1.0 |
| 無効電力（バール） | `var` | `vars` | 1.0 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.watts`、`mega.watts`、`milli.watts` など）。

### 皮相電力と無効電力（VA、var）

交流システムでは3種類の電力が区別されますが、いずれもワットと次元的に同一です。

* **有効電力** `P = U · I · cos φ`（ワット、`W`）— 仕事をする成分、
* **皮相電力** `S = U · I`（ボルトアンペア、`VA`）— 実効電圧と実効電流の積、
* **無効電力** `Q = U · I · sin φ`（バール、`var`）— 電源と負荷の間で振動し、仕事をしない成分。

この3つは慣習上異なるだけなので、KUnitはこれらを1つのグループに保持し、記号で区別します:
`1 VA = 1 var = 1 W`。接頭辞も通常どおり使用でき、`kilo.voltAmperes` は 1 kVA、`kilo.vars` は 1 kvar です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

// 25 kVA定格のトランスが力率 cos φ = 0.8 の負荷に給電する場合:
val s = 25 of kilo.voltAmperes
val p = (25 * 0.8) of kilo.watts     // 20 kW の有効電力
val q = (25 * 0.6) of kilo.vars      // 15 kvar の無効電力
s into kilo.voltAmperes               // 25.0
q into kilo.vars                      // 15.0
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## 複数の分解表現

電力は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい電力を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `voltage * current` | `KPowerUnitInstance` | 電気的な電力 `P = U · I`（可換） |
| `force * speed` | `KPowerUnitInstance` | 機械的な動力 `P = F · v`（可換） |
| `energy / time` | `KPowerUnitInstance` | `P = W / t`（[エネルギー](energy.md)を参照） |
| `mass·length²/time³` | `.toPower()` 経由 | ネイティブの正準 `kg·m²·s⁻³` 表現 |

型付き演算子形式は直接電力を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toPower()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

電気的な形式の逆演算子は電圧、電流、電力を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `power / current` | `KVoltageUnitInstance` | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.power.*

// 実例 - コンセント: 230 Vで10 Aは2.3 kWを供給する。
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// 定義式を解いて、230 Vで2.3 kWの負荷が引く電流を求める:
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance, 10 A

// 同じ電力をネイティブの kg·m²·s⁻³ 表現として:
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
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

(1 of kilo.watts).toString()     // "1000.0 W"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻³`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `W` | `watts` | 電力、基本単位（名前付きトークン、ワット） |
| `U · I` | `(230 of volts) * (10 of amperes)` | 電圧と電流からの電気的な電力 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 質量・長さ² / 時間³ としての電力（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 純粋な積としての同じ電力 |
| `kW` | `kilo.watts` | 接頭辞付き電力（キロワット） |
| `S = U · I` を `VA` で | `voltAmperes` | 皮相電力（交流） |
| `Q` を `var` で | `vars` | 無効電力（交流） |
| `kVA` | `kilo.voltAmperes` | 接頭辞付き皮相電力（キロボルトアンペア） |
| `kvar` | `kilo.vars` | 接頭辞付き無効電力 |
