# الطاقة النوعية

الحزمة: `org.pcsoft.framework.kunit.thermo.specificenergy`
الوحدة الأساسية: **جول لكل كيلوغرام** (`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

النوع: **وحدة مركّبة**

الطاقة النوعية هي الطاقة لكل وحدة كتلة: `energy / mass` (`J/kg`). وتُسمّى نفس الكمّية *الإنثالبي
النوعي* أو *الحرارة الكامنة النوعية* أو *القيمة الحرارية* حسب السياق — وجميعها تشترك في هذه المجموعة.

يغلّف `KSpecificEnergyUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية
`distance² · time⁻²` (`m²·s⁻²`)، مطبَّعًا دائمًا إلى J/kg.

!!! note "بُعد الكتلة يُلغى"
    `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`. لذا لا تحمل الصيغة القياسية **أي** حدّ كتلة على الإطلاق. تربط
    عمليات المقارنة مع `KMassUnitInstance` وحدها بين قاعدة الغرام لمجموعة الكتلة وتعريف "لكل كيلوغرام"
    الخاص بهذه المجموعة.

ولكل وحدة درجة حرارة تصبح [السعة الحرارية النوعية](specific-heat-capacity.md)؛ ولكل مول بدلًا من
كيلوغرام تصبح [الطاقة المولية](molar-energy.md).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ J/kg |
|---|---|---:|---:|
| جول لكل كيلوغرام | `J/kg` | `joulesPerKilogram` | 1.0 |
| سعرة لكل غرام | `cal/g` | `caloriesPerGram` | 4184.0 |
| واط-ساعة لكل كيلوغرام | `Wh/kg` | `wattHoursPerKilogram` | 3600.0 |
| وحدة حرارية بريطانية لكل رطل | `Btu/lb` | `btusPerPound` | 2326.0 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`kilo.joulesPerKilogram`،
`mega.joulesPerKilogram`، `kilo.wattHoursPerKilogram`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val h = 334 of kilo.joulesPerKilogram
h into joulesPerKilogram      // 334_000.0
h into caloriesPerGram        // ≈ 79.83
h into wattHoursPerKilogram   // ≈ 92.78
```

## مثال واقعي: انصهار الجليد

الحرارة الكامنة لانصهار الماء هي 334 kJ/kg. كم من الطاقة يستغرق إذابة كتلة جليد وزنها 2.5 kg؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val latentHeat = 334 of kilo.joulesPerKilogram
val block = 2.5 of kilo.grams

val energy = latentHeat * block     // KEnergyUnitInstance
energy into kilo.joules             // 835.0 kJ
energy into joules                  // 835_000.0 J

// Inverse: how much ice can 1 MJ melt?
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## الحساب باستخدام الوحدات الأساسية (الطاقة والكتلة)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / mass` | `KSpecificEnergyUnitInstance` | الطاقة النوعية |
| `specificEnergy * mass` | `KEnergyUnitInstance` | الطاقة الكلّية |
| `mass * specificEnergy` | `KEnergyUnitInstance` | الطاقة الكلّية (تبادلي) |
| `energy / specificEnergy` | `KMassUnitInstance` | الكتلة المعنيّة |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `energy / mass` | معامل مكتوب بنوع صريح | `KSpecificEnergyUnitInstance` مباشرة |
| `distance² · time⁻²` | تعبير أصلي + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// typed operator form
val typed = (1 of joules) / (1 of kilo.grams)

// native base-dimension form (m²·s⁻²), recognised by toSpecificEnergy()
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true - both are 1.0 J/kg
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/kg` | `joulesPerKilogram` | الطاقة النوعية، الوحدة الأساسية — رمز مسمّى |
| `m²·s⁻²` | `(meters pow 2) / (seconds pow 2)` | نفس الكمّية بالأبعاد الأساسية |
| `kJ/kg` | `kilo.joulesPerKilogram` | كيلوجول لكل كيلوغرام |
| `Wh/kg` | `wattHoursPerKilogram` | واط-ساعة لكل كيلوغرام (كثافة طاقة البطارية) |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | الطاقة النوعية من الطاقة ÷ الكتلة |
| `Q = q · m` | `latentHeat * block` | الطاقة من الطاقة النوعية × الكتلة |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat` | الكتلة من الطاقة ÷ الطاقة النوعية |
