# الاستضاءة

الحزمة: `org.pcsoft.framework.kunit.optic.illuminance`
الوحدة الأساسية: **اللوكس**(`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`)

النوع: **وحدة مُركَّبة**

الاستضاءة `E` هي التدفق الضوئي **الواصل إلى سطح**، لكل وحدة من مساحة ذلك السطح: `E = Φ / A`، أي
`1 lx = 1 lm/m²`. إنها الكمية التي تُكتب بها كل معايير إضاءة أماكن العمل — وخلافًا للتدفق الضوئي، فهي
تعتمد على بُعد المصباح وعلى حجم المساحة المُضاءة، وليس فقط على المصباح نفسه.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · solidAngle¹ · distance⁻²`.

## الوحدات

| الوحدة         | قيمة التعداد                     | الرمز |         الرمز المميز | 1 وحدة باللوكس |
|--------------|--------------------------------|--------|--------------:|--------------:|
| لوكس          | `KIlluminanceUnit.LUX`         | `lx`   |         `lux` |           1.0 |
| فوت         | `KIlluminanceUnit.PHOT`        | `ph`   |       `phots` |        10 000 |
| قدم-شمعة  | `KIlluminanceUnit.FOOT_CANDLE` | `fc`   | `footCandles` |    ≈ 10.76391 |
| نوكس          | `KIlluminanceUnit.NOX`         | `nx`   |         `nox` |         0.001 |

الفوت هو وحدة نظام CGS (1 lm/cm²)، والقدم-شمعة هي الوحدة الإمبراطورية (1 lm/ft²)، بينما يُستخدم النوكس
لمستويات الإضاءة المنخفضة جدًا مثل ضوء القمر. تقبل جميع الرموز المميزة أي بادئة SI (`kilo.lux`، `milli.lux`، إلخ).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة:

| الصيغة             | التعبير                                                             |
|------------------|------------------------------------------------------------------------|
| العامل المصنَّف   | `luminousFlux / area`                                                  |
| الصيغة الأصلية (`toX()`) | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## الحساب باستخدام المجموعة

| التعبير                 | نوع النتيجة                 | المعنى                     |
|----------------------------|------------------------------|------------------------------|
| `luminousFlux / area`      | `KIlluminanceUnitInstance`  | `E = Φ / A`                 |
| `illuminance * area`       | `KLuminousFluxUnitInstance` | `Φ = E · A`                 |
| `luminousFlux / illuminance` | `KAreaUnitInstance`       | المساحة التي يمكن لتدفق ما إضاءتها   |
| `illuminance / solidAngle` | `KLuminanceUnitInstance`    | `L = E / Ω`                 |
| `illuminance * time`       | `KLuminousExposureUnitInstance` | `H = E · t`             |

## مثال من واقع الحياة — هل مكتبي مضاء بما يكفي؟

يتطلب العمل المكتبي تقريبًا **500 lx**. مصباح 800 lm فوق مكتب مساحته 2 m² يوفر:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 — أقل من هدف 500 lx
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // ستحتاج إلى 1000.0 lm
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة باللوكس**، لذا `(1 of phots) == (10000 of lux)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"500.0 lx"`.

## انظر أيضًا

* [التدفق الضوئي](luminous-flux.ar.md) — ما يصدره المصباح.
* [السطوع](luminance.ar.md) — الاستضاءة لكل زاوية مجسمة، "سطوع" السطح.
* [التعرض الضوئي](luminous-exposure.ar.md) — الاستضاءة المتراكمة عبر الزمن.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
