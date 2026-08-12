# एक्सपोज़र (आयनीकरण मात्रा)

पैकेज: `org.pcsoft.framework.kunit.electric.specificcharge`
आधार इकाई: **कूलॉम प्रति किलोग्राम**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

प्रकार: **निर्मित इकाई (constructed unit)**

एक्सपोज़र `X` — पारंपरिक **आयनीकरण मात्रा** — आयनकारी विकिरण को उस विद्युत आवेश से मापता है जो
यह प्रति इकाई वायु द्रव्यमान मुक्त करता है: `X = Q / m`, इकाई `C/kg` में। इसकी ऐतिहासिक इकाई
**रॉन्टजन** है (1 R = 2.58 × 10⁻⁴ C/kg)।

इसका आयाम `current · time · mass⁻¹` है — जो एक कण के [विशिष्ट आवेश](../electrical/specificcharge.hi.md)
के **समान** है। KUnit दोनों रीडिंग के लिए एक समूह मॉडल करता है; एक्सपोज़र उनमें से एक है। यह
पृष्ठ उस रीडिंग का दस्तावेज़ीकरण करता है।

## एक्सपोज़र का अपना प्रकार क्यों नहीं है

KUnit जानबूझकर एक्सपोज़र को एक अलग `KExposureUnitInstance` के बजाय `KSpecificChargeUnitInstance`
से मॉडल करता है। इसका कारण इस लाइब्रेरी का रूप-पहचान अनुबंध है:

* हर मानकीकृत समूह का **एक** ही मानक आधार-आयाम सामान्य रूप होता है, और
* `toX()` ठीक उसी रूप को पहचानता है।

एक्सपोज़र और विशिष्ट आवेश सामान्य रूप `current¹ · time¹ · mass⁻¹` साझा करते हैं। एक सामान्य रूप
पर दो प्रकार होने से मूल अभिव्यक्ति अस्पष्ट हो जाएगी — `toSpecificCharge()` और एक काल्पनिक
`toExposure()` दोनों ही एक ही मिश्रित इकाई से मेल खाएंगे, और कोई भी उत्तर दूसरे से अधिक सही नहीं
होगा। एक ही प्रकार होने से राउंड-ट्रिप निर्धारक बना रहता है।

इसलिए यह भेद इस बात का है कि *आप अपने वेरिएबल को क्या नाम देते हैं*, न कि इस बात का कि लाइब्रेरी
आपको कौन-सा प्रकार सौंपती है — बिल्कुल भौतिकी की तरह, जहाँ दोनों C/kg में लिखे जाते हैं।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## वास्तविक उदाहरण — एक पुरानी डोसीमीटर रीडिंग

एक पेन डोसीमीटर शिफ्ट के बाद **200 mR** दिखाता है। SI में और उस 1 kg वायु में मुक्त हुए आवेश में
परिवर्तित, जिसके विरुद्ध चैंबर कैलिब्रेट किया गया है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## यह भी देखें

* [विशिष्ट आवेश](../electrical/specificcharge.hi.md) — वही प्रकार, एक कण गुण के रूप में पढ़ा गया।
* [अवशोषित मात्रा](absorbed-dose.hi.md) और [तुल्य मात्रा](dose-equivalent.hi.md) — ऊर्जा-आधारित मात्राएँ।
* [मात्रा दर](dose-rate.hi.md) — प्रति समय मात्रा।
