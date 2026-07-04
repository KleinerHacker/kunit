<p align="center">
  <img src="docs/docs/assets/images/logo.png" alt="KUnit logo" width="320">
</p>

# kunit

> 🌐 [English](README.md) · [한국어](README.ko.md) · **中文** · [日本語](README.ja.md)
>
> 完整文档同样在 [GitHub Pages](https://kleinerhacker.github.io/kunit/) 上提供四种语言版本
> ([EN](https://kleinerhacker.github.io/kunit/) ·
> [KO](https://kleinerhacker.github.io/kunit/ko/) ·
> [ZH](https://kleinerhacker.github.io/kunit/zh/) ·
> [JA](https://kleinerhacker.github.io/kunit/ja/))。

Kotlin（及 Java）单位计算框架 —— 使用真实物理单位以 `Double` 精度进行计算，而不是使用裸数字。

## 检出与构建

```bash
git clone <repository-url>
cd kunit
```

本项目使用 Gradle（仓库已包含 wrapper，无需本地安装 Gradle）：

```bash
# 构建
./gradlew build          # Windows: gradlew.bat build

# 仅运行测试
./gradlew test            # Windows: gradlew.bat test
```

需要能够解析 toolchain 25 的 JDK（`foojay-resolver` 插件会在需要时自动下载）。

## 文档站点

📖 **[在 GitHub Pages 上阅读文档](https://kleinerhacker.github.io/kunit/)**

完整文档（概述、快速上手、混合单位、添加自定义单位、预定义单位）使用
[MkDocs Material](https://squidfunk.github.io/mkdocs-material/) 构建，并通过
[mkdocs-static-i18n](https://github.com/ultrabug/mkdocs-static-i18n) 提供英文、韩文、
中文和日文版本，支持浅色/深色模式切换。

```bash
pip install -r docs/requirements.txt

# 本地实时预览
mkdocs serve

# 构建静态站点到 ./site
mkdocs build
```

## 架构

* **`KMixedUnitInstance`** —— 表示一个*混合单位*：一个归一化的 `Double` 基准值，加上一组 `KUnit`，
  每个都与一个指数（正 = 分子，负 = 分母）结合，被视为彼此相乘。
* **`KUnit`** —— 单个“纯”单位的接口（符号 + 到所属组基准单位的换算系数）。
  每个单位组以 `enum class ... : KUnit`（例如 `KLengthUnit`）实现。
* **包装类**（例如 `KLengthUnitInstance`）—— 为具体单位组通过委托封装一个 `KMixedUnitInstance`，
  并始终将其值归一化到该组的基准单位。它不局限于指数 1，也涵盖同组的导出量
  （例如 面积 = 长度²，体积 = 长度³）。
* **`KUnitPrefix`** —— 根包中的泛型枚举，包含完整的 SI 词头表（Quetta/Q 到 Quecto/q）。
  词头不是 `KUnit` 本身的一部分，仅在读写数值时才有意义，通过每组各自的 `infix` 函数
  （例如 `5 kilo meters`）组合，直接返回该组的具体单位（`5 kilo meters` 即 `KLengthUnitInstance`，
  等价于 `5000.meters`）。
* **特殊单位**（`KDerivedUnit` / `KScaledDerivedUnit`）—— 具有自身名称/符号的、绑定到组与指数的
  附加换算目标（例如 面积的公顷、体积的升），是对基本机制的补充而非替代。

```mermaid
classDiagram
    class KUnit {
        <<interface>>
        +symbol: String
        +baseValue: Double
    }
    class KMixedUnitInstance {
        +value: Double
        +units: List~KUnitTerm~
        +valueAs(...)
        +toString()
        +plus() minus() times() div()
    }
    class KUnitTerm {
        +unit: KUnit
        +exponent: Int
    }
    class KUnitPrefix {
        <<enum>>
        Quetta ... Quecto
    }
    class KDerivedUnit {
        <<interface>>
        +referenceUnit: KUnit
        +exponent: Int
        +baseValue: Double
    }

    KMixedUnitInstance "1" o-- "many" KUnitTerm
    KUnitTerm --> KUnit
    KDerivedUnit --> KUnit : referenceUnit

    class KLengthUnit {
        <<enum>>
        METER, MILE, YARD, ...
    }
    class KLengthUnitInstance {
        +value: Double
        +valueAs(unit)
        +plus() minus() times() div()
    }
    class KLengthDerivedUnit {
        <<enum>>
        HECTARE, ARE, ACRE, LITER, ...
    }

    KUnit <|.. KLengthUnit
    KDerivedUnit <|.. KLengthDerivedUnit
    KLengthUnitInstance *-- KMixedUnitInstance : delegates to
    KLengthDerivedUnit --> KLengthUnit : referenceUnit
```

### 包结构

* 根包 `org.pcsoft.framework.kunit` 包含基础类型 `KUnit`、`KMixedUnitInstance`、`KUnitPrefix`、
  `KDerivedUnit`……每个单位子包还各自声明自己的词头 `infix` 函数（例如 `KLengthUnitPrefix.kt`）。
* 每个“纯”单位组都有自己的子包（例如 `org.pcsoft.framework.kunit.length`），其中包含各自的
  `KXxxUnit`、`KXxxUnitInstance`、`KXxxDerivedUnit` 以及相关的创建扩展函数。

### 运算符

* `+`、`-`、`*`、`/` 支持纯单位、混合单位以及两者的混合。
* `==`、`!=`、`<`、`<=`、`>`、`>=` 支持纯单位；混合单位另外提供一个用于纯单位/指数检查的方法
  （`hasSameUnits`）。
* `+`/`-` 仅在同一单位组且指数相同（纯单位），或 `KUnit`（含指数）完全相同（混合单位）时才允许 ——
  否则会抛出 `IllegalStateException`。

## 该框架目前支持什么

当前实现状态（详见 [STATUS.md](STATUS.md)）：

### 根引擎

* 具备完整运算符和 `toString` 换算的 `KMixedUnitInstance`/`KUnitTerm` 混合单位引擎
* 通过 `KUnitPrefix` 提供的完整 SI 词头表（24 个值，Quetta/Q 到 Quecto/q）
* 每组各自的词头构造，直接返回具体单位（`5 kilo meters`）
* 用于特殊/导出单位的泛型机制（`KScaledUnit`、`KDerivedUnit`、`KScaledDerivedUnit`）

### 单位组

| 组 | 子包 | 基准单位 |
|---|---|---|
| 长度 | `org.pcsoft.framework.kunit.length` | 米 (`KLengthUnit.BASE`) |

#### 长度 (`KLengthUnit`)

米、英里、海里、码、英尺、英寸、英寻、测链、弗隆、天文单位、光年、秒差距。

#### 多维支持（指数 > 1）

`KLengthUnitInstance` 封装 `KLengthUnit.BASE` 的任意指数，包括：

* **指数 2（面积）** —— 含特殊单位（`KLengthDerivedUnit`）：公亩、公顷、英亩
* **指数 3（体积）** —— 含特殊单位（`KLengthDerivedUnit`）：升、美制加仑、英制加仑、
  美制液盎司、油桶

### 尚未完成

* 遵循 `length` 模式的更多单位组（例如 质量、时间、温度）
* 本身由混合单位构成的复合“纯”单位（例如 牛顿）

## 快速上手

将所需的单位组添加为依赖（或作为项目/源集包含），并 import 你需要的单位组词汇。

### 长度

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 从任意 Number 类型创建纯长度值
val distance = 5.meters
val trip = 10.miles

// 运算符：在同一组和指数内自动换算
val total = distance + trip          // KLengthUnitInstance，归一化为米
val diff = trip - distance

// 比较
val isFarther = trip > distance      // true

// 以特定单位读取数值
println(total.valueAs(KUnitPrefix.KILO with meters)) // 例如 21.0467...
println(total.valueAs(yards))         // 例如 23018.4...

// 纯单位相乘/相除会构建混合单位（KMixedUnitInstance）
val area = distance.toKMixedUnitInstance() * trip.toKMixedUnitInstance()

// 面积（指数 2）和体积（指数 3）的特殊单位
val plot = 3.hectares
println(plot.valueAs(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters
println(tank.valueAs(KLengthDerivedUnit.US_GALLON))
```

### SI 词头

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

// 手动组合一个混合单位，例如 米每秒（当存在时间组时为 length^1 * time^-1）
val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // 指数相加 -> length^2
```
