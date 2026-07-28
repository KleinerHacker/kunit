# العزم ثنائي القطب الكهربائي

الحزمة: `org.pcsoft.framework.kunit.electricdipolemoment`
الوحدة الأساسية: **كولوم متر**
(`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`)

النوع: **وحدة مركّبة**

العزم ثنائي القطب الكهربائي وحدة **مركّبة**: التركيب `current · time · length`
(`A·s·m` = `C·m`). يغلّف `KElectricDipoleMomentUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود —
`KElectricCurrentUnit.BASE` (أمبير) بالأس `+1`، و`KTimeUnit.BASE` (ثانية) بالأس `+1`، و`KDistanceUnit.BASE`
(متر) بالأس `+1`. لا تحمل هذه المجموعة أي بُعد كتلة، لذا لا حاجة إلى جسر غرام/كيلوغرام؛ وتُطبَّع القيمة
المخزّنة دائمًا إلى كولوم متر.

العزم ثنائي القطب الكهربائي `p = Q · d` يقيس الفصل بين [شحنة](charge.ar.md) موجبة وأخرى سالبة. وهي
الكمية التي تربط الجزيء بـ[شدة المجال الكهربائي](electricfieldstrength.ar.md).

## إنشاء عزم ثنائي القطب الكهربائي

يُنشأ العزم برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| العزم | الرمز | الرمز البرمجي | 1 وحدة بـ C·m |
|---|---|---:|---:|
| كولوم متر | `C·m` | `coulombMeters` | 1.0 |
| ديباي (CGS) | `D` | `debyes` | 3.335640952e-30 |

يسود الديباي في فيزياء الجزيئات والكيمياء. تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder`
(`pico.coulombMeters`، `milli.debyes`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricdipolemoment.*

val p = 1.85 of debyes        // جزيء الماء
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## تفكيكات متعددة

يمكن الوصول إلى العزم ثنائي القطب الكهربائي عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج عزمًا متساوي القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `charge * length` | `KElectricDipoleMomentUnitInstance` | `p = Q · d`، شحنة مضروبة في مسافة الفصل بينهما (تبادلي) |
| `current·time·length` | عبر `.toElectricDipoleMoment()` | التعبير الأصلي القياسي `A·s·m` |

تُعيد الصيغة المكتوبة بنوع صريح عزمًا مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toElectricDipoleMoment()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). كلا المسارَين متساوٍ في القيمة.

تربط العمليات العكسية بين الشحنة ومسافة الفصل والعزم:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.electricdipolemoment.*

// مثال واقعي - فصل 1 pC بمسافة 1 nm يعطي 1e-21 C·m، أي نحو 3.0e8 ديباي.
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// محلولة لمسافة الفصل:
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance، 3 m

// نفس العزم كتعبير أصلي A·s·m:
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricdipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricdipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `C·m` | `coulombMeters` | العزم ثنائي القطب الكهربائي، الوحدة الأساسية (رمز مسمّى، كولوم متر) |
| `D` | `debyes` | ديباي CGS، 3.335 640 952e-30 C·m |
| `Q · d` | `(1 of pico.coulombs) * (1 of nano.meters)` | العزم من شحنة ومسافة الفصل بينها |
| `p / Q` | `(6 of coulombMeters) / (2 of coulombs)` | مسافة الفصل خلف عزم معطى |
| `A·s·m` | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | العزم كتيار·زمن·طول (حاصل ضرب خالص) |
| `pC·m` | `pico.coulombMeters` | عزم ببادئة (بيكوكولوم متر) |
