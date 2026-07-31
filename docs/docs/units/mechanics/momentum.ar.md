# الزخم

الحزمة: `org.pcsoft.framework.kunit.mechanic.momentum`
الوحدة الأساسية: **كيلوغرام متر لكل ثانية**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

النوع: **وحدة مركّبة**

الزخم `p = m · v` هو "كمّية الحركة" لجسم ما. إنّه وحدة **مركّبة** — التركيب `mass · length · time⁻¹`
(`kg·m/s`).

يغلّف `KMomentumUnitInstance` نسخةَ `KMixedUnitInstance` من ثلاثة حدود بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) عند `+1`، و`KDistanceUnit.BASE` (متر) عند `+1`، و`KTimeUnit.BASE` (ثانية) عند
`-1`. ولأنّ مكوّن الكتلة في هذه المكتبة مُطبَّع إلى غرامات، فإنّ القيمة المخزّنة هي قيمة المكوّن الخام القائمة على
الغرام، والقراءات بـ kg·m/s تُقسَم على معامل ثابت.

!!! note "الدفع هو الكمّية نفسها"
لـ **الدفع** `F · t` هذا البُعد بالضبط (`1 N·s = 1 kg·m/s`)، لذا فهو *نفس* هذه المجموعة وليس مجموعة قائمة بذاتها — راجع
صفحة [الدفع](impulse.md).

## الوحدات المسمّاة

| الوحدة                 | الرمز     |              الرمز البرمجي | 1 وحدة بـ kg·m/s |
|------------------------|-----------|---------------------------:|-----------------:|
| كيلوغرام متر لكل ثانية | `kg*m/s`  |  `kilogramMetersPerSecond` |              1.0 |
| نيوتن ثانية            | `N*s`     |            `newtonSeconds` |              1.0 |
| غرام سنتيمتر لكل ثانية | `g*cm/s`  | `gramCentimetersPerSecond` |             1e-5 |
| رطل-قدم لكل ثانية      | `lb*ft/s` |       `poundFeetPerSecond` |       ≈ 0.138255 |

تدعم جميع الوحدات نطاق بادئات SI الكامل (`kilo.newtonSeconds`، `milli.kilogramMetersPerSecond`).

## التفكيكات

للزخم تفكيكان متكافئان؛ يصبّان جميعًا في المصنع المُطبِّع نفسه ويُنتجان بالتالي نفس النتيجة المحكومة بالنوع والمتساوية
القيمة.

| الصيغة            | Kotlin                                                           | نوع النتيجة             |
|-------------------|------------------------------------------------------------------|-------------------------|
| كتلة × سرعة       | `mass * speed`                                                   | `KMomentumUnitInstance` |
| قوّة × زمن (الدفع) | `force * time`                                                   | `KMomentumUnitInstance` |
| التعبير الأصلي    | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## الحساب بالوحدات الأساسية

| التعبير                        | نوع النتيجة                    | المعنى                              |
|--------------------------------|--------------------------------|-------------------------------------|
| `mass * speed`، `speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                         |
| `force * time`، `time * force` | `KMomentumUnitInstance`        | الدفع `p = F · t`                   |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                         |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                         |
| `momentum / time`              | `KForceUnitInstance`           | متوسّط القوة `F = p / t`             |
| `momentum / force`             | `KTimeUnitInstance`            | زمن التأثير `t = p / F`             |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [الزخم الزاوي](angular-momentum.md) |

## مثال واقعي: كبح سيارة

تسير سيارة كتلتها 1200 كغ بسرعة 20 م/ث. ما زخمها، وأيّ قوّة ثابتة توقفها خلال 5 ثوانٍ؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true (البُعد نفسه)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s" (الوحدة الأساسية)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات   | Kotlin                                   | المعنى                            |
|-------------|------------------------------------------|-----------------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | الزخم، الوحدة الأساسية (رمز مسمّى) |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | الكمّية نفسها كحاصل ضرب صرف        |
| `N·s`       | `newtonSeconds`                          | كتابة الدفع للبُعد نفسه            |
| `p = m · v` | `mass * speed`                           | التفكيك A                         |
| `p = F · t` | `force * time`                           | التفكيك B (الدفع)                 |
| `v = p / m` | `momentum / mass`                        | محلولة للسرعة                     |
| `F = p / t` | `momentum / time`                        | محلولة لمتوسّط القوة               |
