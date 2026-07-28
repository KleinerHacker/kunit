# 電界強度

パッケージ: `org.pcsoft.framework.kunit.electricfieldstrength`
基本単位: **メートルあたりのボルト** (`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`)

種別: **構成単位（constructed unit）**

電界強度は**構成単位**です。組成は `質量 · 長さ · 時間⁻³ · 電流⁻¹`
（`kg·m·s⁻³·A⁻¹`）です。`KElectricFieldStrengthUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `+1`、`KDistanceUnit.BASE`（メートル）を `+1`、`KTimeUnit.BASE`（秒）を `-3`、
`KElectricCurrentUnit.BASE`（アンペア）を `-1` として保持します。ライブラリの質量成分は
**グラム**（キログラムではない）に正規化されているため、正準積は1000で割ってメートルあたりのボルトに換算されます。
格納される値は常にメートルあたりのボルトに正規化されています。

電界強度 `E` は単位長さあたりの電圧降下であり、同時に単位電荷に作用する力でもあります。
[誘電率](permittivity.md)を介して[電束密度](electricfluxdensity.md)と関係し（`D = ε · E`）、
[電気移動度](electricmobility.md)によって決まる速度で電荷担体を駆動します（`v = μ · E`）。

## 電界強度を組み立てる

名前付きトークンで、または分解表現（下記参照）から電界強度を組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 電界強度 | 記号 | トークン | V/mでの1単位 |
|---|---|---:|---:|
| メートルあたりのボルト | `V/m` | `voltsPerMeter` | 1.0 |
| センチメートルあたりのボルト | `V/cm` | `voltsPerCentimeter` | 100.0 |
| センチメートルあたりのスタットボルト（CGS-ESU） | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`kilo.voltsPerMeter`、`mega.voltsPerMeter`、
`kilo.voltsPerCentimeter` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electricfieldstrength.*

val e = 3 of mega.voltsPerMeter        // 空気の絶縁破壊強度
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## 複数の分解表現

電界強度は複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しい電界強度を生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`、単位長さあたりの電圧降下 |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`、単位電荷に作用する力 |
| `mass·length/(time³·current)` | `.toElectricFieldStrength()` 経由 | ネイティブの正準 `kg·m·s⁻³·A⁻¹` 表現 |

型付き演算子形式は直接電界強度を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toElectricFieldStrength()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は電圧、長さ、力、電荷、電界強度を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l`（可換） |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q`（可換） |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

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
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.electricfieldstrength.*

// 実例 - 2 mmの空隙にかかる商用電源電圧は115 kV/mになる。
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance, 115000 V/m

// 力からの分解による同じ電界強度:
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// 同じ電界強度をネイティブの kg·m·s⁻³·A⁻¹ 表現として:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `V/m` | `voltsPerMeter` | 電界強度、基本単位（名前付きトークン、メートルあたりのボルト） |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | 距離にわたる電圧からの電界強度 |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | 単位電荷あたりの力としての電界強度 |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | 質量・長さ / (時間³・電流) としての電界強度（分数形式） |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | 純粋な積としての同じ電界強度 |
| `kV/m` | `kilo.voltsPerMeter` | 接頭辞付き電界強度（キロボルト毎メートル） |
