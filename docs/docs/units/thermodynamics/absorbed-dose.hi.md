# अवशोषित मात्रा (ग्रे)

पैकेज: `org.pcsoft.framework.kunit.thermo.specificenergy`
आधार इकाई: **जूल प्रति किलोग्राम**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

प्रकार: **निर्मित इकाई (constructed unit)**

अवशोषित मात्रा `D` वह आयनकारी-विकिरण ऊर्जा है जो प्रति इकाई द्रव्यमान जमा होती है: `D = E / m`।
इसकी इकाई **ग्रे** है, और `1 Gy = 1 J/kg` — जो [विशिष्ट ऊर्जा](specific-energy.hi.md) के
**आयामिक रूप से समान** है।

## ग्रे का अपना प्रकार क्यों नहीं है

KUnit जानबूझकर अवशोषित मात्रा को एक अलग `KAbsorbedDoseUnitInstance` के बजाय
`KSpecificEnergyUnitInstance` से मॉडल करता है। इसका कारण इस लाइब्रेरी का रूप-पहचान अनुबंध है:

* हर मानकीकृत समूह का **एक** ही मानक आधार-आयाम सामान्य रूप होता है, और
* `toX()` ठीक उसी रूप को पहचानता है।

अवशोषित मात्रा और विशिष्ट ऊर्जा सामान्य रूप `length² · time⁻²` साझा करते हैं। एक सामान्य रूप पर दो
प्रकार होने से मूल अभिव्यक्ति अस्पष्ट हो जाएगी — `toSpecificEnergy()` और एक काल्पनिक
`toAbsorbedDose()` दोनों ही एक ही मिश्रित इकाई से मेल खाएंगे, और कोई भी उत्तर दूसरे से अधिक सही
नहीं होगा। एक ही प्रकार होने से राउंड-ट्रिप निर्धारक बना रहता है।

इसलिए यह भेद इस बात का है कि *आप अपने वेरिएबल को क्या नाम देते हैं*, न कि इस बात का कि लाइब्रेरी
आपको कौन-सा प्रकार सौंपती है — बिल्कुल भौतिकी की तरह, जहाँ ग्रे **है** जूल प्रति किलोग्राम।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## वास्तविक उदाहरण — एक छाती का एक्स-रे

एक छाती की एक्स-रे तस्वीर लगभग **0.1 mGy** जमा करती है। 70 किलोग्राम के व्यक्ति में यह कुल कितनी
ऊर्जा है, और यह प्राकृतिक पृष्ठभूमि के एक वर्ष (≈ 2.4 mGy) की तुलना में कैसी है?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## यह भी देखें

* [विशिष्ट ऊर्जा](specific-energy.hi.md) — वही प्रकार, ऊर्जा घनत्व के रूप में पढ़ा गया।
* [तुल्य मात्रा](dose-equivalent.hi.md) — सीवर्ट, जैविक प्रभाव के लिए भारित।
* [मात्रा दर](dose-rate.hi.md) — प्रति समय मात्रा, जिसका अपना प्रकार **है**।
* [एक्सपोज़र](exposure.hi.md) — आवेश-आधारित आयनीकरण मात्रा।
