# النفضة (Jerk)

الحزمة: `org.pcsoft.framework.kunit.kinematic.jerk`
الوحدة الأساسية: **متر لكل ثانية مكعبة** (`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

النوع: **وحدة مركّبة**

النفضة `j` هي معدّل تغيّر **التسارع**: `j = Δa / t`. وهي الكمّية التي تحدّها معايير الراحة أثناء الركوب فعليًا
— يمكن لمصعد أو قطار أن يتسارع بقوة، لكن التسارع يجب ألا يتغيّر فجأة، وإلا اهتزّ الركاب. تقع حدود الراحة
عادةً عند حوالي 0.5 m/s³.

صيغتها القانونية للبعد الأساسي المُطبَّع هي `length · time⁻³`.

## الوحدات المسمّاة

| الوحدة                          | الرمز    |                          الرمز البرمجي | القيمة بـ m/s³ (لوحدة واحدة) |
|----------------------------------|----------|------------------------------------------:|---------------------------------:|
| متر لكل ثانية مكعبة              | `m/s^3`  |         `metersPerSecondCubed`             |                              1.0 |
| الجاذبية القياسية لكل ثانية      | `g/s`    |   `standardGravitiesPerSecond`             |                          9.80665 |
| قدم لكل ثانية مكعبة              | `ft/s^3` |          `feetPerSecondCubed`              |                           0.3048 |

تقبل جميع الرموز البرمجية جميع بادئات النظام الدولي (`milli.metersPerSecondCubed`، إلخ).

## التفكيك

تملك هذه المجموعة تفكيكًا واحدًا، وكلا صيغتيه ينتجان نفس النسخة المُصنَّفة والمتساوية القيمة:

| الصيغة                     | التعبير                                                            |
|-----------------------------|------------------------------------------------------------------------|
| المُشغّل المُصنَّف          | `acceleration / time`                                               |
| الصيغة الأصلية (`toX()`)   | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s² (1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## الحساب باستخدام المجموعة

| التعبير                | نوع النتيجة                        | المعنى                        |
|-------------------------|---------------------------------------|----------------------------------|
| `acceleration / time`  | `KJerkUnitInstance`                   | `j = Δa / t`                     |
| `jerk * time`          | `KAccelerationUnitInstance`           | التسارع المتراكم                 |
| `acceleration / jerk`  | `KTimeUnitInstance`                   | المدة التي يستغرقها التدرّج      |

## مثال من الواقع — تدرّج مصعد ضمن حد الراحة

يجب أن يصل مصعد إلى **1 m/s²** دون تجاوز نفضة قدرها **0.5 m/s³**. كم يجب أن تكون مدة التدرّج؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// وبالعكس: ما هي النفضة التي يفرضها تدرّج مدته ثانية واحدة؟
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — ضعف حد الراحة
```

## دلالة القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة m/s³**، لذا
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`. تعرض `toString()` القيمة
بالوحدة الأساسية: `"0.6 m/s^3"`.

## انظر أيضًا

* [التسارع](acceleration.ar.md) — الكمّية التي تمثّل هذه الوحدة معدّل تغيّرها.
* [السرعة](speed.ar.md) و[المسافة](distance.ar.md) — بقية سلسلة الحركة.
* [نظرة عامة على علم الحركة](overview.ar.md)
