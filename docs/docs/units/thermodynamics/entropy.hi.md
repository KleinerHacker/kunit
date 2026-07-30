# एन्ट्रॉपी

पैकेज: `org.pcsoft.framework.kunit.thermo.heatcapacity`
मूल इकाई: **जूल प्रति केल्विन** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

प्रकार: **संघटित इकाई**

एन्ट्रॉपी `S` किसी तंत्र में ऊर्जा के फैलाव को मापती है। इसकी इकाई `J/K` है — जो
[ऊष्मा क्षमता](heat-capacity.md) के **आयामिक रूप से समान** है।

## एन्ट्रॉपी का अपना कोई प्रकार क्यों नहीं है

KUnit जानबूझकर एन्ट्रॉपी को एक अलग `KEntropyUnitInstance` के बजाय `KHeatCapacityUnitInstance` से
मॉडल करता है। इसका कारण इस लाइब्रेरी की रूप-पहचान संविदा है:

* प्रत्येक मानकीकृत समूह का **एक** विहित आधार-आयाम सामान्य रूप होता है, और
* `toX()` ठीक उसी रूप को पहचानता है।

एन्ट्रॉपी और ऊष्मा क्षमता सामान्य रूप `mass¹ · distance² · time⁻² · temperature⁻¹` साझा करते हैं। एक
सामान्य रूप पर दो प्रकार होने से मूल व्यंजक अस्पष्ट हो जाएगा — `toHeatCapacity()` और एक काल्पनिक
`toEntropy()` दोनों उसी मिश्रित इकाई से मेल खाएँगे, और कोई भी उत्तर दूसरे से अधिक सही नहीं होगा। एक
ही प्रकार राउंड-ट्रिप को निश्चयात्मक बनाए रखता है।

इसलिए इन दोनों राशियों के बीच अंतर इस बात का मामला है कि *आप अपने वेरिएबल का नाम क्या रखते हैं*, न
कि इस बात का कि लाइब्रेरी आपको कौन-सा प्रकार देती है — बिल्कुल वैसे ही जैसे भौतिकी संकेतन में, जहाँ
दोनों को J/K में लिखा जाता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// दोनों KHeatCapacityUnitInstance हैं
```

## वास्तविक उदाहरण: बर्फ का पिघलना

273.15 K पर 1 kg बर्फ को पिघलाने में 334 kJ गुप्त ऊष्मा अवशोषित होती है। एन्ट्रॉपी परिवर्तन
`ΔS = Q / T` है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // परम शून्य से अंतराल के रूप में

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance, J/K में
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// उलटा: गलनांक पर वह एन्ट्रॉपी परिवर्तन कितनी ऊष्मा वहन करता है?
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "`ΔS = Q / T` में परम तापमान"
    एन्ट्रॉपी को एक **परम** तापमान से विभाजित किया जाता है, लेकिन इस लाइब्रेरी के भागफल तापमान
    *अंतर* समूह (`KTemperatureDifferenceUnit`) का उपयोग करते हैं — एक ऐफ़ाइन पैमाना हर में नहीं
    आ सकता। ऊपर की तरह परम केल्विन पठन को परम शून्य से एक अंतराल के रूप में व्यक्त करें:
    `KTemperatureDifference.ofKelvin(273.15)`। केल्विन में दोनों संख्यात्मक रूप से मेल खाते हैं,
    यही कारण है कि ऊष्मागतिकी केल्विन पैमाने का उपयोग करती है।

## यह भी देखें

* [ऊष्मा क्षमता](heat-capacity.md) — वह प्रकार जो एन्ट्रॉपी साझा करती है, पूर्ण इकाई तालिका, सभी
  अपघटन और संपूर्ण संकारक सतह सहित
* [मोलर ऊष्मा क्षमता](molar-heat-capacity.md) — प्रति-मोल रूप (मोलर एन्ट्रॉपी)
* [विशिष्ट ऊष्मा क्षमता](specific-heat-capacity.md) — प्रति-किलोग्राम रूप (विशिष्ट एन्ट्रॉपी)
* [ऊर्जा](energy.md) — `ΔS = Q / T` का अंश

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह राशि गणितीय रूप से कैसे लिखी जाती है बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J/K` | `joulesPerKelvin` | एन्ट्रॉपी, मूल इकाई (ऊष्मा क्षमता के साथ साझा) |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | वही राशि आधार आयामों में |
| `ΔS = Q / T` | `latentHeat / meltingPoint` | ऊष्मा ÷ तापमान से एन्ट्रॉपी परिवर्तन |
| `Q = ΔS · T` | `entropyChange * meltingPoint` | एन्ट्रॉपी परिवर्तन × तापमान से ऊष्मा |
