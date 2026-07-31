# 線密度

パッケージ: `org.pcsoft.framework.kunit.mechanic.lineardensity`
基本単位: **キログラム毎メートル**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

種別: **構成された単位**

線密度は単位長さあたりの質量です — [面密度](areadensity.md)(`kg/m²`)と[密度](density.md)(`kg/m³`)の 一次元版です。
**構成された**単位です — 合成 `mass · length⁻¹`(`kg/m`)。

`KLinearDensityUnitInstance` は、正準の正規形でちょうど2つの項をラップする `KMixedUnitInstance` です:
指数 `+1` の `KMassUnit.BASE`(グラム)と指数 `-1` の `KDistanceUnit.BASE`(メートル)。このライブラリの
質量成分はグラムに正規化されているため、保存される値は生のグラム基準の成分値で、kg/m での読み取りは 固定係数で除算されます。

## 名前付き単位

| 単位                   | 記号    |             トークン | kg/mでの1単位 |
|------------------------|---------|---------------------:|--------------:|
| キログラム毎メートル   | `kg/m`  |  `kilogramsPerMeter` |           1.0 |
| グラム毎メートル       | `g/m`   |      `gramsPerMeter` |          1e-3 |
| グラム毎センチメートル | `g/cm`  | `gramsPerCentimeter` |           0.1 |
| テックス(繊維)         | `tex`   |                `tex` |          1e-6 |
| デニール(繊維)         | `den`   |             `denier` |   ≈ 1.1111e-7 |
| ポンド毎フィート       | `lb/ft` |      `poundsPerFoot` |     ≈ 1.48816 |

すべての単位がSI接頭辞の全範囲に対応しています。繊維業界のデシテックスは `deci.tex` です。

## 基本単位による計算

| 式                                                 | 結果の型                     | 意味          |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`, `length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

ネイティブ形式も利用できます: 一般エンジンで構築されたグラム毎メートルの式は `toLinearDensity()` で 変換されます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true - どちらも 0.5 kg/m
typed into gramsPerMeter        // 500.0
```

## 実例: ドラムに巻かれた鋼鉄ケーブル

鋼鉄ケーブルの重さは2.6 kg/mです。45 mの長さの質量はいくつで、500 kgの荷重上限ではどれだけの長さの ケーブルを許容できるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true(繊維単位の関係)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m"(基本単位)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学          | Kotlin                         | 意味                               |
|---------------|--------------------------------|------------------------------------|
| `kg/m`        | `kilogramsPerMeter`            | 線密度、基本単位(名前付きトークン) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | 同じ量を純粋な積で表現             |
| `tex`         | `tex`                          | 繊維の線密度(1 g/km)               |
| `ρ_l = m / l` | `mass / length`                | 型付き分解表現                     |
| `m = ρ_l · l` | `lineardensity * length`       | 質量について解く                   |
| `dtex`        | `deci.tex`                     | 接頭辞付きの繊維読み取り方         |
