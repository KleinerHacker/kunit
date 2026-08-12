# السعة الحرارية الحجمية

الحزمة: `org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
الوحدة الأساسية: **جول لكل متر مكعب-كلفن**
(`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`)

النوع: **وحدة مركّبة**

السعة الحرارية الحجمية `c_v` هي مقدار الحرارة التي يخزّنها **حجم** من مادّة لكل كلفن:
`c_v = C / V = c · ρ`. وهي الكمّية التي تحدّد مقدار الكتلة الحرارية الفعلية لمبنى أو خزّان تخزين أو مُبدِّد
حراري — فمادّتان لهما نفس السعة الحرارية النوعية تخزّنان كميّات مختلفة جدًا من الحرارة إذا اختلفت
كثافتهما.

الصيغة القياسية بالأبعاد الأساسية لها هي `mass · length⁻¹ · time⁻² · temperature⁻¹`.

## الوحدات المسمّاة

| الوحدة                                  | الرمز          |                              الرمز البرمجي | 1 وحدة بـ J/(m³·K) |
|------------------------------------------|----------------|-----------------------------------:|-------------------:|
| جول لكل متر مكعب-كلفن                    | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |                1.0 |
| سعرة لكل سنتيمتر مكعب-كلفن               | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |            4.184e6 |

القيم كبيرة، لذا فإن الصيغة بالميجاجول هي الأكثر عملية: الماء يبلغ تقريبًا 4.18 MJ/(m³·K). جميع الرموز
البرمجية تقبل كل بادئات النظام الدولي (`mega.joulesPerCubicMeterKelvin`، …).

## التفكيكات

تحتوي هذه المجموعة على تفكيكَين **اثنين**. كلاهما يصبّان في نفس المصنع التسووي، لذا يُنتجان
نفس النسخة المحكومة بالنوع والمتساوية القيمة:

| الصيغة                    | التعبير                                                             |
|---------------------------|------------------------------------------------------------------------|
| معامل مكتوب بنوع صريح A   | `heatCapacity / volume`                                          |
| معامل مكتوب بنوع صريح B   | `specificHeatCapacity * density`                                 |
| أصلي (`toX()`)            | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## الحساب باستخدام المجموعة

| التعبير                                            | نوع النتيجة                                | المعنى                  |
|------------------------------------------------------|--------------------------------------------|--------------------------|
| `heatCapacity / volume`                              | `KVolumetricHeatCapacityUnitInstance`       | `c_v = C / V`           |
| `specificHeatCapacity * density`                     | `KVolumetricHeatCapacityUnitInstance`       | `c_v = c · ρ`           |
| `volumetricHeatCapacity * volume`                    | `KHeatCapacityUnitInstance`                 | `C = c_v · V`           |
| `heatCapacity / volumetricHeatCapacity`              | `KVolumeUnitInstance`                       | الحجم الموافق           |
| `volumetricHeatCapacity / density`                   | `KSpecificHeatCapacityUnitInstance`         | العودة إلى `c`          |
| `volumetricHeatCapacity / specificHeatCapacity`      | `KDensityUnitInstance`                      | العودة إلى `ρ`          |

## مثال واقعي — الكتلة الحرارية لخزّان ماء تخزيني

خزّان ماء تخزيني سعته **300 لتر**: كم من الطاقة يلزم لرفع درجة حرارته 1 K، وكيف تُقارن هذه القيمة
بنفس الحجم من الخرسانة (≈ 2.0 MJ/(m³·K))؟

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09 ضعف الكتلة الحرارية
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة J/(m³·K) الموحّدة**، لذا فإن
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)`. تعرض `toString()`
القيمة بالوحدة الأساسية: `"4184000.0 J/(m^3*K)"`.

## انظر أيضًا

* [السعة الحرارية](heat-capacity.ar.md) — الكمّية غير الموحّدة.
* [السعة الحرارية النوعية](specific-heat-capacity.ar.md) — نفس الفكرة لكل **كتلة** بدلًا من الحجم.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
