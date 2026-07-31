# الكتلة المولية

الحزمة: `org.pcsoft.framework.kunit.thermo.molarmass`
الوحدة الأساسية: **غرام لكل مول** (`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`)

النوع: **وحدة مركّبة**

الكتلة المولية هي الكتلة لكل كمّية مادة: `mass / amountOfSubstance` (`g/mol`). وهي الجسر بين العالم العياني (الغرامات
على الميزان) وعالم الجسيمات (المولات)، وتساوي عدديًا الكتلة الذرية أو الجزيئية النسبية لمادة ما.

يغلّف `KMolarMassUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدين بالضبط بالصيغة القياسية
`mass¹ · substance⁻¹` (`g·mol⁻¹`)، مطبَّعًا دائمًا إلى g/mol. ولأن المكتبة تطبّع الكتل إلى غرامات، فإن أساس المكوّن
الخام *هو* الوحدة الأساسية المسمّاة — دون الحاجة إلى عامل وسيط.

مقسومةً على الكثافة تصبح [الحجم المولي](molar-volume.md)؛ ويعرض كل عنصر من [الجدول الدوري](../../periodic-table.md)
كتلته المولية كقيمة من هذه المجموعة.

## الوحدات المسمّاة

| الوحدة           | الرمز      |        الرمز البرمجي | 1 وحدة بـ g/mol |
|------------------|------------|---------------------:|----------------:|
| غرام لكل مول     | `g/mol`    |       `gramsPerMole` |             1.0 |
| كيلوغرام لكل مول | `kg/mol`   |   `kilogramsPerMole` |          1000.0 |
| رطل لكل رطل-مول  | `lb/lbmol` | `poundsPerPoundMole` |             1.0 |
| دالتون لكل وحدة  | `Da`       |   `daltonsPerEntity` |   1.00000000105 |

يُعرَّف الرطل-مول بحيث تساوي كتلته بالأرطال الكتلة المولية، مما يجعل `lb/lbmol` مطابقًا عدديًا لـ
`g/mol`. ومنذ إعادة تعريف النظام الدولي عام 2019 لم يعد ثابت الكتلة المولية يساوي بالضبط 1 g/mol، ومن هنا عامل الدالتون.
جميع الوحدات تدعم نطاق بادئات النظام الدولي الكامل (`kilo.gramsPerMole`،
`milli.kilogramsPerMole`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 18.015 Da per molecule
```

## مثال واقعي: وزن مول واحد

تتطلّب وصفة 0.25 mol من ملح الطعام (NaCl، 58.44 g/mol). كم يجب أن تزن — وكم عدد المولات في عبوة زنتها 500 g؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// How much mass are 0.25 mol?
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// How many moles are in a 500 g package?
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// And the molar mass itself, measured from a weighed sample:
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## الحساب باستخدام الوحدات الأساسية (الكتلة وكمّية المادة)

| التعبير                         | نوع النتيجة                      | المعنى                          |
|---------------------------------|----------------------------------|---------------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | الكتلة المولية                  |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | الكتلة الكلّية                   |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | الكتلة الكلّية (تبادلي)          |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | كمّية المادة المعنيّة             |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [الحجم المولي](molar-volume.md) |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك                    | الصيغة                       | النتيجة                         |
|----------------------------|------------------------------|---------------------------------|
| `mass / amountOfSubstance` | معامل مكتوب بنوع صريح        | `KMolarMassUnitInstance` مباشرة |
| `mass · substance⁻¹`       | تعبير أصلي + `toMolarMass()` | `KMolarMassUnitInstance`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// typed operator form
val typed = (18.015 of grams) / (1 of moles)

// native base-dimension form (g·mol⁻¹), recognised by toMolarMass()
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true - both are 18.015 g/mol
```

تتعرّف `toMolarMass()` على **الصيغة القياسية فقط**؛ أما الشكل الخاطئ فيرمي
`IllegalStateException`.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات     | Kotlin                               | المعنى                                     |
|---------------|--------------------------------------|--------------------------------------------|
| `g/mol`       | `gramsPerMole`                       | الكتلة المولية، الوحدة الأساسية — رمز مسمّى |
| `g·mol⁻¹`     | `grams / moles`                      | نفس الكمّية بالأبعاد الأساسية               |
| `kg/mol`      | `kilogramsPerMole`                   | كيلوغرام لكل مول                           |
| `Da`          | `daltonsPerEntity`                   | دالتون لكل وحدة أولية                      |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | الكتلة المولية من الكتلة ÷ الكمّية          |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | الكتلة من الكتلة المولية × الكمّية          |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | الكمّية من الكتلة ÷ الكتلة المولية          |
| `V_m = M / ρ` | `molarMass / density`                | الحجم المولي من الكتلة المولية ÷ الكثافة   |
