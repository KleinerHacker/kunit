# الكثافة الخطّية

الحزمة: `org.pcsoft.framework.kunit.mechanic.lineardensity`
الوحدة الأساسية: **كيلوغرام لكل متر**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

النوع: **وحدة مركّبة**

الكثافة الخطّية هي الكتلة لكل وحدة طول — الشقيقة أحادية البُعد لِـ
[الكثافة السطحية](areadensity.md) (`kg/m²`) و[الكثافة](density.md) (`kg/m³`). إنّها وحدة **مركّبة** — التركيبة
`mass · length⁻¹` (`kg/m`).

يغلّف `KLinearDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) بأس `+1` و`KDistanceUnit.BASE` (متر) بأس `-1`. ولأنّ مكوّن الكتلة في هذه المكتبة مطبَّع إلى
الغرام، فإنّ القيمة المخزَّنة هي قيمة المكوّن الخام المبنيّ على الغرام، وتُقسَم القراءات بوحدة kg/m على عامل ثابت.

## الوحدات المسمّاة

| الوحدة           | الرمز   |        الرمز البرمجي | 1 وحدة بـ kg/m |
|------------------|---------|---------------------:|---------------:|
| كيلوغرام لكل متر | `kg/m`  |  `kilogramsPerMeter` |            1.0 |
| غرام لكل متر     | `g/m`   |      `gramsPerMeter` |           1e-3 |
| غرام لكل سنتيمتر | `g/cm`  | `gramsPerCentimeter` |            0.1 |
| تكس (نسيجي)      | `tex`   |                `tex` |           1e-6 |
| دنييه (نسيجي)    | `den`   |             `denier` |    ≈ 1.1111e-7 |
| رطل لكل قدم      | `lb/ft` |      `poundsPerFoot` |      ≈ 1.48816 |

تدعم جميع الوحدات نطاق بادئات النظام الدولي الكامل؛ الديسي-تكس النسيجي هو `deci.tex`.

## الحساب باستخدام الوحدات الأساسية

| التعبير                                            | نوع النتيجة                  | المعنى        |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`, `length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

الصيغة الأصلية متاحة أيضًا: أيّ تعبير غرام-لكل-متر مبنيّ عبر المحرّك العامّ يتحوّل عبر
`toLinearDensity()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true - كلاهما 0.5 kg/m
typed into gramsPerMeter        // 500.0
```

## مثال واقعي: كابل فولاذي على بكرة

كابل فولاذي يزن 2.6 kg/m. ما كتلة طول 45 m منه، وما طول الكابل الذي يسمح به حدّ حمولة 500 kg؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true (علاقة نسيجية)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m" (وحدة أساسية)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات     | Kotlin                         | المعنى                                     |
|---------------|--------------------------------|--------------------------------------------|
| `kg/m`        | `kilogramsPerMeter`            | الكثافة الخطّية، الوحدة الأساسية (رمز مسمّى) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | نفس الكمّية كحاصل ضرب صرف                   |
| `tex`         | `tex`                          | كثافة خطّية نسيجية (1 g/km)                 |
| `ρ_l = m / l` | `mass / length`                | التفكيك المكتوب                            |
| `m = ρ_l · l` | `lineardensity * length`       | محلولة من أجل الكتلة                       |
| `dtex`        | `deci.tex`                     | قراءة نسيجية ببادئة                        |
