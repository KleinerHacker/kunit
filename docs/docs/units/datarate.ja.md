# データ転送率

パッケージ: `org.pcsoft.framework.kunit.datarate`
基本単位: **バイト毎秒**(`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

データ転送率は**構成された**単位です([速度](speed.md) に続く2つ目): 単一の「実在する」量ではなく、
`storage · time⁻¹`(`B/s`)という合成です。したがって `KDataRateUnitInstance` は、ちょうど2つの項 — 指数 `+1`
の `KStorageUnit.BASE`(バイト)と指数 `-1` の `KTimeUnit.BASE`(秒) — からなる `KMixedUnitInstance` をラップ
します。値は、どの単位やストレージ/時間の組み合わせから作成されたかに関係なく、常にバイト毎秒に正規化されて
保存されます。

## データ転送率の作成

データ転送率は**ストレージ毎時間の式**として作成します。例: `100 of bytes / seconds`、
`5 of mega.bytes / seconds`、`10 of kibi.bytes / seconds` — いずれも `KDataRateUnitInstance` を生成します。
任意のストレージ毎時間テンプレートで読み戻します(`r into (bits / seconds)`)。`bytesPerSecond` のような綴られた
複合トークンは意図的に**ありません**(それらはまさに `bytes / seconds` です)。

基本単位: ストレージグループと一貫して、*バイト*毎秒です。ネットワークで一般的な bit/s(`bps`)は `0.125 B/s`
です。「メガビット毎秒」は `1 of mega.bits / seconds` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0(B/s に正規化)
r into (bits / seconds)  // 800.0(bit/s に戻して読み取り)
```

## 中核単位(ストレージと時間)での計算

データ転送率*とは*、ストレージ量を時間で割ったものです。3つの量 — ストレージ、時間、データ転送率 — の間を
単純な `*` と `/` で行き来でき、各結果は**強く型付け**されます。

| 式 | 結果の型 | 意味 |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | 転送率 = 量 / 時間 |
| `data rate * time` | `KStorageUnitInstance` | 量 = 転送率 × 時間 |
| `time * data rate` | `KStorageUnitInstance` | 量(可換) |
| `storage / data rate` | `KTimeUnitInstance` | 時間 = 量 / 転送率 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- 中核単位 -> データ転送率 --------------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance(.toDataRate() は不要!)
r.value                  // 10.0(B/s)
r into (bits / seconds)  // 80.0

// 接頭辞付きの分子、括弧不要:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance(5 MB/s)

// --- データ転送率 -> ストレージ(時間を掛ける) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // 同じ結果(可換)

// --- データ転送率 -> 時間(ストレージ量をそれで割る) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "データ転送率になるのは*純粋な*ストレージ/時間の形のみ"
    `KMixedUnitInstance.toDataRate()` は、ちょうど1つの指数 `+1` のストレージ項と1つの指数 `-1` の時間項を
    要求します。`B²`(ストレージの2乗)、`B·s⁻²`、`B·s` の形はデータ転送率ではありません — 変換は
    `IllegalStateException` をスローします。同様に、`storage + data rate`(異なる次元)はコンパイルエラーです。

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : 同じグループ内、バイトベースとビットベースの転送率の間の自動変換
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance、2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// 比較(正規化された B/s 値による)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// 2つのデータ転送率の間の * / / は KMixedUnitInstance に脱出する(もはや純粋な転送率ではない)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance, [B^2, s^-2]
```

## SI および2進(IEC)接頭辞

データ転送率グループは [ストレージ](storage.md) グループの接頭辞ポリシーを反映します(その分子はストレージ量
です): 分子は**増大** SI ビルダー(`kilo`、`mega` など)または**2進**ビルダー(`kibi`、`mebi` など)を使用
します。縮小ビルダーには `bytes`/`bits` プロパティがないため、`milli.bytes / seconds` はコンパイルされません。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// 10進 vs 2進: 1000(kilo)!= 1024(kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// ストレージ毎時間テンプレートで値を読み戻す
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096(kB/s)
r into (kibi.bytes / seconds)  // 4.0  (KiB/s)
```

## toString フォーマット

基本単位の `toString()` のみが存在します。特定の単位は `into` を使ってフォーマットします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s"(基本単位)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```
