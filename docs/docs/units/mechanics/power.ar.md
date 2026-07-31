# القدرة (ميكانيكا)

الحزمة: `org.pcsoft.framework.kunit.common.power`
الوحدة الأساسية: **واط** (`KPowerUnit.BASE == KPowerUnit.WATT`)

النوع: **وحدة مركّبة**

القدرة وحدة **مركّبة**: التركيب `mass · length² · time⁻³` (`kg·m²·s⁻³`). يغلّف
`KPowerUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-3`. ولأن مكوّن الكتلة في المكتبة مطبَّع
إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الواط؛ وتُطبَّع القيمة المخزّنة دائمًا إلى
الواط.

القدرة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها *الميكانيكية*
(`P = F · v`). المجموعة نفسها في Kotlin موثّقة للمجالات الأخرى في
[القدرة (كهربائية)](../electrical/power.md) و[القدرة (ديناميكا حرارية)](../thermodynamics/power.md).

## إنشاء قدرة

تُنشأ القدرة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| القدرة              | الرمز   |           الرمز البرمجي |     1 وحدة بالواط |
|---------------------|---------|------------------------:|------------------:|
| واط                 | `W`     |                 `watts` |               1.0 |
| حصان بخاري متري     | `PS`    |     `metricHorsePowers` |         735.49875 |
| حصان بخاري ميكانيكي | `hp`    | `mechanicalHorsePowers` | 745.6998715822702 |
| إرغ لكل ثانية (CGS) | `erg/s` |         `ergsPerSecond` |            1.0e-7 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.watts`، `mega.watts`،
`milli.watts`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 100 of metricHorsePowers
p into kilo.watts               // 73.549875
p into mechanicalHorsePowers    // 98.63200706...
```

## تفكيكات متعددة

يمكن الوصول إلى القدرة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج قدرة متساوية القيمة:

| التعبير              | نوع النتيجة          | المعنى                                                                           |
|----------------------|----------------------|----------------------------------------------------------------------------------|
| `force * speed`      | `KPowerUnitInstance` | القدرة الميكانيكية `P = F · v` (تبادلي)                                          |
| `voltage * current`  | `KPowerUnitInstance` | القدرة الكهربائية `P = U · I` (انظر [القدرة (كهربائية)](../electrical/power.md)) |
| `energy / time`      | `KPowerUnitInstance` | `P = W / t` (انظر [الطاقة (ميكانيكا)](energy.md))                                |
| `mass·length²/time³` | عبر `.toPower()`     | التعبير الأصلي القياسي `kg·m²·s⁻³`                                               |

تُعيد الصيغ المكتوبة بأنواع صريحة قدرة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toPower()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية للصيغة الميكانيكية بين القوة والسرعة والقدرة:

| التعبير         | نوع النتيجة          | المعنى      |
|-----------------|----------------------|-------------|
| `power / force` | `KSpeedUnitInstance` | `v = P / F` |
| `power / speed` | `KForceUnitInstance` | `F = P / v` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*

// مثال واقعي - رافعة شحن: سحب بقوة 100 N عند سرعة 5 m/s يتطلب 500 W.
val p = (100 of newtons) * ((5 of meters) / (1 of seconds))  // KPowerUnitInstance
p into watts                                                 // 500.0

// التعريف محلولًا لإيجاد قوة السحب عند سرعة معطاة:
val f = (500 of watts) / ((5 of meters) / (1 of seconds))     // KForceUnitInstance، 100 N

// ومحلولًا لإيجاد السرعة الممكنة عند قوة معطاة:
val v = (500 of watts) / (100 of newtons)                     // KSpeedUnitInstance، 5 m/s

// نفس القدرة كتعبير أصلي kg·m²·s⁻³:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (500 of watts)                               // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

(1 of metricHorsePowers).toString()     // "735.49875 W" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `⁻³`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر
الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                                                | المعنى                                  |
|-------------|-------------------------------------------------------|-----------------------------------------|
| `W`         | `watts`                                               | القدرة، الوحدة الأساسية (رمز مسمّى، واط) |
| `F · v`     | `(100 of newtons) * ((5 of meters) / (1 of seconds))` | القدرة الميكانيكية من القوة والسرعة     |
| `kg·m²/s³`  | `(kilo.grams * (meters pow 2)) / (seconds pow 3)`     | القدرة ككتلة·طول² / زمن³ (صيغة الكسر)   |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)`      | نفس القدرة كحاصل ضرب خالص               |
| `PS`        | `metricHorsePowers`                                   | حصان بخاري متري (رمز مسمّى)              |
