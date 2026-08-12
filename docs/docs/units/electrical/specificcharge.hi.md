# विशिष्ट आवेश

पैकेज: `org.pcsoft.framework.kunit.electric.specificcharge`
आधार इकाई: **कूलॉम प्रति किलोग्राम**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

प्रकार: **संरचित इकाई**

विशिष्ट आवेश `q/m`, किसी वस्तु के प्रति इकाई द्रव्यमान पर वहन किया गया आवेश है। यह वही राशि है जिसे जे. जे. थॉमसन ने
इलेक्ट्रॉन की पहचान के लिए मापा था, और यही वह राशि है जिसके आधार पर द्रव्यमान स्पेक्ट्रोमेट्री कणों को अलग करती है।

इसका विहित आधार-आयाम मानक रूप `current · time · mass⁻¹` है।

!!! note "एक समूह, दो पठन"
    यही आयाम विकिरण सुरक्षा के **आयनीकरण खुराक** (एक्सपोज़र) को भी दर्शाता है, जिसे ऐतिहासिक रूप से रॉन्टजन में मापा
    जाता था — देखें [एक्सपोज़र](../thermodynamics/exposure.hi.md)। एक ही मानक रूप एक ही प्रकार पर मैप होता है,
    इसलिए दोनों पठन इस समूह को साझा करते हैं; रॉन्टजन इसकी नामित इकाइयों में से एक है। अपने मानों को नाम देकर इन्हें अलग करें।

## नामित इकाइयाँ

| इकाई                 | प्रतीक |                 टोकन | 1 इकाई C/kg में |
|----------------------|--------|----------------------:|---------------:|
| कूलॉम प्रति किलोग्राम | `C/kg` | `coulombsPerKilogram` |            1.0 |
| रॉन्टजन              | `R`    |            `roentgens` |        2.58e-4 |

सभी टोकन हर SI उपसर्ग स्वीकार करते हैं (`milli.roentgens`, …)।

## स्थिरांक

| स्थिरांक                     | मान                  | अर्थ                                     |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | इलेक्ट्रॉन का आवेश-द्रव्यमान अनुपात  |

चिह्न छोड़ दिया गया है: इलेक्ट्रॉन का आवेश ऋणात्मक है, लेकिन अनुपात परिमाण के रूप में दिया गया है।

## अपघटन

इस समूह का एक अपघटन है, और इसके दोनों रूप एक ही मान-समान टाइप्ड इंस्टेंस बनाते हैं। मूल रूप **यूनिट टेम्पलेट्स**
से जोड़ा जाता है क्योंकि समूह में द्रव्यमान पद है।

| रूप              | व्यंजक                                               |
|------------------|----------------------------------------------------------|
| टाइप्ड ऑपरेटर    | `charge / mass`                                         |
| मूल (`toX()`)    | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## समूह के साथ गणना

| व्यंजक                       | परिणाम प्रकार                     | अर्थ              |
|-----------------------------|----------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | कुल आवेश     |
| `charge / specificCharge`   | `KMassUnitInstance`             | वहन करने वाला द्रव्यमान    |

## वास्तविक उदाहरण — इलेक्ट्रॉन, और एक एक्सपोज़र रीडिंग

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// थॉमसन का अनुपात
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// एक सर्वे मीटर की एक्सपोज़र रीडिंग, और 1 kg वायु में मुक्त होने वाला आवेश
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत C/kg मान** की तुलना करते हैं, इसलिए
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)` सत्य है। `toString()` मान को आधार इकाई में दर्शाता है:
`"1.0 C/kg"`।

## यह भी देखें

* [आवेश](charge.hi.md) और [द्रव्यमान](../mechanics/mass.hi.md) — दो संकारक।
* [एक्सपोज़र](../thermodynamics/exposure.hi.md) — आयनीकरण खुराक के रूप में पढ़ा गया वही प्रकार।
* [इलेक्ट्रिकल इंजीनियरिंग अवलोकन](overview.hi.md)
