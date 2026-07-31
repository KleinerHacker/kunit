# الحجم النوعي

الحزمة: `org.pcsoft.framework.kunit.mechanic.specificvolume`
الوحدة الأساسية: **متر مكعّب لكل كيلوغرام**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

النوع: **وحدة مركّبة**

الحجم النوعي `v` هو الحجم الذي تشغله وحدة كتلة — أي **مقلوب [الكثافة](density.md)**. إنّها وحدة **مركّبة** — التركيبة
`length³ · mass⁻¹` (`m³/kg`).

يغلّف `KSpecificVolumeUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية:
`KDistanceUnit.BASE` (متر) بأس `+3` و`KMassUnit.BASE` (غرام) بأس `-1`. ولأنّ مكوّن الكتلة في هذه المكتبة مطبَّع إلى
الغرام، فإنّ القيمة المخزَّنة هي قيمة المكوّن الخام المبنيّ على الغرام، وتُربَط القراءات بوحدة m³/kg بعامل ثابت.

## الوحدات المسمّاة

| الوحدة                | الرمز     |             الرمز البرمجي | 1 وحدة بـ m³/kg |
|-----------------------|-----------|--------------------------:|----------------:|
| متر مكعّب لكل كيلوغرام | `m^3/kg`  |  `cubicMetersPerKilogram` |             1.0 |
| لتر لكل كيلوغرام      | `l/kg`    |       `litersPerKilogram` |            1e-3 |
| سنتيمتر مكعّب لكل غرام | `cm^3/g`  | `cubicCentimetersPerGram` |            1e-3 |
| قدم مكعّب لكل رطل      | `ft^3/lb` |       `cubicFeetPerPound` |     ≈ 0.0624280 |

تدعم جميع الوحدات نطاق بادئات النظام الدولي الكامل (`milli.cubicMetersPerKilogram`).

## الحساب باستخدام الوحدات الأساسية

| التعبير                                          | نوع النتيجة                   | المعنى      |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`, `mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

عمليّات المقلوب محكومة بالنوع: `1 / density` تحتفظ بنوع وحدة حقيقي بدلاً من التدهور إلى وحدة مختلطة عامّة. تتحوّل الصيغة
الأصلية عبر `toSpecificVolume()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true - عملية عكس دقيقة
```

## مثال واقعي: الاستعلام في جداول البخار

للبخار المشبَع عند 1 bar حجم نوعي يبلغ نحو 1.694 m³/kg. أيّ حجم يشغله 2 kg من هذا البخار، وما كثافته؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true (نفس القيمة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg" (وحدة أساسية)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                                 | المعنى                                   |
|-------------|----------------------------------------|------------------------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | الحجم النوعي، الوحدة الأساسية (رمز مسمّى) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | نفس الكمّية كحاصل ضرب صرف                 |
| `l/kg`      | `litersPerKilogram`                    | قراءة لتر لكل كيلوغرام                   |
| `v = V / m` | `volume / mass`                        | التفكيك المكتوب                          |
| `v = 1 / ρ` | `1 / density`                          | مقلوب الكثافة                            |
| `ρ = 1 / v` | `1 / specificvolume`                   | العودة إلى الكثافة                       |
