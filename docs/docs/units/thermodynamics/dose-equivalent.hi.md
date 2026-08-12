# तुल्य मात्रा (सीवर्ट)

पैकेज: `org.pcsoft.framework.kunit.thermo.specificenergy`
आधार इकाई: **जूल प्रति किलोग्राम**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

प्रकार: **निर्मित इकाई (constructed unit)**

तुल्य मात्रा `H`, [अवशोषित मात्रा](absorbed-dose.hi.md) को एक **आयामरहित** विकिरण भारण कारक
`w_R` से भारित करती है, जो यह बताता है कि किसी दिए गए विकिरण प्रकार से कितना नुकसान होता है:
`H = w_R · D`। इसकी इकाई **सीवर्ट** है, और चूँकि `w_R` आयामरहित है, `1 Sv = 1 J/kg` — जो ग्रे
के समान आयाम है।

## सीवर्ट का अपना प्रकार क्यों नहीं है

KUnit तुल्य मात्रा को ग्रे और विशिष्ट ऊर्जा के समान प्रकार `KSpecificEnergyUnitInstance` से
मॉडल करता है। इसका कारण इस लाइब्रेरी का रूप-पहचान अनुबंध है:

* हर मानकीकृत समूह का **एक** ही मानक आधार-आयाम सामान्य रूप होता है, और
* `toX()` ठीक उसी रूप को पहचानता है।

सीवर्ट, ग्रे और विशिष्ट ऊर्जा सभी सामान्य रूप `length² · time⁻²` साझा करते हैं। एक सामान्य रूप पर
कई प्रकार होने से मूल अभिव्यक्ति अस्पष्ट हो जाएगी, और कोई भी उत्तर दूसरे से अधिक सही नहीं होगा।
एक ही प्रकार होने से राउंड-ट्रिप निर्धारक बना रहता है।

!!! warning "भारण कारक लागू करना आपकी ज़िम्मेदारी है"
    चूँकि `w_R` आयामरहित है, KUnit ग्रे और सीवर्ट के बीच अंतर नहीं कर सकता। अवशोषित मात्रा को
    भारण कारक से गुणा करना एक साधारण अदिश गुणन है — लाइब्रेरी इसे आपके लिए नहीं करेगी, और यह
    आपको दोनों रीडिंग को मिलाने से नहीं रोकेगी। अपने मानों को तदनुसार नाम दें।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## वास्तविक उदाहरण — एक उड़ान और एक वर्ष की पृष्ठभूमि

प्राकृतिक पृष्ठभूमि लगभग **2.4 mSv प्रति वर्ष** है; एक ट्रांस-अटलांटिक उड़ान लगभग 0.05 mSv
जोड़ती है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## यह भी देखें

* [अवशोषित मात्रा](absorbed-dose.hi.md) — बिना भारित ग्रे।
* [विशिष्ट ऊर्जा](specific-energy.hi.md) — अंतर्निहित प्रकार।
* [मात्रा दर](dose-rate.hi.md) — प्रति समय मात्रा, जिसमें सीवर्ट की वर्तनियाँ शामिल हैं।
* [एक्सपोज़र](exposure.hi.md) — आवेश-आधारित आयनीकरण मात्रा।
