# 電気移動度

パッケージ: `org.pcsoft.framework.kunit.electricmobility`
基本単位: **ボルト秒あたりの平方メートル**
(`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`)

種別: **構成単位（constructed unit）**

電気移動度は**構成単位**です。組成は `質量⁻¹ · 時間² · 電流`
（`kg⁻¹·s²·A` = `m²/(V·s)`）です。`KElectricMobilityUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `-1`、`KTimeUnit.BASE`（秒）を `+2`、`KElectricCurrentUnit.BASE`（アンペア）を
`+1` として保持します。長さ次元はボルトがすでに `m²` を含むため打ち消し合い、正準形は3項のみになります。
ライブラリの質量成分は**グラム**（キログラムではない）に正規化されており、質量の指数が*負*であるため、
正準積は1000を掛けて基本単位に換算されます。格納される値は常にボルト秒あたりの平方メートルに正規化されています。

電気移動度 `μ` は電荷担体が電界中でどれだけ速く移動するかを表します: `v = μ · E`、ここで
`E` は[電界強度](electricfieldstrength.md)です。

## 電気移動度を組み立てる

名前付きトークンで、または分解表現（下記参照）から電気移動度を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 移動度 | 記号 | トークン | m²/(V·s)での1単位 |
|---|---|---:|---:|
| ボルト秒あたりの平方メートル | `m²/(V·s)` | `squareMetersPerVoltSecond` | 1.0 |
| ボルト秒あたりの平方センチメートル | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 1.0e-4 |

センチメートル形式は半導体物理学全般で使われる表記です。名前付き単位は `KPrefixBuilder` を通じてSI接頭辞を
サポートします（`milli.squareMetersPerVoltSecond`、`kilo.squareCentimetersPerVoltSecond` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricmobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // シリコン中の電子移動度
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## 複数の分解表現

電気移動度は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい移動度を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`、単位電界あたりのドリフト速度 |
| `(time²·current)/mass` | `.toElectricMobility()` 経由 | ネイティブの正準 `kg⁻¹·s²·A` 表現 |

型付き演算子形式は直接移動度を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toElectricMobility()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。両方の経路は値として等しくなります。

逆演算子はドリフト速度、電界強度、移動度を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance` | `v = μ · E`（可換） |
| `speed / electricMobility` | `KElectricFieldStrengthUnitInstance` | `E = v / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricmobility.*

// 実例 - 1400 cm²/(V·s)のシリコン電子は1 kV/mの電界で140 m/sのドリフト速度になる。
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance, 140 m/s

// 定義式を移動度について解く:
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// 同じ移動度をネイティブの kg⁻¹·s²·A 表現として:
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `m²/(V·s)` | `squareMetersPerVoltSecond` | 電気移動度、基本単位（名前付きトークン） |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 半導体物理学の表記、1e-4 m²/(V·s) |
| `v / E` | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)` | ドリフト速度と電界強度からの移動度 |
| `μ · E` | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | 与えられた電界でのドリフト速度 |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)` | (時間²・電流) / 質量 としての移動度（分数形式） |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)` | 純粋な積としての同じ移動度 |
