# 光束

パッケージ: `org.pcsoft.framework.kunit.optic.luminousflux`
基本単位: **ルーメン**（`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`）

種別: **構成単位**

光束 `Φ` は光源がカバーするすべての方向に放出する **可視光の総量** です — あらゆるランプのパッケージに印刷されている数値です。
これは光度を立体角にわたって積分したものです: `Φ = I · Ω`、つまり `1 lm = 1 cd·sr`。

その正準基本次元標準形は `luminousIntensity¹ · solidAngle¹` です。

## 単位

| 単位               | Enum値                            | 記号  |               トークン | 1単位（ルーメン） |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| ルーメン              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| カンデラステラジアン  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians` はルーメンの定義を書き下したものであり — 数値的には同一ですが、単位がどこから来るかを式で明示できます。
両方のトークンはあらゆるSI接頭辞を受け付けます（`kilo.lumens`、`milli.lumens` など）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。

| 形式                | 式                                                       |
|---------------------|--------------------------------------------------------------------|
| 型付き演算子      | `luminousIntensity * solidAngle`                                  |
| ネイティブ形式（`toX()`）    | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## グループでの計算

| 式                       | 結果型                      | 意味                       |
|----------------------------------|-----------------------------------|--------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | 光束が広がる円錐 |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## 実例 — 等方性電球

裸電球はすべての方向に均等に放射します。全球は `4π sr` なので、100 cdの光源は次の光束を放出します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm — おおよそ100Wの白熱電球に相当
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたルーメン値** を比較するため、`(1 of lumens) == (1000 of milli.lumens)` となります。
`toString()` は値を基本単位で表示します: `"800.0 lm"`。

## 関連項目

* [光度](luminous-intensity.ja.md) — 立体角あたりの光束。
* [照度](illuminance.ja.md) — 照らされる面積あたりの光束。
* [発光効率](luminous-efficacy.ja.md) — 電力1ワットあたりの光束。
* [光学の概要](overview.ja.md)
</content>
