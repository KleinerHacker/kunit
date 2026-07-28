# 電気双極子モーメント

パッケージ: `org.pcsoft.framework.kunit.electric.electricdipolemoment`
基本単位: **クーロンメートル**
(`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`)

種別: **構成単位（constructed unit）**

電気双極子モーメントは**構成単位**です。組成は `電流 · 時間 · 長さ`
（`A·s·m` = `C·m`）です。`KElectricDipoleMomentUnitInstance` は3つの項からなる `KMixedUnitInstance` をラップします —
`KElectricCurrentUnit.BASE`（アンペア）を `+1`、`KTimeUnit.BASE`（秒）を `+1`、`KDistanceUnit.BASE`
（メートル）を `+1` として保持します。このグループには質量次元が含まれないため、グラム／キログラムの橋渡しは不要です。
格納される値は常にクーロンメートルに正規化されています。

電気双極子モーメント `p = Q · d` は正負の[電荷](charge.md)の分離を測る量です。分子を
[電界強度](electricfieldstrength.md)と結び付ける量でもあります。

## 電気双極子モーメントを組み立てる

名前付きトークンで、または分解表現（下記参照）から双極子モーメントを組み立てられます。名前付き単位は値1の
トークンとして存在します（`of`/`into` と併用）。

| 双極子モーメント | 記号 | トークン | C·mでの1単位 |
|---|---|---:|---:|
| クーロンメートル | `C·m` | `coulombMeters` | 1.0 |
| デバイ（CGS） | `D` | `debyes` | 3.335640952e-30 |

デバイは分子物理学や化学で広く使われています。名前付き単位は `KPrefixBuilder` を通じてSI接頭辞をサポートします
（`pico.coulombMeters`、`milli.debyes` など）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val p = 1.85 of debyes        // 水分子
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## 複数の分解表現

電気双極子モーメントは複数の**等価な分解表現**を通じて到達でき、いずれも同じ値として等しいモーメントを生成します。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `charge * length` | `KElectricDipoleMomentUnitInstance` | `p = Q · d`、電荷とその分離距離の積（可換） |
| `current·time·length` | `.toElectricDipoleMoment()` 経由 | ネイティブの正準 `A·s·m` 表現 |

型付き演算子形式は直接双極子モーメントを返します。完全にネイティブな表現は汎用の
`KMixedUnitInstance` のままとなり、`toElectricDipoleMoment()`（正準の正規形のみを認識し、それ以外では
`IllegalStateException` を投げる）で絞り込まれます。両方の経路は値として等しくなります。

逆演算子は電荷、分離距離、モーメントを結び付けます。

| 表現 | 結果の型 | 意味 |
|---|---|---|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.dipolemoment.*

// 実例 - 1 pCが1 nm離れると1e-21 C·m、約3.0e8デバイになる。
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// 分離距離について解き戻す:
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance, 3 m

// 同じモーメントをネイティブの A·s·m 表現として:
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance（グループから外れる）
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m"（基本単位）
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `C·m` | `coulombMeters` | 電気双極子モーメント、基本単位（名前付きトークン、クーロンメートル） |
| `D` | `debyes` | CGSのデバイ、3.335 640 952e-30 C·m |
| `Q · d` | `(1 of pico.coulombs) * (1 of nano.meters)` | 電荷とその分離距離からのモーメント |
| `p / Q` | `(6 of coulombMeters) / (2 of coulombs)` | モーメントの背後にある分離距離 |
| `A·s·m` | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | 電流・時間・長さ としてのモーメント（純粋な積） |
| `pC·m` | `pico.coulombMeters` | 接頭辞付きモーメント（ピコクーロンメートル） |
