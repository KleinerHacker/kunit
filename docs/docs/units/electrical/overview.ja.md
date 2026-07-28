# 電気工学 — 概要

パッケージ: `org.pcsoft.framework.kunit.ec`、`…voltage`、`…resistance`、`…charge`、`…conductance`、
`…magneticfieldstrength`、`…capacitance`、`…inductance`、`…magneticflux`、`…magneticfluxdensity`、
`…currentdensity`、`…chargedensity`、`…resistivity`、`…conductivity`、`…power`、`…energy`

電気工学は、回路を流れる電流、それを駆動する電圧、そしてそれを妨げる抵抗を結び付けます。これら 3 つは
**オームの法則**で結ばれており、KUnit はその法則を型付きの `*` と `/` 演算子として直接表現します。
1 つの**ネイティブ**基本量(電流)と、基本次元から**構成された**量(電圧、抵抗、電荷、コンダクタンス、
磁界の強さ)です。

## この話題の単位

| 単位 | 種別 | 基準単位 | ページ |
|---|---|---|---|
| 電流 | ネイティブ | アンペア(`A`) | [電流](ec.md) |
| 電圧 | 構成 | ボルト(`V`) | [電圧](voltage.md) |
| 抵抗 | 構成 | オーム(`Ω`) | [抵抗](resistance.md) |
| 電荷 | 構成 | クーロン(`C`) | [電荷](charge.md) |
| コンダクタンス | 構成 | ジーメンス(`S`) | [コンダクタンス](conductance.md) |
| 磁界の強さ | 構成 | アンペア毎メートル(`A/m`) | [磁界の強さ](magneticfieldstrength.md) |
| キャパシタンス | 構成 | ファラド(`F`) | [キャパシタンス](capacitance.md) |
| インダクタンス | 構成 | ヘンリー(`H`) | [インダクタンス](inductance.md) |
| 磁束 | 構成 | ウェーバ(`Wb`) | [磁束](magneticflux.md) |
| 磁束密度 | 構成 | テスラ(`T`) | [磁束密度](magneticfluxdensity.md) |
| 電流密度 | 構成 | アンペア毎平方メートル(`A/m²`) | [電流密度](currentdensity.md) |
| 電荷密度 | 構成 | クーロン毎立方メートル(`C/m³`) | [電荷密度](chargedensity.md) |
| 抵抗率 | 構成 | オームメートル(`Ω·m`) | [抵抗率](resistivity.md) |
| 導電率 | 構成 | ジーメンス毎メートル(`S/m`) | [導電率](conductivity.md) |
| 電界の強さ | 構成 | ボルト毎メートル(`V/m`) | [電界の強さ](electricfieldstrength.md) |
| 電束密度 | 構成 | クーロン毎平方メートル(`C/m²`) | [電束密度](electricfluxdensity.md) |
| 誘電率 | 構成 | ファラド毎メートル(`F/m`) | [誘電率](permittivity.md) |
| 透磁率 | 構成 | ヘンリー毎メートル(`H/m`) | [透磁率](permeability.md) |
| 線電荷密度 | 構成 | クーロン毎メートル(`C/m`) | [線電荷密度](linearchargedensity.md) |
| 磁気抵抗 | 構成 | アンペア毎ウェーバ(`A/Wb`) | [磁気抵抗](reluctance.md) |
| 電気移動度 | 構成 | 平方メートル毎ボルト秒(`m²/(V·s)`) | [電気移動度](electricmobility.md) |
| 電気双極子モーメント | 構成 | クーロンメートル(`C·m`) | [電気双極子モーメント](electricdipolemoment.md) |
| 電力 | 構成 | ワット(`W`) | [電力(電気)](power.md) |
| エネルギー | 構成 | ジュール(`J`) | [エネルギー(電気)](energy.md) |

電力とエネルギーは技術的にはそれぞれ**単一の**量であり、他の分野とも共有されています。それらは分野ごとに
文書化され、互いに相互参照します([電力(力学)](../mechanics/power.md)、
[電力(熱力学)](../thermodynamics/power.md)、[エネルギー(力学)](../mechanics/energy.md)、
[エネルギー(熱力学)](../thermodynamics/energy.md))。

## 型付き演算子としてのオームの法則

| 式 | 結果 | 公式 |
|---|---|---|
| `resistance * current` | 電圧 | `U = R · I` |
| `current * resistance` | 電圧 | `U = R · I`(可換) |
| `voltage / current` | 抵抗 | `R = U / I` |
| `voltage / resistance` | 電流 | `I = U / R` |
| `current / voltage` | コンダクタンス | `G = I / U` |
| `1 / resistance` | コンダクタンス | `G = 1 / R` |
| `1 / conductance` | 抵抗 | `R = 1 / G` |
| `conductance * voltage` | 電流 | `I = G · U` |
| `current / conductance` | 電圧 | `U = I / G` |

## その他の型付き演算子

