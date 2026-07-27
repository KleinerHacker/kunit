# 電気工学 — 概要

パッケージ: `org.pcsoft.framework.kunit.ec`、`…voltage`、`…resistance`、`…charge`、`…conductance`、
`…magneticfieldstrength`

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

各結果は正しい型付き量になります — 生の混合単位を手作業で組み立てることはありません。さらに電圧、抵抗、
電荷、コンダクタンス、磁界の強さは、完全に**ネイティブ**な分解(`kg·m²·s⁻³·A⁻¹`、`kg·m²·s⁻³·A⁻²`、
`A·s`、`kg⁻¹·m⁻²·s³·A²`、`A·m⁻¹`)を `toVoltage()` / `toResistance()` / `toCharge()` /
`toConductance()` / `toMagneticFieldStrength()` で認識します。

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
