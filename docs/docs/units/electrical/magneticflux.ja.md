# 磁束

パッケージ: `org.pcsoft.framework.kunit.electric.magneticflux`
基本単位: **ウェーバ** (`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`)

種別: **構成単位（constructed unit）**

磁束は **構成単位**です。組成は `質量 · 長さ² · 時間⁻² · 電流⁻¹`
（`kg·m²·s⁻²·A⁻¹`）です。`KMagneticFluxUnitInstance` は4つの項からなる `KMixedUnitInstance` をラップします —
`KMassUnit.BASE`（グラム）を `+1`、`KDistanceUnit.BASE`（メートル）を `+2`、`KTimeUnit.BASE`（秒）を `-2`、
`KElectricCurrentUnit.BASE`（アンペア）を `-1` として保持します。ライブラリの質量成分は **グラム**
（キログラムではない）に正規化されているため、正準積は1000で割ってウェーバに換算されます。格納される値は 常にウェーバに正規化されています。

## 磁束を組み立てる

名前付きトークンで、または分解表現（下記参照）から磁束を組み立てられます。名前付き単位は値1のトークンとして 存在します（`of`/
`into` と併用）。

| 磁束                    | 記号   |    トークン |           Wbでの1単位 |
|-------------------------|--------|------------:|----------------------:|
| ウェーバ                | `Wb`   |    `webers` |                   1.0 |
| マクスウェル（CGS-EMU） | `Mx`   |  `maxwells` |                1.0e-8 |
| 単位磁極                | `pole` | `unitPoles` | 1.2566370614359173e-7 |

名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします（`milli.webers`、`micro.webers`、`kilo.maxwells` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## 複数の分解表現

磁束は複数の **等価な分解表現**を通じて到達でき、いずれも同じ値として等しい磁束を生成します。

| 表現                           | 結果の型                    | 意味                                                    |
|--------------------------------|-----------------------------|---------------------------------------------------------|
| `voltage * time`               | `KMagneticFluxUnitInstance` | ファラデーの誘導法則 `Φ = U · t`（可換）                |
| `voltage / frequency`          | `KMagneticFluxUnitInstance` | 逆時間形式（`V/Hz = V·s`）                              |
| `inductance * current`         | `KMagneticFluxUnitInstance` | `Φ = L · I`（[インダクタンス](inductance.md)を参照）    |
| `fluxDensity * area`           | `KMagneticFluxUnitInstance` | `Φ = B · A`（[磁束密度](magneticfluxdensity.md)を参照） |
| `mass·length²/(time²·current)` | `.toMagneticFlux()` 経由    | ネイティブの正準 `kg·m²·s⁻²·A⁻¹` 表現                   |

型付き演算子形式は直接磁束を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toMagneticFlux()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。すべての経路は値として等しくなります。

逆演算子は電圧、時間、磁束を結び付けます。

| 表現               | 結果の型               | 意味                 |
|--------------------|------------------------|----------------------|
| `flux / time`      | `KVoltageUnitInstance` | 誘導電圧 `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | 逆時間の対応形式     |
| `flux / voltage`   | `KTimeUnitInstance`    | `t = Φ / U`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.magneticflux.*

// 実例 - 点火コイル: 20 mWbのコア磁束が4 ms以内に消滅すると5 Vが誘導される。
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance, 5 V

// 誘導法則を磁束について解く:
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance, 2 Wb

// 周波数からの同じ磁束、およびネイティブの kg·m²·s⁻²·A⁻¹ 表現として:
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

(20 of webers).toString()     // "20.0 Wb"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学            | Kotlin                                                                | 意味                                                 |
|-----------------|-----------------------------------------------------------------------|------------------------------------------------------|
| `Wb`            | `webers`                                                              | 磁束、基本単位（名前付きトークン、ウェーバ）         |
| `V·s`           | `(10 of volts) * (0.2 of seconds)`                                    | 電圧・時間としての磁束（誘導法則）                   |
| `kg·m²/(s²·A)`  | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | 質量・長さ² / (時間²・電流) としての磁束（分数形式） |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)`   | 純粋な積としての同じ磁束                             |
| `mWb`           | `milli.webers`                                                        | 接頭辞付き磁束（ミリウェーバ）                       |
