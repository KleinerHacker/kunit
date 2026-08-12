# तापीय चालन (Thermal Conductance)

पैकेज: `org.pcsoft.framework.kunit.thermo.conductance`
मूल इकाई: **वाट प्रति केल्विन** (`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`)

प्रकार: **संघटित इकाई**

किसी घटक की तापीय चालन `G` बताती है कि उसमें से प्रति इकाई तापमान अंतर पर कितनी ऊष्मा प्रवाहित होती है:
`G = P / ΔT`, इकाई `W/K` में मापा जाता है। यह [परम तापीय प्रतिरोध](thermal-resistance.md) का ठीक व्युत्क्रम है, और
जब ऊष्मा मार्ग **समानांतर** में होते हैं तो यह अधिक सुविधाजनक रूप है — समानांतर चालन बस जुड़ जाते हैं।

इसका विहित आधार-आयाम सामान्य रूप `mass · length² · time⁻³ · temperature⁻¹` है।

## नामित इकाइयाँ

| इकाई                     | संकेत        |                   टोकन | W/K में 1 इकाई |
|--------------------------|--------------|------------------------:|----------------:|
| वाट प्रति केल्विन        | `W/K`        |         `wattsPerKelvin` |             1.0 |
| Btu प्रति घंटा-°F        | `Btu/(h*°F)` | `btusPerHourFahrenheit` |       ≈ 0.52753 |

सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`milli.wattsPerKelvin`, …)।

## अपघटन

इस समूह का एक अपघटन है, और उसके दोनों रूप समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं। नेटिव रूप
**यूनिट टेम्पलेट्स** से जोड़ा जाता है क्योंकि इस समूह में द्रव्यमान पद होता है।

| रूप                    | व्यंजक                                                             |
|------------------------|-----------------------------------------------------------------------|
| टाइप किया गया संकारक   | `power / temperatureDifference`                                        |
| नेटिव (`toX()`)        | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`             |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## समूह के साथ गणना

| व्यंजक                                          | परिणाम प्रकार                            | अर्थ                     |
|-----------------------------------------------------|---------------------------------------------|---------------------------|
| `power / temperatureDifference`                     | `KThermalConductanceUnitInstance`          | `G = P / ΔT`              |
| `thermalConductance * temperatureDifference`        | `KPowerUnitInstance`                       | `P = G · ΔT`              |
| `power / thermalConductance`                        | `KTemperatureDifferenceUnitInstance`       | आवश्यक तापमान अंतर       |
| `thermalConductance + …`                            | `KThermalConductanceUnitInstance`          | समानांतर ऊष्मा मार्ग     |
| `1 / thermalConductance`                            | `KThermalResistanceUnitInstance`           | `R = 1 / G`               |
| `1 / thermalResistance`                             | `KThermalConductanceUnitInstance`          | `G = 1 / R`               |

## वास्तविक उदाहरण: दो समानांतर ऊष्मा मार्ग

एक मॉड्यूल अपने आधार-प्लेट (0.4 W/K) और अपने आवरण (0.1 W/K) से होकर ऊष्मा खोता है। समानांतर होने के कारण
चालन जुड़ जाते हैं, और व्युत्क्रम से कुल प्रतिरोध मिलता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // ΔT = 30 K पर 15.0 W ले जाई गई
```

## मान सिमेंटिक्स

`equals`/`hashCode` **सामान्यीकृत W/K मान** की तुलना करते हैं, इसलिए
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`। `toString()` मूल इकाई में मान प्रदर्शित करता है:
`"0.4 W/K"`।

## यह भी देखें

* [परम तापीय प्रतिरोध](thermal-resistance.hi.md) — व्युत्क्रम राशि।
* [तापीय इन्सुलेंस](thermal-insulance.hi.md) — प्रतिरोध का प्रति-क्षेत्र रूप।
* [ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.hi.md) — इस राशि का प्रति-क्षेत्र रूप।
* [ऊष्मागतिकी अवलोकन](overview.hi.md)
