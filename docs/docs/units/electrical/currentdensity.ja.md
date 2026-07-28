# 電流密度

パッケージ: `org.pcsoft.framework.kunit.electric.currentdensity`
基本単位: **平方メートルあたりのアンペア** (`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`)

種別: **構成単位（constructed unit）**

電流密度は**構成単位**です。組成は `電流 · 長さ⁻²`（`A/m²`）— 導体断面積あたりの電流です。
`KCurrentDensityUnitInstance` は2つの項からなる `KMixedUnitInstance` をラップします —
`KElectricCurrentUnit.BASE`（アンペア）を `+1`、`KDistanceUnit.BASE`（メートル）を `-2` として保持します。
両方の成分はそれぞれのグループの基本単位で格納されるため、値はそのままA/m²での読み値になります。

## 電流密度を組み立てる

電流密度には固有の**名前付きトークンがなく**、独自の接頭辞ビルダーもありません。あらゆる表記は比率
（`A/m²`、`A/mm²`、…）です。式として、または型付きの `current / area` 演算子で組み立て、
そのような式に対して `into` で読み戻します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## 複数の分解表現

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `current / area` | `KCurrentDensityUnitInstance` | 定義式 `J = I / A` |
| `current/length²` | `.toCurrentDensity()` 経由 | ネイティブの正準 `A·m⁻²` 表現 |

型付き演算子形式は直接電流密度を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toCurrentDensity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。両方の経路とも値として等しくなります。

逆演算子は電流、面積、電流密度を結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `currentDensity * area` | `KElectricCurrentUnitInstance` | `I = J · A`（可換） |
| `current / currentDensity` | `KAreaUnitInstance` | `A = I / J` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

// 実例 - 電線のサイジング: 2.5 mm²の銅線に16 Aを流すと6.4 A/mm²になる。
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// その密度である断面積が流せる電流を求める:
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance, 25.6 A

// 同じ密度をネイティブの A·m⁻² 表現として:
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`⁻²`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `A/m²` | `amperes / (meters pow 2)` | 電流密度、基本単位（分数形式） |
| `A·m⁻²` | `amperes * (meters pow -2)` | 純粋な積としての同じ電流密度 |
| `I / A` | `(16 of amperes) / crossSection` | 電流と面積からの電流密度 |
| `A/mm²` | `amperes / (milli.meters pow 2)` | 配線でよく使われる単位での電流密度 |
