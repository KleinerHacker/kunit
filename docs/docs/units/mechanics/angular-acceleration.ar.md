# التسارع الزاوي

الحزمة: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
الوحدة الأساسية: **راديان لكل ثانية مربّعة**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

النوع: **وحدة مركّبة**

التسارع الزاوي `α` هو النظير الدوراني لِـ[التسارع](../kinematics/acceleration.md): تغيّر
[السرعة الزاوية](angular-velocity.md) لكلّ وحدة زمن. إنّها وحدة **مركّبة** — التركيب `angle · time⁻²`
(`rad/s²`).

يغلّف `KAngularAccelerationUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين بالضبط بالصيغة القياسية:
`KAngleUnit.BASE` (راديان) عند `+1` و`KTimeUnit.BASE` (ثانية) عند `-2`. القيمة مطبَّعة دائمًا إلى rad/s².

## الوحدات المسمّاة

| الوحدة                   | الرمز     |                   الرمز البرمجي | 1 وحدة بـ rad/s² |
|--------------------------|-----------|--------------------------------:|-----------------:|
| راديان لكل ثانية مربّعة   | `rad/s^2` |       `radiansPerSecondSquared` |              1.0 |
| درجة لكل ثانية مربّعة     | `°/s^2`   |       `degreesPerSecondSquared` |            π/180 |
| لفّة لكل ثانية مربّعة      | `rps^2`   |   `revolutionsPerSecondSquared` |               2π |
| لفّة في الدقيقة لكل ثانية | `rpm/s`   | `revolutionsPerMinutePerSecond` |            2π/60 |

تُطبَّق البادئات على المكوّنات (`kilo.radians / (seconds pow 2)`)، لذا لا تملك هذه المجموعة معامِلات بادئة خاصة بها.

## التفكيكات

للتسارع الزاوي تفكيكان متكافئان؛ وكلاهما يُختزلان إلى القيمة القياسية نفسها.

| الصيغة                 | Kotlin                                                             | نوع النتيجة                        |
|------------------------|--------------------------------------------------------------------|------------------------------------|
| المعامل المحكوم بالنوع | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| التعبير الأصلي         | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true - كلاهما 2 rad/s²
typed into radiansPerSecondSquared     // 2.0
```

## الحساب بالوحدات الأساسية

| التعبير                                 | نوع النتيجة                        | المعنى                                     |
|-----------------------------------------|------------------------------------|--------------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                                |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | السرعة المكتسبة `ω = α · t`                |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | نفسه، تبادلي                               |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | زمن الوصول `t = ω / α`                     |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | العزم `M = J · α`، راجع [العزم](torque.md) |

## مثال واقعي: تسارع محرّك

يبلغ محرّك سيرفو سرعة 3000 لفة/دقيقة خلال 0.4 ثانية. ما تسارعه الزاوي، وأيّ زاوية يدورها خلال 0.2 ثانية من التسارع بدءًا
من السكون؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات   | Kotlin                                                                  | المعنى                                     |
|-------------|-------------------------------------------------------------------------|--------------------------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | التسارع الزاوي، الوحدة الأساسية (رمز مسمّى) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | الكمّية نفسها كحاصل ضرب صرف                 |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | التفكيك الأصلي                             |
| `α = ω / t` | `angularvelocity / time`                                                | التفكيك المحكوم بالنوع                     |
| `ω = α · t` | `angularacceleration * time`                                            | محلولة للسرعة الزاوية                      |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | معدّل تسارع آلي                             |
