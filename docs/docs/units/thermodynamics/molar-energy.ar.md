# الطاقة المولية

الحزمة: `org.pcsoft.framework.kunit.thermo.molarenergy`
الوحدة الأساسية: **جول لكل مول** (`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`)

النوع: **وحدة مركّبة**

الطاقة المولية هي الطاقة لكل كمّية مادة: `energy / amountOfSubstance` (`J/mol`). وحسب السياق تُسمّى
نفس الكمّية *الإنثالبي المولي* أو *إنثالبي التفاعل* أو *طاقة الرابطة*.

يغلّف `KMolarEnergyUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود بالضبط بالصيغة القياسية
`mass¹ · distance² · time⁻² · substance⁻¹` (`kg·m²·s⁻²·mol⁻¹`)، مطبَّعًا دائمًا إلى J/mol.

ولكل وحدة درجة حرارة تصبح [السعة الحرارية المولية](molar-heat-capacity.md)؛ ولكل كيلوغرام بدلًا من
مول تصبح [الطاقة النوعية](specific-energy.md).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ J/mol |
|---|---|---:|---:|
| جول لكل مول | `J/mol` | `joulesPerMole` | 1.0 |
| سعرة لكل مول | `cal/mol` | `caloriesPerMole` | 4.184 |
| إلكترون فولت لكل وحدة | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

يحوّل رمز الإلكترون فولت لكل وحدة طاقة *لكل جسيم* إلى طاقة *لكل مول* — عامله هو ثابت فارادي. جميع
الوحدات تدعم نطاق بادئات النظام الدولي الكامل (`kilo.joulesPerMole`، `kilo.caloriesPerMole`،
`milli.electronVoltsPerEntity`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV per molecule
```

## مثال واقعي: احتراق الهيدروجين

إنثالبي تكوين الماء السائل هو −286 kJ/mol. كم من الطاقة تُطلَق عند احتراق 4 مولات من الهيدروجين، وكم
ذلك لكل جزيء؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// per molecule, in the chemists' unit
formation into electronVoltsPerEntity // ≈ -2.964 eV

// Inverse: how much substance does 1 MJ correspond to?
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## الحساب باستخدام الوحدات الأساسية (الطاقة وكمّية المادة)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | الطاقة المولية |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | الطاقة الكلّية |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | الطاقة الكلّية (تبادلي) |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | كمّية المادة المعنيّة |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `energy / amountOfSubstance` | معامل مكتوب بنوع صريح | `KMolarEnergyUnitInstance` مباشرة |
| `mass · distance² · time⁻² · substance⁻¹` | تعبير أصلي + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// typed operator form
val typed = (1 of joules) / (1 of moles)

// native base-dimension form (kg·m²·s⁻²·mol⁻¹), recognised by toMolarEnergy()
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true - both are 1.0 J/mol
```

تتعرّف `toMolarEnergy()` على **الصيغة القياسية فقط**؛ أما الشكل الخاطئ فيرمي
`IllegalStateException`.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/mol` | `joulesPerMole` | الطاقة المولية، الوحدة الأساسية — رمز مسمّى |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | نفس الكمّية بالأبعاد الأساسية |
| `kJ/mol` | `kilo.joulesPerMole` | كيلوجول لكل مول |
| `eV` (لكل جسيم) | `electronVoltsPerEntity` | إلكترون فولت لكل وحدة أولية |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | الطاقة المولية من الطاقة ÷ الكمّية |
| `Q = ΔH_m · n` | `formation * hydrogen` | الطاقة من الطاقة المولية × الكمّية |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | الكمّية من الطاقة ÷ الطاقة المولية |
