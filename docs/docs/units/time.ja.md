# 時間

パッケージ: `org.pcsoft.framework.kunit.time`
基本単位: **秒** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` は [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
を 100% ラップします。`Duration` が唯一の情報源（ナノ秒精度）であり、`Duration` の全 API が転送されます。
その上で、他のすべての「純粋」単位ラッパーと同じインターフェース（`value`/`valueAs`/`+`/`-`/`*`/`/`/
`toString`/`toUnit`）を提供するため、時間値は汎用の混合単位エンジンにそのまま接続できます
（例: `length / time` = 速度）。値は常に秒に正規化して保持されます。

`Duration` は常に単純な期間のみを表すため、時間値は常に指数 1 です —— time² や 1/time のラッパーはあり
ません（乗算/除算は長さと同様に生の `KMixedUnitInstance` へ「エスケープ」します）。したがって
`KMixedUnitInstance.toTime()` は**指数 1 の**単一 `KTimeUnit` 項のみを受け付けます。

## 単位

| 単位 | Enum 値 | 記号 | 生成関数 | 1 単位 (秒) |
|---|---|---|---:|---:|
| 秒 | `KTimeUnit.SECOND` | `s` | `Number.seconds` | 1.0 |
| 分 | `KTimeUnit.MINUTE` | `min` | `Number.minutes` | 60.0 |
| 時 | `KTimeUnit.HOUR` | `h` | `Number.hours` | 3600.0 |
| 日 | `KTimeUnit.DAY` | `d` | `Number.days` | 86 400.0 |

物理的な時間スケールのみをモデル化しています。暦に基づく単位（週、年）は、固定された物理量ではなく暦によって
定義されるため、意図的に除外しています。

上記の各単位には、`valueAs`/`toString` のターゲットとして、または接頭辞 infix 関数の `unit` 引数として
使用できる素の `val` エイリアスがあります: `seconds`、`minutes`、`hours`、`days`。

ミリ秒・マイクロ秒・ナノ秒などのサブ秒スケールは専用単位では**なく**、`second` に SI 接頭辞を適用して
汎用的に表現します（下記の [SI 接頭辞](#si) を参照）。

```kotlin
import org.pcsoft.framework.kunit.time.*

val t = 2.hours
t.value                      // 7200.0 (秒に正規化)
t.valueAs(KTimeUnit.HOUR)    // 2.0 (時として読み戻し)
t.valueAs(minutes)           // 120.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.time.*

// + / - : 同じグループ、異なる時間単位間の自動変換（正確な Duration 演算）
val a = 1.hours + 30.minutes   // KTimeUnitInstance、秒に正規化 (5400.0)
val b = 2.hours - 30.minutes

// 比較
2.hours > 90.minutes            // true
1.hours == 60.minutes           // true (正規化値が等しい)

// * / / : 常に許可され、新しい指数を持つ KMixedUnitInstance を生成
val secondsSquared = 3.seconds * 4.seconds   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = 10.seconds / 2.seconds           // KMixedUnitInstance: value=5.0, 無次元
```

## 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は 2 つの `KTimeUnitInstance` を基底の `Duration`（ナノ秒精度）で比較
します。時間値は常に指数 1 なので、長さの面積/体積のような指数不一致エラーは発生しません。

## `java.time.Duration` ラッパー

`KTimeUnitInstance` は `Duration` のドロップイン・ファサードです: ラップされた `Duration` を取得し、既存
の `Duration` をラップし、転送された `Duration` メソッドを直接使用できます（`Duration` を返すメソッドは
`KTimeUnitInstance` を返し、問い合わせメソッドはそのまま透過します）。

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.time.*

val t = 90.minutes
t.toDuration()                       // PT1H30M
Duration.ofMinutes(90).toTime() // KTimeUnitInstance, valueAs(HOUR) == 1.5

// 転送された変更メソッドは KTimeUnitInstance を返す
t.plusHours(1).valueAs(KTimeUnit.HOUR) // 2.5
t.negated().isNegative()               // true

// 転送された問い合わせメソッドはそのまま透過
t.toHours()      // 1
t.toMinutesPart() // 30
t.dividedBy(30.minutes) // 3
```

## SI 接頭辞

任意の `KTimeUnit` は、グループごとの infix 構築関数（具体単位を直接返す）と `with`（valueAs/toString のターゲット用）を使って、24 個の
SI 接頭辞（`KUnitPrefix`、ルートパッケージ、Quetta/Q から Quecto/q まで）のいずれとも組み合わせられます。
サブ秒（および日を超える）スケールはこの方法で表現します。

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.time.*

// 構築: "5 milli seconds" -> KTimeUnitInstance (direct)
val fiveMillis = 5 milli seconds
fiveMillis.value // 0.005 (秒)

// 接頭辞付きターゲットを使って値を読み戻す
val t = 2.hours
t.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND)  // 7 200 000.0 (ms)
t.toString(KUnitPrefix.MILLI with KTimeUnit.SECOND) // "7200000.0 ms"
```

!!! note "Duration の範囲"
    値が `java.time.Duration`（整数秒を `Long` で保持、ナノ秒分解能）で支えられているため、
    `KTimeUnitInstance` はおおよそ `[1 ns, Long.MAX 秒]`（≈ 2920 億年）の範囲の大きさのみを正確に表現
    できます。日に `quetta` のような極端な接頭辞を適用するとこの範囲を超え、ナノ秒未満の値は 0 に丸め
    られます。汎用の `KMixedUnitInstance`/接頭辞レイヤー自体は `Double` ベースで影響を受けません —— Duration
    に支えられたラッパーへの変換のみが範囲制限されます。

## toString フォーマット

```kotlin
import org.pcsoft.framework.kunit.time.*

2.hours.toString()               // "7200.0 s" (基本単位表現)
2.hours.toString(KTimeUnit.HOUR) // "2.0 h"
2.hours.toString(minutes)        // "120.0 min"
```

## 他の単位との混合

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = 10.meters / 1.seconds.toUnit()          // KMixedUnitInstance, units=[METER^1, SECOND^-1]
speed.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR) // "36.0 km*h^-1"

// 速度に時間を掛け戻すと純粋な長さが復元される
val distance = speed * 2.seconds.toUnit()
distance.toDistance().value // 20.0
```
