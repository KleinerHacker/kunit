# السعة الحرارية

الحزمة: `org.pcsoft.framework.kunit.thermo.heatcapacity`
الوحدة الأساسية: **جول لكل كلفن** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

النوع: **وحدة مركّبة**

السعة الحرارية هي الطاقة التي يمتصّها جسم لكل وحدة ارتفاع درجة حرارة: `energy / temperature`
(`J/K`). يغلّف `KHeatCapacityUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود بالضبط
بالصيغة القياسية `mass¹ · distance² · time⁻² · temperature⁻¹` (`kg·m²·s⁻²·K⁻¹`)، مطبَّعًا دائمًا إلى J/K.

!!! note "فرق درجة الحرارة، وليس درجة الحرارة المطلقة أبدًا"
    بُعد درجة الحرارة هو مجموعة **الفرق** (`KTemperatureDifferenceUnit`، بالرمز `ΔK`)، وليس أبدًا
    الدرجة المطلقة الأفينية `KTemperatureUnit`. تربط السعة الحرارية الطاقة بـ*فترة* درجة حرارة؛ فمقياس
    مطلق يحمل إزاحة (°C، °F) سيكون خاطئًا فيزيائيًا في حاصل قسمة.

يصف نفس البُعد `J/K` أيضًا **الإنتروبيا** — انظر [الإنتروبيا](entropy.md) لمعرفة لماذا تشارك تلك
الكمّية هذا النوع بدل أن تحصل على نوع خاص بها. ولكل وحدة كتلة تصبح
[السعة الحرارية النوعية](specific-heat-capacity.md)، ولكل مول [السعة الحرارية المولية](molar-heat-capacity.md).

## الوحدات المسمّاة

| الوحدة | الرمز | الرمز البرمجي | 1 وحدة بـ J/K |
|---|---|---:|---:|
| جول لكل كلفن | `J/K` | `joulesPerKelvin` | 1.0 |
| سعرة لكل كلفن | `cal/K` | `caloriesPerKelvin` | 4.184 |
| وحدة حرارية بريطانية لكل درجة فهرنهايت | `Btu/°F` | `btusPerFahrenheit` | ≈ 1899.1005 |

جميعها تدعم نطاق بادئات النظام الدولي الكامل (`kilo.joulesPerKelvin`، `kilo.caloriesPerKelvin`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## مثال واقعي: تسخين غلّاية ماء

يُسخَّن لتر واحد من الماء (4184 J/K) من 20 °C إلى 100 °C. كم من الطاقة يستغرق ذلك؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 1 liter of water
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance, 80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ... and the other way round: how far does 100 kJ get us?
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## الحساب باستخدام الوحدات الأساسية (الطاقة وفرق درجة الحرارة)

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / temperatureDifference` | `KHeatCapacityUnitInstance` | السعة الحرارية |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance` | الطاقة المطلوبة |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance` | الطاقة (تبادلي) |
| `energy / heatCapacity` | `KTemperatureDifferenceUnitInstance` | ارتفاع درجة الحرارة الممكن |

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك | الصيغة | النتيجة |
|---|---|---|
| `energy / temperatureDifference` | معامل مكتوب بنوع صريح | `KHeatCapacityUnitInstance` مباشرة |
| `mass · distance² · time⁻² · temperature⁻¹` | تعبير أصلي + `toHeatCapacity()` | `KHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// typed operator form
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// native base-dimension form (kg·m²·s⁻²·K⁻¹), recognised by toHeatCapacity()
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true - both are 1.0 J/K
```

تتعرّف `toHeatCapacity()` على **الصيغة القياسية فقط**؛ وأي تعبير مكافئ يُختزَل إليها تلقائيًا، أما
الشكل الخاطئ فيرمي `IllegalStateException`.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - : same group, automatic conversion between units and prefixes
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// comparisons (by normalized J/K value)
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// * / / between two heat capacities escape to a KMixedUnitInstance
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب
الأسس بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر
وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/K` | `joulesPerKelvin` | السعة الحرارية، الوحدة الأساسية — رمز مسمّى |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | نفس الكمّية بالأبعاد الأساسية |
| `kJ/K` | `kilo.joulesPerKelvin` | كيلوجول لكل كلفن |
| `cal/K` | `caloriesPerKelvin` | سعرة لكل كلفن |
| `C = Q / ΔT` | `(4184 of joules) / rise` | السعة الحرارية من الطاقة ÷ ارتفاع درجة الحرارة |
| `Q = C · ΔT` | `kettle * rise` | الطاقة من السعة الحرارية × ارتفاع درجة الحرارة |
| `ΔT = Q / C` | `(100 of kilo.joules) / kettle` | ارتفاع درجة الحرارة من الطاقة ÷ السعة الحرارية |
