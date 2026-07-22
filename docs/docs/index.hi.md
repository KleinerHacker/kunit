# KUnit

**KUnit** एक Kotlin फ़्रेमवर्क है (Java से भी उपयोग योग्य) जो नंगे संख्याओं के बजाय भौतिक इकाइयों के साथ
गणना करने के लिए है। मीटर, मील या वर्ग मीटर को सादे `Double` मानों के रूप में ट्रैक करने और यह आशा करने के
बजाय कि हर कॉल-साइट इकाई पर सहमत है, `kunit` इकाई को मान के साथ रखता है और आपके लिए रूपांतरण, गुणन और
विमीय हिसाब-किताब करता है।

## KUnit क्यों?

भौतिक राशियों के लिए कच्ची संख्याओं के साथ काम करना त्रुटि-प्रवण है: बिना रूपांतरण के गलती से मीटर में मील
जोड़ देना, या किसी लंबाई में क्षेत्रफल जोड़ देना आसान है। kunit इसे इकाई को प्रकार का हिस्सा बनाकर हल करता
है:

- **दो क्रियाएँ, `of` और `into`.** `number of <unit>` (`5 of meters`) से बनाएँ, `value into <unit>`
  (`v into kilo.meters`) से पढ़ें। संख्या और इकाई सख्ती से अलग हैं।
- **प्रकार-सुरक्षित अंकगणित.** असंगत इकाई समूहों या घातांकों के बीच `+` और `-` चुपचाप गलत संख्या उत्पन्न
  करने के बजाय `IllegalStateException` फेंकते हैं।
- **स्वचालित रूपांतरण.** `(5 of meters) + (3 of miles)` बस काम करता है — दोनों संकार्य आंतरिक रूप से
  प्रसामान्यीकृत होते हैं, इसलिए संयोजन से पहले आपको इकाइयों को मैन्युअल रूप से रूपांतरित नहीं करना पड़ता।
- **मुक्त-रूप गुणन और भाग.** इकाइयों का गुणन या भाग *हमेशा* अनुमत है और स्वचालित रूप से परिणामी भौतिक विमा
  (घातांक) को ट्रैक करता है, जैसे `length * length` एक क्षेत्रफल बन जाता है।
- **पूर्ण `Number` समर्थन.** `Int`, `Long`, `Float`, `Double` और किसी भी अन्य `Number` प्रकार से मान
  बनाएँ; सब कुछ आंतरिक रूप से `Double` में प्रसामान्यीकृत होता है।
- **संपूर्ण SI उपसर्ग तालिका**, क्वेटा (Q) से क्वेक्टो (q) तक, उपसर्ग बिल्डरों (`kilo.meters`,
  `milli.seconds`) के रूप में, प्रति-इकाई उपसर्ग नीति संकलन-समय पर लागू।
- **नामित विशेष इकाइयाँ** (जैसे हेक्टेयर, लीटर, एकड़) `of`/`into` के साथ प्रयुक्त सामान्य मान-1 टोकन के
  रूप में।

## मूल संकल्पनाएँ

kunit दो केंद्रीय प्रकारों के इर्द-गिर्द बना है:

- **`KMixedUnitInstance`** — एक *मिश्रित इकाई*: एक `Double` आधार मान के साथ एक या अधिक `KUnit`, प्रत्येक
  एक पूर्णांक घातांक के साथ युग्मित (जैसे किसी चाल के लिए `m^1 * s^-1`)। यह वह सामान्य इंजन है जो बाकी सब
  को शक्ति देता है।
- **`KUnit`** — किसी इकाई समूह से संबंधित एकल «शुद्ध» इकाई (जैसे मीटर लंबाई समूह से संबंधित है)। ठोस इकाई
  समूह `enum class ... : KUnit` (जैसे `KDistanceUnit`) के रूप में मॉडल किए जाते हैं।

