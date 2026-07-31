# الزخم الزاوي

الحزمة: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
الوحدة الأساسية: **كيلوغرام متر مربّع لكل ثانية**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

النوع: **وحدة مركّبة**

الزخم الزاوي `L` هو النظير الدوراني لِـ[الزخم](momentum.md) والكمّية المحفوظة في الأنظمة الدوّارة. إنّه وحدة
**مركّبة** — التركيب `mass · length² · time⁻¹` (`kg·m²/s`).

يغلّف `KAngularMomentumUnitInstance` نسخةَ `KMixedUnitInstance` من ثلاثة حدود بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) عند `+1`، و`KDistanceUnit.BASE` (متر) عند `+2`، و`KTimeUnit.BASE` (ثانية) عند
`-1`. لا يظهر الراديان في الصيغة القياسية — فهو نسبة عديمة الأبعاد.

!!! note "الفعل هو الكمّية نفسها"
لِـ **الفعل** (الطاقة × الزمن) هذا البُعد بالضبط، ولهذا فإنّ الجول ثانية (`jouleSeconds`، وحدة ثابت بلانك) رمز من رموز
*هذه* المجموعة: `1 J·s = 1 kg·m²/s`.

## الوحدات المسمّاة

| الوحدة                      | الرمز      |                     الرمز البرمجي | 1 وحدة بـ kg·m²/s |
|-----------------------------|------------|----------------------------------:|------------------:|
| كيلوغرام متر مربّع لكل ثانية | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |               1.0 |
| نيوتن متر ثانية             | `N*m*s`    |              `newtonMeterSeconds` |               1.0 |
| جول ثانية                   | `J*s`      |                    `jouleSeconds` |               1.0 |
| غرام سنتيمتر مربّع لكل ثانية | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |              1e-7 |

تدعم جميع الوحدات نطاق بادئات SI الكامل (`femto.jouleSeconds`، `milli.jouleSeconds`).

## التفكيكات

للزخم الزاوي تفكيكان متكافئان؛ وكلاهما يصبّان في المصنع المُطبِّع نفسه.

| الصيغة                             | Kotlin                                                                          | نوع النتيجة                    |
|------------------------------------|---------------------------------------------------------------------------------|--------------------------------|
| عزم القصور الذاتي × السرعة الزاوية | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| الزخم × ذراع الرافعة               | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| التعبير الأصلي                     | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true - كلاهما 6 kg·m²/s
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## الحساب بالوحدات الأساسية

| التعبير                                  | نوع النتيجة                    | المعنى       |
|------------------------------------------|--------------------------------|--------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω`  |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | نفسه، تبادلي |
| `momentum * length`، `length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r`  |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J`  |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω`  |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r`  |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p`  |

## مثال واقعي: متزلّجة فنّية تضمّ ذراعيها

تدور متزلّجة بمعدّل 2 لفة/ث بعزم قصور ذاتي مقداره 4 kg·m². ضمّ الذراعين يخفّضه إلى 1.6 kg·m². بما أنّ الزخم الزاوي
محفوظ، فإنّ المعدّل الجديد ينتج من `ω = L / J`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true (البُعد نفسه)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s" (الوحدة الأساسية)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات   | Kotlin                                           | المعنى                                   |
|-------------|--------------------------------------------------|------------------------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | الزخم الزاوي، الوحدة الأساسية (رمز مسمّى) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | الكمّية نفسها كحاصل ضرب صرف               |
| `J·s`       | `jouleSeconds`                                   | كتابة الفعل للبُعد نفسه                   |
| `L = J · ω` | `inertia * angularvelocity`                      | التفكيك A                                |
| `L = p · r` | `momentum * length`                              | التفكيك B                                |
| `ω = L / J` | `angularmomentum / inertia`                      | محلولة للسرعة الزاوية                    |
| `r = L / p` | `angularmomentum / momentum`                     | محلولة لذراع الرافعة                     |
