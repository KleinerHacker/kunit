# عزم القصور الذاتي

الحزمة: `org.pcsoft.framework.kunit.mechanic.inertia`
الوحدة الأساسية: **كيلوغرام متر مربّع** (`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

النوع: **وحدة مركّبة**

عزم القصور الذاتي `J` هو النظير الدوراني لِـ[الكتلة](mass.md): يُبيّن مدى مقاومة جسم لتغيّر دورانه. إنّه وحدة
**مركّبة** — التركيب `mass · length²` (`kg·m²`).

يغلّف `KInertiaUnitInstance` نسخةَ `KMixedUnitInstance` من حدّين بالضبط بالصيغة القياسية:
`KMassUnit.BASE` (غرام) عند `+1` و`KDistanceUnit.BASE` (متر) عند `+2`. ولأنّ مكوّن الكتلة في هذه المكتبة مُطبَّع إلى
غرامات، فإنّ القيمة المخزّنة هي قيمة المكوّن الخام القائمة على الغرام، والقراءات بـ kg·m² تُقسَم على معامل ثابت.

## الوحدات المسمّاة

| الوحدة            | الرمز     |            الرمز البرمجي | 1 وحدة بـ kg·m² |
|-------------------|-----------|-------------------------:|----------------:|
| كيلوغرام متر مربّع | `kg*m^2`  |  `kilogramMetersSquared` |             1.0 |
| غرام سنتيمتر مربّع | `g*cm^2`  | `gramCentimetersSquared` |            1e-7 |
| رطل-قدم مربّع      | `lb*ft^2` |       `poundFeetSquared` |     ≈ 0.0421401 |

تدعم جميع الوحدات نطاق بادئات SI الكامل (`milli.kilogramMetersSquared` لدوّارات السيرفو الصغيرة).

## التفكيكات

| الصيغة         | Kotlin                                                  | نوع النتيجة            |
|----------------|---------------------------------------------------------|------------------------|
| كتلة × مساحة   | `mass * area`                                           | `KInertiaUnitInstance` |
| التعبير الأصلي | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## الحساب بالوحدات الأساسية

| التعبير                         | نوع النتيجة                    | المعنى                                          |
|---------------------------------|--------------------------------|-------------------------------------------------|
| `mass * area`، `area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                                    |
| `inertia / mass`                | `KAreaUnitInstance`            | مربّع نصف قطر التدوير `r² = J / m`               |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                                    |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [الزخم الزاوي](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [العزم](torque.md) `M = J · α`                  |

## مثال واقعي: دولاب موازنة في مكبس

لدولاب موازنة صلب (`J = ½ · m · r²`) كتلة 40 كغ ونصف قطر 0.3 م. ما عزم قصوره الذاتي، وأيّ زخم زاوي يحمله عند 1500
لفة/دقيقة؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2" (الوحدة الأساسية)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات    | Kotlin                          | المعنى                                        |
|--------------|---------------------------------|-----------------------------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | عزم القصور الذاتي، الوحدة الأساسية (رمز مسمّى) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | الكمّية نفسها كحاصل ضرب صرف                    |
| `J = m · r²` | `mass * area`                   | التفكيك المحكوم بالنوع                        |
| `r² = J / m` | `inertia / mass`                | مربّع نصف قطر التدوير                          |
| `L = J · ω`  | `inertia * angularvelocity`     | الزخم الزاوي                                  |
| `M = J · α`  | `inertia * angularacceleration` | العزم                                         |
