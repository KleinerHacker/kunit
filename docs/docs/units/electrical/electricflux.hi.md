# विद्युत फ्लक्स

पैकेज: `org.pcsoft.framework.kunit.electric.flux`
आधार इकाई: **वोल्ट मीटर** (`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

प्रकार: **संरचित इकाई**

विद्युत फ्लक्स `Φ_E`, विद्युत क्षेत्र तीव्रता का क्षेत्रफल पर समाकलन है: `Φ_E = E · A`। यह वही राशि है जिसमें गॉस का नियम
लिखा जाता है — किसी बंद सतह से गुजरने वाला फ्लक्स, आवृत आवेश को विद्युतशीलता से विभाजित करने पर बराबर होता है।

इसका विहित आधार-आयाम मानक रूप `mass · length³ · time⁻³ · current⁻¹` है।

!!! note "यह विद्युत फ्लक्स घनत्व नहीं है"
    [विद्युत फ्लक्स घनत्व](electricfluxdensity.hi.md) `D` (`C/m²`) अलग आयाम वाली एक अलग राशि है। यह पृष्ठ फ्लक्स के
    बारे में है, जो `V·m` में मापा जाता है।

## नामित इकाइयाँ

| इकाई               | प्रतीक  |             टोकन | 1 इकाई V·m में |
|-----------------|---------|------------------:|--------------:|
| वोल्ट मीटर      | `V*m`   |      `voltMeters` |           1.0 |
| वोल्ट सेंटीमीटर | `V*cm`  | `voltCentimeters` |          0.01 |

सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`kilo.voltMeters`, …)।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही मान-समान टाइप्ड इंस्टेंस बनाते हैं। मूल रूप **यूनिट टेम्पलेट्स**
से जोड़ा जाता है क्योंकि समूह में द्रव्यमान पद है: कच्चा मिश्रित मान ग्राम-आधारित गुणनफल है, जबकि टाइप्ड इंस्टेंस अपना
मान नामित इकाई में संग्रहीत करता है।

| रूप              | व्यंजक                                                     |
|------------------|-----------------------------------------------------------------|
| टाइप्ड ऑपरेटर    | `electricFieldStrength * area`                                 |
| मूल (`toX()`)    | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## समूह के साथ गणना

| व्यंजक                              | परिणाम प्रकार                            | अर्थ        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | क्षेत्रफल       |

## वास्तविक उदाहरण — संधारित्र प्लेट से गुजरता फ्लक्स

**1000 V/m** का क्षेत्र 0.5 m × 0.25 m की प्लेट से गुजरता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// उस प्लेट पर दिए गए फ्लक्स द्वारा निहित क्षेत्र
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत V·m मान** की तुलना करते हैं, इसलिए `(1 of voltMeters) == (100 of voltCentimeters)` सत्य है।
`toString()` मान को आधार इकाई में दर्शाता है: `"125.0 V*m"`।

## यह भी देखें

* [विद्युत क्षेत्र तीव्रता](electricfieldstrength.hi.md) — जिसका समाकलन किया जा रहा है।
* [विद्युत फ्लक्स घनत्व](electricfluxdensity.hi.md) — अलग आयाम वाला `D` क्षेत्र।
* [इलेक्ट्रिकल इंजीनियरिंग अवलोकन](overview.hi.md)
