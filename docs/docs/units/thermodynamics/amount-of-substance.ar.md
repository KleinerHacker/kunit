# كمّية المادة

الحزمة: `org.pcsoft.framework.kunit.thermo.amountofsubstance`
الوحدة الأساسية: **مول** (`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`)

النوع: **وحدة أصلية**

كمّية المادة هي إحدى الكمّيات السبع الأساسية في النظام الدولي — كمّية قابلة للقياس المباشر وغير
مركّبة، ومن ثمّ فهي **وحدة أصلية**. `KAmountOfSubstanceUnitInstance` هو الشكل الغلافي الأحادي البُعد
البسيط: حدّ واحد من `KAmountOfSubstanceUnit.BASE` (مول) بالأس 1، مطبَّع دائمًا إلى المول.

وهي أساس كل كمّية *مولية* في مجال الديناميكا الحرارية
([الطاقة المولية](molar-energy.md)، [السعة الحرارية المولية](molar-heat-capacity.md)).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بالمول |
|---|---|---:|---:|
| مول | `mol` | `moles` | 1.0 |
| مول-رطل | `lbmol` | `poundMoles` | 453.59237 |

كلتاهما تدعمان نطاق بادئات النظام الدولي الكامل (`milli.moles`، `micro.moles`، `kilo.moles`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0 (normalized to moles)
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## ثابت أفوغادرو

تعرض المجموعة القيمة الدقيقة لثابت أفوغادرو في النظام الدولي عبر `AVOGADRO_CONSTANT`
(6.02214076e23 mol⁻¹)، والدالة المساعِدة `particleCount()` على أي نسخة. كلاهما يُعيد `Double` عاديًا،
لأن عدد الجسيمات كمّية بلا أبعاد.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24 particles
```

## مثال واقعي: إذابة ملح الطعام

كم مول من كلوريد الصوديوم (كتلته المولية 58.44 g/mol) يوجد في 25 g من ملح الطعام، وكم وحدة صيغية
يعادل ذلك؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // g/mol for NaCl
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23 formula units
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - : same group, automatic conversion between different units and prefixes
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// comparisons (by normalized mole value)
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

ضرب أو قسمة كمّية مادة في كمّية أخرى ينتقل إلى محرّك الوحدات المختلطة العام ما لم توجد نتيجة محكومة
بالنوع — فمثلًا `energy / amountOfSubstance` هي [طاقة مولية](molar-energy.md) محكومة بالنوع.

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس
بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `mol` | `moles` | كمّية المادة، الوحدة الأساسية |
| `mmol` | `milli.moles` | ميلي مول |
| `kmol` | `kilo.moles` | كيلومول |
| `lbmol` | `poundMoles` | مول-رطل (وحدة هندسية إمبراطورية) |
| `n = m / M` | `(sample.value / molarMass) of moles` | الكمّية من الكتلة ÷ الكتلة المولية |
| `N = n · N_A` | `n.particleCount()` | عدد الجسيمات من الكمّية × ثابت أفوغادرو |
