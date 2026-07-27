# エネルギー（力学）

パッケージ: `org.pcsoft.framework.kunit.energy`
基本単位: **ジュール** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

種別: **構成単位（constructed unit）**

エネルギーは**構成単位**です。組成は `質量 · 長さ² · 時間⁻²`（`kg·m²·s⁻²`）です。
`KEnergyUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします — `KMassUnit.BASE`（グラム）を `+1`、
`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってジュールに換算されます。
格納される値は常にジュールに正規化されています。

エネルギーは技術的には**一つ**の量ですが、複数の分野に現れます。このページではその*力学的*な解釈 —
**仕事**、`W = F · s` — を説明します。同じKotlinグループは他の分野向けに
[エネルギー（電気）](../electrical/energy.md)および[エネルギー（熱力学）](../thermodynamics/energy.md)でも文書化されています。

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
`kilo.calories` など）。

**キロワット時には固有のトークンがありません** — 純粋に名前付きの単位ではなく、積
`kilo.watts * hours` として組み立てられます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.energy.*

val w = 500 of joules
w into joules                   // 500.0
w into calories                 // 119.502868...
(1 of kilo.joules) into joules  // 1000.0
```

## 複数の分解表現

エネルギーは複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しいエネルギーを生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `force * length` | `KEnergyUnitInstance` | 機械的な仕事 `W = F · s`（可換） |
| `power * time` | `KEnergyUnitInstance` | 時間にわたる動力からの仕事 `W = P · t`（可換） |
| `power / frequency` | `KEnergyUnitInstance` | 逆時間形式（`W/Hz = W·s`） |
| `charge * voltage` | `KEnergyUnitInstance` | 電気エネルギー `W = Q · U`（[エネルギー（電気）](../electrical/energy.md)を参照） |
| `mass·length²/time²` | `.toEnergy()` 経由 | ネイティブの正準 `kg·m²·s⁻²` 表現 |

型付き演算子形式は直接エネルギーを返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toEnergy()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は動力、時間、エネルギーを結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | `P = W / t`（[動力（力学）](power.md)を参照） |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

// 実例 - 持ち上げる仕事: 100 Nの力で5 mの距離を引くと500 Jの仕事になる。
val w = (100 of newtons) * (5 of meters)   // KEnergyUnitInstance
w into joules                              // 500.0

// その仕事を5秒以内に行うために必要な動力を求める:
val p = (500 of joules) / (5 of seconds)   // KPowerUnitInstance, 100 W

// そして、100 Wの駆動装置がその仕事にかかる時間を求める:
val t = (500 of joules) / (100 of watts)   // KTimeUnitInstance, 5 s

// 同じ仕事をネイティブの kg·m²·s⁻² 表現として:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)          // true
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

(1 of calories).toString()     // "4.184 J"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `J` | `joules` | エネルギー（仕事）、基本単位（名前付きトークン、ジュール） |
| `F · s` | `(100 of newtons) * (5 of meters)` | 力と長さからの機械的な仕事 |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 質量・長さ² / 時間² としてのエネルギー（分数形式） |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | 純粋な積としての同じエネルギー |
| `kJ` | `kilo.joules` | 接頭辞付きエネルギー（キロジュール） |
