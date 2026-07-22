# विद्युत अभियांत्रिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`

विद्युत अभियांत्रिकी परिपथ में बहने वाली धारा, उसे चलाने वाली वोल्टता, और उसका विरोध करने वाले प्रतिरोध
को एक साथ जोड़ती है। ये तीनों **ओम के नियम** से बँधे हैं, और KUnit इस नियम को सीधे प्रकार-युक्त `*` और `/`
संकारकों के रूप में व्यक्त करता है: एक **नेटिव** मूल राशि (विद्युत धारा) और मूल विमाओं से **निर्मित** दो
राशियाँ (वोल्टता और प्रतिरोध)।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | मूल इकाई | पृष्ठ |
|---|---|---|---|
| विद्युत धारा | नेटिव | ऐम्पियर (`A`) | [विद्युत धारा](ec.md) |
| वोल्टता | निर्मित | वोल्ट (`V`) | [वोल्टता](voltage.md) |
| प्रतिरोध | निर्मित | ओम (`Ω`) | [प्रतिरोध](resistance.md) |

## प्रकार-युक्त संकारकों के रूप में ओम का नियम

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `resistance * current` | वोल्टता | `U = R · I` |
| `current * resistance` | वोल्टता | `U = R · I` (क्रमविनिमेय) |
| `voltage / current` | प्रतिरोध | `R = U / I` |
| `voltage / resistance` | विद्युत धारा | `I = U / R` |

प्रत्येक परिणाम सही प्रकार-युक्त राशि है — कोई कच्ची मिश्रित इकाई हाथ से नहीं बनाई जाती। इसके अलावा वोल्टता
और प्रतिरोध अपने पूर्ण **नेटिव** अपघटन (`kg·m²·s⁻³·A⁻¹` और `kg·m²·s⁻³·A⁻²`) को `toVoltage()` /
`toResistance()` के माध्यम से पहचानते हैं।

## वास्तविक उदाहरण — एक परिपथ के इर्द-गिर्द ओम का नियम

एक भार **2 A** खींचते हुए **230 V** का पात दिखाता है। इसका प्रतिरोध `R = U / I` है; उस प्रतिरोध को धारा
के साथ वापस डालने पर वोल्टता `U = R · I` पुनः प्राप्त होती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance, 115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (मान + प्रतीक) में प्रस्तुत करता है; किसी अन्य इकाई के
लिए, इसे स्ट्रिंग टेम्पलेट में `into` से पढ़ें और प्रतीक स्वयं जोड़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V" (मूल इकाई)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## संकेतन

नीचे दी गई तालिका ओम के नियम को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। घातांक Unicode उपरिलेख
(`²`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | वोल्टता ÷ धारा से प्रतिरोध |
| `U = R · I` | `r * (2 of amperes)` | प्रतिरोध × धारा से वोल्टता |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | वोल्टता ÷ प्रतिरोध से धारा |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | अपने नेटिव मानक रूप में प्रतिरोध |

## आगे कहाँ जाएँ

* [विद्युत धारा](ec.md) — नेटिव ऐम्पियर समूह (साथ ही CGS का बायो और स्टैट-ऐम्पियर)।
* [वोल्टता](voltage.md) — वोल्ट, और इसके अपघटन `R · I` तथा नेटिव रूप।
* [प्रतिरोध](resistance.md) — ओम, `U / I`, और व्युत्क्रम ओम-नियम संकारक।
