# झटका (जर्क)

पैकेज: `org.pcsoft.framework.kunit.kinematic.jerk`
आधार इकाई: **मीटर प्रति सेकंड घन**(`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

प्रकार: **निर्मित इकाई**

झटका (जर्क) `j` वह दर है जिससे **त्वरण** बदलता है: `j = Δa / t`। यह वही राशि है जिसे सवारी-आराम मानक वास्तव
में सीमित करते हैं — कोई लिफ्ट या ट्रेन ज़ोर से त्वरित हो सकती है, लेकिन त्वरण अचानक नहीं बदलना चाहिए, वरना
यात्री झटके से हिल जाते हैं। आराम की सीमाएँ लगभग 0.5 m/s³ के आसपास होती हैं।

इसका विहित आधार-आयाम सामान्य रूप `length · time⁻³` है।

## नामित इकाइयाँ

| इकाई                          | प्रतीक   |                        टोकन | m/s³ में 1 इकाई |
|--------------------------------|----------|-------------------------------:|-------------------:|
| मीटर प्रति सेकंड घन            | `m/s^3`  |       `metersPerSecondCubed`   |                1.0 |
| मानक गुरुत्व प्रति सेकंड       | `g/s`    | `standardGravitiesPerSecond`   |            9.80665 |
| फ़ुट प्रति सेकंड घन             | `ft/s^3` |          `feetPerSecondCubed`  |             0.3048 |

सभी टोकन हर SI उपसर्ग को स्वीकार करते हैं (`milli.metersPerSecondCubed`, आदि)।

## विघटन

इस समूह का एक विघटन है, और इसके दोनों रूप एक ही टाइप्ड, मान-समान इंस्टेंस उत्पन्न करते हैं:

| रूप                    | अभिव्यक्ति                                                         |
|--------------------------|-------------------------------------------------------------------------|
| टाइप्ड ऑपरेटर            | `acceleration / time`                                               |
| मूल (`toX()`)          | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`        |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s²(1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## समूह के साथ गणना

| अभिव्यक्ति             | परिणाम प्रकार                    | अर्थ                            |
|--------------------------|-------------------------------------|-----------------------------------|
| `acceleration / time`  | `KJerkUnitInstance`                 | `j = Δa / t`                      |
| `jerk * time`          | `KAccelerationUnitInstance`         | संचित त्वरण                       |
| `acceleration / jerk`  | `KTimeUnitInstance`                 | रैंप में कितना समय लगता है         |

## वास्तविक उदाहरण — आराम सीमा के भीतर एक लिफ्ट रैंप

एक लिफ्ट को **0.5 m/s³** के झटके से अधिक हुए बिना **1 m/s²** तक पहुँचना है। रैंप कितनी लंबी होनी चाहिए?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// और इसके उलट: 1 सेकंड की रैंप कितना झटका देती है?
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — आराम सीमा का दोगुना
```

## मान शब्दार्थ

`equals`/`hashCode` **सामान्यीकृत m/s³ मान** की तुलना करते हैं, इसलिए
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`। `toString()` मान को आधार इकाई
में प्रस्तुत करता है: `"0.6 m/s^3"`।

## यह भी देखें

* [त्वरण](acceleration.hi.md) — वह राशि जिसके परिवर्तन की दर यह इकाई दर्शाती है।
* [गति](speed.hi.md) और [दूरी](distance.hi.md) — गति श्रृंखला के शेष भाग।
* [गतिकी अवलोकन](overview.hi.md)
