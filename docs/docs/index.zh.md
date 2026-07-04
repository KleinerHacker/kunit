# kunit

**kunit** 是一个用于在 Kotlin（也可在 Java 中使用）中以物理单位而非裸数字进行计算的框架。与其把米、英里或
平方米当作普通的 `Double` 值处理、并寄希望于每一处调用都使用相同的单位，kunit 会让值始终携带单位信息，并为你
处理换算、乘法以及量纲的记录。

## 为什么选择 kunit？

用裸数字处理物理量很容易出错：可能会在没有换算的情况下把米和英里相加，或者把面积和长度相加。kunit 通过把单位
变成类型的一部分来解决这个问题：

- **类型安全的运算。** 在不兼容的单位组或指数之间进行 `+`、`-` 运算会抛出 `IllegalStateException`，
  而不是悄悄地产生一个错误的数字。
- **自动换算。** `5.meters + 3.miles` 直接可用——两个操作数在内部都会被归一化，因此在组合之前无需手动
  换算单位。
- **自由的乘法与除法。** 单位之间的乘除 *始终* 被允许，并会自动跟踪结果的物理量纲（指数），例如
  `长度 * 长度` 得到面积。
- **完整支持 `Number` 类型。** 可以从 `Int`、`Long`、`Float`、`Double` 以及其他任意 `Number` 类型构造
  数值；内部始终归一化为 `Double`。
- **完整的 SI 前缀表**，从 Quetta（Q）到 Quecto（q），可以通用地与任意单位组合。
- **命名的特殊单位**（如公顷、升、英亩），作为方便的、绑定到特定组和指数的输入/输出目标，并不会取代底层的原始
  指数表示方式。

## 核心概念

kunit 围绕两个核心类型构建：

- **`KMixedUnitInstance`** —— *混合单位*（Mischeinheit）：一个 `Double` 基础值加上一个或多个 `KUnit`，每个都
  搭配一个整数指数（例如速度的 `m^1 * s^-1`）。这是驱动一切的通用引擎。
- **`KUnit`** —— 属于某个单位组的单一"纯"单位（例如米属于长度组）。具体的单位组以
  `enum class ... : KUnit`（例如 `KLengthUnit`）的形式建模。

每个单位组还额外提供一个 **包装类**（例如 `KLengthUnitInstance`），它封装了一个限定于单个单位组的
`KMixedUnitInstance`，并始终归一化到该组的基础单位。这是你在大多数情况下会使用的类型——目前提供的单位请参见
[预定义单位](units/length.md)，何时以及如何直接使用通用的 `KMixedUnitInstance` 引擎请参见
[混合单位](mixed-units.md)。

如果你想为新的物理量（例如质量或时间）添加支持，请参阅
[添加自定义单位](custom-units.md) 中的完整分步说明。

!!! note "单位对象是不可变的"
    每个单位值 —— 无论是 `KMixedUnitInstance` 引擎，还是诸如 `KLengthUnitInstance`、
    `KTimeUnitInstance` 之类的各种“纯”包装类 —— 都是**不可变的（immutable）**。任何操作都不会修改
    现有实例；运算符（`+`、`-`、`*`、`/`）和转换始终返回一个**新**对象，而保持操作数不变。因此单位值
    可以放心地自由共享，也可以用作键或常量。

## 快速开始

将该模块添加为依赖（或作为项目/源代码集引入），并导入所需单位组的词汇。

### 长度

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 从任意 Number 类型创建纯长度值
val distance = 5.meters
val trip = 10.miles

// 运算符：同一组、同一指数内自动换算
val total = distance + trip          // KLengthUnitInstance，归一化为米
val diff = trip - distance

// 比较
val isFarther = trip > distance      // true

// 以指定单位读取数值
println(total.valueAs(KUnitPrefix.KILO with meters)) // 例如 21.0467...
println(total.valueAs(yards))                         // 例如 23018.4...

// 纯单位相乘/相除会产生一个混合单位（KMixedUnitInstance）
val area = distance.toKMixedUnitInstance() * trip.toKMixedUnitInstance()

// 面积（指数 2）和体积（指数 3）的特殊单位
val plot = 3.hectares
println(plot.valueAs(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters
println(tank.valueAs(KLengthDerivedUnit.US_GALLON))
```

### SI 前缀

```kotlin
import org.pcsoft.framework.kunit.length.kilo
import org.pcsoft.framework.kunit.length.meters

// "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
println(fiveKm.value) // 5000.0（归一化为米）
```

### 混合单位

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

// 手动组合一个混合单位，例如平方米（长度^1 * 长度^1）
val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // 指数相加 -> 长度^2
```

## 检出与构建

```bash
git clone <repository-url>
cd kunit
```

kunit 使用 Gradle（仓库中已包含 wrapper，无需本地安装 Gradle）：

```bash
# 构建
./gradlew build          # Windows: gradlew.bat build

# 仅运行测试
./gradlew test            # Windows: gradlew.bat test
```

需要一个能够解析 toolchain 25 的 JDK（`foojay-resolver` 插件会在需要时自动下载）。
