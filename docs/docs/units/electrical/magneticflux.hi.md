# चुंबकीय फ्लक्स

पैकेज: `org.pcsoft.framework.kunit.electric.magneticflux`
आधार इकाई: **वेबर** (`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`)

प्रकार: **संरचित इकाई**

चुंबकीय फ्लक्स एक **संरचित** इकाई है: संयोजन `mass · length² · time⁻² · current⁻¹`
(`kg·m²·s⁻²·A⁻¹`)। `KMagneticFluxUnitInstance` चार पदों वाले `KMixedUnitInstance` को लपेटता है — घातांक `+1`
पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर `KDistanceUnit.BASE` (मीटर), घातांक `-2` पर `KTimeUnit.BASE`
(सेकंड) और घातांक `-1` पर `KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का द्रव्यमान घटक **ग्राम** में
सामान्यीकृत है (किलोग्राम में नहीं), वेबर तक पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित किया जाता है; संग्रहीत मान
हमेशा वेबर में सामान्यीकृत रहता है।

## चुंबकीय फ्लक्स बनाना

फ्लक्स को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में उपलब्ध रहती हैं
(`of`/`into` के साथ प्रयोग):

| चुंबकीय फ्लक्स        | संकेत    |         टोकन |          1 इकाई = ? Wb |
|----------------|--------|------------:|----------------------:|
| वेबर            | `Wb`   |    `webers` |                   1.0 |
| मैक्सवेल (CGS-EMU) | `Mx`   |  `maxwells` |                1.0e-8 |
| यूनिट पोल         | `pole` | `unitPoles` | 1.2566370614359173e-7 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`milli.webers`,
`micro.webers`, `kilo.maxwells`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## अनेक अपघटन

चुंबकीय फ्लक्स तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान फ्लक्स देते हैं:

| व्यंजक                            | परिणाम प्रकार                    | अर्थ                                                     |
|--------------------------------|-----------------------------|--------------------------------------------------------|
| `voltage * time`               | `KMagneticFluxUnitInstance` | फैराडे का प्रेरण नियम `Φ = U · t` (क्रमविनिमेय)                     |
| `voltage / frequency`          | `KMagneticFluxUnitInstance` | व्युत्क्रम‑समय रूप (`V/Hz = V·s`)                              |
| `inductance * current`         | `KMagneticFluxUnitInstance` | `Φ = L · I` (देखें [प्रेरकत्व](inductance.md))                 |
| `fluxDensity * area`           | `KMagneticFluxUnitInstance` | `Φ = B · A` (देखें [चुंबकीय फ्लक्स घनत्व](magneticfluxdensity.md)) |
| `mass·length²/(time²·current)` | `.toMagneticFlux()` द्वारा      | मूल विहित `kg·m²·s⁻²·A⁻¹` व्यंजक                             |

टाइप किए गए ऑपरेटर रूप सीधे फ्लक्स लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toMagneticFlux()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। सभी मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर वोल्टता, समय और फ्लक्स को जोड़ते हैं:

| व्यंजक                | परिणाम प्रकार               | अर्थ                  |
|--------------------|------------------------|---------------------|
| `flux / time`      | `KVoltageUnitInstance` | प्रेरित वोल्टता `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | व्युत्क्रम‑समय समकक्ष        |
| `flux / voltage`   | `KTimeUnitInstance`    | `t = Φ / U`         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.magneticflux.*

// वास्तविक उदाहरण - इग्निशन कॉइल: 4 ms में ढहता 20 mWb कोर फ्लक्स 5 V प्रेरित करता है।
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance, 5 V

// फ्लक्स के लिए हल किया गया प्रेरण नियम:
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance, 2 Wb

// आवृत्ति से वही फ्लक्स, और मूल kg·m²·s⁻²·A⁻¹ व्यंजक के रूप में:
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

(20 of webers).toString()     // "20.0 Wb" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित             | Kotlin                                                                | अर्थ                                      |
|-----------------|-----------------------------------------------------------------------|-----------------------------------------|
| `Wb`            | `webers`                                                              | चुंबकीय फ्लक्स, आधार इकाई (नामित टोकन, वेबर)         |
| `V·s`           | `(10 of volts) * (0.2 of seconds)`                                    | वोल्टता·समय के रूप में फ्लक्स (प्रेरण नियम)             |
| `kg·m²/(s²·A)`  | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | द्रव्यमान·लंबाई² / (समय²·धारा) के रूप में फ्लक्स (भिन्न रूप) |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)`   | वही फ्लक्स शुद्ध गुणनफल के रूप में                   |
| `mWb`           | `milli.webers`                                                        | उपसर्ग सहित फ्लक्स (मिलीवेबर)                     |
