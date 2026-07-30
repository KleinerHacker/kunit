# मोलर ऊर्जा

पैकेज: `org.pcsoft.framework.kunit.thermo.molarenergy`
मूल इकाई: **जूल प्रति मोल** (`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`)

प्रकार: **संघटित इकाई**

मोलर ऊर्जा प्रति पदार्थ की मात्रा ऊर्जा है: `energy / amountOfSubstance` (`J/mol`)। संदर्भ के आधार पर
इसी राशि को *मोलर एन्थैल्पी*, *अभिक्रिया एन्थैल्पी* या *बंधन ऊर्जा* भी कहा जाता है।

`KMolarEnergyUnitInstance` विहित सामान्य रूप `mass¹ · distance² · time⁻² · substance⁻¹`
(`kg·m²·s⁻²·mol⁻¹`) में ठीक चार पदों वाले `KMixedUnitInstance` को लपेटता है, जो हमेशा J/mol में
सामान्यीकृत रहता है।

प्रति इकाई तापमान यह [मोलर ऊष्मा क्षमता](molar-heat-capacity.md) बन जाती है; प्रति मोल के बजाय प्रति
किलोग्राम यह [विशिष्ट ऊर्जा](specific-energy.md) बन जाती है।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | 1 इकाई = ? J/mol |
|---|---|---:|---:|
| जूल प्रति मोल | `J/mol` | `joulesPerMole` | 1.0 |
| कैलोरी प्रति मोल | `cal/mol` | `caloriesPerMole` | 4.184 |
| इलेक्ट्रॉनवोल्ट प्रति इकाई | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

इलेक्ट्रॉनवोल्ट-प्रति-इकाई टोकन एक *प्रति-कण* ऊर्जा को एक *प्रति-मोल* ऊर्जा में परिवर्तित करता है — इसका
गुणक फैराडे स्थिरांक है। सभी इकाइयों में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.joulesPerMole`,
`kilo.caloriesPerMole`, `milli.electronVoltsPerEntity`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV प्रति अणु
```

## वास्तविक उदाहरण: हाइड्रोजन का जलना

तरल पानी की निर्माण एन्थैल्पी −286 kJ/mol है। 4 मोल हाइड्रोजन जलने पर कितनी ऊर्जा निकलती है, और वह
प्रति अणु कितनी है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// प्रति अणु, रसायनज्ञों की इकाई में
formation into electronVoltsPerEntity // ≈ -2.964 eV

// उलटा: 1 MJ कितनी मात्रा के बराबर है?
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## मूल इकाइयों (ऊर्जा और पदार्थ की मात्रा) से गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | मोलर ऊर्जा |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | कुल ऊर्जा |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | कुल ऊर्जा (क्रमविनिमेय) |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | शामिल पदार्थ की मात्रा |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन | रूप | परिणाम |
|---|---|---|
| `energy / amountOfSubstance` | टाइप किया गया संकारक | सीधे `KMolarEnergyUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹` | मूल व्यंजक + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// टाइप किया गया संकारक रूप
val typed = (1 of joules) / (1 of moles)

// मूल आधार-आयाम रूप (kg·m²·s⁻²·mol⁻¹), toMolarEnergy() द्वारा पहचाना गया
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true - दोनों 1.0 J/mol हैं
```

`toMolarEnergy()` **केवल** विहित सामान्य रूप को पहचानता है; एक गलत रूप `IllegalStateException`
फेंकता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `J/mol` | `joulesPerMole` | मोलर ऊर्जा, मूल इकाई — नामित टोकन |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | वही राशि आधार आयामों में |
| `kJ/mol` | `kilo.joulesPerMole` | किलोजूल प्रति मोल |
| `eV` (प्रति कण) | `electronVoltsPerEntity` | प्रति मौलिक इकाई इलेक्ट्रॉनवोल्ट |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | ऊर्जा ÷ मात्रा से मोलर ऊर्जा |
| `Q = ΔH_m · n` | `formation * hydrogen` | मोलर ऊर्जा × मात्रा से ऊर्जा |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | ऊर्जा ÷ मोलर ऊर्जा से मात्रा |
