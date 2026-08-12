# उत्प्रेरक गतिविधि

पैकेज: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
आधार इकाई: **कैटल** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

प्रकार: **निर्मित इकाई (constructed unit)**

किसी एंजाइम तैयारी की उत्प्रेरक गतिविधि `z` यह दर्शाती है कि वह **प्रति समय** कितना
सब्सट्रेट परिवर्तित करता है: `z = n / t`। इसकी SI इकाई **कैटल** है (1 kat = 1 mol/s) —
एक बहुत बड़ी इकाई, इसलिए व्यवहार में माइक्रोकैटल या पारंपरिक **एंजाइम इकाई** `U`
(प्रति मिनट एक माइक्रोमोल) का उपयोग किया जाता है।

इसका मानक आधार-आयाम सामान्य रूप `substance¹ · time⁻¹` है।

## नामित इकाइयाँ

| इकाई        | प्रतीक |         टोकन |          kat में 1 इकाई |
|-------------|--------|--------------:|-----------------------:|
| कैटल        | `kat`  |      `katals` |                    1.0 |
| एंजाइम इकाई  | `U`    | `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min, इसलिए 1 kat = 60,000,000 U और 1 U ≈ 16.67 nkat। सभी टोकन हर SI उपसर्ग
स्वीकार करते हैं (`micro.katals`, `nano.katals`, ...)।

## विघटन

इस समूह का एक विघटन है, और इसके दोनों रूप एक ही टाइप वाला, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                  | अभिव्यक्ति                                                                |
|----------------------|-------------------------------------------------------------------------------|
| टाइप किया ऑपरेटर      | `amountOfSubstance / time`                                                   |
| मूल (`toX()`)         | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## समूह के साथ गणना

| अभिव्यक्ति                                  | परिणाम प्रकार                     | अर्थ                  |
|-----------------------------------------------|-------------------------------------|-----------------------|
| `amountOfSubstance / time`                    | `KCatalyticActivityUnitInstance`    | `z = n / t`           |
| `catalyticActivity * time`                    | `KAmountOfSubstanceUnitInstance`    | `n = z · t`           |
| `amountOfSubstance / catalyticActivity`       | `KTimeUnitInstance`                 | लगने वाला समय          |

## वास्तविक उदाहरण — एक एंजाइम परख

एक परख **10 सेकंड** में **0.5 mmol** सब्सट्रेट परिवर्तित करती है। दोनों रूपों में व्यक्त, और
एक छोटे बैच को लगने वाला समय:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## मान अर्थ विज्ञान

`equals`/`hashCode` **सामान्यीकृत kat मान** की तुलना करते हैं, इसलिए
`(1 of katals) == (1000 of milli.katals)`। `toString()` आधार इकाई में मान प्रस्तुत करता है:
`"5.0E-5 kat"`।

## यह भी देखें

* [पदार्थ की मात्रा](amount-of-substance.hi.md) — अंश।
* [पदार्थ की मात्रा सांद्रता](concentration.hi.md) — जो एक परख आमतौर पर मापती है।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
