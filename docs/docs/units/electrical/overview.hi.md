# विद्युत अभियांत्रिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`, `…charge`, `…conductance`,
`…magneticfieldstrength`, `…capacitance`, `…inductance`, `…magneticflux`, `…magneticfluxdensity`,
`…currentdensity`, `…chargedensity`, `…resistivity`, `…conductivity`, `…power`, `…energy`

विद्युत अभियांत्रिकी परिपथ में बहने वाली धारा, उसे चलाने वाली वोल्टता, और उसका विरोध करने वाले प्रतिरोध
को एक साथ जोड़ती है। ये तीनों **ओम के नियम** से बँधे हैं, और KUnit इस नियम को सीधे प्रकार-युक्त `*` और `/`
संकारकों के रूप में व्यक्त करता है: एक **नेटिव** मूल राशि (विद्युत धारा) और मूल विमाओं से **निर्मित**
राशियाँ (वोल्टता, प्रतिरोध, आवेश, चालकता और चुंबकीय क्षेत्र तीव्रता)।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | मूल इकाई | पृष्ठ |
|---|---|---|---|
| विद्युत धारा | नेटिव | ऐम्पियर (`A`) | [विद्युत धारा](ec.md) |
| वोल्टता | निर्मित | वोल्ट (`V`) | [वोल्टता](voltage.md) |
| प्रतिरोध | निर्मित | ओम (`Ω`) | [प्रतिरोध](resistance.md) |
| आवेश | निर्मित | कूलॉम (`C`) | [आवेश](charge.md) |
| चालकता | निर्मित | सीमेंस (`S`) | [चालकता](conductance.md) |
| चुंबकीय क्षेत्र तीव्रता | निर्मित | ऐम्पियर प्रति मीटर (`A/m`) | [चुंबकीय क्षेत्र तीव्रता](magneticfieldstrength.md) |
| धारिता | निर्मित | फैरड (`F`) | [धारिता](capacitance.md) |
| प्रेरकत्व | निर्मित | हेनरी (`H`) | [प्रेरकत्व](inductance.md) |
| चुंबकीय फ्लक्स | निर्मित | वेबर (`Wb`) | [चुंबकीय फ्लक्स](magneticflux.md) |
| चुंबकीय फ्लक्स घनत्व | निर्मित | टेस्ला (`T`) | [चुंबकीय फ्लक्स घनत्व](magneticfluxdensity.md) |
| धारा घनत्व | निर्मित | ऐम्पियर प्रति वर्ग मीटर (`A/m²`) | [धारा घनत्व](currentdensity.md) |
| आवेश घनत्व | निर्मित | कूलॉम प्रति घन मीटर (`C/m³`) | [आवेश घनत्व](chargedensity.md) |
| प्रतिरोधकता | निर्मित | ओम मीटर (`Ω·m`) | [प्रतिरोधकता](resistivity.md) |
| चालकता (सामग्री) | निर्मित | सीमेंस प्रति मीटर (`S/m`) | [चालकता (सामग्री)](conductivity.md) |
| शक्ति | निर्मित | वाट (`W`) | [शक्ति (विद्युत)](power.md) |
| ऊर्जा | निर्मित | जूल (`J`) | [ऊर्जा (विद्युत)](energy.md) |

शक्ति और ऊर्जा तकनीकी रूप से प्रत्येक **एक** राशि है, जो अन्य विषय क्षेत्रों के साथ साझा की जाती है; इन्हें
प्रत्येक क्षेत्र में प्रलेखित किया गया है और वे एक-दूसरे को संदर्भित करते हैं
([शक्ति (यांत्रिकी)](../mechanics/power.md), [शक्ति (ऊष्मागतिकी)](../thermodynamics/power.md),
[ऊर्जा (यांत्रिकी)](../mechanics/energy.md), [ऊर्जा (ऊष्मागतिकी)](../thermodynamics/energy.md))।

