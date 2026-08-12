# 光学 — 概要

パッケージ: `org.pcsoft.framework.kunit.optic.luminousintensity`、`…luminousflux`、`…illuminance`、
`…luminance`、`…luminousenergy`、`…luminousexposure`、`…efficacy`、`…radiantintensity`、`…radiance`、
および `org.pcsoft.framework.kunit.common.reciprocallength`

光学は **光** の記述です — 光源がどれだけの光を放出するか、どれだけの光が面に到達するか、そして電力がどれだけ効率的に光へ変換されるかを扱います。この分野は
**カンデラ**、すなわち7番目にして最後のSI基本単位を基盤としています。カンデラは人間の知覚に基づいて定義される唯一の基本単位であり、放射電力を
目の感度で重み付けしたものです。

そのため、この分野には2つの並行したファミリーがあります。**測光量**（カンデラ、ルーメン、ルクス、ニト）は光を*人の目に見えるとおり*に
記述し、**放射量**（ワット毎ステラジアン、ワット毎ステラジアン平方メートル）は目の重み付けなしに、同じ放射を*検出器が測定するとおり*に
記述します。両者をつなぐ橋渡しが [光視効果（発光効率）](luminous-efficacy.ja.md) であり、683 lm/W を上限とします。

## このトピックの単位

| 単位     | 種別        | 基本単位                                | ページ                                     |
|----------|-------------|------------------------------------------|--------------------------------------------|
| 光度     | ネイティブ  | カンデラ (`cd`)                          | [光度](luminous-intensity.ja.md) |
| 光束     | 構成単位    | ルーメン (`lm`)                           | [光束](luminous-flux.ja.md)        |
| 照度     | 構成単位    | ルクス (`lx`)                             | [照度](illuminance.ja.md)            |
| 輝度     | 構成単位    | カンデラ毎平方メートル (`cd/m²`)     | [輝度](luminance.ja.md)                |
| 光エネルギー | 構成単位 | ルーメン秒 (`lm·s`)                  | [光エネルギー](luminous-energy.ja.md)    |
| 光露光量 | 構成単位    | ルクス秒 (`lx·s`)                    | [光露光量](luminous-exposure.ja.md) |
| 発光効率 | 構成単位    | ルーメン毎ワット (`lm/W`)                | [発光効率](luminous-efficacy.ja.md) |
| 放射強度 | 構成単位    | ワット毎ステラジアン (`W/sr`)            | [放射強度](radiant-intensity.ja.md) |
| 放射輝度 | 構成単位    | ワット毎ステラジアン平方メートル (`W/(sr·m²)`)    | [放射輝度](radiance.ja.md)                  |
| 屈折力   | 構成単位    | ディオプター (`dpt` = `m⁻¹`)                | [ディオプター](dioptre.ja.md)                    |

強度量と光束量を結びつける立体角は、この分野には **属しません** — それは [力学](../mechanics/solid-angle.md) のトピックに属し、
ここではそのまま再利用されます。

## 量の関係

以下の各関係は正しく **型付けされた** 量を返します。生の混合単位を手で組み立てることはありません。

| 式                     | 結果             | 数式        |
|--------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | 光束    | `Φ = I · Ω`    |
| `luminousFlux / area`          | 照度        | `E = Φ / A`    |
| `luminousIntensity / area`     | 輝度          | `L = I / A`    |
| `illuminance / solidAngle`     | 輝度          | `L = E / Ω`    |
| `luminousFlux * time`          | 光エネルギー    | `Q = Φ · t`    |
| `illuminance * time`           | 光露光量  | `H = E · t`    |
| `luminousFlux / power`         | 発光効率  | `η = Φ / P`    |
| `power / solidAngle`           | 放射強度  | `Iₑ = P / Ω`   |
| `radiantIntensity / area`      | 放射輝度           | `Lₑ = Iₑ / A`  |
| `1 / length`                   | 屈折力   | `D = 1 / f`    |

## 実例 — このバルブは机の照明として十分か？

LED電球の定格は **800 lm**、消費電力は **7 W** です。**2 m²** の机の上に吊るされています。オフィス作業には約500 lxが必要です。
足りるでしょうか？そしてこの電球はどれくらい効率的でしょうか？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 — 500 lxの目標にはやや届かない

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 — 物理的上限の17%
```

## 実例 — 老眼鏡

焦点距離**40 cm**のレンズは `D = 1 / f` の屈折力を持ちます。密着させた2枚の薄いレンズはそれぞれの屈折力を単純に足し合わせます。
これはまさに型付き量に対する `+` が行う操作です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // 密着させたレンズは加算される
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 — 合成焦点距離
```

## 表記

以下の表は、この分野の中心的な関係を数式表記とKotlin（KUnit使用）表記で示しています。指数にはUnicode上付き文字
（`²`、`⁻¹`）を使い、`·` は乗算、`/` は分数を表します。

| 数学表記   | Kotlin                                    | 意味                             |
|---------------|---------------------------------------------|--------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | 強度×立体角による光束 |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | 光束÷面積による照度        |
| `L = I / A`   | `(250 of candelas) / screen`              | 強度÷面積による輝度     |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | 光束×時間による光エネルギー    |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | 照度×時間による光量  |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | 発光効率                   |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | 放射強度                   |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | 焦点距離からの屈折力  |

## 次に読むべきページ

* [光度](luminous-intensity.ja.md) — カンデラ、この分野のネイティブな基本量。
* [光束](luminous-flux.ja.md) と [照度](illuminance.ja.md) — ランプが放出するものと面が受け取るもの。
* [輝度](luminance.ja.md) — ディスプレイの「ニト」表示が指す量。
* [発光効率](luminous-efficacy.ja.md) — 測光量ファミリーと放射量ファミリーをつなぐ橋渡し。
* [ディオプター](dioptre.ja.md) — 屈折力、そして分光学における双子の量である [波数](../mechanics/wavenumber.md)。
</content>
