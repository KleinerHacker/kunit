# संपीड्यता (Compressibility)

पैकेज: `org.pcsoft.framework.kunit.mechanic.compressibility`
मूल इकाई: **पास्कल का व्युत्क्रम**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

प्रकार: **संरचित इकाई**

संपीड्यता `κ = −(1/V)·(∂V/∂p)` यह बताती है कि दाब की प्रति इकाई किसी पदार्थ का आयतन कितना
सिकुड़ता है। यह **आयतन मापांक (bulk modulus)** `K` का ठीक व्युत्क्रम है, जो एक प्रत्यास्थता
मापांक है और इसलिए एक [दाब](pressure.hi.md) है। पानी की संपीड्यता लगभग 4.5 × 10⁻¹⁰ Pa⁻¹ है —
यही कारण है कि हाइड्रॉलिक्स में इसे असंपीड्य माना जा सकता है।

इसका विहित आधार-आयाम मानक रूप `mass⁻¹ · length · time²` है।

## नामित इकाइयाँ

| इकाई                             | प्रतीक  |                   टोकन | 1/Pa में 1 इकाई |
|-------------------------------------|---------|------------------------:|---------------:|
| पास्कल का व्युत्क्रम                  | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| बार का व्युत्क्रम                    | `1/bar` |        `reciprocalBars` |           1e-5 |
| मानक वायुमंडलीय दाब का व्युत्क्रम      | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं (`pico.reciprocalPascals` आदि)। पड़ोसी दाब समूह की तरह,
यह इंस्टेंस अपना **कच्चा ग्राम-आधारित घटक मान** संग्रहीत करता है।

## समूह के साथ गणना

| अभिव्यक्ति                      | परिणाम प्रकार                       | अर्थ                            |
|------------------------------------|-------------------------------------------|--------------------------------------|
| `1 / pressure`                    | `KCompressibilityUnitInstance`           | `κ = 1 / K`                          |
| `1 / compressibility`             | `KPressureUnitInstance`                  | `K = 1 / κ`                          |
| `compressibility * pressure`      | `Double`                                 | सापेक्ष आयतन परिवर्तन `ΔV/V`          |

ये दोनों व्युत्क्रम सटीक हैं: घटकों के मूल आधार (दाब के लिए `g·m⁻¹·s⁻²`, यहाँ `g⁻¹·m·s²`) एक-दूसरे
के व्युत्क्रम हैं, इसलिए किसी पुल गुणांक की आवश्यकता नहीं होती।

## वास्तविक उदाहरण — पानी कितना संपीड़ित होता है

पानी का आयतन मापांक लगभग **2.2 GPa** है। इसकी संपीड्यता क्या है, और 10 MPa (लगभग 1000 मीटर
पानी की गहराई के बराबर) पर यह कितना सिकुड़ता है?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 — 0.45% आयतन हानि

// और वापस आयतन मापांक में
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## मान अर्थशास्त्र (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत घटक मान** की तुलना करते हैं, इसलिए
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`। `toString()` मूल इकाई में मान
प्रदर्शित करता है: `"1.0 1/Pa"`।

## यह भी देखें

* [दाब](pressure.hi.md) — व्युत्क्रम राशि (आयतन मापांक)।
* [प्रतिबल और प्रत्यास्थता मापांक](stress.hi.md) — पदार्थ गुण के रूप में पढ़ा गया वही प्रकार।
* [यांत्रिकी अवलोकन](overview.hi.md)
