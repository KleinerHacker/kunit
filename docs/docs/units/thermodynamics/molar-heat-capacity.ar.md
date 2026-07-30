# السعة الحرارية المولية

الحزمة: `org.pcsoft.framework.kunit.thermo.molarheatcapacity`
الوحدة الأساسية: **جول لكل مول-كلفن** (`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`)

النوع: **وحدة مركّبة**

السعة الحرارية المولية هي [السعة الحرارية](heat-capacity.md) لمادّة ما *لكل مول*: `J/(mol·K)`. وهي
الصيغة الطبيعية للغازات وللديناميكا الحرارية الكيميائية، حيث تُحسب الكمّيات بالمولات لا بالكيلوغرامات
(تلك هي [السعة الحرارية النوعية](specific-heat-capacity.md)).

يغلّف `KMolarHeatCapacityUnitInstance` كائن `KMixedUnitInstance` مكوّن من خمسة حدود بالضبط بالصيغة
القياسية `mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹` (`kg·m²·s⁻²·mol⁻¹·K⁻¹`). بُعد
درجة الحرارة هو مجموعة **الفرق**، وليس درجة الحرارة المطلقة الأفينية أبدًا.

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ J/(mol·K) |
|---|---|---:|---:|
| جول لكل مول-كلفن | `J/(mol·K)` | `joulesPerMoleKelvin` | 1.0 |
| سعرة لكل مول-كلفن | `cal/(mol·K)` | `caloriesPerMoleKelvin` | 4.184 |

كلاهما يدعمان نطاق بادئات النظام الدولي الكامل (`kilo.joulesPerMoleKelvin`،
`milli.joulesPerMoleKelvin`، …).

## ثابت الغاز

تعرض المجموعة القيمة الدقيقة لثابت الغاز المولي في النظام الدولي كـ`GAS_CONSTANT`
(8.31446261815324 J/(mol·K)) — وهو `Double` عادي، لذا يمكن أن يخدم كعامل وكقراءة في آنٍ واحد.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## مثال واقعي: تسخين النيتروجين (فحص منطقي لدولونغ-بوتي)

النيتروجين ثنائي الذرّة له `c_p ≈ 29.1 J/(mol·K)`. كم من الطاقة يستغرق تسخين 3 مولات بمقدار 50 K، وكم
ذلك لكل مول؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val nitrogen = 29.1 of joulesPerMoleKelvin
val sample = 3 of moles
val rise = KTemperatureDifference.ofKelvin(50)

// route 1: the sample's heat capacity, then the energy
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// route 2: per mole first
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J - identical
```

## الحساب باستخدام الوحدات المجاورة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `heatCapacity / amountOfSubstance` | `KMolarHeatCapacityUnitInstance` | خاصّية المادّة من عيّنة |
| `molarEnergy / temperatureDifference` | `KMolarHeatCapacityUnitInstance` | نفسه، عبر الطاقة المولية |
| `molarHeatCapacity * amountOfSubstance` | `KHeatCapacityUnitInstance` | السعة الحرارية للعيّنة |
| `amountOfSubstance * molarHeatCapacity` | `KHeatCapacityUnitInstance` | نفسه (تبادلي) |
| `heatCapacity / molarHeatCapacity` | `KAmountOfSubstanceUnitInstance` | كمّية المادة |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance` | الطاقة لكل مول |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance` | نفسه (تبادلي) |
| `molarEnergy / molarHeatCapacity` | `KTemperatureDifferenceUnitInstance` | الارتفاع الممكن |

## التفكيكات

جميع التفكيكات الثلاثة تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `heatCapacity / amountOfSubstance` | معامل مكتوب بنوع صريح | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference` | معامل مكتوب بنوع صريح | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | تعبير أصلي + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
val viaMolarEnergy  = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit() /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toMolarHeatCapacity()

viaHeatCapacity == viaMolarEnergy // true
viaHeatCapacity == native         // true - all are 1.0 J/(mol·K)
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/(mol·K)` | `joulesPerMoleKelvin` | السعة الحرارية المولية، الوحدة الأساسية |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | الأبعاد الأساسية |
| `cal/(mol·K)` | `caloriesPerMoleKelvin` | سعرة لكل مول-كلفن |
| `R` | `GAS_CONSTANT of joulesPerMoleKelvin` | ثابت الغاز المولي، 8.3145 J/(mol·K) |
| `C_m = C / n` | `(58.2 of joulesPerKelvin) / (2 of moles)` | من السعة الحرارية ÷ الكمّية |
| `C_m = ΔH_m / ΔT` | `(58.2 of joulesPerMole) / rise` | من الطاقة المولية ÷ ارتفاع درجة الحرارة |
| `Q = C_m · n · ΔT` | `nitrogen * sample * rise` | إجمالي الطاقة |
