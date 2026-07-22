# मिश्रित इकाइयाँ

एक **मिश्रित इकाई** कई `KUnit` से बना मान है, प्रत्येक अपने घातांक तक उठाया गया,
जैसे किसी चाल के लिए `m^1 * s^-1`, या किसी बल के लिए `m^1 * kg^1 * s^-2`। kunit में इसे सामान्य
`KMixedUnitInstance` वर्ग द्वारा दर्शाया जाता है।

जबकि समूह-विशिष्ट रैपर वर्ग (जैसे `KLengthUnitInstance`, देखें
[पूर्वनिर्धारित इकाइयाँ](units/kinematics/distance.md)) एकल भौतिक विमा के साथ काम करने के लिए सुविधाजनक
हैं, `KMixedUnitInstance` वह है जिसकी ओर आप तब बढ़ते हैं जब आपको **विभिन्न** समूहों की इकाइयों को संयोजित
करना हो, या जब आप रैपर वर्गों द्वारा प्रदत्त स्वचालित समान-समूह रूपांतरण नहीं चाहते।

## संरचना

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` प्रसामान्यीकृत `Double` परिमाण है, सदैव ठीक `units` में सूचीबद्ध इकाइयों और घातांकों के सापेक्ष
  — समूह रैपरों के विपरीत, `KMixedUnitInstance` किसी समूह की मूल इकाई में **कोई** प्रसामान्यीकरण नहीं
  करता।
- `units`, `(KUnit, exponent)` युग्मों की सूची है जो भौतिक विमा का वर्णन करती है।

प्रत्येक «शुद्ध» इकाई इस सामान्य निरूपण में बदलने के लिए एक `toUnit()` विस्तार उजागर करती है:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## गुणन और भाग

दो `KMixedUnitInstance` के बीच `*` और `/` **सदैव** अनुमत हैं — कोई विमीय प्रतिबंध नहीं, क्योंकि इकाइयों का
गुणन/भाग सदैव भौतिक रूप से सार्थक है।

- `*` मिलती इकाइयों के घातांक जोड़ता है, और केवल एक पक्ष पर मौजूद किसी भी इकाई को सरलता से आगे ले जाता है।
- `/` मिलती इकाइयों से दाएँ पक्ष के घातांक घटाता है (और केवल दाएँ पक्ष पर मौजूद इकाइयों के घातांक को
  ऋणात्मक करता है)।
- परिणामी `0` घातांक उस इकाई को परिणाम से पूरी तरह हटा देता है।

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

दो भिन्न इकाई समूहों (जैसे लंबाई और, उपलब्ध होने पर, समय) को मिलाना ठीक उसी तरह काम करता है और वास्तव में
एक मिश्रित इकाई उत्पन्न करता है:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## सादे संख्या से मापन

किसी भी इकाई मान को एक सादे `Number` से मापा जा सकता है। यह एक **केवल-परिमाण** संक्रिया है: यह मान बदलती
है पर इकाई-पदों और घातांकों को अछूता छोड़ती है, इसलिए परिणाम अपना प्रकार और विमा बनाए रखता है।

- `unit * n`, `n * unit` और `unit / n` सभी **वही प्रकार-युक्त इकाई** लौटाते हैं (लंबाई लंबाई रहती है,
  क्षेत्रफल क्षेत्रफल रहता है)।
- `n / unit` विमा को **व्युत्क्रमित** करता है (हर घातांक ऋणात्मक होता है) और इसलिए एक सामान्य
  `KMixedUnitInstance` उत्पन्न करता है — किसी आवर्तकाल से आवृत्ति जैसे व्युत्क्रम बनाने का मुहावरेदार
  तरीक़ा।
- जानबूझकर **कोई** अदिश `+`/`-` नहीं है: किसी विमाहीन संख्या को विमायुक्त मान में जोड़ना निरर्थक है।

एक वास्तविक उदाहरण — एक वृत्त का क्षेत्रफल, `A = π · r²`, पूरी तरह इकाई प्रणाली के माध्यम से गणना किया गया:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance, 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²
area into (meters * meters)      // ≈ 0.04524 (वर्ग मीटर)
```

किसी सादी लंबाई को मापना या किसी मार्ग को समान चरणों में बाँटना उसी तरह काम करता है:

```kotlin
val tripled = (12 of meters) * 3 // KLengthUnitInstance, 36 m
val leg = (10 of kilo.meters) / 4 // KLengthUnitInstance, 2.5 km (मार्ग का एक चौथाई)
```

किसी संख्या को किसी इकाई **से** भाग देना विमा को व्युत्क्रमित करता है, जैसे किसी आवर्तकाल से आवृत्ति:

```kotlin
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.time.seconds

val frequency = 1 / (2 of seconds) // KMixedUnitInstance: value=0.5, units=[SECOND^-1]  (0.5 Hz)
```

