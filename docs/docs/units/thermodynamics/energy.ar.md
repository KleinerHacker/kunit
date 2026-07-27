# الطاقة (ديناميكا حرارية)

الحزمة: `org.pcsoft.framework.kunit.energy`
الوحدة الأساسية: **جول** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

النوع: **وحدة مركّبة**

الطاقة وحدة **مركّبة**: التركيب `mass · length² · time⁻²` (`kg·m²·s⁻²`). يغلّف
`KEnergyUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-2`. ولأن مكوّن الكتلة
في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الجول؛
وتُطبَّع القيمة المخزّنة دائمًا إلى الجول.

الطاقة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها
*الديناميكية الحرارية* — **الحرارة**، `Q = Φ · t`. المجموعة نفسها في Kotlin موثّقة للمجالات الأخرى في
[الطاقة (كهربائية)](../electrical/energy.md) و[الطاقة (ميكانيكا)](../mechanics/energy.md).

## إنشاء طاقة

تُنشأ الطاقة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`). الوحدتان الحراريتان في المجموعة هما السعرة والوحدة الحرارية البريطانية:

| الطاقة | الرمز | الرمز البرمجي | 1 وحدة بالجول |
|---|---|---:|---:|
| جول | `J` | `joules` | 1.0 |
| إرغ (CGS) | `erg` | `ergs` | 1.0e-7 |
| سعرة (كيميائية حرارية) | `cal` | `calories` | 4.184 |
| إلكترون فولت | `eV` | `electronVolts` | 1.602176634e-19 |
| وحدة حرارية بريطانية | `BTU` | `britishThermalUnits` | 1055.05585262 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.calories` — "سعرة الطعام" —
و`kilo.joules`، و`mega.joules`، …).

**الكيلوواط ساعة لا يملك رمزًا خاصًا به** — فهو ليس وحدة مسمّاة أصيلة بل الناتج
`kilo.watts * hours` ويُبنى بهذه الطريقة.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.energy.*

val q = 2000 of kilo.calories   // نظام غذائي يومي
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## تفكيكات متعددة

يمكن الوصول إلى الطاقة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج طاقة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | الحرارة من تدفق حراري عبر زمن `Q = Φ · t` (تبادلي) |
| `power / frequency` | `KEnergyUnitInstance` | صيغة الزمن العكسي (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | الشغل الميكانيكي `W = F · s` (انظر [الطاقة (ميكانيكا)](../mechanics/energy.md)) |
| `charge * voltage` | `KEnergyUnitInstance` | الطاقة الكهربائية `W = Q · U` (انظر [الطاقة (كهربائية)](../electrical/energy.md)) |
| `mass·length²/time²` | عبر `.toEnergy()` | التعبير الأصلي القياسي `kg·m²·s⁻²` |

تُعيد الصيغ المكتوبة بأنواع صريحة طاقة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toEnergy()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين التدفق الحراري والزمن والحرارة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | معدّل التدفق الحراري `Φ = Q / t` (انظر [القدرة (ديناميكا حرارية)](power.md)) |
| `energy / power` | `KTimeUnitInstance` | زمن التسخين `t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.minutes
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

// مثال واقعي - سخّان ماء: تدفق حراري بقدرة 2 kW عبر 10 دقائق يسلّم 1200 kJ من الحرارة.
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// الحرارة محلولًا لإيجاد زمن التسخين لسخّان بقدرة 2 kW:
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance، 600 s

// ومحلولًا لإيجاد معدّل التدفق الحراري:
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance، 2 kW

// نفس الحرارة كتعبير أصلي kg·m²·s⁻²:
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.energy.*

(1 of britishThermalUnits).toString()     // "1055.05585262 J" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J` | `joules` | الطاقة (الحرارة)، الوحدة الأساسية (رمز مسمّى، جول) |
| `Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | الحرارة من معدّل التدفق الحراري والزمن |
| `kcal` | `kilo.calories` | طاقة حرارية ببادئة (سعرة الطعام) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | الطاقة ككتلة·طول² / زمن² (صيغة الكسر) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | نفس الطاقة كحاصل ضرب خالص |
