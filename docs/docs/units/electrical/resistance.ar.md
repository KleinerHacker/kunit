# المقاومة

الحزمة: `org.pcsoft.framework.kunit.resistance`
الوحدة الأساسية: **أوم** (`KResistanceUnit.BASE == KResistanceUnit.OHM`)

النوع: **وحدة مركّبة**

المقاومة الكهربائية وحدة **مركّبة**: التركيب `mass · length² · time⁻³ · current⁻²` (`kg·m²·s⁻³·A⁻²`).
يغلّف `KResistanceUnitInstance` نسخةَ `KMixedUnitInstance` من أربعة حدود — `KMassUnit.BASE` (غرام) عند
`+1`، و`KDistanceUnit.BASE` (متر) عند `+2`، و`KTimeUnit.BASE` (ثانية) عند `-3`،
و`KElectricCurrentUnit.BASE` (أمبير) عند `-2`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى **الغرامات** (لا
الكيلوغرامات)، فإنّ الأوم أكبر 1000× من الأساس الخام للمكوّنات؛ القيمة المخزّنة مُطبَّعة إلى الأوم.

## بناء مقاومة

ابنِ مقاومة برمز مسمّى، أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| المقاومة | الرمز | الرمز البرمجي | 1 وحدة بـ Ω |
|---|---|---:|---:|
| أوم | `Ω` | `ohms` | 1.0 |
| ستات-أوم (CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| أب-أوم (CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| أوم دولي | `Ω_int` | `internationalOhms` | 1.000049 |
| أوم قانوني (1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| وحدة سيمنز الزئبقية | `Ω_S` | `siemensUnits` | 0.9534 |

تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`kilo.ohms`، `mega.ohms`، `milli.ohms`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## تفكيكات متعدّدة

يمكن بلوغ المقاومة عبر عدّة **تفكيكات مكافئة**، كلّها تُنتج مقاومة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | قانون أوم `R = U / I` |
| `mass·length²/(time³·current²)` | عبر `.toResistance()` | تعبير `kg·m²·s⁻³·A⁻²` الأصلي القياسي |

تُعيد صيغة المعامِل المحكومة بالنوع مقاومة مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toResistance()` (الذي يتعرّف فقط على الشكل القياسي ويُطلق `IllegalStateException` خلاف
ذلك). كلا المسارين متساوي القيمة.

تربط معاملات قانون أوم العكسية الجهد والمقاومة والتيار معًا:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I` (تبادلي) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// مثال واقعي - قانون أوم: 230 V عبر حِمل يسحب 2 A يعني مقاومة 115 Ω.
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance، 115 Ω

// المقاومة نفسها كتعبير kg·m²·s⁻³·A⁻² الأصلي:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `Ω` | `ohms` | المقاومة، الوحدة الأساسية (رمز مسمّى، أوم) |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | المقاومة ككتلة·طول² / (زمن³·تيار²) (صيغة الكسر) |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | المقاومة نفسها كحاصل ضرب صرف |
| `kΩ` | `kilo.ohms` | مقاومة ببادئة (كيلوأوم) |
