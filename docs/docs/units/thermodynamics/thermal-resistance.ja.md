# 絶対熱抵抗

パッケージ: `org.pcsoft.framework.kunit.thermo.resistance`
基本単位: **ケルビン毎ワット** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`)

種別: **構成単位（constructed unit）**

部品の絶対熱抵抗 `R` は、そこを流れる熱量あたりに維持される温度差です: `R = ΔT / P`、単位は `K/W` で
測定されます。これは**個々の対象物**を表します — このヒートシンク、このトランジスタパッケージ、この
サイズのこの壁、というように。

その正準の正規形は `mass⁻¹ · length⁻² · time³ · temperature` です。

!!! warning "熱抵抗係数（thermal insulance）とは異なります"
    このグループを [熱抵抗係数](thermal-insulance.md) `m²·K/W`(R値)と混同しないでください。これは
    同じ概念を**単位面積あたり**で正規化したものです。両者は面積の係数だけ異なり、正規形も型も異な
    ります。バージョン0.8.0以前は `thermo.resistance` / `KThermalResistanceUnit` という名前は熱抵抗
    係数を指していましたが、現在はこのグループを指します。

## 名前付き単位

| 単位                       | 記号       |                       トークン | K/Wでの1単位 |
|----------------------------|------------|------------------------:|--------------:|
| ケルビン毎ワット            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| 摂氏度毎ワット              | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| 時間°F毎Btu                | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

1 °Cの温度**差**は1 Kなので、半導体やヒートシンクのデータシートで使われる表記である
`degreesCelsiusPerWatt` は数値的に `kelvinsPerWatt` と同一です。すべてのトークンが全SI接頭辞を
サポートします。

## 分解表現

このグループには一つの分解表現があり、その両方の形式が同じ型付きで値が等しいインスタンスを生成します。
このグループは質量の項を持つため、ネイティブ形式は**単位テンプレート**から組み立てられます: 生の混合値
はグラム基準の積であり、型付きインスタンスはその値を名前付き単位で保持します。

| 形式             | 式                                                            |
|------------------|------------------------------------------------------------------------|
| 型付き演算子   | `temperatureDifference / power`                                        |
| ネイティブ (`toX()`) | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## グループでの計算

| 式                                        | 結果の型                            | 意味              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | 生じる熱流 |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | 直列の熱抵抗 |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

熱抵抗は**直列で加算されます** — これはグループの同型演算 `+` がまさに行うことです。

## 実例 — ヒートシンクの予算

あるパワートランジスタは **12 W** を放散します。熱経路はジャンクション-ケース間が0.5 K/W、
ケース-ヒートシンク間が0.2 °C/W、ヒートシンク-大気間が1.8 K/Wです。ジャンクションは周囲温度から
どれだけ上昇するでしょうか?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // 周囲温度から30.0 K上昇

// 25 Kの上限に対してどれだけの電力を放散できるか?
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## 値のセマンティクス

`equals`/`hashCode` は**正規化されたK/W値**を比較するため、
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)` となります。`toString()` は値を基本単位で
表示します: `"2.5 K/W"`。

## 関連項目

* [熱抵抗係数](thermal-insulance.ja.md) — 同じ概念を単位面積あたりで表したもの(R値)。
* [熱コンダクタンス](thermal-conductance.ja.md) — その逆数の量。
* [熱力学の概要](overview.ja.md)
