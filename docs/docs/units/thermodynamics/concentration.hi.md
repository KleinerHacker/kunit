# पदार्थ-की-मात्रा सांद्रता (मोलरता)

पैकेज: `org.pcsoft.framework.kunit.thermo.concentration`
आधार इकाई: **मोल प्रति घन मीटर** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

प्रकार: **निर्मित इकाई (constructed unit)**

पदार्थ-की-मात्रा सांद्रता `c` यह दर्शाती है कि **विलयन के आयतन प्रति** कितना पदार्थ घुला हुआ है:
`c = n / V`। रसायन विज्ञान इसे लगभग हमेशा मोल प्रति लीटर में व्यक्त करता है और इसे **मोलरता**
कहता है, जिसे `M` लिखा जाता है; नैदानिक प्रयोगशालाएँ मिलीमोल प्रति लीटर का उपयोग करती हैं।

इसका मानक आधार-आयाम सामान्य रूप `substance¹ · length⁻³` है।

## नामित इकाइयाँ

| इकाई                    | प्रतीक    |                 टोकन | mol/m³ में 1 इकाई |
|-------------------------|-----------|------------------------:|-----------------:|
| मोल प्रति घन मीटर          | `mol/m^3` |    `molesPerCubicMeter` |              1.0 |
| मोल प्रति लीटर (मोलर)      | `mol/l`   |         `molesPerLiter` |             1000 |
| मोलर (`M`)                | `mol/l`   |                 `molar` |             1000 |
| मिलीमोल प्रति लीटर         | `mmol/l`  |    `millimolesPerLiter` |              1.0 |

`molar`, `molesPerLiter` की एक और वर्तनी है, न कि अपनी अलग इकाई। ध्यान दें कि मिलीमोल प्रति
लीटर संख्यात्मक रूप से मोल प्रति घन मीटर के समान है — SI आधार इकाई संख्यात्मक रूप से बिल्कुल
नैदानिक इकाई ही है। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`milli.molesPerLiter`,
`micro.molar`, ...)।

## विघटन

इस समूह का एक विघटन है, और इसके दोनों रूप एक ही टाइप वाला, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                  | अभिव्यक्ति                                                                |
|----------------------|--------------------------------------------------------------------------|
| टाइप किया ऑपरेटर      | `amountOfSubstance / volume`                                             |
| मूल (`toX()`)         | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## समूह के साथ गणना

| अभिव्यक्ति                                | परिणाम प्रकार                     | अर्थ                     |
|-----------------------------------------------|-------------------------------------|-----------------------------|
| `amountOfSubstance / volume`                 | `KConcentrationUnitInstance`        | `c = n / V`                 |
| `concentration * volume`                     | `KAmountOfSubstanceUnitInstance`    | `n = c · V`                 |
| `amountOfSubstance / concentration`          | `KVolumeUnitInstance`               | आवश्यक आयतन                 |
| `conductivity / concentration`               | `KMolarConductivityUnitInstance`    | `Λ = κ / c`                 |

## वास्तविक उदाहरण — रक्त शर्करा

लगभग 5 लीटर रक्त में उपवास रक्त शर्करा **5.5 mmol/l** निम्न के अनुरूप है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## मान अर्थ विज्ञान

`equals`/`hashCode` **सामान्यीकृत mol/m³ मान** की तुलना करते हैं, इसलिए
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`। `toString()` आधार इकाई में मान
प्रस्तुत करता है: `"1000.0 mol/m^3"`।

## यह भी देखें

* [मोलैलिटी](molality.hi.md) — वही विचार विलायक के **द्रव्यमान** के प्रति, ऊष्मीय प्रसार से स्वतंत्र।
* [पदार्थ की मात्रा](amount-of-substance.hi.md) — अंश।
* [मोलर आयतन](molar-volume.hi.md) — शुद्ध पदार्थ के लिए व्युत्क्रम राशि।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