## प्रकार-युक्त संकारकों के रूप में ओम का नियम

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `resistance * current` | वोल्टता | `U = R · I` |
| `current * resistance` | वोल्टता | `U = R · I` (क्रमविनिमेय) |
| `voltage / current` | प्रतिरोध | `R = U / I` |
| `voltage / resistance` | विद्युत धारा | `I = U / R` |
| `current / voltage` | चालकता | `G = I / U` |
| `1 / resistance` | चालकता | `G = 1 / R` |
| `1 / conductance` | प्रतिरोध | `R = 1 / G` |
| `conductance * voltage` | विद्युत धारा | `I = G · U` |
| `current / conductance` | वोल्टता | `U = I / G` |

## अन्य प्रकार-युक्त संकारक

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `current * time` | आवेश | `Q = I · t` |
| `current / frequency` | आवेश | `Q = I / f` |
| `charge / time` | विद्युत धारा | `I = Q / t` |
| `charge / current` | समय | `t = Q / I` |
| `current / length` | चुंबकीय क्षेत्र तीव्रता | `H = I / l` |
| `field strength * length` | विद्युत धारा | `I = H · l` |
| `charge / voltage` | धारिता | `C = Q / U` |
| `capacitance * voltage` | आवेश | `Q = C · U` |
| `voltage * time` | चुंबकीय फ्लक्स | `Φ = U · t` |
| `flux / time` | वोल्टता | `U = Φ / t` |
| `flux / current` | प्रेरकत्व | `L = Φ / I` |
| `inductance * current` | चुंबकीय फ्लक्स | `Φ = L · I` |
| `resistance / frequency` | प्रेरकत्व | `L = X / ω` |
| `flux / area` | चुंबकीय फ्लक्स घनत्व | `B = Φ / A` |
| `flux density * area` | चुंबकीय फ्लक्स | `Φ = B · A` |
| `current / area` | धारा घनत्व | `J = I / A` |
| `current density * area` | विद्युत धारा | `I = J · A` |
| `charge / volume` | आवेश घनत्व | `ρ = Q / V` |
| `charge density * volume` | आवेश | `Q = ρ · V` |
| `resistance * length` | प्रतिरोधकता | `ρ = R · A / l` |
| `1 / resistivity` | चालकता (सामग्री) | `σ = 1 / ρ` |
| `1 / conductivity` | प्रतिरोधकता | `ρ = 1 / σ` |
| `conductance / length` | चालकता (सामग्री) | `σ = G · l / A` |
| `conductivity * length` | चालकता | `G = σ · A / l` |
| `voltage * current` | शक्ति | `P = U · I` |
| `power / voltage` | विद्युत धारा | `I = P / U` |
| `power / current` | वोल्टता | `U = P / I` |
| `power * time` | ऊर्जा | `W = P · t` |
| `energy / time` | शक्ति | `P = W / t` |
| `charge * voltage` | ऊर्जा | `W = Q · U` |
| `energy / charge` | वोल्टता | `U = W / Q` |

प्रत्येक परिणाम सही प्रकार-युक्त राशि है — कोई कच्ची मिश्रित इकाई हाथ से नहीं बनाई जाती। इसके अलावा वोल्टता,
प्रतिरोध, आवेश, चालकता और चुंबकीय क्षेत्र तीव्रता अपने पूर्ण **नेटिव** अपघटन (`kg·m²·s⁻³·A⁻¹`,
`kg·m²·s⁻³·A⁻²`, `A·s`, `kg⁻¹·m⁻²·s³·A²`, `A·m⁻¹`) को `toVoltage()` / `toResistance()` / `toCharge()` /
`toConductance()` / `toMagneticFieldStrength()` के माध्यम से पहचानते हैं। यही बात नए समूहों पर भी लागू
होती है: `toCapacitance()` (`kg⁻¹·m⁻²·s⁴·A²`), `toInductance()` (`kg·m²·s⁻²·A⁻²`), `toMagneticFlux()`
(`kg·m²·s⁻²·A⁻¹`), `toMagneticFluxDensity()` (`kg·s⁻²·A⁻¹`), `toCurrentDensity()` (`A·m⁻²`),
`toChargeDensity()` (`A·s·m⁻³`), `toResistivity()` (`kg·m³·s⁻³·A⁻²`), `toConductivity()`
(`kg⁻¹·m⁻³·s³·A²`), `toPower()` (`kg·m²·s⁻³`) और `toEnergy()` (`kg·m²·s⁻²`)।

