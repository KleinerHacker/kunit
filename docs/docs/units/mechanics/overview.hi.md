# यांत्रिकी — अवलोकन

पैकेज: `org.pcsoft.framework.kunit.mass`, `…force`, `…pressure`, `…density`, `…areadensity`, `…power`,
`…energy`

यांत्रिकी (गतिकी) पूछती है कि पिंड **क्यों** गति करते हैं और पदार्थ कैसे वितरित होता है: द्रव्यमान, उस
पर लगने वाले बल, किसी बल द्वारा क्षेत्रफल पर डाले गए दाब, और किसी आयतन या सतह में कितना द्रव्यमान भरा
है — इनकी परस्पर क्रिया। [शुद्धगतिकी](../kinematics/overview.md) की दरों पर आधार बनाकर, यह विषय एक
**नेटिव** मूल राशि (द्रव्यमान) और द्रव्यमान, लंबाई तथा समय से **निर्मित** चार राशियाँ जोड़ता है।

## इस विषय की इकाइयाँ

| इकाई | प्रकार | मूल इकाई | पृष्ठ |
|---|---|---|---|
| द्रव्यमान | नेटिव | ग्राम (`g`) | [द्रव्यमान](mass.md) |
| बल | निर्मित | न्यूटन (`N`) | [बल](force.md) |
| दाब | निर्मित | पास्कल (`Pa`) | [दाब](pressure.md) |
| घनत्व | निर्मित | किलोग्राम प्रति घन मीटर (`kg/m³`) | [घनत्व](density.md) |
| क्षेत्रीय घनत्व | निर्मित | किलोग्राम प्रति वर्ग मीटर (`kg/m²`) | [क्षेत्रीय घनत्व](areadensity.md) |
| शक्ति | निर्मित | वाट (`W`) | [शक्ति (यांत्रिकी)](power.md) |
| ऊर्जा | निर्मित | जूल (`J`) | [ऊर्जा (यांत्रिकी)](energy.md) |

शक्ति और ऊर्जा तकनीकी रूप से प्रत्येक **एक** राशि है, जो अन्य विषय क्षेत्रों के साथ साझा की जाती है; इन्हें
प्रत्येक क्षेत्र में प्रलेखित किया गया है और वे एक-दूसरे को संदर्भित करते हैं
([शक्ति (विद्युत)](../electrical/power.md), [शक्ति (ऊष्मागतिकी)](../thermodynamics/power.md),
[ऊर्जा (विद्युत)](../electrical/energy.md), [ऊर्जा (ऊष्मागतिकी)](../thermodynamics/energy.md))।

## राशियाँ कैसे संबंधित हैं

| व्यंजक | परिणाम | सूत्र |
|---|---|---|
| `mass * acceleration` | बल | `F = m · a` |
| `force / area` | दाब | `p = F / A` |
| `pressure * area` | बल | `F = p · A` |
| `mass / volume` | घनत्व | `ρ = m / V` |
| `density * length` | क्षेत्रीय घनत्व | `ρ_A = ρ · d` |
| `force * speed` | शक्ति | `P = F · v` |
| `power / speed` | बल | `F = P / v` |
| `power / force` | गति | `v = P / F` |
| `force * length` | ऊर्जा (कार्य) | `W = F · s` |
| `power * time` | ऊर्जा | `W = P · t` |
| `energy / time` | शक्ति | `P = W / t` |
| `energy / power` | समय | `t = W / P` |

## वास्तविक उदाहरण — न्यूटन का द्वितीय नियम और भूमि दाब

एक **2 kg** ब्लॉक को मानक गुरुत्व पर त्वरित किया जाता है, और परिणामी भार बल को **0.5 m²** के आधार-क्षेत्र
पर फैलाया जाता है। बल `F = m · a` है, दाब `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*
import org.pcsoft.framework.kunit.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61 (N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance, 0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23 (Pa)
```

## वास्तविक उदाहरण — घनत्व से इस्पात भाग का द्रव्यमान

इस्पात का घनत्व **7850 kg/m³** है। एक **2 L** भाग का द्रव्यमान `m = ρ · V` है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7 (प्रति 2 L kg)
```

## वास्तविक उदाहरण — विंच का कार्य और शक्ति

एक विंच **5 s** में **5 m** तक **100 N** के साथ खींचती है। कार्य `W = F · s` है, शक्ति `P = W / t` — जो
सीधे यांत्रिक रूप `P = F · v` के बराबर है:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val w = (100 of newtons) * (5 of meters)                    // KEnergyUnitInstance
w into joules                                                // 500.0

val p = w / (5 of seconds)                                   // KPowerUnitInstance
p into watts                                                 // 100.0

val direct = (100 of newtons) * ((1 of meters) / (1 of seconds)) // P = F · v, 100 W
p == direct                                                  // true
```

## मान छापना (`toString`)

`toString()` किसी मान को उसके समूह की **मूल इकाई** (मान + प्रतीक) में प्रस्तुत करता है; किसी अन्य इकाई के
लिए, इसे स्ट्रिंग टेम्पलेट में `into` से पढ़ें और प्रतीक स्वयं जोड़ें:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N" (मूल इकाई)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## संकेतन

नीचे दी गई तालिका इस क्षेत्र के मूल संबंधों को गणितीय बनाम KUnit के Kotlin संकेतन में दर्शाती है। घातांक
Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)` | द्रव्यमान × त्वरण से बल |
| `p = F / A` | `f / area` | बल ÷ क्षेत्रफल से दाब |
| `F = p · A` | `p * area` | दाब × क्षेत्रफल से बल |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)` | द्रव्यमान ÷ आयतन से घनत्व |
| `m = ρ · V` | `steel * (2 of liters)` | घनत्व × आयतन से द्रव्यमान |
| `W = F · s` | `(100 of newtons) * (5 of meters)` | बल × लंबाई से कार्य |
| `P = F · v` | `(100 of newtons) * ((1 of meters) / (1 of seconds))` | बल × गति से शक्ति |
| `P = W / t` | `w / (5 of seconds)` | कार्य ÷ समय से शक्ति |

## आगे कहाँ जाएँ

* [द्रव्यमान](mass.md) — नेटिव मूल राशि (ग्राम-प्रसामान्यीकृत)।
* [बल](force.md) और [दाब](pressure.md) — न्यूटन का नियम और क्षेत्रफल पर बल।
* [घनत्व](density.md) और [क्षेत्रीय घनत्व](areadensity.md) — प्रति आयतन और प्रति सतह द्रव्यमान।
* [शक्ति (यांत्रिकी)](power.md) — वाट, `F · v`, और अश्वशक्ति इकाइयाँ।
* [ऊर्जा (यांत्रिकी)](energy.md) — यांत्रिक कार्य `F · s` के रूप में जूल।
