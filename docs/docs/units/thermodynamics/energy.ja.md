# エネルギー（熱力学）

パッケージ: `org.pcsoft.framework.kunit.energy`
基本単位: **ジュール** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

種別: **構成単位（constructed unit）**

エネルギーは**構成単位**です。組成は `質量 · 長さ² · 時間⁻²`（`kg·m²·s⁻²`）です。
`KEnergyUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってジュールに換算されます。
格納される値は常にジュールに正規化されています。

エネルギーは技術的には**一つ**の量ですが、複数の分野に現れます。このページではその*熱力学的*な解釈 —
**熱**、`Q = Φ · t` — を説明します。同じKotlinグループは他の分野向けに
[エネルギー（電気）](../electrical/energy.md)および[エネルギー（力学）](../mechanics/energy.md)でも文書化されています。

## エネルギーを組み立てる

名前付きトークンで、または分解表現（下記参照）からエネルギーを組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。このグループの熱的な単位はカロリーと英熱量単位です。

| エネルギー | 記号 | トークン | Jでの1単位 |
|---|---|---:|---:|
| ジュール | `J` | `joules` | 1.0 |
| エルグ（CGS） | `erg` | `ergs` | 1.0e-7 |
| カロリー（熱化学） | `cal` | `calories` | 4.184 |
| 電子ボルト | `eV` | `electronVolts` | 1.602176634e-19 |
| 英熱量単位 | `BTU` | `britishThermalUnits` | 1055.05585262 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.calories` — いわゆる「食物カロリー」 —
`kilo.joules`、`mega.joules` など）。

**キロワット時には固有のトークンがありません** — 純粋に名前付きの単位ではなく、積
`kilo.watts * hours` として組み立てられます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.energy.*

val q = 2000 of kilo.calories   // 1日分の食事
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## 複数の分解表現

エネルギーは複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しいエネルギーを生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | 時間にわたる熱流量からの熱 `Q = Φ · t`（可換） |
| `power / frequency` | `KEnergyUnitInstance` | 逆時間形式（`W/Hz = W·s`） |
| `force * length` | `KEnergyUnitInstance` | 機械的な仕事 `W = F · s`（[エネルギー（力学）](../mechanics/energy.md)を参照） |
| `charge * voltage` | `KEnergyUnitInstance` | 電気エネルギー `W = Q · U`（[エネルギー（電気）](../electrical/energy.md)を参照） |
| `mass·length²/time²` | `.toEnergy()` 経由 | ネイティブの正準 `kg·m²·s⁻²` 表現 |

型付き演算子形式は直接エネルギーを返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toEnergy()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は熱流量、時間、熱を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | 熱流量 `Φ = Q / t`（[動力（熱力学）](power.md)を参照） |
| `energy / power` | `KTimeUnitInstance` | 加熱時間 `t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

// 実例 - 給湯器: 2 kWの熱流量が10分間続くと1200 kJの熱を供給する。
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// その熱を解いて、2 kWの給湯器の加熱時間を求める:
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance, 600 s

// そして、熱流量について解く:
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance, 2 kW

// 同じ熱をネイティブの kg·m²·s⁻² 表現として:
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
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
import org.pcsoft.framework.kunit.energy.*

(1 of britishThermalUnits).toString()     // "1055.05585262 J"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J` | `joules` | エネルギー（熱）、基本単位（名前付きトークン、ジュール） |
| `Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | 熱流量と時間からの熱 |
| `kcal` | `kilo.calories` | 接頭辞付き熱エネルギー（食物カロリー） |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 質量・長さ² / 時間² としてのエネルギー（分数形式） |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 純粋な積としての同じエネルギー |
