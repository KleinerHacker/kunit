# الزاوية

الحزمة: `org.pcsoft.framework.kunit.mechanic.angle`
الوحدة الأساسية: **راديان** (`KAngleUnit.BASE == KAngleUnit.RADIAN`)

النوع: **وحدة أصلية**

الزاوية المستوية وحدة **أصلية** في KUnit: كمّية أساسية قابلة للقياس مباشرة ولها مفرداتها الخاصة، وليست تركيبًا. يغلّف
`KAngleUnitInstance` نسخةَ `KMixedUnitInstance` من حدّ واحد فقط هو `KAngleUnit.BASE` عند الأُسّ 1، مطبَّعًا دائمًا إلى
راديان.

الزاوية هي أساس كامل الجزء الدوراني من الميكانيكا:
[السرعة الزاوية](angular-velocity.md)، و[التسارع الزاوي](angular-acceleration.md)، و[الزخم الزاوي](angular-momentum.md)،
و[الزاوية المجسّمة](solid-angle.md) كلّها مبنيّة عليها.

## الوحدات المسمّاة

| الوحدة           | الرمز | الرمز البرمجي |     1 وحدة بـ rad |
|------------------|-------|--------------:|------------------:|
| راديان           | `rad` |     `radians` |               1.0 |
| درجة             | `°`   |     `degrees` | π/180 ≈ 0.0174533 |
| دقيقة قوسية      | `'`   |  `arcminutes` |           π/10800 |
| ثانية قوسية      | `"`   |  `arcseconds` |          π/648000 |
| غراديان (غون)    | `gon` |    `gradians` |             π/200 |
| دورة (لفّة كاملة) | `tr`  |       `turns` |       2π ≈ 6.2832 |

تدعم جميع الوحدات نطاق بادئات SI الكامل (`milli.radians`، `micro.arcseconds` لقياس المواقع الفلكية، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## الحساب بالزوايا

| التعبير                          | نوع النتيجة                    | المعنى                     |
|----------------------------------|--------------------------------|----------------------------|
| `angle + angle`، `angle - angle` | `KAngleUnitInstance`           | حساب من النوع نفسه         |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | زاوية مجسّمة (`rad² = sr`)  |
| `angle / time`                   | `KAngularVelocityUnitInstance` | السرعة الزاوية `ω = φ / t` |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | زمن إتمام دورة             |
| `angle / angle`                  | `KMixedUnitInstance`           | نسبة عديمة الأبعاد         |

الدوال المثلثية متاحة مباشرةً على القيمة، لأنّها تستهلك قراءة الراديان: `angle.sin()`،
`angle.cos()`، `angle.tan()`.

## مثال واقعي: زاوية خرج علبة التروس

يدور عمود محرّك 3 دورات كاملة. زوج تروس بنسبة 5:1 يخفّض ذلك. ما زاوية الخرج بالدرجات، وكم من الوقت تستغرق الحركة عند 600
لفة/دقيقة؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance، 0.6 دورة
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true (مساواة قائمة على القيمة)
(90 of degrees).sin()                       // 1.0
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad" (الوحدة الأساسية)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات       | Kotlin                      | المعنى                            |
|-----------------|-----------------------------|-----------------------------------|
| `rad`           | `radians`                   | الزاوية المستوية، الوحدة الأساسية |
| `°`             | `degrees`                   | درجة                              |
| `mrad`          | `milli.radians`             | زاوية ببادئة (ملّيراديان)          |
| `1 tr = 2π rad` | `(1 of turns) into radians` | الدورة الكاملة بالراديان          |
| `ω = φ / t`     | `angle / time`              | السرعة الزاوية من زاوية           |
| `Ω = φ²`        | `angle * angle`             | زاوية مجسّمة من زاويتين مستويتين   |
