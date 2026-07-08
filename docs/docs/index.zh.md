# KUnit

**KUnit** 是一个用物理单位而非裸数值进行计算的 Kotlin 框架(也可从 Java 使用)。与其将米、英里或平方米作为纯
`Double` 值追踪并寄望每个调用点都在单位上达成一致,`kunit` 将单位与值一同携带,并为你完成转换、乘法和量纲记账。

## 为什么选择 KUnit?

用原始数字处理物理量容易出错: 很容易在不转换的情况下意外地把米加到英里上,或者把面积加到长度上。kunit 通过让
单位成为类型的一部分来解决这个问题:

- **两个动词,`of` 和 `into`。** 用 `number of <单位>`(`5 of meters`)构建,用 `value into <单位>`
  (`v into kilo.meters`)读取。数字和单位严格分离。
- **类型安全的算术。** 不兼容的单位组或指数之间的 `+` 和 `-` 会抛出 `IllegalStateException`,而不是悄悄产生
  错误的数字。
- **自动转换。** `(5 of meters) + (3 of miles)` 直接可用 —— 两个操作数在内部被归一化,因此在组合它们之前
  永远不必手动转换单位。
- **自由的乘法和除法。** 单位的乘法或除法*总是*被允许,并自动追踪产生的物理量纲(指数),例如
  `length * length` 变成面积。
- **完整的 `Number` 支持。** 从 `Int`、`Long`、`Float`、`Double` 以及任何其他 `Number` 类型构建值;所有内容
  在内部归一化为 `Double`。
- **完整的 SI 前缀表**,从 Quetta(Q)到 Quecto(q),以前缀构建器(`kilo.meters`、`milli.seconds`)提供,
  并在编译时强制执行每单位的前缀策略。
- **命名特殊单位**(如公顷、升、英亩)作为与 `of`/`into` 一起使用的普通值为 1 的令牌。

## 核心概念

kunit 围绕两个核心类型构建:

- **`KMixedUnitInstance`** — *混合单位*("Mischeinheit"): 一个 `Double` 基础值加上一个或多个 `KUnit`(每个与
  一个整数指数配对,例如速度为 `m^1 * s^-1`)。这是驱动其他一切的通用引擎。
- **`KUnit`** — 属于某个单位组的单个"纯"单位(例如米属于长度组)。具体的单位组建模为
  `enum class ... : KUnit`(例如 `KDistanceUnit`)。

每个单位组还额外提供一个**包装器类**(例如 `KLengthUnitInstance`),它封装一个限制在单个单位组内的
`KMixedUnitInstance`,始终归一化为该组的基本单位。这是你大多数时候会使用的类型 —— 关于当前提供的单位见
[预定义单位](units/distance.md),关于何时以及如何直接降到通用 `KMixedUnitInstance` 引擎见[混合单位](mixed-units.md)。

如果你想为新的物理量(例如质量或时间)添加支持,请参见[添加自定义单位](custom-units.md)获取完整的分步指南。

!!! note "单位对象是不可变的"
    每个单位值 —— `KMixedUnitInstance` 引擎以及每个"纯"包装器如 `KLengthUnitInstance` 或
    `KTimeUnitInstance` —— 都是**不可变的**。任何操作都不会改变现有实例;运算符(`+`、`-`、`*`、`/`)和转换
    始终返回一个**新**对象,保持操作数不变。这使得单位值可以自由共享,也可以安全地用作键或常量。

## 快速开始

将模块添加为依赖(或作为项目/源集包含),并导入你需要的单位组的词汇。

### 长度

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// 从任何 Number 类型用 `of` 构建纯长度值
val distance = 5 of meters
val trip = 10 of miles

// 运算符: 同一组和指数内的自动转换
val total = distance + trip          // KLengthUnitInstance, 归一化为米
val diff = trip - distance

// 比较
val isFarther = trip > distance      // true

// 用 `into` 以特定单位读取值
println(total into kilo.meters)      // 例如 21.0467...
println(total into yards)            // 例如 23018.4...

// 相乘纯长度构建出强类型的面积
val area = distance * trip           // KAreaUnitInstance

// 面积(指数 2)和体积(指数 3)的命名特殊单位
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### SI 前缀

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance(== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0(归一化为米)
```

### 混合 / 复合单位

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds

// 从值为 1 的模板组合单位表达式,并用 `of` 缩放
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance, m·s⁻²
```

## 检出与构建

```bash
git clone <repository-url>
cd kunit
```

kunit 使用 Gradle(包装器已包含在仓库中,无需本地安装 Gradle):

```bash
# 构建
./gradlew build          # Windows: gradlew.bat build

# 仅运行测试
./gradlew test            # Windows: gradlew.bat test
```

需要一个能够解析工具链 25 的 JDK(如有需要,`foojay-resolver` 插件会自动下载)。
