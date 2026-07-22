# दूरी

पैकेज: `org.pcsoft.framework.kunit.distance`
मूल इकाई: **मीटर** (`KDistanceUnit.BASE == KDistanceUnit.METER`)

प्रकार: **नेटिव इकाई**

दूरी समूह घातांकों को एक खुले आधार रैपर `KDistanceUnitInstance` (किसी **भी** घातांक पर
`KDistanceUnit.BASE` का एकल पद) के अंतर्गत उनके अपने संकलन-समय-सुरक्षित प्रकारों के रूप में मॉडल करता है:

* **`KLengthUnitInstance`** — घातांक 1 (एक लंबाई)
* **`KAreaUnitInstance`** — घातांक 2 (एक क्षेत्रफल)
* **`KVolumeUnitInstance`** — घातांक 3 (एक आयतन)

मान सदैव मीटर (या वर्ग/घन मीटर) में प्रसामान्यीकृत संग्रहित होता है। चूँकि लंबाई, क्षेत्रफल और आयतन भिन्न
प्रकार हैं, उन्हें `+`/`-`/तुलना में मिलाना एक **संकलन त्रुटि** है (ऐसा कोई संकारक नहीं), जबकि `*`/`/`
जहाँ संभव हो परिवार में रहते हैं (`length * length = area`, `area / length = length`) और `{1,2,3}` के बाहर
के घातांकों (या विमाहीन घातांक-0 परिणाम) के लिए `KDistanceUnitInstance`/`KMixedUnitInstance` पर लौटते हैं।

हर मान को `number of <token>` से बनाएँ और `value into <token>` से वापस पढ़ें।

## घातांक 1 — लंबाई

| इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई मीटर में |
|---|---|---|---:|---:|
| मीटर | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| मील | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| नॉटिकल मील | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| यार्ड | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| फ़ुट | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| इंच | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| फ़ैदम | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| चेन | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| फ़र्लांग | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| खगोलीय इकाई | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| पारसेक | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |
| क्यूबिट | `KDistanceUnit.CUBIT` | `cubit` | `cubits` | 0.4572 |
| रोमन फ़ुट (pes) | `KDistanceUnit.ROMAN_FOOT` | `pes` | `romanFeet` | 0.2957 |
| रोमन पेस (passus) | `KDistanceUnit.ROMAN_PACE` | `passus` | `romanPaces` | 1.4787 |
| स्टेडियम | `KDistanceUnit.STADIUM` | `stadium` | `stadia` | 185.0 |
| रोमन मील (mille passus) | `KDistanceUnit.ROMAN_MILE` | `mp` | `romanMiles` | 1481.5 |
| रॉड (perch) | `KDistanceUnit.ROD` | `rod` | `rods` | 5.0292 |
| लीग | `KDistanceUnit.LEAGUE` | `lea` | `leagues` | 4828.032 |
| केबल लंबाई | `KDistanceUnit.CABLE_LENGTH` | `cable` | `cableLengths` | 185.2 |
| वर्स्त | `KDistanceUnit.VERST` | `verst` | `versts` | 1066.8 |
| प्रशियाई मील | `KDistanceUnit.PRUSSIAN_MILE` | `prussian mi` | `prussianMiles` | 7532.5 |

### प्रकाश-यात्रा दूरियाँ (उपसर्ग-रहित `light` समूह)

प्रकाश-यात्रा दूरियाँ उपसर्ग-रहित `light` बिल्डर के पीछे समूहित हैं और लगभग गद्य की तरह पढ़ी जाती हैं,
जैसे `5 of light.seconds`, `3 of light.years`। ये जानबूझकर **कोई** SI उपसर्ग स्वीकार नहीं करतीं (एक
`kilo.lightYears` भौतिक रूप से निरर्थक है)।

| इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई मीटर में |
|---|---|---|---:|---:|
| प्रकाश-सेकंड | `KDistanceUnit.LIGHT_SECOND` | `ls` | `light.seconds` | 299792458.0 |
| प्रकाश-मिनट | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `light.minutes` | 1.798754748e10 |
| प्रकाश-घंटा | `KDistanceUnit.LIGHT_HOUR` | `lh` | `light.hours` | 1.0792528488e12 |
| प्रकाश-दिन | `KDistanceUnit.LIGHT_DAY` | `ld` | `light.days` | 2.59020683712e13 |
| प्रकाश-सप्ताह | `KDistanceUnit.LIGHT_WEEK` | `lw` | `light.weeks` | 1.813144785984e14 |
| प्रकाश-वर्ष | `KDistanceUnit.LIGHT_YEAR` | `ly` | `light.years` | 9.4607304725808e15 |

