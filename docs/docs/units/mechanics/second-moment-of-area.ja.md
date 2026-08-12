# 断面二次モーメント

パッケージ: `org.pcsoft.framework.kunit.kinematic.distance`
基本単位: **メートルの4乗** (`m⁴`、distance グループの指数4の葉)

種別: **構成された単位**

断面二次モーメント `I` (面積の二次モーメント) は、梁の断面が曲げに対してどれだけ剛性を持つかを決める幾何学的
特性です — 曲げ剛性 `EI` の `I` です。鋼材プロファイル表では `cm⁴` で示され、小さな断面では `mm⁴` で示されます。

このサイトの他のグループとは異なり、これは独自のグループではありません: distance グループの
**指数4の葉** である `KSecondMomentOfAreaUnitInstance` であり、[長さ](../kinematics/distance.ja.md)
(指数1)、面積 (指数2)、体積 (指数3) の隣に位置します。

!!! warning "慣性モーメントとは異なります"
    これを *質量* の [慣性モーメント](moment-of-inertia.ja.md) (`kg·m²`) と混同しないでください。慣性モーメントは
    角加速度に対する抵抗を表します。名前は似ていますが、次元は異なります。

## 名前付きトークン

| 単位                  | 記号  |                トークン | 1単位を m⁴ で |
|-----------------------|--------|---------------------:|-------------:|
| メートルの4乗          | `m⁴`   |       `quarticMeters` |          1.0 |
| センチメートルの4乗    | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| ミリメートルの4乗      | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| インチの4乗            | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

すべてのトークンは SI 接頭辞を受け入れます。

## 葉での計算

指数4に到達する積は、一般的な `KDistanceUnitInstance` の代わりに型付きの葉を返すようになります:

| 式                            | 結果の型                            | 意味                        |
|---------------------------------|----------------------------------------|-------------------------------|
| `area * area`                  | `KSecondMomentOfAreaUnitInstance`    | m² · m² = m⁴                  |
| `volume * length`              | `KSecondMomentOfAreaUnitInstance`    | m³ · m = m⁴                   |
| `length * volume`              | `KSecondMomentOfAreaUnitInstance`    | m · m³ = m⁴                   |
| `secondMomentOfArea / length`  | `KVolumeUnitInstance`                | 断面係数                       |
| `secondMomentOfArea / area`    | `KAreaUnitInstance`                  | m⁴/m² = m²                    |
| `secondMomentOfArea / volume`  | `KLengthUnitInstance`                | m⁴/m³ = m                     |
| `secondMomentOfArea + …`       | `KSecondMomentOfAreaUnitInstance`    | 組み立て断面の各部分           |

加算は同じ次元に限定されます — `secondMomentOfArea + area` は `length + area` と同様に **コンパイル
エラー** になります。

ネイティブ形式は `toSecondMomentOfArea()` で変換します:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## 実例 — 矩形梁

幅 `b`、高さ `h` の矩形について `I = b·h³/12` です。100 mm × 200 mm の場合:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// 断面係数 W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// 組み立て断面: 同じ梁を2本並べる
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## 値のセマンティクス

`equals`/`hashCode` と比較は、正規化された `m⁴` の値に対して、同じ次元に限定して機能します。
`exponent` は `4` を返します。

## 関連項目

* [距離](../kinematics/distance.ja.md) — この葉が属するグループ。
* [慣性モーメント](moment-of-inertia.ja.md) — 似た名前を持つ *質量* ベースの量。
* [力学の概要](overview.ja.md)
