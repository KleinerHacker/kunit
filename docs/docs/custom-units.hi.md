# कस्टम इकाइयाँ जोड़ना

kunit आज कई इकाई समूह शिप करता है ([दूरी](units/kinematics/distance.md),
[समय](units/kinematics/time.md), [भंडारण](units/information/storage.md),
[चाल](units/kinematics/speed.md), [डेटा दर](units/information/datarate.md)), लेकिन समूचा इंजन
(`KUnit`, `KMixedUnitInstance`, `of`/`into` क्रियाएँ, उपसर्ग बिल्डर) सामान्य और समूह-निरपेक्ष है। एक नई
भौतिक राशि जोड़ने का अर्थ है वही पैटर्न अपनाना। यह पृष्ठ एक प्रदर्शनकारी **द्रव्यमान** समूह
(`org.pcsoft.framework.kunit.mass`) जोड़ने से गुज़रता है — भंडारण समूह पर आधारित एक सादा, एक-विमीय समूह।

## 1. उप-पैकेज और `KUnit` enum बनाएँ

प्रत्येक इकाई समूह को `org.pcsoft.framework.kunit` के अंतर्गत अपना उप-पैकेज मिलता है, और इसकी इकाइयाँ
`KUnit` को लागू करने वाले `enum class` के रूप में घोषित होती हैं। `baseValue` समूह की मूल इकाई में
रूपांतरण गुणक है — मूल इकाई का स्वयं `baseValue == 1.0` होता है।

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** किलोग्राम, द्रव्यमान की SI मूल इकाई; परिभाषा से [baseValue] = 1.0। */
    KILOGRAM("kg", 1.0),

    /** ग्राम, 1 g = 0.001 kg। */
    GRAM("g", 0.001),

    /** अंतरराष्ट्रीय एवोर्डुपॉइस पाउंड, 1 lb = 0.45359237 kg। */
    POUND("lb", 0.45359237),

    /** अंतरराष्ट्रीय एवोर्डुपॉइस औंस, 1 oz = 0.028349523125 kg। */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** द्रव्यमान समूह की मूल इकाई; [KMassUnitInstance] के सभी आंतरिक मान इसी इकाई में प्रसामान्यीकृत होते हैं। */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. रैपर वर्ग बनाएँ

रैपर (`KMassUnitInstance`) एक `KMixedUnitInstance` को **प्रत्यायोजन** (`KUnitMeasurable by instance`) से
समाहित करता है और `KUnitInstance<KMassUnitInstance>` को लागू करता है। यह केवल `KUnitInstance`-मात्र सदस्यों
(`plus`/`minus`/`compareTo`) के साथ-साथ `scaledBy` ओवरराइड (जो `of` को समर्थन देता है) और
`equals`/`hashCode`/`toString` को हाथ से लिखता है। कोई `valueAs`/`toString(target)` **नहीं** है — पढ़ना
समूह-निरपेक्ष `into` क्रिया है। `KStorageUnitInstance` का आकार कॉपी करें।

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** `of` को समर्थन देता है: मान (किलोग्राम) को मापता है, वही प्रकार लौटाता है। */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** किलोग्राम ([KMassUnit.BASE]) में पहले से व्यक्त मान से एक [KMassUnitInstance] बनाता है। */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** एक शुद्ध-द्रव्यमान [KMixedUnitInstance] को [KMassUnitInstance] में वापस बदलता है, [KMassUnit.BASE] में प्रसामान्यीकृत करते हुए। */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. मान-1 नंगे टोकन और उपसर्ग-बिल्डर गुण जोड़ें

