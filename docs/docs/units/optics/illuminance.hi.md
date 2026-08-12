# इल्यूमिनेंस

पैकेज: `org.pcsoft.framework.kunit.optic.illuminance`
मूल इकाई: **लक्स** (`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`)

प्रकार: **संघटित इकाई**

इल्यूमिनेंस `E` वह ल्यूमिनस फ्लक्स है जो किसी **सतह पर पहुँचता है**, उस सतह की प्रति इकाई क्षेत्रफल में: `E = Φ / A`, अतः
`1 lx = 1 lm/m²`। यह वह राशि है जिसमें हर कार्यस्थल प्रकाश-मानक लिखा जाता है — और, ल्यूमिनस फ्लक्स के विपरीत, यह इस पर
निर्भर करता है कि लैंप कितनी दूर है और प्रकाशित क्षेत्र कितना बड़ा है, न कि केवल लैंप पर।

इसका विहित आधार-आयाम सामान्य रूप `luminousIntensity¹ · solidAngle¹ · distance⁻²` है।

## इकाइयाँ

| इकाई          | Enum मान                        | प्रतीक |          टोकन | लक्स में 1 इकाई |
|----------------|----------------------------------|--------|---------------:|-----------------:|
| लक्स            | `KIlluminanceUnit.LUX`           | `lx`   |           `lux` |               1.0 |
| फोट             | `KIlluminanceUnit.PHOT`          | `ph`   |         `phots` |            10 000 |
| फुट-कैंडल        | `KIlluminanceUnit.FOOT_CANDLE`   | `fc`   |   `footCandles` |        ≈ 10.76391 |
| नॉक्स            | `KIlluminanceUnit.NOX`           | `nx`   |           `nox` |             0.001 |

फोट CGS इकाई है (1 lm/cm²), फुट-कैंडल इंपीरियल इकाई है (1 lm/ft²), और नॉक्स बहुत कम प्रकाश स्तरों जैसे चाँदनी के लिए
प्रयुक्त होता है। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`kilo.lux`, `milli.lux`, …)।

## अपघटन

समूह का एक अपघटन है, और इसके दोनों रूप एक ही टाइप किया गया, मान-समतुल्य उदाहरण उत्पन्न करते हैं:

| रूप               | अभिव्यक्ति                                                             |
|--------------------|--------------------------------------------------------------------------|
| टाइप किया गया संकारक | `luminousFlux / area`                                                    |
| नेटिव (`toX()`)     | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## समूह के साथ गणना

| अभिव्यक्ति                     | परिणाम प्रकार                     | अर्थ                        |
|-----------------------------------|------------------------------------|------------------------------|
| `luminousFlux / area`             | `KIlluminanceUnitInstance`         | `E = Φ / A`                 |
| `illuminance * area`              | `KLuminousFluxUnitInstance`        | `Φ = E · A`                 |
| `luminousFlux / illuminance`      | `KAreaUnitInstance`                | वह क्षेत्रफल जिसे एक फ्लक्स प्रकाशित कर सकता है |
| `illuminance / solidAngle`        | `KLuminanceUnitInstance`           | `L = E / Ω`                 |
| `illuminance * time`              | `KLuminousExposureUnitInstance`    | `H = E · t`                 |

## वास्तविक उदाहरण — क्या मेरा डेस्क पर्याप्त उजला है?

कार्यालय के काम के लिए लगभग **500 lx** चाहिए। 2 m² के डेस्क के ऊपर एक 800 lm बल्ब यह देता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 — 500 lx लक्ष्य से कम
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // 1000.0 lm आवश्यक होगा
```

## मान अर्थ-विज्ञान

`equals`/`hashCode` **सामान्यीकृत लक्स मान** की तुलना करते हैं, इसलिए `(1 of phots) == (10000 of lux)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"500.0 lx"`।

## यह भी देखें

* [ल्यूमिनस फ्लक्स](luminous-flux.md) — लैंप क्या उत्सर्जित करता है।
* [ल्यूमिनेंस](luminance.hi.md) — प्रति ठोस कोण इल्यूमिनेंस, किसी सतह की "चमक"।
* [ल्यूमिनस एक्सपोज़र](luminous-exposure.md) — समय के साथ संचित इल्यूमिनेंस।
* [प्रकाशिकी अवलोकन](overview.hi.md)
