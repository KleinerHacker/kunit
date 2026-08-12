# 照度

パッケージ: `org.pcsoft.framework.kunit.optic.illuminance`
基本単位: **ルクス**（`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`）

種別: **構成単位**

照度 `E` は **面に到達する** 光束を、その面の単位面積あたりで表したものです: `E = Φ / A`、つまり
`1 lx = 1 lm/m²`。これはあらゆる職場照明基準が記述されている量です — 光束と異なり、ランプまでの距離や照らされる面積の
大きさにも依存し、ランプ自体だけでは決まりません。

その正準基本次元標準形は `luminousIntensity¹ · solidAngle¹ · distance⁻²` です。

## 単位

| 単位         | Enum値                     | 記号 |         トークン | 1単位（ルクス） |
|--------------|--------------------------------|--------|--------------:|--------------:|
| ルクス          | `KIlluminanceUnit.LUX`         | `lx`   |         `lux` |           1.0 |
| フォト         | `KIlluminanceUnit.PHOT`        | `ph`   |       `phots` |        10 000 |
| フットキャンドル  | `KIlluminanceUnit.FOOT_CANDLE` | `fc`   | `footCandles` |    ≈ 10.76391 |
| ノックス          | `KIlluminanceUnit.NOX`         | `nx`   |         `nox` |         0.001 |

フォットはCGS単位（1 lm/cm²）、フットキャンドルはヤード・ポンド法単位（1 lm/ft²）であり、ノックスは月光のような非常に低い
照度レベルに用いられます。すべてのトークンはあらゆるSI接頭辞を受け付けます（`kilo.lux`、`milli.lux` など）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。

| 形式             | 式                                                             |
|------------------|------------------------------------------------------------------------|
| 型付き演算子   | `luminousFlux / area`                                                  |
| ネイティブ形式（`toX()`） | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## グループでの計算

| 式                 | 結果型                 | 意味                     |
|----------------------------|------------------------------|------------------------------|
| `luminousFlux / area`      | `KIlluminanceUnitInstance`  | `E = Φ / A`                 |
| `illuminance * area`       | `KLuminousFluxUnitInstance` | `Φ = E · A`                 |
| `luminousFlux / illuminance` | `KAreaUnitInstance`       | 光束が照らせる面積   |
| `illuminance / solidAngle` | `KLuminanceUnitInstance`    | `L = E / Ω`                 |
| `illuminance * time`       | `KLuminousExposureUnitInstance` | `H = E · t`             |

## 実例 — この机は十分に明るいか？

オフィス作業にはおよそ **500 lx** が必要です。800 lmの電球を2 m²の机の上に置くと得られる照度は次の通りです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 — 500 lxの目標には届かない
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // 目標達成には1000.0 lmが必要
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたルクス値** を比較するため、`(1 of phots) == (10000 of lux)` となります。
`toString()` は値を基本単位で表示します: `"500.0 lx"`。

## 関連項目

* [光束](luminous-flux.ja.md) — ランプが放出するもの。
* [輝度](luminance.ja.md) — 立体角あたりの照度、面の「明るさ」。
* [光露光量](luminous-exposure.ja.md) — 時間積算された照度。
* [光学の概要](overview.ja.md)
</content>
