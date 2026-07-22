# 周波数

パッケージ: `org.pcsoft.framework.kunit.frequency`
基準単位: **ヘルツ** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

周波数グループは、単位時間あたりに何かが何回起こるかをモデル化します。これは **ネイティブな一次元** グループであり、
**時間の逆数** (`1 Hz = 1/s`) です。`KFrequencyUnitInstance` は単一の `KFrequencyUnit.HERTZ` 項をラップし、
常にヘルツに正規化して保持します。

周波数は時間の逆数なので、そのグループ間の挙動は **時間と完全に逆** に定義されます。周波数を掛けることは時間で割ることと同じように、
周波数で割ることは時間を掛けることと同じように振る舞います。

## 単位

| 単位 | 列挙値 | 記号 | トークン | 1 単位のヘルツ値 |
|---|---|---|---:|---:|
| ヘルツ | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| 毎秒回転数 | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| 毎秒フレーム数 | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| 毎分回転数 | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| 毎分拍数 | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

各 `トークン` は `of`（構築）と `into`（読み取り）で使う値 1 の `KFrequencyUnitInstance` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz（SI 接頭辞による kHz）
f.value                      // 2000.0（ヘルツに正規化）
(3000 of rpm) into hertz     // 50.0（3000 rpm = 50 Hz）
(50 of hertz) into rpm       // 3000.0
```

## 演算子

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - ：同一グループ、単位間の自動変換
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// 比較と等価性（正規化されたヘルツ値による）
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### 時間の逆数となるグループ間演算子

周波数と時間は互いに逆数なので、強く型付けされた結果に組み合わされます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance, 60 Hz（回数 / 時間 = 周波数）
val period = 1 / (2 of hertz)        // KTimeUnitInstance, 0.5 s（回数 / 周波数 = 時間）
val count = (50 of hertz) * (2 of seconds)   // 100.0（周波数 * 時間 = 無次元の回数）

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s（長さ * 周波数 = 速度）
(v / (5 of hertz)) into meters       // 2.0（速度 / 周波数 = 距離）
```

## 実世界の例：回転する車輪の周速度

円周 **2 m** の車輪が **毎秒 5 回転** しています。円周に回転周波数を掛けると周速度（接地速度）が得られます。
これは `length * frequency = speed` で、おなじみの `length / time = speed` の逆です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed.value                       // 10.0 m/s
```

## `pow` によるべき乗

infix 演算子 `pow` で整数のべき乗を計算します（Kotlin にはオーバーロード可能な `^` がありません）。周波数グループでは
`pow` は汎用の `KMixedUnitInstance` を返します（周波数には次元付きのべき乗型がありません）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## SI 接頭辞

周波数は **あらゆる** 桁を受け入れるので、すべての SI 接頭辞ビルダー（`quetta` … `quecto`）をプロパティアクセスで
あらゆる周波数単位に組み合わせられます。`kilo.hertz` は kHz、`mega.hertz` は MHz、`giga.hertz` は GHz です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0（MHz）
(2_400_000_000 of hertz) into giga.hertz // 2.4（GHz）
```

## toString 形式

基準単位の `toString()` のみが存在します。特定の単位で整形するには `into` を使います。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz"（基準単位表現）
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## 記法

下表は、この単位とその構成要素を数学的な記法と KUnit の Kotlin 記法で示します。指数は Unicode の上付き文字（`²`、`³`、`⁻¹`）を使い、`·` は乗算、`/` は分数を表します。分数と負の指数の積の両方で書ける場合は、両方の等価な Kotlin 形式を示します。

| 数学 | Kotlin | 意味 |
|---|---|---|
| `Hz` | `hertz` | 周波数、基準単位（ヘルツ） |
| `kHz` | `kilo.hertz` | キロヘルツ（ヘルツに接頭辞を適用） |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | 周期からの周波数（型付きヘルツ） |
| `Hz²` | `hertz pow 2` | ヘルツの 2 乗（汎用混合単位） |
