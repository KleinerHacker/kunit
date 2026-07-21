# 加速度

パッケージ: `org.pcsoft.framework.kunit.acceleration`
基本単位: **メートル毎秒毎秒**(`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

加速度は**構成された**単位で、合成 `length · time⁻²`(`m/s²`)です。`KAccelerationUnitInstance` は、指数 `+1`
の `KDistanceUnit.BASE`(メートル)と指数 `-2` の `KTimeUnit.BASE`(秒)の、ちょうど2つの項からなる
`KMixedUnitInstance` をラップします。値は常に m/s² に正規化されます。基本単位が成分の基本単位(メートル、秒)
と一致するため、追加のスケール係数はありません。

## 加速度の作成

加速度は通常 `speed / time` から、または名前付きトークンで作成します。`metersPerSecondSquared` トークンは意図的に
**ありません**(それはまさに `meters / (seconds pow 2)` です)。真に名前を持つ単位のみが値1のトークンとして残ります
(`of`/`into` で使用):

| 加速度 | 記号 | トークン | m/s² 換算(1単位) |
|---|---|---:|---:|
| ガル(ガリレオ) | `Gal` | `gals` | 0.01(1 cm/s²) |
| 標準重力 | `g₀` | `standardGravities` | 9.80665 |

どちらのトークンも完全な SI 接頭辞に対応します(例: `milli.gals` = 1 mGal、重力測定の日常単位)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05(m/s² に正規化)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001(1 mGal)
```

## 基本単位(速度と時間)による計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | 加速度 = Δ速度 / 時間 |
| `acceleration * time` | `KSpeedUnitInstance` | 速度 = 加速度 × 時間 |
| `time * acceleration` | `KSpeedUnitInstance` | 速度(可換) |
| `speed / acceleration` | `KTimeUnitInstance` | 時間 = 速度 / 加速度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance, 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance, 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : 同じグループ、異なる加速度式の間で自動変換
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// 2つの加速度の * / / は KMixedUnitInstance に「脱出」します
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## toString の書式

基本単位の `toString()` のみが存在します。特定の単位は `into` で書式化します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²"(基本単位)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```
