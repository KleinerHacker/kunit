# التعرض الضوئي

الحزمة: `org.pcsoft.framework.kunit.optic.luminousexposure`
الوحدة الأساسية: **لوكس ثانية**(`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`)

النوع: **وحدة مُركَّبة**

التعرض الضوئي `H` هو الاستضاءة **المتراكمة عبر الزمن**: `H = E · t`. إنه *جرعة الضوء* التي تلقاها سطح
ما — وهي الكمية التي يضعها القائمون على المتاحف في ميزانياتهم كلوكس ساعة سنويًا للحد من بهتان الأصباغ،
وهي الكمية الكامنة وراء قيمة التعريض الضوئي في الكاميرات.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹`.

## الوحدات

| الوحدة       | قيمة التعداد                          | الرمز |        الرمز المميز | 1 وحدة بـ lx·s |
|------------|-------------------------------------|--------|-------------:|---------------:|
| لوكس ثانية | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |            1.0 |
| لوكس ساعة   | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |           3600 |

تقبل جميع الرموز المميزة أي بادئة SI (`kilo.luxHours` هي الوحدة المعتادة لميزانية جرعة الضوء السنوية).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة:

| الصيغة             | التعبير                                                                   |
|------------------|--------------------------------------------------------------------------|
| العامل المصنَّف   | `illuminance * time`                                                         |
| الصيغة الأصلية (`toX()`) | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## الحساب باستخدام المجموعة

| التعبير                        | نوع النتيجة                     | المعنى                    |
|-----------------------------------|---------------------------------|-----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | متوسط الاستضاءة        |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | مدة التعرض          |

## مثال من واقع الحياة — ميزانية إضاءة متحف

تُحدَّد الرسومات المائية الحساسة بحوالي **50000 lx·h في السنة**. عند إضاءة عرض تبلغ 50 lx وساعات فتح
يومية تبلغ 8 ساعات، كم يومًا يمكن عرض القطعة؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // 125 يوم فتح في السنة

// الاتجاه المعاكس: كم من الوقت يمكن أن تبقى معروضة عند 200 lx؟
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0 ساعة
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة lx·s**، لذا `(1 of luxHours) == (3600 of luxSeconds)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"3600.0 lx*s"`.

## انظر أيضًا

* [الاستضاءة](illuminance.ar.md) — المعدل الذي تتراكم به هذه الكمية.
* [الطاقة الضوئية](luminous-energy.ar.md) — الفكرة نفسها للتدفق بدلًا من الاستضاءة.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
