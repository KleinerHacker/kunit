# 放射輝度

パッケージ: `org.pcsoft.framework.kunit.optic.radiance`
基本単位: **ワット毎ステラジアン平方メートル**
（`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`）

種別: **構成単位**

放射輝度 `Lₑ` は **発光面積あたり** の放射強度です: `Lₑ = Iₑ / A`。これは [輝度](luminance.ja.md) の **放射量** としての
対応量であり、リモートセンシングやサーモグラフィが扱う量です — カメラのピクセルが実際に積分しているもので、面までの
距離とは無関係です。

その正準基本次元標準形は `mass¹ · time⁻³ · solidAngle⁻¹` です。長さの指数は相殺されます: ワットが `distance²` を、
面積が `distance⁻²` をそれぞれ寄与するためです。

## 単位

| 単位                            | Enum値                                    | 記号       |                            トークン | 1単位（W/(sr·m²)） |
|---------------------------------|-----------------------------------------------|--------------|---------------------------------:|--------------------:|
| ワット毎ステラジアン平方メートル | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER` | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

このトークンはあらゆるSI接頭辞を受け付けます（`milli.wattsPerSteradianSquareMeter` など）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。このグループは質量項を持つため、
ネイティブ形式は **ユニットテンプレート** から組み立てられます。

| 形式             | 式                                                    |
|------------------|-----------------------------------------------------------------|
| 型付き演算子   | `radiantIntensity / area`                                     |
| ネイティブ形式（`toX()`） | `(5 of kilo.grams / s³ / sr).toRadiance()`                    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## グループでの計算

| 式                        | 結果型                     | 意味         |
|-----------------------------------|---------------------------------|-----------------|
| `radiantIntensity / area`         | `KRadianceUnitInstance`         | `Lₑ = Iₑ / A`   |
| `radiance * area`                 | `KRadiantIntensityUnitInstance` | `Iₑ = Lₑ · A`   |
| `radiantIntensity / radiance`     | `KAreaUnitInstance`             | 発光面積 |

## 実例 — サーモグラフィカメラのピクセル

**2 m²** の炉壁がカメラに向けて **10 W/sr** を放射しています。その放射輝度 — 距離に関わらずカメラが報告する値 — は次の通りです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// 同じ壁の0.5 m²の部分は、それに比例して少ない強度を放出する …
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 — しかし放射輝度は変わらない
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたW/(sr·m²)値** を比較するため、
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)` となります。`toString()` は値を
基本単位で表示します: `"5.0 W/(sr*m^2)"`。

## 関連項目

* [放射強度](radiant-intensity.ja.md) — 分子。
* [輝度](luminance.ja.md) — 測光量としての対応量。
* [熱流束密度](../thermodynamics/heat-flux-density.md) — 半球にわたって積分された放射輝度。
* [光学の概要](overview.ja.md)
</content>
