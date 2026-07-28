# 記憶密度

パッケージ: `org.pcsoft.framework.kunit.it.storagedensity`
基本単位: **バイト毎平方メートル**(`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

種別: **構成された単位**

記憶密度は**構成された**単位です。単一の「実在する」量ではなく、`storage · distance⁻²`(`B/m²`)という合成です。
したがって `KStorageDensityUnitInstance` は、ちょうど2つの項 — 指数 `+1` の `KStorageUnit.BASE`(バイト)と
指数 `-2` の `KDistanceUnit.BASE`(メートル) — からなる `KMixedUnitInstance` をラップします。値は、どの単位や
ストレージ/面積の組み合わせから作成されたかに関係なく、常にバイト毎平方メートルに正規化されて保存されます。

## 記憶密度の作成

記憶密度は**ストレージ毎面積の式**として作成します。例: `100 of bytes / area`、`5 of mega.bytes / area`。
面積は任意の `KAreaUnitInstance`(例: `(1 of meters) * (1 of meters)`)なので、あらゆる SI/二進接頭辞と長さの単位が
自由に組み合わせられます。任意のストレージ毎面積テンプレートで読み戻します(`d into (bits / area)`)。綴られた
複合トークンは意図的に**ありません**。

基本単位: ストレージグループと一貫して、*バイト*毎平方メートルです。「ビット毎平方メートル」は `0.125 B/m²` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0 (B/m² に正規化)
d into (bits / area)  // 800.0 (bit/m² で読み戻し)
```

## 実世界の例: SSD ダイの面記録密度

フラッシュダイは **100 mm²** の面上に **256 GB** を記憶します。その面記録密度は、データ量を面積で割ったものです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // 10 mm × 10 mm のダイ = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15 (B/m²)
density into (giga.bytes / (side * side))           // 256.0 (100 mm² あたり GB)
```

## 基本単位(ストレージと面積)による計算

記憶密度は、ストレージ量を面積で割ったものです。3つの量 — ストレージ、面積、記憶密度 — の間を、素の `*` と `/`
で移動できます。各結果は**強く型付け**されます。

| 式 | 結果の型 | 意味 |
|---|---|---|
| `storage / area` | `KStorageDensityUnitInstance` | 密度 = 量 / 面積 |
| `storage density * area` | `KStorageUnitInstance` | 量 = 密度 × 面積 |
| `area * storage density` | `KStorageUnitInstance` | 量(交換法則) |
| `storage / storage density` | `KAreaUnitInstance` | 面積 = 量 / 密度 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- 基本単位 -> 記憶密度 --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance(.toStorageDensity() は不要!)
d.value               // 100.0 (B/m²)

// --- 記憶密度 -> ストレージ(面積を掛ける)-------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // 同じ結果(交換法則)

// --- 記憶密度 -> 面積(ストレージ量を割る)------------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "*純粋な* ストレージ / 面積 の形のみが記憶密度です"
    `KMixedUnitInstance.toStorageDensity()` は、指数 `+1` のストレージ項ちょうど1つと指数 `-2` の距離項
    ちょうど1つを必要とします。`B²·m⁻²`、`B·m⁻¹`、`B·m²` の形は記憶密度ではなく、変換は
    `IllegalStateException` を送出します。同様に `storage + storage density`(異なる次元)はコンパイルエラーです。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : 同じグループ、バイト系とビット系の密度を自動変換
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// 比較(正規化された B/m² 値による)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// 2つの記憶密度の間の * / / は KMixedUnitInstance に脱出します(もはや純粋な密度ではありません)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## SI および二進(IEC)接頭辞

記憶密度グループは [ストレージ](storage.md) グループの接頭辞ポリシーを踏襲します(その分子はストレージ量です):
分子は**増加系**の SI ビルダー(`kilo`、`mega`、…)または**二進**ビルダー(`kibi`、`mebi`、…)を使用します。
分母(面積)は任意の長さの単位と接頭辞を使用します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## toString による書式化

基本単位の `toString()` のみが存在します。特定の単位は `into` または `format` で書式化します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.it.storage.bytes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²" (基本単位)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## 記法

以下の表は、この単位とその構成要素が数学的にどのように書かれ、KUnit を用いた Kotlin ではどのように書かれるかを示します。指数は Unicode の上付き文字(`²`、`³`、`⁻¹`)を使用し、`·` は乗算、`/` は分数を表します。分数としても負の指数の積としても書ける場合は、両方の Kotlin 形式を示します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `B/m²` | `bytes / area` | 記憶密度、基本単位(バイト毎平方メートル) — 分数形式 |
| `B·m⁻²` | `bytes * (meters pow -2)` | 負の指数の積としての同じ密度 |
| `bit/m²` | `bits / area` | ビット毎平方メートル |
| `kB/mm²` | `kilo.bytes / mm2` | キロバイト毎平方ミリメートル |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | ストレージ ÷ 面積 から作成 |
