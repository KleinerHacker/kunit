# الجدول الدوري

الحزمة: `org.pcsoft.framework.kunit`
الأنواع: `KChemicalElement`، `KChemicalElementCategory`

`KChemicalElement` هو المكان المركزي للعناصر الكيميائية. وهو تعداد (enum) بسيط في Kotlin، لذا فإن كل عنصر ثابت وقت
الترجمة — وكل ثابت فيزيائي يحمله هو **نسخة وحدة محكومة بالنوع** من هذه المكتبة، جاهزة للتركيب مع كل شيء آخر.

## النطاق

يغطّي التعداد الجدول الدوري المدرسي الكلاسيكي: المجموعات الرئيسية والفرعية للدورات 1-6 **بدون كتلة f**. لذا فإن
اللانثانيدات (57-71) غير موجودة — يقفز العدد الذري من الباريوم (56) إلى الهافنيوم (72) — كما لا تُدرَج الأكتينيدات ولا
العناصر عبر الأكتينيدية. وهذا يجعل المجموع 71 مُدخَلًا.

## البيانات الموضعية

| الخاصية         | النوع                      | المعنى                                                              |
|-----------------|----------------------------|---------------------------------------------------------------------|
| `ordinalNumber` | `Int`                      | العدد الذري Z، أي الفهرس في الجدول الدوري                           |
| `symbol`        | `String`                   | رمز العنصر، مثل `"Pb"`                                              |
| `fullName`      | `String`                   | الاسم الإنجليزي، مثل `"Lead"` (مدخل التعداد هو `LEAD`)              |
| `period`        | `Int`                      | الدورة (الصف)، 1-6                                                  |
| `mainGroup`     | `Int?`                     | المجموعة الرئيسية 1-8 لعناصر الكتلة s/p، و`null` للفلزات الانتقالية |
| `subGroup`      | `Int?`                     | المجموعة الفرعية 1-8 لعناصر الكتلة d، و`null` غير ذلك               |
| `category`      | `KChemicalElementCategory` | الفصيلة الكيميائية                                                  |

يكون بالضبط واحد من `mainGroup` و`subGroup` مضبوطًا. تستخدم المجموعات الفرعية الترقيم الكلاسيكي (Cu = 1، Zn = 2، Sc =
3 … Fe/Co/Ni = 8).

يحتوي `KChemicalElementCategory` على المدخلات `HYDROGEN`، `ALKALI_METAL`، `ALKALINE_EARTH_METAL`،
`TRANSITION_METAL`، `POST_TRANSITION_METAL`، `METALLOID`، `NONMETAL`، `HALOGEN` و`NOBLE_GAS`.

## بيانات الوحدات

| الخاصية                 | النوع                                | راية التوفّر                |
|-------------------------|--------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`             | متوفّرة دائمًا               |
| `molarVolume`           | `KMolarVolumeUnitInstance?`          | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`               | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`               | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`              | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`          | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`          | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?` | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`  | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`               | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`          | `hasElectricalResistivity` |
| `electronegativity`     | `Double?` (بولنغ، بلا بُعد)           | `hasElectronegativity`     |

الثوابت غير المعرَّفة بمعنى فعلي لعنصر ما تكون `null` — الهليوم ليست له نقطة انصهار عند الضغط العادي، والزرنيخ يتسامى
بدلًا من الغليان، والأستاتين نادر جدًا بحيث لا تتوفّر كثافة مقيسة له. وتجيب الخاصية المطابقة `has...` على السؤال نفسه
دون التعامل مع القيم الفارغة.

يُشتقّ `molarVolume` من `molarMass / density`، أي أنه يستخدم التفكيك الثاني لمجموعة
[الحجم المولي](units/thermodynamics/molar-volume.md).

## مثال واقعي: كم يزن سبيكة ذهب؟

تبلغ أبعاد سبيكة ذهب قياسية 7 cm × 4 cm × 2 cm. كم يبلغ وزنها، وكم مولًا من الذهب يمثّل ذلك؟

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## مثال واقعي: تسخين مقلاة نحاسية

كم من الطاقة يلزم لتسخين مقلاة نحاسية وزنها 1.2 kg من 20 °C إلى 200 °C؟

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## البحث

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON (case-insensitive)
KChemicalElement.ofFullName("iron")    // IRON (case-insensitive)
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null - lanthanides are not part of this table
KChemicalElement.ofMainGroup(4, 6)     // LEAD (main group 4, period 6)
KChemicalElement.ofSubGroup(8, 4)      // IRON (sub group 8, period 4 - first of Fe/Co/Ni)
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

تضمّ المجموعة الفرعية 8 ثلاثة عناصر لكل دورة؛ وتُعيد `ofSubGroup` أوّلها (Fe، Ru، Os) — استخدم
`ofPeriod` مع تصفية للحصول عليها جميعًا.

## الترميز

| الرياضيات     | Kotlin                                         | المعنى                                 |
|---------------|------------------------------------------------|----------------------------------------|
| `Z`           | `element.ordinalNumber`                        | العدد الذري                            |
| `M`           | `element.molarMass`                            | الكتلة المولية، `g/mol`                |
| `V_m = M / ρ` | `element.molarVolume`                          | الحجم المولي، `m³/mol`                 |
| `ρ`           | `element.density`                              | الكثافة                                |
| `T_m`، `T_b`  | `element.meltingPoint`، `element.boilingPoint` | نقطة الانصهار / الغليان بالكلفن        |
| `m = ρ · V`   | `gold.density!! * volume`                      | الكتلة من الكثافة × الحجم              |
| `n = m / M`   | `mass / gold.molarMass`                        | كمّية المادة من الكتلة ÷ الكتلة المولية |
