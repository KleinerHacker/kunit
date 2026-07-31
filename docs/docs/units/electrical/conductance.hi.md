# चालकता

पैकेज: `org.pcsoft.framework.kunit.electric.conductance`
आधार इकाई: **सीमेंस** (`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`)

प्रकार: **संरचित इकाई**

विद्युत चालकता एक **संरचित** इकाई है: संयोजन `mass⁻¹ · length⁻² · time³ · current²`
(`kg⁻¹·m⁻²·s³·A²`)। `KConductanceUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `-1` पर
`KMassUnit.BASE` (ग्राम), घातांक `-2` पर `KDistanceUnit.BASE` (मीटर), घातांक `+3` पर
`KTimeUnit.BASE` (सेकंड) और घातांक `+2` पर `KElectricCurrentUnit.BASE` (एम्पियर)। चूँकि लाइब्रेरी का द्रव्यमान घटक
**ग्राम** (किलोग्राम नहीं) में सामान्यीकृत है और द्रव्यमान का घातांक ऋणात्मक है, इसलिए सीमेंस कच्चे घटक आधार का 1/1000
गुना है; संग्रहीत मान सीमेंस में सामान्यीकृत होता है।

चालकता [प्रतिरोध](resistance.md) का व्युत्क्रम है (`G = 1 / R`) और ओम के नियम के माध्यम से
[वोल्टेज](voltage.md) तथा [विद्युत धारा](ec.md) को जोड़ती है।

## चालकता बनाना

चालकता को नामित टोकन से, या किसी अपघटन से (नीचे देखें) बनाएँ। नामित इकाइयाँ मान-1 टोकन के रूप में बनी रहती हैं (`of`/
`into` के साथ प्रयुक्त):

| चालकता          | प्रतीक     |        टोकन |  1 इकाई (S में) |
|---------------|---------|-----------:|-------------:|
| सीमेंस           | `S`     |  `siemens` |          1.0 |
| म्हो (पारंपरिक नाम)  | `℧`     |     `mhos` |          1.0 |
| ऐबम्हो (CGS-EMU) | `ab℧`   |   `abmhos` |        1.0e9 |
| स्टैटम्हो (CGS-ESU) | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` बनाम `siemensUnits`"
`siemens` (इस पैकेज में) **चालकता** की SI इकाई है। मिलते-जुलते नाम वाला
`org.pcsoft.framework.kunit.electric.resistance` का `siemensUnits` ऐतिहासिक **सीमेंस मरकरी इकाई** है, जो 0.9534 Ω का
*प्रतिरोध* है। ये अलग-अलग पैकेजों की असंबंधित राशियाँ हैं।

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`milli.siemens`,
`micro.siemens`, `kilo.siemens`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## अनेक अपघटन

चालकता कई **समतुल्य अपघटनों** से प्राप्त की जा सकती है, और सभी मान-समान चालकता उत्पन्न करते हैं:

| व्यंजक                             | परिणाम प्रकार                   | अर्थ                          |
|---------------------------------|----------------------------|-----------------------------|
| `current / voltage`             | `KConductanceUnitInstance` | ओम का नियम `G = I / U`        |
| `1 / resistance`                | `KConductanceUnitInstance` | प्रतिरोध का व्युत्क्रम `G = 1 / R`      |
| `time³·current²/(mass·length²)` | `.toConductance()` द्वारा      | मूल विहित व्यंजक `kg⁻¹·m⁻²·s³·A²` |

प्रकार-युक्त ऑपरेटर रूप सीधे चालकता लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और उसे
`toConductance()` से संकुचित किया जाता है (जो केवल विहित रूप पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान-समान हैं।

व्युत्क्रम ऑपरेटर चालकता, वोल्टेज और धारा को आपस में जोड़ते हैं:

| व्यंजक                     | परिणाम प्रकार                       | अर्थ                   |
|-------------------------|--------------------------------|----------------------|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U` (क्रमविनिमेय) |
| `current / conductance` | `KVoltageUnitInstance`         | `U = I / G`          |
| `1 / conductance`       | `KResistanceUnitInstance`      | `R = 1 / G`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// वास्तविक उदाहरण - आपूर्ति केबल की चालकता: 2 A धारा वहन करने वाली केबल में
// मापा गया वोल्टेज पतन 1 V हो, तो उसकी चालकता 2 S है (अर्थात् प्रतिरोध 0.5 Ω)।
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance, 2 S
val r = 1 / g                            // KResistanceUnitInstance, 0.5 Ω

// प्रतिरोध के साथ व्युत्क्रम संबंध:
1 / (1 of ohms) == (1 of siemens)        // true

// वही चालकता मूल kg⁻¹·m⁻²·s³·A² व्यंजक के रूप में:
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप में कैसे लिखा जाता है, और KUnit के साथ Kotlin में
कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि
को भिन्न रूप में और ऋणात्मक घातांकों वाले गुणनफल के रूप में, दोनों तरह से लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप
सूचीबद्ध हैं।

| गणित              | Kotlin                                                                      | अर्थ                                         |
|------------------|-----------------------------------------------------------------------------|--------------------------------------------|
| `S`              | `siemens`                                                                   | चालकता, आधार इकाई (नामित टोकन, सीमेंस)               |
| `s³·A²/(kg·m²)`  | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))`       | चालकता समय³·धारा² / (द्रव्यमान·लंबाई²) के रूप में (भिन्न रूप) |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | वही चालकता शुद्ध गुणनफल के रूप में                    |
| `mS`             | `milli.siemens`                                                             | उपसर्ग-युक्त चालकता (मिलीसीमेंस)                       |