| 式 | 結果 | 公式 |
|---|---|---|
| `current * time` | 電荷 | `Q = I · t` |
| `current / frequency` | 電荷 | `Q = I / f` |
| `charge / time` | 電流 | `I = Q / t` |
| `charge / current` | 時間 | `t = Q / I` |
| `current / length` | 磁界の強さ | `H = I / l` |
| `field strength * length` | 電流 | `I = H · l` |
| `charge / voltage` | キャパシタンス | `C = Q / U` |
| `capacitance * voltage` | 電荷 | `Q = C · U` |
| `voltage * time` | 磁束 | `Φ = U · t` |
| `flux / time` | 電圧 | `U = Φ / t` |
| `flux / current` | インダクタンス | `L = Φ / I` |
| `inductance * current` | 磁束 | `Φ = L · I` |
| `resistance / frequency` | インダクタンス | `L = X / ω` |
| `flux / area` | 磁束密度 | `B = Φ / A` |
| `flux density * area` | 磁束 | `Φ = B · A` |
| `current / area` | 電流密度 | `J = I / A` |
| `current density * area` | 電流 | `I = J · A` |
| `charge / volume` | 電荷密度 | `ρ = Q / V` |
| `charge density * volume` | 電荷 | `Q = ρ · V` |
| `resistance * length` | 抵抗率 | `ρ = R · A / l` |
| `1 / resistivity` | 導電率 | `σ = 1 / ρ` |
| `1 / conductivity` | 抵抗率 | `ρ = 1 / σ` |
| `conductance / length` | 導電率 | `σ = G · l / A` |
| `conductivity * length` | コンダクタンス | `G = σ · A / l` |
| `voltage * current` | 電力 | `P = U · I` |
| `power / voltage` | 電流 | `I = P / U` |
| `power / current` | 電圧 | `U = P / I` |
| `power * time` | エネルギー | `W = P · t` |
| `energy / time` | 電力 | `P = W / t` |
| `charge * voltage` | エネルギー | `W = Q · U` |
| `energy / charge` | 電圧 | `U = W / Q` |
| `voltage / length` | 電界の強さ | `E = U / l` |
| `force / charge` | 電界の強さ | `E = F / Q` |
| `field strength * length` | 電圧 | `U = E · l` |
| `field strength * charge` | 力 | `F = E · Q` |
| `charge / area` | 電束密度 | `D = Q / A` |
| `flux density * area` | 電荷 | `Q = D · A` |
| `flux density / field strength` | 誘電率 | `ε = D / E` |
| `permittivity * field strength` | 電束密度 | `D = ε · E` |
| `capacitance / length` | 誘電率 | `ε = C · d / A` |
| `permittivity * length` | キャパシタンス | `C = ε · A / d` |
| `magnetic flux density / magnetic field strength` | 透磁率 | `μ = B / H` |
| `permeability * magnetic field strength` | 磁束密度 | `B = μ · H` |
| `inductance / length` | 透磁率 | `μ = L · l / (N² · A)` |
| `permeability * length` | インダクタンス | `L = μ · N² · A / l` |
| `charge / length` | 線電荷密度 | `λ = Q / l` |
| `linear charge density * length` | 電荷 | `Q = λ · l` |
| `current / magnetic flux` | 磁気抵抗 | `Rm = Θ / Φ` |
| `reluctance * magnetic flux` | 電流 | `Θ = Rm · Φ` |
| `1 / inductance` | 磁気抵抗 | `Rm = 1 / Λ` |
| `1 / reluctance` | インダクタンス | `Λ = 1 / Rm` |
| `speed / field strength` | 電気移動度 | `μ = v / E` |
| `mobility * field strength` | 速さ | `v = μ · E` |
| `charge * length` | 電気双極子モーメント | `p = Q · d` |
| `dipole moment / charge` | 長さ | `d = p / Q` |

各結果は正しい型付き量になります — 生の混合単位を手作業で組み立てることはありません。さらに電圧、抵抗、
電荷、コンダクタンス、磁界の強さは、完全に**ネイティブ**な分解(`kg·m²·s⁻³·A⁻¹`、`kg·m²·s⁻³·A⁻²`、
`A·s`、`kg⁻¹·m⁻²·s³·A²`、`A·m⁻¹`)を `toVoltage()` / `toResistance()` / `toCharge()` /
`toConductance()` / `toMagneticFieldStrength()` で認識します。新しいグループについても同様です:
`toCapacitance()`(`kg⁻¹·m⁻²·s⁴·A²`)、`toInductance()`(`kg·m²·s⁻²·A⁻²`)、`toMagneticFlux()`
(`kg·m²·s⁻²·A⁻¹`)、`toMagneticFluxDensity()`(`kg·s⁻²·A⁻¹`)、`toCurrentDensity()`(`A·m⁻²`)、
`toChargeDensity()`(`A·s·m⁻³`)、`toResistivity()`(`kg·m³·s⁻³·A⁻²`)、`toConductivity()`
(`kg⁻¹·m⁻³·s³·A²`)、`toPower()`(`kg·m²·s⁻³`)、`toEnergy()`(`kg·m²·s⁻²`)。フィールド、材料、
磁気回路のグループも同じパターンに従います: `toElectricFieldStrength()`(`kg·m·s⁻³·A⁻¹`)、
`toElectricFluxDensity()`(`A·s·m⁻²`)、`toPermittivity()`(`kg⁻¹·m⁻³·s⁴·A²`)、`toPermeability()`
(`kg·m·s⁻²·A⁻²`)、`toLinearChargeDensity()`(`A·s·m⁻¹`)、`toReluctance()`(`kg⁻¹·m⁻²·s²·A²`)、
`toElectricMobility()`(`kg⁻¹·s²·A`)、`toElectricDipoleMoment()`(`A·s·m`)。

