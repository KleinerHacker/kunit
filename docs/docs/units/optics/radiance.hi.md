# रेडियंस (Radiance)

पैकेज: `org.pcsoft.framework.kunit.optic.radiance`
मूल इकाई: **वाट प्रति स्टेरेडियन वर्ग मीटर**
(`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`)

प्रकार: **संघटित इकाई (constructed unit)**

रेडियंस `Lₑ` **उत्सर्जक क्षेत्रफल प्रति** विकिरण तीव्रता है: `Lₑ = Iₑ / A`। यह [luminance](luminance.hi.md) का
**विकिरणमितीय (radiometric)** समकक्ष है, और वह राशि है जिसमें रिमोट सेंसिंग और थर्मल इमेजिंग कार्य करते हैं — जो एक
कैमरा पिक्सेल वास्तव में समाकलित करता है, सतह की दूरी से स्वतंत्र।

इसका विहित आधार-आयाम सामान्य रूप `mass¹ · time⁻³ · solidAngle⁻¹` है। दोनों दूरी घातांक रद्द हो जाते हैं: वाट
`distance²` और क्षेत्रफल `distance⁻²` का योगदान देता है।

## इकाइयाँ

| इकाई                              | Enum मान                                       | संकेत        |                            टोकन | 1 इकाई = ? W/(sr·m²) |
|------------------------------------|--------------------------------------------------|--------------|---------------------------------:|--------------------:|
| वाट प्रति स्टेरेडियन वर्ग मीटर      | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`   | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

यह टोकन प्रत्येक SI उपसर्ग स्वीकार करता है (`milli.wattsPerSteradianSquareMeter`, …)।

## अपघटन

समूह का एक अपघटन है, और इसके दोनों रूप समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं। मूल रूप **यूनिट टेम्पलेट्स** से
जोड़ा गया है क्योंकि समूह में एक द्रव्यमान (mass) पद है।

| रूप                | व्यंजक                                                       |
|---------------------|----------------------------------------------------------------|
| टाइप किया गया संकारक | `radiantIntensity / area`                                      |
| मूल (`toX()`)        | `(5 of kilo.grams / s³ / sr).toRadiance()`                     |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## समूह के साथ गणना

| व्यंजक                         | परिणाम प्रकार                     | अर्थ               |
|----------------------------------|-------------------------------------|--------------------|
| `radiantIntensity / area`        | `KRadianceUnitInstance`             | `Lₑ = Iₑ / A`      |
| `radiance * area`                | `KRadiantIntensityUnitInstance`     | `Iₑ = Lₑ · A`      |
| `radiantIntensity / radiance`    | `KAreaUnitInstance`                 | उत्सर्जक क्षेत्रफल |

## वास्तविक उदाहरण — एक थर्मल कैमरा पिक्सेल

एक भट्टी की दीवार **2 m²** की है और कैमरे की ओर **10 W/sr** विकिरण करती है। इसका रेडियंस — वह मान जो कैमरा दूरी की
परवाह किए बिना रिपोर्ट करता है — यह है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// उसी दीवार का 0.5 m² हिस्सा आनुपातिक रूप से कम तीव्रता उत्सर्जित करता है …
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 — लेकिन रेडियंस अपरिवर्तित रहता है
```

## मान अर्थ-विज्ञान (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत W/(sr·m²) मान** की तुलना करते हैं, इसलिए
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`। `toString()` मान को
मूल इकाई में प्रस्तुत करता है: `"5.0 W/(sr*m^2)"`।

## संबंधित देखें

* [विकिरण तीव्रता](radiant-intensity.hi.md) — अंश (numerator)।
* [चमकता (Luminance)](luminance.hi.md) — प्रकाशमितीय समकक्ष।
* [ऊष्मा प्रवाह घनत्व](../thermodynamics/heat-flux-density.hi.md) — गोलार्ध पर समाकलित रेडियंस।
* [प्रकाशिकी अवलोकन](overview.hi.md)
