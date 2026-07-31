# エントロピー

パッケージ: `org.pcsoft.framework.kunit.thermo.heatcapacity`
基本単位: **ジュール毎ケルビン** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

種別: **構成単位（constructed unit）**

エントロピー `S` はシステム内でのエネルギーの拡散を測定します。その単位は `J/K` —
[熱容量](heat-capacity.md)と **次元的に同一**です。

## エントロピーが独自の型を持たない理由

KUnit は意図的に、独立した `KEntropyUnitInstance` ではなく `KHeatCapacityUnitInstance` でエントロピーを
モデル化しています。理由はこのライブラリの形状認識契約にあります:

* 標準化された各グループには **唯一**の正準の基本次元正規形があり、
* `toX()` は正確にその形のみを認識します。

エントロピーと熱容量は正規形 `mass¹ · distance² · time⁻² · temperature⁻¹` を共有しています。1つの正規形に
対して2つの型があると、ネイティブ表現が曖昧になってしまいます — `toHeatCapacity()` と仮の
`toEntropy()` はどちらも同じ混合単位に一致し、どちらの答えがより正しいということもありません。
1つの型にすることでラウンドトリップが決定的になります。

したがって、この2つの量の違いは、ライブラリが渡す型の違いではなく、 *変数にどんな名前を付けるか*の問題に すぎません —
これはまさに物理表記の場合と同じで、どちらも J/K と書かれます。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// どちらも KHeatCapacityUnitInstance
```

## 実例 — 氷を融かす

1 kgの氷を273.15 Kで融かすには334 kJの潜熱が必要です。エントロピー変化は `ΔS = Q / T` です。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // 絶対零度からの区間として

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance、J/K単位
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// 逆方向: そのエントロピー変化が融点でどれだけの熱を運ぶか?
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "`ΔS = Q / T` における絶対温度"
エントロピーは **絶対**温度で割られますが、このライブラリの商演算は温度 **差**グループ (`KTemperatureDifferenceUnit`)
を使います — アフィンな尺度は分母に現れることができません。
上記のように絶対ケルビンの読み値を絶対零度からの区間として表現してください:
`KTemperatureDifference.ofKelvin(273.15)`。ケルビン尺度ではこの2つが数値的に一致します。 まさにこれが、熱力学がケルビン尺度を使う理由です。

## 関連項目

* [熱容量](heat-capacity.md) — エントロピーが共有する型で、完全な単位表、すべての分解表現、 演算子全体を記載
* [モル熱容量](molar-heat-capacity.md) — モルあたりの形式 (モルエントロピー)
* [比熱容量](specific-heat-capacity.md) — キログラムあたりの形式 (比エントロピー)
* [エネルギー](energy.md) — `ΔS = Q / T` の分子

## 記法

以下の表は、この量が数学的にどう書かれ、KUnitを使ったKotlinでどう書かれるかを示します。指数はUnicodeの上付き文字（`²`、`³`、
`⁻¹`）を使用し、`·` は乗算、`/` は分数を表します。

| 数学            | Kotlin                                          | 意味                                 |
|-----------------|-------------------------------------------------|--------------------------------------|
| `J/K`           | `joulesPerKelvin`                               | エントロピー、基本単位(熱容量と共有) |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 同じ量を基本次元で                   |
| `ΔS = Q / T`    | `latentHeat / meltingPoint`                     | 熱÷温度からエントロピー変化          |
| `Q = ΔS · T`    | `entropyChange * meltingPoint`                  | エントロピー変化×温度から熱          |
