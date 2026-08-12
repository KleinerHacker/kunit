# इलास्टेंस

पैकेज: `org.pcsoft.framework.kunit.electric.elastance`
आधार इकाई: **रेसिप्रोकल फैरड** (`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

प्रकार: **संरचित इकाई**

इलास्टेंस `S = U / Q = 1 / C`, [धारिता](capacitance.hi.md) का ठीक व्युत्क्रम है। जब संधारित्र **श्रृंखला** में हों तो
यह सुविधाजनक रूप है: श्रृंखला इलास्टेंस केवल जुड़ जाते हैं, ठीक जैसे श्रृंखला प्रतिरोध जुड़ते हैं। इसकी इकाई, रेसिप्रोकल फैरड,
पारंपरिक रूप से **daraf** कहलाती है — "farad" को उल्टा लिखने से बना नाम।

इसका विहित आधार-आयाम मानक रूप `mass · length² · time⁻⁴ · current⁻²` है।

## नामित इकाइयाँ

| इकाई              | प्रतीक  |              टोकन | 1 इकाई F⁻¹ में |
|-------------------|---------|-------------------:|--------------:|
| रेसिप्रोकल फैरड   | `1/F`   | `reciprocalFarads` |           1.0 |
| daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` आधार इकाई की दूसरी वर्तनी है, अपने आप में कोई अलग इकाई नहीं। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं
(`mega.reciprocalFarads`, …)।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही मान-समान टाइप्ड इंस्टेंस बनाते हैं। मूल रूप **यूनिट टेम्पलेट्स**
से जोड़ा जाता है क्योंकि समूह में द्रव्यमान पद है।

| रूप              | व्यंजक                                                    |
|------------------|----------------------------------------------------------------|
| टाइप्ड ऑपरेटर    | `voltage / charge`                                            |
| मूल (`toX()`)    | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## समूह के साथ गणना

| व्यंजक                 | परिणाम प्रकार                     | अर्थ                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | संग्रहीत आवेश          |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | श्रृंखला में संधारित्र |

## वास्तविक उदाहरण — श्रृंखला में दो संधारित्र

श्रृंखला में जुड़े दो 1 mF संधारित्र एक 0.5 mF संधारित्र जैसा व्यवहार करते हैं। इलास्टेंस के संदर्भ में यह केवल जोड़ है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — समतुल्य धारिता
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत F⁻¹ मान** की तुलना करते हैं, इसलिए `(1 of reciprocalFarads) == (1 of darafs)` सत्य है।
`toString()` मान को आधार इकाई में दर्शाता है: `"1000.0 1/F"`।

## यह भी देखें

* [धारिता](capacitance.hi.md) — व्युत्क्रम राशि।
* [वोल्टेज](voltage.hi.md) और [आवेश](charge.hi.md) — अपघटन के दो संकारक।
* [इलेक्ट्रिकल इंजीनियरिंग अवलोकन](overview.hi.md)
