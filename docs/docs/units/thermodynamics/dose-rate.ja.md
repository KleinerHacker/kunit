# 線量率

パッケージ: `org.pcsoft.framework.kunit.thermo.doserate`
基本単位: **グレイ毎秒** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

種別: **構成単位（constructed unit）**

線量率は、**時間あたり**に吸収される放射線量です: `Ḋ = D / t`。これはサーベイメーターが表示する値
であり — ほとんど常にマイクロシーベルト毎時で表されます — 蓄積線量は被曝時間にわたる積分値です。

その正準の基本次元正規形は `length² · time⁻³` です。グレイの `J/kg` のキログラムがジュールの
キログラムと相殺されるため、質量の項は残りません。

## 名前付き単位

| 単位             | 記号    | トークン              | Gy/sにおける1単位 |
|------------------|---------|-----------------------|----------------:|
| グレイ毎秒         | `Gy/s`  | `graysPerSecond`      |            1.0 |
| グレイ毎時         | `Gy/h`  | `graysPerHour`        |         1/3600 |
| シーベルト毎秒      | `Sv/s`  | `sievertsPerSecond`   |            1.0 |
| シーベルト毎時      | `Sv/h`  | `sievertsPerHour`     |         1/3600 |

グレイ（吸収線量）とシーベルト（等価線量）は同一の次元を共有するため、KUnitは両方に対して1つの
グループをモデル化しています — シーベルトの表記は、放射線防護の測定値を直接記述できるように存在します。
すべてのトークンはSI接頭辞を受け付けます。`micro.sievertsPerHour` が日常的なものです。

!!! note "1つのグループ、2つの読み方"
    グレイとシーベルトは次元ではなく、無次元の放射線加重係数によって異なります。1つの正規形は
    1つの型にマップされなければなりません（同じ議論について[エントロピー](entropy.ja.md)を
    参照）。したがって、この区別は値にどんな名前を付けるかの問題です。

## 分解

このグループには1つの分解表現があり、両方の形式は同じ型で値が等しいインスタンスを生成します:

| 形式                | 表現                                                                          |
|--------------------|--------------------------------------------------------------------------------|
| 型付き演算子          | `specificEnergy / time`                                                       |
| ネイティブ（`toX()`）  | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`  |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## グループでの計算

| 式                            | 結果の型                        | 意味                  |
|-------------------------------|----------------------------------|-----------------------|
| `specificEnergy / time`       | `KDoseRateUnitInstance`          | `Ḋ = D / t`           |
| `doseRate * time`             | `KSpecificEnergyUnitInstance`    | 蓄積された線量           |
| `specificEnergy / doseRate`   | `KTimeUnitInstance`              | 被曝時間                |

吸収線量そのものは[比エネルギー](specific-energy.ja.md)グループです — 1 Gy = 1 J/kg。

## 実例 — 年間自然放射線バックグラウンド

自然放射線バックグラウンドはおよそ **0.274 µSv/h**です。1年間（8766時間）でこれは、おなじみの
2.4 mSvになります:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## 値の意味論

`equals`/`hashCode` は**正規化されたGy/s値**を比較するため、
`(1 of graysPerHour) == (1 of sievertsPerHour)` となります。`toString()` は基本単位での値を
表示します: `"1.0 Gy/s"`。

## 関連項目

* [比エネルギー](specific-energy.ja.md) — 吸収線量そのもの（`Gy` = `J/kg`）。
* [熱力学の概要](overview.ja.md)
