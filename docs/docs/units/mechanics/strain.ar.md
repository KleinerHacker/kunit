# الانفعال

الحزمة: `org.pcsoft.framework.kunit.mechanic.strain`
الوحدة الأساسية: **نسبة صرفة** (`KStrainUnit.BASE == KStrainUnit.RATIO`)

النوع: **وحدة مركّبة**

الانفعال `ε = ΔL / L` هو التشوّه النسبي لجسم ما. إنّه **عديم الأبعاد** — طول مقسوم على طول — لكنّ قراءاته (النسبة
المئوية، الجزء بالألف، الانفعال الميكروي) تشكّل مفردات وحدات حقيقية، لذا تُنمذجها KUnit كمجموعتها الخاصّة.

يغلّف `KStrainUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّ واحد فقط من `KStrainUnit.BASE`
بأس 1، مطبَّعًا دائمًا إلى النسبة الصرفة.

!!! note "لماذا `toStrain()` وليس عاملًا"
يُمثّل المحرّك العامّ `length / length` كوحدة مختلطة **بلا** حدود وحدات. ولأنّ
`KLengthUnitInstance.div` عامل عضو لا يمكن تجاوزه، يُوصَل التفكيك الأصلي عبر خطّاف التعرّف على الصيغة `toStrain()` بدلاً
من عامل مكتوب.

## الوحدات المسمّاة

| الوحدة          | الرمز | الرمز البرمجي | 1 وحدة كنسبة |
|-----------------|-------|--------------:|-------------:|
| نسبة صرفة (م/م) | `1`   |       `ratio` |          1.0 |
| نسبة مئوية      | `%`   |     `percent` |         0.01 |
| جزء بالألف      | `‰`   |    `perMille` |         1e-3 |
| انفعال ميكروي   | `µe`  | `microstrain` |         1e-6 |

تدعم جميع الوحدات نطاق بادئات النظام الدولي الكامل، لذا `micro.ratio` كتابة أخرى للانفعال الميكروي.

## بناء انفعال

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// قضيب طوله 1 m استطال بمقدار 2 mm
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## الحساب بواسطة انفعال

| التعبير                                  | نوع النتيجة             | المعنى                        |
|------------------------------------------|-------------------------|-------------------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L` (الصيغة الأصلية) |
| `stress / strain`                        | `KPressureUnitInstance` | معامل المرونة `E = σ / ε`     |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | الإجهاد `σ = E · ε`           |
| `strain + strain`, `strain - strain`     | `KStrainUnitInstance`   | حساب من نفس النوع             |

انظر صفحة [الإجهاد](stress.md) للجانب المتعلّق بمعامل المرونة من قانون هوك.

## مثال واقعي: مقياس انفعال على قضيب فولاذي

يقرأ مقياس انفعال على قضيب فولاذي (E = 210 GPa) قيمة 950 µe. أيّ إجهاد ميكانيكي يقابل ذلك، وكم يستطيل قضيب طوله 2 m؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // تحجيم قياسي لطول
elongation into milli.meters                    // 1.9
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1" (وحدة أساسية: النسبة الصرفة)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات    | Kotlin                         | المعنى                                   |
|--------------|--------------------------------|------------------------------------------|
| `1` (م/م)    | `ratio`                        | الانفعال، الوحدة الأساسية (عديم الأبعاد) |
| `%`          | `percent`                      | قراءة النسبة المئوية                     |
| `‰`          | `perMille`                     | قراءة الجزء بالألف                       |
| `µe`         | `microstrain`                  | قراءة مقياس الانفعال (1 µm/m)            |
| `ε = ΔL / L` | `(length / length).toStrain()` | التفكيك الأصلي                           |
| `σ = E · ε`  | `pressure * strain`            | قانون هوك                                |
| `E = σ / ε`  | `stress / strain`              | معامل المرونة                            |
