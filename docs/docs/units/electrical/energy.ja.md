# エネルギー（電気）

パッケージ: `org.pcsoft.framework.kunit.energy`
基本単位: **ジュール** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

種別: **構成単位（constructed unit）**

エネルギーは**構成単位**です。組成は `質量 · 長さ² · 時間⁻²`（`kg·m²·s⁻²`）です。
`KEnergyUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってジュールに換算されます。
格納される値は常にジュールに正規化されています。

エネルギーは技術的には**一つ**の量ですが、複数の分野に現れます。このページではその*電気的*な解釈
（`W = Q · U`、消費された電気エネルギーについては `W = P · t`）を説明します。同じKotlinグループは他の分野向けに
[エネルギー（力学）](../mechanics/energy.md)および[エネルギー（熱力学）](../thermodynamics/energy.md)でも文書化されています。

## エネルギーを組み立てる

名前付きトークンで、または分解表現（下記参照）からエネルギーを組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| エネルギー | 記号 | トークン | Jでの1単位 |
|---|---|---:|---:|
| ジュール | `J` | `joules` | 1.0 |
| エルグ（CGS） | `erg` | `ergs` | 1.0e-7 |
| カロリー（熱化学） | `cal` | `calories` | 4.184 |
| 電子ボルト | `eV` | `electronVolts` | 1.602176634e-19 |
| 英熱量単位 | `BTU` | `britishThermalUnits` | 1055.05585262 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.joules`、`mega.joules`、
`mega.electronVolts` など）。

**キロワット時には固有のトークンがありません** — 純粋に名前付きの単位ではなく、積
`kilo.watts * hours` として組み立てられます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

val w = 500 of kilo.joules
w into kilo.joules                          // 500.0
w into joules                               // 500000.0

val kwh = (1 of kilo.watts) * (1 of hours)  // 1 kWh = 3.6 MJ
kwh into kilo.joules                        // 3600.0
```

## 複数の分解表現

エネルギーは複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しいエネルギーを生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `charge * voltage` | `KEnergyUnitInstance` | 電気エネルギー `W = Q · U`（可換） |
| `power * time` | `KEnergyUnitInstance` | 消費エネルギー `W = P · t`（可換） |
| `power / frequency` | `KEnergyUnitInstance` | 逆時間形式（`W/Hz = W·s`） |
| `force * length` | `KEnergyUnitInstance` | 機械的な仕事 `W = F · s`（[エネルギー（力学）](../mechanics/energy.md)を参照） |
| `mass·length²/time²` | `.toEnergy()` 経由 | ネイティブの正準 `kg·m²·s⁻²` 表現 |

型付き演算子形式は直接エネルギーを返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toEnergy()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は電荷、電圧、電力、時間、エネルギーを結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `energy / charge` | `KVoltageUnitInstance` | `U = W / Q` |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

// 実例 - 2 kWのヒーターを3時間運転すると6 kWh = 21600 kJを消費する。
val w = (2 of kilo.watts) * (3 of hours)   // KEnergyUnitInstance
w into kilo.joules                         // 21600.0

// 電荷と電圧からの電気エネルギー: 50 Vを介して移動した10 Cは500 J。
val fromCharge = (10 of coulombs) * (50 of volts)  // KEnergyUnitInstance, 500 J

// 定義式を電圧について解く:
val u = (500 of joules) / (10 of coulombs)         // KVoltageUnitInstance, 50 V

// 同じエネルギーをネイティブの kg·m²·s⁻² 表現として:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)                  // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.energy.*

(1 of kilo.joules).toString()     // "1000.0 J"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J` | `joules` | エネルギー、基本単位（名前付きトークン、ジュール） |
| `Q · U` | `(10 of coulombs) * (50 of volts)` | 電荷と電圧からの電気エネルギー |
| `P · t` | `(2 of kilo.watts) * (3 of hours)` | 消費エネルギー（kWhにはトークンがない） |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 質量・長さ² / 時間² としてのエネルギー（分数形式） |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 純粋な積としての同じエネルギー |
| `kJ` | `kilo.joules` | 接頭辞付きエネルギー（キロジュール） |
