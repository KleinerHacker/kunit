# 圧縮率

パッケージ: `org.pcsoft.framework.kunit.mechanic.compressibility`
基本単位: **パスカルの逆数**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

種別: **構成された単位**

圧縮率 `κ = −(1/V)·(∂V/∂p)` は、圧力の単位あたりに材料の体積がどれだけ収縮するかを示します。
これは弾性率であり、したがって [圧力](pressure.ja.md) である **体積弾性率** `K` の正確な逆数です。
水は約 4.5 × 10⁻¹⁰ Pa⁻¹ であり、これが水力学において非圧縮性として扱われる理由です。

その正準の基本次元標準形は `mass⁻¹ · length · time²` です。

## 名前付き単位

| 単位                            | 記号    |                   トークン | 1単位を 1/Pa で |
|----------------------------------|---------|------------------------:|---------------:|
| パスカルの逆数                   | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| バールの逆数                     | `1/bar` |        `reciprocalBars` |           1e-5 |
| 標準大気圧の逆数                 | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

すべてのトークンは SI 接頭辞を受け入れます (`pico.reciprocalPascals` など)。隣接する圧力グループと同様に、
インスタンスは **グラム基準の生の成分値** を保持します。

## グループでの計算

| 式                             | 結果の型                        | 意味                             |
|----------------------------------|-----------------------------------|-----------------------------------|
| `1 / pressure`                  | `KCompressibilityUnitInstance`  | `κ = 1 / K`                       |
| `1 / compressibility`           | `KPressureUnitInstance`         | `K = 1 / κ`                       |
| `compressibility * pressure`    | `Double`                        | 相対体積変化 `ΔV/V`               |

2つの逆数は正確です: 成分の基準 (圧力の `g·m⁻¹·s⁻²` と、ここでの `g⁻¹·m·s²`) は互いに逆数の関係にあるため、
橋渡しとなる係数は不要です。

## 実例 — 水はどれだけ圧縮されるか

水の体積弾性率は約 **2.2 GPa** です。その圧縮率はいくらで、10 MPa (水深およそ1000 m 相当) の下でどれだけ
収縮するでしょうか？

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 — 体積減少 0.45 %

// 体積弾性率に戻す
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化された成分値** を比較するため、
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)` です。`toString()` は基本単位で値を表示します:
`"1.0 1/Pa"`。

## 関連項目

* [圧力](pressure.ja.md) — 逆数の量 (体積弾性率)。
* [応力と弾性率](stress.ja.md) — 材料特性として読む同じ型。
* [力学の概要](overview.ja.md)