## वास्तविक उदाहरण — एक परिपथ के इर्द-गिर्द ओम का नियम

एक भार **2 A** खींचते हुए **230 V** का पात दिखाता है। इसका प्रतिरोध `R = U / I` है; उस प्रतिरोध को धारा
के साथ वापस डालने पर वोल्टता `U = R · I` पुनः प्राप्त होती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance, 115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## वास्तविक उदाहरण — मुख्य आपूर्ति शक्ति से खपत ऊर्जा तक

**230 V** का सॉकेट **10 A** भार को शक्ति देता है, `P = U · I`; इसे तीन घंटे चलाने पर `W = P · t` ऊर्जा
खर्च होती है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (मान + प्रतीक) में प्रस्तुत करता है; किसी अन्य इकाई के
लिए, इसे स्ट्रिंग टेम्पलेट में `into` से पढ़ें और प्रतीक स्वयं जोड़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V" (मूल इकाई)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## संकेतन

नीचे दी गई तालिका ओम के नियम को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। घातांक Unicode उपरिलेख
(`²`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | वोल्टता ÷ धारा से प्रतिरोध |
| `U = R · I` | `r * (2 of amperes)` | प्रतिरोध × धारा से वोल्टता |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | वोल्टता ÷ प्रतिरोध से धारा |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | अपने नेटिव मानक रूप में प्रतिरोध |

## आगे कहाँ जाएँ

* [विद्युत धारा](ec.md) — नेटिव ऐम्पियर समूह (साथ ही CGS का बायो और स्टैट-ऐम्पियर)।
* [वोल्टता](voltage.md) — वोल्ट, और इसके अपघटन `R · I` तथा नेटिव रूप।
* [प्रतिरोध](resistance.md) — ओम, `U / I`, और व्युत्क्रम ओम-नियम संकारक।
* [आवेश](charge.md) — कूलॉम, `I · t`, और बैटरी क्षमता की ऐम्पियर-घंटा इकाई।
* [चालकता](conductance.md) — सीमेंस, `1 / R`, और `I / U`।
* [चुंबकीय क्षेत्र तीव्रता](magneticfieldstrength.md) — ऐम्पियर प्रति मीटर, `I / l`, और ओर्स्टेड।
* [धारिता](capacitance.md) — फैरड, `Q / U`, और CGS का ऐब्फैरड/स्टैट्फैरड।
* [प्रेरकत्व](inductance.md) — हेनरी, `Φ / I`, और प्रतिघात रूप `X / ω`।
* [चुंबकीय फ्लक्स](magneticflux.md) — वेबर, `U · t`, और मैक्सवेल।
* [चुंबकीय फ्लक्स घनत्व](magneticfluxdensity.md) — टेस्ला, `Φ / A`, और गॉस।
* [धारा घनत्व](currentdensity.md) — ऐम्पियर प्रति वर्ग मीटर, `I / A`, तार आकार के लिए।
* [आवेश घनत्व](chargedensity.md) — कूलॉम प्रति घन मीटर, `Q / V`।
* [प्रतिरोधकता](resistivity.md) — ओम मीटर, `R · A / l`, प्रतिरोध के पीछे की सामग्री गुणधर्म।
* [चालकता (सामग्री)](conductivity.md) — सीमेंस प्रति मीटर, `1 / ρ`, और `G · l / A`।
* [शक्ति (विद्युत)](power.md) — वाट, `U · I`, और अश्वशक्ति इकाइयाँ।
* [ऊर्जा (विद्युत)](energy.md) — जूल, `Q · U`, `P · t`, और किलोवाट-घंटा `kilo.watts * hours` के रूप में।
