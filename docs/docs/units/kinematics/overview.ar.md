# علم الحركة — نظرة عامة

الحزم: `org.pcsoft.framework.kunit.distance`، `…time`، `…speed`، `…acceleration`، `…frequency`

علم الحركة هو وصف **الحركة** — إلى أي مدى، ولأي مدة، وبأي سرعة، وكيف يتغيّر معدّل الحركة نفسه — دون
السؤال بعد عن القوى الكامنة وراءها (ذلك موضوع [الميكانيكا](../mechanics/overview.md)). يُنمذج KUnit هذا
المجال بكمّيتين أساسيتين **أصليتين** وثلاث كمّيات **مركّبة** منهما، فتتحوّل صيغ الحركة الكلاسيكية إلى
تعبيرات `*` و`/` عادية تظلّ محكومة بالنوع بقوة.

## وحدات هذا الموضوع

| الوحدة | النوع | الوحدة الأساسية | الصفحة |
|---|---|---|---|
| المسافة | أصلية | متر (`m`) | [المسافة](distance.md) |
| الزمن | أصلية | ثانية (`s`) | [الزمن](time.md) |
| التردد | أصلية | هرتز (`Hz`) | [التردد](frequency.md) |
| السرعة | مركّبة | متر لكل ثانية (`m/s`) | [السرعة](speed.md) |
| التسارع | مركّبة | متر لكل ثانية² (`m/s²`) | [التسارع](acceleration.md) |

## كيف ترتبط الكمّيات

السرعة هي المسافة مقسومة على الزمن، والتسارع هو السرعة مقسومة على الزمن، والتردد هو مقلوب الزمن. يُعيد
KUnit الكمّية **المحكومة بالنوع** الصحيحة لكل تركيب — دون أن تُجمّع وحدة مختلطة خامًا بيدك:

| التعبير | النتيجة | الصيغة |
|---|---|---|
| `distance / time` | السرعة | `v = s / t` |
| `speed * time` | المسافة | `s = v · t` |
| `speed / time` | التسارع | `a = Δv / t` |
| `acceleration * time` | السرعة | `v = a · t` |
| `distance * frequency` | السرعة | `v = s · f` |

## مثال واقعي — متوسط سرعة رحلة

تقطع سيارة **120 km** في **1.5 h**. متوسط سرعتها هو `v = s / t`، وضرب هذه السرعة في مدة يعطي المسافة
المقطوعة من جديد:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0 (km/h)
v.value                                          // ≈ 22.22 (m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0 (km خلال 3 h)
```

## مثال واقعي — تسارع عدّاء

يبلغ عدّاء **10 m/s** انطلاقًا من السكون في **2 s**. التسارع هو `a = Δv / t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance، 5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance، 10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51 (نسبة من g)
```

## طباعة قيمة (`toString`)

تُخرج `toString()` القيمة بالوحدة **الأساسية** لمجموعتها (القيمة + الرمز)؛ ولأي وحدة أخرى، اقرأها بـ
`into` داخل قالب نصّي وأضِف الرمز بنفسك:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s" (الوحدة الأساسية)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## الترميز

يعرض الجدول التالي العلاقات الأساسية لهذا المجال بالترميز الرياضي مقابل ترميز Kotlin في KUnit. تُكتب
الأُسّس بحروف Unicode المرتفعة (`²`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | السرعة من المسافة ÷ الزمن |
| `s = v · t` | `v * (3 of hours)` | المسافة من السرعة × الزمن |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | التسارع من السرعة ÷ الزمن |
| `v = a · t` | `a * (2 of seconds)` | السرعة من التسارع × الزمن |
| `f = 1 / T` | `1 / (2 of hertz)` | الدور ↔ التردد (مقلوب الزمن) |

## إلى أين بعد ذلك

* [المسافة](distance.md) — الطول والمساحة والحجم في مجموعة واحدة.
* [الزمن](time.md) — المدد المبنية على `Duration`.
* [السرعة](speed.md) و[التسارع](acceleration.md) — معدّلات الحركة المركّبة.
* [التردد](frequency.md) — مقلوب الزمن ومعاملاته المتقاطعة.
