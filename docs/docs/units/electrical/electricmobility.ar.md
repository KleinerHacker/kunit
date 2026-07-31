# الحركية الكهربائية

الحزمة: `org.pcsoft.framework.kunit.electric.electricmobility`
الوحدة الأساسية: **متر مربع لكل فولت ثانية**
(`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`)

النوع: **وحدة مركّبة**

الحركية الكهربائية وحدة **مركّبة**: التركيب `mass⁻¹ · time² · current`
(`kg⁻¹·s²·A` = `m²/(V·s)`). يغلّف `KElectricMobilityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود —
`KMassUnit.BASE` (غرام) بالأس `-1`، و`KTimeUnit.BASE` (ثانية) بالأس `+2`، و`KElectricCurrentUnit.BASE`
(أمبير) بالأس `+1`. يلغي بُعد الطول نفسه لأن الفولت يحمل بالفعل `m²`، ولذلك تتكوّن الصيغة القياسية من ثلاثة حدود فقط.
ولأن مكوّن الكتلة في المكتبة مطبَّع إلى **غرامات** (وليس كيلوغرامات) وأس الكتلة *سالب*، يُضرَب الناتج القياسي في 1000
للوصول إلى الوحدة الأساسية؛ وتُطبَّع القيمة المخزّنة دائمًا إلى متر مربع لكل فولت ثانية.

تصف الحركية الكهربائية `μ` سرعة انجراف حامل الشحنة في مجال كهربائي: `v = μ · E`، حيث `E` هي
[شدة المجال الكهربائي](electricfieldstrength.ar.md).

## إنشاء حركية كهربائية

تُنشأ الحركية برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| الحركية                     | الرمز       |                    الرمز البرمجي | 1 وحدة بـ m²/(V·s) |
|-----------------------------|-------------|---------------------------------:|-------------------:|
| متر مربع لكل فولت ثانية     | `m²/(V·s)`  |      `squareMetersPerVoltSecond` |                1.0 |
| سنتيمتر مربع لكل فولت ثانية | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` |             1.0e-4 |

صيغة السنتيمتر هي الترميز المستخدم في فيزياء أشباه الموصلات. تدعم الوحدات المسمّاة بادئات النظام الدولي عبر
`KPrefixBuilder` (`milli.squareMetersPerVoltSecond`، `kilo.squareCentimetersPerVoltSecond`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.mobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // حركية الإلكترونات في السيليكون
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## تفكيكات متعددة

يمكن الوصول إلى الحركية الكهربائية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج حركية متساوية القيمة:

| التعبير                         | نوع النتيجة                     | المعنى                                   |
|---------------------------------|---------------------------------|------------------------------------------|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`، سرعة الانجراف لكل وحدة مجال |
| `(time²·current)/mass`          | عبر `.toElectricMobility()`     | التعبير الأصلي القياسي `kg⁻¹·s²·A`       |

تُعيد الصيغة المكتوبة بنوع صريح حركية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toElectricMobility()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). كلا المسارَين متساوٍ في القيمة.

تربط العمليات العكسية بين سرعة الانجراف وشدة المجال والحركية:

| التعبير                                    | نوع النتيجة                          | المعنى               |
|--------------------------------------------|--------------------------------------|----------------------|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance`                 | `v = μ · E` (تبادلي) |
| `speed / electricMobility`                 | `KElectricFieldStrengthUnitInstance` | `E = v / μ`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.mobility.*

// مثال واقعي - إلكترونات السيليكون بحركية 1400 cm²/(V·s) تنجرف بسرعة 140 m/s في مجال 1 kV/m.
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance، 140 m/s

// التعريف محلولًا لإيجاد الحركية:
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// نفس الحركية كتعبير أصلي kg⁻¹·s²·A:
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.mobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.mobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر
الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                                                               | المعنى                                         |
|-------------|----------------------------------------------------------------------|------------------------------------------------|
| `m²/(V·s)`  | `squareMetersPerVoltSecond`                                          | الحركية الكهربائية، الوحدة الأساسية (رمز مسمّى) |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond`                                     | ترميز فيزياء أشباه الموصلات، 1e-4 m²/(V·s)     |
| `v / E`     | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)`            | الحركية من سرعة الانجراف على شدة المجال        |
| `μ · E`     | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | سرعة الانجراف في مجال معطى                     |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)`           | الحركية كـ(زمن²·تيار) / كتلة (صيغة الكسر)      |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)`            | نفس الحركية كحاصل ضرب خالص                     |
