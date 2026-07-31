# यांत्रिक प्रतिबल और प्रत्यास्थ मापांक

पैकेज: `org.pcsoft.framework.kunit.mechanic.pressure`
मूल इकाई: **पास्कल** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

प्रकार: **निर्मित इकाई**

यांत्रिक प्रतिबल `σ = F / A` और प्रत्यास्थ (यंग) मापांक `E = σ / ε` का आयाम ठीक
[दाब](pressure.md) का ही है: `mass · length⁻¹ · time⁻²`। इसलिए KUnit इनके लिए एक इकाई समूह पेश **नहीं**
करता — दोनों दाब समूह के **पठन** हैं, जो इसके उपसर्ग उपनामों के माध्यम से व्यक्त किए जाते हैं। यह पृष्ठ उन पठनों का
दस्तावेज़ीकरण करता है; समूह स्वयं [दाब](pressure.md) पृष्ठ पर वर्णित है।

!!! note "MPa, N/mm² और GPa उपसर्ग उपनाम हैं"
स्टैटिक्स इकाइयाँ **समर्पित** टोकन **नहीं** हैं, क्योंकि वे ठीक-ठीक पहुँच योग्य हैं:
**MPa = N/mm² = `mega.pascals`** और **GPa = `giga.pascals`**। `(1 of newtons) / ((1 of milli.meters) *
    (1 of milli.meters))` वही मान देता है जो `1 of mega.pascals` देता है।

## पठन तालिका

| पठन           | प्रतीक    | Kotlin         | Pa में 1 इकाई |
|---------------|--------|----------------|-----------:|
| पास्कल           | `Pa`   | `pascals`      |        1.0 |
| किलोपास्कल         | `kPa`  | `kilo.pascals` |        1e3 |
| मेगापास्कल = N/mm² | `MPa`  | `mega.pascals` |        1e6 |
| गीगापास्कल (मापांक)   | `GPa`  | `giga.pascals` |        1e9 |
| बल प्रति क्षेत्रफल    | `N/m²` | `force / area` |        1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0 (N/mm² ही मेगापास्कल है)
```

## हुक का नियम

[विकृति](strain.md) समूह के साथ मिलकर, दाब समूह हुक के नियम के दोनों पक्षों को धारण करता है:

| व्यंजक                                      | परिणाम प्रकार                | अर्थ                  |
|------------------------------------------|-------------------------|---------------------|
| `force / area`                           | `KPressureUnitInstance` | प्रतिबल `σ = F / A`    |
| `stress / strain`                        | `KPressureUnitInstance` | प्रत्यास्थ मापांक `E = σ / ε` |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | प्रतिबल `σ = E · ε`    |
| `pressure * area`                        | `KForceUnitInstance`    | क्रियाशील बल `F = σ · A` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0 (स्टील)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## वास्तविक उदाहरण: भार के अंतर्गत टाई रॉड

20 mm व्यास (A ≈ 314 mm²) की एक स्टील टाई रॉड 60 kN वहन करती है। प्रतिबल क्या है, क्या यह S235 स्टील की 235 MPa उपज
शक्ति से नीचे है, और 3 m की छड़ कितनी खिंचती है (E = 210 GPa)?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - उपज शक्ति के भीतर

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E एक सरल अनुपात के रूप में
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa" (समूह की मूल इकाई)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित          | Kotlin                                            | अर्थ                           |
|--------------|---------------------------------------------------|------------------------------|
| `MPa`        | `mega.pascals`                                    | प्रतिबल पठन (= N/mm²)           |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | बल प्रति क्षेत्रफल के रूप में वही पठन     |
| `GPa`        | `giga.pascals`                                    | प्रत्यास्थ-मापांक पठन                  |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | वही राशि आधार आयामों में              |
| `σ = F / A`  | `force / area`                                    | बल और क्षेत्रफल से प्रतिबल            |
| `E = σ / ε`  | `stress / strain`                                 | हुक का नियम, मापांक के लिए हल किया गया  |
| `σ = E · ε`  | `pressure * strain`                               | हुक का नियम, प्रतिबल के लिए हल किया गया |

</content>
