# المعاوقة الصوتية النوعية

الحزمة: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
الوحدة الأساسية: **باسكال ثانية لكل متر**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

النوع: **وحدة مُركَّبة**

المعاوقة الصوتية النوعية `Z` هي الضغط الصوتي الذي يُنتجه الوسط لكل وحدة من سرعة الجسيمات:
`Z = p / v = ρ · c`. وهي التي تحدد مقدار الصوت المنعكس عند الحد الفاصل — فالهواء له معاوقة تبلغ نحو
413 Pa·s/m، بينما الماء نحو 1.48 MPa·s/m، أي بنسبة تقارب 3600، وهذا هو السبب في أن الصوت
المحمول جوًا يكاد لا يدخل إلى الماء إطلاقًا.

الشكل القياسي الأساسي المعياري لأبعادها هو `mass · length⁻² · time⁻¹`.

## الوحدات المسمّاة

| الوحدة                    | الرمز        |                   الرمز المميز | 1 وحدة بالـ Pa·s/m |
|----------------------------|--------------|------------------------:|-----------------:|
| باسكال ثانية لكل متر         | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| ريل SI                     | `rayl`       |                 `rayls` |              1.0 |
| ريل CGS                    | `rayl (CGS)` |              `cgsRayls` |               10 |

يُعدّ `rayls` تهجئة ثانية للوحدة الأساسية، وليس وحدة قائمة بذاتها. تقبل جميع الرموز المميزة كل بادئات
النظام الدولي (`mega.rayls` هي الشائعة للأنسجة والماء). كما هو الحال في مجموعات القوة والضغط والكثافة
المجاورة، تُخزّن الحالة **القيمة الأولية للمكوّن على أساس الغرام**.

## التفكيكات

تحتوي هذه المجموعة على تفكيكين **اثنين**. كلاهما يصبّان في نفس المصنع الموحِّد:

| الشكل                   | التعبير                                                        |
|--------------------------|--------------------------------------------------------------------|
| مُعامِل مُصنَّف A            | `pressure / speed`                                                |
| مُعامِل مُصنَّف B            | `density * speed` (`Z = ρ · c`، المعاوقة المميزة)                  |
| الشكل الأصلي (`toX()`)   | `(1 of kilo.grams / m² / s).toAcousticImpedance()`                |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## الحساب باستخدام المجموعة

| التعبير                            | نوع النتيجة                          | المعنى                    |
|--------------------------------------|-----------------------------------------|-----------------------------|
| `pressure / speed`                  | `KAcousticImpedanceUnitInstance`      | `Z = p / v`                 |
| `density * speed`                   | `KAcousticImpedanceUnitInstance`      | `Z = ρ · c`                 |
| `acousticImpedance * speed`         | `KPressureUnitInstance`               | الضغط الصوتي                 |
| `pressure / acousticImpedance`      | `KSpeedUnitInstance`                  | سرعة الجسيمات                 |
| `acousticImpedance / speed`         | `KDensityUnitInstance`                | العودة إلى `ρ`               |
| `acousticImpedance / density`       | `KSpeedUnitInstance`                  | العودة إلى `c`               |

## مثال من الواقع — الحد الفاصل بين الهواء والماء

لماذا لا تنجح محاولة الصراخ باتجاه رأس سبّاح تحت الماء؟ لنقارن المعاوقتين المميزتين:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 — انعكاس شبه كامل
```

## دلالة القيم

يقارن `equals`/`hashCode` **القيمة المُطبَّعة للمكوّن**، لذا `(1 of cgsRayls) == (10 of rayls)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"413.0 Pa*s/m"`.

## طالع أيضًا

* [الكثافة](density.ar.md) و[السرعة](../kinematics/speed.ar.md) — عاملا `Z = ρ · c`.
* [الضغط](pressure.ar.md) — جانب الضغط الصوتي.
* [نظرة عامة على الميكانيكا](overview.ar.md)
