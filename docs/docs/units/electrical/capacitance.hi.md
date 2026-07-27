# धारिता

पैकेज: `org.pcsoft.framework.kunit.capacitance`
आधार इकाई: **फैरड** (`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

प्रकार: **संरचित इकाई**

विद्युत धारिता एक **संरचित** इकाई है: संयोजन `mass⁻¹ · length⁻² · time⁴ · current²` (`kg⁻¹·m⁻²·s⁴·A²`)।
`KCapacitanceUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `-1` पर `KMassUnit.BASE`
(ग्राम), घातांक `-2` पर `KDistanceUnit.BASE` (मीटर), घातांक `+4` पर `KTimeUnit.BASE` (सेकंड) और घातांक `+2`
पर `KElectricCurrentUnit.BASE` (एम्पियर)। चूँकि लाइब्रेरी का द्रव्यमान घटक **ग्राम** (किलोग्राम नहीं) पर
सामान्यीकृत है और द्रव्यमान का घातांक *ऋणात्मक* है, इसलिए फैरड कच्चे घटक आधार से विपरीत दिशा में 1000 गुना है;
संग्रहीत मान फैरड में सामान्यीकृत रहता है।

## धारिता बनाना

धारिता को नामित टोकन से बनाएँ, या किसी अपघटन से (नीचे देखें)। नामित इकाइयाँ मान-1 टोकन के रूप में रहती हैं
(`of`/`into` के साथ प्रयोग):

| धारिता | प्रतीक | टोकन | 1 इकाई F में |
|---|---|---:|---:|
| फैरड | `F` | `farads` | 1.0 |
| ऐब्फैरड (CGS-EMU) | `abF` | `abfarads` | 1.0e9 |
| स्टैटफैरड (CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| जार (लाइडेन जार) | `jar` | `jars` | 1.11265e-9 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`micro.farads`, `nano.farads`,
`pico.farads`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## अनेक अपघटन

धारिता कई **समतुल्य अपघटनों** से प्राप्त की जा सकती है, और सभी मान-समान धारिता देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `charge / voltage` | `KCapacitanceUnitInstance` | परिभाषा `C = Q / U` |
| `current²·time⁴/(mass·length²)` | `.toCapacitance()` द्वारा | मूल विहित `kg⁻¹·m⁻²·s⁴·A²` व्यंजक |

टाइप्ड ऑपरेटर रूप सीधे धारिता लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toCapacitance()` से संकुचित किया जाता है (जो केवल विहित रूप पहचानता है, अन्यथा `IllegalStateException`
फेंकता है)। दोनों रास्ते मान-समान हैं।

व्युत्क्रम ऑपरेटर आवेश, वोल्टेज और धारिता को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `capacitance * voltage` | `KChargeUnitInstance` | `Q = C · U` (क्रमविनिमेय) |
| `charge / capacitance` | `KVoltageUnitInstance` | `U = Q / C` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.capacitance.*

// वास्तविक उदाहरण - आवेशित संधारित्र: 12 V पर 470 µF का संधारित्र 5.64 mC संग्रहीत करता है।
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance, 0.00564 C

// धारिता के लिए हल की गई परिभाषा:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance, 2 F

// वही धारिता मूल kg⁻¹·m⁻²·s⁴·A² व्यंजक के रूप में:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

(470 of farads).toString()     // "470.0 F" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप में तथा KUnit के साथ Kotlin में कैसे लिखे जाते हैं। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁴`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न तथा ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `F` | `farads` | धारिता, आधार इकाई (नामित टोकन, फैरड) |
| `A²·s⁴/(kg·m²)` | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))` | धारिता = धारा²·समय⁴ / (द्रव्यमान·लंबाई²) (भिन्न रूप) |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | वही धारिता शुद्ध गुणनफल के रूप में |
| `µF` | `micro.farads` | उपसर्ग सहित धारिता (माइक्रोफैरड) |
