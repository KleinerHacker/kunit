# تركيز كمية المادة (المولارية)

الحزمة: `org.pcsoft.framework.kunit.thermo.concentration`
الوحدة الأساسية: **مول لكل متر مكعب** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

النوع: **وحدة مركّبة (constructed unit)**

تركيز كمية المادة `c` هو مقدار المادة المذابة **لكل حجم من المحلول**: `c = n / V`. تُعبَّر عنه
الكيمياء دائمًا تقريبًا بالمول لكل لتر وتسمّيه **المولارية**، وتُكتب `M`؛ بينما تستخدم المختبرات
السريرية الميليمول لكل لتر.

صيغتها القياسية للبُعد الأساسي هي `substance¹ · length⁻³`.

## الوحدات المسمّاة

| الوحدة                       | الرمز     |                 الرمز البرمجي | 1 وحدة بالـ mol/m³ |
|-------------------------------|-----------|------------------------------:|--------------------:|
| مول لكل متر مكعب               | `mol/m^3` |         `molesPerCubicMeter` |                  1.0 |
| مول لكل لتر (مولاري)           | `mol/l`   |              `molesPerLiter` |                 1000 |
| مولاري (`M`)                   | `mol/l`   |                       `molar` |                 1000 |
| ميليمول لكل لتر                | `mmol/l`  |         `millimolesPerLiter` |                  1.0 |

`molar` هي تهجئة أخرى لـ `molesPerLiter`، وليست وحدة مستقلة. لاحظ أن الميليمول لكل لتر يساوي
عدديًا المول لكل متر مكعب — فوحدة النظام الدولي الأساسية هي بالضبط الوحدة السريرية عدديًا. تقبل
جميع الرموز البرمجية كل بادئات النظام الدولي (`milli.molesPerLiter`، `micro.molar`، ...).

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا الشكلين ينتجان نفس المثيل المكتوب النوع والمتساوي القيمة:

| الشكل                  | التعبير                                                                   |
|------------------------|--------------------------------------------------------------------------|
| المُشغّل المكتوب النوع    | `amountOfSubstance / volume`                                             |
| الأصلي (`toX()`)        | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## الحساب باستخدام المجموعة

| التعبير                                    | نوع النتيجة                        | المعنى                     |
|-----------------------------------------------|--------------------------------------|-----------------------------|
| `amountOfSubstance / volume`                  | `KConcentrationUnitInstance`         | `c = n / V`                 |
| `concentration * volume`                      | `KAmountOfSubstanceUnitInstance`     | `n = c · V`                 |
| `amountOfSubstance / concentration`           | `KVolumeUnitInstance`                | الحجم المطلوب                |
| `conductivity / concentration`                | `KMolarConductivityUnitInstance`     | `Λ = κ / c`                 |

## مثال من الواقع — سكر الدم

مستوى سكر دم صائم قدره **5.5 mmol/l** في نحو 5 لترات من الدم يقابل:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## دلالات القيمة

تقارن `equals`/`hashCode` **قيمة mol/m³ المُطبَّعة**، لذا
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`. تعرض `toString()` القيمة بالوحدة
الأساسية: `"1000.0 mol/m^3"`.

## انظر أيضًا

* [المولالية](molality.ar.md) — نفس الفكرة لكل **كتلة** من المذيب، بمعزل عن التمدد الحراري.
* [كمية المادة](amount-of-substance.ar.md) — البسط.
* [الحجم المولي](molar-volume.ar.md) — الكمية المقلوبة لمادة نقية.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
