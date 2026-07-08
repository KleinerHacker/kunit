# 時間

パッケージ: `org.pcsoft.framework.kunit.time`
基本単位: **秒**(`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` は [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html)
を 100 % ラップしたものです: `Duration` が唯一の真実の源(ナノ秒精度)であり、`Duration` の完全な API が
転送されます。その上で、他のすべての「純粋な」単位ラッパーと同じ表面(`value`/`+`/`-`/`*`/`/`/`toString`/
`toUnit` と `of`/`into` の動詞)を提供するため、時間の値は汎用の混合単位エンジンに組み込めます(例:
`length / time` = 速度)。値は常に秒に正規化されて保存されます。

`Duration` は単なる継続時間しか表さないため、時間の値は常に指数1です — 時間² や 1/時間 のラッパーはありません
(乗算/除算は長さとまったく同様に生の `KMixedUnitInstance` へ「脱出」します)。したがって
`KMixedUnitInstance.toTime()` は**指数1の**単一の `KTimeUnit` 項のみを受け付けます。

## 単位

| 単位 | Enum 値 | 記号 | トークン | 秒換算(1単位) |
|---|---|---|---:|---:|
| 秒 | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| 分 | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| 時 | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| 日 | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

物理的な時間スケールのみをモデル化しています。暦に基づく単位(週、年)は、固定された物理量ではなく暦によって
定義されるため、意図的に省略されています。各 `トークン` は値1の `KTimeUnitInstance` であり、`of`(作成)と
`into`(読み取り)で使用します。

秒未満のスケール(ミリ秒、マイクロ秒、ナノ秒など)は専用の単位では**ありません** — `seconds` に対する SI
接頭辞ビルダーを通じて汎用的に到達します(下記 [SI 接頭辞](#si) を参照)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0(秒に正規化)
t into hours     // 2.0(時に戻して読み取り)
t into minutes   // 120.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : 同じグループ内、異なる時間単位間の自動変換(正確な Duration 演算)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance、秒に正規化(5400.0)
val b = (2 of hours) - (30 of minutes)

// 比較
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true(正規化された値が同じ)

// * / / : 常に許可され、新しい指数を持つ KMixedUnitInstance を生成
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0, 無次元
```

## 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は2つの `KTimeUnitInstance` を、その基礎となる `Duration`(ナノ秒精度)で
比較します。時間の値は常に指数1であるため、長さの面積/体積のような指数不一致エラーは発生しません。

## `java.time.Duration` ラッパー

`KTimeUnitInstance` は `Duration` に対するドロップインのファサードです: ラップされた `Duration` を取得したり、
既存のものをラップしたり、転送された `Duration` メソッドを直接使用したりできます(`Duration` を返すものは
`KTimeUnitInstance` を返し、クエリメソッドはそのまま通過します)。

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// 転送されたミューテータは KTimeUnitInstance を返す
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// 転送されたクエリメソッドはそのまま通過
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## <a name="si"></a>SI 接頭辞

任意の時間単位は、24種類の SI 接頭辞**ビルダー**(`kilo`、`milli`、`micro` など、ルートパッケージ)のいずれとも
プロパティアクセスで組み合わせることができ、`of`/`into` 用の値1テンプレートを生成します。これが秒未満の
スケールの表現方法です。`Duration` による裏付けが表現可能な範囲を制限する(下記の注記を参照)ため、複数秒の
基底に対する極端な接頭辞は表現できないことに注意してください:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// 構築: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005(秒)

// 接頭辞付きの単位で値を読み戻す
val t = 2 of hours
t into milli.seconds  // 7 200 000.0(ms)
```

!!! note "Duration の範囲"
    値は `java.time.Duration`(整数秒を `Long` として保存、ナノ秒分解能)で裏付けられているため、
    `KTimeUnitInstance` はおおよそ `[1 ns, Long.MAX 秒]`(≈ 2920 億年)の範囲内の大きさしか忠実に表現できません。
    `quetta` を日に適用するような極端な接頭辞はこの範囲を超え、ナノ秒未満の値はゼロに丸められます。汎用の
    `KMixedUnitInstance`/接頭辞層自体は `Double` ベースであり影響を受けません — 範囲が制限されるのは Duration に
    裏付けられたラッパーへの変換のみです。

## toString フォーマット

基本単位の `toString()` のみが存在します。特定の単位は `into` を使ってフォーマットします:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s"(基本単位表現)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## 他の単位との組み合わせ

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0(km/h)

// 速度に時間を掛け戻すと純粋な長さが復元される
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

専用のグループ間演算子を**持たない**グループの2つの純粋な単位(例: `(2 of hours) * (5 of bytes)`)は、
`.toUnit()` を必要とせず、直接 `KMixedUnitInstance` に結合します。