प्रत्येक `Token` एक मान-1 `KLengthUnitInstance` है जो `of` (निर्माण) और `into` (पठन) दोनों के साथ प्रयुक्त
होता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72 (मीटर में प्रसामान्यीकृत)
d into miles          // 5.0 (मील में वापस पढ़ा)
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : समान समूह, भिन्न लंबाई इकाइयों के बीच स्वचालित रूपांतरण
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance, मीटर में प्रसामान्यीकृत
val b = (2 of miles) - (800 of meters)

// तुलनाएँ
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true (समान प्रसामान्यीकृत मान)
// (5 of hectares) > (5 of meters)       // संकलित नहीं होता: क्षेत्रफल बनाम लंबाई भिन्न प्रकार हैं

// * / / दोनों संकार्यों के स्थैतिक रूप से विमायुक्त होने पर लंबाई परिवार में रहते हैं
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0 (m)
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance (विमाहीन), value=5.0
```

### संख्या से मापन

किसी दूरी मान को एक सादे `Number` से मापा जा सकता है, इसके प्रकार को बनाए रखते हुए (लंबाई लंबाई रहती है,
क्षेत्रफल क्षेत्रफल)। इससे सूत्र-शैली की गणनाएँ स्वाभाविक रूप से पढ़ी जाती हैं — जैसे एक वृत्त का क्षेत्रफल
`A = π · r²` पूरी तरह इकाई प्रणाली के माध्यम से:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.distance.*

val r = 12 of centi.meters       // KLengthUnitInstance, 0.12 m
val area = Math.PI * (r * r)     // KAreaUnitInstance: π·r² ≈ 0.04524 m²

val tripled = (12 of meters) * 3 // KLengthUnitInstance, 36 m
val half = area / 2              // KAreaUnitInstance, वृत्त क्षेत्रफल का आधा
```

### तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` **समान प्रकार** (समान विमा) के दो मानों के प्रसामान्यीकृत `value` की तुलना
करते हैं। विमाओं को मिलाना (जैसे लंबाई और क्षेत्रफल) संकलक द्वारा अस्वीकृत होता है — ऐसा कोई संकारक नहीं —
`+`/`-` नियमों के अनुरूप। विमाओं के बीच `equals` सरलता से `false` लौटाता है।

## घातांक 2 — क्षेत्रफल

