# 磁束密度

パッケージ: `org.pcsoft.framework.kunit.electric.magneticfluxdensity`
基本単位: **テスラ**(`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

種別: **構成された単位**

磁束密度 (磁気誘導 `B`)は **構成された**単位で、合成 `mass · time⁻² · current⁻¹`(`kg·s⁻²·A⁻¹`)です。
`KMagneticFluxDensityUnitInstance` は3つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `-2` の
`KTimeUnit.BASE`(秒)、指数 `-1` の `KElectricCurrentUnit.BASE`(アンペア) — をラップします。 ライブラリの質量成分は
**グラム**(キログラムではない)に正規化されるため、テスラは生の成分基準の1000倍です。 保存される値はテスラに正規化されます。

## 磁束密度の作成

磁束密度は名前付きトークンから、または分解 (下記参照)から作成します。名前付き単位は値1のトークンとして残ります (`of`/
`into` で使用):

| 磁束密度               | 記号    |               トークン | T 換算(1単位) |
|------------------------|---------|-----------------------:|--------------:|
| テスラ                 | `T`     |               `teslas` |           1.0 |
| ウェーバ毎平方メートル | `Wb/m²` | `webersPerSquareMeter` |           1.0 |
| ガウス(CGS-EMU)        | `G`     |                `gauss` |        1.0e-4 |
| ガンマ                 | `γ`     |               `gammas` |        1.0e-9 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します (`milli.teslas`、`micro.teslas`、`nano.teslas` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## 複数の分解

磁束密度はいくつかの **等価な分解**から得られ、いずれも同じ値の等しい磁束密度を生成します:

| 式                     | 結果の型                           | 意味                                 |
|------------------------|------------------------------------|--------------------------------------|
| `flux / area`          | `KMagneticFluxDensityUnitInstance` | 定義 `B = Φ / A`                     |
| `mass/(time²·current)` | `.toMagneticFluxDensity()` 経由    | ネイティブな正規形 `kg·s⁻²·A⁻¹` の式 |

型付き演算子の形式は磁束密度を直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toMagneticFluxDensity()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。
両方の経路は値が等しくなります。

逆演算子は磁束・磁束密度・面積を結び付けます:

| 式                   | 結果の型                    | 意味              |
|----------------------|-----------------------------|-------------------|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A`       |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B`(可換) |
| `flux / fluxDensity` | `KAreaUnitInstance`         | `A = Φ / B`       |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

// 実例 - MRI 装置: 6 m² のコイルを貫く 18 Wb の磁束は 3 T の磁場です。
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance、3 T

// 同じ磁束密度をネイティブな kg·s⁻²·A⁻¹ の式で:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// 50 µT の地磁気が 2 m² のループを貫くと 1e-4 Wb の磁束になります。
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance、1e-4 Wb
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                                             | 意味                                                     |
|--------------|----------------------------------------------------|----------------------------------------------------------|
| `T`          | `teslas`                                           | 磁束密度、基本単位（名前付きトークン、テスラ）           |
| `Wb/m²`      | `webersPerSquareMeter`                             | 単位面積あたりの磁束としての磁束密度（名前付きトークン） |
| `kg/(s²·A)`  | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | 質量 / (時間²·電流) としての磁束密度（分数形式）         |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | 同じ磁束密度を純粋な積で表現                             |
| `µT`         | `micro.teslas`                                     | 接頭辞付きの磁束密度（マイクロテスラ）                   |