ऐफ़ाइन **परम तापमान** समूह एकमात्र अपवाद है: किसी परम तापमान को किसी संख्या से मापना भौतिक रूप से निरर्थक
है (इसका केल्विन मान −273.15 का ऑफ़सेट वहन करता है), इसलिए `(20 of celsius) * 2` एक **संकलन त्रुटि** है।
इसके बजाय किसी रैखिक **तापमान अंतर** को मापें (देखें
[तापमान अंतर](units/thermodynamics/temperature-difference.md))।

## जोड़ और घटाव

`*`/`/` के विपरीत, `+` और `-` केवल उन दो `KMixedUnitInstance` के बीच अनुमत हैं जो **समान भौतिक विमा** का
वर्णन करते हैं: एक पक्ष के हर पद के लिए दूसरे पक्ष पर ठीक एक पद होना चाहिए जो समान इकाई समूह (जैसे सभी
`KDistanceUnit` मान) से समान घातांक (क्रम-निरपेक्ष) के साथ संबंधित हो। `KUnit` स्वयं समान होने की आवश्यकता
**नहीं**; मिलते पद प्रसामान्यीकरण के माध्यम से स्वचालित रूप से रूपांतरित होते हैं, ठीक वैसे ही जैसे
समूह-विशिष्ट रैपर वर्ग (`KLengthUnitInstance`, आदि) «शुद्ध» इकाइयों के लिए करते हैं। परिणाम बाएँ संकार्य
के `units` में व्यक्त होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032 (3 मील मीटर में रूपांतरित, फिर जोड़ा गया), units=[METER^1]
```

बेमेल इकाई समूह या बेमेल घातांक अब भी विफल होते हैं:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // IllegalStateException फेंकता है: समय पद के लिए कोई मिलता इकाई समूह नहीं
a + ((2 of meters) pow 2).toUnit() // IllegalStateException फेंकता है: बेमेल घातांक (1 बनाम 2)
```

पहले से एक **सटीक** मिलान (समान `KUnit`, न कि केवल समान समूह) जाँचने के लिए `hasSameUnits` का उपयोग करें:

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // (unit -> exponent) हस्ताक्षर की तुलना करता है, क्रम-निरपेक्ष
```

## मान पढ़ना

`into` किसी लक्ष्य इकाई टेम्पलेट (एक नंगा टोकन, एक उपसर्ग-युक्त बिल्डर टेम्पलेट, या एक विशेष मान-1
इंस्टेंस) में मान पढ़ता है, एक सादा `Double` लौटाते हुए। दोनों पक्षों को समान भौतिक विमा का वर्णन करना
चाहिए। कोई `valueAs` और कोई कस्टम-इकाई `toString` नहीं है; किसी विशिष्ट इकाई को
`"${v into kilo.meters} km"` के रूप में स्वरूपित करें।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0 (km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

डिफ़ॉल्ट (बिना-तर्क) `toString()` सदैव प्रत्येक पद का अपना `KUnit.symbol` उपयोग करता है, `*` से जुड़ा हुआ,
जैसे `"5.0 m*s^-1"`।

## शुद्ध इकाइयों और मिश्रित इकाइयों का मिश्रण

प्रत्येक शुद्ध इकाई रैपर वर्ग `KMixedUnitInstance` के विरुद्ध सीधे `*`/`/` का समर्थन करता है, इसलिए इन
संकारकों के लिए आपको शायद ही कभी स्पष्ट रूप से `toUnit()` कॉल करना पड़ता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## वापस शुद्ध इकाई में बदलना

एक बार जब कोई `KMixedUnitInstance` पुनः किसी एकल इकाई समूह के ठीक एक पद को दर्शाता है, तो इसे उस समूह के
रैपर वर्ग में समूह-विशिष्ट `toXxxUnit()` विस्तार (जैसे `toDistance()`) के माध्यम से वापस बदला जा सकता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0 (एक क्षेत्रफल, घातांक 2)
```

यदि `KMixedUnitInstance` उस समूह के ठीक एक पद से **नहीं** बना है (जैसे यह अब भी एक मिश्रित लंबाई/समय मान
है), तो रूपांतरण `IllegalStateException` फेंकता है।

वही संकुचन **किसी दूरी मान पर सीधे** भी उपलब्ध है (केवल `KMixedUnitInstance` पर नहीं): एक सामान्य
`KDistanceUnitInstance` — या कोई भी पत्ती — को `toLength()`, `toArea()` या `toVolume()` से किसी विशिष्ट
विमा तक संकुचित किया जा सकता है, जो घातांक-जाँचित हैं और बेमेल पर `IllegalStateException` फेंकते हैं:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance (घातांक 2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0 (चौड़ा किया, फिर वापस संकुचित)
area.toLength()                              // IllegalStateException (घातांक 2, न कि 1)
```
