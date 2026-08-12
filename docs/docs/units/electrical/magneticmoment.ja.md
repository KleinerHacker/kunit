# 磁気双極子モーメント

パッケージ: `org.pcsoft.framework.kunit.electric.magneticmoment`
基本単位: **アンペア平方メートル**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

種別: **構成された単位**

電流ループの磁気双極子モーメント `m` は、電流とそれが囲む面積の積です: `m = I · A`。
これは磁場がループに及ぼすトルクを決定する量であり、原子・原子核の磁性(ボーア磁子・核磁子)が表される量です。

その正規の基本次元標準形は `current · length²` です。

## 名前付き単位

| 単位                | 記号    |                トークン |     1単位のA·m²値 |
|---------------------|---------|---------------------:|-------------------:|
| アンペア平方メートル | `A*m^2` | `ampereSquareMeters` |                1.0 |
| ジュール毎テスラ    | `J/T`   |      `joulesPerTesla` |                1.0 |
| ボーア磁子          | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| 核磁子              | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` は同じ単位のエネルギーに基づく綴りです — 双極子が磁束密度の単位あたりに得るエネルギーです。
すべてのトークンはあらゆる SI 接頭辞を受け付けます。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します:

| 形式             | 式                                                       |
|------------------|-------------------------------------------------------------------|
| 型付き演算子     | `current * area`                                                 |
| ネイティブ (`toX()`) | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## グループでの計算

| 式                          | 結果の型                      | 意味          |
|-----------------------------|-----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | ループ電流 |
| `magneticMoment / current`  | `KAreaUnitInstance`              | ループの面積    |

## 実例 — コイルループと原子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// これは何ボーア磁子に相当するか?
m into bohrMagnetons                // ≈ 1.078e21

// 逆に: 1 cm² のループで 1 A·m² を得るにはどれだけの電流が必要か?
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたA·m²値**を比較するため、
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)` となります。`toString()` は値を基本単位で表示します:
`"0.01 A*m^2"`。

## 関連項目

* [磁束密度](magneticfluxdensity.ja.md) — このモーメントが相互作用する場。
* [電流](ec.ja.md) と [距離](../kinematics/distance.ja.md) — 2つの因子。
* [電気工学の概要](overview.ja.md)
