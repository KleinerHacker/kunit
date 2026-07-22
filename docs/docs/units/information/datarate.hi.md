# डेटा दर

पैकेज: `org.pcsoft.framework.kunit.datarate`
मूल इकाई: **बाइट प्रति सेकंड** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

प्रकार: **निर्मित इकाई**

डेटा दर एक **निर्मित** इकाई है ([चाल](../kinematics/speed.md) के बाद दूसरी): यह कोई एकल «वास्तविक» राशि
नहीं बल्कि एक संघटन है, `storage · time⁻¹` (`B/s`)। इसलिए `KDataRateUnitInstance` ठीक दो पदों के एक
`KMixedUnitInstance` को लपेटता है — घातांक `+1` पर एक `KStorageUnit.BASE` (बाइट) और घातांक `-1` पर एक
`KTimeUnit.BASE` (सेकंड)। मान सदैव बाइट प्रति सेकंड में प्रसामान्यीकृत संग्रहित होता है, चाहे इसे किस
इकाई या भंडारण/समय संयोजन से बनाया गया हो।

## एक डेटा दर बनाना

डेटा दर एक **भंडारण-प्रति-समय व्यंजक** के रूप में बनाई जाती है, जैसे `100 of bytes / seconds`,
`5 of mega.bytes / seconds` या `10 of kibi.bytes / seconds` — प्रत्येक एक `KDataRateUnitInstance` देता
है। इसे किसी भी भंडारण-प्रति-समय टेम्पलेट में वापस पढ़ें (`r into (bits / seconds)`)। जानबूझकर
`bytesPerSecond` जैसे स्पष्ट संयुक्त टोकन **नहीं** हैं (वे ठीक `bytes / seconds` हैं)।

मूल इकाई: एक *बाइट* प्रति सेकंड, भंडारण समूह के अनुरूप। नेटवर्किंग-मूल बिट/सेकंड (`bps`) `0.125 B/s` है;
एक "मेगाबिट प्रति सेकंड" `1 of mega.bits / seconds` है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0 (B/s में प्रसामान्यीकृत)
r into (bits / seconds)  // 800.0 (bit/s में वापस पढ़ा)
```

## मूल इकाइयों (भंडारण और समय) के साथ गणना

एक डेटा दर *होती है* एक भंडारण मात्रा भाग एक समय। तीनों राशियों — भंडारण, समय और डेटा दर — के बीच सादे
`*` और `/` से आवागमन करें; हर परिणाम **प्रबल-प्रकार** होता है।

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | दर = मात्रा / अवधि |
| `data rate * time` | `KStorageUnitInstance` | मात्रा = दर × अवधि |
| `time * data rate` | `KStorageUnitInstance` | मात्रा (क्रमविनिमेय) |
| `storage / data rate` | `KTimeUnitInstance` | अवधि = मात्रा / दर |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- मूल इकाइयाँ -> डेटा दर --------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance (.toDataRate() की आवश्यकता नहीं!)
r.value                  // 10.0 (B/s)
r into (bits / seconds)  // 80.0

// उपसर्ग-युक्त अंश, कोष्ठक-मुक्त:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance (5 MB/s)

// --- डेटा दर -> भंडारण (किसी समय से गुणा) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // वही परिणाम (क्रमविनिमेय)

// --- डेटा दर -> समय (किसी भंडारण मात्रा को इससे भाग) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "केवल एक *शुद्ध* भंडारण / समय आकार ही डेटा दर है"
    `KMixedUnitInstance.toDataRate()` के लिए घातांक `+1` पर ठीक एक भंडारण पद और घातांक `-1` पर एक समय पद
    आवश्यक है। एक `B²` (भंडारण वर्ग), एक `B·s⁻²`, या एक `B·s` आकार डेटा दर नहीं है — रूपांतरण
    `IllegalStateException` फेंकता है। इसी प्रकार, `storage + data rate` (भिन्न विमाएँ) एक संकलन त्रुटि
    है।

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : समान समूह, बाइट- और बिट-आधारित दरों के बीच स्वचालित रूपांतरण
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance, 2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// तुलनाएँ (प्रसामान्यीकृत B/s मान से)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// दो डेटा दरों के बीच * / / एक KMixedUnitInstance में भाग जाते हैं (अब शुद्ध दर नहीं)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance, [B^2, s^-2]
```

## SI और द्विआधारी (IEC) उपसर्ग

डेटा-दर समूह [भंडारण](storage.md) समूह की उपसर्ग नीति को प्रतिबिंबित करता है (इसका अंश एक भंडारण मात्रा
है): अंश **संवर्धक** SI बिल्डरों (`kilo`, `mega`, …) या **द्विआधारी** बिल्डरों (`kibi`, `mebi`, …) का
उपयोग करता है; ह्रासमान बिल्डरों में कोई `bytes`/`bits` गुण नहीं है, इसलिए `milli.bytes / seconds`
संकलित नहीं होता।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// दशमलव बनाम द्विआधारी: 1000 (kilo) != 1024 (kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// किसी भंडारण-प्रति-समय टेम्पलेट में मान वापस पढ़ना
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096 (kB/s)
r into (kibi.bytes / seconds)  // 4.0   (KiB/s)
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s" (मूल इकाई)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `B/s` | `bytes / seconds` | डेटा दर, मूल इकाई (बाइट प्रति सेकंड) — भिन्न रूप |
| `B·s⁻¹` | `bytes * (seconds pow -1)` | वही दर ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `bit/s` | `bits / seconds` | बिट प्रति सेकंड |
| `MB/s` | `mega.bytes / seconds` | मेगाबाइट प्रति सेकंड |
| `100 B / 10 s` | `(100 of bytes) / (10 of seconds)` | भंडारण ÷ समय से निर्माण |
