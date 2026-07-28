# القدرة (كهربائية)

الحزمة: `org.pcsoft.framework.kunit.common.power`
الوحدة الأساسية: **واط** (`KPowerUnit.BASE == KPowerUnit.WATT`)

النوع: **وحدة مركّبة**

القدرة وحدة **مركّبة**: التركيب `mass · length² · time⁻³` (`kg·m²·s⁻³`). يغلّف
`KPowerUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود — `KMassUnit.BASE` (غرام) بالأس
`+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية) بالأس `-3`. ولأن مكوّن الكتلة
في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الواط؛
وتُطبَّع القيمة المخزّنة دائمًا إلى الواط.

القدرة من الناحية التقنية كمّية **واحدة** تظهر في عدة مجالات موضوعية. تصف هذه الصفحة قراءتها
*الكهربائية* (`P = U · I`). المجموعة نفسها في Kotlin موثّقة للمجالات الأخرى في
[القدرة (ميكانيكا)](../mechanics/power.md) و[القدرة (ديناميكا حرارية)](../thermodynamics/power.md).

## إنشاء قدرة

تُنشأ القدرة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| القدرة | الرمز | الرمز البرمجي | 1 وحدة بالواط |
|---|---|---:|---:|
| واط | `W` | `watts` | 1.0 |
| حصان بخاري متري | `PS` | `metricHorsePowers` | 735.49875 |
| حصان بخاري ميكانيكي | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| إرغ لكل ثانية (CGS) | `erg/s` | `ergsPerSecond` | 1.0e-7 |
| فولت أمبير (قدرة ظاهرية) | `VA` | `voltAmperes` | 1.0 |
| فولت أمبير تفاعلي | `var` | `vars` | 1.0 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.watts`، `mega.watts`،
`milli.watts`، …).

### القدرة الظاهرية والتفاعلية (VA، var)

في أنظمة التيار المتردد تُميَّز ثلاث قدرات، جميعها متطابقة بُعديًا مع الواط:

* **القدرة الفعّالة** `P = U · I · cos φ` بالواط (`W`) — الجزء الذي يبذل شغلًا،
* **القدرة الظاهرية** `S = U · I` بالفولت أمبير (`VA`) — حاصل ضرب الجهد الفعّال والتيار الفعّال،
* **القدرة التفاعلية** `Q = U · I · sin φ` بالفولت أمبير التفاعلي (`var`) — الجزء المتذبذب بين
  المصدر والحمل دون بذل شغل.

ولأن الثلاثة تختلف بالاصطلاح فقط، تحتفظ KUnit بها في هذه المجموعة الواحدة وتفصلها بالرمز:
`1 VA = 1 var = 1 W`. تعمل البادئات كالمعتاد، فـ `kilo.voltAmperes` تساوي 1 kVA و`kilo.vars` تساوي
1 kvar.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

// محوّل بقدرة اسمية 25 kVA يغذّي حملًا بمعامل قدرة cos φ = 0.8:
val s = 25 of kilo.voltAmperes
val p = (25 * 0.8) of kilo.watts     // 20 kW قدرة فعّالة
val q = (25 * 0.6) of kilo.vars      // 15 kvar قدرة تفاعلية
s into kilo.voltAmperes               // 25.0
q into kilo.vars                      // 15.0
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## تفكيكات متعددة

يمكن الوصول إلى القدرة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج قدرة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `voltage * current` | `KPowerUnitInstance` | القدرة الكهربائية `P = U · I` (تبادلي) |
| `force * speed` | `KPowerUnitInstance` | القدرة الميكانيكية `P = F · v` (تبادلي) |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` (انظر [الطاقة](energy.md)) |
| `mass·length²/time³` | عبر `.toPower()` | التعبير الأصلي القياسي `kg·m²·s⁻³` |

تُعيد الصيغ المكتوبة بأنواع صريحة قدرة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toPower()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي `IllegalStateException`
خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية للصيغة الكهربائية بين الجهد والتيار والقدرة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `power / current` | `KVoltageUnitInstance` | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.common.power.*

// مثال واقعي - مقبس كهربائي: 230 V عند 10 A يمنح 2.3 kW.
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// التعريف محلولًا لإيجاد التيار الذي يسحبه حمل 2.3 kW عند 230 V:
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance، 10 A

// نفس القدرة كتعبير أصلي kg·m²·s⁻³:
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
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

(1 of kilo.watts).toString()     // "1000.0 W" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻³`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `W` | `watts` | القدرة، الوحدة الأساسية (رمز مسمّى، واط) |
| `U · I` | `(230 of volts) * (10 of amperes)` | القدرة الكهربائية من الجهد والتيار |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | القدرة ككتلة·طول² / زمن³ (صيغة الكسر) |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | نفس القدرة كحاصل ضرب خالص |
| `kW` | `kilo.watts` | قدرة ببادئة (كيلوواط) |
| `S = U · I` بـ `VA` | `voltAmperes` | القدرة الظاهرية (تيار متردد) |
| `Q` بـ `var` | `vars` | القدرة التفاعلية (تيار متردد) |
| `kVA` | `kilo.voltAmperes` | قدرة ظاهرية ببادئة (كيلوفولت أمبير) |
| `kvar` | `kilo.vars` | قدرة تفاعلية ببادئة |
