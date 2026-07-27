# चुंबकीय फ्लक्स घनत्व

पैकेज: `org.pcsoft.framework.kunit.magneticfluxdensity`
आधार इकाई: **टेस्ला** (`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

प्रकार: **संरचित इकाई**

चुंबकीय फ्लक्स घनत्व (चुंबकीय प्रेरण `B`) एक **संरचित** इकाई है: संयोजन `mass · time⁻² · current⁻¹`
(`kg·s⁻²·A⁻¹`)। `KMagneticFluxDensityUnitInstance` तीन पदों वाले `KMixedUnitInstance` को लपेटता है —
घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `-2` पर `KTimeUnit.BASE` (सेकंड) और घातांक `-1` पर
`KElectricCurrentUnit.BASE` (ऐम्पियर)। चूँकि लाइब्रेरी का द्रव्यमान घटक **ग्राम** में सामान्यीकृत है
(किलोग्राम में नहीं), टेस्ला मूल घटक आधार का 1000× है; संग्रहीत मान टेस्ला में सामान्यीकृत रहता है।

## चुंबकीय फ्लक्स घनत्व बनाना

फ्लक्स घनत्व को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप
में उपलब्ध रहती हैं (`of`/`into` के साथ प्रयोग):

| फ्लक्स घनत्व | संकेत | टोकन | 1 इकाई = ? T |
|---|---|---:|---:|
| टेस्ला | `T` | `teslas` | 1.0 |
| वेबर प्रति वर्ग मीटर | `Wb/m²` | `webersPerSquareMeter` | 1.0 |
| गॉस (CGS-EMU) | `G` | `gauss` | 1.0e-4 |
| गामा | `γ` | `gammas` | 1.0e-9 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`milli.teslas`, `micro.teslas`,
`nano.teslas`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## अनेक अपघटन

चुंबकीय फ्लक्स घनत्व तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान फ्लक्स घनत्व देते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `flux / area` | `KMagneticFluxDensityUnitInstance` | परिभाषा `B = Φ / A` |
| `mass/(time²·current)` | `.toMagneticFluxDensity()` द्वारा | मूल विहित `kg·s⁻²·A⁻¹` व्यंजक |

टाइप किया गया ऑपरेटर रूप सीधे फ्लक्स घनत्व लौटाता है। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही
रहता है और `toMagneticFluxDensity()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा
`IllegalStateException` फेंकता है)। दोनों मार्ग मान‑समान हैं।

व्युत्क्रम ऑपरेटर फ्लक्स, फ्लक्स घनत्व और क्षेत्रफल को जोड़ते हैं:

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A` |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B` (क्रमविनिमेय) |
| `flux / fluxDensity` | `KAreaUnitInstance` | `A = Φ / B` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.magneticfluxdensity.*

// वास्तविक उदाहरण - MRI स्कैनर: 6 m² कॉइल से गुज़रता 18 Wb फ्लक्स 3 T का क्षेत्र है।
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance, 3 T

// मूल kg·s⁻²·A⁻¹ व्यंजक के रूप में वही फ्लक्स घनत्व:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// पृथ्वी का 50 µT चुंबकीय क्षेत्र 2 m² लूप से गुज़रकर 1e-4 Wb फ्लक्स देता है।
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance, 1e-4 Wb
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `T` | `teslas` | फ्लक्स घनत्व, आधार इकाई (नामित टोकन, टेस्ला) |
| `Wb/m²` | `webersPerSquareMeter` | फ्लक्स प्रति क्षेत्रफल के रूप में फ्लक्स घनत्व (नामित टोकन) |
| `kg/(s²·A)` | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | द्रव्यमान / (समय²·धारा) के रूप में फ्लक्स घनत्व (भिन्न रूप) |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | वही फ्लक्स घनत्व शुद्ध गुणनफल के रूप में |
| `µT` | `micro.teslas` | उपसर्ग सहित फ्लक्स घनत्व (माइक्रोटेस्ला) |
