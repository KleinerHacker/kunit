# الطاقة (كهربائية)

الحزمة: `org.pcsoft.framework.kunit.energy`
الوحدة الأساسية: **جول** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

النوع: **وحدة مركّبة**

الطاقة وحدة **مركّبة**: التركيب `mass · length² · time⁻²` (`kg·m²·s⁻²`). يغلّف
`KEnergyUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-2`. ولأن مكوّن الكتلة
في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الجول؛
وتُطبَّع القيمة المخزّنة دائمًا إلى الجول.

الطاقة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها
*الكهربائية* (`W = Q · U`، و`W = P · t` للطاقة الكهربائية المستهلَكة). المجموعة نفسها في Kotlin موثّقة
للمجالات الأخرى في [الطاقة (ميكانيكا)](../mechanics/energy.md) و
[الطاقة (ديناميكا حرارية)](../thermodynamics/energy.md).

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
`mega.electronVolts`، …).

**الكيلوواط ساعة لا يملك رمزًا خاصًا به** — فهو ليس وحدة مسمّاة أصيلة بل الناتج
`kilo.watts * hours` ويُبنى بهذه الطريقة.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

val w = 500 of kilo.joules
w into kilo.joules                          // 500.0
w into joules                               // 500000.0

val kwh = (1 of kilo.watts) * (1 of hours)  // 1 kWh = 3.6 MJ
kwh into kilo.joules                        // 3600.0
```

## تفكيكات متعددة

يمكن الوصول إلى الطاقة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج طاقة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `charge * voltage` | `KEnergyUnitInstance` | الطاقة الكهربائية `W = Q · U` (تبادلي) |
| `power * time` | `KEnergyUnitInstance` | الطاقة المستهلَكة `W = P · t` (تبادلي) |
| `power / frequency` | `KEnergyUnitInstance` | صيغة الزمن العكسي (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | الشغل الميكانيكي `W = F · s` (انظر [الطاقة (ميكانيكا)](../mechanics/energy.md)) |
| `mass·length²/time²` | عبر `.toEnergy()` | التعبير الأصلي القياسي `kg·m²·s⁻²` |

تُعيد الصيغ المكتوبة بأنواع صريحة طاقة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toEnergy()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الشحنة والجهد والقدرة والزمن والطاقة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / charge` | `KVoltageUnitInstance` | `U = W / Q` |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.power.watts
import org.pcsoft.framework.kunit.energy.*

// مثال واقعي - سخّان بقدرة 2 kW يعمل 3 ساعات يستهلك 6 kWh = 21600 kJ.
val w = (2 of kilo.watts) * (3 of hours)   // KEnergyUnitInstance
w into kilo.joules                         // 21600.0

// الطاقة الكهربائية من الشحنة والجهد: 10 C منقولة عبر 50 V تساوي 500 J.
val fromCharge = (10 of coulombs) * (50 of volts)  // KEnergyUnitInstance، 500 J

// التعريف محلولًا لإيجاد الجهد:
val u = (500 of joules) / (10 of coulombs)         // KVoltageUnitInstance، 50 V

// نفس الطاقة كتعبير أصلي kg·m²·s⁻²:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)                  // true
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
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.energy.*

(1 of kilo.joules).toString()     // "1000.0 J" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J` | `joules` | الطاقة، الوحدة الأساسية (رمز مسمّى، جول) |
| `Q · U` | `(10 of coulombs) * (50 of volts)` | الطاقة الكهربائية من الشحنة والجهد |
| `P · t` | `(2 of kilo.watts) * (3 of hours)` | الطاقة المستهلَكة (الكيلوواط ساعة لا يملك رمزًا) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | الطاقة ككتلة·طول² / زمن² (صيغة الكسر) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | نفس الطاقة كحاصل ضرب خالص |
| `kJ` | `kilo.joules` | طاقة ببادئة (كيلوجول) |
