# السماحية

الحزمة: `org.pcsoft.framework.kunit.permittivity`
الوحدة الأساسية: **فاراد لكل متر** (`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`)

النوع: **وحدة مركّبة**

السماحية وحدة **مركّبة**: التركيب `mass⁻¹ · length⁻³ · time⁴ · current²`
(`kg⁻¹·m⁻³·s⁴·A²` = `F/m`). يغلّف `KPermittivityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `-1`، و`KDistanceUnit.BASE` (متر) بالأس `-3`، و`KTimeUnit.BASE` (ثانية)
بالأس `+4`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `+2`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى
**غرامات** (وليس كيلوغرامات) وأس الكتلة *سالب*، يُضرَب الناتج القياسي في 1000 للوصول إلى فاراد لكل
متر؛ وتُطبَّع القيمة المخزّنة دائمًا إلى فاراد لكل متر.

السماحية `ε` هي الثابت الكهربائي للمادة: تربط
[كثافة التدفق الكهربائي](electricfluxdensity.ar.md) بـ
[شدة المجال الكهربائي](electricfieldstrength.ar.md) (`ε = D / E`) وتربط
[السعة](capacitance.ar.md) بهندسة الألواح. ونظيرها المغناطيسي هو
[النفاذية](permeability.ar.md).

## إنشاء سماحية

تُنشأ السماحية برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| السماحية | الرمز | الرمز البرمجي | 1 وحدة بـ F/m |
|---|---|---:|---:|
| فاراد لكل متر | `F/m` | `faradsPerMeter` | 1.0 |
| فاراد لكل سنتيمتر | `F/cm` | `faradsPerCentimeter` | 100.0 |
| سماحية الفراغ `ε₀` | `F/m` | `vacuumPermittivity` | 8.8541878188e-12 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`pico.faradsPerMeter`، `nano.faradsPerMeter`، …).
كما يتوفّر الثابت أيضًا باسم `KPermittivityUnit.VACUUM_PERMITTIVITY`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## تفكيكات متعددة

يمكن الوصول إلى السماحية عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج سماحية متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `capacitance / length` | `KPermittivityUnitInstance` | `ε = C · d / A`، عامل الهندسة `d / A` طول |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |
| `(time⁴·current²)/(mass·length³)` | عبر `.toPermittivity()` | التعبير الأصلي القياسي `kg⁻¹·m⁻³·s⁴·A²` |

تُعيد الصيغ المكتوبة بأنواع صريحة سماحية مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toPermittivity()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين السعة والطول وكمّيتَي المجال:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `permittivity * length` | `KCapacitanceUnitInstance` | `C = ε · A / d` (تبادلي) |
| `capacitance / permittivity` | `KLengthUnitInstance` | عامل الهندسة `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (تبادلي) |
| `electricFluxDensity / permittivity` | `KElectricFieldStrengthUnitInstance` | `E = D / ε` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.capacitance.farads
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricfluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.permittivity.*

// مثال واقعي - في الفراغ ينتج مجال قدره 1 MV/m كثافة تدفق تبلغ 8.854 µC/m².
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// التعريف محلولًا لإيجاد السماحية:
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// نفس السماحية كتعبير أصلي kg⁻¹·m⁻³·s⁴·A²:
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`⁴`، `⁻³`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `F/m` | `faradsPerMeter` | السماحية، الوحدة الأساسية (رمز مسمّى، فاراد لكل متر) |
| `ε₀` | `vacuumPermittivity` | ثابت سماحية الفراغ، 8.854 pF/m |
| `D / E` | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)` | السماحية ككثافة تدفق على شدة مجال |
| `C · (d/A)` | `(10 of farads) / (5 of meters)` | السماحية من السعة وعامل الهندسة |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))` | السماحية كـ(زمن⁴·تيار²) / (كتلة·طول³) (صيغة الكسر) |
| `kg⁻¹·m⁻³·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | نفس السماحية كحاصل ضرب خالص |
| `pF/m` | `pico.faradsPerMeter` | سماحية ببادئة (بيكوفاراد لكل متر) |
