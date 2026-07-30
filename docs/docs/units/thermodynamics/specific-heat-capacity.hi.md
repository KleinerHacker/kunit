# विशिष्ट ऊष्मा क्षमता

पैकेज: `org.pcsoft.framework.kunit.thermo.specificheatcapacity`
मूल इकाई: **जूल प्रति किलोग्राम-केल्विन** (`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`)

प्रकार: **संघटित इकाई**

विशिष्ट ऊष्मा क्षमता किसी सामग्री की [ऊष्मा क्षमता](heat-capacity.md) *प्रति इकाई द्रव्यमान* है:
`J/(kg·K)`। यह हर "इसे गर्म करने में कितनी ऊर्जा लगती है" गणना के पीछे की सामग्री-गुणधर्म है।

`KSpecificHeatCapacityUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`distance² · time⁻² · temperature⁻¹` (`m²·s⁻²·K⁻¹`) में ठीक तीन पद होते हैं — द्रव्यमान आयाम रद्द हो
जाता है, ठीक वैसे ही जैसे [विशिष्ट ऊर्जा](specific-energy.md) में होता है। तापमान आयाम **अंतर** समूह
(`KTemperatureDifferenceUnit`) है, कभी भी एफाइन निरपेक्ष तापमान नहीं।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | J/(kg·K) में 1 इकाई |
|---|---|---:|---:|
| जूल प्रति किलोग्राम-केल्विन | `J/(kg·K)` | `joulesPerKilogramKelvin` | 1.0 |
| कैलोरी प्रति ग्राम-केल्विन | `cal/(g·K)` | `caloriesPerGramKelvin` | 4184.0 |
| Btu प्रति पाउंड-°F | `Btu/(lb·°F)` | `btusPerPoundFahrenheit` | 4186.8 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.joulesPerKilogramKelvin`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0 (पानी परिभाषा के अनुसार 1 cal/(g·K) है)
```

## वास्तविक उदाहरण: बाथटब गर्म करना

150 लीटर पानी (150 kg) को 12 °C से 40 °C तक गर्म किया जाता है। पानी की विशिष्ट ऊष्मा क्षमता
4184 J/(kg·K) है।

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

// रास्ता 1: पहले टब की ऊष्मा क्षमता बनाएँ
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// रास्ता 2: इसके बजाय विशिष्ट ऊर्जा (प्रति किलोग्राम ऊर्जा) से जाएँ
val perKilogram = water * rise                // KSpecificEnergyUnitInstance, 117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ - समान
```

## पड़ोसी इकाइयों के साथ गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `heatCapacity / mass` | `KSpecificHeatCapacityUnitInstance` | किसी वस्तु से सामग्री-गुणधर्म |
| `specificEnergy / temperatureDifference` | `KSpecificHeatCapacityUnitInstance` | वही, विशिष्ट ऊर्जा के जरिए |
| `specificHeatCapacity * mass` | `KHeatCapacityUnitInstance` | वस्तु की ऊष्मा क्षमता |
| `mass * specificHeatCapacity` | `KHeatCapacityUnitInstance` | वही (क्रमविनिमेय) |
| `heatCapacity / specificHeatCapacity` | `KMassUnitInstance` | वस्तु का द्रव्यमान |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance` | प्रति किलोग्राम ऊर्जा |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance` | वही (क्रमविनिमेय) |
| `specificEnergy / specificHeatCapacity` | `KTemperatureDifferenceUnitInstance` | प्राप्य वृद्धि |

## अपघटन

सभी तीनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन | रूप | परिणाम |
|---|---|---|
| `heatCapacity / mass` | टाइप किया गया संकारक | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | टाइप किया गया संकारक | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹` | मूल व्यंजक + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

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
viaHeatCapacity == native            // true - सभी 1.0 J/(kg·K) हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J/(kg·K)` | `joulesPerKilogramKelvin` | विशिष्ट ऊष्मा क्षमता, मूल इकाई |
| `m²·s⁻²·K⁻¹` | `(meters pow 2) / (seconds pow 2) / ΔK` | वही राशि आधार आयामों में |
| `cal/(g·K)` | `caloriesPerGramKelvin` | कैलोरी प्रति ग्राम-केल्विन |
| `c = C / m` | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | ऊष्मा क्षमता ÷ द्रव्यमान से |
| `c = q / ΔT` | `(8368 of joulesPerKilogram) / rise` | विशिष्ट ऊर्जा ÷ तापमान वृद्धि से |
| `C = c · m` | `water * bath` | सामग्री × द्रव्यमान से वस्तु की ऊष्मा क्षमता |
| `Q = c · m · ΔT` | `water * bath * rise` | कुल ऊर्जा |
