# निरपेक्ष तापीय प्रतिरोध

पैकेज: `org.pcsoft.framework.kunit.thermo.resistance`
मूल इकाई: **वाट प्रति केल्विन** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`)

प्रकार: **संघटित इकाई**

किसी घटक का निरपेक्ष तापीय प्रतिरोध `R` वह तापमान अंतर है जो उससे प्रवाहित होने वाली प्रति इकाई ऊष्मा
पर बना रहता है: `R = ΔT / P`, जिसे `K/W` में मापा जाता है। यह **एक संपूर्ण वस्तु** को दर्शाता है — यह
हीट सिंक, यह ट्रांजिस्टर पैकेज, इस आकार की यह दीवार।

इसका विहित सामान्य रूप `mass⁻¹ · length⁻² · time³ · temperature` है।

!!! warning "यह तापीय प्रतिरोध (thermal insulance) जैसा नहीं है"
    इस समूह को [तापीय प्रतिरोध क्षमता](thermal-insulance.md) `m²·K/W` (R-मान) के साथ भ्रमित न करें,
    जो इसी विचार को **प्रति इकाई क्षेत्रफल** सामान्यीकृत करता है। दोनों क्षेत्रफल के एक गुणांक से भिन्न
    हैं, इनके अलग-अलग सामान्य रूप हैं और इसलिए अलग-अलग प्रकार हैं। संस्करण 0.8.0 तक (इसे शामिल करते
    हुए), नाम `thermo.resistance` / `KThermalResistanceUnit` उस तापीय प्रतिरोध क्षमता को संदर्भित
    करता था; अब यह इस समूह को संदर्भित करता है।

## नामित इकाइयाँ

| इकाई                       | संकेत       |                    टोकन | K/W में 1 इकाई |
|----------------------------|------------|------------------------:|--------------:|
| वाट प्रति केल्विन            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| वाट प्रति डिग्री सेल्सियस    | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| Btu प्रति घंटा·°F                | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

1 °C का तापमान **अंतर** 1 K के बराबर होता है, इसलिए सेमीकंडक्टर और हीट-सिंक डेटाशीट पर उपयोग की
जाने वाली वर्तनी `degreesCelsiusPerWatt` संख्यात्मक रूप से `kelvinsPerWatt` के बराबर है। सभी टोकन
पूर्ण SI उपसर्ग सीमा को स्वीकार करते हैं।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं। मूल रूप
**यूनिट टेम्पलेट्स** से जोड़ा गया है क्योंकि यह समूह द्रव्यमान का एक पद रखता है: कच्चा मिश्रित मान
ग्राम-आधारित गुणनफल है, जबकि टाइप किया गया इंस्टेंस अपने मान को नामित इकाई में संग्रहीत करता है।

| रूप             | व्यंजक                                                            |
|------------------|------------------------------------------------------------------------|
| टाइप किया गया संकारक   | `temperatureDifference / power`                                        |
| मूल (`toX()`) | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## समूह के साथ गणना

| व्यंजक                                | परिणाम प्रकार                            | अर्थ              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | उत्पन्न ऊष्मा प्रवाह |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | श्रेणी में प्रतिरोध |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

तापीय प्रतिरोध **श्रेणी में जुड़ते हैं** — यह ठीक वही है जो समूह का समान-प्रकार `+` संकारक करता है।

## वास्तविक उदाहरण — एक हीट-सिंक बजट

एक पावर ट्रांजिस्टर **12 W** व्यय करता है। तापीय श्रृंखला जंक्शन-टू-केस के लिए 0.5 K/W,
केस-टू-हीटसिंक के लिए 0.2 °C/W और हीटसिंक-टू-एयर के लिए 1.8 K/W है। जंक्शन परिवेश से कितना ऊपर है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // परिवेश से 30.0 K ऊपर

// 25 K की सीमा के लिए वह कितनी शक्ति व्यय कर सकता है?
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत K/W मान** की तुलना करते हैं, इसलिए
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`। `toString()` मान को मूल इकाई में प्रस्तुत
करता है: `"2.5 K/W"`।

## यह भी देखें

* [तापीय प्रतिरोध क्षमता](thermal-insulance.hi.md) — प्रति इकाई क्षेत्रफल में वही विचार (R-मान)।
* [तापीय चालकता](thermal-conductance.hi.md) — इसकी व्युत्क्रम राशि।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
