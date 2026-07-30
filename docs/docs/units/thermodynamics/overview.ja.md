# 熱力学 — 概要

パッケージ: `org.pcsoft.framework.kunit.thermo.*`、および `org.pcsoft.framework.kunit.common.energy`
と `…common.power`

熱力学は**熱と温度**の物理です。KUnit ではこの分野は温度を中心にしており、温度は**関連する 2 つの
ネイティブなグループ**でモデル化されます — なぜなら、温度の*読み値*と温度の*変化*は物理的に異なる種類の
量であり、それらを区別することが計算を正しくするからです。その周りには、あらゆる熱収支に登場する 2 つの
**構成された**量、すなわち熱そのもの(エネルギー)とその流れる速さ(電力)があります。

## この話題の単位

| 単位 | 種別 | 性質 | 基準単位 | ページ |
|---|---|---|---|---|
| 絶対温度 | ネイティブ | アフィンな**点** | ケルビン(`K`) | [絶対温度](temperature.md) |
| 温度差 | ネイティブ | 線形な**区間** | ケルビン(`ΔK`) | [温度差](temperature-difference.md) |
| 物質量 | ネイティブ | 線形量 | モル(`mol`) | [物質量](amount-of-substance.md) |
| エネルギー | 構成 | 線形量 | ジュール(`J`) | [エネルギー(熱力学)](energy.md) |
| 電力 | 構成 | 線形量 | ワット(`W`) | [電力(熱力学)](power.md) |
| 熱流 | 構成 | 電力の型を共有 | ワット(`W`) | [熱流](heat-flow.md) |
| 熱容量 | 構成 | 線形量 | `J/K` | [熱容量](heat-capacity.md) |
| エントロピー | 構成 | 熱容量の型を共有 | `J/K` | [エントロピー](entropy.md) |
| 比熱容量 | 構成 | 線形量 | `J/(kg·K)` | [比熱容量](specific-heat-capacity.md) |
| モル熱容量 | 構成 | 線形量 | `J/(mol·K)` | [モル熱容量](molar-heat-capacity.md) |
| 比エネルギー | 構成 | 線形量 | `J/kg` | [比エネルギー](specific-energy.md) |
| モルエネルギー | 構成 | 線形量 | `J/mol` | [モルエネルギー](molar-energy.md) |
| 熱流束密度 | 構成 | 線形量 | `W/m²` | [熱流束密度](heat-flux-density.md) |
| 熱伝導率 | 構成 | 線形量 | `W/(m·K)` | [熱伝導率](thermal-conductivity.md) |
| 熱伝達率 | 構成 | 線形量 | `W/(m²·K)` | [熱伝達率](heat-transfer-coefficient.md) |
| 熱抵抗 | 構成 | 線形量 | `m²·K/W` | [熱抵抗](thermal-resistance.md) |
| 熱膨張率 | 構成 | 線形量 | `1/K` | [熱膨張率](thermal-expansion.md) |
| 温度勾配 | 構成 | 線形量 | `K/m` | [温度勾配](temperature-gradient.md) |
| 熱拡散率 | 構成 | 線形量 | `m²/s` | [熱拡散率](thermal-diffusivity.md) |

2つの項目は意図的に既存の型を**共有**しており、独自の型を持ちません — エントロピーは次元的に熱容量と
同一であり、熱流は電力と同一です。正準の基本次元正規形は正確に1つの型に対応する必要があります。
そうでなければ `toX()` の形状認識が曖昧になってしまいます — 詳しい理由は
[エントロピー](entropy.md)と[熱流](heat-flow.md)のページを参照してください。

エネルギー(熱)と電力(熱流量)は技術的にはそれぞれ**単一の**量であり、他の分野とも共有されています。
それらは分野ごとに文書化され、互いに相互参照します([エネルギー(電気)](../electrical/energy.md)、
[エネルギー(力学)](../mechanics/energy.md)、[電力(電気)](../electrical/power.md)、
[電力(力学)](../mechanics/power.md))。

専用の[温度概要](temperature-overview.md)が点と区間の区別を詳しく説明します。このページは熱力学分野全体の
入口です。

## 点 vs 区間 — 演算子の規則

| 演算 | 結果 |
|---|---|
| `絶対温度 − 絶対温度` | **温度差** |
| `絶対温度 + 差` | 絶対温度 |
| `絶対温度 − 差` | 絶対温度 |
| `差 ± 差` | 温度差 |
| `絶対温度 + 絶対温度` | **コンパイルエラー**(物理的に無意味) |

## 熱と熱流量の型付き演算子

| 式 | 結果 | 公式 |
|---|---|---|
| `power * time` | エネルギー(熱) | `Q = Φ · t` |
| `energy / time` | 電力(熱流量) | `Φ = Q / t` |
| `energy / power` | 時間 | `t = Q / Φ` |
| `power / frequency` | エネルギー | `Q = Φ / f` |

## 伝熱の連鎖

