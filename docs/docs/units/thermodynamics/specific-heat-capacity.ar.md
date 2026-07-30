# السعة الحرارية النوعية

الحزمة: `org.pcsoft.framework.kunit.thermo.specificheatcapacity`
الوحدة الأساسية: **جول لكل كيلوغرام-كلفن** (`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`)

النوع: **وحدة مركّبة**

السعة الحرارية النوعية هي [السعة الحرارية](heat-capacity.md) لمادّة *لكل وحدة كتلة*: `J/(kg·K)`.
وهي الخاصّية المادّية وراء كل حساب "كم من الطاقة لتسخين هذا".

يغلّف `KSpecificHeatCapacityUnitInstance` كائن `KMixedUnitInstance` مكوّن من ثلاثة حدود بالضبط
بالصيغة القياسية `distance² · time⁻² · temperature⁻¹` (`m²·s⁻²·K⁻¹`) — يُلغى بُعد الكتلة تمامًا كما في
[الطاقة النوعية](specific-energy.md). بُعد درجة الحرارة هو مجموعة **الفرق**
(`KTemperatureDifferenceUnit`)، وليس درجة الحرارة المطلقة الأفينية أبدًا.

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ J/(kg·K) |
|---|---|---:|---:|
| جول لكل كيلوغرام-كلفن | `J/(kg·K)` | `joulesPerKilogramKelvin` | 1.0 |
| سعرة لكل غرام-كلفن | `cal/(g·K)` | `caloriesPerGramKelvin` | 4184.0 |
| وحدة حرارية بريطانية لكل رطل-°F | `Btu/(lb·°F)` | `btusPerPoundFahrenheit` | 4186.8 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`kilo.joulesPerKilogramKelvin`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0 (water is 1 cal/(g·K) by definition of the calorie)
```

## مثال واقعي: تسخين حوض استحمام

يُسخَّن 150 لترًا من الماء (150 kg) من 12 °C إلى 40 °C. للماء سعة حرارية نوعية قدرها
4184 J/(kg·K).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val bath = 150 of kilo.grams
val rise = (40 of celsius) - (12 of celsius)  // 28 K

// route 1: build the tub's heat capacity first
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// route 2: go via specific energy (energy per kilogram) instead
val perKilogram = water * rise                // KSpecificEnergyUnitInstance, 117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ - identical
```

## الحساب باستخدام الوحدات المجاورة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `heatCapacity / mass` | `KSpecificHeatCapacityUnitInstance` | خاصّية المادّة من جسم |
| `specificEnergy / temperatureDifference` | `KSpecificHeatCapacityUnitInstance` | نفسه، عبر الطاقة النوعية |
| `specificHeatCapacity * mass` | `KHeatCapacityUnitInstance` | السعة الحرارية للجسم |
| `mass * specificHeatCapacity` | `KHeatCapacityUnitInstance` | نفسه (تبادلي) |
| `heatCapacity / specificHeatCapacity` | `KMassUnitInstance` | كتلة الجسم |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance` | الطاقة لكل كيلوغرام |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance` | نفسه (تبادلي) |
| `specificEnergy / specificHeatCapacity` | `KTemperatureDifferenceUnitInstance` | الارتفاع الممكن |

## التفكيكات

جميع التفكيكات الثلاثة تُنتج نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `heatCapacity / mass` | معامل مكتوب بنوع صريح | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | معامل مكتوب بنوع صريح | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹` | تعبير أصلي + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity   = (1 of joulesPerKelvin) / (1 of kilo.grams)
val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
val native = (
    ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toSpecificHeatCapacity()

viaHeatCapacity == viaSpecificEnergy // true
viaHeatCapacity == native            // true - all are 1.0 J/(kg·K)
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/(kg·K)` | `joulesPerKilogramKelvin` | السعة الحرارية النوعية، الوحدة الأساسية |
| `m²·s⁻²·K⁻¹` | `(meters pow 2) / (seconds pow 2) / ΔK` | نفس الكمّية بالأبعاد الأساسية |
| `cal/(g·K)` | `caloriesPerGramKelvin` | سعرة لكل غرام-كلفن |
| `c = C / m` | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | من السعة الحرارية ÷ الكتلة |
| `c = q / ΔT` | `(8368 of joulesPerKilogram) / rise` | من الطاقة النوعية ÷ ارتفاع درجة الحرارة |
| `C = c · m` | `water * bath` | السعة الحرارية للجسم من المادّة × الكتلة |
| `Q = c · m · ΔT` | `water * bath * rise` | إجمالي الطاقة |
