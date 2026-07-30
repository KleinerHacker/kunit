# ऊष्मा प्रवाह घनत्व

पैकेज: `org.pcsoft.framework.kunit.thermo.heatfluxdensity`
मूल इकाई: **वाट प्रति वर्ग मीटर** (`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`)

प्रकार: **संघटित इकाई**

ऊष्मा प्रवाह घनत्व प्रति इकाई क्षेत्रफल ऊष्मा प्रवाह है: `power / area` (`W/m²`)। वही इकाई
*विकिरण तीव्रता (irradiance)* और *विकिरण उत्सर्जन (radiant exitance)* को मापती है — किसी सतह पर आने
या उससे निकलने वाले विकिरण की तीव्रता।

`KHeatFluxDensityUnitInstance` विहित सामान्य रूप `mass¹ · time⁻³` (`kg·s⁻³`) में ठीक दो पदों वाले
`KMixedUnitInstance` को लपेटता है, जो हमेशा W/m² में सामान्यीकृत रहता है।

!!! note "दूरी आयाम रद्द हो जाता है"
    `W/m² = kg·m²·s⁻³/m² = kg·s⁻³`। इसलिए विहित सामान्य रूप में **कोई** दूरी पद नहीं है।

कुल ऊष्मा प्रवाह स्वयं एक साधारण [शक्ति](power.md) है; देखें
[ऊष्मा प्रवाह](heat-flow.md)। किसी तापमान अंतर से विभाजित करने पर यह एक
[ऊष्मा स्थानांतरण गुणांक](heat-transfer-coefficient.md) बन जाता है।

## नामित इकाइयाँ

| इकाई | संकेत | टोकन | 1 इकाई = ? W/m² |
|---|---|---:|---:|
| वाट प्रति वर्ग मीटर | `W/m²` | `wattsPerSquareMeter` | 1.0 |
| Btu प्रति घंटा-वर्ग फुट | `Btu/(h·ft²)` | `btusPerHourSquareFoot` | ≈ 3.15459 |
| कैलोरी प्रति सेकंड-वर्ग सेंटीमीटर | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` | 41840.0 |

सभी में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.wattsPerSquareMeter`, `milli.wattsPerSquareMeter`, …)।

## सौर स्थिरांक

यह समूह औसत बाह्य-पृथ्वी सौर विकिरण तीव्रता को `SOLAR_CONSTANT` (1361 W/m²) के रूप में, एक सादे
`Double` के रूप में उजागर करता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## वास्तविक उदाहरण: एक सौर सरणी का आकार निर्धारण

एक छत पर साफ़ दिन में 800 W/m² प्राप्त होता है। सरणी 25 m² को कवर करती है और घटित विकिरण का 20 %
परिवर्तित करती है। यह कितनी विद्युत शक्ति देती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // अदिश मापन प्रकार को बनाए रखता है
electrical into kilo.watts                 // 4.0 kW

// उलटा: 20 % दक्षता पर 10 kW विद्युत के लिए कितना छत क्षेत्रफल चाहिए?
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## मूल इकाइयों (शक्ति और क्षेत्रफल) से गणना

| व्यंजक | परिणाम प्रकार | अर्थ |
|---|---|---|
| `power / area` | `KHeatFluxDensityUnitInstance` | ऊष्मा प्रवाह घनत्व |
| `heatFluxDensity * area` | `KPowerUnitInstance` | कुल ऊष्मा प्रवाह |
| `area * heatFluxDensity` | `KPowerUnitInstance` | कुल ऊष्मा प्रवाह (क्रमविनिमेय) |
| `power / heatFluxDensity` | `KAreaUnitInstance` | जिस क्षेत्रफल पर यह फैला है |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन | रूप | परिणाम |
|---|---|---|
| `power / area` | टाइप किया गया संकारक | सीधे `KHeatFluxDensityUnitInstance` |
| `mass · time⁻³` | मूल व्यंजक + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true - दोनों 1.0 W/m² हैं
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे। घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित | Kotlin | अर्थ |
|---|---|---|
| `W/m²` | `wattsPerSquareMeter` | ऊष्मा प्रवाह घनत्व, मूल इकाई — नामित टोकन |
| `kg·s⁻³` | `grams / (seconds pow 3)` | वही राशि आधार आयामों में |
| `kW/m²` | `kilo.wattsPerSquareMeter` | किलोवाट प्रति वर्ग मीटर |
| `E_0` | `SOLAR_CONSTANT of wattsPerSquareMeter` | सौर स्थिरांक, 1361 W/m² |
| `q̇ = P / A` | `(1000 of watts) / roof` | शक्ति ÷ क्षेत्रफल से प्रवाह घनत्व |
| `P = q̇ · A` | `irradiance * roof` | प्रवाह घनत्व × क्षेत्रफल से शक्ति |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance` | शक्ति ÷ प्रवाह घनत्व से क्षेत्रफल |
