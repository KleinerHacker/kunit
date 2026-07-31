# 元素周期表

包：`org.pcsoft.framework.kunit`
类型：`KChemicalElement`、`KChemicalElementCategory`

`KChemicalElement` 是化学元素的中心存放位置。它是一个纯粹的 Kotlin 枚举，因此每个元素都是编译期常量—— 它所携带的每一个物理常量都是本库中的
**类型化单位实例**，可以随时与其他一切内容进行组合运算。

## 覆盖范围

该枚举覆盖了经典的中学元素周期表： **第 1-6 周期、不含 f 区**的主族与副族元素。因此镧系元素
（57-71）不在其中——原子序数从钡（56）直接跳到铪（72）——锕系元素及超锕系元素也未包含在内。 这样共有 71 个条目。

## 位置数据

| 属性            | 类型                       | 含义                                         |
|-----------------|----------------------------|----------------------------------------------|
| `ordinalNumber` | `Int`                      | 原子序数 Z，即在元素周期表中的索引           |
| `symbol`        | `String`                   | 元素符号，例如 `"Pb"`                        |
| `fullName`      | `String`                   | 英文名称，例如 `"Lead"`（枚举条目为 `LEAD`） |
| `period`        | `Int`                      | 周期（行），1-6                              |
| `mainGroup`     | `Int?`                     | s/p 区元素的主族 1-8，过渡金属为 `null`      |
| `subGroup`      | `Int?`                     | d 区元素的副族 1-8，其他情况为 `null`        |
| `category`      | `KChemicalElementCategory` | 化学族                                       |

`mainGroup` 和 `subGroup` 中恰好有一个被赋值。副族采用经典编号方式 （Cu = 1，Zn = 2，Sc = 3 … Fe/Co/Ni = 8）。

`KChemicalElementCategory` 包含条目 `HYDROGEN`、`ALKALI_METAL`、`ALKALINE_EARTH_METAL`、
`TRANSITION_METAL`、`POST_TRANSITION_METAL`、`METALLOID`、`NONMETAL`、`HALOGEN` 以及 `NOBLE_GAS`。

## 单位数据

| 属性                    | 类型                                 | 可用性标志                 |
|-------------------------|--------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`             | 始终存在                   |
| `molarVolume`           | `KMolarVolumeUnitInstance?`          | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`               | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`               | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`              | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`          | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`          | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?` | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`  | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`               | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`          | `hasElectricalResistivity` |
| `electronegativity`     | `Double?`（泡林标度，无量纲）        | `hasElectronegativity`     |

对于某个元素而言没有实际意义的常量为 `null`——氦在常压下没有熔点，砷是升华而非沸腾， 砹极为稀有以至于没有测得密度。对应的
`has...` 属性无需处理 null 即可回答同样的问题。

`molarVolume` 是由 `molarMass / density` 推导而来的，即它使用了
[摩尔体积](units/thermodynamics/molar-volume.md)单位组的第二种分解方式。

## 现实示例：一块金条有多重？

一块标准金条的尺寸为 7 cm × 4 cm × 2 cm。它有多重，这又相当于多少摩尔黄金？

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## 现实示例：加热一口铜锅

将一口 1.2 kg 的铜锅从 20 °C 加热到 200 °C 需要多少能量？

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## 查找

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON（不区分大小写）
KChemicalElement.ofFullName("iron")    // IRON（不区分大小写）
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null —— 镧系元素不属于此表
KChemicalElement.ofMainGroup(4, 6)     // LEAD（主族 4，周期 6）
KChemicalElement.ofSubGroup(8, 4)      // IRON（副族 8，周期 4 —— Fe/Co/Ni 中的第一个）
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

副族 8 每个周期包含三个元素；`ofSubGroup` 返回第一个（Fe、Ru、Os）——若要获取全部， 请使用 `ofPeriod` 并进行过滤。

## 记法

| 数学表示      | Kotlin                                         | 含义                          |
|---------------|------------------------------------------------|-------------------------------|
| `Z`           | `element.ordinalNumber`                        | 原子序数                      |
| `M`           | `element.molarMass`                            | 摩尔质量，`g/mol`             |
| `V_m = M / ρ` | `element.molarVolume`                          | 摩尔体积，`m³/mol`            |
| `ρ`           | `element.density`                              | 密度                          |
| `T_m`、`T_b`  | `element.meltingPoint`、`element.boilingPoint` | 熔点 / 沸点，以 K 表示        |
| `m = ρ · V`   | `gold.density!! * volume`                      | 由密度 × 体积得到质量         |
| `n = m / M`   | `mass / gold.molarMass`                        | 由质量 ÷ 摩尔质量得到物质的量 |
