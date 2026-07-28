# شدة المجال الكهربائي

الحزمة: `org.pcsoft.framework.kunit.electric.electricfieldstrength`
الوحدة الأساسية: **فولت لكل متر** (`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`)

النوع: **وحدة مركّبة**

شدة المجال الكهربائي وحدة **مركّبة**: التركيب `mass · length · time⁻³ · current⁻¹`
(`kg·m·s⁻³·A⁻¹`). يغلّف `KElectricFieldStrengthUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `+1`، و`KDistanceUnit.BASE` (متر) بالأس `+1`، و`KTimeUnit.BASE` (ثانية)
بالأس `-3`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `-1`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى
**غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى فولت لكل متر؛ وتُطبَّع القيمة
المخزّنة دائمًا إلى فولت لكل متر.

شدة المجال `E` هي هبوط الجهد لكل وحدة طول، وهي في الوقت ذاته القوة المؤثرة على شحنة واحدة.
وترتبط بـ[كثافة التدفق الكهربائي](electricfluxdensity.ar.md) عبر
[السماحية](permittivity.ar.md) (`D = ε · E`) وتدفع حاملات الشحنة بسرعة تُعطى بواسطة
[الحركية الكهربائية](electricmobility.ar.md) الخاصة بها (`v = μ · E`).

## إنشاء شدة مجال كهربائي

تُنشأ شدة المجال برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| شدة المجال | الرمز | الرمز البرمجي | 1 وحدة بـ V/m |
|---|---|---:|---:|
| فولت لكل متر | `V/m` | `voltsPerMeter` | 1.0 |
| فولت لكل سنتيمتر | `V/cm` | `voltsPerCentimeter` | 100.0 |
| ستاتفولت لكل سنتيمتر (CGS-ESU) | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.voltsPerMeter`، `mega.voltsPerMeter`،
`kilo.voltsPerCentimeter`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val e = 3 of mega.voltsPerMeter        // متانة عزل الهواء العزلية
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## تفكيكات متعددة

يمكن الوصول إلى شدة المجال الكهربائي عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج شدة مجال متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`، هبوط الجهد لكل وحدة طول |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`، القوة المؤثرة على شحنة واحدة |
| `mass·length/(time³·current)` | عبر `.toElectricFieldStrength()` | التعبير الأصلي القياسي `kg·m·s⁻³·A⁻¹` |

تُعيد الصيغ المكتوبة بأنواع صريحة شدة مجال مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toElectricFieldStrength()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الجهد والطول والقوة والشحنة وشدة المجال:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l` (تبادلي) |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q` (تبادلي) |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.fieldstrength.*

// مثال واقعي - جهد التيار الكهربائي المنزلي عبر فجوة هوائية 2 مم يعطي 115 kV/m.
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance، 115000 V/m

// نفس شدة المجال من تفكيك القوة:
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// نفس شدة المجال كتعبير أصلي kg·m·s⁻³·A⁻¹:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`³`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `V/m` | `voltsPerMeter` | شدة المجال الكهربائي، الوحدة الأساسية (رمز مسمّى، فولت لكل متر) |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | شدة المجال من الجهد عبر مسافة |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | شدة المجال كقوة لكل وحدة شحنة |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | شدة المجال ككتلة·طول / (زمن³·تيار) (صيغة الكسر) |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | نفس شدة المجال كحاصل ضرب خالص |
| `kV/m` | `kilo.voltsPerMeter` | شدة مجال ببادئة (كيلوفولت لكل متر) |
