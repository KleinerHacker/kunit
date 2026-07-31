# 磁通密度

包: `org.pcsoft.framework.kunit.electric.magneticfluxdensity`
基本单位: **特斯拉**(`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

类型: **构造单位**

磁通密度 (磁感应强度 `B`)是一个 **构造**单位: 组合 `mass · time⁻² · current⁻¹`(`kg·s⁻²·A⁻¹`)。
`KMagneticFluxDensityUnitInstance` 包装了一个包含三个项的 `KMixedUnitInstance` — 指数 `+1` 的
`KMassUnit.BASE`(克)、指数 `-2` 的 `KTimeUnit.BASE`(秒)以及指数 `-1` 的
`KElectricCurrentUnit.BASE`(安培)。由于本库的质量分量归一化为 **克**(而非千克),特斯拉是原始分量基准的 1000 倍;
存储的值归一化为特斯拉。

## 构建磁通密度

可以用命名标记构建磁通密度,或者通过分解 (见下文)。命名单位保留为值为 1 的标记 (与 `of`/`into` 一起使用):

| 磁通密度      | 符号    |                   标记 | 1 单位对应 T |
|---------------|---------|-----------------------:|-------------:|
| 特斯拉        | `T`     |               `teslas` |          1.0 |
| 韦伯每平方米  | `Wb/m²` | `webersPerSquareMeter` |          1.0 |
| 高斯(CGS-EMU) | `G`     |                `gauss` |       1.0e-4 |
| 伽马          | `γ`     |               `gammas` |       1.0e-9 |

命名单位通过 `KPrefixBuilder` 支持 SI 词头 (`milli.teslas`、`micro.teslas`、`nano.teslas` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## 多种分解

磁通密度可以通过若干 **等价分解**得到,它们都产生数值相等的磁通密度:

| 表达式                 | 结果类型                           | 含义                             |
|------------------------|------------------------------------|----------------------------------|
| `flux / area`          | `KMagneticFluxDensityUnitInstance` | 定义 `B = Φ / A`                 |
| `mass/(time²·current)` | 通过 `.toMagneticFluxDensity()`    | 原生标准形式 `kg·s⁻²·A⁻¹` 表达式 |

带类型的运算符形式直接返回磁通密度。完全原生的表达式仍然是通用的 `KMixedUnitInstance`,需要用
`toMagneticFluxDensity()` 收窄 (它只识别标准形式,否则抛出 `IllegalStateException`)。两条路径数值相等。

逆运算符把磁通、磁通密度和面积联系起来:

| 表达式               | 结果类型                    | 含义                |
|----------------------|-----------------------------|---------------------|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A`         |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B`(可交换) |
| `flux / fluxDensity` | `KAreaUnitInstance`         | `A = Φ / B`         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

// 真实示例 - MRI 扫描仪: 穿过 6 m² 线圈的 18 Wb 磁通对应 3 T 的磁场。
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance,3 T

// 同一磁通密度的原生 kg·s⁻²·A⁻¹ 表达式:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// 50 µT 的地磁场穿过 2 m² 的线圈得到 1e-4 Wb 的磁通。
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance,1e-4 Wb
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance(脱离该组)
```

## toString 格式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T"(基本单位)
```

## 记法

下表展示该单位及其组成部分在数学上的写法与在 Kotlin 中使用 KUnit 的写法。指数使用 Unicode 上标 (`²`、`³`、`⁻¹`),`·` 表示乘法,
`/` 表示分数。若某个量既可写成分数也可写成带负指数的乘积,则同时列出两种等价的 Kotlin 形式。

| 数学         | Kotlin                                             | 含义                                        |
|--------------|----------------------------------------------------|---------------------------------------------|
| `T`          | `teslas`                                           | 磁通密度,基本单位(命名标记,特斯拉)          |
| `Wb/m²`      | `webersPerSquareMeter`                             | 作为单位面积磁通的磁通密度(命名标记)        |
| `kg/(s²·A)`  | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | 作为质量 / (时间²·电流)的磁通密度(分数形式) |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | 同一磁通密度的纯乘积形式                    |
| `µT`         | `micro.teslas`                                     | 带词头的磁通密度(微特斯拉)                  |
