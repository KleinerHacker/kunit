# मोलर ऊष्मा क्षमता

पैकेज: `org.pcsoft.framework.kunit.thermo.molarheatcapacity`
मूल इकाई: **जूल प्रति मोल-केल्विन** (`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`)

प्रकार: **संघटित इकाई**

मोलर ऊष्मा क्षमता किसी पदार्थ की [ऊष्मा क्षमता](heat-capacity.md) *प्रति मोल* है: `J/(mol·K)`। यह गैसों और रासायनिक
ऊष्मागतिकी के लिए स्वाभाविक रूप है, जहाँ मात्राएँ किलोग्राम के बजाय मोल में गिनी जाती हैं
(वह [विशिष्ट ऊष्मा क्षमता](specific-heat-capacity.md) है)।

`KMolarHeatCapacityUnitInstance` विहित सामान्य रूप `mass¹ · distance² · time⁻² · substance⁻¹ ·
temperature⁻¹` (`kg·m²·s⁻²·mol⁻¹·K⁻¹`) में ठीक पाँच पदों वाले `KMixedUnitInstance` को लपेटता है। तापमान आयाम **अंतर**
समूह है, कभी भी ऐफ़ाइन परम तापमान नहीं।

## नामित इकाइयाँ

| इकाई           | संकेत           |                     टोकन | 1 इकाई = ? J/(mol·K) |
|---------------|---------------|------------------------:|--------------------:|
| जूल प्रति मोल-केल्विन  | `J/(mol·K)`   |   `joulesPerMoleKelvin` |                 1.0 |
| कैलोरी प्रति मोल-केल्विन | `cal/(mol·K)` | `caloriesPerMoleKelvin` |               4.184 |

दोनों में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.joulesPerMoleKelvin`, `milli.joulesPerMoleKelvin`, …)।

## गैस स्थिरांक

यह समूह मोलर गैस स्थिरांक का सटीक SI मान `GAS_CONSTANT` के रूप में (8.31446261815324 J/ (mol·K))
उजागर करता है — एक सादा `Double`, जिससे यह गुणक और पठन दोनों के रूप में काम कर सकता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## वास्तविक उदाहरण: नाइट्रोजन को गर्म करना (डूलॉन्ग-पेटिट सत्यापन)

द्विपरमाण्विक नाइट्रोजन का `c_p ≈ 29.1 J/(mol·K)` है। 3 मोल को 50 K से गर्म करने में कितनी ऊर्जा लगती है, और वह प्रति
मोल कितनी है?

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

// मार्ग 1: पहले नमूने की ऊष्मा क्षमता, फिर ऊर्जा
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// मार्ग 2: पहले प्रति मोल
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J - समान
```

## पड़ोसी इकाइयों से गणना

| व्यंजक                                         | परिणाम प्रकार                             | अर्थ               |
|---------------------------------------------|--------------------------------------|------------------|
| `heatCapacity / amountOfSubstance`          | `KMolarHeatCapacityUnitInstance`     | एक नमूने से पदार्थ गुण  |
| `molarEnergy / temperatureDifference`       | `KMolarHeatCapacityUnitInstance`     | वही, मोलर ऊर्जा के जरिए |
| `molarHeatCapacity * amountOfSubstance`     | `KHeatCapacityUnitInstance`          | नमूने की ऊष्मा क्षमता     |
| `amountOfSubstance * molarHeatCapacity`     | `KHeatCapacityUnitInstance`          | वही (क्रमविनिमेय)      |
| `heatCapacity / molarHeatCapacity`          | `KAmountOfSubstanceUnitInstance`     | पदार्थ की मात्रा         |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance`           | प्रति मोल ऊर्जा         |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance`           | वही (क्रमविनिमेय)      |
| `molarEnergy / molarHeatCapacity`           | `KTemperatureDifferenceUnitInstance` | प्राप्य वृद्धि            |

## अपघटन

तीनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                                                     | रूप                           | परिणाम                             |
|-----------------------------------------------------------|------------------------------|----------------------------------|
| `heatCapacity / amountOfSubstance`                        | टाइप किया गया संकारक               | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference`                     | टाइप किया गया संकारक               | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | मूल + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

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
viaHeatCapacity == native         // true - सभी 1.0 J/(mol·K) हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित                   | Kotlin                                                  | अर्थ                           |
|-----------------------|---------------------------------------------------------|------------------------------|
| `J/(mol·K)`           | `joulesPerMoleKelvin`                                   | मोलर ऊष्मा क्षमता, मूल इकाई           |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | आधार आयाम                      |
| `cal/(mol·K)`         | `caloriesPerMoleKelvin`                                 | कैलोरी प्रति मोल-केल्विन                |
| `R`                   | `GAS_CONSTANT of joulesPerMoleKelvin`                   | मोलर गैस स्थिरांक, 8.3145 J/(mol·K) |
| `C_m = C / n`         | `(58.2 of joulesPerKelvin) / (2 of moles)`              | ऊष्मा क्षमता ÷ मात्रा से                |
| `C_m = ΔH_m / ΔT`     | `(58.2 of joulesPerMole) / rise`                        | मोलर ऊर्जा ÷ तापमान वृद्धि से           |
| `Q = C_m · n · ΔT`    | `nitrogen * sample * rise`                              | कुल ऊर्जा                        |
