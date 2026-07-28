# आवेश

पैकेज: `org.pcsoft.framework.kunit.electric.charge`
आधार इकाई: **कूलॉम** (`KChargeUnit.BASE == KChargeUnit.COULOMB`)

प्रकार: **संरचित इकाई**

विद्युत आवेश एक **संरचित** इकाई है: संयोजन `current · time` (`A·s`)। `KChargeUnitInstance` दो पदों वाले
`KMixedUnitInstance` को लपेटता है — घातांक `+1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर) और घातांक `+1` पर
`KTimeUnit.BASE` (सेकंड)। चाहे किसी भी नामित इकाई, SI उपसर्ग या धारा/समय संयोजन से बनाया गया हो, संग्रहीत
मान हमेशा कूलॉम में सामान्यीकृत रहता है।

## आवेश बनाना

आवेश को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में
उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| आवेश | संकेत | टोकन | 1 इकाई = ? C |
|---|---|---:|---:|
| कूलॉम | `C` | `coulombs` | 1.0 |
| ऐम्पियर सेकंड | `As` | `ampereSeconds` | 1.0 |
| ऐम्पियर घंटा | `Ah` | `ampereHours` | 3600.0 |
| ऐबकूलॉम (CGS-EMU) | `abC` | `abcoulombs` | 10.0 |
| स्टैटकूलॉम (CGS-ESU) | `statC` | `statcoulombs` | 3.335641e-10 |
| फैराडे | `F_c` | `faradays` | 96485.332 |
| मूल आवेश | `e` | `elementaryCharges` | 1.602176634e-19 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.coulombs`,
`milli.coulombs`, `milli.ampereHours`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## अनेक अपघटन

आवेश तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान आवेश देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `current * time` | `KChargeUnitInstance` | परिभाषा `Q = I · t` |
| `time * current` | `KChargeUnitInstance` | `Q = I · t` का क्रमविनिमेय रूप |
| `current / frequency` | `KChargeUnitInstance` | व्युत्क्रम‑समय रूप `Q = I / f` (`1/Hz = s`) |
| `current·time` | `.toCharge()` द्वारा | मूल विहित `A·s` व्यंजक |

टाइप किए गए ऑपरेटर रूप सीधे आवेश लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toCharge()` से संकुचित किया जाता है (जो केवल विहित रूप — घातांक `+1` पर एक `KElectricCurrentUnit` पद और
घातांक `+1` पर एक `KTimeUnit` पद — को पहचानता है, अन्यथा `IllegalStateException` फेंकता है)। सभी मार्ग
मान‑समान हैं।

व्युत्क्रम ऑपरेटर आवेश, धारा और समय को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `charge / time` | `KElectricCurrentUnitInstance` | `I = Q / t` |
| `charge / current` | `KTimeUnitInstance` | `t = Q / I` |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f` (व्युत्क्रम‑समय रूप) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.*

// वास्तविक उदाहरण - बैटरी क्षमता: 2000 mAh की सेल 7200 C संग्रहीत करती है।
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance, 7200 C

// 250 mA की स्थिर खपत पर यह कितनी देर चलेगी?
battery / (0.25 of amperes)               // KTimeUnitInstance, 28800 s (8 घंटे)

// वही आवेश टाइप किए गए अपघटन से और मूल A·s व्यंजक से:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance, 7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## विद्युत फ्लक्स

किसी बंद पृष्ठ के माध्यम से **विद्युत फ्लक्स** `Ψ` उसमें संलग्न आवेश के बराबर होता है (गॉस का नियम,
`Ψ = Q`)। इसलिए यह आवेश के **विमीय रूप से समान** है और कूलॉम में ही मापा जाता है। KUnit इसे इसी समूह और
प्रतीक `C` से मॉडल करता है; कोई अलग टोकन और कोई अलग प्रकार नहीं है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.*

// 2 µC संलग्न करने वाला एक गोला 2 µC का विद्युत फ्लक्स वहन करता है।
val psi = 2 of micro.coulombs
psi into micro.coulombs        // 2.0
```

किसी क्षेत्रफल से विभाजित करने पर फ्लक्स [विद्युत फ्लक्स घनत्व](electricfluxdensity.md) `D = Ψ / A`
देता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

(470 of coulombs).toString()   // "470.0 C" (आधार इकाई)
(1 of ampereHours).toString()  // "3600.0 C" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `C` | `coulombs` | आवेश, आधार इकाई (नामित टोकन, कूलॉम) |
| `A·s` | `amperes * seconds` | धारा·समय के रूप में आवेश (गुणनफल रूप) |
| `A/Hz` | `amperes / hertz` | वही आवेश धारा को आवृत्ति से भाग देकर (`1/Hz = s`) |
| `mAh` | `milli.ampereHours` | उपसर्ग सहित आवेश (मिलीऐम्पियर घंटा, बैटरी क्षमता) |

## यह भी देखें

- [विद्युत धारा](ec.md) — आवेश संयोजन का धारा गुणक
- [वोल्टता](voltage.md) — विभवांतर
- [प्रतिरोध](resistance.md) — ओम का नियम विद्युत समूह को पूर्ण करता है
