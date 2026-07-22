# शुद्धगतिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.distance`, `…time`, `…speed`, `…acceleration`, `…frequency`

शुद्धगतिकी **गति** का वर्णन है — कितनी दूर, कितनी देर, कितनी तेज़, और गति की दर स्वयं कैसे बदलती है —
इसके पीछे के बलों को अभी पूछे बिना (वह [यांत्रिकी](../mechanics/overview.md) का विषय है)। KUnit इस
क्षेत्र को 2 **नेटिव** मूल राशियों और उनसे **निर्मित** 3 राशियों से मॉडल करता है, जिससे शास्त्रीय गति
सूत्र प्रबल-प्रकार बने रहते हुए सामान्य `*` और `/` व्यंजक बन जाते हैं।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | मूल इकाई | पृष्ठ |
|---|---|---|---|
| दूरी | नेटिव | मीटर (`m`) | [दूरी](distance.md) |
| समय | नेटिव | सेकंड (`s`) | [समय](time.md) |
| आवृत्ति | नेटिव | हर्ट्ज़ (`Hz`) | [आवृत्ति](frequency.md) |
| चाल | निर्मित | मीटर प्रति सेकंड (`m/s`) | [चाल](speed.md) |
| त्वरण | निर्मित | मीटर प्रति सेकंड² (`m/s²`) | [त्वरण](acceleration.md) |

## राशियाँ कैसे संबंधित हैं

चाल दूरी बटा समय है, त्वरण चाल बटा समय है, और आवृत्ति समय का व्युत्क्रम है। KUnit प्रत्येक संयोजन के लिए
सही **प्रकार-युक्त** राशि लौटाता है — आपको कभी कच्ची मिश्रित इकाई हाथ से बनानी नहीं पड़ती:

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `distance / time` | चाल | `v = s / t` |
| `speed * time` | दूरी | `s = v · t` |
| `speed / time` | त्वरण | `a = Δv / t` |
| `acceleration * time` | चाल | `v = a · t` |
| `distance * frequency` | चाल | `v = s · f` |

## वास्तविक उदाहरण — यात्रा की औसत चाल

एक कार **1.5 h** में **120 km** तय करती है। इसकी औसत चाल `v = s / t` है, और इस चाल को अवधि से गुणा करने
पर पुनः तय की गई दूरी मिलती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0 (km/h)
v.value                                          // ≈ 22.22 (m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0 (3 h में km)
```

## वास्तविक उदाहरण — धावक का त्वरण

एक धावक विराम से **2 s** में **10 m/s** तक पहुँचता है। त्वरण `a = Δv / t` है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance, 5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance, 10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51 (g का अंश)
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (मान + प्रतीक) में प्रस्तुत करता है; किसी अन्य इकाई के
लिए, इसे स्ट्रिंग टेम्पलेट में `into` से पढ़ें और प्रतीक स्वयं जोड़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s" (मूल इकाई)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## संकेतन

नीचे दी गई तालिका इस क्षेत्र के मूल संबंधों को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। घातांक
Unicode उपरिलेख (`²`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | दूरी ÷ समय से चाल |
| `s = v · t` | `v * (3 of hours)` | चाल × समय से दूरी |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | चाल ÷ समय से त्वरण |
| `v = a · t` | `a * (2 of seconds)` | त्वरण × समय से चाल |
| `f = 1 / T` | `1 / (2 of hertz)` | आवर्तकाल ↔ आवृत्ति (समय का व्युत्क्रम) |

## आगे कहाँ जाएँ

* [दूरी](distance.md) — लंबाई, क्षेत्रफल और आयतन एक समूह में।
* [समय](time.md) — `Duration` पर आधारित अवधियाँ।
* [चाल](speed.md) और [त्वरण](acceleration.md) — निर्मित गति दरें।
* [आवृत्ति](frequency.md) — समय का व्युत्क्रम और उसके पार-इकाई संकारक।
