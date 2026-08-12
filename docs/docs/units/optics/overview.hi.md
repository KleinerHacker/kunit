# प्रकाशिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.optic.luminousintensity`, `…luminousflux`, `…illuminance`,
`…luminance`, `…luminousenergy`, `…luminousexposure`, `…efficacy`, `…radiantintensity`, `…radiance`,
और `org.pcsoft.framework.kunit.common.reciprocallength`

प्रकाशिकी **प्रकाश** का विवरण है — कोई स्रोत कितना प्रकाश उत्सर्जित करता है, उसमें से कितना किसी सतह पर पहुँचता है, और
विद्युत शक्ति को कितनी दक्षता से प्रकाश में बदला जाता है। यह क्षेत्र **कैंडेला** पर आधारित है, जो सातवीं और अंतिम SI मूल
इकाई है, और एकमात्र मूल इकाई है जो मानवीय अनुभूति के आधार पर परिभाषित है: यह विकिरणित शक्ति को आँख की संवेदनशीलता से
भारित करती है।

यही कारण है कि इस क्षेत्र के दो समानांतर परिवार हैं। **प्रकाशमितीय** (photometric) राशियाँ (कैंडेला, ल्यूमेन, लक्स, निट)
प्रकाश को *जैसा आँख देखती है* वैसा वर्णित करती हैं; **विकिरणमितीय** (radiometric) राशियाँ (वाट प्रति स्टेरेडियन, वाट प्रति
स्टेरेडियन वर्ग मीटर) उसी विकिरण को *जैसा एक डिटेक्टर मापता है* वैसा वर्णित करती हैं, बिना आँख के भारण के। इन दोनों के
बीच का सेतु [ल्यूमिनस दक्षता](luminous-efficacy.md) है, जो 683 lm/W पर सीमित है।

## इस विषय की इकाइयाँ

| इकाई                | प्रकार  | मूल इकाई                               | पृष्ठ                                        |
|--------------------|-------|---------------------------------------|--------------------------------------------|
| ल्यूमिनस तीव्रता        | नेटिव  | कैंडेला (`cd`)                         | [ल्यूमिनस तीव्रता](luminous-intensity.md)     |
| ल्यूमिनस फ्लक्स         | संघटित | ल्यूमेन (`lm`)                          | [ल्यूमिनस फ्लक्स](luminous-flux.md)          |
| इल्यूमिनेंस           | संघटित | लक्स (`lx`)                            | [इल्यूमिनेंस](illuminance.md)                |
| ल्यूमिनेंस            | संघटित | कैंडेला प्रति वर्ग मीटर (`cd/m²`)          | [ल्यूमिनेंस](luminance.md)                   |
| ल्यूमिनस ऊर्जा         | संघटित | ल्यूमेन सेकंड (`lm·s`)                  | [ल्यूमिनस ऊर्जा](luminous-energy.md)         |
| ल्यूमिनस एक्सपोज़र      | संघटित | लक्स सेकंड (`lx·s`)                    | [ल्यूमिनस एक्सपोज़र](luminous-exposure.md)    |
| ल्यूमिनस दक्षता        | संघटित | ल्यूमेन प्रति वाट (`lm/W`)               | [ल्यूमिनस दक्षता](luminous-efficacy.md)      |
| विकिरण तीव्रता        | संघटित | वाट प्रति स्टेरेडियन (`W/sr`)             | [विकिरण तीव्रता](radiant-intensity.md)       |
| रेडिएंस             | संघटित | वाट प्रति स्टेरेडियन m² (`W/(sr·m²)`)   | [रेडिएंस](radiance.md)                       |
| अपवर्तक शक्ति         | संघटित | डायोप्टर (`dpt` = `m⁻¹`)                | [डायोप्टर](dioptre.md)                       |

ठोस कोण, जो तीव्रता राशियों को फ्लक्स राशियों से जोड़ता है, इस क्षेत्र का हिस्सा **नहीं** है — यह
[यांत्रिकी](../mechanics/solid-angle.md) विषय में रहता है और यहाँ ज्यों-का-त्यों पुनः उपयोग किया जाता है।

## राशियाँ आपस में कैसे संबंधित हैं

