# विशिष्ट ऊर्जा

पैकेज: `org.pcsoft.framework.kunit.thermo.specificenergy`
मूल इकाई: **जूल प्रति किलोग्राम** (`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

प्रकार: **संघटित इकाई**

विशिष्ट ऊर्जा प्रति इकाई द्रव्यमान ऊर्जा है: `energy / mass` (`J/kg`)। संदर्भ के आधार पर इसी राशि को
*विशिष्ट एन्थैल्पी*, *विशिष्ट गुप्त ऊष्मा* या *कैलोरी मान* कहा जाता है — ये सभी इसी इकाई समूह को
साझा करते हैं।

`KSpecificEnergyUnitInstance` विहित सामान्य रूप `distance² · time⁻²` (`m²·s⁻²`) में ठीक दो पदों वाले
`KMixedUnitInstance` को लपेटता है, जो हमेशा J/kg में सामान्यीकृत रहता है।

!!! note "द्रव्यमान आयाम रद्द हो जाता है"
    `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`। इसलिए विहित सामान्य रूप में द्रव्यमान का कोई पद नहीं है।
    केवल `KMassUnitInstance` के विरुद्ध संकारक ही द्रव्यमान समूह के ग्राम आधार को इस समूह की
    प्रति-किलोग्राम परिभाषा से जोड़ते हैं।

प्रति इकाई तापमान यह [विशिष्ट ऊष्मा क्षमता](specific-heat-capacity.md) बन जाती है; प्रति किलोग्राम के
बजाय प्रति मोल यह [मोलर ऊर्जा](molar-energy.md) बन जाती है।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | 1 इकाई = ? J/kg |
|---|---|---:|---:|
| जूल प्रति किलोग्राम | `J/kg` | `joulesPerKilogram` | 1.0 |
| कैलोरी प्रति ग्राम | `cal/g` | `caloriesPerGram` | 4184.0 |
| वाट-घंटा प्रति किलोग्राम | `Wh/kg` | `wattHoursPerKilogram` | 3600.0 |
| Btu प्रति पाउंड | `Btu/lb` | `btusPerPound` | 2326.0 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.joulesPerKilogram`, `mega.joulesPerKilogram`,
`kilo.wattHoursPerKilogram`, …)।

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

## वास्तविक उदाहरण: बर्फ का पिघलना

पानी की गलन गुप्त ऊष्मा 334 kJ/kg है। 2.5 kg बर्फ के ब्लॉक को पिघलाने में कितनी ऊर्जा लगती है?

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

// उलटा: 1 MJ से कितनी बर्फ पिघल सकती है?
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## मूल इकाइयों (ऊर्जा और द्रव्यमान) से गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `energy / mass` | `KSpecificEnergyUnitInstance` | विशिष्ट ऊर्जा |
| `specificEnergy * mass` | `KEnergyUnitInstance` | कुल ऊर्जा |
| `mass * specificEnergy` | `KEnergyUnitInstance` | कुल ऊर्जा (क्रमविनिमेय) |
| `energy / specificEnergy` | `KMassUnitInstance` | शामिल द्रव्यमान |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन | रूप | परिणाम |
|---|---|---|
| `energy / mass` | टाइप किया गया संकारक | सीधे `KSpecificEnergyUnitInstance` |
| `distance² · time⁻²` | मूल व्यंजक + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// टाइप किया गया संकारक रूप
val typed = (1 of joules) / (1 of kilo.grams)

// मूल आधार-आयाम रूप (m²·s⁻²), toSpecificEnergy() द्वारा पहचाना गया
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true - दोनों 1.0 J/kg हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J/kg` | `joulesPerKilogram` | विशिष्ट ऊर्जा, मूल इकाई — नामित टोकन |
| `m²·s⁻²` | `(meters pow 2) / (seconds pow 2)` | वही राशि आधार आयामों में |
| `kJ/kg` | `kilo.joulesPerKilogram` | किलोजूल प्रति किलोग्राम |
| `Wh/kg` | `wattHoursPerKilogram` | वाट-घंटा प्रति किलोग्राम (बैटरी ऊर्जा घनत्व) |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | ऊर्जा ÷ द्रव्यमान से विशिष्ट ऊर्जा |
| `Q = q · m` | `latentHeat * block` | विशिष्ट ऊर्जा × द्रव्यमान से ऊर्जा |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat` | ऊर्जा ÷ विशिष्ट ऊर्जा से द्रव्यमान |
