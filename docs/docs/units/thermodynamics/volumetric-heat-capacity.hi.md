# आयतन ऊष्मा क्षमता

पैकेज: `org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
मूल इकाई: **जूल प्रति घन मीटर-केल्विन**
(`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`)

प्रकार: **संघटित इकाई**

आयतन ऊष्मा क्षमता `c_v` यह दर्शाती है कि किसी सामग्री का एक **आयतन** प्रति केल्विन कितनी ऊष्मा संग्रहीत करता है:
`c_v = C / V = c · ρ`। यह वह राशि है जो तय करती है कि किसी इमारत, भंडारण टैंक या हीट सिंक में वास्तव में कितना
ऊष्मीय द्रव्यमान है — यदि घनत्व भिन्न हो तो समान विशिष्ट ऊष्मा क्षमता वाली दो सामग्रियाँ बहुत भिन्न मात्रा में
ऊष्मा संग्रहीत करती हैं।

इसका विहित आधार-आयाम सामान्य रूप `mass · length⁻¹ · time⁻² · temperature⁻¹` है।

## नामित इकाइयाँ

| इकाई                                    | संकेत          |                              टोकन | J/(m³·K) में 1 इकाई |
|-------------------------------------------|----------------|-----------------------------------:|-------------------:|
| जूल प्रति घन मीटर-केल्विन                | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |                1.0 |
| कैलोरी प्रति घन सेंटीमीटर-केल्विन        | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |            4.184e6 |

मान बड़े होते हैं, इसलिए मेगाजूल रूप व्यावहारिक है: पानी लगभग 4.18 MJ/(m³·K) है। सभी टोकन हर
SI उपसर्ग स्वीकार करते हैं (`mega.joulesPerCubicMeterKelvin`, …)।

## अपघटन

इस समूह के **दो** अपघटन हैं। दोनों एक ही सामान्यीकरण फैक्टरी में मिलते हैं, इसलिए वे
समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                       | व्यंजक                                                              |
|----------------------------|-------------------------------------------------------------------------|
| टाइप किया गया संकारक A    | `heatCapacity / volume`                                          |
| टाइप किया गया संकारक B    | `specificHeatCapacity * density`                                 |
| मूल (`toX()`)             | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## समूह के साथ गणना

| व्यंजक                                            | परिणाम प्रकार                                | अर्थ                    |
|------------------------------------------------------|-----------------------------------------------|--------------------------|
| `heatCapacity / volume`                              | `KVolumetricHeatCapacityUnitInstance`          | `c_v = C / V`           |
| `specificHeatCapacity * density`                     | `KVolumetricHeatCapacityUnitInstance`          | `c_v = c · ρ`           |
| `volumetricHeatCapacity * volume`                    | `KHeatCapacityUnitInstance`                    | `C = c_v · V`           |
| `heatCapacity / volumetricHeatCapacity`              | `KVolumeUnitInstance`                          | संबंधित आयतन            |
| `volumetricHeatCapacity / density`                   | `KSpecificHeatCapacityUnitInstance`            | वापस `c` पर             |
| `volumetricHeatCapacity / specificHeatCapacity`      | `KDensityUnitInstance`                         | वापस `ρ` पर             |

## वास्तविक उदाहरण — पानी भंडारण टैंक का ऊष्मीय द्रव्यमान

**300 l** का पानी भंडारण टैंक: इसे 1 K बढ़ाने में कितनी ऊर्जा लगती है, और यह उसी आयतन के कंक्रीट
(≈ 2.0 MJ/(m³·K)) की तुलना में कैसा है?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09 गुना ऊष्मीय द्रव्यमान
```

## मान सिमेंटिक्स

`equals`/`hashCode` **सामान्यीकृत J/(m³·K) मान** की तुलना करते हैं, इसलिए
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)`। `toString()`
मूल इकाई में मान प्रस्तुत करता है: `"4184000.0 J/(m^3*K)"`।

## यह भी देखें

* [ऊष्मा क्षमता](heat-capacity.hi.md) — असामान्यीकृत राशि।
* [विशिष्ट ऊष्मा क्षमता](specific-heat-capacity.hi.md) — वही विचार आयतन के बजाय **द्रव्यमान** के प्रति।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
