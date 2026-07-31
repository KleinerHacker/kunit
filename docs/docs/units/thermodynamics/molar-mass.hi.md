# मोलर द्रव्यमान

पैकेज: `org.pcsoft.framework.kunit.thermo.molarmass`
मूल इकाई: **ग्राम प्रति मोल** (`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`)

प्रकार: **संघटित इकाई**

मोलर द्रव्यमान प्रति पदार्थ की मात्रा द्रव्यमान है: `mass / amountOfSubstance` (`g/mol`)। यह स्थूल जगत (तराजू पर ग्राम)
और कण जगत (मोल) के बीच सेतु है, और संख्यात्मक रूप से किसी पदार्थ के सापेक्ष परमाणु या आणविक द्रव्यमान के बराबर होता है।

`KMolarMassUnitInstance` विहित सामान्य रूप `mass¹ · substance⁻¹` (`g·mol⁻¹`) में ठीक दो पदों वाले
`KMixedUnitInstance` को लपेटता है, जो हमेशा g/mol में सामान्यीकृत रहता है। चूँकि लाइब्रेरी द्रव्यमानों को ग्राम में
सामान्यीकृत करती है, कच्चा घटक आधार *ही* नामित मूल इकाई है — कोई सेतु गुणक शामिल नहीं है।

घनत्व से विभाजित करने पर यह [मोलर आयतन](molar-volume.md) बन जाती है; [आवर्त सारणी](../../periodic-table.md)
का हर तत्व अपना मोलर द्रव्यमान इस समूह के एक मान के रूप में उपलब्ध कराता है।

## नामित इकाइयाँ

| इकाई           | संकेत        |                  टोकन | 1 इकाई = ? g/mol |
|---------------|------------|---------------------:|----------------:|
| ग्राम प्रति मोल      | `g/mol`    |       `gramsPerMole` |             1.0 |
| किलोग्राम प्रति मोल    | `kg/mol`   |   `kilogramsPerMole` |          1000.0 |
| पाउंड प्रति पाउंड-मोल | `lb/lbmol` | `poundsPerPoundMole` |             1.0 |
| डाल्टन प्रति इकाई    | `Da`       |   `daltonsPerEntity` |   1.00000000105 |

पाउंड-मोल को इस तरह परिभाषित किया गया है कि पाउंड में इसका द्रव्यमान मोलर द्रव्यमान के बराबर हो, जिससे
`lb/lbmol` संख्यात्मक रूप से `g/mol` के समान होता है। 2019 की SI पुनर्परिभाषा के बाद से मोलर द्रव्यमान स्थिरांक अब ठीक 1
g/mol नहीं रहा, इसलिए डाल्टन गुणक। सभी इकाइयों में पूर्ण SI उपसर्ग सीमा समर्थित है (`kilo.gramsPerMole`,
`milli.kilogramsPerMole`, …)।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 18.015 Da प्रति अणु
```

## वास्तविक उदाहरण: एक मोल तौलना

एक नुस्खे में 0.25 mol टेबल नमक (NaCl, 58.44 g/mol) चाहिए। कितना तौलना होगा — और 500 g पैकेट में कितने मोल होते हैं?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// 0.25 mol में कितना द्रव्यमान है?
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// 500 g पैकेट में कितने मोल हैं?
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// और खुद मोलर द्रव्यमान, तौले गए नमूने से मापा गया:
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## मूल इकाइयों (द्रव्यमान और पदार्थ की मात्रा) से गणना

| व्यंजक                             | परिणाम प्रकार                         | अर्थ                          |
|---------------------------------|----------------------------------|-----------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | मोलर द्रव्यमान                    |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | कुल द्रव्यमान                     |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | कुल द्रव्यमान (क्रमविनिमेय)            |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | शामिल पदार्थ की मात्रा                |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [मोलर आयतन](molar-volume.md) |

## अपघटन

दोनों अपघटन समान टाइप, मान-समान इंस्टेंस उत्पन्न करते हैं।

| अपघटन                      | रूप                       | परिणाम                        |
|----------------------------|--------------------------|-----------------------------|
| `mass / amountOfSubstance` | टाइप किया गया संकारक           | सीधे `KMolarMassUnitInstance` |
| `mass · substance⁻¹`       | मूल व्यंजक + `toMolarMass()` | `KMolarMassUnitInstance`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// टाइप किया गया संकारक रूप
val typed = (18.015 of grams) / (1 of moles)

// मूल आधार-आयाम रूप (g·mol⁻¹), toMolarMass() द्वारा पहचाना गया
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true - दोनों 18.015 g/mol हैं
```

`toMolarMass()` **केवल** विहित सामान्य रूप को पहचानता है; एक गलत रूप `IllegalStateException`
फेंकता है।

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को
भिन्न और ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित           | Kotlin                               | अर्थ                         |
|---------------|--------------------------------------|----------------------------|
| `g/mol`       | `gramsPerMole`                       | मोलर द्रव्यमान, मूल इकाई — नामित टोकन |
| `g·mol⁻¹`     | `grams / moles`                      | वही राशि आधार आयामों में            |
| `kg/mol`      | `kilogramsPerMole`                   | किलोग्राम प्रति मोल                 |
| `Da`          | `daltonsPerEntity`                   | प्रति मौलिक इकाई डाल्टन             |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | द्रव्यमान ÷ मात्रा से मोलर द्रव्यमान       |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | मोलर द्रव्यमान × मात्रा से द्रव्यमान       |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | द्रव्यमान ÷ मोलर द्रव्यमान से मात्रा       |
| `V_m = M / ρ` | `molarMass / density`                | मोलर द्रव्यमान ÷ घनत्व से मोलर आयतन  |
