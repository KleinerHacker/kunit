# 熱コンダクタンス

パッケージ: `org.pcsoft.framework.kunit.thermo.conductance`
基本単位: **ワット毎ケルビン** (`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`)

種別: **構成単位（constructed unit）**

部品の熱コンダクタンス `G` は、単位温度差あたりにどれだけの熱が流れるかを表します: `G = P / ΔT`、単位は
`W/K`。これは[絶対熱抵抗](thermal-resistance.md)の正確な逆数であり、熱経路が**並列**に存在する場合により
便利な表現形式です — 並列のコンダクタンスは単純に加算されます。

正準の正規形は `mass · length² · time⁻³ · temperature⁻¹` です。

## 名前付き単位

| 単位                            | 記号         |                   トークン | W/K での1単位 |
|---------------------------------|--------------|------------------------:|--------------:|
| ワット毎ケルビン                | `W/K`        |         `wattsPerKelvin` |           1.0 |
| Btu 毎時間華氏度                | `Btu/(h*°F)` | `btusPerHourFahrenheit` |     ≈ 0.52753 |

すべてのトークンが全てのSI接頭辞を受け付けます (`milli.wattsPerKelvin`、…)。

## 分解表現

このグループは一つの分解表現を持ち、両方の形式が同じ値として等しい型付きインスタンスを生成します。ネイティブ形式は、
このグループが質量項を含むため、**ユニットテンプレート**から組み立てられます。

| 形式             | 表現                                                             |
|------------------|--------------------------------------------------------------|
| 型付き演算子     | `power / temperatureDifference`                               |
| ネイティブ (`toX()`) | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## グループを使った計算

| 式                                          | 結果の型                              | 意味                    |
|----------------------------------------------|---------------------------------------|-------------------------|
| `power / temperatureDifference`              | `KThermalConductanceUnitInstance`     | `G = P / ΔT`            |
| `thermalConductance * temperatureDifference` | `KPowerUnitInstance`                  | `P = G · ΔT`            |
| `power / thermalConductance`                 | `KTemperatureDifferenceUnitInstance`  | 必要な温度差            |
| `thermalConductance + …`                     | `KThermalConductanceUnitInstance`     | 並列の熱経路            |
| `1 / thermalConductance`                     | `KThermalResistanceUnitInstance`      | `R = 1 / G`             |
| `1 / thermalResistance`                      | `KThermalConductanceUnitInstance`     | `G = 1 / R`             |

## 実例 — 2つの並列熱経路

あるモジュールが底板 (0.4 W/K) と筐体 (0.1 W/K) を通じて熱を失っています。並列であるためコンダクタンスは
加算され、その逆数から合計抵抗が得られます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // 30 K の温度差で運び去られる 15.0 W
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたW/K値**を比較するため、
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)` となります。`toString()` は基本単位で値を表示します:
`"0.4 W/K"`。

## 関連項目

* [絶対熱抵抗](thermal-resistance.ja.md) — その逆数量。
* [熱インスラタンス](thermal-insulance.ja.md) — 抵抗の単位面積あたりの形式。
* [熱伝達率](heat-transfer-coefficient.ja.md) — この量の単位面積あたりの形式。
* [熱力学の概要](overview.ja.md)
