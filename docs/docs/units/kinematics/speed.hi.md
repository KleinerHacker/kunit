# चाल

पैकेज: `org.pcsoft.framework.kunit.speed`
मूल इकाई: **मीटर प्रति सेकंड** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

प्रकार: **निर्मित इकाई**

चाल पहली **निर्मित** इकाई है: लंबाई या समय के विपरीत यह कोई एकल «वास्तविक» राशि नहीं बल्कि एक संघटन है,
`length · time⁻¹` (`m/s`)। इसलिए `KSpeedUnitInstance` ठीक दो पदों के एक `KMixedUnitInstance` को लपेटता
है — घातांक `+1` पर एक `KDistanceUnit.BASE` (मीटर) और घातांक `-1` पर एक `KTimeUnit.BASE` (सेकंड)। मान
सदैव मीटर प्रति सेकंड में प्रसामान्यीकृत संग्रहित होता है, चाहे इसे किस इकाई या लंबाई/समय संयोजन से बनाया
गया हो।

## एक चाल बनाना

चाल एक **लंबाई-प्रति-समय व्यंजक** के रूप में बनाई जाती है, जैसे `10 of kilo.meters / hours` या
`100 of meters / (10 of seconds)` — दोनों एक `KSpeedUnitInstance` देते हैं। इसे किसी भी लंबाई-प्रति-समय
टेम्पलेट में वापस पढ़ें (`v into (kilo.meters / hours)`)। जानबूझकर `metersPerSecond` या
`kilometersPerHour` जैसे स्पष्ट संयुक्त टोकन **नहीं** हैं (वे ठीक `meters / seconds` /
`kilo.meters / hours` हैं)।

केवल वास्तविक एकल, पारंपरिक नाम वाली चालें मान-1 टोकन के रूप में बचती हैं (`of`/`into` के साथ प्रयुक्त):

| चाल | प्रतीक | टोकन | 1 इकाई m/s में |
|---|---|---:|---:|
| नॉट | `kn` | `knots` | 0.514444 (1852/3600) |
| मैक (ISA समुद्र तल) | `Ma` | `mach` | 340.29 |
| प्रकाश की चाल | `c` | `speedOfLight` | 299792458.0 |

> **मैक** समुद्र तल (15 °C) पर अंतरराष्ट्रीय मानक वायुमंडल में ध्वनि की चाल है। यह एक सुविधाजनक संदर्भ-बिंदु
> है, कोई भौतिक स्थिरांक नहीं — वास्तविक ध्वनि-चाल तापमान और ऊँचाई के साथ बदलती है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888...(m/s में प्रसामान्यीकृत)
v into (kilo.meters / hours)   // 50.0 (km/h में वापस पढ़ा)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408 (ध्वनि की चाल का अंश)
```

## मूल इकाइयों (लंबाई और समय) के साथ गणना

यही तो निर्मित इकाई का पूरा उद्देश्य है। एक चाल *होती है* एक लंबाई भाग एक समय। KUnit आपको तीनों राशियों —
लंबाई, समय और चाल — के बीच सादे `*` और `/` से आवागमन देता है, और हर परिणाम **प्रबल-प्रकार** होता है।
आपको कभी कोई कच्चा `KMixedUnitInstance` स्वयं बनाना या खोलना नहीं पड़ता।

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | चाल = दूरी / अवधि |
| `speed * time` | `KLengthUnitInstance` | दूरी = चाल × अवधि |
| `time * speed` | `KLengthUnitInstance` | दूरी (क्रमविनिमेय) |
| `length / speed` | `KTimeUnitInstance` | अवधि = दूरी / चाल |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- मूल इकाइयाँ -> चाल ------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance (.toSpeed() की आवश्यकता नहीं!)
v.value                    // 10.0 (m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// एक सघन दर के लिए लंबाई पर उपसर्ग लगाएँ (कोष्ठक-मुक्त, `of`, `/` से कमज़ोर बंधता है):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- चाल -> लंबाई (किसी समय से गुणा) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // वही परिणाम (क्रमविनिमेय)

// --- चाल -> समय (किसी लंबाई को इससे भाग) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "केवल एक *शुद्ध* लंबाई ही चाल में विभाजित होती है"
    `length / time` और `length / speed` के लिए लंबाई का घातांक 1 होना आवश्यक है। एक **क्षेत्रफल** (`m²`)
    या **आयतन** (`m³`) लंबाई नहीं है, इसलिए `area / time` `m²/s` होगा, चाल नहीं — संकारक चुपचाप गलत मान
    लौटाने के बजाय `IllegalStateException` फेंकता है। ऐसा मध्यवर्ती जानबूझकर बनाने के लिए, किसी संकार्य को
    `toUnit()` से मिश्रित स्तर पर गिराएँ:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance, [METER^2, SECOND^-1]
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : समान समूह, भिन्न चाल व्यंजकों के बीच स्वचालित रूपांतरण
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance, 20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// तुलनाएँ (प्रसामान्यीकृत m/s मान से)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// दो चालों के बीच * / / एक KMixedUnitInstance में भाग जाते हैं (अब शुद्ध चाल नहीं)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance, [m^2, s^-2]
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s" (मूल इकाई)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `m/s` | `meters / seconds` | चाल, मूल इकाई (मीटर प्रति सेकंड) — भिन्न रूप |
| `m·s⁻¹` | `meters * (seconds pow -1)` | वही चाल ऋणात्मक घातांक वाले गुणनफल के रूप में |
| `km/h` | `kilo.meters / hours` | किलोमीटर प्रति घंटा |
| `mi/h` | `miles / hours` | मील प्रति घंटा |
| `100 m / 10 s` | `(100 of meters) / (10 of seconds)` | लंबाई ÷ समय से निर्माण |
