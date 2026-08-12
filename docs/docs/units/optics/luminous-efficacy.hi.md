# दीप्त दक्षता (Luminous Efficacy)

पैकेज: `org.pcsoft.framework.kunit.optic.efficacy`
मूल इकाई: **लुमेन प्रति वाट** (`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`)

प्रकार: **संघटित इकाई**

दीप्त दक्षता `η` वह दीप्त फ्लक्स है जो एक लैंप **प्रति वाट विद्युत शक्ति** पर उत्पन्न करता है: `η = Φ / P`। यह वह एकल
संख्या है जो बताती है कि कोई प्रकाश स्रोत कितना अच्छा है, और यह प्रकाशमितीय (photometric) तथा विकिरणमितीय
(radiometric) परिवार के बीच का पुल है: यह वाट को, जिसे कोई संसूचक (detector) मापता है, लुमेन में बदलती है, जिसे आँख
अनुभव करती है।

इसका विहित आधार-आयाम सामान्य रूप है `luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³`।

## इकाइयाँ

| इकाई              | Enum मान                              | संकेत  |             टोकन | 1 इकाई = ? lm/W |
|------------------|-----------------------------------------|--------|-----------------:|----------------:|
| लुमेन प्रति वाट        | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |             1.0 |

टोकन हर SI उपसर्ग को स्वीकार करता है (`milli.lumensPerWatt`, `kilo.lumensPerWatt`, …)।

## स्थिरांक

| स्थिरांक                  | मान         | अर्थ                                                       |
|-------------------------|-------------|---------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | SI कैंडेला परिभाषा से 555 nm पर भौतिक ऊपरी सीमा |

कोई भी प्रकाश स्रोत 683 lm/W से अधिक नहीं हो सकता, क्योंकि यह प्रकाशिक ज्योति फलन (photopic luminosity function) के
शिखर पर एकवर्णी हरे प्रकाश की दक्षता है। हर वास्तविक लैंप उसका एक अंश ही है।

## अपघटन (Decomposition)

इस समूह का एक ही अपघटन है, और इसके दोनों रूप एक ही टाइप किया गया, मान-समान इंस्टेंस उत्पन्न करते हैं। ध्यान दें कि
मूल (native) रूप **यूनिट टेम्पलेट्स** से जोड़ा जाता है: द्रव्यमान पद रखने वाले समूह के लिए कच्चा मिश्रित मान
ग्राम-आधारित गुणनफल होता है, जबकि टाइप किया गया इंस्टेंस अपना मान नामित इकाई में संग्रहीत करता है।

| रूप               | व्यंजक                                                                           |
|------------------|-----------------------------------------------------------------------------------|
| टाइप किया गया संकारक  | `luminousFlux / power`                                                            |
| मूल (`toX()`)     | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## समूह के साथ गणना

| व्यंजक                                | परिणाम प्रकार                     | अर्थ                |
|-------------------------------------|---------------------------------|------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | आवश्यक शक्ति     |

## वास्तविक उदाहरण — तीन बल्बों की तुलना

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 — भौतिक ऊपरी सीमा का 17 %

// 3000 lm के लिए एक LED स्ट्रिप को कितनी शक्ति चाहिए?
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## मान अर्थविज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत lm/W मान** की तुलना करते हैं, इसलिए
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`। `toString()` मान को मूल इकाई में प्रस्तुत करता है:
`"120.0 lm/W"`।

## यह भी देखें

* [दीप्त फ्लक्स](luminous-flux.md) — अंश (numerator)।
* [विकिरण तीव्रता](radiant-intensity.md) और [विकिरण (Radiance)](radiance.md) — पुल का विकिरणमितीय पक्ष।
* [शक्ति (विद्युत)](../electrical/power.md) — हर (denominator)।
* [प्रकाशिकी अवलोकन](overview.md)
