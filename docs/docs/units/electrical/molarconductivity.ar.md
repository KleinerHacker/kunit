# الموصلية المولية

الحزمة: `org.pcsoft.framework.kunit.electric.molarconductivity`
الوحدة الأساسية: **سيمنز متر مربع لكل مول**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

النوع: **وحدة مركّبة**

الموصلية المولية `Λ` لمحلول إلكتروليتي هي [موصليته](conductivity.ar.md) مُطبَّعةً بواسطة
[التركيز](../thermodynamics/concentration.ar.md): `Λ = κ / c`. قسمة التركيز تجعل المحاليل ذات القوى المختلفة قابلة
للمقارنة — فهي تجيب عن سؤال "ما مدى جودة توصيل *هذا الأيون*"، لا "ما مدى جودة توصيل هذا الكأس بالتحديد".

الشكل القياسي للبُعد الأساسي هو `mass⁻¹ · time³ · current² · substance⁻¹`. يُلغى بُعد الطول تمامًا: الموصلية تسهم
بـ `length⁻³` والتركيز في المقام يسهم بـ `length⁻³` أخرى.

## الوحدات المسمّاة

| الوحدة                             | الرمز       |                            الرمز البرمجي | 1 وحدة بـ S·m²/mol |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| سيمنز متر مربع لكل مول            | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| سيمنز سنتيمتر مربع لكل مول        | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

عادةً ما تُذكَر جداول الكيمياء الكهربائية بوحدة S·cm²/mol؛ ويُكتب الشكل القياسي الدولي عادةً ببادئة milli
(`milli.siemensSquareMetersPerMole`). تدعم كل الرموز البرمجية جميع بادئات SI.

## التفكيك

تمتلك هذه المجموعة تفكيكًا واحدًا، وكلا شكليه ينتجان نسخة متساوية القيمة من النوع نفسه. يُبنى الشكل الأصلي من
**قوالب الوحدات** لأنّ المجموعة تحمل حدّ كتلة: القيمة المختلطة الخام هي حاصل الضرب القائم على الغرام، بينما تخزّن
النسخة المحكومة بالنوع قيمتها في الوحدة المسمّاة.

| الشكل            | التعبير                                                          |
|------------------|---------------------------------------------------------------------|
| معامِل محكوم بالنوع | `conductivity / concentration`                                      |
| أصلي (`toX()`)    | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## الحساب مع المجموعة

| التعبير                              | نوع النتيجة                      | المعنى       |
|-------------------------------------|-----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | قانون كولراوش |

ينصّ قانون كولراوش للهجرة المستقلة للأيونات على أنّه عند التخفيف اللانهائي تكون الموصلية المولية هي **مجموع**
إسهامات الأيونات — وهو بالضبط عملية `+` من النوع نفسه في هذه المجموعة.

## مثال واقعي — قانون كولراوش لكلوريد البوتاسيوم KCl

الموصليات الأيونية الحدّية هي 7.35 mS·m²/mol لأيون K⁺ و7.63 mS·m²/mol لأيون Cl⁻. مجموعهما هو الموصلية المولية
الحدّية لكلوريد البوتاسيوم، وضربه في تركيز ما يعطي الموصلية التي يقرؤها جهاز قياس:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // كولراوش
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85 (قيمة الجدول)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## دلالة القيمة

يقارن `equals`/`hashCode` **قيمة S·m²/mol المُطبَّعة**، لذا
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)`. تعرض `toString()` القيمة
بالوحدة الأساسية: `"0.0126 S*m^2/mol"`.

## انظر أيضًا

* [الموصلية](conductivity.ar.md) — البسط.
* [تركيز كمّية المادة](../thermodynamics/concentration.ar.md) — المقام.
* [التوصيلية](conductance.ar.md) — الكمّية غير المُطبَّعة التي يقيسها الجهاز.
* [نظرة عامة على الهندسة الكهربائية](overview.ar.md)
