# المولالية

الحزمة: `org.pcsoft.framework.kunit.thermo.molality`
الوحدة الأساسية: **مول لكل كيلوغرام** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

النوع: **وحدة مركّبة (constructed unit)**

المولالية `b` هي مقدار المادة المذابة **لكل كتلة من المذيب**: `b = n / m`. على عكس
[التركيز](concentration.ar.md)، الذي يشير إلى حجم، لا تتغيّر المولالية عند تسخين المحلول —
فكتلة المذيب لا تتأثر بالتمدد الحراري. هذا يجعلها الكمية المفضّلة للخصائص التجميعية مثل انخفاض
نقطة التجمّد وارتفاع نقطة الغليان.

صيغتها القياسية للبُعد الأساسي هي `substance¹ · mass⁻¹`.

## الوحدات المسمّاة

| الوحدة                    | الرمز     |                    الرمز البرمجي | 1 وحدة بالـ mol/kg |
|----------------------------|-----------|--------------------------------:|---------------------:|
| مول لكل كيلوغرام            | `mol/kg`  |            `molesPerKilogram` |                  1.0 |
| ميليمول لكل كيلوغرام         | `mmol/kg` |       `millimolesPerKilogram` |                0.001 |

تقبل جميع الرموز البرمجية كل بادئات النظام الدولي (`milli.molesPerKilogram`، ...).

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا الشكلين ينتجان نفس المثيل المكتوب النوع والمتساوي القيمة.
لاحظ أن الشكل الأصلي مُجمَّع من **قوالب الوحدات**: بالنسبة لمجموعة تحمل حدّ كتلة، تكون القيمة
المختلطة الخام هي حاصل ضرب قائم على الغرام، بينما يخزّن المثيل المكتوب النوع قيمته بالوحدة
المسمّاة.

| الشكل                  | التعبير                                              |
|------------------------|--------------------------------------------------------|
| المُشغّل المكتوب النوع    | `amountOfSubstance / mass`                              |
| الأصلي (`toX()`)        | `(0.25 of moles / kilo.grams).toMolality()`             |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## الحساب باستخدام المجموعة

| التعبير                          | نوع النتيجة                        | المعنى                       |
|--------------------------------------|--------------------------------------|-------------------------------|
| `amountOfSubstance / mass`          | `KMolalityUnitInstance`              | `b = n / m`                   |
| `molality * mass`                   | `KAmountOfSubstanceUnitInstance`     | `n = b · m`                   |
| `amountOfSubstance / molality`      | `KMassUnitInstance`                  | كتلة المذيب المطلوبة            |
| `1 / molarMass`                     | `KMolalityUnitInstance`              | مولالية مادة نقية                |
| `1 / molality`                      | `KMolarMassUnitInstance`             | العودة إلى الكتلة المولية        |

تعكس العلاقتان الأخيرتان أن المولالية و[الكتلة المولية](molar-mass.ar.md) متبادلتان (مقلوبتان
لبعضهما).

## مثال من الواقع — كم مولًا يوجد في كيلوغرام من الماء؟

الكتلة المولية للماء 18.015 غ/مول، لذا يحتوي كيلوغرام واحد منه على حوالي 55.5 مول — وهذا مثال
عملي على علاقة المقلوب:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## دلالات القيمة

تقارن `equals`/`hashCode` **قيمة mol/kg المُطبَّعة**، لذا
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`. تعرض `toString()` القيمة
بالوحدة الأساسية: `"0.25 mol/kg"`.

## انظر أيضًا

* [تركيز كمية المادة](concentration.ar.md) — نفس الفكرة لكل حجم.
* [الكتلة المولية](molar-mass.ar.md) — الكمية المقلوبة.
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
