# 熱流

パッケージ: `org.pcsoft.framework.kunit.common.power`
基本単位: **ワット** (`KPowerUnit.BASE == KPowerUnit.WATT`)

種別: **構成単位（constructed unit）**

熱流 `Q̇`(熱動力、または熱流量とも呼ばれる)は、単位時間あたりに伝わる熱の量です: `W`。これは
[電力](power.md)と **次元的にも物理的にも同一**です — エネルギー÷時間 — そのためKUnitは
`KPowerUnitInstance` でこれをモデル化します。

## 熱流が独自の型を持たない理由

熱流は独立した量ではなく、たまたま熱的な電力です。唯一の正準の正規形 `mass¹ · distance² · time⁻³` が
存在し、これに対して2つ目の型を作ると、物理を追加することなく `toPower()` を曖昧にしてしまいます。
ワットが電動機を表すか、レーザーを表すか、ラジエーターを表すかは文脈の問題であり、次元の問題では ありません。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // 機械的な動力
val radiator = 1500 of watts    // 熱流
// どちらも KPowerUnitInstance
```

## 実例 — ラジエーター

定格1500 Wのラジエーターを4時間運転します。どれだけのエネルギーを供給し、その 0.6 m² の表面で どれだけの熱流束密度を生じるでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ(= 6 kWh)

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## この分野での熱流の現れ方

| 式                       | 結果の型                       | 意味                               |
|--------------------------|--------------------------------|------------------------------------|
| `energy / time`          | `KPowerUnitInstance`           | 熱÷継続時間から熱流                |
| `power * time`           | `KEnergyUnitInstance`          | 継続時間にわたって供給される熱     |
| `power / area`           | `KHeatFluxDensityUnitInstance` | [熱流束密度](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance`           | 表面を通過する総熱流               |

壁の熱損失は典型的な連鎖です: [熱伝達率](heat-transfer-coefficient.md)に温度差を掛けると
[熱流束密度](heat-flux-density.md)が得られ、それに面積を掛けるとワット単位の熱流が得られます。

## 関連項目

* [電力](power.md) — 熱流が共有する型で、完全な単位表、すべての分解表現、演算子全体を記載
* [熱流束密度](heat-flux-density.md) — 単位面積あたりの熱流
* [熱伝達率](heat-transfer-coefficient.md) — ケルビンあたりの熱流束密度
* [エネルギー](energy.md) — 時間で積分された熱流

## 記法

以下の表は、この量が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、
`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。

| 数学        | Kotlin                                     | 意味                       |
|-------------|--------------------------------------------|----------------------------|
| `W`         | `watts`                                    | 熱流、基本単位(電力と共有) |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | 同じ量を基本次元で         |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime`         | 熱÷継続時間から熱流        |
| `Q = Q̇ · t` | `radiator * runtime`                       | 熱流×継続時間から熱        |
| `q̇ = Q̇ / A` | `radiator / surface`                       | 熱流÷面積から熱流束密度    |
