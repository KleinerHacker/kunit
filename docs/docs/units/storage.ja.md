# ストレージ

パッケージ: `org.pcsoft.framework.kunit.storage`
基本単位: **バイト**(`KStorageUnit.BASE == KStorageUnit.BYTE`)

ストレージグループはデジタルデータ量をモデル化します。これは**素朴な1次元の**グループです(距離グループのような
指数特化のサブタイプも、時間グループのような `Duration` 裏付けもありません): `KStorageUnitInstance` は単一の
`KStorageUnit.BASE`(バイト)項をラップし、常にバイトに正規化されて保存されます。

このグループを特別にしているのは2点です:

* **縮小接頭辞なし。** ビットの端数は意味のあるデータ量ではないため、縮小 SI 接頭辞(`deci`、`centi`、`milli`
  など — 係数 `< 1`)は `bytes`/`bits` では**使用できません**。`milli.bytes` と書くことは実行時失敗ではなく
  **コンパイルエラー**です: `bytes`/`bits` プロパティは増大 SI ビルダー(`KAugmentingPrefixBuilder`)と2進
  ビルダーにのみ付き、縮小ビルダーには決して付きません。
* **2進(IEC)接頭辞。** 10進 SI ビルダー(`kilo` = 1000)に加えて、2つ目の2進ビルダー体系(`kibi` = 1024、
  `mebi` = 1024² など)があり、値が10進の刻み 1000 と2進の刻み 1024 を区別できます。

## 単位

| 単位 | Enum 値 | 記号 | トークン | バイト換算(1単位) |
|---|---|---|---:|---:|
| バイト | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| ビット | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

1バイトは8ビットです。各 `トークン` は値1の `KStorageUnitInstance` であり、`of`(作成)と `into`(読み取り)で
使用します。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0(バイトに正規化)
size into bits      // 40.0(ビットに戻して読み取り)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : 同じグループ内、ビットとバイトの間の自動変換
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// 比較
(1 of bytes) == (8 of bits)          // true(正規化された量が同じ)
(2 of bytes) > (1 of bytes)          // true

// storage / time は型付きのデータ転送率(データ転送率のページを参照)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は2つの `KStorageUnitInstance` 値の正規化された `value`(バイト)を比較します。
`equals` は正規化された量によるため、`(1 of bytes) == (8 of bits)` です。

## `pow` によるべき乗

infix `pow` 演算子で値を整数乗します(Kotlin にはオーバーロード可能な `^` がありません)。ストレージグループでは
`pow` は一般の `KMixedUnitInstance` を返します(ストレージには次元を持つべき乗型がありません):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## 10進 SI 接頭辞

任意のストレージ単位は、**増大**(1超)SI 接頭辞ビルダー(`deca`、`hecto`、`kilo`、`mega`、`giga`、`tera`、
`peta`、`exa`、`zetta`、`yotta`、`ronna`、`quetta`)とプロパティアクセスで組み合わせられます。縮小ビルダー
(`deci` 以下)には `bytes`/`bits` プロパティが**ない**ため、`milli.bytes` はコンパイルされません。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance(== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003(kB)

// 5 of milli.bytes                  // コンパイルされない: 縮小ビルダーに `bytes` はない
```

## 2進(IEC)接頭辞

2進接頭辞ビルダーは 1024 のべき乗で、値が 1000(`kilo`)と 1024(`kibi`)を区別できるようにします: `kibi`、
`mebi`、`gibi`、`tebi`、`pebi`、`exbi`、`zebi`、`yobi`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (10進)
(1 of kibi.bytes).value   // 1024.0     (2進)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0(KiB)
```

| 2進ビルダー | 記号 | バイト換算(1単位) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## 他の単位との組み合わせ

ストレージの値を時間と組み合わせるとデータ転送率(`byte·second⁻¹`)になり、元に分解し戻すこともできます:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s(型付き KDataRateUnitInstance)
val amount = rate * (60 of seconds)          // 60000 B(型付き KStorageUnitInstance)
amount into kibi.bytes                        // ≈ 58.59(KiB)
```

## toString フォーマット

基本単位の `toString()` のみが存在します。特定の単位は `into` を使ってフォーマットします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B"(基本単位表現)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `B` | `bytes` | データ量、基本単位（バイト） |
| `bit` | `bits` | ビット（`1 B = 8 bit`） |
| `kB` | `kilo.bytes` | 十進接頭辞付きバイト（1000 B） |
| `KiB` | `kibi.bytes` | 二進接頭辞付きバイト（1024 B） |
