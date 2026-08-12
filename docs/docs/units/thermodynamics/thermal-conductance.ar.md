# الموصلية الحرارية (Thermal Conductance)

الحزمة: `org.pcsoft.framework.kunit.thermo.conductance`
الوحدة الأساسية: **واط لكل كلفن** (`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`)

النوع: **وحدة مركّبة**

الموصلية الحرارية `G` لمكوّن ما تُبيّن كمّية الحرارة المتدفّقة عبره لكل وحدة فرق حرارة: `G = P / ΔT`، وتُقاس بـ
`W/K`. وهي مقلوب [المقاومة الحرارية المطلقة](thermal-resistance.md) بالضبط، وهي الصيغة الأنسب متى ما كانت مسارات
الحرارة **متوازية** — إذ تُجمع الموصليات المتوازية ببساطة.

الصيغة القياسية للأبعاد الأساسية هي `mass · length² · time⁻³ · temperature⁻¹`.

## الوحدات المسمّاة

| الوحدة                     | الرمز        |                       الرمز البرمجي | 1 وحدة بـ W/K |
|----------------------------|--------------|------------------------------------:|---------------:|
| واط لكل كلفن               | `W/K`        |             `wattsPerKelvin` |            1.0 |
| وحدة حرارية بريطانية لكل ساعة-°F | `Btu/(h*°F)` | `btusPerHourFahrenheit` |     ≈ 0.52753 |

جميع الرموز البرمجية تقبل نطاق بادئات النظام الدولي الكامل (`milli.wattsPerKelvin`، …).

## التفكيك

تملك هذه المجموعة تفكيكًا واحدًا، وكلا صيغتيه تُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة. الصيغة الأصلية
تُبنى من **قوالب الوحدات** لأنّ المجموعة تحمل حدّ كتلة.

| الصيغة                  | التعبير                                                          |
|-------------------------|---------------------------------------------------------------------|
| معامل مكتوب بنوع صريح   | `power / temperatureDifference`                                      |
| أصلية (`toX()`)         | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## الحساب باستخدام المجموعة

| التعبير                                        | نوع النتيجة                             | المعنى                  |
|--------------------------------------------------|-------------------------------------------|--------------------------|
| `power / temperatureDifference`                   | `KThermalConductanceUnitInstance`        | `G = P / ΔT`             |
| `thermalConductance * temperatureDifference`      | `KPowerUnitInstance`                     | `P = G · ΔT`             |
| `power / thermalConductance`                      | `KTemperatureDifferenceUnitInstance`     | فرق الحرارة المطلوب      |
| `thermalConductance + …`                          | `KThermalConductanceUnitInstance`        | مسارات حرارية متوازية    |
| `1 / thermalConductance`                          | `KThermalResistanceUnitInstance`         | `R = 1 / G`              |
| `1 / thermalResistance`                           | `KThermalConductanceUnitInstance`        | `G = 1 / R`              |

## مثال واقعي: مساران حراريان متوازيان

تفقد إحدى الوحدات الحرارة عبر لوحتها القاعدية (0.4 W/K) وعبر هيكلها (0.1 W/K). بما أنّهما متوازيان، تُجمع
الموصليتان، ومقلوب المجموع يعطي المقاومة الإجمالية:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // 15.0 W تُنقل عند ΔT = 30 K
```

## الدلالة القيمية

يقارن `equals`/`hashCode` **قيمة W/K المطبَّعة**، لذا
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`. تعرض `toString()` القيمة بالوحدة الأساسية:
`"0.4 W/K"`.

## انظر أيضًا

* [المقاومة الحرارية المطلقة](thermal-resistance.ar.md) — الكمّية المقلوبة.
* [العزل الحراري](thermal-insulance.ar.md) — صيغة المقاومة لكل وحدة مساحة.
* [معامل انتقال الحرارة](heat-transfer-coefficient.ar.md) — صيغة هذه الكمّية لكل وحدة مساحة.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
