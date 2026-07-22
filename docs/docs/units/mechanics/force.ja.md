# 力

パッケージ: `org.pcsoft.framework.kunit.force`
基本単位: **ニュートン**(`KForceUnit.BASE == KForceUnit.NEWTON`)

種別: **構成された単位**

力は**構成された**単位で、合成 `mass · length · time⁻²`(`kg·m/s²`)です。`KForceUnitInstance` は3つの項 —
指数 `+1` の `KMassUnit.BASE`(グラム)、指数 `+1` の `KDistanceUnit.BASE`(メートル)、指数 `-2` の
`KTimeUnit.BASE`(秒) — をラップします。ライブラリの質量成分は**グラム**(キログラムではない)に正規化される
ため、ニュートンは生の成分基準の1000倍です。保存される値は生の成分値で、ニュートンでの読み取りはこの固定係数
で除算されます。

## 力の作成

力は `mass * acceleration` から、または名前付きトークンで作成します。名前付き単位は値1のトークンとして残ります
(`of`/`into` で使用):

| 力 | 記号 | トークン | N 換算(1単位) |
|---|---|---:|---:|
| ニュートン | `N` | `newtons` | 1.0 |
| ダイン | `dyn` | `dynes` | 1.0e-5 |
| ポンド重 | `lbf` | `poundsForce` | 4.4482216152605 |
| ポンド(グラム重) | `p` | `ponds` | 9.80665e-3 |

**キロポンド / キログラム重(kgf)は専用トークンではありません** — キロニュートンが `kilo.newtons` であるのと
同様に `kilo.ponds` です。名前付き単位は `KPrefixBuilder` 経由で SI 接頭辞に対応します(`kilo.newtons`、
`mega.newtons`、`kilo.ponds` など)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f into newtons               // 10.0
f into poundsForce           // ≈ 2.248
(1 of kilo.ponds) into newtons // 9.80665(1 kp = 1 kgf)
```

## 基本単位(質量と加速度)による計算

| 式 | 結果の型 | 意味 |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | 力 = m · a(ニュートンの第2法則) |
| `acceleration * mass` | `KForceUnitInstance` | 力(可換) |
| `force / mass` | `KAccelerationUnitInstance` | 加速度 = F / m |
| `force / acceleration` | `KMassUnitInstance` | 質量 = F / a |
| `force / area` | `KPressureUnitInstance` | 圧力(圧力を参照) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*

val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
f into newtons               // ≈ 58.84
val a = (10 of newtons) / (2 of kilo.grams)          // KAccelerationUnitInstance, 5 m/s²
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance(グループから脱出)
```

## toString の書式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N"(基本単位)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```

## 記法

下の表は、この単位とその構成要素を数学的にどう書くか、KUnit を用いて Kotlin でどう書くかを対比します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）で表し、`·` は乗算、`/` は分数を表します。分数としても負の指数を用いた積としても書ける量については、同等な Kotlin の両形式を併記します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `N` | `newtons` | 力、基本単位（名前付きトークン、ニュートン） |
| `kg·m/s²` | `kilo.grams * meters / (seconds pow 2)` | 質量·長さ / 時間² としての力（分数形式） |
| `kg·m·s⁻²` | `kilo.grams * meters * (seconds pow -2)` | 同じ力を純粋な積で表現 |
| `kN` | `kilo.newtons` | 接頭辞付きの力（キロニュートン） |
