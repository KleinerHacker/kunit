# शक्ति (विद्युत)

पैकेज: `org.pcsoft.framework.kunit.common.power`
आधार इकाई: **वाट** (`KPowerUnit.BASE == KPowerUnit.WATT`)

प्रकार: **संरचित इकाई**

शक्ति एक **संरचित** इकाई है: संयोजन `mass · length² · time⁻³` (`kg·m²·s⁻³`)। `KPowerUnitInstance` तीन पदों वाले
`KMixedUnitInstance` को लपेटता है — घातांक `+1` पर `KMassUnit.BASE` (ग्राम), घातांक `+2` पर
`KDistanceUnit.BASE` (मीटर) और घातांक `-3` पर `KTimeUnit.BASE` (सेकंड)। चूँकि लाइब्रेरी का द्रव्यमान घटक **ग्राम** में
सामान्यीकृत है (किलोग्राम में नहीं), वाट तक पहुँचने के लिए विहित गुणनफल को 1000 से विभाजित किया जाता है; संग्रहीत मान
हमेशा वाट में सामान्यीकृत रहता है।

शक्ति तकनीकी रूप से **एक** राशि है जो कई विषय‑क्षेत्रों में प्रकट होती है। यह पृष्ठ इसके *विद्युत* पठन (`P = U · I`) का
वर्णन करता है। वही Kotlin समूह अन्य क्षेत्रों के लिए
[शक्ति (यांत्रिकी)](../mechanics/power.md) और [शक्ति (ऊष्मागतिकी)](../thermodynamics/power.md) में प्रलेखित है।

## शक्ति बनाना

शक्ति को नामित टोकन से या किसी अपघटन (नीचे देखें) से बनाया जाता है। नामित इकाइयाँ मान‑1 टोकन के रूप में उपलब्ध रहती हैं
(`of`/`into` के साथ प्रयोग):

| शक्ति               | संकेत     |                     टोकन |       1 इकाई = ? W |
|------------------|---------|------------------------:|------------------:|
| वाट               | `W`     |                 `watts` |               1.0 |
| मीट्रिक अश्वशक्ति         | `PS`    |     `metricHorsePowers` |         735.49875 |
| यांत्रिक अश्वशक्ति         | `hp`    | `mechanicalHorsePowers` | 745.6998715822702 |
| अर्ग प्रति सेकंड (CGS)  | `erg/s` |         `ergsPerSecond` |            1.0e-7 |
| वोल्ट ऐम्पियर (प्रकट शक्ति) | `VA`    |           `voltAmperes` |               1.0 |
| वोल्ट ऐम्पियर रिएक्टिव     | `var`   |                  `vars` |               1.0 |

नामित इकाइयाँ `KPrefixBuilder` के माध्यम से SI उपसर्गों का समर्थन करती हैं (`kilo.watts`, `mega.watts`,
`milli.watts`, …)।

### प्रकट और रिएक्टिव शक्ति (VA, var)

प्रत्यावर्ती धारा प्रणालियों में तीन शक्तियों को अलग किया जाता है, जो सभी वाट के विमीय रूप से समान हैं:

* **सक्रिय शक्ति** `P = U · I · cos φ` वाट (`W`) में — वह भाग जो कार्य करता है,
* **प्रकट शक्ति** `S = U · I` वोल्ट ऐम्पियर (`VA`) में — RMS वोल्टता और RMS धारा का गुणनफल,
* **रिएक्टिव शक्ति** `Q = U · I · sin φ` वोल्ट ऐम्पियर रिएक्टिव (`var`) में — वह भाग जो स्रोत और भार के बीच बिना कार्य
  किए दोलन करता है।

चूँकि तीनों केवल परंपरा में भिन्न हैं, KUnit उन्हें इसी एक समूह में रखता है और प्रतीक से अलग करता है:
`1 VA = 1 var = 1 W`। उपसर्ग सामान्य रूप से कार्य करते हैं, इसलिए `kilo.voltAmperes` 1 kVA है और
`kilo.vars` 1 kvar है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

