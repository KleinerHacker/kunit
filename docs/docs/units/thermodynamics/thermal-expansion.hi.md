# तापीय प्रसार गुणांक

पैकेज: `org.pcsoft.framework.kunit.thermo.expansion`
मूल इकाई: **प्रति केल्विन** (`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`)

प्रकार: **संघटित इकाई**

तापीय प्रसार गुणांक `α` किसी लंबाई (या क्षेत्रफल, या आयतन) का प्रति केल्विन *सापेक्ष* परिवर्तन है:
`1/K`। यह तापमान अंतर का व्युत्क्रम है।

`KThermalExpansionUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`temperature⁻¹` (`K⁻¹`) में ठीक एक पद होता है, जो हमेशा 1/K में सामान्यीकृत रहता है। तापमान आयाम
**अंतर** समूह है — यह गुणांक प्रति तापमान *अंतराल* परिवर्तन का वर्णन करता है।

!!! note "पैकेज नाम बनाम क्लास नाम"
    पैकेज `thermo.expansion` है, `thermo.thermalexpansion` नहीं — किसी यूनिट पैकेज को अपने फील्ड
    पैकेज का नाम दोहराना नहीं चाहिए। टाइप्स पूरा तकनीकी शब्द बनाए रखते हैं
    (`KThermalExpansionUnitInstance`)।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | 1/K में 1 इकाई |
|---|---|---:|---:|
| प्रति केल्विन | `1/K` | `perKelvin` | 1.0 |
| प्रति डिग्री फारेनहाइट | `1/°F` | `perFahrenheit` | 1.8 |
| प्रति मिलियन भाग प्रति केल्विन | `ppm/K` | `ppmPerKelvin` | 1e-6 |

सामग्री तालिकाएँ `α` को ppm/K में सूचीबद्ध करती हैं, जो ठीक `micro.perKelvin` है। सभी इकाइयों में
पूर्ण SI उपसर्ग सीमा समर्थित है।

## विशिष्ट मान

| सामग्री | α |
|---|---:|
| स्टील | ≈ 12 ppm/K |
| कंक्रीट | ≈ 12 ppm/K |
| एल्युमिनियम | ≈ 23 ppm/K |
| कांच (बोरोसिलिकेट) | ≈ 3.3 ppm/K |

## वास्तविक उदाहरण: गर्मियों में एक स्टील बीम

एक 10 m स्टील बीम (α = 12 ppm/K) 0 °C से 50 °C तक गर्म होता है। यह कितनी लंबी हो जाती है? यही कारण
है कि पुलों में विस्तार जोड़ (expansion joints) होते हैं।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// आयामरहित सापेक्ष परिवर्तन
val strain = steel * rise                      // 6.0e-4

// निरपेक्ष परिवर्तन, टाइप किया गया
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// समान झूले में एक 100 m पुल डेक
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## ऑपरेटर

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `1 / temperatureDifference` | `KThermalExpansionUnitInstance` | अंतराल से गुणांक |
| `1 / thermalExpansion` | `KTemperatureDifferenceUnitInstance` | गुणांक से अंतराल |
| `thermalExpansion * temperatureDifference` | `Double` | **सापेक्ष** परिवर्तन (आयामरहित) |
| `temperatureDifference * thermalExpansion` | `Double` | वही (क्रमविनिमेय) |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance` | **निरपेक्ष** परिवर्तन |

दोनों व्युत्क्रम संकारक संकीर्ण रूप से घोषित किए गए हैं, ताकि `1 / d` और `1 / α` समूह-अज्ञेय
`Number.div` द्वारा उत्पन्न सामान्य मिश्रित इकाई के बजाय एक **टाइप किया गया** मान लौटाएँ।

!!! warning "`elongationOf`, न कि जंजीरबद्ध `*`"
    `α · ΔT` जानबूझकर एक साधारण `Double` है — सापेक्ष परिवर्तन आयामरहित होता है। उस `Double` को किसी
    लंबाई पर गुणा करने के लिए रूट पैकेज से सामान्य स्केलर `times` की आवश्यकता होगी, और उसे स्पष्ट रूप
    से आयात करने से इस समूह का `times` संकारक **छिप जाएगा (shadow)**। `elongationOf` जानबूझकर एक
    साधारण फ़ंक्शन है ताकि इसे छिपाया न जा सके; जब भी आपको निरपेक्ष परिवर्तन चाहिए, इसे प्राथमिकता दें।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन | रूप | परिणाम |
|---|---|---|
| `1 / temperatureDifference` | टाइप किया गया संकारक | `KThermalExpansionUnitInstance` |
| `temperature⁻¹` | मूल व्यंजक + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true - दोनों 1.0 1/K हैं
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `1/K` | `perKelvin` | तापीय प्रसार गुणांक, मूल इकाई |
| `K⁻¹` | `ΔK pow -1` | वही राशि ऋणात्मक घातांक के रूप में |
| `ppm/K` | `ppmPerKelvin` | प्रति मिलियन भाग प्रति केल्विन (सामग्री तालिकाएँ) |
| `α = 1 / ΔT` | `1 / KTemperatureDifference.ofKelvin(2)` | अंतराल से गुणांक |
| `ε = α · ΔT` | `steel * rise` | सापेक्ष परिवर्तन (आयामरहित) |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)` | निरपेक्ष लंबाई परिवर्तन |