`KAreaUnitInstance` एक क्षेत्रफल को दर्शाता है, जैसे `length * length` का परिणाम या किसी लंबाई को infix
`pow` संकारक से द्वितीय घात तक उठाना (`(2 of meters) pow 2` == `(2 m)²` == 4 m²,
`(2 of kilo.meters) pow 2` == 4 000 000 m²)। कोई `squareXxx` टोकन नहीं — `pow` एकमात्र घात वाक्यविन्यास
है (देखें [`pow` से घात](#pow))। निम्नलिखित नामित विशेष-इकाई टोकन उपलब्ध हैं:

| विशेष इकाई | प्रतीक | टोकन | 1 इकाई m² में |
|---|---:|---:|---:|
| एयर | `a` | `ares` | 100.0 |
| हेक्टेयर | `ha` | `hectares` | 10 000.0 |
| एकड़ | `ac` | `acres` | 4046.8564224 |
| रूड | `ro` | `roods` | 1011.7141056 |
| वर्ग पर्च (वर्ग रॉड) | `perch²` | `squarePerches` | 25.29285264 |
| मॉर्गन (प्रशियाई) | `Mg` | `morgens` | 2553.22 |
| योख (ऑस्ट्रियाई) | `Joch` | `jochs` | 5754.642 |
| टागवर्क (बवेरियाई) | `Tw` | `tagwerks` | 3407.27 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0 (m²)
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance (10 000 m²)
computed into hectares                           // 1.0

plot + computed   // अनुमत: दोनों क्षेत्रफल हैं -> KAreaUnitInstance
// plot + (5 of meters)  // संकलित नहीं होता: क्षेत्रफल बनाम लंबाई
```

## घातांक 3 — आयतन

`KVolumeUnitInstance` एक आयतन को दर्शाता है, जैसे `length * length * length`, `area * length`, या किसी
लंबाई को तृतीय घात तक उठाना (`(2 of meters) pow 3` == 8 m³)। क्षेत्रफल की तरह, कोई `cubicXxx` टोकन नहीं —
`pow` का उपयोग करें (देखें [`pow` से घात](#pow))। निम्नलिखित नामित विशेष-इकाई टोकन उपलब्ध हैं:

| विशेष इकाई | प्रतीक | टोकन | 1 इकाई m³ में |
|---|---:|---:|---:|
| लीटर | `L` | `liters` | 0.001 |
| यूएस द्रव गैलन | `gal (US)` | `usGallons` | 0.003785411784 |
| इंपीरियल गैलन | `gal (UK)` | `imperialGallons` | 0.00454609 |
| यूएस फ़्लूइड औंस | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| तेल बैरल | `bbl` | `oilBarrels` | 0.158987294928 |
| इंपीरियल बुशल | `bu (UK)` | `imperialBushels` | 0.03636872 |
| इंपीरियल हॉग्सहेड | `hhd` | `hogsheads` | 0.32731785 |
| इंपीरियल पिंट | `pt (UK)` | `imperialPints` | 0.00056826125 |
| इंपीरियल क्वार्ट | `qt (UK)` | `imperialQuarts` | 0.0011365225 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2 (m³)
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance (8 m³)
cube into liters                                          // 8000.0

tank + cube         // अनुमत: दोनों आयतन हैं -> KVolumeUnitInstance
```

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ। Kotlin में कोई अतिभारयोग्य `^` संकारक नहीं
है (और न `^=`), इसलिए `pow` एकमात्र, समूह-व्यापी घात वाक्यविन्यास है — कोई `squareXxx`/`cubicXxx` टोकन
नहीं।

`pow` मान को उठाता है **और** हर घातांक को `n` से गुणा करता है, इसलिए `(2 of meters) pow 2` है
`(2 m)² = 4 m²` (मान उठाया जाता है, केवल घातांक नहीं)। दूरी समूह के लिए परिणाम विमायुक्त है: `pow 2` एक
`KAreaUnitInstance` देता है, `pow 3` एक `KVolumeUnitInstance`, अन्य घातांक सामान्य
`KDistanceUnitInstance`।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

val area = (2 of meters) pow 2         // KAreaUnitInstance: 4.0 m²
val big = (2 of kilo.meters) pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = (2 of meters) pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = (2 of meters) pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = (2 of meters) pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow`, `* / + -` से **कमज़ोर** बंधता है; मिश्रित व्यंजकों में कोष्ठक लगाएँ (`(a * b) pow 2`)। यह हर इकाई
समूह पर उपलब्ध है — जैसे `(2 of hours) pow 2` (एक सामान्य `KMixedUnitInstance`, चूँकि समय के पास कोई
विमायुक्त घात प्रकार नहीं है)।

## SI उपसर्ग

किसी भी लंबाई इकाई को 24 SI उपसर्ग **बिल्डरों** (`kilo`, `milli`, …; रूट पैकेज) में से किसी के साथ गुण
पहुँच के माध्यम से जोड़ा जा सकता है, जो `of`/`into` के लिए एक मान-1 टेम्पलेट उत्पन्न करता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// निर्माण: "5 of kilo.meters" -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// किसी उपसर्ग-युक्त इकाई में मान वापस पढ़ना
val d = 5 of miles
d into kilo.meters  // 8.04672 (km)

// उपसर्ग नामित क्षेत्रफल/आयतन टोकन के साथ भी संयोजित होते हैं
val tank = 200 of liters
tank into milli.liters  // 200000.0 (mL)
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m" (मूल इकाई निरूपण)
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।
जहाँ किसी राशि को भिन्न और ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य
Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `m` | `meters` | लंबाई, मूल इकाई (मीटर) |
| `km` | `kilo.meters` | उपसर्ग-युक्त लंबाई (किलोमीटर) |
| `m²` | `meters pow 2` | क्षेत्रफल (मीटर वर्ग) |
| `m³` | `meters pow 3` | आयतन (मीटर घन) |
| `m⁻¹` | `meters pow -1` | व्युत्क्रम लंबाई |
| `2 m · 2 m` | `(2 of meters) * (2 of meters)` | लंबाई × लंबाई से निर्मित क्षेत्रफल |
| `π · A` | `Math.PI * area` | अदिश × क्षेत्रफल (परिमाण मापन, क्षेत्रफल रहता है) |
| `A / 2` | `area / 2` | क्षेत्रफल एक सादे संख्या से विभाजित (क्षेत्रफल रहता है) |
