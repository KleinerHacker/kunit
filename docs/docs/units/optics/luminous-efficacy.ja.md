# 発光効率

パッケージ: `org.pcsoft.framework.kunit.optic.efficacy`
基本単位: **ルーメン毎ワット**（`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`）

種別: **構成単位**

発光効率 `η` はランプが **電力1ワットあたりに** 生成する光束です: `η = Φ / P`。これは光源の良し悪しを表す単一の数値であり、
測光量ファミリーと放射量ファミリーをつなぐ橋渡しでもあります — 検出器が測定するワットを、目が知覚するルーメンに変換します。

その正準基本次元標準形は `luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³` です。

## 単位

| 単位           | Enum値                              | 記号 |           トークン | 1単位（lm/W） |
|----------------|-----------------------------------------|--------|----------------:|---------------:|
| ルーメン毎ワット | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |            1.0 |

このトークンはあらゆるSI接頭辞を受け付けます（`milli.lumensPerWatt`、`kilo.lumensPerWatt` など）。

## 定数

| 定数                | 値       | 意味                                                       |
|-------------------------|-------------|-----------------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | SIカンデラの定義に由来する、555 nmにおける物理的上限 |

いかなる光源も683 lm/Wを超えることはできません。これは明所視標準比視感度関数のピークにおける単色緑色光の効率だからです。
実際のすべてのランプはこの値の一部分にすぎません。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。ネイティブ形式は
**ユニットテンプレート** から組み立てられる点に注意してください: 質量項を持つグループの場合、生の混合値は
グラム単位の積となる一方、型付きインスタンスは名前付き単位でその値を保持します。

| 形式             | 式                                                                       |
|------------------|-------------------------------------------------------------------------------------|
| 型付き演算子   | `luminousFlux / power`                                                            |
| ネイティブ形式（`toX()`） | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## グループでの計算

| 式                          | 結果型                     | 意味                |
|--------------------------------------|----------------------------------|-------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | 必要な電力     |

## 実例 — 3種類の電球を比較する

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 — 物理的上限の17%

// 3000 lmを得るためにLEDテープに必要な電力は？
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたlm/W値** を比較するため、
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)` となります。`toString()` は値を基本単位で表示します:
`"120.0 lm/W"`。

## 関連項目

* [光束](luminous-flux.ja.md) — 分子。
* [放射強度](radiant-intensity.ja.md) と [放射輝度](radiance.ja.md) — 橋渡しの放射量側。
* [電力（電気）](../electrical/power.md) — 分母。
* [光学の概要](overview.ja.md)
</content>
