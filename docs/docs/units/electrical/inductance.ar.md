# المحاثة

الحزمة: `org.pcsoft.framework.kunit.electric.inductance`
الوحدة الأساسية: **هنري** (`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

النوع: **وحدة مركّبة**

المحاثة وحدة **مركّبة**: التركيب `mass · length² · time⁻² · current⁻²` (`kg·m²·s⁻²·A⁻²`). يغلّف
`KInductanceUnitInstance` نسخةَ `KMixedUnitInstance` من أربعة حدود — `KMassUnit.BASE` (غرام) عند
`+1`، و`KDistanceUnit.BASE` (متر) عند `+2`، و`KTimeUnit.BASE` (ثانية) عند `-2`، و`KElectricCurrentUnit.BASE` (أمبير) عند
`-2`. ولأنّ مكوّن الكتلة في المكتبة مُطبَّع إلى **الغرامات** (لا الكيلوغرامات)، فإنّ الهنري أكبر 1000× من الأساس الخام
للمكوّنات؛ القيمة المخزّنة مُطبَّعة إلى الهنري.

## بناء محاثة

ابنِ محاثة برمز مسمّى، أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1 (تُستخدم مع
`of`/`into`):

| المحاثة             | الرمز   |     الرمز البرمجي |    1 وحدة بـ H |
|---------------------|---------|------------------:|---------------:|
| هنري                | `H`     |         `henries` |            1.0 |
| ويبر لكل أمبير      | `Wb/A`  | `webersPerAmpere` |            1.0 |
| أب-هنري (CGS-EMU)   | `abH`   |       `abhenries` |         1.0e-9 |
| ستات-هنري (CGS-ESU) | `statH` |     `stathenries` | 8.987551787e11 |

تدعم الوحدات المسمّاة بادئات SI عبر `KPrefixBuilder` (`milli.henries`، `micro.henries`،
`nano.henries`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## تفكيكات متعدّدة

يمكن بلوغ المحاثة عبر عدّة **تفكيكات مكافئة**، كلّها تُنتج محاثة متساوية القيمة:

| التعبير                         | نوع النتيجة               | المعنى                                       |
|---------------------------------|---------------------------|----------------------------------------------|
| `flux / current`                | `KInductanceUnitInstance` | التعريف `L = Φ / I`                          |
| `resistance / frequency`        | `KInductanceUnitInstance` | صيغة المفاعلة `L = X / ω` (`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | عبر `.toInductance()`     | تعبير `kg·m²·s⁻²·A⁻²` الأصلي القياسي         |

تُعيد صيغ المعامِلات المحكومة بالنوع محاثةً مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toInductance()` (الذي يتعرّف فقط على الشكل القياسي ويُطلق `IllegalStateException` خلاف ذلك). كل
المسارات متساوية القيمة.

تربط المعاملات العكسية التدفّق المغناطيسي والتيار والتردّد والمقاومة معًا:

| التعبير                  | نوع النتيجة                    | المعنى               |
|--------------------------|--------------------------------|----------------------|
| `inductance * current`   | `KMagneticFluxUnitInstance`    | `Φ = L · I` (تبادلي) |
| `flux / inductance`      | `KElectricCurrentUnitInstance` | `I = Φ / L`          |
| `inductance * frequency` | `KResistanceUnitInstance`      | `X = ω · L`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.inductance.*

// مثال واقعي - ملف خانق في مزوّد قدرة مفتاحي: ملف 470 µH يمرّ فيه 2 A يربط تدفّقًا 0.00094 Wb،
// وعند تردّد زاوي 100 kHz يقدّم مفاعلة قدرها 47 Ω.
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance، 0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance، 47 Ω

// المحاثة نفسها من تعريفها ومن صيغة المفاعلة:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// المحاثة نفسها كتعبير kg·m²·s⁻²·A⁻² الأصلي:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## النفاذية المغناطيسية العكسية

**النفاذية المغناطيسية العكسية** `Λ` لدارة مغناطيسية هي مقلوب
[الممانعة المغناطيسية](reluctance.md) الخاصة بها، `Λ = 1 / Rm`. وهي **مطابقة بُعديًا** للمحاثة وتُقاس بالهنري أيضًا، لذا
تُنمذجها KUnit بهذه المجموعة وبالرمز `H`؛ ولا يوجد رمز برمجي منفصل ولا نوع منفصل. تربط معاملات المقلوب المجموعتين معًا:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.inductance.*
import org.pcsoft.framework.kunit.electric.reluctance.*

// دارة مغناطيسية بممانعة Rm = 500 A/Wb لها نفاذية عكسية قدرها 2 mH.
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// …وبالعكس:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance (يهرب من المجموعة)
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

(2 of henries).toString()     // "2.0 H" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات       | Kotlin                                                              | المعنى                                         |
|-----------------|---------------------------------------------------------------------|------------------------------------------------|
| `H`             | `henries`                                                           | المحاثة، الوحدة الأساسية (رمز مسمّى، هنري)      |
| `Wb/A`          | `webersPerAmpere`                                                   | المحاثة كويبر لكل أمبير (رمز مسمّى)             |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | المحاثة ككتلة·طول² / (زمن²·تيار²) (صيغة الكسر) |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | المحاثة نفسها كحاصل ضرب صرف                    |
| `mH`            | `milli.henries`                                                     | محاثة ببادئة (ميلي هنري)                       |
