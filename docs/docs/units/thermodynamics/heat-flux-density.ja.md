# 熱流束密度

パッケージ: `org.pcsoft.framework.kunit.thermo.heatfluxdensity`
基本単位: **ワット毎平方メートル** (`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`)

種別: **構成単位（constructed unit）**

熱流束密度は単位面積あたりの熱流です: `power / area`(`W/m²`)。同じ単位は*放射照度*と
*放射発散度*も測定します — 表面に当たる、または表面から出ていく放射の強さです。

`KHeatFluxDensityUnitInstance` は正準の正規形 `mass¹ · time⁻³`(`kg·s⁻³`)にちょうど2つの項からなる
`KMixedUnitInstance` をラップし、常に W/m² に正規化されます。

!!! note "距離の次元は打ち消される"
    `W/m² = kg·m²·s⁻³/m² = kg·s⁻³`。したがって正準の正規形には距離の項が**存在しません**。

総熱流そのものは単純に[電力](power.md)です。[熱流](heat-flow.md)を参照してください。温度差で割ると
これは[熱伝達率](heat-transfer-coefficient.md)になります。

## 名前付き単位

| 単位 | 記号 | トークン | W/m²での1単位 |
|---|---|---:|---:|
| ワット毎平方メートル | `W/m²` | `wattsPerSquareMeter` | 1.0 |
| Btu 毎時間平方フィート | `Btu/(h·ft²)` | `btusPerHourSquareFoot` | ≈ 3.15459 |
| カロリー毎秒平方センチメートル | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` | 41840.0 |

すべてがSI接頭辞の全範囲をサポートします(`kilo.wattsPerSquareMeter`、`milli.wattsPerSquareMeter` など)。

## 太陽定数

このグループは平均的な地球外太陽放射照度を `SOLAR_CONSTANT`(1361 W/m²)として公開します。単純な
`Double` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## 実例 — 太陽光発電アレイのサイジング

屋根が晴天時に800 W/m²を受け取ります。アレイは25 m²をカバーし、入射放射の20%を変換します。
どれだけの電力を供給できるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // スカラー倍は型を保つ
electrical into kilo.watts                 // 4.0 kW

// 逆方向: 効率20%で電気10 kWを得るには屋根の面積はどれだけ必要か?
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## 中核単位(電力と面積)での計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `power / area` | `KHeatFluxDensityUnitInstance` | 熱流束密度 |
| `heatFluxDensity * area` | `KPowerUnitInstance` | 総熱流 |
| `area * heatFluxDensity` | `KPowerUnitInstance` | 総熱流(可換) |
| `power / heatFluxDensity` | `KAreaUnitInstance` | 広がっている面積 |

## 分解表現

どちらの分解表現も同じ値として等しい型付きインスタンスを生成します。

| 分解表現 | 形式 | 結果 |
|---|---|---|
| `power / area` | 型付き演算子 | `KHeatFluxDensityUnitInstance` 直接 |
| `mass · time⁻³` | ネイティブ表現 + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true - どちらも 1.0 W/m²
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## toString によるフォーマット

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## 記法

以下の表は、この単位およびその構成要素が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。分数と負の指数を用いた積の両方で表記可能な量については、両方の等価なKotlin表現を記載しています。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `W/m²` | `wattsPerSquareMeter` | 熱流束密度、基本単位 — 名前付きトークン |
| `kg·s⁻³` | `grams / (seconds pow 3)` | 同じ量を基本次元で |
| `kW/m²` | `kilo.wattsPerSquareMeter` | キロワット毎平方メートル |
| `E_0` | `SOLAR_CONSTANT of wattsPerSquareMeter` | 太陽定数、1361 W/m² |
| `q̇ = P / A` | `(1000 of watts) / roof` | 電力÷面積から流束密度 |
| `P = q̇ · A` | `irradiance * roof` | 流束密度×面積から電力 |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance` | 電力÷流束密度から面積 |
