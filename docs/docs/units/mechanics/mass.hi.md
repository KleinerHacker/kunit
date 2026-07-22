# द्रव्यमान

पैकेज: `org.pcsoft.framework.kunit.mass`
मूल इकाई: **ग्राम** (`KMassUnit.BASE == KMassUnit.GRAM`)

प्रकार: **नेटिव इकाई**

द्रव्यमान समूह द्रव्यमान की एक मात्रा को मॉडल करता है। यह एक **सादा, एक-विमीय** समूह है (दूरी समूह की तरह
कोई घातांक-विशेषीकृत उपप्रकार नहीं, और समय समूह की तरह कोई `Duration` समर्थन नहीं): `KMassUnitInstance`
एक एकल `KMassUnit.GRAM` पद को लपेटता है, सदैव ग्राम में प्रसामान्यीकृत।

मूल इकाई जानबूझकर **ग्राम** है, किलोग्राम नहीं। किलोग्राम कोई समर्पित इकाई है ही नहीं — यह सरलता से
`kilo.grams` है, अर्थात् ग्राम पर लागू SI उपसर्ग `kilo`। ग्राम के हर दशमलव परिमाण (मिलीग्राम, किलोग्राम,
…) तक उसी सामान्य तरीके से SI उपसर्गों के माध्यम से पहुँचा जाता है।

## इकाइयाँ

| समूह | इकाई | Enum मान | प्रतीक | टोकन | 1 इकाई ग्राम में |
|---|---|---|---|---:|---:|
| मीट्रिक | ग्राम | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| मीट्रिक | टन (मीट्रिक) | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| मीट्रिक | कैरेट (मीट्रिक) | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| एवोर्डुपॉइस | ग्रेन | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| एवोर्डुपॉइस | ड्रैम | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| एवोर्डुपॉइस | औंस | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| एवोर्डुपॉइस | पाउंड | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| एवोर्डुपॉइस | स्टोन | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| एवोर्डुपॉइस | हंड्रेडवेट यूएस (शॉर्ट) | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| एवोर्डुपॉइस | हंड्रेडवेट यूके (लॉन्ग) | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| एवोर्डुपॉइस | शॉर्ट टन (यूएस) | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| एवोर्डुपॉइस | लॉन्ग टन (यूके) | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| एवोर्डुपॉइस | स्लग | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| ट्रॉय | पेनीवेट | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| ट्रॉय | ट्रॉय औंस | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| ट्रॉय | ट्रॉय पाउंड | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| ऐतिहासिक | जर्मन पाउंड | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| ऐतिहासिक | ज़ेंटनर | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| ऐतिहासिक | लॉट | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| क्षेत्रीय | जिन / कैटी | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| क्षेत्रीय | ल्यांग / टेल | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| क्षेत्रीय | मोम्मे | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| क्षेत्रीय | कान / कानमे | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| वैज्ञानिक | डाल्टन (u) | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

प्रत्येक `Token` एक मान-1 `KMassUnitInstance` है जो `of` (निर्माण) और `into` (पठन) के साथ प्रयुक्त होता
है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g (किलोग्राम `kilo.grams` है)
m.value                      // 2000.0 (ग्राम में प्रसामान्यीकृत)
m into pounds                // ≈ 4.409 (पाउंड में वापस पढ़ा)
(1 of pounds) into grams     // 453.59237
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : समान समूह, इकाइयों के बीच स्वचालित रूपांतरण
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// तुलनाएँ
(1 of kilo.grams) == (1000 of grams)         // true (समान प्रसामान्यीकृत मात्रा)
(1 of kilo.grams) > (500 of grams)           // true
```

### तुलनाएँ और समता

`==`, `!=`, `<`, `<=`, `>`, `>=` दो `KMassUnitInstance` मानों के प्रसामान्यीकृत `value` (ग्राम) की तुलना
करते हैं। `equals` प्रसामान्यीकृत मात्रा से होता है, इसलिए `(1 of kilo.grams) == (1000 of grams)`।

## `pow` से घात

infix `pow` संकारक से किसी मान को एक पूर्णांक घात तक उठाएँ (Kotlin में कोई अतिभारयोग्य `^` नहीं)।
द्रव्यमान समूह के लिए `pow` एक सामान्य `KMixedUnitInstance` लौटाता है (द्रव्यमान के पास कोई विमायुक्त घात
प्रकार नहीं):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## SI उपसर्ग

द्रव्यमान **कोई भी** परिमाण स्वीकारता है, इसलिए हर SI उपसर्ग बिल्डर (`quetta` … `quecto`) को हर द्रव्यमान
इकाई के साथ गुण पहुँच के माध्यम से जोड़ा जा सकता है। किलोग्राम ठीक `kilo.grams` है; मिलीग्राम
`milli.grams` है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     (किलोग्राम)
(1 of milli.grams).value   // 0.001      (मिलीग्राम)

(2500 of grams) into kilo.grams  // 2.5
```

## `toString` स्वरूपण

केवल मूल-इकाई `toString()` मौजूद है; किसी विशिष्ट इकाई को `into` के माध्यम से स्वरूपित करें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g" (मूल इकाई निरूपण)
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे
लिखा जाता है। घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `g` | `grams` | द्रव्यमान, मूल इकाई (ग्राम) |
| `kg` | `kilo.grams` | किलोग्राम (ग्राम पर लागू उपसर्ग) |
| `mg` | `milli.grams` | मिलीग्राम |
| `g²` | `grams pow 2` | ग्राम वर्ग (सामान्य मिश्रित इकाई) |
