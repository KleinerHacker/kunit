# ひずみ

パッケージ: `org.pcsoft.framework.kunit.mechanic.strain`
基本単位: **無次元比**(`KStrainUnit.BASE == KStrainUnit.RATIO`)

種別: **構成された単位**

ひずみ `ε = ΔL / L` は物体の相対的な変形量です。これは長さを長さで割ったものであり **無次元**ですが、 その読み取り方
(パーセント、パーミル、マイクロひずみ)は独自の単位語彙を形成するため、KUnitはこれを 独自のグループとしてモデル化しています。

`KStrainUnitInstance` は、指数1の単一の `KStrainUnit.BASE` 項からなる `KMixedUnitInstance` をラップ し、常に無次元比に正規化されます。

!!! note "演算子ではなく `toStrain()` である理由"
汎用エンジンは `length / length` を単位項を **持たない**混合単位として表現します。
`KLengthUnitInstance.div` はメンバー演算子であるためオーバーライドできず、そのためネイティブの 分解表現は型付き演算子ではなく形式認識フック
`toStrain()` を通じて到達します。

## 名前付き単位

| 単位           | 記号 |      トークン | 比としての1単位 |
|----------------|------|--------------:|----------------:|
| 無次元比(m/m)  | `1`  |       `ratio` |             1.0 |
| パーセント     | `%`  |     `percent` |            0.01 |
| パーミル       | `‰`  |    `perMille` |            1e-3 |
| マイクロひずみ | `µe` | `microstrain` |            1e-6 |

すべての単位はSI接頭辞の全範囲を受け付けるため、`micro.ratio` はマイクロひずみの別の表記です。

## ひずみの作成

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// 1 mの棒が2 mm伸びた場合
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## ひずみでの計算

| 式                                       | 結果の型                | 意味                         |
|------------------------------------------|-------------------------|------------------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L`(ネイティブ形式) |
| `stress / strain`                        | `KPressureUnitInstance` | 弾性係数 `E = σ / ε`         |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | 応力 `σ = E · ε`             |
| `strain + strain`, `strain - strain`     | `KStrainUnitInstance`   | 同一型の算術演算             |

フックの法則の弾性係数側については[応力](stress.md)のページを参照してください。

## 実例: 鋼棒のひずみゲージ

鋼棒 (E = 210 GPa)に取り付けたひずみゲージが950 µeを示しています。これはどの力学的応力に相当し、 2 mの棒はどれだけ伸びるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // 長さのスカラー倍
elongation into milli.meters                    // 1.9
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1"(基本単位: 無次元比)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode
の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な
Kotlin の両形式を併記します。

| 数学         | Kotlin                         | 意味                             |
|--------------|--------------------------------|----------------------------------|
| `1`(m/m)     | `ratio`                        | ひずみ、基本単位(無次元)         |
| `%`          | `percent`                      | パーセントの読み取り方           |
| `‰`          | `perMille`                     | パーミルの読み取り方             |
| `µe`         | `microstrain`                  | ひずみゲージの読み取り方(1 µm/m) |
| `ε = ΔL / L` | `(length / length).toStrain()` | ネイティブの分解表現             |
| `σ = E · ε`  | `pressure * strain`            | フックの法則                     |
| `E = σ / ε`  | `stress / strain`              | 弾性係数                         |
