# 密度

パッケージ: `org.pcsoft.framework.kunit.density`
基本単位: **キログラム毎立方メートル**(`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

密度(質量密度)は**構成された**単位で、合成 `mass · length⁻³`(`kg/m³`)です。`KDensityUnitInstance` は2つの
項 — 指数 `+1` の `KMassUnit.BASE`(グラム)と指数 `-3` の `KDistanceUnit.BASE`(メートル) — をラップします。
保存される値は生のグラム基準の成分値で、kg/m³ での読み取りは固定係数で除算されます。

## 密度の作成

密度には**素のトークンはありません** — すべての表記(kg/m³、g/cm³ など)は比です。式として、または型付きの
`mass / volume` 演算子で作成し、そのような式に対して `into` で読み戻します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785(= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## 基本単位(質量と体積)による計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | 密度 = m / V |
| `density * volume` | `KMassUnitInstance` | 質量 = ρ · V |
| `volume * density` | `KMassUnitInstance` | 質量(可換) |
| `mass / density` | `KVolumeUnitInstance` | 体積 = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | 面密度(面密度を参照) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³"(基本単位)
```
