# الصلابة (معامل الزنبرك)

الحزمة: `org.pcsoft.framework.kunit.mechanic.lineforce`
الوحدة الأساسية: **نيوتن لكل متر** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

النوع: **وحدة مركّبة**

الصلابة (معامل الزنبرك) `k = F / s` هي القوّة اللازمة لكلّ وحدة انحراف. بُعدها هو
`mass · time⁻²` (`N/m`) — وهو بالضبط بُعد [التوتّر السطحي](surface-tension.md). تُنمذج KUnit مجموعة محايدة واحدة،
`lineforce`، لكلتا القراءتين؛ والصلابة إحداهما. توثّق هذه الصفحة تلك القراءة.

!!! note "مجموعة واحدة، قراءتان"
`KLineForceUnitInstance` هو النوع المشترك، لذا فإنّ الصلابة والتوتّر السطحي هما نفس الوحدة من منظور KUnit. تحمل المجموعة
الاسم المحايد `lineforce` حتّى لا تدّعي إحدى القراءتين اسم الأخرى. ميّز بينهما بتسمية قيمك.

## الوحدات المسمّاة

| الوحدة           | الرمز    |          الرمز البرمجي | 1 وحدة بـ N/m |
|------------------|----------|-----------------------:|--------------:|
| نيوتن لكل متر    | `N/m`    |      `newtonsPerMeter` |           1.0 |
| نيوتن لكل ملّيمتر | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| كيلوبوند لكل متر | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |
| رطل-قوّة لكل بوصة | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| داين لكل سنتيمتر | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |

جداول بيانات الزنبرك تذكر N/mm؛ والكيلونيوتن لكل متر هو الصيغة ببادئة `kilo.newtonsPerMeter`
وتساوي عدديًا N/mm.

## الحساب باستخدام الوحدات الأساسية

| التعبير                                    | نوع النتيجة              | المعنى                                    |
|--------------------------------------------|--------------------------|-------------------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                               |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | قوّة الزنبرك `F = k · s`                   |
| `force / lineforce`                        | `KLengthUnitInstance`    | الانحراف `s = F / k`                      |
| `energy / area`                            | `KLineForceUnitInstance` | قراءة [التوتّر السطحي](surface-tension.md) |

تتحوّل الصيغة الأصلية عبر `toLineForce()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - كلاهما 1 N/m
typed into newtonsPerMeter // 1.0
```

## مثال واقعي: زنبرك لولبي في نظام تعليق

زنبرك لولبي مصنَّف بـ 40 N/mm. كم ينضغط تحت حمل عجلة قدره 2000 N، وأيّ قوّة ينتجها انحراف قدره 15 mm؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// الزنبركات المتوازية تُجمَع ببساطة
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m" (وحدة أساسية)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                          | المعنى                       |
|-------------|---------------------------------|------------------------------|
| `N/m`       | `newtonsPerMeter`               | الصلابة، الوحدة الأساسية     |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | نفس الكمّية بالأبعاد الأساسية |
| `N/mm`      | `newtonsPerMillimeter`          | قراءة جداول بيانات الزنبرك   |
| `k = F / s` | `force / length`                | التفكيك المكتوب              |
| `F = k · s` | `lineforce * length`            | قوّة الزنبرك                  |
| `s = F / k` | `force / lineforce`             | الانحراف                     |
