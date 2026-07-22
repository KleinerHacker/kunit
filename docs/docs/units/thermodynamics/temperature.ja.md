# 絶対温度

> **温度**トピックの一部です — [概要](temperature-overview.md)および線形の対応物
> [温度差](temperature-difference.md)を参照してください。

パッケージ: `org.pcsoft.framework.kunit.temperature`
基本単位: **ケルビン**(`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

種別: **ネイティブ単位**

温度グループは熱力学温度をモデル化します。これはフレームワーク**最初の(そして設計上恒久的な)アフィン例外**です:
他のすべてのグループと異なり、温度単位間の変換は単一の乗算係数ではなく、**オフセットとスケール**(アフィン)変換です
— `25 °C` は `25 × 1 °C` では**ありません**。値は**絶対ケルビン**に正規化して保存されるため、`*`/`/`/`pow` は
汎用エンジンをそのまま通過します。

このグループを特別にしているのは2点です:

* **オーバーロードではなくフックによるアフィン変換。** 共有エンジンは純粋な乗算のままです。アフィン変換は2つの
  measurable フック `scaledBy`(作成、`of` の裏側)と `readBaseValue`(読み取り、`into` の裏側)を通じて注入され
  ます。そのため `25 of celsius` や `t into fahrenheit` は通常の動詞で機能し、グループ固有の `of`/`into`
  オーバーロード(明示的にインポートした汎用動詞に隠されてしまう)は不要です。
* **接頭辞なし。** 温度グループは意図的に接頭辞ビルダーを**提供しません**(`milli.celsius` はモデル化されません)。
  `KTemperatureUnitExtensions.kt` はありません。

## 単位

| 単位 | Enum 値 | 記号 | トークン | ケルビン変換 |
|---|---|---|---:|---|
| ケルビン | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | 恒等 |
| セルシウス度 | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| ファーレンハイト度 | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| ランキン度 | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

各 `トークン` は値1の `KTemperatureUnitInstance` であり、`of`(作成)と `into`(読み取り)で使用します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15(絶対ケルビンに正規化)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0(摂氏/華氏の交点)
```

## 演算子

絶対温度はアフィンな**点**であり、ベクトルではありません。そのため演算は意図的に非対称です — これが物理的に正しい
挙動です([温度差](temperature-difference.md)も参照):

* `AbsTemp − AbsTemp` → **`KTemperatureDifferenceUnitInstance`**(両者間のケルビン*区間*。例:`30 °C − 10 °C = 20 ΔK`、
  `20 °C` では**ない**)。
* `AbsTemp ± 差` → 再び絶対温度。
* `AbsTemp + AbsTemp` → **コンパイルエラー**(2つの絶対温度の加算は物理的に無意味)。
* `AbsTemp * number` / `AbsTemp / number` → **コンパイルエラー**: アフィンな点を数値でスケーリングするのは無意味です(ケルビン値が −273.15 のオフセットを含むため)。代わりに線形の[温度差](temperature-difference.md)をスケーリングしてください。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// 絶対 − 絶対 = 温度差(ケルビン)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// 絶対 ± 差 = 絶対温度
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // コンパイルされません

// 比較(絶対ケルビンによる)
(0 of celsius) == (273.15 of kelvin)      // true(同じ絶対温度)
(100 of celsius) > (100 of fahrenheit)    // true
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は正規化された絶対ケルビン `value` を比較します。`equals` は作成単位に依存せず
絶対温度で比較するため、`(0 of celsius) == (273.15 of kelvin)` です。

## `pow` によるべき乗

中置 `pow` 演算子で整数べき乗を計算します。温度グループでは `pow` は汎用 `KMixedUnitInstance` を返し(温度には
次元付きのべき乗型がありません)、絶対ケルビン項に対して線形に作用します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## 他の単位との混合

温度を別のグループと乗除すると、汎用 `KMixedUnitInstance` になります(標準化された温度の組み合わせは存在しません)。
計算は絶対ケルビン値に対して行われます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## toString の書式

基本単位の `toString()` のみが存在します。特定の単位で書式化するには `into` を使用します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K"(基本単位表現)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。 温度はアフィンなので `·`／指数の積形式はありません。名前付き単位とオフセット変換のみです。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `K` | `kelvin` | 絶対温度、基本単位（ケルビン） |
| `°C` | `celsius` | セルシウス度（`K = °C + 273.15`） |
| `°F` | `fahrenheit` | 華氏度 |
| `25 °C` | `25 of celsius` | 絶対温度を構築 |
