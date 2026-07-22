# 温度差

パッケージ: `org.pcsoft.framework.kunit.temperature`
基本単位: **ケルビン**(`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

種別: **ネイティブ単位**

温度*差*は2つの温度の間の区間であり、アフィンで絶対的な[温度](temperature.md)グループとは対照的に**線形**な量です。
オフセットを**持たない**(ケルビンのスケールのみ)ため、通常の単位グループとして振る舞い、汎用エンジンをそのまま
通過します。

物理的には、これが2つの絶対温度を減算するとケルビンになる理由です:`30 °C − 10 °C = 20 ΔK` であり、`20 °C` では
ありません。`20 ΔK` の差は数値的に `20 °C` の差と等しい(ステップ幅が同じ)ため、このグループは意図的に
**ケルビンのみ**を提供し、**接頭辞はありません**。

## 単位

| 単位 | Enum 値 | 記号 | ケルビン変換 |
|---|---|---|---|
| ケルビン | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | 恒等 |

!!! note "記号は `K` ではなく `ΔK`"
    温度差は記号 **`ΔK`**(例:`"20.0 ΔK"`)で表示され、絶対ケルビン(`K`)とは意図的に区別されます。両者は同じ
    *次元*(ケルビン)ですが異なる量です — アフィンな点と線形な区間の違いです。したがって
    [混合単位](../../mixed-units.md)では `m·K`(絶対)と `m·ΔK`(差)は**同じ単位ではなく**、等価でも加算可能でも
    ありません。区別された記号によりそれが一目で分かります。

## 生成

差は汎用 `of` 動詞(絶対量専用)では生成しません。**2つの絶対温度の減算**、または `KTemperatureDifference.ofKelvin(…)`
ファクトリによる**明示的**な生成のいずれかで得られます — これにより「これは区間である」という意図が明確になります:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // 明示的、d1 と等しい
d1.value                                      // 20.0(ケルビン)
```

## 演算子

`+`/`-`/比較は通常の線形な同型演算子です(差と差の和は差):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

温度差は線形なので、(絶対温度とは異なり)型を保ったまま**数値でスケーリング**することもできます。

```kotlin
import org.pcsoft.framework.kunit.times

val doubled = KTemperatureDifference.ofKelvin(5) * 2 // KTemperatureDifferenceUnitInstance: 10 ΔK
```

差は絶対温度に加減算して再び絶対温度を得ることができます([温度](temperature.md)参照):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## 他の単位との混合

差を別のグループと乗除すると、汎用 `KMixedUnitInstance` になります:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## toString の書式

基本単位の `toString()` のみが存在します(ケルビン):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。 温度差はケルビンのスケールのみ（オフセットなし）を持ち、汎用の `of` ではなく明示的に構築します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 温度間隔、基本単位（ケルビン） |
| `30 °C − 10 °C` | `(30 of celsius) - (10 of celsius)` | 2つの絶対温度の差 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 2つの温度差の和 |
