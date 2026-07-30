# पदार्थ की मात्रा

पैकेज: `org.pcsoft.framework.kunit.thermo.amountofsubstance`
मूल इकाई: **मोल** (`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`)

प्रकार: **नेटिव इकाई**

पदार्थ की मात्रा सात SI मूल राशियों में से एक है — सीधे मापने योग्य, गैर-संघटित राशि, इसलिए यह एक
**नेटिव इकाई** है। `KAmountOfSubstanceUnitInstance` सरल, एक-आयामी रैपर रूप है: घातांक 1 पर एक ही
`KAmountOfSubstanceUnit.BASE` (मोल) पद, जो हमेशा मोल में सामान्यीकृत रहता है।

यह ऊष्मागतिकी क्षेत्र की हर *मोलर* राशि का आधार है
([मोलर ऊर्जा](molar-energy.md), [मोलर ऊष्मा क्षमता](molar-heat-capacity.md))।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | 1 इकाई = ? mol |
|---|---|---:|---:|
| मोल | `mol` | `moles` | 1.0 |
| पाउंड-मोल | `lbmol` | `poundMoles` | 453.59237 |

दोनों में पूर्ण SI उपसर्ग सीमा समर्थित है (`milli.moles`, `micro.moles`, `kilo.moles`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0 (मोल में सामान्यीकृत)
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## अवोगाद्रो स्थिरांक

यह समूह अवोगाद्रो स्थिरांक का सटीक SI मान `AVOGADRO_CONSTANT` के रूप में
(6.02214076e23 mol⁻¹) और किसी इंस्टेंस पर सुविधाजनक `particleCount()` उजागर करता है। दोनों एक सादा
`Double` लौटाते हैं, क्योंकि कण गणना विमाहीन (dimensionless) है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24 कण
```

## वास्तविक उदाहरण: टेबल सॉल्ट को घोलना

सोडियम क्लोराइड (मोलर द्रव्यमान 58.44 g/mol) के 25 g टेबल सॉल्ट में कितने मोल हैं, और वह कितनी सूत्र
इकाइयों के बराबर है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // NaCl के लिए g/mol
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23 सूत्र इकाइयाँ
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - : समान समूह, विभिन्न इकाइयों और उपसर्गों के बीच स्वतः रूपांतरण
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// तुलनाएँ (सामान्यीकृत मोल मान के अनुसार)
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

पदार्थ की मात्रा को किसी अन्य राशि से गुणा या भाग करने पर यह जेनेरिक मिश्रित-इकाई इंजन में बाहर निकल
जाती है, जब तक कोई टाइप किया गया परिणाम मौजूद न हो — जैसे `energy / amountOfSubstance` एक टाइप की गई
[मोलर ऊर्जा](molar-energy.md) है।

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई गणितीय रूप से कैसे लिखी जाती है बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `mol` | `moles` | पदार्थ की मात्रा, मूल इकाई |
| `mmol` | `milli.moles` | मिलिमोल |
| `kmol` | `kilo.moles` | किलोमोल |
| `lbmol` | `poundMoles` | पाउंड-मोल (इंपीरियल अभियांत्रिकी इकाई) |
| `n = m / M` | `(sample.value / molarMass) of moles` | द्रव्यमान ÷ मोलर द्रव्यमान से मात्रा |
| `N = n · N_A` | `n.particleCount()` | मात्रा × अवोगाद्रो स्थिरांक से कण गणना |
