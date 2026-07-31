# ऊष्मा प्रवाह

पैकेज: `org.pcsoft.framework.kunit.common.power`
मूल इकाई: **वाट** (`KPowerUnit.BASE == KPowerUnit.WATT`)

प्रकार: **संघटित इकाई**

ऊष्मा प्रवाह `Q̇` (जिसे ऊष्मीय शक्ति या ऊष्मा दर भी कहा जाता है) प्रति इकाई समय में स्थानांतरित ऊष्मा की मात्रा है: `W`।
यह [शक्ति](power.md) — यानी प्रति समय ऊर्जा — के **आयामिक और भौतिक रूप से समान**
है, और इसीलिए KUnit इसे `KPowerUnitInstance` से मॉडल करता है।

## ऊष्मा प्रवाह का अपना कोई प्रकार क्यों नहीं है

ऊष्मा प्रवाह कोई अलग राशि नहीं है, यह ऐसी शक्ति है जो संयोगवश ऊष्मीय है। इसका ठीक एक विहित सामान्य रूप है
`mass¹ · distance² · time⁻³`, और उस पर एक दूसरा प्रकार बिना कोई भौतिकी जोड़े `toPower()` को अस्पष्ट बना देगा। एक वाट
किसी इलेक्ट्रिक मोटर, लेज़र या रेडिएटर का वर्णन करता है या नहीं, यह संदर्भ है, आयाम नहीं।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // यांत्रिक शक्ति
val radiator = 1500 of watts    // ऊष्मा प्रवाह
// दोनों KPowerUnitInstance हैं
```

## वास्तविक उदाहरण: एक रेडिएटर

1500 W रेटेड एक रेडिएटर 4 घंटे तक चलता है। यह कितनी ऊर्जा पहुँचाता है, और अपनी 0.6 m² सतह पर यह कितनी ऊष्मा प्रवाह घनत्व
उत्पन्न करता है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ (= 6 kWh)

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## इस क्षेत्र में ऊष्मा प्रवाह कहाँ दिखाई देता है

| व्यंजक                      | परिणाम प्रकार                       | अर्थ                                 |
|--------------------------|--------------------------------|------------------------------------|
| `energy / time`          | `KPowerUnitInstance`           | ऊष्मा ÷ अवधि से ऊष्मा प्रवाह                  |
| `power * time`           | `KEnergyUnitInstance`          | एक अवधि में पहुँचाई गई ऊष्मा                |
| `power / area`           | `KHeatFluxDensityUnitInstance` | [ऊष्मा प्रवाह घनत्व](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance`           | किसी सतह से कुल ऊष्मा प्रवाह                 |

एक दीवार की ऊष्मा हानि एक शास्त्रीय शृंखला है: एक
[ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.md) को एक तापमान अंतर से गुणा करने पर
[ऊष्मा प्रवाह घनत्व](heat-flux-density.md) मिलता है, और उसे क्षेत्रफल से गुणा करने पर वाट में ऊष्मा प्रवाह मिलता है।

## यह भी देखें

* [शक्ति](power.md) — वह प्रकार जो ऊष्मा प्रवाह साझा करती है, पूर्ण इकाई तालिका, सभी अपघटन और संपूर्ण संकारक सतह सहित
* [ऊष्मा प्रवाह घनत्व](heat-flux-density.md) — प्रति इकाई क्षेत्रफल ऊष्मा प्रवाह
* [ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.md) — प्रति केल्विन ऊष्मा प्रवाह घनत्व
* [ऊर्जा](energy.md) — समय के साथ समाकलित ऊष्मा प्रवाह

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह राशि गणितीय रूप से कैसे लिखी जाती है बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड
सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है।

| गणित         | Kotlin                                     | अर्थ                          |
|-------------|--------------------------------------------|-----------------------------|
| `W`         | `watts`                                    | ऊष्मा प्रवाह, मूल इकाई (शक्ति के साथ साझा) |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | वही राशि आधार आयामों में             |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime`         | ऊष्मा ÷ अवधि से ऊष्मा प्रवाह           |
| `Q = Q̇ · t` | `radiator * runtime`                       | ऊष्मा प्रवाह × अवधि से ऊष्मा           |
| `q̇ = Q̇ / A` | `radiator / surface`                       | ऊष्मा प्रवाह ÷ क्षेत्रफल से ऊष्मा प्रवाह घनत्व  |
