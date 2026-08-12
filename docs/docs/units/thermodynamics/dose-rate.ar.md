# معدل الجرعة

الحزمة: `org.pcsoft.framework.kunit.thermo.doserate`
الوحدة الأساسية: **غراي لكل ثانية** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

النوع: **وحدة مركّبة (constructed unit)**

معدل الجرعة هو جرعة الإشعاع الممتصة **لكل وحدة زمن**: `Ḋ = D / t`. وهو ما يعرضه جهاز المسح
الإشعاعي — غالبًا بوحدة ميكروسيفرت في الساعة — بينما الجرعة المتراكمة هي تكامل هذه القيمة عبر
زمن التعرّض.

صيغتها القياسية للبُعد الأساسي هي `length² · time⁻³`. يُلغى الكيلوغرام في `J/kg` الخاص بالغراي
مقابل كيلوغرام الجول، ولهذا السبب لا يبقى أي حد للكتلة.

## الوحدات المسمّاة

| الوحدة              | الرمز   | الرمز البرمجي          | 1 وحدة بالـ Gy/s |
|---------------------|---------|-------------------------|-----------------:|
| غراي لكل ثانية        | `Gy/s`  | `graysPerSecond`        |             1.0 |
| غراي لكل ساعة         | `Gy/h`  | `graysPerHour`          |          1/3600 |
| سيفرت لكل ثانية       | `Sv/s`  | `sievertsPerSecond`     |             1.0 |
| سيفرت لكل ساعة        | `Sv/h`  | `sievertsPerHour`       |          1/3600 |

يشترك الغراي (الجرعة الممتصة) والسيفرت (الجرعة المكافئة) في بُعد واحد، لذا يُنمذج KUnit مجموعة
واحدة للاثنين — توجد تهجئات السيفرت لكي يمكن كتابة قراءات الحماية من الإشعاع مباشرةً. تقبل جميع
الرموز البرمجية كل بادئات النظام الدولي؛ `micro.sievertsPerHour` هي الصيغة الشائعة يوميًا.

!!! note "مجموعة واحدة، قراءتان"
    يختلف الغراي والسيفرت بعامل الترجيح الإشعاعي عديم البُعد، وليس بالبُعد. يجب أن تُخطَّط صيغة
    قياسية واحدة إلى نوع واحد (انظر [الإنتروبيا](entropy.ar.md) لنفس الحجة)، لذا فإن التمييز هو
    مسألة كيف تُسمّي قيمتك.

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا الشكلين ينتجان نفس المثيل المكتوب النوع والمتساوي القيمة:

| الشكل                  | التعبير                                                                       |
|------------------------|-----------------------------------------------------------------------------------|
| المُشغّل المكتوب النوع    | `specificEnergy / time`                                                          |
| الأصلي (`toX()`)        | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## الحساب باستخدام المجموعة

| التعبير                        | نوع النتيجة                        | المعنى                |
|-----------------------------------|---------------------------------------|------------------------|
| `specificEnergy / time`          | `KDoseRateUnitInstance`               | `Ḋ = D / t`            |
| `doseRate * time`                | `KSpecificEnergyUnitInstance`         | الجرعة المتراكمة          |
| `specificEnergy / doseRate`      | `KTimeUnitInstance`                   | زمن التعرّض               |

الجرعة الممتصة نفسها هي مجموعة [الطاقة النوعية](specific-energy.ar.md) — 1 Gy = 1 J/kg.

## مثال من الواقع — الإشعاع الخلفي السنوي الطبيعي

تبلغ الخلفية الإشعاعية الطبيعية نحو **0.274 µSv/h**. على مدار عام (8766 ساعة) يتراكم ذلك إلى
القيمة المألوفة 2.4 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## دلالات القيمة

تقارن `equals`/`hashCode` **قيمة الـ Gy/s المُطبَّعة**، لذا
`(1 of graysPerHour) == (1 of sievertsPerHour)`. تعرض `toString()` القيمة بالوحدة الأساسية:
`"1.0 Gy/s"`.

## انظر أيضًا

* [الطاقة النوعية](specific-energy.ar.md) — الجرعة الممتصة نفسها (`Gy` = `J/kg`).
* [نظرة عامة على الديناميكا الحرارية](overview.ar.md)
