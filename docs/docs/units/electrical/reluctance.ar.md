# الممانعة المغناطيسية

الحزمة: `org.pcsoft.framework.kunit.electric.reluctance`
الوحدة الأساسية: **أمبير لكل ويبر** (`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`)

النوع: **وحدة مركّبة**

الممانعة المغناطيسية وحدة **مركّبة**: التركيب `mass⁻¹ · length⁻² · time² · current²`
(`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`). يغلّف `KReluctanceUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `-1`، و`KDistanceUnit.BASE` (متر) بالأس `-2`، و`KTimeUnit.BASE` (ثانية)
بالأس `+2`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `+2`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى
**غرامات** (وليس كيلوغرامات) وأس الكتلة *سالب*، يُضرَب الناتج القياسي في 1000 للوصول إلى أمبير لكل
ويبر؛ وتُطبَّع القيمة المخزّنة دائمًا إلى أمبير لكل ويبر.

الممانعة `Rm` هي نظير الدارة المغناطيسية لـ[المقاومة](resistance.ar.md) الكهربائية: تربط القوة
الدافعة المغناطيسية `Θ` (تُقاس بلفّات الأمبير، انظر [التيار الكهربائي](ec.ar.md)) بـ
[التدفق المغناطيسي](magneticflux.ar.md) الناتج عبر قانون هوبكنسون `Θ = Rm · Φ`. ومقلوبها هو
**النفاذية المغناطيسية (Permeance)** `Λ`، التي تُقاس بالهنري، ولذلك تحملها مجموعة
[الحث](inductance.ar.md).

## إنشاء ممانعة

تُنشأ الممانعة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| الممانعة | الرمز | الرمز البرمجي | 1 وحدة بـ A/Wb |
|---|---|---:|---:|
| أمبير لكل ويبر | `A/Wb` | `amperesPerWeber` | 1.0 |
| هنري معكوس | `H⁻¹` | `inverseHenries` | 1.0 |
| لفّة أمبير لكل ويبر | `At/Wb` | `ampereTurnsPerWeber` | 1.0 |

تصف الصياغات الثلاث الكمية نفسها — لأن عدد لفّات الملف عدد صِرف — ولذلك فهي متساوية القيمة؛ وتوثّق
الرموز المختلفة وجهة النظر. تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder`
(`mega.amperesPerWeber`، `kilo.inverseHenries`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.reluctance.*

val rm = 2 of mega.amperesPerWeber    // قلب حديدي بفجوة هوائية
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## تفكيكات متعددة

يمكن الوصول إلى الممانعة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج ممانعة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `current / magneticFlux` | `KReluctanceUnitInstance` | قانون هوبكنسون `Rm = Θ / Φ` |
| `1 / inductance` | `KReluctanceUnitInstance` | مقلوب النفاذية المغناطيسية، `Rm = 1 / Λ` |
| `(time²·current²)/(mass·length²)` | عبر `.toReluctance()` | التعبير الأصلي القياسي `kg⁻¹·m⁻²·s²·A²` |

تُعيد الصيغ المكتوبة بأنواع صريحة ممانعة مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toReluctance()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين القوة الدافعة المغناطيسية والتدفق والنفاذية المغناطيسية والممانعة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ` (تبادلي) |
| `current / reluctance` | `KMagneticFluxUnitInstance` | `Φ = Θ / Rm` |
| `1 / reluctance` | `KInductanceUnitInstance` | النفاذية المغناطيسية `Λ = 1 / Rm` (بالهنري) |

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
import org.pcsoft.framework.kunit.electric.current.ampereTurns
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.reluctance.*

// مثال واقعي - قوة دافعة مغناطيسية قدرها 2 kAt عبر قلب ممانعته 2 MA/Wb تعطي تدفقًا قدره 1 mWb.
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// التعريف محلولًا لإيجاد الممانعة:
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// نفس الممانعة كتعبير أصلي kg⁻¹·m⁻²·s²·A²:
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb (دارة مغناطيسية على التوالي)
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `A/Wb` | `amperesPerWeber` | الممانعة، الوحدة الأساسية (رمز مسمّى، أمبير لكل ويبر) |
| `H⁻¹` | `inverseHenries` | صياغة مقلوب الحث لنفس الكمية |
| `Θ / Φ` | `(6 of amperes) / (3 of webers)` | الممانعة من قانون هوبكنسون |
| `1 / Λ` | `1 / (0.5 of henries)` | الممانعة كمقلوب النفاذية المغناطيسية |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | الممانعة كـ(زمن²·تيار²) / (كتلة·طول²) (صيغة الكسر) |
| `kg⁻¹·m⁻²·s²·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | نفس الممانعة كحاصل ضرب خالص |
| `MA/Wb` | `mega.amperesPerWeber` | ممانعة ببادئة (ميغاأمبير لكل ويبر) |
