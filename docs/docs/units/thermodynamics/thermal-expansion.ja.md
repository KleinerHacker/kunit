# 熱膨張率

パッケージ: `org.pcsoft.framework.kunit.thermo.expansion`
基本単位: **毎ケルビン** (`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`)

種別: **構成単位（constructed unit）**

熱膨張率 `α` は、長さ (または面積、体積)のケルビンあたりの *相対的な*変化です: `1/K`。これは温度差の 逆数です。

`KThermalExpansionUnitInstance` は正準の正規形 `temperature⁻¹`(`K⁻¹`)にちょうど1つの項からなる
`KMixedUnitInstance` をラップし、常に 1/K に正規化されます。温度の次元は **差**グループです — 係数は温度 *区間*
あたりの変化を表します。

!!! note "パッケージ名とクラス名"
パッケージは `thermo.expansion` であり、`thermo.thermalexpansion` ではありません — 単位パッケージ
はその分野パッケージの名前を繰り返してはなりません。型は完全な技術用語 (`KThermalExpansionUnitInstance`)を保持します。

## 名前付き単位

| 単位               | 記号    |        トークン | 1/Kでの1単位 |
|--------------------|---------|----------------:|-------------:|
| 毎ケルビン         | `1/K`   |     `perKelvin` |          1.0 |
| 毎華氏度           | `1/°F`  | `perFahrenheit` |          1.8 |
| 百万分率毎ケルビン | `ppm/K` |  `ppmPerKelvin` |         1e-6 |

材料表では `α` を ppm/K で記載しますが、これはちょうど `micro.perKelvin` です。すべての単位がSI接頭辞 の全範囲をサポートします。

## 代表的な値

| 材料               |           α |
|--------------------|------------:|
| 鋼                 |  ≈ 12 ppm/K |
| コンクリート       |  ≈ 12 ppm/K |
| アルミニウム       |  ≈ 23 ppm/K |
| ガラス(ホウケイ酸) | ≈ 3.3 ppm/K |

## 実例 — 夏の鋼鉄製の梁

10 mの鋼鉄製の梁 (α = 12 ppm/K)が 0 °C から 50 °C へ暖まります。どれだけ長くなるでしょうか? これが 橋に伸縮継手がある理由です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// 無次元の相対変化
val strain = steel * rise                      // 6.0e-4

// 絶対的な変化、型付き
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// 同じ温度変化における100 mの橋桁
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## 演算子

| 式                                                             | 結果の型                             | 意味                     |
|----------------------------------------------------------------|--------------------------------------|--------------------------|
| `1 / temperatureDifference`                                    | `KThermalExpansionUnitInstance`      | 区間から係数             |
| `1 / thermalExpansion`                                         | `KTemperatureDifferenceUnitInstance` | 係数から区間             |
| `thermalExpansion * temperatureDifference`                     | `Double`                             | **相対的な**変化(無次元) |
| `temperatureDifference * thermalExpansion`                     | `Double`                             | 同じ(可換)               |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance`                | **絶対的な**変化         |

2つの逆数演算子は狭く宣言されているため、`1 / d` と `1 / α` はグループに依存しない `Number.div` が 生成するであろう汎用の混合単位ではなく、
**型付き**の値を返します。

!!! warning "連鎖した `*` の代わりに `elongationOf`"
`α · ΔT` は意図的にただの `Double` です — 相対的な変化は無次元だからです。その `Double` を長さに 掛けるにはルートパッケージの汎用スカラー
`times` が必要になり、それを明示的にインポートすると このグループの `times` 演算子を **シャドウ**してしまいます。
`elongationOf` はシャドウされ得ない 純粋な関数であるため、絶対的な変化を求めたい場合は常にこちらを優先してください。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## 分解表現

両方の分解表現が同じ値として等しい型付きインスタンスを生成します。

| 分解表現                    | 形式                                    | 結果                            |
|-----------------------------|-----------------------------------------|---------------------------------|
| `1 / temperatureDifference` | 型付き演算子                            | `KThermalExpansionUnitInstance` |
| `temperature⁻¹`             | ネイティブ表現 + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true - どちらも 1.0 1/K
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（
`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学              | Kotlin                                   | 意味                       |
|-------------------|------------------------------------------|----------------------------|
| `1/K`             | `perKelvin`                              | 熱膨張率、基本単位         |
| `K⁻¹`             | `ΔK pow -1`                              | 同じ量を負の指数として     |
| `ppm/K`           | `ppmPerKelvin`                           | 百万分率毎ケルビン(材料表) |
| `α = 1 / ΔT`      | `1 / KTemperatureDifference.ofKelvin(2)` | 区間から係数               |
| `ε = α · ΔT`      | `steel * rise`                           | 相対的な変化(無次元)       |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)`         | 絶対的な長さの変化         |
