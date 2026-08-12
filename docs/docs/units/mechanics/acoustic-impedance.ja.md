# 比音響インピーダンス

パッケージ: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
基本単位: **パスカル秒毎メートル**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

種別: **構成された単位**

比音響インピーダンス `Z` は、媒質が粒子速度の単位あたりに生じる音圧です:
`Z = p / v = ρ · c`。境界でどれだけの音が反射されるかを決定します — 空気は約 413 Pa·s/m、水は
約 1.48 MPa·s/m で、その比はおよそ 3600 です。これが空中の音がほとんど水中に入らない理由です。

その正準の基本次元標準形は `mass · length⁻² · time⁻¹` です。

## 名前付き単位

| 単位                    | 記号         |                   トークン | 1単位を Pa·s/m で |
|-------------------------|--------------|------------------------:|-----------------:|
| パスカル秒毎メートル     | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| SI レイル                | `rayl`       |                 `rayls` |              1.0 |
| CGS レイル               | `rayl (CGS)` |              `cgsRayls` |               10 |

`rayls` は基本単位の別表記であり、独自の単位ではありません。すべてのトークンは SI 接頭辞を受け入れます
(`mega.rayls` は組織や水でよく使われます)。隣接する力、圧力、密度のグループと同様に、インスタンスは
**グラム基準の生の成分値**を保持します。

## 分解

このグループには **2つ** の分解があります。どちらも同じ正規化ファクトリに集約されます:

| 形式               | 式                                                              |
|--------------------|------------------------------------------------------------------|
| 型付き演算子 A     | `pressure / speed`                                              |
| 型付き演算子 B     | `density * speed` (`Z = ρ · c`、特性インピーダンス)             |
| ネイティブ (`toX()`) | `(1 of kilo.grams / m² / s).toAcousticImpedance()`            |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## グループでの計算

| 式                                | 結果の型                            | 意味                    |
|------------------------------------|--------------------------------------|-------------------------|
| `pressure / speed`                | `KAcousticImpedanceUnitInstance`   | `Z = p / v`             |
| `density * speed`                 | `KAcousticImpedanceUnitInstance`   | `Z = ρ · c`             |
| `acousticImpedance * speed`       | `KPressureUnitInstance`            | 音圧                     |
| `pressure / acousticImpedance`    | `KSpeedUnitInstance`               | 粒子速度                 |
| `acousticImpedance / speed`       | `KDensityUnitInstance`             | `ρ` に戻る               |
| `acousticImpedance / density`     | `KSpeedUnitInstance`               | `c` に戻る               |

## 実例 — 空気と水の境界

なぜ水中の泳者の頭に向かって叫んでも聞こえないのでしょうか？2つの特性インピーダンスを比較してみます:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 — ほぼ完全反射
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化された成分値** を比較するため、`(1 of cgsRayls) == (10 of rayls)` です。
`toString()` は基本単位で値を表示します: `"413.0 Pa*s/m"`。

## 関連項目

* [密度](density.ja.md) と [速さ](../kinematics/speed.ja.md) — `Z = ρ · c` の2つの因子。
* [圧力](pressure.ja.md) — 音圧側。
* [力学の概要](overview.ja.md)
