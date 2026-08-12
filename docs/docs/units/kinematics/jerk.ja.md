# 加加速度(ジャーク)

パッケージ: `org.pcsoft.framework.kunit.kinematic.jerk`
基本単位: **メートル毎秒毎秒毎秒**(`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

種別: **構成された単位**

加加速度(ジャーク) `j` は、**加速度**が変化する割合です: `j = Δa / t`。乗り心地の基準が実際に制限しているのは
この量です — エレベーターや列車は強く加速してもよいのですが、加速度が急激に変化すると乗客はよろめきます。快適
性の限界はおよそ 0.5 m/s³ です。

その正準な基本次元の正規形は `length · time⁻³` です。

## 名前付き単位

| 単位                     | 記号     |                          トークン | 1単位 = m/s³ |
|--------------------------|----------|-----------------------------------:|--------------:|
| メートル毎秒毎秒毎秒     | `m/s^3`  |         `metersPerSecondCubed`     |           1.0 |
| 標準重力毎秒             | `g/s`    |   `standardGravitiesPerSecond`     |       9.80665 |
| フィート毎秒毎秒毎秒     | `ft/s^3` |          `feetPerSecondCubed`      |        0.3048 |

すべてのトークンは、あらゆる SI 接頭辞を受け付けます(`milli.metersPerSecondCubed` など)。

## 分解

このグループは1つの分解を持ち、その両方の形式が同じ型付きで値の等しいインスタンスを生成します:

| 形式                | 式                                                                 |
|---------------------|----------------------------------------------------------------------|
| 型付き演算子        | `acceleration / time`                                             |
| ネイティブ(`toX()`) | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s²(1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## グループでの計算

| 式                     | 結果の型                       | 意味                         |
|------------------------|---------------------------------|------------------------------|
| `acceleration / time`  | `KJerkUnitInstance`             | `j = Δa / t`                 |
| `jerk * time`          | `KAccelerationUnitInstance`     | 積み上がった加速度            |
| `acceleration / jerk`  | `KTimeUnitInstance`             | ランプにかかる時間            |

## 実世界の例 — 快適限界内でのエレベーターのランプ

エレベーターが加加速度 **0.5 m/s³** を超えずに **1 m/s²** に到達しようとしています。ランプはどれだけの長さが
必要でしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// 逆に: 1秒のランプはどれだけの加加速度を課すか?
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — 快適限界の2倍
```

## 値の意味論

`equals`/`hashCode` は**正規化された m/s³ の値**を比較するため、
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)` となります。`toString()` は基本単位で
値を表示します: `"0.6 m/s^3"`。

## 関連項目

* [加速度](acceleration.ja.md) — この単位がその変化率である量。
* [速度](speed.ja.md) と [距離](distance.ja.md) — 運動の連鎖の残り。
* [運動学の概要](overview.ja.md)
