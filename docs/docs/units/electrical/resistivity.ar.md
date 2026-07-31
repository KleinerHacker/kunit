# المقاومية

الحزمة: `org.pcsoft.framework.kunit.electric.resistivity`
الوحدة الأساسية: **أوم متر** (`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`)

النوع: **وحدة مركّبة**

المقاومية الكهربائية وحدة **مركّبة**: التركيب `mass · length³ · time⁻³ · current⁻²`
(`kg·m³·s⁻³·A⁻²`). يغلّف `KResistivityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `+1`، و`KDistanceUnit.BASE` (متر) بالأس `+3`، و`KTimeUnit.BASE` (ثانية)
بالأس `-3`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `-2`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى **غرامات** (وليس
كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الأوم متر؛ وتُطبَّع القيمة المخزّنة دائمًا إلى الأوم متر.

المقاومية هي خاصية المادة الكامنة وراء المقاومة، ومقلوب [الموصلية](conductivity.md) (`ρ = 1 / σ`).

## إنشاء مقاومية

تُنشأ المقاومية برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| المقاومية                 | الرمز      |        الرمز البرمجي | 1 وحدة بالأوم·متر |
|---------------------------|------------|---------------------:|------------------:|
| أوم متر                   | `Ω·m`      |          `ohmMeters` |               1.0 |
| أوم سنتيمتر               | `Ω·cm`     |     `ohmCentimeters` |              0.01 |
| ستاتأوم سنتيمتر (CGS-ESU) | `statΩ·cm` | `statohmCentimeters` |      8.98755179e9 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`nano.ohmMeters`، `micro.ohmMeters`،
`milli.ohmCentimeters`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.electric.resistivity.*

val rho = 17 of nano.ohmMeters     // نحاس
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## تفكيكات متعددة

يمكن الوصول إلى المقاومية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج مقاومية متساوية القيمة:

| التعبير                         | نوع النتيجة                | المعنى                                             |
|---------------------------------|----------------------------|----------------------------------------------------|
| `resistance * length`           | `KResistivityUnitInstance` | `ρ = R · A / l`، عامل الهندسة `A / l` طول (تبادلي) |
| `1 / conductivity`              | `KResistivityUnitInstance` | المقلوب `ρ = 1 / σ`                                |
| `mass·length³/(time³·current²)` | عبر `.toResistivity()`     | التعبير الأصلي القياسي `kg·m³·s⁻³·A⁻²`             |

تُعيد الصيغ المكتوبة بأنواع صريحة مقاومية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toResistivity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين المقاومة والطول والمقاومية:

| التعبير                    | نوع النتيجة               | المعنى                       |
|----------------------------|---------------------------|------------------------------|
| `resistivity / length`     | `KResistanceUnitInstance` | `R = ρ · l / A`              |
| `resistivity / resistance` | `KLengthUnitInstance`     | عامل الهندسة `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.resistivity.*

// مثال واقعي - أسلاك نحاسية: 17 nΩ·m عبر عامل هندسة 1 mm يعطي 17 µΩ.
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance، 1.7e-5 Ω

// التعريف محلولًا لإيجاد المقاومية:
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance، 2 Ω·m

// نفس المقاومية كتعبير أصلي kg·m³·s⁻³·A⁻²:
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`³`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر
الصيغتان المتكافئتان في Kotlin.

| الرياضيات       | Kotlin                                                                | المعنى                                           |
|-----------------|-----------------------------------------------------------------------|--------------------------------------------------|
| `Ω·m`           | `ohmMeters`                                                           | المقاومية، الوحدة الأساسية (رمز مسمّى، أوم متر)   |
| `R · (A/l)`     | `(5 of ohms) * (0.4 of meters)`                                       | المقاومية من المقاومة وعامل الهندسة              |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | المقاومية ككتلة·طول³ / (زمن³·تيار²) (صيغة الكسر) |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)`   | نفس المقاومية كحاصل ضرب خالص                     |
| `nΩ·m`          | `nano.ohmMeters`                                                      | مقاومية ببادئة (نانو أوم متر)                    |