派生したグループは、材料特性から総熱損失まで1本につながった連鎖を形成します。各段階は型付き演算子であり、
生の `KMixedUnitInstance` が現れることはありません。

| 式 | 結果 | 公式 |
|---|---|---|
| `temperatureDifference / length` | 温度勾配 | `∇T = ΔT / d` |
| `thermalConductivity * temperatureGradient` | 熱流束密度 | `q̇ = λ · ∇T`(フーリエの法則) |
| `thermalConductivity / length` | 熱伝達率 | `U = λ / d` |
| `length / thermalConductivity` | 熱抵抗 | `R = d / λ` |
| `1 / heatTransferCoefficient` | 熱抵抗 | `R = 1 / U` |
| `heatFluxDensity * area` | 電力(熱流量) | `Φ = q̇ · A` |
| `energy / temperatureDifference` | 熱容量 | `C = Q / ΔT` |
| `heatCapacity / mass` | 比熱容量 | `c = C / m` |
| `heatCapacity / amountOfSubstance` | モル熱容量 | `C_m = C / n` |
| `energy / mass` | 比エネルギー | `q = Q / m` |
| `energy / amountOfSubstance` | モルエネルギー | `ΔH_m = Q / n` |

熱拡散率は唯一の**三項**関係です(`α = λ / (ρ · c_p)`)。中間量である体積熱容量 `ρ · c_p` はモデル化された
単位ではないため、二項演算子ではなく名前付き関数 `diffusivityWith` / `conductivityWith` として公開されます。

## 実例 — 加熱の 1 ステップ

水を **10 °C** から **30 °C** へ加熱します。その*変化*は温度**差**(`ΔT`)であり、これは `Q = m · c · ΔT`
のような熱の公式に入る量です。ゼロ点が打ち消されるため、`°C` と `K` はステップの大きさで一致します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0(ケルビン区間)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## 実例 — ボイラーの熱量と加熱時間

**2 kW** のボイラーを **10 分間**稼働させます。供給される熱量は `Q = Φ · t` です。これを熱流量で割り戻すと
加熱時間が得られます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8(kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(ケルビン)で出力します。絶対温度は `K`、差は区別された
`ΔK` 記号で表示されます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

(25 of celsius).toString()                       // "298.15 K"(絶対、基準単位)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK"(区間)
```

## 記法

下表は温度の関係を数学表記と KUnit の Kotlin 表記で対比します。`Δ` は区間量を表し、絶対的な点とは意図的に
区別されます。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | 2 つの絶対温度からの差 |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | 区間で移動した絶対温度 |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 明示的な温度区間 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 2 つの区間の和 |
| `Q = Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | 熱流量×時間から熱量 |
| `Φ = Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | 熱量÷時間から熱流量 |

## 次に読むもの

* [温度概要](temperature-overview.md) — 点と区間の完全な議論と、それが物理的になぜ重要か
  (熱エネルギー、放射、理想気体の法則)。
* [絶対温度](temperature.md) — ケルビン、摂氏、華氏、ランキンとアフィン演算子。
* [温度差](temperature-difference.md) — 線形なケルビン区間のグループ。
* [エネルギー(熱力学)](energy.md) — 熱としてのジュール、およびカロリーと BTU。
* [電力(熱力学)](power.md) — 熱流量としてのワット、`Q / t`。
* [物質量](amount-of-substance.md) — モル、あらゆるモル量の基礎。

### 熱を蓄える

* [熱容量](heat-capacity.md) — `J/K`、物体がケルビンあたりに吸収するエネルギー。
* [エントロピー](entropy.md) — 同じ `J/K` 型を `ΔS = Q / T` として読む。
* [比熱容量](specific-heat-capacity.md) — `J/(kg·K)`、材料特性。
* [モル熱容量](molar-heat-capacity.md) — `J/(mol·K)`、および気体定数 `R`。
* [比エネルギー](specific-energy.md) — `J/kg`、潜熱や発熱量でもある。
* [モルエネルギー](molar-energy.md) — `J/mol`、反応エンタルピーと生成エンタルピー。

### 熱を移動させる

* [熱流](heat-flow.md) — 熱力学的な電力としてのワット。
* [熱流束密度](heat-flux-density.md) — `W/m²`、放射照度でもある。太陽定数を含む。
* [温度勾配](temperature-gradient.md) — `K/m`、伝導の駆動力。
* [熱伝導率](thermal-conductivity.md) — `W/(m·K)`、フーリエの法則。
* [熱伝達率](heat-transfer-coefficient.md) — `W/(m²·K)`、建築物理学のU値。
* [熱抵抗](thermal-resistance.md) — `m²·K/W`、R値。層が直列に加算される。
* [熱拡散率](thermal-diffusivity.md) — `m²/s`、温度変化がどれだけ速く伝わるか。

### 熱への反応

* [熱膨張率](thermal-expansion.md) — `1/K`、橋に伸縮継手がある理由。
