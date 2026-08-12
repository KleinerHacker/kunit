# 輝度

パッケージ: `org.pcsoft.framework.kunit.optic.luminance`
基本単位: **カンデラ毎平方メートル**（`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`）

種別: **構成単位**

輝度 `L` は **発光面積あたり** の光度です: `L = I / A`、つまり `1 cd/m² = 1 nit`。これは目が実際に面の「明るさ」として
知覚する量であり、あらゆるディスプレイの仕様に記載されている数値です — 一般的なオフィス用モニターは250〜350ニト、HDRテレビは
1000ニット以上です。

その正準基本次元標準形は `luminousIntensity¹ · distance⁻²` です。

## 単位

| 単位                     | Enum値                                | 記号   |                    トークン | 1単位（cd/m²） |
|--------------------------|---------------------------------------------|----------|-------------------------:|----------------:|
| カンデラ毎平方メートル | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` | `candelasPerSquareMeter` |             1.0 |
| ニト                      | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` |                   `nits` |             1.0 |
| スチルブ                    | `KLuminanceUnit.STILB`                    | `sb`     |                 `stilbs` |          10 000 |
| アポスチルブ                 | `KLuminanceUnit.APOSTILB`                 | `asb`    |              `apostilbs` |           1 / π |
| ランバート                  | `KLuminanceUnit.LAMBERT`                  | `L`      |               `lamberts` |        10⁴ / π  |
| フットランバート             | `KLuminanceUnit.FOOT_LAMBERT`             | `fL`     |           `footLamberts` |      ≈ 3.426259 |

`nits` は基本単位の別表記であり、独自の単位ではありません — これはディスプレイ業界がカンデラ毎平方メートルを呼ぶ名前です。
アポスチルブ、ランバート、フットランバートは*ランバーシアン*ファミリーに属し、理想的な拡散発光体の照度を輝度に変換する
係数 `1/π` を含みます。すべてのトークンはあらゆるSI接頭辞を受け付けます。

## 分解

このグループには **2つ** の分解があります。どちらも同じ正規化ファクトリに集約されるため、同じ型付きで値の等しい
インスタンスを生成します。

| 形式                   | 式                                                     |
|------------------------|------------------------------------------------------------------------|
| 型付き演算子A       | `luminousIntensity / area`                                     |
| 型付き演算子B       | `illuminance / solidAngle`                                     |
| ネイティブ形式（`toX()`）       | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## グループでの計算

| 式                     | 結果型                      | 意味                    |
|--------------------------------|-----------------------------------|-----------------------------|
| `luminousIntensity / area`     | `KLuminanceUnitInstance`         | `L = I / A`                |
| `illuminance / solidAngle`     | `KLuminanceUnitInstance`         | `L = E / Ω`                |
| `luminance * area`             | `KLuminousIntensityUnitInstance` | `I = L · A`                |
| `luminance * solidAngle`       | `KIlluminanceUnitInstance`       | `E = L · Ω`                |
| `luminousIntensity / luminance` | `KAreaUnitInstance`             | 発光面積          |
| `illuminance / luminance`      | `KSolidAngleUnitInstance`        | 光が広がる円錐 |

## 実例 — モニターのニト表示

**0.21 m²** のパネルを持つ27インチモニターの定格は **300ニト** です。これは正面方向の合計光度に換算すると次のようになります。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6（ヤード・ポンド法での値）
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたcd/m²値** を比較するため、`(1 of stilbs) == (10000 of candelasPerSquareMeter)` となります。
`toString()` は値を基本単位で表示します: `"250.0 cd/m^2"`。

## 関連項目

* [光度](luminous-intensity.ja.md) — 輝度の分子。
* [照度](illuminance.ja.md) — 面から出る光ではなく面に到達する光。
* [放射輝度](radiance.ja.md) — 放射量としての対応量。
* [光学の概要](overview.ja.md)
</content>
