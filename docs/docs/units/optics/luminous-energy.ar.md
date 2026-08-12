# الطاقة الضوئية

الحزمة: `org.pcsoft.framework.kunit.optic.luminousenergy`
الوحدة الأساسية: **لومن ثانية**(`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`)

النوع: **وحدة مُركَّبة**

الطاقة الضوئية `Q` هي التدفق الضوئي **المتراكم عبر الزمن**: `Q = Φ · t`. بينما يُخبرنا التدفق بمدى
سطوع مصباح ما *في هذه اللحظة*، تُخبرنا الطاقة الضوئية بإجمالي مقدار الضوء الذي أصدره — وهي الكمية
الكامنة وراء تصنيفات عمر المصابيح وطاقات وميض التصوير الفوتوغرافي. يُعرف لومن ثانية أيضًا باسم
**تالبوت**.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · solidAngle¹ · time¹`.

## الوحدات

| الوحدة         | قيمة التعداد                          | الرمز |          الرمز المميز | 1 وحدة بـ lm·s |
|--------------|-------------------------------------|--------|---------------:|---------------:|
| لومن ثانية | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |            1.0 |
| تالبوت       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |            1.0 |
| لومن ساعة   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |           3600 |

`talbots` هي تسمية بديلة للوحدة الأساسية، وليست وحدة مستقلة. تقبل جميع الرموز المميزة أي بادئة SI
(`kilo.lumenHours`، `milli.lumenSeconds`، إلخ).

## التحليل

تمتلك هذه المجموعة تحليلًا واحدًا، وتُنتج كلتا صيغتيه نفس الكائن المصنَّف المتساوي في القيمة:

| الصيغة             | التعبير                                                                  |
|------------------|-------------------------------------------------------------------------|
| العامل المصنَّف   | `luminousFlux * time`                                                       |
| الصيغة الأصلية (`toX()`) | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## الحساب باستخدام المجموعة

| التعبير                       | نوع النتيجة                   | المعنى                       |
|-----------------------------------|--------------------------------|--------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | متوسط التدفق              |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | مدة صدور التدفق |

## مثال من واقع الحياة — إجمالي الضوء الصادر خلال عمر المصباح

مصباح LED بقدرة 800 lm مُصنَّف لعمر **25000 ساعة**. إجمالي الضوء الذي سيصدره طوال عمره هو:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// إذا شُغِّل 3 ساعات يوميًا، كم يومًا يغطي ذلك؟
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333 يومًا
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة lm·s**، لذا `(1 of lumenHours) == (3600 of lumenSeconds)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"3600.0 lm*s"`.

## انظر أيضًا

* [التدفق الضوئي](luminous-flux.ar.md) — المعدل الذي تتراكم به هذه الكمية.
* [التعرض الضوئي](luminous-exposure.ar.md) — الفكرة نفسها للاستضاءة بدلًا من التدفق.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
