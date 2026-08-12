# الإشعاع اللمعاني

الحزمة: `org.pcsoft.framework.kunit.optic.radiance`
الوحدة الأساسية: **واط لكل ستراديان متر مربع**
(`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`)

النوع: **وحدة مُركَّبة**

الإشعاع اللمعاني `Lₑ` هو الشدة الإشعاعية **لكل مساحة مُصدِرة**: `Lₑ = Iₑ / A`. إنه النظير
**الإشعاعي** لـ [السطوع](luminance.ar.md)، والكمية التي يعمل بها الاستشعار عن بُعد والتصوير الحراري —
ما يُكامِله بكسل الكاميرا فعليًا، بصرف النظر عن بُعد السطح.

صيغتها القياسية الأساسية بالأبعاد هي `mass¹ · time⁻³ · solidAngle⁻¹`. يتلاشى أُسّا الطول: يسهم
الواط بـ `distance²` وتسهم المساحة بـ `distance⁻²`.

## الوحدات

| الوحدة                            | قيمة التعداد                                    | الرمز       |                            الرمز المميز | 1 وحدة بـ W/(sr·m²) |
|---------------------------------|-----------------------------------------------|--------------|---------------------------------:|--------------------:|
| واط لكل ستراديان متر مربع | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER` | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

يقبل هذا الرمز المميز أي بادئة SI (`milli.wattsPerSteradianSquareMeter`، إلخ).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة. تُجمَّع
الصيغة الأصلية من **قوالب وحدات** لأن المجموعة تحمل حد كتلة.

| الصيغة             | التعبير                                                    |
|------------------|-----------------------------------------------------------------|
| العامل المصنَّف   | `radiantIntensity / area`                                     |
| الصيغة الأصلية (`toX()`) | `(5 of kilo.grams / s³ / sr).toRadiance()`                    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## الحساب باستخدام المجموعة

| التعبير                        | نوع النتيجة                     | المعنى         |
|-----------------------------------|---------------------------------|-----------------|
| `radiantIntensity / area`         | `KRadianceUnitInstance`         | `Lₑ = Iₑ / A`   |
| `radiance * area`                 | `KRadiantIntensityUnitInstance` | `Iₑ = Lₑ · A`   |
| `radiantIntensity / radiance`     | `KAreaUnitInstance`             | المساحة المُصدِرة |

## مثال من واقع الحياة — بكسل كاميرا حرارية

جدار فرن مساحته **2 m²** يُشِعّ **10 W/sr** باتجاه الكاميرا. إشعاعه اللمعاني — القيمة التي تُبلِّغ عنها
الكاميرا بغض النظر عن البُعد — هو:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// جزء بمساحة 0.5 m² من نفس الجدار يصدر شدة أقل نسبيًا …
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 — لكن الإشعاع اللمعاني لا يتغير
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة W/(sr·m²)**، لذا
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`. تعرض
`toString()` القيمة بالوحدة الأساسية: `"5.0 W/(sr*m^2)"`.

## انظر أيضًا

* [الشدة الإشعاعية](radiant-intensity.ar.md) — البسط.
* [السطوع](luminance.ar.md) — النظير الضوئي.
* [كثافة التدفق الحراري](../thermodynamics/heat-flux-density.md) — الإشعاع اللمعاني مُكامَلًا عبر نصف الكرة.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
