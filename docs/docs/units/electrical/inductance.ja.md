# インダクタンス

パッケージ: `org.pcsoft.framework.kunit.electric.inductance`
基本単位: **ヘンリー**(`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

種別: **構成された単位**

インダクタンスは**構成された**単位で、合成 `mass · length² · time⁻² · current⁻²`(`kg·m²·s⁻²·A⁻²`)です。
`KInductanceUnitInstance` は4つの項 — 指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+2` の
`KDistanceUnit.BASE`(メートル)、指数 `-2` の `KTimeUnit.BASE`(秒)、指数 `-2` の
`KElectricCurrentUnit.BASE`(アンペア) — をラップします。ライブラリの質量成分は**グラム**(キログラムではない)
に正規化されるため、ヘンリーは生の成分基準の1000倍です。保存される値はヘンリーに正規化されます。

## インダクタンスの作成

インダクタンスは名前付きトークンから、または分解(下記参照)から作成します。名前付き単位は値1のトークンとして
残ります(`of`/`into` で使用):

| インダクタンス | 記号 | トークン | H 換算(1単位) |
|---|---|---:|---:|
| ヘンリー | `H` | `henries` | 1.0 |
| ウェーバ毎アンペア | `Wb/A` | `webersPerAmpere` | 1.0 |
| アブヘンリー(CGS-EMU) | `abH` | `abhenries` | 1.0e-9 |
| スタットヘンリー(CGS-ESU) | `statH` | `stathenries` | 8.987551787e11 |

名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`milli.henries`、`micro.henries`、
`nano.henries` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## 複数の分解

インダクタンスはいくつかの**等価な分解**から得られ、いずれも同じ値の等しいインダクタンスを生成します:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `flux / current` | `KInductanceUnitInstance` | 定義 `L = Φ / I` |
| `resistance / frequency` | `KInductanceUnitInstance` | リアクタンス形 `L = X / ω`(`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | `.toInductance()` 経由 | ネイティブな正規形 `kg·m²·s⁻²·A⁻²` の式 |

型付き演算子の形式はインダクタンスを直接返します。完全にネイティブな式は汎用の `KMixedUnitInstance` のままで、
`toInductance()`(正規形のみを認識し、そうでなければ `IllegalStateException` を投げる)で絞り込みます。
すべての経路は値が等しくなります。

逆の演算子は磁束・電流・周波数・抵抗を結び付けます:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I`(可換) |
| `flux / inductance` | `KElectricCurrentUnitInstance` | `I = Φ / L` |
| `inductance * frequency` | `KResistanceUnitInstance` | `X = ω · L` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.inductance.*

// 実例 - スイッチング電源のチョークコイル: 470 µH のコイルに 2 A が流れると鎖交磁束は 0.00094 Wb となり、
// 角周波数 100 kHz ではリアクタンスは 47 Ω になります。
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance、0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance、47 Ω

// 同じインダクタンスを定義とリアクタンス形から:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// 同じインダクタンスをネイティブな kg·m²·s⁻²·A⁻² の式で:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## パーミアンス

磁気回路の**パーミアンス** `Λ` は、その[磁気抵抗](reluctance.md)の逆数です（`Λ = 1 / Rm`）。これは
インダクタンスと**次元的に同一**であり、同様にヘンリーで測定されるため、KUnitはこのグループと記号 `H` で
モデル化します。専用のトークンや専用の型はありません。逆数演算子が両グループを結び付けます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.inductance.*
import org.pcsoft.framework.kunit.electric.reluctance.*

// Rm = 500 A/Wb の磁気回路のパーミアンスは 2 mH。
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// …そして逆も同様:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

(2 of henries).toString()     // "2.0 H"(基本単位)
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `H` | `henries` | インダクタンス、基本単位（名前付きトークン、ヘンリー） |
| `Wb/A` | `webersPerAmpere` | ウェーバ毎アンペアとしてのインダクタンス（名前付きトークン） |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | 質量·長さ² / (時間²·電流²) としてのインダクタンス（分数形式） |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | 同じインダクタンスを純粋な積で表現 |
| `mH` | `milli.henries` | 接頭辞付きのインダクタンス（ミリヘンリー） |
