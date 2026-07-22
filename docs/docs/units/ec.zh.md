# 电流

包: `org.pcsoft.framework.kunit.ec`
基准单位: **安培** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

电流组用于建模电流。它是一个 **简单的一维** 原生组（没有指数特化的子类型，也没有跨单位的类型化结果）:
`KElectricCurrentUnitInstance` 包装单个 `KElectricCurrentUnit.AMPERE` 项，始终归一化为安培存储。

除了 SI 安培之外，该组还提供两个经典的 CGS 电流单位: 电磁单位制（EMU）的 **毕奥**（abampere，`1 Bi = 10 A`）
和静电单位制（ESU）的 **静安培**（`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`）。

## 单位

| 系统 | 单位 | 枚举值 | 符号 | 令牌 | 1 单位对应安培值 |
|---|---|---|---|---:|---:|
| SI | 安培 | `KElectricCurrentUnit.AMPERE` | `A` | `amperes` | 1.0 |
| CGS | 毕奥 / abampere | `KElectricCurrentUnit.BIOT` | `Bi`（`abA`） | `biot` / `abamperes` | 10 |
| CGS | 静安培 | `KElectricCurrentUnit.STATAMPERE` | `statA` | `statamperes` | 3.335641e-10 |

每个 `令牌` 都是值为 1 的 `KElectricCurrentUnitInstance`，配合 `of`（构建）和 `into`（读取）使用。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002（归一化为安培）
i into amperes                // 0.002（以安培读回）
(1 of biot) into amperes      // 10.0
```

## 实际示例

欧姆定律: `R = 220 Ω` 的电阻两端施加 `U = 5 V`，流过的电流为 `I = U / R`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val voltage = 5.0       // V
val resistance = 220.0  // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.ec.*

// + / - : 同组，单位间自动换算
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// 比较
(1 of biot) == (10 of amperes)         // true（归一化数量相同）
(1 of biot) > (1 of amperes)           // true
```

### 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较两个 `KElectricCurrentUnitInstance` 的归一化 `value`（安培）。
`equals` 按归一化数量比较，因此 `(1 of biot) == (10 of amperes)`。

## 使用 `pow` 求幂

用中缀运算符 `pow` 计算整数次幂（Kotlin 没有可重载的 `^`）。对于电流组，`pow` 返回通用的
`KMixedUnitInstance`（电流没有带量纲的幂类型）:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.ec.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## SI 前缀

电流接受 **任意** 数量级，因此每个 SI 前缀构建器（`quetta` … `quecto`）都可通过属性访问与每个电流单位组合。
毫安是 `milli.amperes`，千安是 `kilo.amperes`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of milli.amperes).value   // 0.001      （毫安）
(1 of kilo.amperes).value    // 1000.0     （千安）

(2500 of amperes) into kilo.amperes  // 2.5
```

## toString 格式

只存在基准单位的 `toString()`；要以特定单位格式化，请使用 `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of biot).toString()                       // "10.0 A"（基准单位表示）
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## 记法

下表显示该单位及其组成部分在数学表示与 KUnit 的 Kotlin 表示中的写法。指数使用 Unicode 上标
（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `A` | `amperes` | 电流，基准单位（安培） |
| `mA` | `milli.amperes` | 毫安（前缀应用于安培） |
| `kA` | `kilo.amperes` | 千安 |
| `Bi` | `biot` | 毕奥 / abampere（10 A） |
| `A²` | `amperes pow 2` | 安培平方（通用混合单位） |
