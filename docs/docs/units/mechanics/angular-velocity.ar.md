# السرعة الزاوية

الحزمة: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
الوحدة الأساسية: **راديان لكل ثانية** (`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

النوع: **وحدة مركّبة**

السرعة الزاوية `ω` هي النظير الدوراني لِـ[السرعة](../kinematics/speed.md): الزاوية المكتسحة لكلّ وحدة زمن. إنّها وحدة
**مركّبة** — التركيب `angle · time⁻¹` (`rad/s`).

يغلّف `KAngularVelocityUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين بالضبط بالصيغة القياسية:
`KAngleUnit.BASE` (راديان) عند `+1` و`KTimeUnit.BASE` (ثانية) عند `-1`. القيمة مطبَّعة دائمًا إلى rad/s.

## بناء سرعة زاوية

ابنِها من `angle / time`، أو بأحد رموز معدّل الدوران المتعارف عليها. الكتابات المركّبة ببساطة **ليس** لها رموز خاصة بها
عمدًا: `rad/s` هي `radians / seconds` و`°/s` هي `degrees / seconds`. تُطبَّق البادئات على المكوّنات
(`kilo.radians / seconds`)، لذا لا تملك هذه المجموعة معامِلات بادئة خاصة بها.

| الوحدة           | الرمز   |          الرمز البرمجي | 1 وحدة بـ rad/s |
|------------------|---------|-----------------------:|----------------:|
| راديان لكل ثانية | `rad/s` |    `radians / seconds` |             1.0 |
| درجة لكل ثانية   | `°/s`   |    `degrees / seconds` |           π/180 |
| لفّة في الدقيقة   | `rpm`   | `revolutionsPerMinute` | 2π/60 ≈ 0.10472 |
| لفّة في الثانية   | `rps`   | `revolutionsPerSecond` |     2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## الحساب بالوحدات الأساسية (الزاوية والزمن)

| التعبير                     | نوع النتيجة                        | المعنى                                    |
|-----------------------------|------------------------------------|-------------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                               |
| `angularvelocity * time`    | `KAngleUnitInstance`               | الزاوية المكتسحة `φ = ω · t`              |
| `time * angularvelocity`    | `KAngleUnitInstance`               | نفسه، تبادلي                              |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | الزمن المطلوب `t = φ / ω`                 |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [التسارع الزاوي](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [الزخم الزاوي](angular-momentum.md)       |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | القدرة الدورانية، راجع [العزم](torque.md) |

الصيغة الأصلية متاحة أيضًا: أيّ تعبير `angle / time` يُبنى عبر المحرّك العامّ يتحوّل بواسطة
`toAngularVelocity()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true - كلاهما 0.5 rad/s
```

## مثال واقعي: سرعة مغزل

يدور مغزل تفريز بسرعة 12 000 لفة/دقيقة. أيّ مسافة زاوية تقطعها نقطة على محيط الأداة في الثانية، وكم يستغرق دوران كامل؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات   | Kotlin                       | المعنى                                       |
|-------------|------------------------------|----------------------------------------------|
| `rad/s`     | `radians / seconds`          | السرعة الزاوية، الوحدة الأساسية (صيغة الكسر) |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | الكمّية نفسها كحاصل ضرب صرف                   |
| `rpm`       | `revolutionsPerMinute`       | لفّة في الدقيقة (رمز مسمّى)                    |
| `ω = φ / t` | `angle / time`               | التفكيك المحكوم بالنوع                       |
| `φ = ω · t` | `angularvelocity * time`     | محلولة للزاوية                               |
| `t = φ / ω` | `angle / angularvelocity`    | محلولة للزمن                                 |
