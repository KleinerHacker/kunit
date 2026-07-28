# भंडारण घनत्व

पैकेज: `org.pcsoft.framework.kunit.it.storagedensity`
आधार इकाई: **बाइट प्रति वर्ग मीटर** (`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

प्रकार: **संयोजित इकाई**

भंडारण घनत्व एक **संयोजित** इकाई है: यह कोई एकल "वास्तविक" राशि नहीं, बल्कि एक संयोजन है, `storage · distance⁻²`
(`B/m²`)। इसलिए `KStorageDensityUnitInstance` ठीक दो पदों वाले एक `KMixedUnitInstance` को लपेटता है — घातांक
`+1` वाला एक `KStorageUnit.BASE` (बाइट) और घातांक `-2` वाला एक `KDistanceUnit.BASE` (मीटर)। मान हमेशा बाइट
प्रति वर्ग मीटर में सामान्यीकृत होकर संग्रहीत रहता है, चाहे इसे किसी भी इकाई या भंडारण/क्षेत्रफल संयोजन से बनाया गया हो।

## भंडारण घनत्व बनाना

भंडारण घनत्व को **भंडारण-प्रति-क्षेत्रफल व्यंजक** के रूप में बनाया जाता है, जैसे `100 of bytes / area`,
`5 of mega.bytes / area`। क्षेत्रफल कोई भी `KAreaUnitInstance` है (जैसे `(1 of meters) * (1 of meters)`), अतः सभी
SI/द्विआधारी उपसर्ग और लंबाई इकाइयाँ स्वतंत्र रूप से जुड़ती हैं। इसे किसी भी भंडारण-प्रति-क्षेत्रफल टेम्पलेट में वापस पढ़ें
(`d into (bits / area)`)। जानबूझकर कोई वर्तनी-सहित संयुक्त टोकन **नहीं** है।

आधार इकाई: भंडारण समूह के अनुरूप *बाइट* प्रति वर्ग मीटर। "बिट प्रति वर्ग मीटर" `0.125 B/m²` है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0 (B/m² में सामान्यीकृत)
d into (bits / area)  // 800.0 (bit/m² में वापस पढ़ा गया)
```

## वास्तविक उदाहरण: SSD डाई का क्षेत्रीय घनत्व

एक फ़्लैश डाई **100 mm²** के तल पर **256 GB** संग्रहीत करती है। इसका क्षेत्रीय भंडारण घनत्व डेटा की मात्रा को
क्षेत्रफल से विभाजित करने पर मिलता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // 10 mm × 10 mm डाई = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15 (B/m²)
density into (giga.bytes / (side * side))           // 256.0 (प्रति 100 mm² GB)
```

## मूल इकाइयों (भंडारण और क्षेत्रफल) के साथ गणना

भंडारण घनत्व *ही* भंडारण मात्रा को क्षेत्रफल से विभाजित करना है। तीन राशियों — भंडारण, क्षेत्रफल और भंडारण घनत्व —
के बीच सामान्य `*` और `/` से आवागमन करें; प्रत्येक परिणाम **दृढ़-प्रकार** है।

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `storage / area` | `KStorageDensityUnitInstance` | घनत्व = मात्रा / क्षेत्रफल |
| `storage density * area` | `KStorageUnitInstance` | मात्रा = घनत्व × क्षेत्रफल |
| `area * storage density` | `KStorageUnitInstance` | मात्रा (क्रमविनिमेय) |
| `storage / storage density` | `KAreaUnitInstance` | क्षेत्रफल = मात्रा / घनत्व |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- मूल इकाइयाँ -> भंडारण घनत्व --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance (.toStorageDensity() की आवश्यकता नहीं!)
d.value               // 100.0 (B/m²)

// --- भंडारण घनत्व -> भंडारण (क्षेत्रफल से गुणा करें) -------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // वही परिणाम (क्रमविनिमेय)

// --- भंडारण घनत्व -> क्षेत्रफल (भंडारण मात्रा को इससे विभाजित करें) ------------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "केवल *शुद्ध* भंडारण / क्षेत्रफल आकार ही भंडारण घनत्व है"
    `KMixedUnitInstance.toStorageDensity()` के लिए घातांक `+1` वाला ठीक एक भंडारण पद और घातांक `-2` वाला
    ठीक एक दूरी पद आवश्यक है। `B²·m⁻²`, `B·m⁻¹`, या `B·m²` आकार भंडारण घनत्व नहीं है — रूपांतरण
    `IllegalStateException` फेंकता है। इसी तरह, `storage + storage density` (भिन्न विमाएँ) एक संकलन त्रुटि है।

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : एक ही समूह, बाइट- और बिट-आधारित घनत्व के बीच स्वतः रूपांतरण
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// तुलना (सामान्यीकृत B/m² मान के अनुसार)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// दो भंडारण घनत्वों के बीच * / / एक KMixedUnitInstance में निकल जाते हैं (अब शुद्ध घनत्व नहीं)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## SI और द्विआधारी (IEC) उपसर्ग

भंडारण घनत्व समूह [भंडारण](storage.md) समूह की उपसर्ग नीति का अनुसरण करता है (इसका अंश एक भंडारण मात्रा है):
अंश **वर्धक** SI बिल्डर (`kilo`, `mega`, …) या **द्विआधारी** बिल्डर (`kibi`, `mebi`, …) का उपयोग करता है; हर
(क्षेत्रफल) किसी भी लंबाई इकाई और उपसर्ग का उपयोग करता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## toString स्वरूपण

केवल आधार-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` या `format` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.it.storage.bytes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²" (आधार इकाई)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखे जाते हैं। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) का उपयोग करते हैं, `·` गुणन को और `/` भिन्न को दर्शाता है। जहाँ किसी राशि को भिन्न रूप में और ऋणात्मक घातांक वाले गुणनफल रूप में—दोनों तरह से लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `B/m²` | `bytes / area` | भंडारण घनत्व, आधार इकाई (बाइट प्रति वर्ग मीटर) — भिन्न रूप |
| `B·m⁻²` | `bytes * (meters pow -2)` | वही घनत्व ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `bit/m²` | `bits / area` | बिट प्रति वर्ग मीटर |
| `kB/mm²` | `kilo.bytes / mm2` | किलोबाइट प्रति वर्ग मिलीमीटर |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | भंडारण ÷ क्षेत्रफल से निर्मित |
