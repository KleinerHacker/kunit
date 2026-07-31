# 熵

包：`org.pcsoft.framework.kunit.thermo.heatcapacity`
基本单位： **焦耳每开尔文**（`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`）

类型： **构造单位**

熵 `S` 衡量系统中能量的分散程度。其单位是 `J/K` —— 与[热容](heat-capacity.md)**维度相同**。

## 为什么熵没有自己的类型

KUnit 有意用 `KHeatCapacityUnitInstance` 而非单独的 `KEntropyUnitInstance` 来建模熵。 原因在于本库的形式识别约定：

* 每个标准化组都有 **一个**规范的基础维度正规形式，并且
* `toX()` 只识别该形式。

熵与热容共享相同的正规形式 `mass¹ · distance² · time⁻² · temperature⁻¹`。若在同一正规
形式上有两个类型，原生表达式将会产生歧义 —— `toHeatCapacity()` 与假想中的
`toEntropy()` 会同时匹配同一个混合单位，而两者都不会更"正确"。单一类型使往返转换保持确定性。

因此，这两个量之间的区别在于 *你如何命名变量*，而非库返给你什么类型 —— 这与物理学的记法 完全一致，两者都写作 J/K。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// 两者都是 KHeatCapacityUnitInstance
```

## 现实示例：融化冰块

融化 1 kg 273.15 K 的冰会吸收 334 kJ 的潜热。熵变为 `ΔS = Q / T`。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // 作为从绝对零度起算的区间

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance，单位 J/K
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// 反过来：该熵变在熔点处携带多少热量？
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "`ΔS = Q / T` 中的绝对温度"
熵是除以一个 **绝对**温度得到的，但本库中的商运算使用的是温度 *差*组 （`KTemperatureDifferenceUnit`）——
一个仿射标度不能出现在分母中。请如上所示， 把绝对开尔文读数表示为从绝对零度起算的区间：
`KTemperatureDifference.ofKelvin(273.15)`。 在开尔文标度下，两者在数值上恰好一致，这正是热力学采用开尔文标度的原因。

## 另请参阅

* [热容](heat-capacity.md) —— 熵与之共享的类型，包含完整的单位表、所有分解方式 以及完整的操作符集合
* [摩尔热容](molar-heat-capacity.md) —— 每摩尔形式（摩尔熵）
* [比热容](specific-heat-capacity.md) —— 每千克形式（比熵）
* [能量](energy.md) —— `ΔS = Q / T` 中的分子

## 记法

下表展示了该量在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/`
表示分数。

| 数学表示        | Kotlin                                          | 含义                       |
|-----------------|-------------------------------------------------|----------------------------|
| `J/K`           | `joulesPerKelvin`                               | 熵，基本单位（与热容共享） |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | 相同的量以基础维度表示     |
| `ΔS = Q / T`    | `latentHeat / meltingPoint`                     | 由热量 ÷ 温度得到熵变      |
| `Q = ΔS · T`    | `entropyChange * meltingPoint`                  | 由熵变 × 温度得到热量      |
