# 面密度

パッケージ: `org.pcsoft.framework.kunit.areadensity`
基本単位: **キログラム毎平方メートル**(`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

面密度(面質量 / 面荷重、構造力学で一般的)は**構成された**単位で、合成 `mass · length⁻²`(`kg/m²`)です。
`KAreaDensityUnitInstance` は2つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)と指数 `-2` の
`KDistanceUnit.BASE`(メートル) — をラップします。保存される値は生のグラム基準の成分値で、kg/m² での読み取りは
固定係数で除算されます。

## 面密度の作成

密度と同様に、面密度には**素のトークンはありません** — すべての表記(kg/m²、g/mm² など)は比です。式として、
または型付きの `mass / area` 演算子で作成し、そのような式に対して `into` で読み戻します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005(mm² あたり)
```

## 基本単位(質量、面積、密度)による計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | 面密度 = m / A |
| `area density * area` | `KMassUnitInstance` | 質量 = q · A |
| `area * area density` | `KMassUnitInstance` | 質量(可換) |
| `mass / area density` | `KAreaUnitInstance` | 面積 = m / q |
| `density * length` | `KAreaDensityUnitInstance` | 指定材料・厚さの板 |
| `area density / length` | `KDensityUnitInstance` | 体積密度に戻す |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// 厚さ 3 m の板: 密度 × 厚さ = 面質量
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance, 2000 kg/m³
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²"(基本単位)
```
