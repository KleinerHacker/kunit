# السطوع

الحزمة: `org.pcsoft.framework.kunit.optic.luminance`
الوحدة الأساسية: **شمعة لكل متر مربع**(`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`)

النوع: **وحدة مُركَّبة**

السطوع `L` هو شدة الإضاءة **لكل مساحة مُصدِرة**: `L = I / A`، أي `1 cd/m² = 1 nit`. إنه ما تُدركه
العين فعليًا كـ "سطوع" لسطح ما، وهو الرقم الذي تذكره كل مواصفات شاشات العرض — فالشاشة المكتبية النموذجية
تتراوح بين 250-350 نِت، بينما تلفزيون HDR قد يصل إلى 1000 نِت أو أكثر.

صيغتها القياسية الأساسية بالأبعاد هي `luminousIntensity¹ · distance⁻²`.

## الوحدات

| الوحدة                     | قيمة التعداد                                | الرمز   |                    الرمز المميز | 1 وحدة بـ cd/m² |
|--------------------------|---------------------------------------------|----------|-------------------------:|----------------:|
| شمعة لكل متر مربع | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` | `candelasPerSquareMeter` |             1.0 |
| نِت                      | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` |                   `nits` |             1.0 |
| ستيلب                    | `KLuminanceUnit.STILB`                    | `sb`     |                 `stilbs` |          10 000 |
| أبوستيلب                 | `KLuminanceUnit.APOSTILB`                 | `asb`    |              `apostilbs` |           1 / π |
| لامبرت                  | `KLuminanceUnit.LAMBERT`                  | `L`      |               `lamberts` |        10⁴ / π  |
| قدم-لامبرت             | `KLuminanceUnit.FOOT_LAMBERT`             | `fL`     |           `footLamberts` |      ≈ 3.426259 |

`nits` هي تسمية بديلة للوحدة الأساسية، وليست وحدة مستقلة — إنها الاسم الذي تستخدمه صناعة شاشات العرض
لشمعة لكل متر مربع. تنتمي الأبوستيلب واللامبرت وقدم-لامبرت إلى عائلة *لامبرتيان*، وتحمل المعامل `1/π`
الذي يحوّل استضاءة مُصدِر منتشر مثالي إلى سطوعه. تقبل جميع الرموز المميزة أي بادئة SI.

## التحليلات

تمتلك هذه المجموعة تحليلَين **اثنين**. يصبّان معًا في نفس المصنع المُطَبِّع، لذا يُنتجان نفس الكائن
المصنَّف المتساوي في القيمة:

| الصيغة                   | التعبير                                                     |
|------------------------|------------------------------------------------------------------------|
| العامل المصنَّف A       | `luminousIntensity / area`                                     |
| العامل المصنَّف B       | `illuminance / solidAngle`                                     |
| الصيغة الأصلية (`toX()`)       | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## الحساب باستخدام المجموعة

| التعبير                     | نوع النتيجة                      | المعنى                    |
|--------------------------------|-----------------------------------|-----------------------------|
| `luminousIntensity / area`     | `KLuminanceUnitInstance`         | `L = I / A`                |
| `illuminance / solidAngle`     | `KLuminanceUnitInstance`         | `L = E / Ω`                |
| `luminance * area`             | `KLuminousIntensityUnitInstance` | `I = L · A`                |
| `luminance * solidAngle`       | `KIlluminanceUnitInstance`       | `E = L · Ω`                |
| `luminousIntensity / luminance` | `KAreaUnitInstance`             | المساحة المُصدِرة          |
| `illuminance / luminance`      | `KSolidAngleUnitInstance`        | الزاوية المخروطية التي ينتشر فيها الضوء |

## مثال من واقع الحياة — تصنيف نِت لشاشة عرض

شاشة قياس 27 بوصة بلوحة مساحتها **0.21 m²** مُصنَّفة عند **300 نِت**. يقابل ذلك شدة إضاءة إجمالية على
المحور تساوي:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6 (القراءة الإمبراطورية)
```

## دلالات القيمة

تقارن `equals`/`hashCode` **القيمة المُطبَّعة بوحدة cd/m²**، لذا `(1 of stilbs) == (10000 of candelasPerSquareMeter)`.
تعرض `toString()` القيمة بالوحدة الأساسية: `"250.0 cd/m^2"`.

## انظر أيضًا

* [شدة الإضاءة](luminous-intensity.ar.md) — بسط السطوع.
* [الاستضاءة](illuminance.ar.md) — الضوء الواصل إلى سطح بدلًا من المغادر منه.
* [الإشعاع اللمعاني](radiance.ar.md) — النظير الإشعاعي.
* [نظرة عامة على البصريات](overview.ar.md)
</content>
