# الطاقة (ميكانيكا)

الحزمة: `org.pcsoft.framework.kunit.common.energy`
الوحدة الأساسية: **جول** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

النوع: **وحدة مركّبة**

الطاقة وحدة **مركّبة**: التركيب `mass · length² · time⁻²` (`kg·m²·s⁻²`). يغلّف
`KEnergyUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-2`. ولأن مكوّن الكتلة
في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الجول؛
وتُطبَّع القيمة المخزّنة دائمًا إلى الجول.

الطاقة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها
*الميكانيكية* — **الشغل**، `W = F · s`. المجموعة نفسها في Kotlin موثّقة للمجالات الأخرى في
[الطاقة (كهربائية)](../electrical/energy.md) و[الطاقة (ديناميكا حرارية)](../thermodynamics/energy.md).

## إنشاء طاقة

تُنشأ الطاقة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| الطاقة | الرمز | الرمز البرمجي | 1 وحدة بالجول |
|---|---|---:|---:|
| جول | `J` | `joules` | 1.0 |
| إرغ (CGS) | `erg` | `ergs` | 1.0e-7 |
| سعرة (كيميائية حرارية) | `cal` | `calories` | 4.184 |
| إلكترون فولت | `eV` | `electronVolts` | 1.602176634e-19 |
| وحدة حرارية بريطانية | `BTU` | `britishThermalUnits` | 1055.05585262 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.joules`، `mega.joules`،
`kilo.calories`، …).

**الكيلوواط ساعة لا يملك رمزًا خاصًا به** — فهو ليس وحدة مسمّاة أصيلة بل الناتج
`kilo.watts * hours` ويُبنى بهذه الطريقة.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val w = 500 of joules
w into joules                   // 500.0
w into calories                 // 119.502868...
(1 of kilo.joules) into joules  // 1000.0
```

## تفكيكات متعددة

يمكن الوصول إلى الطاقة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج طاقة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `force * length` | `KEnergyUnitInstance` | الشغل الميكانيكي `W = F · s` (تبادلي) |
| `power * time` | `KEnergyUnitInstance` | الشغل من قدرة عبر زمن `W = P · t` (تبادلي) |
| `power / frequency` | `KEnergyUnitInstance` | صيغة الزمن العكسي (`W/Hz = W·s`) |
| `charge * voltage` | `KEnergyUnitInstance` | الطاقة الكهربائية `W = Q · U` (انظر [الطاقة (كهربائية)](../electrical/energy.md)) |
| `mass·length²/time²` | عبر `.toEnergy()` | التعبير الأصلي القياسي `kg·m²·s⁻²` |

تُعيد الصيغ المكتوبة بأنواع صريحة طاقة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toEnergy()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين القدرة والزمن والطاقة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | `P = W / t` (انظر [القدرة (ميكانيكا)](power.md)) |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// مثال واقعي - شغل الرفع: السحب بقوة 100 N عبر مسافة 5 m يعطي 500 J من الشغل.
val w = (100 of newtons) * (5 of meters)   // KEnergyUnitInstance
w into joules                              // 500.0

// الشغل محلولًا لإيجاد القدرة اللازمة لإنجازه خلال 5 s:
val p = (500 of joules) / (5 of seconds)   // KPowerUnitInstance، 100 W

// ومحلولًا لإيجاد الزمن الذي يحتاجه محرك بقدرة 100 W لإنجاز هذا الشغل:
val t = (500 of joules) / (100 of watts)   // KTimeUnitInstance، 5 s

// نفس الشغل كتعبير أصلي kg·m²·s⁻²:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)          // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

(1 of calories).toString()     // "4.184 J" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J` | `joules` | الطاقة (الشغل)، الوحدة الأساسية (رمز مسمّى، جول) |
| `F · s` | `(100 of newtons) * (5 of meters)` | الشغل الميكانيكي من القوة والطول |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | الطاقة ككتلة·طول² / زمن² (صيغة الكسر) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | نفس الطاقة كحاصل ضرب خالص |
| `kJ` | `kilo.joules` | طاقة ببادئة (كيلوجول) |
