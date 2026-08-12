# मात्रा दर

पैकेज: `org.pcsoft.framework.kunit.thermo.doserate`
आधार इकाई: **ग्रे प्रति सेकंड** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

प्रकार: **निर्मित इकाई (constructed unit)**

मात्रा दर वह विकिरण मात्रा है जो **प्रति समय** अवशोषित होती है: `Ḋ = D / t`। यही एक सर्वे मीटर
प्रदर्शित करता है — लगभग हमेशा माइक्रोसीवर्ट प्रति घंटे में — जबकि संचित मात्रा एक्सपोज़र समय पर
इंटीग्रल है।

इसका मानक आधार-आयाम सामान्य रूप `length² · time⁻³` है। ग्रे के `J/kg` का किलोग्राम जूल के
किलोग्राम से रद्द हो जाता है, यही कारण है कि कोई द्रव्यमान पद शेष नहीं रहता।

## नामित इकाइयाँ

| इकाई                | प्रतीक | टोकन                  | Gy/s में 1 इकाई |
|---------------------|--------|------------------------|----------------:|
| ग्रे प्रति सेकंड        | `Gy/s` | `graysPerSecond`      |            1.0 |
| ग्रे प्रति घंटा          | `Gy/h` | `graysPerHour`        |         1/3600 |
| सीवर्ट प्रति सेकंड      | `Sv/s` | `sievertsPerSecond`   |            1.0 |
| सीवर्ट प्रति घंटा        | `Sv/h` | `sievertsPerHour`     |         1/3600 |

ग्रे (अवशोषित मात्रा) और सीवर्ट (तुल्य मात्रा) एक ही आयाम साझा करते हैं, इसलिए KUnit दोनों के लिए
एक समूह मॉडल करता है — सीवर्ट की वर्तनियाँ इसलिए मौजूद हैं ताकि विकिरण-सुरक्षा रीडिंग सीधे लिखी
जा सकें। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं; `micro.sievertsPerHour` रोज़मर्रा का है।

!!! note "एक समूह, दो रीडिंग"
    ग्रे और सीवर्ट आयामरहित विकिरण भारण कारक से भिन्न होते हैं, न कि आयाम से। एक ही सामान्य रूप को
    एक ही प्रकार में मैप होना चाहिए (उसी तर्क के लिए [एन्ट्रॉपी](entropy.hi.md) देखें), इसलिए यह
    भेद इस बात का है कि आप अपने मान को क्या नाम देते हैं।

## विघटन

इस समूह का एक विघटन है, और इसके दोनों रूप एक ही टाइप वाला, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                  | अभिव्यक्ति                                                                    |
|----------------------|-------------------------------------------------------------------------------|
| टाइप किया ऑपरेटर      | `specificEnergy / time`                                                       |
| मूल (`toX()`)         | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`  |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## समूह के साथ गणना

| अभिव्यक्ति                     | परिणाम प्रकार                     | अर्थ                  |
|-----------------------------------|--------------------------------------|-----------------------|
| `specificEnergy / time`          | `KDoseRateUnitInstance`              | `Ḋ = D / t`           |
| `doseRate * time`                | `KSpecificEnergyUnitInstance`        | संचित मात्रा             |
| `specificEnergy / doseRate`      | `KTimeUnitInstance`                  | एक्सपोज़र समय            |

अवशोषित मात्रा स्वयं [विशिष्ट ऊर्जा](specific-energy.hi.md) समूह है — 1 Gy = 1 J/kg।

## वास्तविक उदाहरण — वार्षिक पृष्ठभूमि विकिरण

प्राकृतिक पृष्ठभूमि लगभग **0.274 µSv/h** है। एक वर्ष (8766 घंटे) में यह उस परिचित 2.4 mSv
तक संचित हो जाती है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## मान अर्थ विज्ञान

`equals`/`hashCode` **सामान्यीकृत Gy/s मान** की तुलना करते हैं, इसलिए
`(1 of graysPerHour) == (1 of sievertsPerHour)`। `toString()` आधार इकाई में मान प्रस्तुत
करता है: `"1.0 Gy/s"`।

## यह भी देखें

* [विशिष्ट ऊर्जा](specific-energy.hi.md) — स्वयं अवशोषित मात्रा (`Gy` = `J/kg`)।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
