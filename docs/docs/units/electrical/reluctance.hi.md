# चुंबकीय प्रतिश्रांति

पैकेज: `org.pcsoft.framework.kunit.electric.reluctance`
आधार इकाई: **ऐम्पियर प्रति वेबर** (`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`)

प्रकार: **संरचित इकाई**

चुंबकीय प्रतिश्रांति एक **संरचित** इकाई है: संयोजन `mass⁻¹ · length⁻² · time² · current²`
(`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`)। `KReluctanceUnitInstance` चार पदों वाले `KMixedUnitInstance` को
लपेटता है — घातांक `-1` पर `KMassUnit.BASE` (ग्राम), घातांक `-2` पर `KDistanceUnit.BASE` (मीटर), घातांक
`+2` पर `KTimeUnit.BASE` (सेकंड) और घातांक `+2` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी
का द्रव्यमान घटक **ग्राम** में सामान्यीकृत है (किलोग्राम में नहीं) और द्रव्यमान घातांक *ऋणात्मक* है, विहित
गुणनफल को ऐम्पियर प्रति वेबर तक पहुँचने के लिए 1000 से गुणा किया जाता है; संग्रहीत मान हमेशा ऐम्पियर प्रति
वेबर में सामान्यीकृत रहता है।

प्रतिश्रांति `Rm` चुंबकीय परिपथ का [प्रतिरोध](resistance.md) के समकक्ष है: यह चुंबकवाहक बल `Θ` (जिसे
ऐम्पियर टर्न में मापा जाता है, देखें [विद्युत धारा](ec.md)) को परिणामी
[चुंबकीय फ्लक्स](magneticflux.md) से हॉपकिंसन के नियम `Θ = Rm · Φ` के माध्यम से जोड़ती है। इसका व्युत्क्रम
**पारगम्यांक** `Λ` है, जिसे हेनरी में मापा जाता है और इसलिए यह [प्रेरकत्व](inductance.md) समूह द्वारा वहन
किया जाता है।

## प्रतिश्रांति बनाना

प्रतिश्रांति को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप
में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| प्रतिश्रांति | संकेत | टोकन | 1 इकाई A/Wb में |
|---|---|---:|---:|
| ऐम्पियर प्रति वेबर | `A/Wb` | `amperesPerWeber` | 1.0 |
| व्युत्क्रम हेनरी | `H⁻¹` | `inverseHenries` | 1.0 |
| ऐम्पियर टर्न प्रति वेबर | `At/Wb` | `ampereTurnsPerWeber` | 1.0 |

तीनों वर्तनी एक ही राशि दर्शाती हैं — कुंडली फेरों की संख्या एक शुद्ध गणना है — इसलिए ये मान‑समान हैं;
अलग-अलग संकेत दृष्टिकोण को दर्शाते हैं। नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन
करती हैं (`mega.amperesPerWeber`, `kilo.inverseHenries`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.reluctance.*

val rm = 2 of mega.amperesPerWeber    // हवा-अंतराल वाला लोहे का क्रोड
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## अनेक अपघटन

प्रतिश्रांति तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान प्रतिश्रांति देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `current / magneticFlux` | `KReluctanceUnitInstance` | हॉपकिंसन का नियम `Rm = Θ / Φ` |
| `1 / inductance` | `KReluctanceUnitInstance` | पारगम्यांक का व्युत्क्रम, `Rm = 1 / Λ` |
| `(time²·current²)/(mass·length²)` | `.toReluctance()` द्वारा | मूल विहित `kg⁻¹·m⁻²·s²·A²` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे प्रतिश्रांति लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही
रहता है और `toReluctance()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर चुंबकवाहक बल, फ्लक्स, पारगम्यांक और प्रतिश्रांति को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ` (क्रमविनिमेय) |
| `current / reluctance` | `KMagneticFluxUnitInstance` | `Φ = Θ / Rm` |
| `1 / reluctance` | `KInductanceUnitInstance` | पारगम्यांक `Λ = 1 / Rm` (हेनरी में) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.current.ampereTurns
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.reluctance.*

// वास्तविक उदाहरण - 2 kAt चुंबकवाहक बल एक 2 MA/Wb क्रोड से होकर 1 mWb का फ्लक्स देता है।
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// प्रतिश्रांति के लिए हल की गई परिभाषा:
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// मूल kg⁻¹·m⁻²·s²·A² व्यंजक के रूप में वही प्रतिश्रांति:
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb (श्रेणी चुंबकीय परिपथ)
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻²`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `A/Wb` | `amperesPerWeber` | प्रतिश्रांति, आधार इकाई (नामित टोकन, ऐम्पियर प्रति वेबर) |
| `H⁻¹` | `inverseHenries` | वही राशि की व्युत्क्रम-प्रेरकत्व वर्तनी |
| `Θ / Φ` | `(6 of amperes) / (3 of webers)` | हॉपकिंसन के नियम से प्रतिश्रांति |
| `1 / Λ` | `1 / (0.5 of henries)` | पारगम्यांक के व्युत्क्रम के रूप में प्रतिश्रांति |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | (समय²·धारा²) / (द्रव्यमान·लंबाई²) के रूप में प्रतिश्रांति (भिन्न रूप) |
| `kg⁻¹·m⁻²·s²·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | वही प्रतिश्रांति शुद्ध गुणनफल के रूप में |
| `MA/Wb` | `mega.amperesPerWeber` | उपसर्ग सहित प्रतिश्रांति (मेगाऐम्पियर प्रति वेबर) |
