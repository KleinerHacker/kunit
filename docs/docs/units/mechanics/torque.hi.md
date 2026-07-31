# टॉर्क

पैकेज: `org.pcsoft.framework.kunit.common.energy`
मूल इकाई: **जूल** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`), जिसे **न्यूटन मीटर** के रूप में पढ़ा जाता है (`N·m`)

प्रकार: **निर्मित इकाई**

टॉर्क `M = F · r` किसी लीवर भुजा पर कार्य करने वाले बल का घूर्णी प्रभाव है। आयामिक रूप से यह *एक*
[ऊर्जा](energy.md) *है*: `1 N·m = 1 J`। इसलिए KUnit इसके लिए **कोई दूसरा** इकाई समूह पेश **नहीं** करता — टॉर्क ऊर्जा
समूह का एक **पठन** है। यह पृष्ठ उस पठन का दस्तावेज़ीकरण करता है; समूह स्वयं
[ऊर्जा (यांत्रिकी)](energy.md) पृष्ठ पर वर्णित है।

!!! note "समान आयाम, भिन्न भौतिकी"
टॉर्क और कार्य भौतिक रूप से भिन्न हैं (टॉर्क एक अक्षीय सदिश है, कार्य एक अदिश), लेकिन दोनों का आयाम ठीक `kg·m²·s⁻²` समान
है। चूँकि KUnit *इकाइयों* का मॉडल बनाता है, न कि सदिश गुण का, इसलिए दोनों एक ही समूह में रहते हैं। नामकरण से इन्हें अलग
रखें: `val torque = (100 of newtons) * (2 of meters)`
N·m के रूप में पढ़ा जाता है, पथ के अनुदिश `val work = force * distance` J के रूप में पढ़ा जाता है।

## एक टॉर्क बनाना

| व्यंजक                                | परिणाम प्रकार                           | अर्थ                     |
|------------------------------------|------------------------------------|------------------------|
| `force * length`, `length * force` | `KEnergyUnitInstance`              | `M = F · r` (लीवर भुजा)   |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α` (घूर्णी न्यूटन)   |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω` (ड्राइवट्रेन सूत्र) |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`            |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`            |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`            |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`            |

तीनों निर्माण रूप ऊर्जा समूह की एकल फैक्ट्री में जाते हैं, इसलिए वे मान-समान हैं:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## नामित इकाइयाँ

टॉर्क ऊर्जा समूह के टोकन का उपयोग करता है; `newtons * meters` विशिष्ट N·m वर्तनी है, और उपसर्ग-युक्त पठन ऊर्जा टोकन से
आते हैं (`kilo.joules` = kN·m)।

| पठन          | प्रतीक    | Kotlin                           |
|--------------|--------|----------------------------------|
| न्यूटन मीटर      | `N*m`  | `(1 of newtons) * (1 of meters)` |
| जूल (समान आयाम) | `J`    | `joules`                         |
| किलोन्यूटन मीटर    | `kN*m` | `kilo.joules`                    |

## वास्तविक उदाहरण: इंजन टॉर्क और शक्ति

एक इंजन 3000 rpm पर 62.83 kW प्रदान करता है। यह कौन-सा टॉर्क है? और यदि वही टॉर्क 6000 rpm पर बनाए रखा जाए तो कौन-सी
शक्ति मिलती है?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0 (N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## संकारक

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## toString स्वरूपण

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J" (समूह की मूल इकाई)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## संकेतन

नीचे दी गई तालिका दर्शाती है कि इस इकाई और इसके घटकों को गणितीय रूप से बनाम KUnit के साथ Kotlin में कैसे लिखा जाता है।
घातांक Unicode उपरिलेख (`²`, `³`, `⁻¹`) से लिखे जाते हैं, `·` गुणन और `/` भिन्न दर्शाता है। जहाँ किसी राशि को भिन्न और
ऋणात्मक घातांकों वाले गुणनफल दोनों रूपों में लिखा जा सकता है, वहाँ दोनों समतुल्य Kotlin रूप सूचीबद्ध हैं।

| गणित         | Kotlin                                           | अर्थ              |
|-------------|--------------------------------------------------|-----------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | टॉर्क, लीवर-भुजा रूप   |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | वही राशि आधार आयामों में |
| `M = F · r` | `force * length`                                 | अपघटन A         |
| `M = J · α` | `inertia * angularacceleration`                  | अपघटन B         |
| `M = P / ω` | `power / angularvelocity`                        | अपघटन C (ड्राइवट्रेन) |
| `P = M · ω` | `torque * angularvelocity`                       | घूर्णी शक्ति           |
| `kN·m`      | `kilo.joules`                                    | उपसर्ग-युक्त टॉर्क पठन  |
