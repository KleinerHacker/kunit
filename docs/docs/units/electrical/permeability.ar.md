# النفاذية

الحزمة: `org.pcsoft.framework.kunit.electric.permeability`
الوحدة الأساسية: **هنري لكل متر** (`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`)

النوع: **وحدة مركّبة**

النفاذية وحدة **مركّبة**: التركيب `mass · length · time⁻² · current⁻²`
(`kg·m·s⁻²·A⁻²` = `H/m`). يغلّف `KPermeabilityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `+1`، و`KDistanceUnit.BASE` (متر) بالأس `+1`، و`KTimeUnit.BASE` (ثانية)
بالأس `-2`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `-2`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى
**غرامات** (وليس كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى هنري لكل متر؛ وتُطبَّع القيمة
المخزّنة دائمًا إلى هنري لكل متر.

النفاذية `μ` هي الثابت المغناطيسي للمادة: تربط
[كثافة التدفق المغناطيسي](magneticfluxdensity.ar.md) بـ
[شدة المجال المغناطيسي](magneticfieldstrength.ar.md) (`μ = B / H`) وتربط
[الحث](inductance.ar.md) بهندسة الملف. ونظيرها الكهربائي هو
[السماحية](permittivity.ar.md).

## إنشاء نفاذية

تُنشأ النفاذية برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| النفاذية | الرمز | الرمز البرمجي | 1 وحدة بـ H/m |
|---|---|---:|---:|
| هنري لكل متر | `H/m` | `henriesPerMeter` | 1.0 |
| هنري لكل سنتيمتر | `H/cm` | `henriesPerCentimeter` | 100.0 |
| نفاذية الفراغ `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`micro.henriesPerMeter`، `milli.henriesPerMeter`،
…). كما يتوفّر الثابت أيضًا باسم `KPermeabilityUnit.VACUUM_PERMEABILITY`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## تفكيكات متعددة

يمكن الوصول إلى النفاذية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج نفاذية متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`، عامل الهندسة طول |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | عبر `.toPermeability()` | التعبير الأصلي القياسي `kg·m·s⁻²·A⁻²` |

تُعيد الصيغ المكتوبة بأنواع صريحة نفاذية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toPermeability()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الحث والطول وكمّيتَي المجال المغناطيسي:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l` (تبادلي) |
| `inductance / permeability` | `KLengthUnitInstance` | عامل الهندسة `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H` (تبادلي) |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.electric.permeability.*

// مثال واقعي - في الفراغ ينتج مجال قدره 1000 A/m كثافة تدفق تبلغ 1.257 mT.
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// التعريف محلولًا لإيجاد النفاذية:
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// نفس النفاذية كتعبير أصلي kg·m·s⁻²·A⁻²:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `H/m` | `henriesPerMeter` | النفاذية، الوحدة الأساسية (رمز مسمّى، هنري لكل متر) |
| `μ₀` | `vacuumPermeability` | ثابت نفاذية الفراغ، 1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | النفاذية ككثافة تدفق على شدة مجال |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | النفاذية من الحث وهندسة الملف |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | النفاذية ككتلة·طول / (زمن²·تيار²) (صيغة الكسر) |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | نفس النفاذية كحاصل ضرب خالص |
| `µH/m` | `micro.henriesPerMeter` | نفاذية ببادئة (ميكروهنري لكل متر) |
