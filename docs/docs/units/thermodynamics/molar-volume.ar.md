# الحجم المولي

الحزمة: `org.pcsoft.framework.kunit.thermo.molarvolume`
الوحدة الأساسية: **متر مكعّب لكل مول** (`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`)

النوع: **وحدة مركّبة**

الحجم المولي هو الحجم لكل كمّية مادة: `volume / amountOfSubstance` (`m³/mol`). بالنسبة للغاز المثالي يكون نفسه لكل مادة
(22.711 l/mol عند 0 °C و100 kPa)؛ أما بالنسبة للمواد الصلبة والسوائل فينتج من
[الكتلة المولية](molar-mass.md) والكثافة.

يغلّف `KMolarVolumeUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدين بالضبط بالصيغة القياسية
`distance³ · substance⁻¹` (`m³·mol⁻¹`)، مطبَّعًا دائمًا إلى m³/mol. ويُخزَّن كلا المكوّنين في الوحدة الأساسية
لمجموعتهما، لذا فإن أساس المكوّن الخام *هو* الوحدة الأساسية المسمّاة.

يشتقّ كل عنصر من [الجدول الدوري](../../periodic-table.md) حجمه المولي من كتلته المولية وكثافته عبر التفكيك الثاني أدناه.

## الوحدات المسمّاة

| الوحدة               | الرمز      |             الرمز البرمجي | 1 وحدة بـ m³/mol |
|----------------------|------------|--------------------------:|-----------------:|
| متر مكعّب لكل مول     | `m^3/mol`  |      `cubicMetersPerMole` |              1.0 |
| لتر لكل مول          | `l/mol`    |           `litersPerMole` |            0.001 |
| سنتيمتر مكعّب لكل مول | `cm^3/mol` | `cubicCentimetersPerMole` |           1.0e-6 |

جميع الوحدات تدعم نطاق بادئات النظام الدولي الكامل (`milli.cubicMetersPerMole`، `milli.litersPerMole`، …). وتوفّر الحزمة
إضافةً الثابت `MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464 (m³/mol)، وهو الحجم المولي لغاز مثالي عند الظروف القياسية.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## مثال واقعي: بالون مملوء بالهيليوم

كم حيّزًا يشغل مولان من غاز مثالي عند الظروف القياسية — وكم مولًا يسع بالون سعته 5 لتر؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// Volume of 2 moles
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// How many moles fit into a 5 l balloon?
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// And the molar volume measured from a filled balloon:
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## مثال واقعي: حجم مول من الماء

كتلة الماء المولية 18.015 g/mol وكثافته 1 kg/l، لذا يشغل المول الواحد نحو 18 cm³ — أي بمقدار ملعقة طعام تقريبًا.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## الحساب باستخدام الوحدات الأساسية

| التعبير                           | نوع النتيجة                      | المعنى                          |
|-----------------------------------|----------------------------------|---------------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | الحجم المولي                    |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | الحجم المولي (التفكيك الثاني)   |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | الحجم الكلّي                     |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | الحجم الكلّي (تبادلي)            |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | كمّية المادة المعنيّة             |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [الكتلة المولية](molar-mass.md) |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | الكتلة المولية (تبادلي)         |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | الكثافة                         |

## التفكيكات

جميع التفكيكات تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك                      | الصيغة                         | النتيجة                           |
|------------------------------|--------------------------------|-----------------------------------|
| `volume / amountOfSubstance` | معامل مكتوب بنوع صريح          | `KMolarVolumeUnitInstance` مباشرة |
| `molarMass / density`        | معامل مكتوب بنوع صريح          | `KMolarVolumeUnitInstance` مباشرة |
| `distance³ · substance⁻¹`    | تعبير أصلي + `toMolarVolume()` | `KMolarVolumeUnitInstance`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// typed operator form: volume / amount
val typedVolume = (0.018015 of liters) / (1 of moles)

// typed operator form: molar mass / density
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// native base-dimension form (m³·mol⁻¹), recognised by toMolarVolume()
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true - all are 1.8015e-5 m³/mol
```

تتعرّف `toMolarVolume()` على **الصيغة القياسية فقط**؛ أما الشكل الخاطئ فيرمي
`IllegalStateException`.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات     | Kotlin                               | المعنى                                   |
|---------------|--------------------------------------|------------------------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | الحجم المولي، الوحدة الأساسية — رمز مسمّى |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | نفس الكمّية بالأبعاد الأساسية             |
| `l/mol`       | `litersPerMole`                      | لتر لكل مول                              |
| `cm³/mol`     | `cubicCentimetersPerMole`            | سنتيمتر مكعّب لكل مول                     |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | الحجم المولي من الحجم ÷ الكمّية           |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | الحجم المولي من الكتلة المولية ÷ الكثافة |
| `V = V_m · n` | `ideal * (2 of moles)`               | الحجم من الحجم المولي × الكمّية           |
| `n = V / V_m` | `(5 of liters) / ideal`              | الكمّية من الحجم ÷ الحجم المولي           |
| `ρ = M / V_m` | `molarMass / molarVolume`            | الكثافة من الكتلة المولية ÷ الحجم المولي |
