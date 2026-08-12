# मोलर चालकता

पैकेज: `org.pcsoft.framework.kunit.electric.molarconductivity`
आधार इकाई: **सीमेंस वर्ग मीटर प्रति मोल**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

प्रकार: **संरचित इकाई**

किसी विद्युत-अपघट्य की मोलर चालकता `Λ`, उसकी [चालकता](conductivity.hi.md) को [सांद्रता](../thermodynamics/concentration.hi.md)
से सामान्यीकृत करके प्राप्त होती है: `Λ = κ / c`। सांद्रता को विभाजित करने से भिन्न प्रबलता के विलयनों की तुलना संभव हो
जाती है — यह इस प्रश्न का उत्तर देता है कि "यह आयन कितनी अच्छी तरह चालन करता है", न कि "यह विशेष बीकर कितनी अच्छी तरह
चालन करता है"।

इसका विहित आधार-आयाम मानक रूप `mass⁻¹ · time³ · current² · substance⁻¹` है। लंबाई आयाम पूर्णतः निरस्त हो जाता है:
चालकता `length⁻³` का योगदान करती है और हर में सांद्रता एक और `length⁻³` का।

## नामित इकाइयाँ

| इकाई                             | प्रतीक       |                            टोकन | 1 इकाई S·m²/mol में |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| सीमेंस वर्ग मीटर प्रति मोल      | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| सीमेंस वर्ग सेंटीमीटर प्रति मोल | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

विद्युत-रसायन तालिकाएँ आमतौर पर S·cm²/mol में दी जाती हैं; SI रूप सामान्यतः milli उपसर्ग के साथ लिखा जाता है
(`milli.siemensSquareMetersPerMole`)। सभी टोकन हर SI उपसर्ग स्वीकार करते हैं।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही मान-समान टाइप्ड इंस्टेंस बनाते हैं। मूल रूप **यूनिट टेम्पलेट्स**
से जोड़ा जाता है क्योंकि समूह में द्रव्यमान पद है: कच्चा मिश्रित मान ग्राम-आधारित गुणनफल है, जबकि टाइप्ड इंस्टेंस अपना
मान नामित इकाई में संग्रहीत करता है।

| रूप              | व्यंजक                                                          |
|------------------|---------------------------------------------------------------------|
| टाइप्ड ऑपरेटर    | `conductivity / concentration`                                      |
| मूल (`toX()`)    | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## समूह के साथ गणना

| व्यंजक                              | परिणाम प्रकार                      | अर्थ       |
|-------------------------------------|-----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | कोलराउश का नियम |

आयनों के स्वतंत्र प्रवास का कोलराउश नियम कहता है कि अनंत तनुता पर मोलर चालकता, आयनिक योगदानों का **योग** होती है —
जो ठीक समूह के समान-प्रकार `+` ऑपरेटर के बराबर है।

## वास्तविक उदाहरण — KCl के लिए कोलराउश नियम

सीमांत आयनिक चालकताएँ K⁺ के लिए 7.35 mS·m²/mol और Cl⁻ के लिए 7.63 mS·m²/mol हैं। इनका योग पोटैशियम क्लोराइड की
सीमांत मोलर चालकता है, और किसी सांद्रता से गुणा करने पर वह चालकता मिलती है जो एक मीटर मापेगा:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // कोलराउश
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85 (सारणी मान)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत S·m²/mol मान** की तुलना करते हैं, इसलिए
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)` सत्य है। `toString()` मान को
आधार इकाई में दर्शाता है: `"0.0126 S*m^2/mol"`।

## यह भी देखें

* [चालकता](conductivity.hi.md) — अंश।
* [पदार्थ-की-मात्रा सांद्रता](../thermodynamics/concentration.hi.md) — हर।
* [चालन क्षमता](conductance.hi.md) — मीटर द्वारा मापी जाने वाली गैर-सामान्यीकृत राशि।
* [इलेक्ट्रिकल इंजीनियरिंग अवलोकन](overview.hi.md)