一部の量は既存のグループと**次元的に同一**であり、そのため独自のグループではなく既存のグループで扱われ
ます — 意味を示すために記号のみが異なります:

| 量 | グループ | 記号 |
|---|---|---|
| インピーダンス `Z`、リアクタンス `X` | [抵抗](resistance.md) | `Ω` |
| アドミタンス `Y`、サセプタンス `B` | [コンダクタンス](conductance.md) | `S`(`℧`) |
| 皮相電力 `S`、無効電力 `Q` | [電力(電気)](power.md) | `VA`、`var` |
| 起磁力 `Θ` | [電流](ec.md) | `At` |
| 電束 `Ψ` | [電荷](charge.md) | `C` |
| パーミアンス `Λ` | [インダクタンス](inductance.md) | `H` |
| 表面電荷密度 `σ` | [電束密度](electricfluxdensity.md) | `C/m²` |

## 実例 — 1 つの回路でのオームの法則

負荷が **2 A** を引きながら **230 V** を降下させます。抵抗は `R = U / I` であり、その抵抗に電流を戻すと
電圧 `U = R · I` を再現します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance、115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## 実例 — 商用電源の電力から消費エネルギーまで

**10 A** の負荷を供給する **230 V** のコンセントは `P = U · I` を供給します。3 時間運転すると
`W = P · t` が消費されます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(値 + 記号)で出力します。他の単位には `into` を文字列
テンプレート内で使い、記号を自分で付け足します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V"(基準単位)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## 記法

下表はオームの法則を数学表記と KUnit の Kotlin 表記で対比します。指数は Unicode 上付き文字
(`²`、`⁻¹`)、`·` は乗算、`/` は分数を表します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | 電圧÷電流から抵抗 |
| `U = R · I` | `r * (2 of amperes)` | 抵抗×電流から電圧 |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | 電圧÷抵抗から電流 |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | ネイティブ正規形としての抵抗 |

## 次に読むもの

* [電流](ec.md) — ネイティブなアンペアのグループ(および CGS のビオとスタットアンペア)。
* [電圧](voltage.md) — ボルトと、その分解 `R · I` およびネイティブ形式。
* [抵抗](resistance.md) — オーム、`U / I`、および逆オームの法則の演算子。
* [電荷](charge.md) — クーロン、`I · t`、および電池容量のアンペア時。
* [コンダクタンス](conductance.md) — ジーメンス、`1 / R`、および `I / U`。
* [磁界の強さ](magneticfieldstrength.md) — アンペア毎メートル、`I / l`、およびエルステッド。
* [キャパシタンス](capacitance.md) — ファラド、`Q / U`、および CGS のアブファラド/スタットファラド。
* [インダクタンス](inductance.md) — ヘンリー、`Φ / I`、およびリアクタンス形式 `X / ω`。
* [磁束](magneticflux.md) — ウェーバ、`U · t`、およびマクスウェル。
* [磁束密度](magneticfluxdensity.md) — テスラ、`Φ / A`、およびガウス。
* [電流密度](currentdensity.md) — アンペア毎平方メートル、`I / A`、配線サイズ決定に使用。
* [電荷密度](chargedensity.md) — クーロン毎立方メートル、`Q / V`。
* [抵抗率](resistivity.md) — オームメートル、`R · A / l`、抵抗の背後にある材料特性。
* [導電率](conductivity.md) — ジーメンス毎メートル、`1 / ρ`、および `G · l / A`。
* [電力(電気)](power.md) — ワット、`U · I`、およびホースパワー単位。
* [エネルギー(電気)](energy.md) — ジュール、`Q · U`、`P · t`、および `kilo.watts * hours` としてのキロワット時。
* [電界の強さ](electricfieldstrength.md) — ボルト毎メートル、`U / l`、および `F / Q`。
* [電束密度](electricfluxdensity.md) — クーロン毎平方メートル、`Q / A`、表面電荷密度 `σ` でもある。
* [誘電率](permittivity.md) — ファラド毎メートル、`D / E`、および真空定数 `ε₀`。
* [透磁率](permeability.md) — ヘンリー毎メートル、`B / H`、および真空定数 `μ₀`。
* [線電荷密度](linearchargedensity.md) — クーロン毎メートル、`Q / l`、電線やフィラメント向け。
* [磁気抵抗](reluctance.md) — アンペア毎ウェーバ、ホプキンソンの法則 `Θ / Φ`、およびパーミアンス `1 / Λ`。
* [電気移動度](electricmobility.md) — 平方メートル毎ボルト秒、`v / E`、半導体向け。
* [電気双極子モーメント](electricdipolemoment.md) — クーロンメートル、`Q · d`、およびデバイ。
