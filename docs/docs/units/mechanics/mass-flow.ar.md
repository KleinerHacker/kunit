# معدّل التدفّق الكتلي

الحزمة: `org.pcsoft.framework.kunit.mechanic.massflow`
الوحدة الأساسية: **كيلوغرام لكل ثانية** (`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

النوع: **وحدة مركّبة**

معدّل التدفّق الكتلي `ṁ` هو الكتلة المنقولة في وحدة الزمن — نظير الكتلة لِـ
[التدفّق الحجمي](../kinematics/volume-flow.md). إنّها وحدة **مركّبة** — التركيبة
`mass · time⁻¹` (`kg/s`).

يغلّف `KMassFlowUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) بأس `+1` و`KTimeUnit.BASE` (ثانية) بأس `-1`. ولأنّ مكوّن الكتلة في هذه المكتبة مطبَّع إلى
الغرام، فإنّ القيمة المخزَّنة هي قيمة المكوّن الخام المبنيّ على الغرام، وتُقسَم القراءات بوحدة kg/s على عامل ثابت.

## الوحدات المسمّاة

| الوحدة             | الرمز  |        الرمز البرمجي |     1 وحدة بـ kg/s |
|--------------------|--------|---------------------:|-------------------:|
| كيلوغرام لكل ثانية | `kg/s` | `kilogramsPerSecond` |                1.0 |
| غرام لكل ثانية     | `g/s`  |     `gramsPerSecond` |               1e-3 |
| كيلوغرام لكل ساعة  | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| طن لكل ساعة        | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| رطل لكل ثانية      | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| رطل لكل ساعة       | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

تدعم جميع الوحدات نطاق بادئات النظام الدولي الكامل (`milli.gramsPerSecond` لمضخّات الجرعات).

## التفكيكات

للتدفّق الكتلي تفكيكان متكافئان؛ كلاهما يصبّان في نفس المصنع المُطبِّع.

| الصيغة            | Kotlin                                         | نوع النتيجة             |
|-------------------|------------------------------------------------|-------------------------|
| كتلة / زمن        | `mass / time`                                  | `KMassFlowUnitInstance` |
| كثافة × تدفّق حجمي | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| تعبير أصلي        | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true - كلاهما 2000 kg/s
viaMassTime into kilogramsPerSecond    // 2000.0
```

## الحساب باستخدام الوحدات الأساسية

| التعبير                                        | نوع النتيجة               | المعنى                      |
|------------------------------------------------|---------------------------|-----------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`                 |
| `massflow * time`, `time * massflow`           | `KMassUnitInstance`       | الكتلة المنقولة `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | الزمن اللازم `t = m / ṁ`    |
| `density * volumeflow`, `volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`                 |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`                 |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`                 |

## مثال واقعي: إنتاجية مضخّة

تضخّ مضخّة 15 m³/h من الماء (ρ = 998 kg/m³). ما معدّل التدفّق الكتلي بوحدة t/h، وما مقدار الكتلة المارّة خلال 8 ساعات؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s" (وحدة أساسية)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                          | المعنى                                    |
|-------------|---------------------------------|-------------------------------------------|
| `kg/s`      | `kilogramsPerSecond`            | التدفّق الكتلي، الوحدة الأساسية (رمز مسمّى) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | نفس الكمّية كحاصل ضرب صرف                  |
| `t/h`       | `tonnesPerHour`                 | قراءة إنتاجية صناعية                      |
| `ṁ = m / t` | `mass / time`                   | التفكيك أ                                 |
| `ṁ = ρ · Q` | `density * volumeflow`          | التفكيك ب                                 |
| `Q = ṁ / ρ` | `massflow / density`            | محلولة من أجل التدفّق الحجمي               |
| `mg/s`      | `milli.gramsPerSecond`          | تدفّق كتلي ببادئة                          |
