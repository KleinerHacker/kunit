# 磁気抵抗

パッケージ: `org.pcsoft.framework.kunit.reluctance`
基本単位: **ウェーバあたりのアンペア** (`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`)

種別: **構成単位（constructed unit）**

磁気抵抗は**構成単位**です。組成は `質量⁻¹ · 長さ⁻² · 時間² · 電流²`
（`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`）です。`KReluctanceUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `-1`、`KDistanceUnit.BASE`（メートル）を `-2`、`KTimeUnit.BASE`（秒）を `+2`、
`KElectricCurrentUnit.BASE`（アンペア）を `+2` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されており、質量の指数が*負*であるため、正準積は1000を掛けて
ウェーバあたりのアンペアに換算されます。格納される値は常にウェーバあたりのアンペアに正規化されています。

磁気抵抗 `Rm` は電気の[抵抗](resistance.md)に対応する磁気回路の量であり、起磁力 `Θ`
（アンペア回数で測定、[電流](ec.md)参照）を、ホプキンソンの法則 `Θ = Rm · Φ` を通じて
生じる[磁束](magneticflux.md)と関係付けます。その逆数は**パーミアンス** `Λ` であり、
ヘンリーで測定されるため[インダクタンス](inductance.md)グループが担います。

## 磁気抵抗を組み立てる

名前付きトークンで、または分解表現（下記参照）から磁気抵抗を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 磁気抵抗 | 記号 | トークン | A/Wbでの1単位 |
|---|---|---:|---:|
| ウェーバあたりのアンペア | `A/Wb` | `amperesPerWeber` | 1.0 |
| 逆ヘンリー | `H⁻¹` | `inverseHenries` | 1.0 |
| ウェーバあたりのアンペア回数 | `At/Wb` | `ampereTurnsPerWeber` | 1.0 |

これら3つの表記はすべて同じ量を表します — コイルの巻数は純粋な数であるため — 値として等しく、
異なる記号は視点の違いを表現するだけです。名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします
（`mega.amperesPerWeber`、`kilo.inverseHenries` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.reluctance.*

val rm = 2 of mega.amperesPerWeber    // ギャップのある鉄心
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## 複数の分解表現

磁気抵抗は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい磁気抵抗を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `current / magneticFlux` | `KReluctanceUnitInstance` | ホプキンソンの法則 `Rm = Θ / Φ` |
| `1 / inductance` | `KReluctanceUnitInstance` | パーミアンスの逆数 `Rm = 1 / Λ` |
| `(time²·current²)/(mass·length²)` | `.toReluctance()` 経由 | ネイティブの正準 `kg⁻¹·m⁻²·s²·A²` 表現 |

型付き演算子形式は直接磁気抵抗を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toReluctance()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は起磁力、磁束、パーミアンス、磁気抵抗を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ`（可換） |
| `current / reluctance` | `KMagneticFluxUnitInstance` | `Φ = Θ / Rm` |
| `1 / reluctance` | `KInductanceUnitInstance` | パーミアンス `Λ = 1 / Rm`（ヘンリー単位） |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.ec.ampereTurns
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.inductance.henries
import org.pcsoft.framework.kunit.reluctance.*

// 実例 - 2 kAtの起磁力が2 MA/Wbの鉄心を通ると1 mWbの磁束が生じる。
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// 定義式を磁気抵抗について解く:
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// 同じ磁気抵抗をネイティブの kg⁻¹·m⁻²·s²·A² 表現として:
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb（直列磁気回路）
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `A/Wb` | `amperesPerWeber` | 磁気抵抗、基本単位（名前付きトークン、ウェーバあたりのアンペア） |
| `H⁻¹` | `inverseHenries` | 同じ量の逆インダクタンス表記 |
| `Θ / Φ` | `(6 of amperes) / (3 of webers)` | ホプキンソンの法則による磁気抵抗 |
| `1 / Λ` | `1 / (0.5 of henries)` | パーミアンスの逆数としての磁気抵抗 |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | (時間²・電流²) / (質量・長さ²) としての磁気抵抗（分数形式） |
| `kg⁻¹·m⁻²·s²·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | 純粋な積としての同じ磁気抵抗 |
| `MA/Wb` | `mega.amperesPerWeber` | 接頭辞付き磁気抵抗（メガアンペア毎ウェーバ） |
