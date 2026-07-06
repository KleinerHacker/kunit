# ストレージ

パッケージ: `org.pcsoft.framework.kunit.storage`
基準単位: **バイト** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

ストレージグループはデジタルデータ量をモデル化します。これは**単純な 1 次元**のグループです（距離グループの
ように指数ごとのサブタイプを持たず、時間グループのように `Duration` を基盤にもしていません）。
`KStorageUnitInstance` は単一の `KStorageUnit.BASE`（バイト）項をラップし、常にバイトに正規化して保持します。

このグループには 2 つの特別な点があります:

* **値を小さくする接頭辞は無し。** ビットの端数は意味のあるデータ量ではないため、値を小さくする SI 接頭辞
  （`deci`、`centi`、`milli` など — 係数 `< 1`）は**提供されません**。`5 milli bytes` と書くと実行時
  エラーではなく**コンパイルエラー**になります。値を小さくしない SI 接頭辞（`deca` 以上）のみ存在します。
* **バイナリ（IEC）接頭辞。** 十進 SI 接頭辞（`kilo` = 1000）に加えて、2 番目のバイナリ接頭辞体系
  （`KStorageBinaryPrefix`: `kibi` = 1024、`mebi` = 1024²、…）があり、値が十進のステップ 1000 と
  バイナリのステップ 1024 を区別できます。

## 単位

| 単位 | 列挙値 | 記号 | 生成子 | バイト換算 |
|---|---|---|---:|---:|
| バイト | `KStorageUnit.BYTE` | `B` | `Number.bytes` | 1.0 |
| ビット | `KStorageUnit.BIT` | `bit` | `Number.bits` | 0.125 |

1 バイトは 8 ビットです。両単位とも `valueAs`/`toString` のターゲット、または接頭辞 `infix` 関数の `unit`
引数として使える bare `val` エイリアス（`bytes`、`bits`）を持ちます。

```kotlin
import org.pcsoft.framework.kunit.storage.*

val size = 5.bytes
size.value                     // 5.0 (normalized to bytes)
size.valueAs(bits)             // 40.0 (read back in bits)
1.bytes.valueAs(bits)          // 8.0
8.bits.valueAs(bytes)          // 1.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.storage.*

// + / - : 同一グループ、ビットとバイトの間で自動変換
val a = 1.bytes + 8.bits        // KStorageUnitInstance: 2.0 B
val b = 4.bytes - 16.bits       // KStorageUnitInstance: 2.0 B

// 比較
1.bytes == 8.bits               // true (same normalized amount)
2.bytes > 1.bytes               // true

// * / / は混合エンジンに委譲（KMixedUnitInstance に対して）
val rate = 1000.bytes.toUnit() / 2.seconds.toUnit() // KMixedUnitInstance: 500 B·s⁻¹
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は 2 つの `KStorageUnitInstance` 値の正規化された `value`（バイト）を
比較します。`equals` は正規化された量に基づくため `1.bytes == 8.bits` です。

## `pow` によるべき乗

中置 `pow` 演算子で値を整数べき乗します（Kotlin にはオーバーロード可能な `^` がありません）。ストレージ
グループでは `pow` は汎用の `KMixedUnitInstance` を返します（ストレージには次元付きのべき乗型がありません）:

```kotlin
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = 2.bytes pow 2     // KMixedUnitInstance: 4.0 B²
```

`pow` は名前付き中置関数なので、`* / + -` よりも**弱く**結合します。混合式では括弧を付けてください
（`(a * b) pow 2`）。

## 十進 SI 接頭辞

任意の `KStorageUnit` は**値を小さくしない** SI 接頭辞（`deca`、`hecto`、`kilo`、`mega`、`giga`、
`tera`、`peta`、`exa`、`zetta`、`yotta`、`ronna`、`quetta`）と組み合わせられます。ストレージグループの
`infix` 構築関数（直接 `KStorageUnitInstance` を返す）と `with`（`valueAs`/`toString` ターゲット用）を
使います。値を小さくする接頭辞（`deci` 以下）はストレージには**存在しません**。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 kilo bytes                 // KStorageUnitInstance (== 5000.bytes)
fiveKb.value                              // 5000.0

val big = 3.bytes
big.valueAs(KUnitPrefix.KILO with bytes)  // 0.003 (kB)

// 5 milli bytes                          // コンパイル不可: 値を小さくする接頭辞は提供されない
```

## バイナリ（IEC）接頭辞

バイナリ接頭辞は 1024 のべき乗であり、値が 1000（`kilo`）と 1024（`kibi`）を区別できるようにします。
`infix` 構築関数（`kibi`、`mebi`、`gibi`、`tebi`、`pebi`、`exbi`、`zebi`、`yobi`）として、また
`KStorageBinaryPrefix` + `with` を介した `valueAs`/`toString` ターゲットとして提供されます。

```kotlin
import org.pcsoft.framework.kunit.storage.*

(1 kilo bytes).value                                  // 1000.0  (decimal)
(1 kibi bytes).value                                  // 1024.0  (binary)
(1 mega bytes).value                                  // 1_000_000.0
(1 mebi bytes).value                                  // 1_048_576.0

val file = 4 mebi bytes
file.valueAs(KStorageBinaryPrefix.KIBI with bytes)    // 4096.0 (KiB)
file.toString(KStorageBinaryPrefix.MEBI with bytes)   // "4.0 MiB"
```

| バイナリ接頭辞 | 列挙値 | 記号 | `infix` | 係数 |
|---|---|---|---:|---:|
| Kibi | `KStorageBinaryPrefix.KIBI` | `Ki` | `kibi` | 1024 |
| Mebi | `KStorageBinaryPrefix.MEBI` | `Mi` | `mebi` | 1024² |
| Gibi | `KStorageBinaryPrefix.GIBI` | `Gi` | `gibi` | 1024³ |
| Tebi | `KStorageBinaryPrefix.TEBI` | `Ti` | `tebi` | 1024⁴ |
| Pebi | `KStorageBinaryPrefix.PEBI` | `Pi` | `pebi` | 1024⁵ |
| Exbi | `KStorageBinaryPrefix.EXBI` | `Ei` | `exbi` | 1024⁶ |
| Zebi | `KStorageBinaryPrefix.ZEBI` | `Zi` | `zebi` | 1024⁷ |
| Yobi | `KStorageBinaryPrefix.YOBI` | `Yi` | `yobi` | 1024⁸ |

## 他の単位との混合

ストレージ値を時間と組み合わせると、混合エンジンを通じてデータレート（`byte·second⁻¹`）になり、再び分解
できます:

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = 1000.bytes.toUnit() / 1.seconds.toUnit()   // 1000 B/s
val amount = (rate * 60.seconds.toUnit()).toStorage() // 60000 B
amount.valueAs(KStorageBinaryPrefix.KIBI with bytes)  // ≈ 58.59 (KiB)
```

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.storage.*

1024.bytes.toString()                                   // "1024.0 B" (base unit representation)
5.bits.toString(bits)                                   // "5.0 bit"
2048.bytes.toString(KStorageBinaryPrefix.KIBI with bytes) // "2.0 KiB"
```
