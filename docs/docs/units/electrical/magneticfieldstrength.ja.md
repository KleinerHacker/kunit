# 磁界の強さ

パッケージ: `org.pcsoft.framework.kunit.electric.magneticfieldstrength`
基本単位: **アンペア毎メートル**(`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

種別: **構成された単位**

磁界の強さ (磁場 `H`)は **構成された**単位で、合成 `current · length⁻¹`(`A/m`)です。
`KMagneticFieldStrengthUnitInstance` は2つの項 — 指数 `+1` の `KElectricCurrentUnit.BASE`(アンペア)と 指数 `-1` の
`KDistanceUnit.BASE`(メートル) — をラップします。保存される値は常にアンペア毎メートルに 正規化されます。

関連ページ: [電流](ec.md) と [距離](../kinematics/distance.md) がこの単位の構成グループです。

## 磁界の強さの作成

磁界の強さは名前付きトークンから、または分解 (下記参照)から作成します。名前付き単位は値1のトークンとして 残ります (`of`/
`into` で使用):

| 磁界の強さ                 | 記号    |                トークン |   A/m 換算(1単位) |
|----------------------------|---------|------------------------:|------------------:|
| アンペア毎メートル         | `A/m`   |       `amperesPerMeter` |               1.0 |
| エルステッド(CGS-EMU)      | `Oe`    |              `oersteds` | 79.57747154594767 |
| ギルバート毎センチメートル | `Gb/cm` | `gilbertsPerCentimeter` | 79.57747154594767 |
| アンペアターン毎インチ     | `At/in` |    `ampereTurnsPerInch` | 39.37007874015748 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します (`kilo.amperesPerMeter`、`milli.oersteds` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## 複数の分解

磁界の強さはいくつかの **等価な分解**から得られ、いずれも同じ値の等しい磁界の強さを生成します:

| 式                 | 結果の型                             | 意味                            |
|--------------------|--------------------------------------|---------------------------------|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | 定義式 `H = I / l`              |
| `current·length⁻¹` | `.toMagneticFieldStrength()` 経由    | ネイティブな正規形 `A·m⁻¹` の式 |

型付き演算子の形式は磁界の強さを直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toMagneticFieldStrength()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で
絞り込みます。両方の経路は値が等しくなります。

逆の演算子は電流・長さ・磁界の強さを結び付けます:

| 式                       | 結果の型                       | 意味        |
|--------------------------|--------------------------------|-------------|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l` |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | 可換形式    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

// 実例 - 巻数500、電流 2 A、長さ 0.25 m のコイル:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance、4000 A/m

// 同じ磁界の強さをネイティブな A·m⁻¹ の式で:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学    | Kotlin                              | 意味                                             |
|---------|-------------------------------------|--------------------------------------------------|
| `A/m`   | `amperesPerMeter`                   | 磁界の強さ、基本単位（名前付きトークン）         |
| `A/m`   | `(amperes pow 1) / (meters pow 1)`  | 電流 / 長さ としての磁界の強さ（分数形式）       |
| `A·m⁻¹` | `(amperes pow 1) * (meters pow -1)` | 同じ磁界の強さを純粋な積で表現                   |
| `kA/m`  | `kilo.amperesPerMeter`              | 接頭辞付きの磁界の強さ（キロアンペア毎メートル） |
