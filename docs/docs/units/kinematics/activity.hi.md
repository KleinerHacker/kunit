# रेडियोधर्मी सक्रियता (बेक्केरेल)

पैकेज: `org.pcsoft.framework.kunit.kinematic.frequency`
आधार इकाई: **हर्ट्ज़**(`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

प्रकार: **मूल इकाई**

किसी रेडियोधर्मी नमूने की सक्रियता `A` प्रति सेकंड होने वाले नाभिकीय क्षय की संख्या है। इसकी इकाई
**बेक्केरेल** है, और `1 Bq = 1 s⁻¹` — [आवृत्ति](frequency.hi.md) के **आयामी रूप से समान** है।

## बेक्केरेल का अपना अलग प्रकार क्यों नहीं है

KUnit जानबूझकर सक्रियता को अलग `KActivityUnitInstance` के बजाय `KFrequencyUnitInstance` से मॉडल करता है।
इसका कारण इस लाइब्रेरी का फ़ॉर्म-पहचान अनुबंध है:

* प्रत्येक मानकीकृत समूह का **एक** ही विहित आधार-आयाम सामान्य रूप होता है, और
* `toX()` ठीक उसी रूप को पहचानता है।

सक्रियता और आवृत्ति सामान्य रूप `time⁻¹` साझा करते हैं। एक सामान्य रूप के लिए दो प्रकार होने से मूल
अभिव्यक्ति अस्पष्ट हो जाएगी — `toFrequency()` और एक काल्पनिक `toActivity()` दोनों उसी मिश्रित इकाई से मेल
खाएँगे, और कोई भी उत्तर दूसरे से अधिक सही नहीं होगा। एक ही प्रकार राउंड-ट्रिप को निर्धारक बनाए रखता है।

अंतर इस बात का है कि *आप अपने वेरिएबल का नाम क्या रखते हैं*: आवृत्ति आवधिक चक्रों को गिनती है, सक्रियता
यादृच्छिक क्षय को गिनती है, लेकिन दोनों "प्रति सेकंड घटनाएँ" ही हैं।

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // 37 GBq के रूप में पढ़ा गया — एक ग्राम रेडियम
activity into mega.hertz             // 37 000.0

// एक मिनट में क्षय
val decays = activity * (60 of seconds)   // बिना इकाई की गिनती
decays                                     // 2.22e12
```

!!! note "क्यूरी"
    ऐतिहासिक इकाई क्यूरी है, 1 Ci = 3.7 × 10¹⁰ Bq। इसका अपना कोई टोकन नहीं है; इसे
    `37 of giga.hertz` लिखें या अपना खुद का स्थिरांक बनाएँ।

## वास्तविक उदाहरण — एक धुआँ संसूचक स्रोत

एक घरेलू धुआँ संसूचक में लगभग **30 kBq** अमेरिशियम-241 होता है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// एक दिन में क्षय
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## यह भी देखें

* [आवृत्ति](frequency.hi.md) — वही प्रकार, आवधिक दर के रूप में पढ़ा गया।
* [खुराक दर](../thermodynamics/dose-rate.hi.md) — कोई स्रोत प्रति समय जो खुराक देता है।
* [अवशोषित खुराक](../thermodynamics/absorbed-dose.hi.md) — ऊर्जा-आधारित खुराक।
