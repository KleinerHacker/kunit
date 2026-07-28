# المواصَلة

الحزمة: `org.pcsoft.framework.kunit.electric.conductance`
الوحدة الأساسية: **سيمنز** (`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`)

النوع: **وحدة مركّبة**

المواصَلة الكهربائية وحدة **مركّبة**: التركيب `mass⁻¹ · length⁻² · time³ · current²`
(`kg⁻¹·m⁻²·s³·A²`). يغلّف `KConductanceUnitInstance` نسخةَ `KMixedUnitInstance` من أربعة حدود —
`KMassUnit.BASE` (غرام) عند `-1`، و`KDistanceUnit.BASE` (متر) عند `-2`، و`KTimeUnit.BASE` (ثانية) عند
`+3`، و`KElectricCurrentUnit.BASE` (أمبير) عند `+2`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى
**الغرامات** (لا الكيلوغرامات) ولأنّ أُسّ الكتلة سالب، فإنّ السيمنز يساوي 1/1000 من الأساس الخام
للمكوّنات؛ القيمة المخزّنة مُطبَّعة إلى السيمنز.

المواصَلة هي مقلوب [المقاومة](resistance.md) (`G = 1 / R`)، وتربط [الجهد](voltage.md) و
[التيار الكهربائي](ec.md) عبر قانون أوم.

## بناء مواصَلة

ابنِ مواصَلة برمز مسمّى، أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| المواصَلة | الرمز | الرمز البرمجي | 1 وحدة بـ S |
|---|---|---:|---:|
| سيمنز | `S` | `siemens` | 1.0 |
| مو (الاسم التقليدي) | `℧` | `mhos` | 1.0 |
| أب-مو (CGS-EMU) | `ab℧` | `abmhos` | 1.0e9 |
| ستات-مو (CGS-ESU) | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` مقابل `siemensUnits`"
    `siemens` (في هذه الحزمة) هي وحدة **المواصَلة** في النظام الدولي. أمّا `siemensUnits` المشابهة في
    الاسم داخل `org.pcsoft.framework.kunit.electric.resistance` فهي **وحدة سيمنز الزئبقية** التاريخية، وهي
    *مقاومة* مقدارها 0.9534 Ω. وهما كمّيتان غير مرتبطتين في حزمتين مختلفتين.

تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`milli.siemens`، `micro.siemens`،
`kilo.siemens`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## تفكيكات متعدّدة

يمكن بلوغ المواصَلة عبر عدّة **تفكيكات مكافئة**، كلّها تُنتج مواصَلة متساوية القيمة:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `current / voltage` | `KConductanceUnitInstance` | قانون أوم `G = I / U` |
| `1 / resistance` | `KConductanceUnitInstance` | مقلوب المقاومة `G = 1 / R` |
| `time³·current²/(mass·length²)` | عبر `.toConductance()` | تعبير `kg⁻¹·m⁻²·s³·A²` الأصلي القياسي |

تُعيد صيغ المعامِلات المحكومة بالنوع مواصَلة مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toConductance()` (الذي يتعرّف فقط على الشكل القياسي ويُطلق `IllegalStateException`
خلاف ذلك). وجميع المسارات متساوية القيمة.

تربط المعاملات العكسية المواصَلة والجهد والتيار معًا:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U` (تبادلي) |
| `current / conductance` | `KVoltageUnitInstance` | `U = I / G` |
| `1 / conductance` | `KResistanceUnitInstance` | `R = 1 / G` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// مثال واقعي - مواصَلة كابل تغذية: كابل يمرّ به 2 A مع هبوط جهد مقيس 1 V
// مواصَلته 2 S (أي مقاومة 0.5 Ω).
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance، 2 S
val r = 1 / g                            // KResistanceUnitInstance، 0.5 Ω

// العلاقة المقلوبة مع المقاومة:
1 / (1 of ohms) == (1 of siemens)        // true

// المواصَلة نفسها كتعبير kg⁻¹·m⁻²·s³·A² الأصلي:
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `S` | `siemens` | المواصَلة، الوحدة الأساسية (رمز مسمّى، سيمنز) |
| `s³·A²/(kg·m²)` | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | المواصَلة كزمن³·تيار² / (كتلة·طول²) (صيغة الكسر) |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | المواصَلة نفسها كحاصل ضرب صرف |
| `mS` | `milli.siemens` | مواصَلة ببادئة (مِلّي سيمنز) |
