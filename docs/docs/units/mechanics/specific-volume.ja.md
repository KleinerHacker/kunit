# 比容積

パッケージ: `org.pcsoft.framework.kunit.mechanic.specificvolume`
基本単位: **立方メートル毎キログラム**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

種別: **構成された単位**

比容積 `v` は単位質量あたりが占める体積 — **[密度](density.md)の逆数**です。これは **構成された**単位で あり、`長さ³・質量⁻¹`
(`m³/kg`)の組み合わせです。

`KSpecificVolumeUnitInstance` は、正規化された形式でちょうど2つの項からなる `KMixedUnitInstance` を ラップします:
`KDistanceUnit.BASE`(メートル)を指数`+3`で、`KMassUnit.BASE`(グラム)を指数`-1`で。
このライブラリの質量成分はグラムに正規化されているため、格納される値は生のグラム基準の成分値であり、
m³/kgでの読み取り方は固定係数で橋渡しされます。

## 名前付き単位

| 単位                       | 記号      |                  トークン | m³/kgでの1単位 |
|----------------------------|-----------|--------------------------:|---------------:|
| 立方メートル毎キログラム   | `m^3/kg`  |  `cubicMetersPerKilogram` |            1.0 |
| リットル毎キログラム       | `l/kg`    |       `litersPerKilogram` |           1e-3 |
| 立方センチメートル毎グラム | `cm^3/g`  | `cubicCentimetersPerGram` |           1e-3 |
| 立方フィート毎ポンド       | `ft^3/lb` |       `cubicFeetPerPound` |    ≈ 0.0624280 |

すべての単位はSI接頭辞の全範囲を受け付けます (`milli.cubicMetersPerKilogram`)。

## 中核となる単位での計算

| 式                                               | 結果の型                      | 意味        |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`, `mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

逆数演算子は型付けされています: `1 / density` は汎用の混合単位に劣化することなく実際の単位型を保持 します。ネイティブ形式は
`toSpecificVolume()` で変換されます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true - 正確な往復変換
```

## 実例: 蒸気表の参照

1 barでの飽和蒸気は約1.694 m³/kgの比容積を持ちます。その蒸気2 kgはどれだけの体積を占め、密度は いくつでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true (同じ値)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg"(基本単位)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学        | Kotlin                                 | 意味                               |
|-------------|----------------------------------------|------------------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | 比容積、基本単位(名前付きトークン) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | 同じ量を純粋な積として             |
| `l/kg`      | `litersPerKilogram`                    | リットル毎キログラムの読み取り方   |
| `v = V / m` | `volume / mass`                        | 型付けされた分解表現               |
| `v = 1 / ρ` | `1 / density`                          | 密度の逆数                         |
| `ρ = 1 / v` | `1 / specificvolume`                   | 密度に戻す                         |
