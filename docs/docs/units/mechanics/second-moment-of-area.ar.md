# عزم المساحة الثاني

الحزمة: `org.pcsoft.framework.kunit.kinematic.distance`
الوحدة الأساسية: **متر رباعي** (`m⁴`، الفرع ذو الأس 4 في مجموعة distance)

النوع: **وحدة مُركَّبة**

عزم المساحة الثاني `I` (عزم القصور الذاتي للمساحة) هو الخاصية الهندسية التي تحدد مدى صلابة مقطع
العارضة عند الانحناء — وهو `I` في الصلابة الانعطافية `EI`. تعرضه جداول المقاطع الفولاذية بوحدة
`cm⁴`، بينما تُستخدم `mm⁴` للمقاطع الصغيرة.

خلافًا للمجموعات الأخرى في هذا الموقع، فهي ليست مجموعة قائمة بذاتها: إنها **الفرع ذو الأس 4** في
مجموعة distance، أي `KSecondMomentOfAreaUnitInstance`، ويقع بجانب [الطول](../kinematics/distance.ar.md)
(الأس 1)، والمساحة (الأس 2)، والحجم (الأس 3).

!!! warning "ليس عزم القصور الذاتي"
    لا تخلط بين هذا وبين [عزم القصور الذاتي](moment-of-inertia.ar.md) *الكتلي* (`kg·m²`)، الذي يصف
    مقاومة التسارع الزاوي. الأسماء متشابهة، لكن الأبعاد مختلفة.

## الرموز المميزة

| الوحدة                  | الرمز |                الرمز المميز | 1 وحدة بالـ m⁴ |
|--------------------------|--------|---------------------:|-------------:|
| متر رباعي                 | `m⁴`   |       `quarticMeters` |          1.0 |
| سنتيمتر رباعي             | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| مليمتر رباعي              | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| إنش رباعي                 | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

تقبل جميع الرموز المميزة كل بادئات النظام الدولي.

## الحساب باستخدام الفرع

كل ناتج ضرب يصل إلى الأس 4 يُعيد الآن الفرع المُصنَّف بدلًا من `KDistanceUnitInstance` العام:

| التعبير                        | نوع النتيجة                              | المعنى                       |
|-----------------------------------|------------------------------------------------|----------------------------------|
| `area * area`                    | `KSecondMomentOfAreaUnitInstance`             | m² · m² = m⁴                     |
| `volume * length`                | `KSecondMomentOfAreaUnitInstance`             | m³ · m = m⁴                      |
| `length * volume`                | `KSecondMomentOfAreaUnitInstance`             | m · m³ = m⁴                      |
| `secondMomentOfArea / length`    | `KVolumeUnitInstance`                         | معامل المقطع                      |
| `secondMomentOfArea / area`      | `KAreaUnitInstance`                           | m⁴/m² = m²                       |
| `secondMomentOfArea / volume`    | `KLengthUnitInstance`                         | m⁴/m³ = m                        |
| `secondMomentOfArea + …`         | `KSecondMomentOfAreaUnitInstance`             | أجزاء مقطع مُركَّب                 |

يقتصر الجمع على البعد نفسه — `secondMomentOfArea + area` تُسبب **خطأ في الترجمة** تمامًا مثل
`length + area`.

يتحوّل الشكل الأصلي عبر `toSecondMomentOfArea()`:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## مثال من الواقع — عارضة مستطيلة

بالنسبة لمستطيل بعرض `b` وارتفاع `h`، فإن `I = b·h³/12`. لمقطع بأبعاد 100 مم × 200 مم:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// معامل المقطع W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// مقطع مُركَّب: عارضتان متماثلتان جنبًا إلى جنب
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## دلالة القيم

يعمل `equals`/`hashCode` والمقارنة على القيمة المُطبَّعة بوحدة `m⁴`، مع الاقتصار على البعد نفسه.
يُرجع `exponent` القيمة `4`.

## طالع أيضًا

* [المسافة](../kinematics/distance.ar.md) — المجموعة التي ينتمي إليها هذا الفرع.
* [عزم القصور الذاتي](moment-of-inertia.ar.md) — الكمية *الكتلية* ذات الاسم المشابه.
* [نظرة عامة على الميكانيكا](overview.ar.md)
