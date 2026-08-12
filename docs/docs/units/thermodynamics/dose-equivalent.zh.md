# 剂量当量（希沃特）

包: `org.pcsoft.framework.kunit.thermo.specificenergy`
基本单位: **焦耳每千克**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

类型: **构造单位（constructed unit）**

剂量当量 `H` 是用一个**无量纲**的辐射加权因子 `w_R` 对[吸收剂量](absorbed-dose.zh.md)进行加权
的结果，该因子说明某种辐射类型的危害程度: `H = w_R · D`。其单位是**希沃特**，由于 `w_R` 是无
量纲的，因此 `1 Sv = 1 J/kg` —— 与戈瑞的量纲相同。

## 为什么希沃特没有自己的类型

KUnit 使用与戈瑞和比能相同的类型 `KSpecificEnergyUnitInstance` 来建模剂量当量。原因在于本库的
形式识别约定:

* 每个标准化分组都有**唯一**的规范基本量纲正规形式，且
* `toX()` 精确识别该形式。

希沃特、戈瑞和比能都共享正规形式 `length² · time⁻²`。一种正规形式对应多种类型会使原生表达式产生
歧义，且没有哪个答案更正确。单一类型保证了往返转换的确定性。

!!! warning "加权因子需要您自行应用"
    由于 `w_R` 是无量纲的，KUnit 无法区分戈瑞和希沃特。将吸收剂量乘以加权因子只是一次普通的标量
    乘法 —— 库不会替您完成这一操作，也不会阻止您混淆这两种读数。请相应地为您的变量命名。

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## 实际示例 — 一次飞行与一年的本底辐射

天然本底辐射约为每年 **2.4 mSv**；一次跨大西洋飞行大约增加 0.05 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## 另请参阅

* [吸收剂量](absorbed-dose.zh.md) —— 未加权的戈瑞。
* [比能](specific-energy.zh.md) —— 底层类型。
* [剂量率](dose-rate.zh.md) —— 单位时间的剂量，包含希沃特的拼写形式。
* [照射量](exposure.zh.md) —— 基于电荷的电离剂量。
