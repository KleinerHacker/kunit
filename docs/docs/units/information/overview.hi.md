# सूचना प्रौद्योगिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.storage`, `…datarate`

सूचना प्रौद्योगिकी **डिजिटल डेटा मात्राओं** और उनके स्थानांतरण की गति से संबंधित है। KUnit संचित मात्रा
को एक **नेटिव** मूल राशि (भंडारण, बाइट में) और थ्रूपुट को उससे **निर्मित** राशि (डेटा दर = भंडारण प्रति
समय) के रूप में मॉडल करता है, जिससे "इस डाउनलोड में कितना समय लगेगा?" जैसा रोज़मर्रा का प्रश्न एक
प्रकार-युक्त व्यंजक बन जाता है।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | मूल इकाई | पृष्ठ |
|---|---|---|---|
| भंडारण | नेटिव | बाइट (`B`) | [भंडारण](storage.md) |
| डेटा दर | निर्मित | बाइट प्रति सेकंड (`B/s`) | [डेटा दर](datarate.md) |

दोनों समूह समान उपसर्ग नीति साझा करते हैं: **कोई ह्रासमान उपसर्ग नहीं** (बिट का अंश निरर्थक है), और
दशमलव SI उपसर्गों (`kilo` = 1000) के अलावा एक दूसरा **द्विआधारी (IEC)** परिवार (`kibi` = 1024)।

## राशियाँ कैसे संबंधित हैं

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `storage / time` | डेटा दर | `r = मात्रा / t` |
| `data rate * time` | भंडारण | `मात्रा = r · t` |
| `time * data rate` | भंडारण | `मात्रा = r · t` (क्रमविनिमेय) |
| `storage / data rate` | समय | `t = मात्रा / r` |

## वास्तविक उदाहरण — डाउनलोड समय

एक **500 MB** फ़ाइल को **10 MB/s** लिंक पर डाउनलोड किया जाता है। समय `t = मात्रा / दर` है, और दर को उस
समय से गुणा करने पर मात्रा `मात्रा = r · t` पुनः प्राप्त होती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance, 10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0 (s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0 (MB)
```

## वास्तविक उदाहरण — दशमलव बनाम द्विआधारी

समान संख्यात्मक मात्रा दशमलव (`kB`) और द्विआधारी (`KiB`) टेम्पलेट के विरुद्ध भिन्न रूप से पढ़ी जाती है —
1000 बनाम 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096 (kB, दशमलव 1000)
size into kibi.bytes    // 4.0   (KiB, द्विआधारी 1024)
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (मान + प्रतीक) में प्रस्तुत करता है; किसी अन्य इकाई के
लिए, इसे स्ट्रिंग टेम्पलेट में `into` से पढ़ें और प्रतीक स्वयं जोड़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s" (मूल इकाई)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## संकेतन

नीचे दी गई तालिका इस क्षेत्र के मूल संबंधों को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। घातांक
Unicode उपरिलेख (`⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `r = मात्रा / t` | `(500 of mega.bytes) / (50 of seconds)` | मात्रा ÷ समय से डेटा दर |
| `मात्रा = r · t` | `rate * (50 of seconds)` | दर × समय से मात्रा |
| `t = मात्रा / r` | `amount / rate` | मात्रा ÷ दर से समय |
| `1 kB = 1000 B` | `kilo.bytes` | दशमलव-उपसर्ग बाइट |
| `1 KiB = 1024 B` | `kibi.bytes` | द्विआधारी-उपसर्ग बाइट |

## आगे कहाँ जाएँ

* [भंडारण](storage.md) — नेटिव बाइट समूह, दशमलव और द्विआधारी उपसर्ग।
* [डेटा दर](datarate.md) — भंडारण प्रति समय, और भंडारण ↔ समय ↔ दर संकारक।