// 25 kVA रेटेड ट्रांसफार्मर एक भार को खिलाते हुए जिसका पावर फैक्टर cos φ = 0.8 है:
val s = 25 of kilo.voltAmperes
val p = (25 * 0.8) of kilo.watts     // 20 kW सक्रिय शक्ति
val q = (25 * 0.6) of kilo.vars      // 15 kvar रिएक्टिव शक्ति
s into kilo.voltAmperes               // 25.0
q into kilo.vars                      // 15.0
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## अनेक अपघटन

शक्ति तक कई **समतुल्य अपघटनों** से पहुँचा जा सकता है, और सभी मान‑समान शक्ति देते हैं:

| व्यंजक                  | परिणाम प्रकार             | अर्थ                               |
|----------------------|----------------------|----------------------------------|
| `voltage * current`  | `KPowerUnitInstance` | विद्युत शक्ति `P = U · I` (क्रमविनिमेय)      |
| `force * speed`      | `KPowerUnitInstance` | यांत्रिक शक्ति `P = F · v` (क्रमविनिमेय)      |
| `energy / time`      | `KPowerUnitInstance` | `P = W / t` (देखें [ऊर्जा](energy.md)) |
| `mass·length²/time³` | `.toPower()` द्वारा      | मूल विहित `kg·m²·s⁻³` व्यंजक           |

टाइप किए गए ऑपरेटर रूप सीधे शक्ति लौटाते हैं। पूर्णतः मूल व्यंजक सामान्य `KMixedUnitInstance` ही रहता है और
`toPower()` से संकुचित किया जाता है (जो केवल विहित रूप को पहचानता है, अन्यथा `IllegalStateException`
फेंकता है)। सभी मार्ग मान‑समान हैं।

विद्युत रूप के व्युत्क्रम ऑपरेटर वोल्टता, धारा और शक्ति को जोड़ते हैं:

| व्यंजक               | परिणाम प्रकार                       | अर्थ          |
|-------------------|--------------------------------|-------------|
| `power / current` | `KVoltageUnitInstance`         | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.common.power.*

// वास्तविक उदाहरण - एक मेन सॉकेट: 10 A पर 230 V, 2.3 kW देता है।
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// 230 V पर 2.3 kW भार द्वारा खींची गई धारा के लिए हल की गई परिभाषा:
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance, 10 A

// मूल kg·m²·s⁻³ व्यंजक के रूप में वही शक्ति:
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
```

## ऑपरेटर

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (समूह से बाहर)
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

(1 of kilo.watts).toString()     // "1000.0 W" (आधार इकाई)
```

## संकेतन

नीचे दी गई तालिका दिखाती है कि यह इकाई और इसके घटक गणितीय रूप से कैसे लिखे जाते हैं बनाम KUnit के साथ Kotlin में कैसे।
घातांक यूनिकोड सुपरस्क्रिप्ट (`²`, `⁻³`) से लिखे जाते हैं, `·` गुणा और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांक वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित                 | Kotlin                                            | अर्थ                                 |
|---------------------|---------------------------------------------------|------------------------------------|
| `W`                 | `watts`                                           | शक्ति, आधार इकाई (नामित टोकन, वाट)          |
| `U · I`             | `(230 of volts) * (10 of amperes)`                | वोल्टता और धारा से विद्युत शक्ति                 |
| `kg·m²/s³`          | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | द्रव्यमान·लंबाई² / समय³ के रूप में शक्ति (भिन्न रूप) |
| `kg·m²·s⁻³`         | `kilo.grams * (meters pow 2) * (seconds pow -3)`  | वही शक्ति शुद्ध गुणनफल के रूप में              |
| `kW`                | `kilo.watts`                                      | उपसर्ग सहित शक्ति (किलोवाट)                 |
| `S = U · I` in `VA` | `voltAmperes`                                     | प्रकट शक्ति (प्रत्यावर्ती धारा)                   |
| `Q` in `var`        | `vars`                                            | रिएक्टिव शक्ति (प्रत्यावर्ती धारा)                  |
| `kVA`               | `kilo.voltAmperes`                                | उपसर्ग सहित प्रकट शक्ति (किलोवोल्ट ऐम्पियर)        |
| `kvar`              | `kilo.vars`                                       | उपसर्ग सहित रिएक्टिव शक्ति                   |