परियोजना परंपरा के अनुसार DSL शब्दावली को दो फ़ाइलों में बाँटें: मान-1 नंगे टोकन `K...UnitBareValues.kt`
में जाते हैं, और उपसर्ग-बिल्डर गुण विस्तार `K...UnitExtensions.kt` में। साथ मिलकर वे कॉलर को
`5 of kilograms` या `5 of kilo.grams` लिखने और `into` से वापस पढ़ने देते हैं।

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 किलोग्राम ([KMassUnit.KILOGRAM])। */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 ग्राम ([KMassUnit.GRAM])। */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 पाउंड ([KMassUnit.POUND])। */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 औंस ([KMassUnit.OUNCE])। */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt` (द्रव्यमान कोई भी परिमाण स्वीकारता है, इसलिए गुण सामान्य आधार `KPrefixBuilder`
पर लटकते हैं):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** उपसर्ग-युक्त किलोग्राम, जैसे `kilo.kilograms`। */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** उपसर्ग-युक्त ग्राम, जैसे `milli.grams` = 1 mg। */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** उपसर्ग-युक्त पाउंड। */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** उपसर्ग-युक्त औंस। */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

बस इतना ही — यह पहले से ही आपको पूर्ण `+`, `-`, `*`, `/`, तुलनाएँ, SI उपसर्ग बिल्डर (`5 of milli.grams`),
और `toUnit()`/`toMass()` राउंड-ट्रिपिंग मुफ़्त में देता है।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance, किलोग्राम में प्रसामान्यीकृत
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4. (वैकल्पिक) विशेष/व्युत्पन्न इकाइयाँ जोड़ें

यदि आपके समूह में किसी विशिष्ट मापन से बँधी सामान्यतः प्रयुक्त नामित इकाइयाँ हैं (जैसे क्षेत्रफल के लिए
हेक्टेयर), तो उन्हें नामित मान-1 इंस्टेंस के रूप में जोड़ें — किसी अलग लक्ष्य प्रकार की आवश्यकता नहीं:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 मीट्रिक टन (1000 kg)। */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. अन्य समूहों के साथ संयोजित करें

चूँकि सब कुछ अंततः सामान्य `KMixedUnitInstance` इंजन से होकर गुज़रता है, आपका नया समूह तुरंत किसी भी अन्य
समूह के साथ `*`/`/` द्वारा संयोजित हो जाता है — नियमों के लिए [मिश्रित इकाइयाँ](mixed-units.md) देखें।
किसी प्रबल-प्रकार पार-समूह परिणाम (जैसे `mass / volume = density`) के लिए, `KSpeedUnitOperators.kt` का
अनुसरण करते हुए `K...UnitOperators.kt` में प्रकार-युक्त संकारक विस्तार जोड़ें।

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// घनत्व = द्रव्यमान / आयतन (सामान्य KMixedUnitInstance: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. नामकरण और परीक्षण जाँच-सूची

- सभी सार्वजनिक प्रकार `K` से शुरू होते हैं (`KMassUnit`, `KMassUnitInstance`, ...); मान-1 नंगे टोकन और
  उपसर्ग-बिल्डर गुण विस्तार (`kilograms`, `grams`, ...) छूट-प्राप्त हैं और भाषा-सहज बने रहते हैं।
- समूह को पैरामीटरीकृत पार-मैट्रिक्स परीक्षण प्रक्रिया से आच्छादित करें, जो `of`/`into` के माध्यम से बनी हो
  (कभी कच्चे enum से नहीं): इकाई → इकाई रूपांतरण, हर इकाई युग्म पर प्रति संकारक और प्रति तुलना एक विधि,
  उपसर्ग-बिल्डर मैट्रिक्स, `of` प्रकार-संरक्षण, और `into` त्रुटि प्रकरण — `../../.claude/CLAUDE.md` में
  "Parameterized cross-matrix test procedure" खंड देखें।
- हर सार्वजनिक सदस्य को अंग्रेज़ी में, Markdown में, उपयोगी जगहों पर उदाहरणों के साथ प्रलेखित करें —
  विशेषकर संकारक।
- यदि समूह परिमाण-प्रतिबंधित है (जैसे भंडारण, जो ह्रासमान उपसर्ग अस्वीकार करता है), तो इसकी इकाई गुणों को
  आधार `KPrefixBuilder` के बजाय `KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder` पर लटकाएँ, ताकि
  अननुमत उपसर्ग एक **संकलन त्रुटि** बनें।
```
