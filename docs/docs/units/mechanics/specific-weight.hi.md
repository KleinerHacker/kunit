# विशिष्ट भार (Specific Weight)

पैकेज: `org.pcsoft.framework.kunit.mechanic.specificweight`
मूल इकाई: **न्यूटन प्रति घन मीटर**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

प्रकार: **संरचित इकाई**

विशिष्ट भार `γ` किसी पदार्थ का प्रति इकाई आयतन **भार बल** है: `γ = F / V = ρ · g`। हाइड्रोस्टैटिक्स
इसी में लिखा जाता है — किसी गहराई पर दाब बस `p = γ · h` होता है — और सिविल इंजीनियरिंग में मिट्टी
और निर्माण सामग्री के लिए इसे उद्धृत किया जाता है। पानी का विशिष्ट भार लगभग 9.81 kN/m³ है।

इसका विहित आधार-आयाम मानक रूप `mass · length⁻² · time⁻²` है।

!!! note "भार, द्रव्यमान नहीं"
    विशिष्ट भार स्थानीय गुरुत्वीय त्वरण पर निर्भर करता है; [घनत्व](density.hi.md) नहीं करता। चंद्रमा
    पर कोई पदार्थ अपना घनत्व बनाए रखता है लेकिन उसका विशिष्ट भार लगभग छठा भाग रह जाता है।

## नामित इकाइयाँ

| इकाई                            | प्रतीक     |                     टोकन | N/m³ में 1 इकाई |
|-------------------------------------|------------|--------------------------:|---------------:|
| न्यूटन प्रति घन मीटर                   | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| किलोन्यूटन प्रति घन मीटर                | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| पाउंड-बल प्रति घन फुट                   | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं। पड़ोसी बल, दाब और घनत्व समूहों की तरह, यह इंस्टेंस अपना
**कच्चा ग्राम-आधारित घटक मान** संग्रहीत करता है; N/m³ में पढ़ने पर इसे 1000 से विभाजित किया जाता है।

## विघटन

इस समूह में **दो** विघटन हैं। दोनों एक ही सामान्यीकरण फैक्ट्री में मिलते हैं:

| रूप                    | अभिव्यक्ति                                                    |
|-------------------------|------------------------------------------------------------------|
| टाइप किया गया ऑपरेटर A  | `force / volume`                                                 |
| टाइप किया गया ऑपरेटर B  | `density * acceleration` (`γ = ρ · g`)                            |
| नेटिव रूप (`toX()`)     | `(1 of kilo.grams / m² / s²).toSpecificWeight()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## समूह के साथ गणना

| अभिव्यक्ति                          | परिणाम प्रकार                    | अर्थ                  |
|-----------------------------------------|---------------------------------------|--------------------------|
| `force / volume`                       | `KSpecificWeightUnitInstance`        | `γ = F / V`              |
| `density * acceleration`               | `KSpecificWeightUnitInstance`        | `γ = ρ · g`              |
| `specificWeight * volume`              | `KForceUnitInstance`                 | भार बल                   |
| `force / specificWeight`               | `KVolumeUnitInstance`                | वह आयतन जिसे यह भरता है   |
| `specificWeight / acceleration`        | `KDensityUnitInstance`               | वापस `ρ` में              |
| `specificWeight / density`             | `KAccelerationUnitInstance`          | वापस `g` में              |

## वास्तविक उदाहरण — पानी की टंकी का भार

एक **300 लीटर** पानी की टंकी, और उसकी सामग्री द्वारा फर्श पर लगाया गया बल:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// और वापस: 1 kN भार वाला आयतन कितना है?
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## मान अर्थशास्त्र (Value Semantics)

`equals`/`hashCode` **सामान्यीकृत घटक मान** की तुलना करते हैं, इसलिए
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`। `toString()` मूल इकाई
में मान प्रदर्शित करता है: `"9807.0 N/m^3"`।

## यह भी देखें

* [घनत्व](density.hi.md) — गुरुत्व से स्वतंत्र, द्रव्यमान-आधारित समकक्ष।
* [बल](force.hi.md) और [दाब](pressure.hi.md) — पड़ोसी समूह।
* [यांत्रिकी अवलोकन](overview.hi.md)
