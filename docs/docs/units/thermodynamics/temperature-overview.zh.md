# 温度 —— 概述

包: `org.pcsoft.framework.kunit.temperature`

温度由**两个相关的组**建模,因为温度读数与温度*变化*在物理上是不同种类的量。正确处理这一区别,正是让运算
保持正确的关键。

| 概念 | 类型 | 性质 | 基本单位 |
|---|---|---|---|
| [绝对温度](temperature.md) | `KTemperatureUnitInstance` | 仿射**点** | 开尔文(`K`) |
| [温度差](temperature-difference.md) | `KTemperatureDifferenceUnitInstance` | 线性**区间** | 开尔文(`ΔK`) |

## 点 vs 区间

**绝对温度**(`25 °C`、`300 K`)是温度标度上的仿射*点*:它从固定的零点测量,零点的选择(0 K 还是 0 °C)会改变
数值。**温度差**(`20 ΔK`)是*向量* —— 两点之间的间隔。它没有零点包袱: `20 ΔK` 的差无论位于标度何处都等于
`20 °C` 的差。

这正是仿射空间的点/向量之别,并决定了运算规则:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val a = 30 of celsius
val b = 10 of celsius

a - b                                    // KTemperatureDifferenceUnitInstance: 20 ΔK  (区间)
a + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 308.15 K         (仍是点)
// a + b                                 // 无法编译 —— 两个点相加无意义
```

| 运算 | 结果 |
|---|---|
| `AbsTemp − AbsTemp` | **温度差**(开尔文区间) |
| `AbsTemp + 差值` | 绝对温度 |
| `AbsTemp − 差值` | 绝对温度 |
| `差值 ± 差值` | 温度差 |
| `AbsTemp + AbsTemp` | **编译错误**(物理上无意义) |

## 为何在物理上重要

一个公式需要绝对温度还是差值,取决于温度**以何种方式**出现:

* **作为差值 / 变化** → 使用**温度差**。零点相互抵消,因此这里 `°C` 与 `K` 可以互换。典型案例 —— 热能:

  $$Q = m \cdot c \cdot \Delta T$$

  `30 °C − 10 °C = 20 ΔK`,且 `Q = m·c·20 ΔK`。热膨胀(`ΔL = α·L·ΔT`)、热传导,以及比热容 / 热导率单位
  (`J/(kg·ΔK)`、`W/(m·ΔK)`)同理。

* **以乘法方式**(T 单独出现、幂 `T⁴`、比值 `T₁/T₂`)→ 使用**绝对温度(开尔文)**,因为绝对零度是物理的一部分:
  理想气体定律 `pV = nRT`、斯特藩–玻尔兹曼辐射 `P = εσA·T⁴`、卡诺效率 `η = 1 − T_c/T_h`。

!!! warning "相同量纲,不同的量"
    两个组的量纲都是*开尔文*,因此在纯单位层面 `m·K` 与 `m·ΔK` 看起来相似。但在这里它们**不是**同一个单位:
    两者使用不同的单位组,因此包含绝对开尔文的混合单位与包含差值开尔文的混合单位既不相等也不可相加。区分的
    **`ΔK`** 符号(相对 `K`)正是为了在 `toString` 和混合单位输出中让这一点可见 —— 在构造自己的混合单位时请
    牢记这一点。

## 下一步

* **[绝对温度](temperature.md)** —— 单位(开尔文、摄氏、华氏、兰氏)、仿射 `of`/`into` 构建,以及非对称运算符。
* **[温度差](temperature-difference.md)** —— 线性组、显式 `KTemperatureDifference.ofKelvin(…)` 构建及其线性运算符。
