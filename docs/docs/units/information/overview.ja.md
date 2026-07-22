# 情報技術 — 概要

パッケージ: `org.pcsoft.framework.kunit.storage`、`…datarate`

情報技術は**デジタルデータ量**と、それがどれだけ速く移動するかを扱います。KUnit は蓄積された量を
**ネイティブ**基本量(ストレージ、バイト単位)として、スループットをそれから**構成された**量
(データレート = ストレージ ÷ 時間)としてモデル化するため、「このダウンロードはどのくらいかかる?」という
日常的な問いが型付きの式になります。

## この話題の単位

| 単位 | 種別 | 基準単位 | ページ |
|---|---|---|---|
| ストレージ | ネイティブ | バイト(`B`) | [ストレージ](storage.md) |
| データレート | 構成 | バイト毎秒(`B/s`) | [データレート](datarate.md) |

両グループは同じプレフィックス方針を共有します:**減少プレフィックスなし**(ビットの端数は無意味)、
そして 10 進 SI プレフィックス(`kilo` = 1000)に加えて 2 番目の**バイナリ(IEC)**系列(`kibi` = 1024)。

## 量どうしの関係

| 式 | 結果 | 公式 |
|---|---|---|
| `storage / time` | データレート | `r = 量 / t` |
| `data rate * time` | ストレージ | `量 = r · t` |
| `time * data rate` | ストレージ | `量 = r · t`(可換) |
| `storage / data rate` | 時間 | `t = 量 / r` |

## 実例 — ダウンロード時間

**500 MB** のファイルを **10 MB/s** の回線でダウンロードします。時間は `t = 量 / レート` であり、
レートにその時間を掛けると量 `量 = r · t` を再現します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance、10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0(s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0(MB)
```

## 実例 — 10 進 vs バイナリ

同じ数値の量が、10 進(`kB`)とバイナリ(`KiB`)のテンプレートで異なって読まれます — 1000 対 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096(kB、10 進 1000)
size into kibi.bytes    // 4.0  (KiB、バイナリ 1024)
```

## 値の出力(`toString`)

`toString()` は値をそのグループの**基準単位**(値 + 記号)で出力します。他の単位には `into` を文字列
テンプレート内で使い、記号を自分で付け足します:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s"(基準単位)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## 記法

下表は、この分野の中核的な関係を数学表記と KUnit の Kotlin 表記で対比します。指数は Unicode 上付き文字
(`⁻¹`)、`·` は乗算、`/` は分数を表します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `r = 量 / t` | `(500 of mega.bytes) / (50 of seconds)` | 量÷時間からデータレート |
| `量 = r · t` | `rate * (50 of seconds)` | レート×時間から量 |
| `t = 量 / r` | `amount / rate` | 量÷レートから時間 |
| `1 kB = 1000 B` | `kilo.bytes` | 10 進プレフィックスのバイト |
| `1 KiB = 1024 B` | `kibi.bytes` | バイナリプレフィックスのバイト |

## 次に読むもの

* [ストレージ](storage.md) — ネイティブなバイトのグループ、10 進とバイナリのプレフィックス。
* [データレート](datarate.md) — ストレージ ÷ 時間と、ストレージ↔時間↔レートの演算子。
