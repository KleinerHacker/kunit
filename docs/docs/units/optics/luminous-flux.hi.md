# दीप्त फ्लक्स (Luminous Flux)

पैकेज: `org.pcsoft.framework.kunit.optic.luminousflux`
मूल इकाई: **लुमेन** (`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`)

प्रकार: **संघटित इकाई**

दीप्त फ्लक्स `Φ` किसी स्रोत द्वारा उन सभी दिशाओं में उत्सर्जित **दृश्य प्रकाश की कुल मात्रा** है जिन्हें वह कवर करता
है — वह संख्या जो हर लैंप के पैकेज पर छपी होती है। यह ठोस कोण पर समाकलित (integrated) दीप्त तीव्रता है: `Φ = I · Ω`,
इसलिए `1 lm = 1 cd·sr`।

इसका विहित आधार-आयाम सामान्य रूप है `luminousIntensity¹ · solidAngle¹`।

## इकाइयाँ

| इकाई               | Enum मान                            | संकेत   |               टोकन | 1 इकाई = ? लुमेन |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| लुमेन              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| कैंडेला स्टेरेडियन  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians` लुमेन की परिभाषा को खुले रूप में लिखा गया रूप है — संख्यात्मक रूप से समान, लेकिन यह किसी सूत्र को
यह बताने देता है कि इकाई कहाँ से आती है। दोनों टोकन हर SI उपसर्ग को स्वीकार करते हैं (`kilo.lumens`, `milli.lumens`, …)।

## अपघटन (Decomposition)

इस समूह का एक ही अपघटन है, और इसके दोनों रूप एक ही टाइप किया गया, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                | व्यंजक                                                       |
|---------------------|------------------------------------------------------------------|
| टाइप किया गया संकारक      | `luminousIntensity * solidAngle`                                  |
| मूल (`toX()`)      | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## समूह के साथ गणना

| व्यंजक                           | परिणाम प्रकार                     | अर्थ                       |
|------------------------------------|-----------------------------------|--------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | वह शंकु जिस पर फ्लक्स फैलता है |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## वास्तविक उदाहरण — एक सर्वदिश (isotropic) बल्ब

एक नंगा बल्ब सभी दिशाओं में समान रूप से विकिरण करता है। पूरा गोला `4π sr` होता है, इसलिए एक 100 cd स्रोत उत्सर्जित
करता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm — लगभग एक 100 W गरमागरम (incandescent) बल्ब के बराबर
```

## मान अर्थविज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत लुमेन मान** की तुलना करते हैं, इसलिए `(1 of lumens) == (1000 of milli.lumens)`।
`toString()` मान को मूल इकाई में प्रस्तुत करता है: `"800.0 lm"`।

## यह भी देखें

* [दीप्त तीव्रता](luminous-intensity.md) — ठोस कोण प्रति फ्लक्स।
* [प्रदीप्ति (Illuminance)](illuminance.md) — प्रकाशित क्षेत्रफल प्रति फ्लक्स।
* [दीप्त दक्षता](luminous-efficacy.md) — विद्युत शक्ति के प्रति वाट फ्लक्स।
* [प्रकाशिकी अवलोकन](overview.md)
