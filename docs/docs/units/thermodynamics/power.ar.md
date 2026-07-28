# القدرة (ديناميكا حرارية)

الحزمة: `org.pcsoft.framework.kunit.common.power`
الوحدة الأساسية: **واط** (`KPowerUnit.BASE == KPowerUnit.WATT`)

النوع: **وحدة مركّبة**

القدرة وحدة **مركّبة**: التركيب `mass · length² · time⁻³` (`kg·m²·s⁻³`). يغلّف
`KPowerUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-3`. ولأن مكوّن الكتلة
في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الواط؛
وتُطبَّع القيمة المخزّنة دائمًا إلى الواط.

القدرة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها
*الديناميكية الحرارية* — **معدّل التدفق الحراري** `Φ = Q / t`، أي طاقة حرارية لكل زمن. المجموعة نفسها
في Kotlin موثّقة للمجالات الأخرى في [القدرة (كهربائية)](../electrical/power.md) و
[القدرة (ميكانيكا)](../mechanics/power.md).

## إنشاء قدرة

تُنشأ القدرة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| القدرة | الرمز | الرمز البرمجي | 1 وحدة بالواط |
|---|---|---:|---:|
| واط | `W` | `watts` | 1.0 |
| حصان بخاري متري | `PS` | `metricHorsePowers` | 735.49875 |
| حصان بخاري ميكانيكي | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| إرغ لكل ثانية (CGS) | `erg/s` | `ergsPerSecond` | 1.0e-7 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.watts`، `mega.watts`،
`milli.watts`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val heatFlow = 9 of kilo.watts   // سخّان غرفة
heatFlow into kilo.watts         // 9.0
heatFlow into watts              // 9000.0
```

## تفكيكات متعددة

يمكن الوصول إلى القدرة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج قدرة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | معدّل التدفق الحراري `Φ = Q / t` (انظر [الطاقة (ديناميكا حرارية)](energy.md)) |
| `voltage * current` | `KPowerUnitInstance` | القدرة الكهربائية `P = U · I` (انظر [القدرة (كهربائية)](../electrical/power.md)) |
| `force * speed` | `KPowerUnitInstance` | القدرة الميكانيكية `P = F · v` (انظر [القدرة (ميكانيكا)](../mechanics/power.md)) |
| `mass·length²/time³` | عبر `.toPower()` | التعبير الأصلي القياسي `kg·m²·s⁻³` |

تُعيد الصيغ المكتوبة بأنواع صريحة قدرة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toPower()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية لصيغة التدفق الحراري بين الطاقة والزمن والقدرة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | الحرارة المسلَّمة، `Q = Φ · t` (تبادلي) |
| `energy / power` | `KTimeUnitInstance` | الزمن اللازم، `t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.*

// مثال واقعي - سخّان ماء: تسليم 1200 kJ من الحرارة خلال 10 دقائق هو تدفق حراري مقداره 2 kW.
val heatFlow = (1200 of kilo.joules) / (10 of minutes)   // KPowerUnitInstance
heatFlow into kilo.watts                                 // 2.0

// التدفق الحراري محلولًا لإيجاد الحرارة المسلَّمة خلال ساعة واحدة:
val heat = (2 of kilo.watts) * (60 of minutes)           // KEnergyUnitInstance، 7.2 MJ

// نفس التدفق الحراري كتعبير أصلي kg·m²·s⁻³:
val raw = 2000 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2 of kilo.watts)                       // true
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
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

(9 of kilo.watts).toString()     // "9000.0 W" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻³`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `W` | `watts` | القدرة (معدّل التدفق الحراري)، الوحدة الأساسية (رمز مسمّى، واط) |
| `Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | معدّل التدفق الحراري من الحرارة والزمن |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | القدرة ككتلة·طول² / زمن³ (صيغة الكسر) |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | نفس القدرة كحاصل ضرب خالص |
| `kW` | `kilo.watts` | قدرة ببادئة (كيلوواط) |
