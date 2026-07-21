# 質量

パッケージ: `org.pcsoft.framework.kunit.mass`
基準単位: **グラム** (`KMassUnit.BASE == KMassUnit.GRAM`)

質量グループは質量の量をモデル化します。これは **単純な一次元** グループ（距離グループのような指数特化型サブタイプ
も、時間グループのような `Duration` バッキングもありません）で、`KMassUnitInstance` は単一の
`KMassUnit.GRAM` 項をラップし、常にグラムに正規化して保持します。

基準単位は意図的に **キログラムではなくグラム** です。キログラムは専用の単位ではなく、単に SI 接頭辞 `kilo`
をグラムに適用した `kilo.grams` です。グラムのあらゆる十進の桁（ミリグラム、キログラムなど）は、SI 接頭辞を
通じて同じ汎用的な方法で得られます。

## 単位

| 系統 | 単位 | 列挙値 | 記号 | トークン | 1 単位のグラム値 |
|---|---|---|---|---:|---:|
| メートル法 | グラム | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| メートル法 | トン（メトリックトン） | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| メートル法 | カラット（メートル） | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| 常衡 | グレーン | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| 常衡 | ドラム | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| 常衡 | オンス | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| 常衡 | ポンド | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| 常衡 | ストーン | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| 常衡 | ハンドレッドウェイト US（ショート） | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| 常衡 | ハンドレッドウェイト UK（ロング） | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| 常衡 | ショートトン（US） | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| 常衡 | ロングトン（UK） | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| 常衡 | スラグ | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| トロイ | ペニーウェイト | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| トロイ | トロイオンス | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| トロイ | トロイポンド | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| 歴史的 | ドイツポンド | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| 歴史的 | ツェントナー | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| 歴史的 | ロート | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| 地域 | 斤 | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| 地域 | 両 | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| 地域 | 匁 | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| 地域 | 貫 | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| 科学 | ダルトン（u） | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

各 `トークン` は `of`（構築）と `into`（読み取り）で使う値 1 の `KMassUnitInstance` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g（キログラムは `kilo.grams`）
m.value                      // 2000.0（グラムに正規化）
m into pounds                // ≈ 4.409（ポンドで読み取り）
(1 of pounds) into grams     // 453.59237
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : 同一グループ、単位間の自動変換
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// 比較
(1 of kilo.grams) == (1000 of grams)         // true（正規化した量が同じ）
(1 of kilo.grams) > (500 of grams)           // true
```

### 比較と等価性

`==`、`!=`、`<`、`<=`、`>`、`>=` は 2 つの `KMassUnitInstance` の正規化された `value`（グラム）を比較します。
`equals` は正規化した量による比較なので、`(1 of kilo.grams) == (1000 of grams)` です。

## `pow` によるべき乗

中置演算子 `pow` で整数べき乗を行います（Kotlin にはオーバーロード可能な `^` がありません）。質量グループでは
`pow` は汎用の `KMixedUnitInstance` を返します（質量には次元付きのべき乗型がありません）:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## SI 接頭辞

質量は **任意の** 桁を受け入れるため、すべての SI 接頭辞ビルダー（`quetta` … `quecto`）をプロパティアクセスで
あらゆる質量単位と組み合わせられます。キログラムはまさに `kilo.grams`、ミリグラムは `milli.grams` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     （キログラム）
(1 of milli.grams).value   // 0.001      （ミリグラム）

(2500 of grams) into kilo.grams  // 2.5
```

## toString の書式化

基準単位の `toString()` のみが存在します。特定の単位で書式化するには `into` を使います:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g"（基準単位表現）
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `g` | `grams` | 質量、基本単位（グラム） |
| `kg` | `kilo.grams` | キログラム（グラムに接頭辞を適用） |
| `mg` | `milli.grams` | ミリグラム |
| `g²` | `grams pow 2` | グラムの2乗（汎用混合単位） |
