# كثافة التدفّق الحراري

الحزمة: `org.pcsoft.framework.kunit.thermo.heatfluxdensity`
الوحدة الأساسية: **واط لكل متر مربع** (`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`)

النوع: **وحدة مركّبة**

كثافة التدفّق الحراري هي تدفّق الحرارة لكل وحدة مساحة: `power / area` (`W/m²`). وتقيس نفس الوحدة *الإشعاع الوارد* و
*الإصدار الإشعاعي* — شدّة الإشعاع الساقط على سطح أو الصادر عنه.

يغلّف `KHeatFluxDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية
`mass¹ · time⁻³` (`kg·s⁻³`)، مطبَّعًا دائمًا إلى W/m².

!!! note "بُعد المسافة يُلغى"
`W/m² = kg·m²·s⁻³/m² = kg·s⁻³`. لذا لا تحمل الصيغة القياسية **أي** حدّ مسافة.

تدفّق الحرارة الإجمالي نفسه هو ببساطة [قدرة](power.md)؛ انظر
[تدفّق الحرارة](heat-flow.md). وعند القسمة على فرق درجة حرارة تصبح
[معامل انتقال الحرارة](heat-transfer-coefficient.md).

## الوحدات المسمّاة

| الوحدة                                 | الرمز         |                       الرمز البرمجي | 1 وحدة بـ W/m² |
|----------------------------------------|---------------|------------------------------------:|---------------:|
| واط لكل متر مربع                       | `W/m²`        |               `wattsPerSquareMeter` |            1.0 |
| وحدة حرارية بريطانية لكل ساعة-قدم مربع | `Btu/(h·ft²)` |             `btusPerHourSquareFoot` |      ≈ 3.15459 |
| سعرة لكل ثانية-سم مربع                 | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` |        41840.0 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`kilo.wattsPerSquareMeter`،
`milli.wattsPerSquareMeter`، …).

## الثابت الشمسي

تعرض المجموعة متوسّط الإشعاع الشمسي فوق الغلاف الجوي كـ`SOLAR_CONSTANT` (1361 W/m²)، وهو `Double`
عادي.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## مثال واقعي: تصميم مصفوفة شمسية

يستقبل سطح المنزل 800 W/m² في يوم صافٍ. تغطّي المصفوفة 25 m² وتحوّل 20 % من الإشعاع الساقط. ما القدرة الكهربائية التي
تسلّمها؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // scalar scaling keeps the type
electrical into kilo.watts                 // 4.0 kW

// Inverse: how much roof area for 10 kW electrical at 20 % efficiency?
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## الحساب باستخدام الوحدات الأساسية (القدرة والمساحة)

| التعبير                   | نوع النتيجة                    | المعنى                       |
|---------------------------|--------------------------------|------------------------------|
| `power / area`            | `KHeatFluxDensityUnitInstance` | كثافة التدفّق الحراري         |
| `heatFluxDensity * area`  | `KPowerUnitInstance`           | إجمالي تدفّق الحرارة          |
| `area * heatFluxDensity`  | `KPowerUnitInstance`           | إجمالي تدفّق الحرارة (تبادلي) |
| `power / heatFluxDensity` | `KAreaUnitInstance`            | المساحة التي ينتشر عليها     |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك         | الصيغة                             | النتيجة                               |
|-----------------|------------------------------------|---------------------------------------|
| `power / area`  | معامل مكتوب بنوع صريح              | `KHeatFluxDensityUnitInstance` مباشرة |
| `mass · time⁻³` | تعبير أصلي + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true - both are 1.0 W/m²
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                                  | المعنى                                           |
|-------------|-----------------------------------------|--------------------------------------------------|
| `W/m²`      | `wattsPerSquareMeter`                   | كثافة التدفّق الحراري، الوحدة الأساسية — رمز مسمّى |
| `kg·s⁻³`    | `grams / (seconds pow 3)`               | نفس الكمّية بالأبعاد الأساسية                     |
| `kW/m²`     | `kilo.wattsPerSquareMeter`              | كيلوواط لكل متر مربع                             |
| `E_0`       | `SOLAR_CONSTANT of wattsPerSquareMeter` | الثابت الشمسي، 1361 W/m²                         |
| `q̇ = P / A` | `(1000 of watts) / roof`                | كثافة التدفّق من القدرة ÷ المساحة                 |
| `P = q̇ · A` | `irradiance * roof`                     | القدرة من كثافة التدفّق × المساحة                 |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance`       | المساحة من القدرة ÷ كثافة التدفّق                 |
