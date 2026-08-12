# 光露光量

パッケージ: `org.pcsoft.framework.kunit.optic.luminousexposure`
基本単位: **ルクス秒**（`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`）

種別: **構成単位**

光露光量 `H` は **時間積算された** 照度です: `H = E · t`。これは面が受け取った*光の総量*であり — 美術館の学芸員が
顔料の褪色を抑えるために年間ルクス時単位で予算化する量であり、カメラの露出値の背後にある量でもあります。

その正準基本次元標準形は `luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹` です。

## 単位

| 単位       | Enum値                          | 記号 |        トークン | 1単位（lx·s） |
|------------|-------------------------------------|--------|-------------:|---------------:|
| ルクス秒 | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |            1.0 |
| ルクス時   | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |           3600 |

すべてのトークンはあらゆるSI接頭辞を受け付けます（`kilo.luxHours` は年間光量予算の一般的な単位です）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。

| 形式             | 式                                                                   |
|------------------|--------------------------------------------------------------------------|
| 型付き演算子   | `illuminance * time`                                                         |
| ネイティブ形式（`toX()`） | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## グループでの計算

| 式                        | 結果型                     | 意味                    |
|-----------------------------------|---------------------------------|-----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | 平均照度        |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | 露光時間          |

## 実例 — 美術館の光量予算

繊細な水彩画は年間 **50,000 lx·h** に制限されます。展示照度50 lx、開館時間1日8時間の場合、何日間展示できるでしょうか？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // 年間125日の開館日数

// 逆に、200 lxでは何時間展示できるか？
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0時間
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたlx·s値** を比較するため、`(1 of luxHours) == (3600 of luxSeconds)` となります。
`toString()` は値を基本単位で表示します: `"3600.0 lx*s"`。

## 関連項目

* [照度](illuminance.ja.md) — この量が積算される元となる速度。
* [光エネルギー](luminous-energy.ja.md) — 照度ではなく光束に対する同様の考え方。
* [光学の概要](overview.ja.md)
</content>
