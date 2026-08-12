# 電束

パッケージ: `org.pcsoft.framework.kunit.electric.flux`
基本単位: **ボルトメートル**(`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

種別: **構成された単位**

電束 `Φ_E` は、電界強度を面積にわたって積分したものです: `Φ_E = E · A`。これはガウスの法則が記述する量であり —
閉曲面を通る電束は、内包する電荷を誘電率で割った値に等しい、というものです。

その正規の基本次元標準形は `mass · length³ · time⁻³ · current⁻¹` です。

!!! note "電束密度ではありません"
    [電束密度](electricfluxdensity.ja.md) `D`(`C/m²`)は異なる次元を持つ別の量です。このページは電束そのもの、
    すなわち `V·m` について扱います。

## 名前付き単位

| 単位            | 記号    |             トークン | 1単位のV·m値 |
|-----------------|---------|------------------:|--------------:|
| ボルトメートル  | `V*m`   |      `voltMeters` |           1.0 |
| ボルトセンチメートル | `V*cm`  | `voltCentimeters` |          0.01 |

すべてのトークンはあらゆる SI 接頭辞を受け付けます(`kilo.voltMeters` など)。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。ネイティブ形式は
**ユニットテンプレート**から組み立てられます。グループが質量項を持つためです: 生のミックス値はグラム基準の積であり、
型付きインスタンスは名前付き単位で値を保持します。

| 形式             | 式                                                     |
|------------------|-----------------------------------------------------------------|
| 型付き演算子     | `electricFieldStrength * area`                                 |
| ネイティブ (`toX()`) | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## グループでの計算

| 式                                 | 結果の型                            | 意味        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | 面積       |

## 実例 — コンデンサ極板を通る電束

**1000 V/m** の電界が 0.5 m × 0.25 m の極板を通過します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// この電束が示唆する、その極板における電界
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたV·m値**を比較するため、`(1 of voltMeters) == (100 of voltCentimeters)` となります。
`toString()` は値を基本単位で表示します: `"125.0 V*m"`。

## 関連項目

* [電界強度](electricfieldstrength.ja.md) — 積分される電界。
* [電束密度](electricfluxdensity.ja.md) — 次元の異なる `D` 場。
* [電気工学の概要](overview.ja.md)
