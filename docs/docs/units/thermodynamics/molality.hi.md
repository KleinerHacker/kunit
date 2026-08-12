# मोलैलिटी

पैकेज: `org.pcsoft.framework.kunit.thermo.molality`
आधार इकाई: **मोल प्रति किलोग्राम** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

प्रकार: **निर्मित इकाई (constructed unit)**

मोलैलिटी `b` यह दर्शाती है कि **विलायक के द्रव्यमान प्रति** कितना पदार्थ घुला हुआ है:
`b = n / m`। आयतन को संदर्भित करने वाली [सांद्रता](concentration.hi.md) के विपरीत, विलयन
गर्म करने पर मोलैलिटी नहीं बदलती — विलायक का द्रव्यमान ऊष्मीय प्रसार से अप्रभावित रहता है। यह
इसे हिमांक अवनमन और क्वथनांक उन्नयन जैसे संयोजी गुणों के लिए पसंदीदा राशि बनाता है।

इसका मानक आधार-आयाम सामान्य रूप `substance¹ · mass⁻¹` है।

## नामित इकाइयाँ

| इकाई                      | प्रतीक    |                    टोकन | mol/kg में 1 इकाई |
|---------------------------|-----------|-------------------------:|-----------------:|
| मोल प्रति किलोग्राम           | `mol/kg`  |       `molesPerKilogram` |              1.0 |
| मिलीमोल प्रति किलोग्राम        | `mmol/kg` | `millimolesPerKilogram`  |            0.001 |

सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`milli.molesPerKilogram`, ...)।

## विघटन

इस समूह का एक विघटन है, और इसके दोनों रूप एक ही टाइप वाला, मान-समान इंस्टेंस उत्पन्न करते हैं।
ध्यान दें कि मूल रूप **यूनिट टेम्पलेट्स** से संयोजित होता है: द्रव्यमान पद वाले समूह के लिए, कच्चा
मिश्रित मान ग्राम-आधारित गुणनफल है, जबकि एक टाइप किया इंस्टेंस अपना मान नामित इकाई में संग्रहीत
करता है।

| रूप                  | अभिव्यक्ति                                              |
|----------------------|--------------------------------------------------------|
| टाइप किया ऑपरेटर      | `amountOfSubstance / mass`                              |
| मूल (`toX()`)         | `(0.25 of moles / kilo.grams).toMolality()`             |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## समूह के साथ गणना

| अभिव्यक्ति                        | परिणाम प्रकार                     | अर्थ                          |
|---------------------------------------|--------------------------------------|--------------------------------|
| `amountOfSubstance / mass`           | `KMolalityUnitInstance`              | `b = n / m`                    |
| `molality * mass`                    | `KAmountOfSubstanceUnitInstance`     | `n = b · m`                    |
| `amountOfSubstance / molality`       | `KMassUnitInstance`                  | आवश्यक विलायक द्रव्यमान            |
| `1 / molarMass`                      | `KMolalityUnitInstance`              | शुद्ध पदार्थ की मोलैलिटी           |
| `1 / molality`                       | `KMolarMassUnitInstance`             | मोलर द्रव्यमान पर वापस             |

अंतिम दो संबंध दर्शाते हैं कि मोलैलिटी और [मोलर द्रव्यमान](molar-mass.hi.md) एक-दूसरे के
व्युत्क्रम हैं।

## वास्तविक उदाहरण — एक किलोग्राम पानी में कितने मोल हैं?

पानी का मोलर द्रव्यमान 18.015 g/mol है, इसलिए इसके एक किलोग्राम में लगभग 55.5 mol होते हैं —
यह व्युत्क्रम संबंध का व्यावहारिक उदाहरण है:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## मान अर्थ विज्ञान

`equals`/`hashCode` **सामान्यीकृत mol/kg मान** की तुलना करते हैं, इसलिए
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`। `toString()` आधार इकाई में
मान प्रस्तुत करता है: `"0.25 mol/kg"`।

## यह भी देखें

* [पदार्थ-की-मात्रा सांद्रता](concentration.hi.md) — वही विचार प्रति आयतन।
* [मोलर द्रव्यमान](molar-mass.hi.md) — व्युत्क्रम राशि।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
