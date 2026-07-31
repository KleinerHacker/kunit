# मोलर आयतन

पैकेज: `org.pcsoft.framework.kunit.thermo.molarvolume`
मूल इकाई: **घन मीटर प्रति मोल** (`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`)

प्रकार: **संघटित इकाई**

मोलर आयतन प्रति पदार्थ की मात्रा आयतन है: `volume / amountOfSubstance` (`m³/mol`)। एक आदर्श गैस के लिए यह हर पदार्थ के
लिए समान होता है (0 °C और 100 kPa पर 22.711 l/mol); ठोसों और तरलों के लिए यह
[मोलर द्रव्यमान](molar-mass.md) और घनत्व से प्राप्त होता है।

`KMolarVolumeUnitInstance` विहित सामान्य रूप `distance³ · substance⁻¹` (`m³·mol⁻¹`) में ठीक दो पदों वाले
`KMixedUnitInstance` को लपेटता है, जो हमेशा m³/mol में सामान्यीकृत रहता है। दोनों घटक अपने समूह की मूल इकाई में संग्रहीत
हैं, इसलिए कच्चा घटक आधार *ही* नामित मूल इकाई है।

[आवर्त सारणी](../../periodic-table.md) का हर तत्व अपने मोलर द्रव्यमान और घनत्व से नीचे दिए गए दूसरे अपघटन के माध्यम से
अपना मोलर आयतन प्राप्त करता है।

## नामित इकाइयाँ

| इकाई            | संकेत        |                       टोकन | 1 इकाई = ? m³/mol |
|----------------|------------|--------------------------:|-----------------:|
| घन मीटर प्रति मोल   | `m^3/mol`  |      `cubicMetersPerMole` |              1.0 |
| लीटर प्रति मोल      | `l/mol`    |           `litersPerMole` |            0.001 |
| घन सेंटीमीटर प्रति मोल | `cm^3/mol` | `cubicCentimetersPerMole` |           1.0e-6 |

सभी इकाइयों में पूर्ण SI उपसर्ग सीमा समर्थित है (`milli.cubicMetersPerMole`, `milli.litersPerMole`, …)। पैकेज इसके
अतिरिक्त स्थिरांक `MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464 (m³/mol) उपलब्ध कराता है, जो मानक परिस्थितियों में एक
आदर्श गैस का मोलर आयतन है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## वास्तविक उदाहरण: हीलियम से भरा गुब्बारा

मानक परिस्थितियों में एक आदर्श गैस के 2 मोल कितनी जगह घेरते हैं — और 5 लीटर के गुब्बारे में कितने मोल समा सकते हैं?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// 2 मोल का आयतन
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// 5 l के गुब्बारे में कितने मोल समाते हैं?
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// और भरे हुए गुब्बारे से मापा गया मोलर आयतन:
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## वास्तविक उदाहरण: पानी के एक मोल का आयतन

पानी का मोलर द्रव्यमान 18.015 g/mol है और घनत्व 1 kg/l है, इसलिए एक मोल लगभग 18 cm³ घेरता है — एक बड़ा चम्मच।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## मूल इकाइयों से गणना

| व्यंजक                               | परिणाम प्रकार                         | अर्थ                        |
|-----------------------------------|----------------------------------|---------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | मोलर आयतन                  |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | मोलर आयतन (दूसरा अपघटन)      |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | कुल आयतन                   |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | कुल आयतन (क्रमविनिमेय)          |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | शामिल पदार्थ की मात्रा              |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [मोलर द्रव्यमान](molar-mass.md) |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | मोलर द्रव्यमान (क्रमविनिमेय)         |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | घनत्व                       |

## अपघटन

सभी अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                        | रूप                         | परिणाम                          |
|------------------------------|----------------------------|-------------------------------|
| `volume / amountOfSubstance` | टाइप किया गया संकारक             | सीधे `KMolarVolumeUnitInstance` |
| `molarMass / density`        | टाइप किया गया संकारक             | सीधे `KMolarVolumeUnitInstance` |
| `distance³ · substance⁻¹`    | मूल व्यंजक + `toMolarVolume()` | `KMolarVolumeUnitInstance`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// टाइप किया गया संकारक रूप: आयतन / मात्रा
val typedVolume = (0.018015 of liters) / (1 of moles)

// टाइप किया गया संकारक रूप: मोलर द्रव्यमान / घनत्व
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// मूल आधार-आयाम रूप (m³·mol⁻¹), toMolarVolume() द्वारा पहचाना गया
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true - सभी 1.8015e-5 m³/mol हैं
```

`toMolarVolume()` **केवल** विहित सामान्य रूप को पहचानता है; एक गलत रूप `IllegalStateException`
फेंकता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                               | अर्थ                         |
|---------------|--------------------------------------|----------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | मोलर आयतन, मूल इकाई — नामित टोकन |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | वही राशि आधार आयामों में            |
| `l/mol`       | `litersPerMole`                      | लीटर प्रति मोल                  |
| `cm³/mol`     | `cubicCentimetersPerMole`            | घन सेंटीमीटर प्रति मोल             |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | आयतन ÷ मात्रा से मोलर आयतन       |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | मोलर द्रव्यमान ÷ घनत्व से मोलर आयतन  |
| `V = V_m · n` | `ideal * (2 of moles)`               | मोलर आयतन × मात्रा से आयतन       |
| `n = V / V_m` | `(5 of liters) / ideal`              | आयतन ÷ मोलर आयतन से मात्रा       |
| `ρ = M / V_m` | `molarMass / molarVolume`            | मोलर द्रव्यमान ÷ मोलर आयतन से घनत्व  |
