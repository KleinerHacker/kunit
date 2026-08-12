# 放射能(ベクレル)

パッケージ: `org.pcsoft.framework.kunit.kinematic.frequency`
基本単位: **ヘルツ**(`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

種別: **実在する単位**

放射性試料の放射能 `A` は、1秒あたりの原子核崩壊数です。その単位は**ベクレル**であり、`1 Bq = 1 s⁻¹` —
[周波数](frequency.ja.md)と**次元的に同一**です。

## ベクレルが独自の型を持たない理由

KUnit は、放射能を独自の `KActivityUnitInstance` ではなく、意図的に `KFrequencyUnitInstance` でモデル化して
います。理由はこのライブラリの形式認識の契約にあります:

* すべての標準化されたグループは**唯一の**正準な基本次元の正規形を持ち、
* `toX()` はまさにその形式を認識します。

放射能と周波数は正規形 `time⁻¹` を共有しています。1つの正規形に対して2つの型があると、ネイティブ表現が曖昧になり
ます — `toFrequency()` と仮想の `toActivity()` は同じ混合単位にマッチしてしまい、どちらの答えがより正しいとも
言えません。単一の型が往復変換の決定性を保ちます。

その違いは*変数に何と名付けるか*の問題です: 周波数は周期的なサイクルを数え、放射能はランダムな崩壊を数えます
が、どちらも「1秒あたりのイベント数」です。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // 37 GBq として読む — ラジウム1グラム分
activity into mega.hertz             // 37 000.0

// 1分間の崩壊数
val decays = activity * (60 of seconds)   // 無次元の個数
decays                                     // 2.22e12
```

!!! note "キュリー"
    歴史的な単位はキュリーであり、1 Ci = 3.7 × 10¹⁰ Bq です。専用のトークンはありません。
    `37 of giga.hertz` と書くか、独自の定数を導入してください。

## 実世界の例 — 煙感知器の線源

家庭用の煙感知器には約 **30 kBq** のアメリシウム241が含まれています:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// 1日あたりの崩壊数
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## 関連項目

* [周波数](frequency.ja.md) — 同じ型を、周期的なレートとして読んだもの。
* [線量率](../thermodynamics/dose-rate.ja.md) — 線源が時間あたりに与える線量。
* [吸収線量](../thermodynamics/absorbed-dose.ja.md) — エネルギーに基づく線量。
