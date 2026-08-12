# 放射強度

パッケージ: `org.pcsoft.framework.kunit.optic.radiantintensity`
基本単位: **ワット毎ステラジアン**（`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`）

種別: **構成単位**

放射強度 `Iₑ` は光源が **単位立体角あたりに** 放出する放射束（電力）です: `Iₑ = P / Ω`。これは
[光度](luminous-intensity.ja.md) の **放射量** としての対応量です — 幾何学的には同一ですが、ルーメンではなくワットで
測定されるため、目に見えない赤外線や紫外線を含むすべての放射をカウントします。

その正準基本次元標準形は `mass¹ · distance² · time⁻³ · solidAngle⁻¹` です。

## 単位

| 単位               | Enum値                                   | 記号 |               トークン | 1単位（W/sr） |
|--------------------|------------------------------------------------|--------|--------------------:|---------------:|
| ワット毎ステラジアン | `KRadiantIntensityUnit.WATT_PER_STERADIAN`   | `W/sr` | `wattsPerSteradian` |            1.0 |

このトークンはあらゆるSI接頭辞を受け付けます（`milli.wattsPerSteradian`、`kilo.wattsPerSteradian` など）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。このグループは質量項を持つため、
ネイティブ形式は **ユニットテンプレート** から組み立てられます（同様の注記は
[発光効率](luminous-efficacy.ja.md) も参照してください）。

| 形式             | 式                                                        |
|------------------|---------------------------------------------------------------------|
| 型付き演算子   | `power / solidAngle`                                              |
| ネイティブ形式（`toX()`） | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## グループでの計算

| 式                        | 結果型                       | 意味                    |
|-----------------------------------|------------------------------------|-----------------------------|
| `power / solidAngle`              | `KRadiantIntensityUnitInstance`   | `Iₑ = P / Ω`               |
| `radiantIntensity * solidAngle`   | `KPowerUnitInstance`              | `P = Iₑ · Ω`               |
| `power / radiantIntensity`        | `KSolidAngleUnitInstance`         | 広がる円錐 |
| `radiantIntensity / area`         | `KRadianceUnitInstance`           | `Lₑ = Iₑ / A`              |

## 実例 — 赤外線LED

IR発光素子は **20 mW** を0.2 srの円錐に放射します。その放射強度と、0.05 srの検出器開口が受け取る電力は次の通りです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // 5.0 mWが検出器に届く
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたW/sr値** を比較するため、
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)` となります。`toString()` は値を基本単位で表示します:
`"5.0 W/sr"`。

## 関連項目

* [光度](luminous-intensity.ja.md) — 測光量としての対応量。
* [放射輝度](radiance.ja.md) — 発光面積あたりの放射強度。
* [発光効率](luminous-efficacy.ja.md) — ワットとルーメンをつなぐ橋渡し。
* [光学の概要](overview.ja.md)
</content>