नीचे दिया गया हर संबंध सही **टाइप किया गया** मान लौटाता है; आप कभी भी कच्चे मिश्रित इकाई को हाथ से नहीं जोड़ते:

| अभिव्यक्ति                       | परिणाम           | सूत्र          |
|----------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | ल्यूमिनस फ्लक्स      | `Φ = I · Ω`    |
| `luminousFlux / area`            | इल्यूमिनेंस         | `E = Φ / A`    |
| `luminousIntensity / area`       | ल्यूमिनेंस          | `L = I / A`    |
| `illuminance / solidAngle`       | ल्यूमिनेंस          | `L = E / Ω`    |
| `luminousFlux * time`            | ल्यूमिनस ऊर्जा       | `Q = Φ · t`    |
| `illuminance * time`             | ल्यूमिनस एक्सपोज़र    | `H = E · t`    |
| `luminousFlux / power`           | ल्यूमिनस दक्षता      | `η = Φ / P`    |
| `power / solidAngle`             | विकिरण तीव्रता       | `Iₑ = P / Ω`   |
| `radiantIntensity / area`        | रेडिएंस             | `Lₑ = Iₑ / A`  |
| `1 / length`                     | अपवर्तक शक्ति        | `D = 1 / f`    |

## वास्तविक उदाहरण — क्या यह बल्ब मेरे डेस्क के लिए पर्याप्त उजला है?

एक LED बल्ब **7 W** पर **800 lm** रेटेड है। यह **2 m²** के डेस्क के ऊपर लटका है। कार्यालय के काम के लिए लगभग 500 lx
चाहिए। क्या यह पर्याप्त है, और बल्ब कितना दक्ष है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 — 500 lx लक्ष्य से थोड़ा कम

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 — भौतिक सीमा का 17 %
```

## वास्तविक उदाहरण — रीडिंग चश्मा

**40 cm** फोकल लंबाई वाले लेंस की अपवर्तक शक्ति `D = 1 / f` होती है। संपर्क में रखे दो पतले लेंस बस अपनी शक्तियाँ जोड़
देते हैं, जो ठीक वही है जो टाइप किए गए मान पर `+` करता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // संपर्क में लेंस जुड़ते हैं
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 — संयुक्त फोकल लंबाई
```

## संकेतन

नीचे दी गई तालिका इस क्षेत्र के मूल संबंधों को गणितीय रूप बनाम KUnit के साथ Kotlin में दर्शाती है। घातांक के लिए
Unicode ऊपरी अंक (`²`, `⁻¹`) प्रयोग किए गए हैं, `·` गुणन दर्शाता है और `/` भिन्न को।

| गणित          | Kotlin                                    | अर्थ                                  |
|---------------|--------------------------------------------|---------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | कोण से ल्यूमिनस फ्लक्स                    |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | फ्लक्स ÷ क्षेत्रफल से इल्यूमिनेंस           |
| `L = I / A`   | `(250 of candelas) / screen`              | तीव्रता ÷ क्षेत्रफल से ल्यूमिनेंस            |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | फ्लक्स × समय से ल्यूमिनस ऊर्जा             |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | इल्यूमिनेंस × समय से प्रकाश खुराक           |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | ल्यूमिनस दक्षता                         |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | विकिरण तीव्रता                         |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | फोकल लंबाई से अपवर्तक शक्ति                |

## आगे कहाँ जाएँ

* [ल्यूमिनस तीव्रता](luminous-intensity.md) — कैंडेला, इस क्षेत्र की नेटिव मूल राशि।
* [ल्यूमिनस फ्लक्स](luminous-flux.md) और [इल्यूमिनेंस](illuminance.md) — एक लैंप क्या उत्सर्जित करता है और एक सतह
  क्या प्राप्त करती है।
* [ल्यूमिनेंस](luminance.md) — वह राशि जिसे डिस्प्ले की "निट्स" रेटिंग संदर्भित करती है।
* [ल्यूमिनस दक्षता](luminous-efficacy.md) — प्रकाशमितीय और विकिरणमितीय परिवार के बीच का सेतु।
* [डायोप्टर](dioptre.md) — अपवर्तक शक्ति, और इसका स्पेक्ट्रोस्कोपिक जुड़वाँ [तरंग संख्या](../mechanics/wavenumber.md)।