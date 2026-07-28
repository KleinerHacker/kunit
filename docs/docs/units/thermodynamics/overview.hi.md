# ऊष्मागतिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.thermo.temperature`, `…energy`, `…power`

ऊष्मागतिकी **ऊष्मा और तापमान** का भौतिकी है। KUnit में यह क्षेत्र तापमान पर केंद्रित है, जिसे
**दो संबंधित नेटिव समूहों** से मॉडल किया जाता है — क्योंकि तापमान का *पठन* और तापमान का *परिवर्तन*
भौतिक रूप से भिन्न प्रकार की राशियाँ हैं, और इन्हें अलग रखना ही गणित को सही बनाता है। इनके इर्द-गिर्द हर
ऊष्मा संतुलन की दो **संघटित** राशियाँ होती हैं: स्वयं ऊष्मा (ऊर्जा) और वह दर जिससे वह प्रवाहित होती है
(शक्ति)।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | प्रकृति | मूल इकाई | पृष्ठ |
|---|---|---|---|---|
| परम तापमान | नेटिव | ऐफ़ाइन **बिंदु** | केल्विन (`K`) | [परम तापमान](temperature.md) |
| तापमान अंतर | नेटिव | रैखिक **अंतराल** | केल्विन (`ΔK`) | [तापमान अंतर](temperature-difference.md) |
| ऊर्जा | संघटित | रैखिक राशि | जूल (`J`) | [ऊर्जा (ऊष्मागतिकी)](energy.md) |
| शक्ति | संघटित | रैखिक राशि | वाट (`W`) | [शक्ति (ऊष्मागतिकी)](power.md) |

ऊर्जा (ऊष्मा) और शक्ति (ऊष्मा प्रवाह दर) तकनीकी रूप से प्रत्येक **एक ही** राशि हैं, जो अन्य विषय क्षेत्रों
के साथ साझा होती हैं; इन्हें प्रत्येक क्षेत्र के अनुसार दस्तावेज़ीकृत किया गया है और ये एक-दूसरे का
संदर्भ देती हैं ([ऊर्जा (विद्युत)](../electrical/energy.md), [ऊर्जा (यांत्रिकी)](../mechanics/energy.md),
[शक्ति (विद्युत)](../electrical/power.md), [शक्ति (यांत्रिकी)](../mechanics/power.md))।

समर्पित [तापमान अवलोकन](temperature-overview.md) बिंदु बनाम अंतराल के भेद को गहराई से समझाता है; यह पृष्ठ
समूची ऊष्मागतिकी का प्रवेश-बिंदु है।

## बिंदु बनाम अंतराल — संकारक नियम

| संक्रिया | परिणाम |
|---|---|
| `परम − परम` | **तापमान अंतर** |
| `परम + अंतर` | परम तापमान |
| `परम − अंतर` | परम तापमान |
| `अंतर ± अंतर` | तापमान अंतर |
| `परम + परम` | **संकलन त्रुटि** (भौतिक रूप से निरर्थक) |

## टाइप-निर्धारित संकारकों के रूप में ऊष्मा और ऊष्मा प्रवाह

| अभिव्यक्ति | परिणाम | सूत्र |
|---|---|---|
| `power * time` | ऊर्जा (ऊष्मा) | `Q = Φ · t` |
| `energy / time` | शक्ति (ऊष्मा प्रवाह) | `Φ = Q / t` |
| `energy / power` | समय | `t = Q / Φ` |
| `power / frequency` | ऊर्जा | `Q = Φ / f` |

## वास्तविक उदाहरण — एक तापन चरण

पानी को **10 °C** से **30 °C** तक गर्म किया जाता है। यह *परिवर्तन* एक तापमान **अंतर** (`ΔT`) है, जो
`Q = m · c · ΔT` जैसे ऊष्मा सूत्रों में आने वाली राशि है; शून्य-बिंदु निरस्त हो जाता है, इसलिए `°C` और `K`
चरण-आकार पर सहमत होते हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0 (केल्विन अंतराल)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## वास्तविक उदाहरण — बॉयलर की ऊष्मा और तापन समय

एक **2 kW** का बॉयलर **10 मिनट** तक चलता है। दी गई ऊष्मा `Q = Φ · t` है; इसे ऊष्मा प्रवाह से पुनः विभाजित
करने पर तापन समय मिलता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8 (kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (केल्विन) में प्रस्तुत करता है: परम तापमान `K` के रूप
में और अंतर विशिष्ट `ΔK` प्रतीक के रूप में छपता है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

(25 of celsius).toString()                       // "298.15 K" (परम, मूल इकाई)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK" (अंतराल)
```

## संकेतन

नीचे दी गई तालिका तापमान संबंधों को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। `Δ` एक अंतराल राशि
को दर्शाता है, जो परम बिंदु से जानबूझकर भिन्न है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | दो परम तापमानों से अंतर |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | अंतराल से खिसका परम तापमान |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | एक स्पष्ट तापमान अंतराल |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | दो अंतरालों का योग |
| `Q = Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | ऊष्मा प्रवाह × समय से ऊष्मा |
| `Φ = Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | ऊष्मा ÷ समय से ऊष्मा प्रवाह |

## आगे कहाँ जाएँ

* [तापमान अवलोकन](temperature-overview.md) — बिंदु बनाम अंतराल की पूर्ण चर्चा और यह भौतिक रूप से क्यों
  महत्वपूर्ण है (ऊष्मा ऊर्जा, विकिरण, आदर्श गैस नियम)।
* [परम तापमान](temperature.md) — केल्विन, सेल्सियस, फ़ारेनहाइट, रैंकिन और ऐफ़ाइन संकारक।
* [तापमान अंतर](temperature-difference.md) — रैखिक केल्विन अंतराल समूह।
* [ऊर्जा (ऊष्मागतिकी)](energy.md) — ऊष्मा के रूप में जूल, साथ ही कैलोरी और BTU।
* [शक्ति (ऊष्मागतिकी)](power.md) — ऊष्मा प्रवाह दर के रूप में वाट, `Q / t`।
