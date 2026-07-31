# التدفق الحجمي

الحزمة: `org.pcsoft.framework.kunit.kinematic.volumeflow`
الوحدة الأساسية: **متر مكعب لكل ثانية** (`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`)

النوع: **وحدة مركّبة**

يصف التدفق الحجمي (معدّل تدفّق الحجم) كمّية الحجم التي تعبر مقطعًا عرضيًا لكل وحدة زمن:
`distance³ · time⁻¹` (`m³/s`). يغلّف `KVolumeFlowUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط — واحد
`KDistanceUnit.BASE` (متر) بالأس `+3`، وآخر `KTimeUnit.BASE` (ثانية) بالأس `-1`. تُطبَّع القيمة دائمًا إلى متر مكعب لكل
ثانية، بصرف النظر عن الوحدة أو تركيبة الحجم/الزمن التي أُنشئت منها.

خلافًا للطاقة أو القدرة، لا يملك التدفق الحجمي **أي** بُعد كتلة، لذا فإن القيمة المخزَّنة *هي* القراءة بـ`m³/s` — دون
وجود أي جسر غرام/كيلوغرام.

## الوحدات المسمّاة

| الوحدة                 | الرمز   |          الرمز البرمجي |      1 وحدة بـ m³/s |
|------------------------|---------|-----------------------:|--------------------:|
| متر مكعب لكل ثانية     | `m³/s`  | `cubicMetersPerSecond` |                 1.0 |
| متر مكعب لكل ساعة      | `m³/h`  |   `cubicMetersPerHour` |   1/3600 ≈ 2.778e-4 |
| لتر لكل ثانية          | `l/s`   |      `litersPerSecond` |               0.001 |
| لتر لكل دقيقة          | `l/min` |      `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| غالون أمريكي لكل دقيقة | `gpm`   |   `usGallonsPerMinute` |          ≈ 6.309e-5 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل أيضًا (`milli.litersPerSecond`،
`kilo.cubicMetersPerHour`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005 (normalized to m³/s)
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## مثال واقعي: ملء خزّان مياه أمطار

تُضخّ مضخّة حديقة 300 l/min في خزّان سعة 5 m³. كم من الوقت يستغرق ملء الخزّان، وما معدّل التدفّق بالوحدات التي تستخدمها
بطاقة بيانات المضخّة؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67 minutes

pump into cubicMetersPerHour        // 18.0 m³/h (datasheet unit)
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// The other direction: how much water in a quarter of an hour?
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## الحساب باستخدام الوحدات الأساسية (الحجم والزمن)

| التعبير               | نوع النتيجة               | المعنى                      |
|-----------------------|---------------------------|-----------------------------|
| `volume / time`       | `KVolumeFlowUnitInstance` | معدّل التدفّق = الحجم / المدّة |
| `volumeFlow * time`   | `KVolumeUnitInstance`     | الحجم = معدّل التدفّق × المدّة |
| `time * volumeFlow`   | `KVolumeUnitInstance`     | الحجم (تبادلي)              |
| `volume / volumeFlow` | `KTimeUnitInstance`       | المدّة = الحجم / معدّل التدفّق |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## التفكيكات

يمكن الوصول إلى التدفق الحجمي بطريقتين؛ وكلتاهما تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك              | الصيغة                        | النتيجة                          |
|----------------------|-------------------------------|----------------------------------|
| `volume / time`      | معامل مكتوب بنوع صريح         | `KVolumeFlowUnitInstance` مباشرة |
| `distance³ · time⁻¹` | تعبير أصلي + `toVolumeFlow()` | `KVolumeFlowUnitInstance`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// typed operator form
val typed = (8000 of liters) / (4 of seconds)

// native base-dimension form (m³ · s⁻¹), recognised by toVolumeFlow()
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true - both are 2.0 m³/s
```

تتعرّف `toVolumeFlow()` على **الصيغة القياسية فقط** (حدّ واحد من `KDistanceUnit` بالأس `+3` وحدّ واحد من `KTimeUnit`
بالأس `-1`)؛ وأي تعبير مكافئ يُختزَل إليها تلقائيًا. أما الشكل الخاطئ فيرمي
`IllegalStateException` بدلًا من إعادة قيمة خاطئة بصمت.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - : same group, automatic conversion between different flow units
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// comparisons (by normalized m³/s value)
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// * / / between two flows escape to a KMixedUnitInstance
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## تنسيق toString

تُخرِج `toString()` القيمة بالوحدة الأساسية؛ استخدم `into` لأي وحدة أخرى:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                             | المعنى                                    |
|-------------|------------------------------------|-------------------------------------------|
| `m³/s`      | `cubicMetersPerSecond`             | التدفق الحجمي، الوحدة الأساسية — رمز مسمّى |
| `m³·s⁻¹`    | `(meters pow 3) / seconds`         | نفس التدفق كتعبير أبعاد أساسية            |
| `l/s`       | `litersPerSecond`                  | لتر لكل ثانية                             |
| `l/min`     | `litersPerMinute`                  | لتر لكل دقيقة                             |
| `m³/h`      | `cubicMetersPerHour`               | متر مكعب لكل ساعة                         |
| `V / t`     | `(600 of liters) / (2 of minutes)` | البناء من الحجم ÷ الزمن                   |
| `V = q̇ · t` | `q * (60 of seconds)`              | الحجم من معدّل التدفّق × المدّة              |
| `t = V / q̇` | `(600 of liters) / q`              | المدّة من الحجم ÷ معدّل التدفّق              |
