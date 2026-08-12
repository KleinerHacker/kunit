# الشدة الإشعاعية

الحزمة: `org.pcsoft.framework.kunit.optic.radiantintensity`
الوحدة الأساسية: **واط لكل ستراديان**(`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`)

النوع: **وحدة مُركَّبة**

الشدة الإشعاعية `Iₑ` هي التدفق الإشعاعي (القدرة) الذي يصدره مصدر ما **لكل زاوية مجسمة**: `Iₑ = P / Ω`.
إنها النظير **الإشعاعي** لـ [شدة الإضاءة](luminous-intensity.ar.md) — نفس الهندسة، لكنها تُقاس بالواط
بدلًا من اللومن، لذا فهي تحسب كل الإشعاع بما في ذلك الأشعة تحت الحمراء وفوق البنفسجية التي لا تراها
العين.

صيغتها القياسية الأساسية بالأبعاد هي `mass¹ · distance² · time⁻³ · solidAngle⁻¹`.

## الوحدات

| الوحدة               | قيمة التعداد                                   | الرمز |               الرمز المميز | 1 وحدة بـ W/sr |
|--------------------|------------------------------------------------|--------|--------------------:|---------------:|
| واط لكل ستراديان | `KRadiantIntensityUnit.WATT_PER_STERADIAN`   | `W/sr` | `wattsPerSteradian` |            1.0 |

يقبل هذا الرمز المميز أي بادئة SI (`milli.wattsPerSteradian`، `kilo.wattsPerSteradian`، إلخ).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة. تُجمَّع
الصيغة الأصلية من **قوالب وحدات** لأن المجموعة تحمل حد كتلة (انظر أيضًا نفس الملاحظة في
[الفعالية الضوئية](luminous-efficacy.ar.md)).

| الصيغة             | التعبير                                                        |
|------------------|---------------------------------------------------------------------|
| العامل المصنَّف   | `power / solidAngle`                                              |
| الصيغة الأصلية (`toX()`) | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## الحساب باستخدام المجموعة

| التعبير                        | نوع النتيجة                       | المعنى                    |
|-----------------------------------|------------------------------------|-----------------------------|
| `power / solidAngle`              | `KRadiantIntensityUnitInstance`   | `Iₑ = P / Ω`               |
| `radiantIntensity * solidAngle`   | `KPowerUnitInstance`              | `P = Iₑ · Ω`               |
| `power / radiantIntensity`        | `KSolidAngleUnitInstance`         | الزاوية المخروطية التي ينتشر فيها |
| `radiantIntensity / area`         | `KRadianceUnitInstance`           | `Lₑ = Iₑ / A`              |

## مثال من واقع الحياة — ثنائي باعث للأشعة تحت الحمراء

يُشِعّ باعث للأشعة تحت الحمراء **20 mW** في مخروط قدره 0.2 sr. شدته الإشعاعية، والقدرة التي يلتقطها
فتحة كاشف قدرها 0.05 sr، هما:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // 5.0 mW تصل إلى الكاشف
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة W/sr**، لذا
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`. تعرض `toString()` القيمة بالوحدة
الأساسية: `"5.0 W/sr"`.

## انظر أيضًا

* [شدة الإضاءة](luminous-intensity.ar.md) — النظير الضوئي.
* [الإشعاع اللمعاني](radiance.ar.md) — الشدة الإشعاعية لكل مساحة مُصدِرة.
* [الفعالية الضوئية](luminous-efficacy.ar.md) — الجسر بين الواطات واللومنات.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
