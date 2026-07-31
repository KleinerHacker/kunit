# الموصلية

الحزمة: `org.pcsoft.framework.kunit.electric.conductivity`
الوحدة الأساسية: **سيمنس لكل متر** (`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`)

النوع: **وحدة مركّبة**

الموصلية الكهربائية وحدة **مركّبة**: التركيب `mass⁻¹ · length⁻³ · time³ · current²`
(`kg⁻¹·m⁻³·s³·A²`). يغلّف `KConductivityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `-1`، و`KDistanceUnit.BASE` (متر) بالأس `-3`، و`KTimeUnit.BASE` (ثانية)
بالأس `+3`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `+2`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى **غرامات** (وليس
كيلوغرامات) وأس الكتلة *سالب*، يُضرَب الناتج القياسي في 1000 للوصول إلى سيمنس لكل متر؛ وتُطبَّع القيمة المخزّنة دائمًا
إلى S/m.

الموصلية هي خاصية المادة الكامنة وراء الناقلية، ومقلوب [المقاومية](resistivity.md) (`σ = 1 / ρ`).

## إنشاء موصلية

تُنشأ الموصلية برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| الموصلية               | الرمز   |               الرمز البرمجي | 1 وحدة بـ S/m |
|------------------------|---------|----------------------------:|--------------:|
| سيمنس لكل متر          | `S/m`   |           `siemensPerMeter` |           1.0 |
| سيمنس لكل سنتيمتر      | `S/cm`  |      `siemensPerCentimeter` |         100.0 |
| ميكروسيمنس لكل سنتيمتر | `µS/cm` | `microsiemensPerCentimeter` |        1.0e-4 |
| ميغاسيمنس لكل متر      | `MS/m`  |       `megasiemensPerMeter` |         1.0e6 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`mega.siemensPerMeter`،
`milli.siemensPerMeter`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // نحاس
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## تفكيكات متعددة

يمكن الوصول إلى الموصلية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج موصلية متساوية القيمة:

| التعبير                         | نوع النتيجة                 | المعنى                                                            |
|---------------------------------|-----------------------------|-------------------------------------------------------------------|
| `1 / resistivity`               | `KConductivityUnitInstance` | المقلوب `σ = 1 / ρ`                                               |
| `conductance / length`          | `KConductivityUnitInstance` | `σ = G · l / A`؛ عامل الهندسة `l / A` هو مقلوب طول، ومن ثمّ القسمة |
| `current²·time³/(mass·length³)` | عبر `.toConductivity()`     | التعبير الأصلي القياسي `kg⁻¹·m⁻³·s³·A²`                           |

تُعيد الصيغ المكتوبة بأنواع صريحة موصلية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toConductivity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الناقلية والطول والموصلية:

| التعبير                      | نوع النتيجة                | المعنى                       |
|------------------------------|----------------------------|------------------------------|
| `conductivity * length`      | `KConductanceUnitInstance` | `G = σ · A / l` (تبادلي)     |
| `conductance / conductivity` | `KLengthUnitInstance`      | عامل الهندسة `A / l = G / σ` |
| `1 / conductivity`           | `KResistivityUnitInstance` | العودة إلى المقاومية         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.conductance.siemens
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.electric.conductivity.*

// مثال واقعي - نحاس: مقاومية قدرها 17 nΩ·m تعادل موصلية تبلغ نحو 58.8 MS/m.
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// الناقلية على هندسة الموصل:
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance، 2 S/m

// نفس الموصلية كتعبير أصلي kg⁻¹·m⁻³·s³·A²:
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// زوج المقلوب متماثل:
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`³`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر
الصيغتان المتكافئتان في Kotlin.

| الرياضيات        | Kotlin                                                                      | المعنى                                              |
|------------------|-----------------------------------------------------------------------------|-----------------------------------------------------|
| `S/m`            | `siemensPerMeter`                                                           | الموصلية، الوحدة الأساسية (رمز مسمّى، سيمنس لكل متر) |
| `1 / ρ`          | `1 / (17 of nano.ohmMeters)`                                                | الموصلية كمقلوب المقاومية                           |
| `A²·s³/(kg·m³)`  | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))`       | الموصلية كتيار²·زمن³ / (كتلة·طول³) (صيغة الكسر)     |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | نفس الموصلية كحاصل ضرب خالص                         |
| `MS/m`           | `mega.siemensPerMeter`                                                      | موصلية ببادئة (ميغاسيمنس لكل متر)                   |
