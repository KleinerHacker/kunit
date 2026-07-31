# عزم الدوران

الحزمة: `org.pcsoft.framework.kunit.common.energy`
الوحدة الأساسية: **جول** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)، وتُقرأ **نيوتن متر** (`N·m`)

النوع: **وحدة مركّبة**

عزم الدوران `M = F · r` هو الأثر الدوراني لقوّة تعمل على ذراع رافعة. من الناحية البعدية فهو *يمثّل*
[طاقة](energy.md): `1 N·m = 1 J`. لذا لا تُقدّم KUnit مجموعة وحدات ثانية له — عزم الدوران هو **قراءة**
لمجموعة الطاقة. توثّق هذه الصفحة تلك القراءة؛ أمّا المجموعة نفسها فمُوثّقة في صفحة
[الطاقة (الميكانيكا)](energy.md).

!!! note "نفس البُعد، فيزياء مختلفة"
عزم الدوران والشغل مختلفان فيزيائيًا (عزم الدوران متجه محوري، والشغل كمّية قياسية)، لكنّهما يتشاركان البُعد `kg·m²·s⁻²`
بالضبط. ولأنّ KUnit تُمثّل *الوحدات* لا الطابع المتجهي، يعيش كلاهما في مجموعة واحدة. للتمييز بينهما استخدم التسمية:
`val torque = (100 of newtons) * (2 of meters)` تُقرأ N·m،
`val work = force * distance` على طول المسار تُقرأ J.

## بناء عزم الدوران

| التعبير                            | نوع النتيجة                        | المعنى                             |
|------------------------------------|------------------------------------|------------------------------------|
| `force * length`, `length * force` | `KEnergyUnitInstance`              | `M = F · r` (ذراع الرافعة)         |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α` (نيوتن الدوراني)       |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω` (صيغة نظام نقل الحركة) |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`                        |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`                        |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`                        |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`                        |

تصبّ صيغ البناء الثلاث جميعها في مصنع واحد لمجموعة الطاقة، لذا فهي متساوية القيمة:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## الوحدات المسمّاة

يستخدم عزم الدوران مفردات مجموعة الطاقة؛ `newtons * meters` هي الكتابة المألوفة لـ N·m، وتأتي القراءات ذات البادئات من
رموز الطاقة (`kilo.joules` = kN·m).

| الوحدة          | الرمز  | Kotlin                           |
|-----------------|--------|----------------------------------|
| نيوتن متر       | `N*m`  | `(1 of newtons) * (1 of meters)` |
| جول (نفس البُعد) | `J`    | `joules`                         |
| كيلونيوتن متر   | `kN*m` | `kilo.joules`                    |

## مثال واقعي: عزم دوران وقدرة محرّك

يُنتج محرّك 62.83 kW عند 3000 دورة/دقيقة. ما عزم الدوران المقابل؟ وما القدرة الناتجة إذا حُوفظ على نفس عزم الدوران عند
6000 دورة/دقيقة؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0 (N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J" (وحدة أساسية للمجموعة)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                                           | المعنى                         |
|-------------|--------------------------------------------------|--------------------------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | عزم الدوران، صيغة ذراع الرافعة |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | نفس الكمّية بالأبعاد الأساسية   |
| `M = F · r` | `force * length`                                 | التفكيك أ                      |
| `M = J · α` | `inertia * angularacceleration`                  | التفكيك ب                      |
| `M = P / ω` | `power / angularvelocity`                        | التفكيك ج (نظام نقل الحركة)    |
| `P = M · ω` | `torque * angularvelocity`                       | القدرة الدورانية               |
| `kN·m`      | `kilo.joules`                                    | قراءة عزم دوران ببادئة         |
