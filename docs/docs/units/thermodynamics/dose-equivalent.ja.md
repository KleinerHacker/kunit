# 等価線量（シーベルト）

パッケージ: `org.pcsoft.framework.kunit.thermo.specificenergy`
基本単位: **ジュール毎キログラム**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

種別: **構成単位（constructed unit）**

等価線量 `H` は、[吸収線量](absorbed-dose.ja.md)に**無次元**の放射線加重係数 `w_R` を掛けて重み付けした
ものです。これは、ある放射線の種類がどれだけ有害かを考慮したもので、`H = w_R · D` です。その単位は
**シーベルト**であり、`w_R` が無次元であるため、`1 Sv = 1 J/kg` — グレイと同じ次元です。

## シーベルトが独自の型を持たない理由

KUnit は、グレイや比エネルギーと同じ型である `KSpecificEnergyUnitInstance` で等価線量をモデル化して
います。理由はこのライブラリの形状認識契約にあります:

* 標準化された各グループには **唯一**の正準の基本次元正規形があり、
* `toX()` は正確にその形のみを認識します。

シーベルト、グレイ、比エネルギーはすべて正規形 `length² · time⁻²` を共有しています。1つの正規形に
対して複数の型があると、ネイティブ表現が曖昧になり、どちらの答えがより正しいということもありません。
1つの型にすることでラウンドトリップが決定的になります。

!!! warning "加重係数の適用はあなた自身の責任です"
    `w_R` は無次元であるため、KUnitはグレイとシーベルトを区別できません。吸収線量に加重係数を掛ける
    のは単なるスカラー乗算であり、ライブラリはそれを代わりに行うことも、2つの読み方を混同することを
    止めることもありません。値には適切な名前を付けてください。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## 実例 — フライトと年間バックグラウンド

自然放射線バックグラウンドは年間約 **2.4 mSv**であり、大西洋横断のフライトはおよそ0.05 mSvを
追加します:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## 関連項目

* [吸収線量](absorbed-dose.ja.md) — 加重されていないグレイ。
* [比エネルギー](specific-energy.ja.md) — 基礎となる型。
* [線量率](dose-rate.ja.md) — 時間あたりの線量。シーベルトの表記も含みます。
* [照射線量](exposure.ja.md) — 電荷に基づく電離線量。
