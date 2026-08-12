# العدد الموجي

الحزمة: `org.pcsoft.framework.kunit.common.reciprocallength`
الوحدة الأساسية: **مقلوب المتر** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

النوع: **وحدة مُركَّبة**

العدد الموجي `ṽ` لموجة ما هو مقلوب طولها الموجي: `ṽ = 1 / λ` — أي عدد دورات الموجة لكل وحدة طول.
يستخدمه علم الطيف بدلًا من الطول الموجي لأنه يتناسب طرديًا مع طاقة الفوتون، ويُعبَّر عنه دائمًا تقريبًا
**بمقلوب السنتيمتر** (`cm⁻¹`، ويُسمّى تاريخيًا *كايزر*): يمتد الضوء المرئي تقريبًا بين
14,000 و25,000 cm⁻¹، بينما تقع منطقة البصمة تحت الحمراء بين 400 و1500 cm⁻¹.

بُعده هو `distance⁻¹` — **وهو نفسه** بُعد القدرة الانكسارية للعدسة، أي [الديوبتر](../optics/dioptre.ar.md).
تُنمذج KUnit مجموعة محايدة واحدة، `reciprocallength`، لكلتا القراءتين؛ والعدد الموجي إحداهما. توثّق
هذه الصفحة تلك القراءة.

!!! note "مجموعة واحدة، قراءتان"
    `KReciprocalLengthUnitInstance` هو النوع المشترك، لذا فإن العدد الموجي والقدرة الانكسارية
    وحدة واحدة من منظور KUnit. تحمل المجموعة الاسم المحايد `reciprocallength` حتى لا تستأثر أي من
    القراءتين باسم الأخرى. ميّز بينهما من خلال تسمية قيمك.

## الوحدات المسمّاة

| الوحدة                  | الرمز |                   الرمز المميز | 1 وحدة بالـ m⁻¹ |
|--------------------------|--------|------------------------:|--------------:|
| مقلوب المتر               | `1/m`  |      `reciprocalMeters` |           1.0 |
| مقلوب السنتيمتر           | `1/cm` | `reciprocalCentimeters` |         100.0 |
| كايزر                     | `1/cm` |                `kaysers` |         100.0 |
| ديوبتر                    | `dpt`  |               `dioptres` |           1.0 |

تقبل جميع الرموز المميزة كل بادئات النظام الدولي (`kilo.reciprocalCentimeters`، وغيرها).

## الحساب باستخدام المجموعة

| التعبير                     | نوع النتيجة                          | المعنى                             |
|------------------------------|------------------------------------------|----------------------------------------|
| `1 / length`                 | `KReciprocalLengthUnitInstance`         | `ṽ = 1 / λ`                             |
| `1 / reciprocalLength`       | `KLengthUnitInstance`                   | العودة إلى الطول الموجي                  |
| `reciprocalLength * length`  | `Double`                                | عدد الدورات عديم الأبعاد                 |
| `reciprocalLength + …`       | `KReciprocalLengthUnitInstance`         | جمع من النوع نفسه                       |

يتحوّل الشكل الأصلي عبر `toReciprocalLength()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## مثال من الواقع — ضوء ليزر أخضر

يتحول خط ليزر بطول 500 نانومتر إلى عدد موجي قدره 20,000 cm⁻¹، ومنه يمكن استنتاج عدد الدورات التي
تناسب مسارًا طوله 1 مم مباشرةً:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0 (نفس الوحدة، الاسم الكلاسيكي)

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 دورة موجية لكل مليمتر

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## دلالة القيم

يقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة m⁻¹**، لذا
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`. تعرض `toString()` القيمة بالوحدة
الأساسية: `"2000000.0 1/m"`.

## طالع أيضًا

* [الديوبتر](../optics/dioptre.ar.md) — النوع نفسه، عند قراءته كقدرة انكسارية.
* [التردد](../kinematics/frequency.ar.md) — مقلوب الزمن، وهو النظير الزمني لهذه المجموعة.
* [نظرة عامة على الميكانيكا](overview.ar.md)
