# 光エネルギー

パッケージ: `org.pcsoft.framework.kunit.optic.luminousenergy`
基本単位: **ルーメン秒**（`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`）

種別: **構成単位**

光エネルギー `Q` は **時間積算された** 光束です: `Q = Φ · t`。光束が*今この瞬間の*ランプの明るさを表すのに対し、
光エネルギーはランプが合計でどれだけの光を届けたかを表します — ランプの寿命定格や写真撮影用フラッシュのエネルギーの
背後にある量です。ルーメン秒は **タルボット** とも呼ばれます。

その正準基本次元標準形は `luminousIntensity¹ · solidAngle¹ · time¹` です。

## 単位

| 単位         | Enum値                          | 記号 |          トークン | 1単位（lm·s） |
|--------------|-------------------------------------|--------|---------------:|---------------:|
| ルーメン秒 | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |            1.0 |
| タルボット       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |            1.0 |
| ルーメン時   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |           3600 |

`talbots` は基本単位の別表記であり、独自の単位ではありません。すべてのトークンはあらゆるSI接頭辞を受け付けます
（`kilo.lumenHours`、`milli.lumenSeconds` など）。

## 分解

このグループには1つの分解があり、両方の形式が同じ型付きで値の等しいインスタンスを生成します。

| 形式             | 式                                                                  |
|------------------|-------------------------------------------------------------------------|
| 型付き演算子   | `luminousFlux * time`                                                       |
| ネイティブ形式（`toX()`） | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## グループでの計算

| 式                       | 結果型                   | 意味                       |
|-----------------------------------|--------------------------------|--------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | 平均光束              |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | 光束が放出された時間 |

## 実例 — ランプの寿命全体で放出される光量

800 lmのLED電球の定格寿命は **25,000時間** です。それが一生涯に届ける光の総量は次の通りです。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// 1日3時間使用する場合、何日分をカバーできるか？
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333日
```

## 値のセマンティクス

`equals`/`hashCode` は **正規化されたlm·s値** を比較するため、`(1 of lumenHours) == (3600 of lumenSeconds)` となります。
`toString()` は値を基本単位で表示します: `"3600.0 lm*s"`。

## 関連項目

* [光束](luminous-flux.ja.md) — この量が積算される元となる速度。
* [光露光量](luminous-exposure.ja.md) — 光束ではなく照度に対する同様の考え方。
* [光学の概要](overview.ja.md)
</content>
