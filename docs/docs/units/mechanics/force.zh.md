# 力

包: `org.pcsoft.framework.kunit.force`
基本单位: **牛顿**(`KForceUnit.BASE == KForceUnit.NEWTON`)

类型：**构造单位**

力是一个**构造**单位,即组合 `mass · length · time⁻²`(`kg·m/s²`)。`KForceUnitInstance` 包装三项——指数为
`+1` 的 `KMassUnit.BASE`(克)、指数为 `+1` 的 `KDistanceUnit.BASE`(米)和指数为 `-2` 的 `KTimeUnit.BASE`
(秒)。由于本库的质量分量归一化为**克**(而非千克),牛顿是原始分量基准的 1000 倍;存储值为原始分量值,以牛顿
读取时除以该固定因子。

## 构建力

由 `mass * acceleration` 构建力,或使用命名令牌。命名单位作为值为 1 的令牌保留(与 `of`/`into` 一起使用):

| 力 | 符号 | 令牌 | 1 单位换算为 N |
|---|---|---:|---:|
| 牛顿 | `N` | `newtons` | 1.0 |
| 达因 | `dyn` | `dynes` | 1.0e-5 |
| 磅力 | `lbf` | `poundsForce` | 4.4482216152605 |
| 克力(pond) | `p` | `ponds` | 9.80665e-3 |

**千克力 / 千磅力(kp = kgf)不是专用令牌**——它就是 `kilo.ponds`,正如千牛顿是 `kilo.newtons`。命名单位通过
`KPrefixBuilder` 支持 SI 前缀(`kilo.newtons`、`mega.newtons`、`kilo.ponds` 等)。

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

## 使用核心单位(质量与加速度)进行计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | 力 = m · a(牛顿第二定律) |
| `acceleration * mass` | `KForceUnitInstance` | 力(可交换) |
| `force / mass` | `KAccelerationUnitInstance` | 加速度 = F / m |
| `force / acceleration` | `KMassUnitInstance` | 质量 = F / a |
| `force / area` | `KPressureUnitInstance` | 压力(见压力) |

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

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance(逃逸出组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N"(基本单位)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `N` | `newtons` | 力，基本单位（命名标记，牛顿） |
| `kg·m/s²` | `kilo.grams * meters / (seconds pow 2)` | 作为 质量·长度 / 时间² 的力（分数形式） |
| `kg·m·s⁻²` | `kilo.grams * meters * (seconds pow -2)` | 同一力写成纯乘积 |
| `kN` | `kilo.newtons` | 带前缀的力（千牛顿） |
