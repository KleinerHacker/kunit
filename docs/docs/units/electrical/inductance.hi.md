# प्रेरकत्व

पैकेज: `org.pcsoft.framework.kunit.inductance`
मूल इकाई: **हेनरी** (`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

प्रकार: **निर्मित इकाई**

प्रेरकत्व एक **निर्मित** इकाई है: संघटन `mass · length² · time⁻² · current⁻²` (`kg·m²·s⁻²·A⁻²`)।
`KInductanceUnitInstance` चार पदों के एक `KMixedUnitInstance` को लपेटता है — `KMassUnit.BASE` (ग्राम)
`+1` पर, `KDistanceUnit.BASE` (मीटर) `+2` पर, `KTimeUnit.BASE` (सेकंड) `-2` पर, और
`KElectricCurrentUnit.BASE` (ऐम्पियर) `-2` पर। चूँकि पुस्तकालय का द्रव्यमान घटक **ग्राम** (किलोग्राम
नहीं) में प्रसामान्यीकृत है, हेनरी कच्चे घटक आधार का 1000× है; संग्रहित मान हेनरी में प्रसामान्यीकृत है।

## एक प्रेरकत्व बनाना

प्रेरकत्व को किसी नामित टोकन से, या किसी अपघटन से बनाएँ (नीचे देखें)। नामित इकाइयाँ मान-1 टोकन के रूप में
बचती हैं (`of`/`into` के साथ प्रयुक्त):

| प्रेरकत्व | प्रतीक | टोकन | 1 इकाई H में |
|---|---|---:|---:|
| हेनरी | `H` | `henries` | 1.0 |
| वेबर प्रति ऐम्पियर | `Wb/A` | `webersPerAmpere` | 1.0 |
| ऐब-हेनरी (CGS-EMU) | `abH` | `abhenries` | 1.0e-9 |
| स्टैट-हेनरी (CGS-ESU) | `statH` | `stathenries` | 8.987551787e11 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`milli.henries`,
`micro.henries`, `nano.henries`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## अनेक अपघटन

प्रेरकत्व तक कई **समतुल्य अपघटनों** के माध्यम से पहुँचा जा सकता है, सभी समान मान-तुल्य प्रेरकत्व उत्पन्न
करते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `flux / current` | `KInductanceUnitInstance` | परिभाषा `L = Φ / I` |
| `resistance / frequency` | `KInductanceUnitInstance` | प्रतिघात रूप `L = X / ω` (`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | `.toInductance()` के माध्यम से | नेटिव विहित `kg·m²·s⁻²·A⁻²` व्यंजक |

प्रकार-युक्त संकारक रूप सीधे एक प्रेरकत्व लौटाते हैं। पूर्णतः नेटिव व्यंजक एक सामान्य `KMixedUnitInstance`
रहता है और `toInductance()` से संकुचित होता है (जो केवल विहित मानक रूप को पहचानता है और अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान-तुल्य हैं।

व्युत्क्रम संकारक चुंबकीय फ्लक्स, धारा, आवृत्ति और प्रतिरोध को एक साथ बाँधते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I` (क्रमविनिमेय) |
| `flux / inductance` | `KElectricCurrentUnitInstance` | `I = Φ / L` |
| `inductance * frequency` | `KResistanceUnitInstance` | `X = ω · L` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.frequency.hertz
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.inductance.*

// वास्तविक उदाहरण - स्विचिंग विद्युत आपूर्ति में चोक: 470 µH की कुंडली में 2 A बहने पर फ्लक्स संधान
// 0.00094 Wb होता है, और 100 kHz की कोणीय आवृत्ति पर यह 47 Ω का प्रतिघात प्रस्तुत करती है।
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance, 0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance, 47 Ω

// वही प्रेरकत्व उसकी परिभाषा से और प्रतिघात रूप से:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// वही प्रेरकत्व नेटिव kg·m²·s⁻²·A⁻² व्यंजक के रूप में:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## पर्मिएंस

किसी चुंबकीय परिपथ का **पर्मिएंस** `Λ` उसकी [चुंबकीय अनिच्छा (reluctance)](reluctance.md) का व्युत्क्रम
है, `Λ = 1 / Rm`। यह प्रेरकत्व के **विमीय रूप से समान** है और हेनरी में ही मापा जाता है, इसलिए KUnit इसे
इसी समूह और प्रतीक `H` से मॉडल करता है; कोई अलग टोकन और कोई अलग प्रकार नहीं है। व्युत्क्रम संकारक दोनों
समूहों को एक साथ बाँधते हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.inductance.*
import org.pcsoft.framework.kunit.reluctance.*

// Rm = 500 A/Wb वाले एक चुंबकीय परिपथ का पर्मिएंस 2 mH है।
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// …और वापस:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.inductance.*

(2 of henries).toString()     // "2.0 H" (मूल इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न को दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `H` | `henries` | प्रेरकत्व, मूल इकाई (नामित टोकन, हेनरी) |
| `Wb/A` | `webersPerAmpere` | वेबर प्रति ऐम्पियर के रूप में प्रेरकत्व (नामित टोकन) |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | द्रव्यमान·लंबाई² / (समय²·धारा²) के रूप में प्रेरकत्व (भिन्न रूप) |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | वही प्रेरकत्व शुद्ध गुणनफल के रूप में |
| `mH` | `milli.henries` | उपसर्ग-युक्त प्रेरकत्व (मिलीहेनरी) |
