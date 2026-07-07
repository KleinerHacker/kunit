# データレート

パッケージ: `org.pcsoft.framework.kunit.datarate`
基本単位: **バイト毎秒** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

データレートは（[速度](speed.md)に続く）2 番目の**構成された(constructed)**単位です。単一の「実在する」
物理量ではなく、`ストレージ · 時間⁻¹` (`B/s`) の組み合わせです。そのため `KDataRateUnitInstance` は、
ちょうど 2 つの項 - 指数 `+1` の `KStorageUnit.BASE`（バイト）と指数 `-1` の `KTimeUnit.BASE`（秒）-
からなる `KMixedUnitInstance` をラップします。値は、どの単位・接頭辞・ストレージ/時間の組み合わせで
生成されても、常にバイト毎秒に正規化されて保存されます。

## 単位

| 単位 | Enum 値 | 記号 | 生成関数 | 1 単位 (B/s) |
|---|---|---|---:|---:|
| バイト毎秒 | `KDataRateUnit.BYTES_PER_SECOND` | `B/s` | `Number.bytesPerSecond` | 1.0 |
| ビット毎秒 | `KDataRateUnit.BITS_PER_SECOND` | `bit/s` | `Number.bitsPerSecond` | 0.125 |

どちらの単位にも、`valueAs`/`toString` のターゲットや接頭辞 infix 関数の `unit` 引数として使える bare な
`val` エイリアスがあります: `bytesPerSecond`, `bitsPerSecond`。

> **バイトベースの基本単位。** 基本単位はバイト毎秒であり、ストレージ群（基本単位がバイト）と一貫して
> います。ネットワークで一般的なビット毎秒 (`bps`) は `0.125 B/s` です。より大きな単位（kB/s, MB/s,
> Mbit/s, KiB/s, …）は専用の enum 値ではなく、後述の接頭辞 DSL から得られます。

```kotlin
import org.pcsoft.framework.kunit.datarate.*

val r = 100.bytesPerSecond
r.value                                     // 100.0 (B/s に正規化)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 800.0 (bit/s で読み戻し)
r.valueAs(bitsPerSecond)                     // 800.0 (bare エイリアス経由)
```

## 核心単位（ストレージ & 時間）で計算する

これこそが構成された単位の要点です。データレート*は*ストレージ量を時間で割ったものです。KUnit では、
ストレージ・時間・データレートの 3 つの量の間を、単純な `*` と `/` で行き来でき、各結果は**強く型付け**
されます。生の `KMixedUnitInstance` を自分で組み立てたり展開したりする必要はありません。

有効な 4 つの組み合わせと結果の型:

| 式 | 結果の型 | 意味 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | レート = 量 / 時間 |
| `data rate * time` | `KStorageUnitInstance` | 量 = レート × 時間 |
| `time * data rate` | `KStorageUnitInstance` | 量（可換） |
| `storage / data rate` | `KTimeUnitInstance` | 時間 = 量 / レート |

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 核心単位 -> データレート -------------------------------------------
val r = 100.bytes / 10.seconds            // KDataRateUnitInstance（.toDataRate() 不要！）
r.value                                     // 10.0 (B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 80.0

// 代入先の型は何も変換しません - 演算子がすでに KDataRateUnitInstance を返します。
val explicit: KDataRateUnitInstance = 100.bytes / 10.seconds

// --- データレート -> ストレージ（時間を掛ける） ------------------------
val amount = r * 60.seconds               // KStorageUnitInstance
amount.value                                // 600.0 (B)
amount.valueAs(bytes)                        // 600.0
amount.valueAs(bits)                         // 4800.0（任意のストレージ単位で読み戻し）
60.seconds * r                            // 同じ結果（可換）

// --- データレート -> 時間（ストレージ量を割る） ------------------------
val time = 600.bytes / r                  // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
```

!!! warning "*純粋な* ストレージ / 時間 の形だけがデータレート"
    `KMixedUnitInstance.toDataRate()` は、指数 `+1` のストレージ項 1 つと指数 `-1` の時間項 1 つを厳密に
    要求します。`B²`（ストレージの二乗）、`B·s⁻²`、`B·s` の形はデータレートではなく、変換は誤った値を
    黙って返す代わりに `IllegalStateException` をスローします。同様に `storage + data rate`（次元が異なる）
    はコンパイルエラーです。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.datarate.*

// + / - : 同一群、異なるデータレート単位間で自動変換
val a = 1.bytesPerSecond + 8.bitsPerSecond   // KDataRateUnitInstance, 2 B/s
val b = 2.bytesPerSecond - 8.bitsPerSecond   // 1 B/s

// 比較（正規化された B/s 値による）
1.bytesPerSecond > 4.bitsPerSecond           // true  (1 B/s > 0.5 B/s)
1.bytesPerSecond == 8.bitsPerSecond          // true  (同じ正規化値)

// 2 つのデータレート間の * / / は KMixedUnitInstance に脱出します（もはや純粋なレートではない）
val squared = 10.bytesPerSecond * 2.bytesPerSecond // KMixedUnitInstance, units=[B^2, s^-2]
```

## 比較と等価性

`==`, `!=`, `<`, `<=`, `>`, `>=` は 2 つの `KDataRateUnitInstance` の正規化された `value`（バイト毎秒）を
比較します。データレートは常に同じ次元を持つため、指数チェックは不要です。

## SI 接頭辞とバイナリ (IEC) 接頭辞

データレート群は[ストレージ](storage.md)群の接頭辞ポリシーを踏襲します（分子がストレージ量のため）:

* **縮小しない**十進 SI 接頭辞（`deca` 以上、係数 >= 1）のみを提供します。縮小する接頭辞（`deci`,
  `centi`, `milli`, …）は**存在しません** - `5 milli bytesPerSecond` は実行時失敗ではなく**コンパイル
  エラー**です。
* 十進 SI 接頭辞に加えて、**バイナリ IEC 接頭辞**（`kibi`, `mebi`, `gibi`, …、1024 の累乗、
  `KStorageBinaryPrefix` から再利用）が利用可能で、レートは 1000 (`kilo`) と 1024 (`kibi`) を区別できます。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

// 構築: "5 mega bytesPerSecond" -> KDataRateUnitInstance（直接、== 5_000_000.bytesPerSecond）
val download = 5 mega bytesPerSecond
download.value // 5000000.0

// 十進 vs バイナリ: 1000 (kilo) != 1024 (kibi)
(1 kilo bytesPerSecond).value // 1000.0
(1 kibi bytesPerSecond).value // 1024.0

// スケール済みの全体レートターゲットで値を読み戻す
val r = 4096.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with bytesPerSecond)              // 4.096  (kB/s)
r.valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond)     // 4.0    (KiB/s)
```

データレートを明示的な**ストレージ毎時間ペア**（2 つのターゲット）として読み戻すこともできます:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.datarate.*

val r = 5000.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)   // 5.0 (kB / s)
r.toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)  // "5.0 kB*s^-1"
```

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

10.bytesPerSecond.toString()                                    // "10.0 B/s"（基本単位）
(100.bytes / 10.seconds).toString(KDataRateUnit.BITS_PER_SECOND) // "80.0 bit/s"
4096.bytesPerSecond.toString(KStorageBinaryPrefix.KIBI with bytesPerSecond) // "4.0 KiB/s"
```
