# 電流

パッケージ: `org.pcsoft.framework.kunit.ec`
基準単位: **アンペア** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

種別: **ネイティブ単位**

電流グループは電流をモデル化します。これは **単純な一次元** のネイティブグループ（指数特化型サブタイプも、
クロス単位の型付き結果もありません）で、`KElectricCurrentUnitInstance` は単一の
`KElectricCurrentUnit.AMPERE` 項をラップし、常にアンペアに正規化して保持します。

SI のアンペアに加えて、このグループは古典的な CGS 電流単位を 2 つ提供します。電磁単位系（EMU）の
**ビオ**（アブアンペア、`1 Bi = 10 A`）と、静電単位系（ESU）の **スタットアンペア**
（`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`）です。

## 単位

| 系統 | 単位 | 列挙値 | 記号 | トークン | 1 単位のアンペア値 |
|---|---|---|---|---:|---:|
| SI | アンペア | `KElectricCurrentUnit.AMPERE` | `A` | `amperes` | 1.0 |
| CGS | ビオ / アブアンペア | `KElectricCurrentUnit.BIOT` | `Bi`（`abA`） | `biot` / `abamperes` | 10 |
| CGS | スタットアンペア | `KElectricCurrentUnit.STATAMPERE` | `statA` | `statamperes` | 3.335641e-10 |

各 `トークン` は、`of`（生成）と `into`（読み取り）で使用する値 1 の `KElectricCurrentUnitInstance` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002（アンペアに正規化）
i into amperes                // 0.002（アンペアで読み取り）
(1 of biot) into amperes      // 10.0
```

## 実例

オームの法則: `R = 220 Ω` の抵抗に `U = 5 V` を印加すると、電流 `I = U / R` が流れます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val voltage = 5.0       // V
val resistance = 220.0  // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.ec.*

// + / - : 同一グループ、単位間で自動変換
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// 比較
(1 of biot) == (10 of amperes)         // true（正規化した量が同じ）
(1 of biot) > (1 of amperes)           // true
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は 2 つの `KElectricCurrentUnitInstance` の正規化された `value`
（アンペア）を比較します。`equals` は正規化した量による比較なので、`(1 of biot) == (10 of amperes)` です。

## `pow` によるべき乗

中置演算子 `pow` で整数のべき乗を計算します（Kotlin にはオーバーロード可能な `^` がありません）。電流グループ
では `pow` は汎用の `KMixedUnitInstance` を返します（電流には次元付きのべき乗型がありません）:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.ec.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## SI 接頭辞

電流は **任意** の桁を受け入れるため、すべての SI 接頭辞ビルダー（`quetta` … `quecto`）をプロパティアクセス
で各電流単位と組み合わせられます。ミリアンペアは `milli.amperes`、キロアンペアは `kilo.amperes` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of milli.amperes).value   // 0.001      （ミリアンペア）
(1 of kilo.amperes).value    // 1000.0     （キロアンペア）

(2500 of amperes) into kilo.amperes  // 2.5
```

## toString の書式

基準単位の `toString()` のみが存在します。特定の単位で表示するには `into` を使用します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of biot).toString()                       // "10.0 A"（基準単位表現）
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## 記法

下表は、この単位とその構成要素を数式表記と KUnit の Kotlin 表記でどう書くかを示します。指数は Unicode 上付き
文字（`²`、`³`、`⁻¹`）を使い、`·` は乗算、`/` は分数を表します。

| 数式 | Kotlin | 意味 |
|---|---|---|
| `A` | `amperes` | 電流、基準単位（アンペア） |
| `mA` | `milli.amperes` | ミリアンペア（アンペアに接頭辞を適用） |
| `kA` | `kilo.amperes` | キロアンペア |
| `Bi` | `biot` | ビオ / アブアンペア（10 A） |
| `A²` | `amperes pow 2` | アンペアの 2 乗（汎用の混合単位） |
