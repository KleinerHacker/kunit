# 光度

パッケージ: `org.pcsoft.framework.kunit.optic.luminousintensity`
基本単位: **カンデラ**（`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`）

種別: **ネイティブ単位**

光度 `I` は光源が特定の方向へ **単位立体角あたりに** 放出する光束です。その単位であるカンデラは **7番目のSI基本単位** —
そして人間の知覚を通じて定義される唯一の基本単位です: 1 cdは、その方向に1/683 W/srの放射強度を持つ540 THzの単色放射を
放出する光源の強度です。

このグループは（指数特化されたサブタイプを持たない）**単純な1次元** のネイティブグループです:
`KLuminousIntensityUnitInstance` は単一の `KLuminousIntensityUnit.CANDELA` 項をラップし、常にカンデラに正規化されて
保持されます。

## 単位

| 単位            | Enum値                                | 記号   |          トークン | 1単位（カンデラ） |
|-----------------|-------------------------------------------|----------|---------------:|-------------------:|
| カンデラ         | `KLuminousIntensityUnit.CANDELA`          | `cd`     |     `candelas` |                1.0 |
| ヘフナーローソク   | `KLuminousIntensityUnit.HEFNER_CANDLE`    | `HK`     | `hefnerCandles` |              0.903 |
| キャンドルパワー     | `KLuminousIntensityUnit.CANDLEPOWER`      | `cp`     |  `candlepower` |              0.981 |
| カルセル          | `KLuminousIntensityUnit.CARCEL`           | `carcel` |      `carcels` |               9.74 |

この3つの非SI単位は、カンデラ以前に用いられていた歴史的な国別標準です — ドイツのヘフナーランプ、イギリスの国際キャンドル、
フランスのカルセル油ランプです。古い仕様書をそのまま読めるように保持されています。

各トークンは `of`（構築）と `into`（読み取り）で使われる、値1の `KLuminousIntensityUnitInstance` です。すべてのトークンは
あらゆるSI接頭辞を受け付けます（`milli.candelas`、`kilo.candelas` など）。

## グループでの計算

| 式                       | 結果型                     | 意味                          |
|----------------------------------|----------------------------------|-----------------------------------|
| `luminousIntensity + …`          | `KLuminousIntensityUnitInstance` | 同一型の加算               |
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`     | `Φ = I · Ω`、放出される光束    |
| `luminousIntensity / area`       | `KLuminanceUnitInstance`        | `L = I / A`、面の輝き  |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | 光束からの逆算                   |

ネイティブ形式は `toLuminousIntensity()` で変換します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## 実例 — 車のヘッドライト

ロービームヘッドライトは光軸上で **1200 cd** と規定されています。0.05 srの円錐に広がった場合、道路に実際に向けられる
光束は次の通りです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // ビーム円錐内で60.0 lm
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたカンデラ値** を比較するため、`(1 of candelas) == (1000 of milli.candelas)` となります。
`toString()` は値を基本単位で表示します: `"1200.0 cd"`。

## 関連項目

* [光束](luminous-flux.ja.md) — 立体角にわたって積分された強度。
* [輝度](luminance.ja.md) — 発光面積あたりの強度。
* [放射強度](radiant-intensity.ja.md) — 目による重み付けのない、放射量としての対応量。
* [光学の概要](overview.ja.md)
</content>
