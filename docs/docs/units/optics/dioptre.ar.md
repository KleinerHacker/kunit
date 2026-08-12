# الديوبتر (القوة الانكسارية)

الحزمة: `org.pcsoft.framework.kunit.common.reciprocallength`
الوحدة الأساسية: **مقلوب المتر**(`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

النوع: **وحدة مُركَّبة**

القوة الانكسارية `D` لعدسة ما هي مقلوب بعدها البؤري: `D = 1 / f`. وحدتها هي **الديوبتر**، وهي بالضبط
مقلوب المتر — فالعدسة التي تُبَئِّر عند 1 m تمتلك قوة 1 dpt، وتلك التي تُبَئِّر عند 0.5 m تمتلك قوة 2 dpt.

بُعدها هو `distance⁻¹` — وهو **نفس** بُعد [العدد الموجي](../mechanics/wavenumber.md) في الطيفية. تُصمِّم
KUnit مجموعة واحدة محايدة تُدعى `reciprocallength` لكلا التفسيرَين؛ والقوة الانكسارية هي إحداهما. تتناول
هذه الصفحة ذلك التفسير.

!!! note "مجموعة واحدة، تفسيران"
    `KReciprocalLengthUnitInstance` هو النوع المشترك، لذا فإن القوة الانكسارية والعدد الموجي هما نفس
    الوحدة بالنسبة لـ KUnit. تحمل المجموعة الاسم المحايد `reciprocallength` لكي لا يستأثر أحد
    التفسيرَين باسم الآخر. مَيِّز بينهما بتسمية قيمك.

## الوحدات المُسمَّاة

| الوحدة                  | الرمز |                  الرمز المميز | 1 وحدة بـ m⁻¹ |
|-----------------------|--------|-----------------------:|--------------:|
| مقلوب المتر      | `1/m`  |     `reciprocalMeters` |           1.0 |
| ديوبتر               | `dpt`  |             `dioptres` |           1.0 |
| مقلوب السنتيمتر | `1/cm` | `reciprocalCentimeters` |         100.0 |
| كايزر                | `1/cm` |               `kaysers` |         100.0 |

`dioptres` و`kaysers` هما تسميتان بديلتان لمقلوب المتر ومقلوب السنتيمتر على التوالي، وليستا وحدتين
مستقلتين. تقبل جميع الرموز المميزة أي بادئة SI (`milli.dioptres`، إلخ).

## الحساب باستخدام المجموعة

| التعبير                       | نوع النتيجة                      | المعنى                          |
|----------------------------------|-----------------------------------|-----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | العودة إلى البعد البؤري         |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | العدسات الرقيقة المتلامسة تجمع قوتيها |
| `reciprocalLength * length`      | `Double`                         | عدد بلا بُعد (`m⁻¹ · m`)  |

يتم تحويل الصيغة الأصلية باستخدام `toReciprocalLength()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## مثال من واقع الحياة — نظارات القراءة

عدسة ذات بعد بؤري **40 cm** تعطي `D = 1 / 0.4 m = 2.5 dpt`. وضع عدسة ثانية أضعف بالتلامس معها يؤدي
ببساطة إلى جمع القوتين — وهذا بالضبط ما يفعله `+` من النوع نفسه:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // عدسات متلامسة
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 — البعد البؤري المُجمَّع
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة m⁻¹**، لذا
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`. تعرض `toString()` القيمة بالوحدة
الأساسية: `"2.5 1/m"`.

## انظر أيضًا

* [العدد الموجي](../mechanics/wavenumber.md) — نفس النوع، مُفسَّر ككمية طيفية.
* [المسافة](../kinematics/distance.md) — المجموعة التي تمثل هذه مقلوبها.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
