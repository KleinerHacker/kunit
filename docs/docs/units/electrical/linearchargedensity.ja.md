# 線電荷密度

パッケージ: `org.pcsoft.framework.kunit.electric.linearchargedensity`
基本単位: **メートルあたりのクーロン**
(`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`)

種別: **構成単位（constructed unit）**

線電荷密度は **構成単位**です。組成は `電流 · 時間 · 長さ⁻¹`
（`A·s·m⁻¹` = `C/m`）です。`KLinearChargeDensityUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします —
`KElectricCurrentUnit.BASE`（アンペア）を `+1`、`KTimeUnit.BASE`（秒）を `+1`、`KDistanceUnit.BASE`
（メートル）を `-1` として保持します。このグループには質量次元が含まれないため、グラム／キログラムの橋渡しは不要です。
格納される値は常にメートルあたりのクーロンに正規化されています。

線電荷密度 `λ` は、たとえば導線や帯電したフィラメントに沿って単位長さあたりに運ばれる電荷です。
**専用の名前付き単位を持ちません**。すべての表記は比（C/m、µC/cm）であるため、このグループには
裸のトークンや接頭辞ビルダーは存在せず、値は式または型付き演算子から組み立てられます。二次元・三次元の
対応物はそれぞれ[電束密度](electricfluxdensity.md)（C/m²）と[電荷密度](chargedensity.md)（C/m³）です。

## 線電荷密度を組み立てる

名前付きトークンはありません。電荷を長さで割って値を組み立てます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6（C/mに正規化）
```

## 複数の分解表現

線電荷密度は複数の **等価な分解表現**を通じて到達でき、いずれも同じ値として等しい密度を生成します。

| 表現                  | 結果の型                           | 意味                                |
|-----------------------|------------------------------------|-------------------------------------|
| `charge / length`     | `KLinearChargeDensityUnitInstance` | `λ = Q / l`、長さに沿って広がる電荷 |
| `current·time/length` | `.toLinearChargeDensity()` 経由    | ネイティブの正準 `A·s·m⁻¹` 表現     |

型付き演算子形式は直接線電荷密度を返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toLinearChargeDensity()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。両方の経路は値として等しくなります。

逆演算子は電荷、長さ、密度を結び付けます。

| 表現                           | 結果の型              | 意味                |
|--------------------------------|-----------------------|---------------------|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l`（可換） |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ`         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

// 実例 - 2 mにわたって5 µCを運ぶフィラメントの線電荷密度は2.5 µC/mになる。
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// 電荷について解き戻す:
val q = lambda * (2 of meters)                       // KChargeUnitInstance, 5 µC
q into micro.coulombs                                // 5.0

// 同じ密度をネイティブの A·s·m⁻¹ 表現として:
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学      | Kotlin                                                 | 意味                                         |
|-----------|--------------------------------------------------------|----------------------------------------------|
| `C/m`     | `(1 of coulombs) / (1 of meters)`                      | 線電荷密度、基本単位（名前付きトークンなし） |
| `Q / l`   | `(5 of micro.coulombs) / (2 of meters)`                | 長さに沿った電荷からの密度                   |
| `λ · l`   | `lambda * (2 of meters)`                               | 長さが運ぶ電荷                               |
| `A·s/m`   | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | 電流・時間 / 長さ としての密度（分数形式）   |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)`  | 純粋な積としての同じ密度                     |