प्रत्येक इकाई समूह इसके अतिरिक्त एक **रैपर वर्ग** (जैसे `KLengthUnitInstance`) प्रदान करता है जो एकल इकाई
समूह तक सीमित `KMixedUnitInstance` को समाहित करता है, सदैव उस समूह की मूल इकाई में प्रसामान्यीकृत। यही वह
प्रकार है जिसे आप अधिकांश समय उपयोग करेंगे — आज उपलब्ध इकाइयों के लिए
[पूर्वनिर्धारित इकाइयाँ](units/kinematics/distance.md) देखें, और सीधे सामान्य `KMixedUnitInstance` इंजन तक
कब और कैसे उतरें इसके लिए [मिश्रित इकाइयाँ](mixed-units.md) देखें।

यदि आप किसी नई भौतिक राशि (जैसे द्रव्यमान या समय) के लिए समर्थन जोड़ना चाहते हैं, तो पूर्ण चरण-दर-चरण
मार्गदर्शन के लिए [कस्टम इकाइयाँ जोड़ना](custom-units.md) देखें।

!!! note "इकाई ऑब्जेक्ट अपरिवर्तनीय हैं"
    प्रत्येक इकाई मान — `KMixedUnitInstance` इंजन के साथ-साथ `KLengthUnitInstance` या
    `KTimeUnitInstance` जैसा प्रत्येक «शुद्ध» रैपर — **अपरिवर्तनीय** है। कोई भी संक्रिया किसी मौजूदा इंस्टेंस
    को कभी नहीं बदलती; संकारक (`+`, `-`, `*`, `/`) और रूपांतरण सदैव एक **नया** ऑब्जेक्ट लौटाते हैं, संकार्यों
    को अछूता छोड़ते हुए। यह इकाई मानों को स्वतंत्र रूप से साझा करने और कुंजियों या स्थिरांकों के रूप में
    उपयोग करने के लिए सुरक्षित बनाता है।

## त्वरित आरंभ

मॉड्यूल को एक निर्भरता के रूप में जोड़ें (या इसे प्रोजेक्ट/सोर्स-सेट के रूप में शामिल करें) और आवश्यक इकाई
समूह की शब्दावली आयात करें।

### लंबाई

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// किसी भी Number प्रकार से `of` द्वारा शुद्ध लंबाई मान बनाएँ
val distance = 5 of meters
val trip = 10 of miles

// संकारक: समान समूह और घातांक के भीतर स्वचालित रूपांतरण
val total = distance + trip          // KLengthUnitInstance, मीटर में प्रसामान्यीकृत
val diff = trip - distance

// तुलनाएँ
val isFarther = trip > distance      // true

// किसी विशिष्ट इकाई में मान को `into` से पढ़ें
println(total into kilo.meters)      // जैसे 21.0467...
println(total into yards)            // जैसे 23018.4...

// शुद्ध लंबाइयों का गुणन एक प्रबल-प्रकार क्षेत्रफल बनाता है
val area = distance * trip           // KAreaUnitInstance

// क्षेत्रफल (घातांक 2) और आयतन (घातांक 3) के लिए नामित विशेष इकाइयाँ
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### SI उपसर्ग

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0 (मीटर में प्रसामान्यीकृत)
```

### मिश्रित / संयुक्त इकाइयाँ

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds

// मान-1 टेम्पलेटों से एक इकाई व्यंजक रचें और उसे `of` से मापें
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance, m·s⁻²
```

## चेकआउट और बिल्ड

```bash
git clone <repository-url>
cd kunit
```

kunit Gradle का उपयोग करता है (रैपर रिपॉज़िटरी में शामिल है, किसी स्थानीय Gradle संस्थापन की आवश्यकता नहीं):

```bash
# बिल्ड
./gradlew build          # Windows: gradlew.bat build

# केवल परीक्षण चलाएँ
./gradlew test            # Windows: gradlew.bat test
```

टूलचेन 25 को हल करने में सक्षम एक JDK आवश्यक है (`foojay-resolver` प्लगइन आवश्यकता होने पर इसे स्वचालित
रूप से डाउनलोड करता है)।
