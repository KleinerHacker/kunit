# तापीय विसरणशीलता

पैकेज: `org.pcsoft.framework.kunit.common.diffusivity`
मूल इकाई: **वर्ग मीटर प्रति सेकंड** (`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

प्रकार: **संघटित इकाई**

तापीय विसरणशीलता `α` बताती है कि तापमान परिवर्तन किसी सामग्री में कितनी *तेजी* से फैलता है — इसके
विपरीत [तापीय चालकता](thermal-conductivity.md) बताती है कि स्थिर अवस्था में *कितनी* ऊष्मा प्रवाहित होती है। इकाई:
`m²/s`। इसे इस प्रकार परिभाषित किया जाता है

```
α = λ / (ρ · c_p)
```

`KDiffusivityUnitInstance` एक `KMixedUnitInstance` को लपेटता है जिसमें विहित सामान्य रूप
`distance² · time⁻¹` (`m²·s⁻¹`) में ठीक दो पद होते हैं, जो हमेशा m²/s में सामान्यीकृत रहता है।

!!! note "पैकेज नाम बनाम क्लास नाम"
पैकेज `thermo.diffusivity` है, `thermo.thermaldiffusivity` नहीं — किसी यूनिट पैकेज को अपने फील्ड पैकेज का नाम दोहराना
नहीं चाहिए। टाइप्स पूरा तकनीकी शब्द बनाए रखते हैं (`KDiffusivityUnitInstance`)। आयाम `m²/s` गतिज श्यानता (kinematic
viscosity) और द्रव्यमान विसरणशीलता के साथ साझा किया जाता है; यह समूह तापीय राशि का मॉडल बनाता है।

## नामित इकाइयाँ

| इकाई             | संकेत     |                          टोकन | m²/s में 1 इकाई |
|-----------------|---------|-----------------------------:|-------------:|
| वर्ग मीटर प्रति सेकंड   | `m²/s`  |      `squareMetersPerSecond` |          1.0 |
| वर्ग मिलीमीटर प्रति सेकंड | `mm²/s` | `squareMillimetersPerSecond` |         1e-6 |
| वर्ग फुट प्रति घंटा     | `ft²/h` |          `squareFeetPerHour` | ≈ 2.58064e-5 |

सामग्री तालिकाएँ `α` को mm²/s में सूचीबद्ध करती हैं, जो ठीक `micro.squareMetersPerSecond` है। सभी इकाइयों में पूर्ण SI
उपसर्ग सीमा समर्थित है।

## विशिष्ट मान

| सामग्री    |            α |
|--------|-------------:|
| तांबा     |  ≈ 116 mm²/s |
| स्टील     |   ≈ 14 mm²/s |
| कांच     | ≈ 0.34 mm²/s |
| पानी     | ≈ 0.14 mm²/s |
| खनिज ऊन |  ≈ 1.2 mm²/s |

## वास्तविक उदाहरण: तांबा कितनी तेजी से संतुलित होता है

तांबे का λ = 401 W/ (m·K), ρ = 8960 kg/m³ और c_p = 385 J/ (kg·K) है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// उलटा: विसरणशीलता से चालकता वापस प्राप्त करें
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## पड़ोसी इकाइयों के साथ गणना

परिभाषित संबंध **त्रिआधारी (ternary)** है (`α = λ / (ρ · c_p)`), इसलिए यहाँ के हर दूसरे समूह के विपरीत यह वॉल्यूमेट्रिक
ऊष्मा क्षमता `ρ · c_p` (J/ (m³·K)) के लिए एक मध्यवर्ती टाइप बनाए बिना एकल द्विआधारी संकारक नहीं हो सकता, जिसे यह
लाइब्रेरी मॉडल नहीं करती। इसलिए यह संबंध नामित, दृढ़ता से टाइप किए गए फ़ंक्शनों के रूप में उजागर किया गया है:

| फ़ंक्शन                                                                  | परिणाम प्रकार                            | अर्थ                  |
|----------------------------------------------------------------------|-------------------------------------|---------------------|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KDiffusivityUnitInstance`          | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance`  | `λ = α · ρ · c_p`   |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance`              | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

चारों ही हर दूसरे अपघटन की तरह उसी सामान्यीकरण फैक्ट्री में जाते हैं।

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                | रूप                              | परिणाम                       |
|----------------------|---------------------------------|----------------------------|
| `λ / (ρ · c_p)`      | टाइप किया गया फ़ंक्शन `diffusivityWith` | `KDiffusivityUnitInstance` |
| `distance² · time⁻¹` | मूल व्यंजक + `toDiffusivity()`      | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K), ρ = 1 kg/m³, c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native // true - दोनों 1.0 m²/s हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित                 | Kotlin                                                  | अर्थ                         |
|---------------------|---------------------------------------------------------|----------------------------|
| `m²/s`              | `squareMetersPerSecond`                                 | तापीय विसरणशीलता, मूल इकाई        |
| `m²·s⁻¹`            | `(meters pow 2) / seconds`                              | वही राशि आधार आयामों में            |
| `mm²/s`             | `squareMillimetersPerSecond`                            | वर्ग मिलीमीटर प्रति सेकंड (सामग्री तालिकाएँ) |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)`           | परिभाषित संबंध                  |
| `λ = α · ρ · c_p`   | `alpha.conductivityWith(density, heat)`                 | विसरणशीलता से चालकता             |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)`                 | विसरणशीलता से घनत्व              |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | विसरणशीलता से विशिष्ट ऊष्मा           |
