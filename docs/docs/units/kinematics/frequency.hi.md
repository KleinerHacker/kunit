# आवृत्ति

पैकेज: `org.pcsoft.framework.kunit.frequency`
मूल इकाई: **हर्ट्ज़** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

प्रकार: **नेटिव इकाई**

आवृत्ति समूह मॉडल करता है कि प्रति इकाई समय कोई घटना कितनी बार होती है। यह एक **नेटिव, एक-विमीय** समूह और
**समय का व्युत्क्रम** है (`1 Hz = 1/s`): `KFrequencyUnitInstance` एक एकल `KFrequencyUnit.HERTZ` पद को
लपेटता है, सदैव हर्ट्ज़ में प्रसामान्यीकृत।

चूँकि आवृत्ति समय का व्युत्क्रम है, इसका पार-समूह व्यवहार **समय के बिलकुल व्युत्क्रम** के रूप में
परिभाषित है: किसी आवृत्ति से गुणा किसी समय से भाग की तरह व्यवहार करता है, और किसी आवृत्ति से भाग किसी समय
से गुणा की तरह।

## इकाइयाँ

| इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई हर्ट्ज़ में |
|---|---|---|---:|---:|
| हर्ट्ज़ | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| चक्कर प्रति सेकंड | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| फ़्रेम प्रति सेकंड | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| चक्कर प्रति मिनट | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| धड़कन प्रति मिनट | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

प्रत्येक `Token` एक मान-1 `KFrequencyUnitInstance` है जो `of` (निर्माण) और `into` (पठन) के साथ प्रयुक्त
होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz (SI उपसर्ग द्वारा kHz)
f.value                      // 2000.0 (हर्ट्ज़ में प्रसामान्यीकृत)
(3000 of rpm) into hertz     // 50.0  (3000 rpm = 50 Hz)
(50 of hertz) into rpm       // 3000.0
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - : समान समूह, इकाइयों के बीच स्वचालित रूपांतरण
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// तुलनाएँ और समता (प्रसामान्यीकृत हर्ट्ज़ मान से)
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### व्युत्क्रम-समय पार संकारक

एक आवृत्ति और एक समय व्युत्क्रम हैं, इसलिए वे प्रबल-प्रकार परिणामों में संयोजित होते हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance, 60 Hz (गणना / समय = आवृत्ति)
val period = 1 / (2 of hertz)        // KTimeUnitInstance, 0.5 s   (गणना / आवृत्ति = समय)
val count = (50 of hertz) * (2 of seconds)   // 100.0 (आवृत्ति * समय = विमाहीन गणना)

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s (लंबाई * आवृत्ति = चाल)
(v / (5 of hertz)) into meters       // 2.0 (चाल / आवृत्ति = दूरी)
```

## वास्तविक उदाहरण: घूमते पहिये की सतही चाल

**2 m** परिधि वाला एक पहिया **5 चक्कर प्रति सेकंड** घूमता है। इसकी परिधि को घूर्णन आवृत्ति से गुणा करने पर
सतही (संपर्क) चाल मिलती है — `length * frequency = speed`, जो परिचित `length / time = speed` का
व्युत्क्रम है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed into meters                 // चाल समूह के माध्यम से m/s में पढ़ता है
surfaceSpeed.value                       // 10.0 m/s
```

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ (Kotlin में कोई अतिभारयोग्य `^` नहीं)। आवृत्ति
समूह के लिए `pow` एक सामान्य `KMixedUnitInstance` लौटाता है (आवृत्ति के पास कोई विमायुक्त घात प्रकार
नहीं):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## SI उपसर्ग

आवृत्ति **कोई भी** परिमाण स्वीकारती है, इसलिए हर SI उपसर्ग बिल्डर (`quetta` … `quecto`) को हर आवृत्ति
इकाई के साथ गुण पहुँच के माध्यम से जोड़ा जा सकता है। `kilo.hertz` kHz है, `mega.hertz` MHz है, `giga.hertz`
GHz है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0 (MHz)
(2_400_000_000 of hertz) into giga.hertz // 2.4 (GHz)
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz" (मूल इकाई निरूपण)
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `Hz` | `hertz` | आवृत्ति, मूल इकाई (हर्ट्ज़) |
| `kHz` | `kilo.hertz` | किलोहर्ट्ज़ (हर्ट्ज़ पर लागू उपसर्ग) |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | आवर्तकाल से आवृत्ति (प्रकार-युक्त हर्ट्ज़) |
| `Hz²` | `hertz pow 2` | हर्ट्ज़ वर्ग (सामान्य मिश्रित इकाई) |
