# الدفع

الحزمة: `org.pcsoft.framework.kunit.mechanic.momentum`
الوحدة الأساسية: **كيلوغرام متر لكل ثانية** (`KMomentumUnit.BASE`)، تُقرأ كـ **نيوتن ثانية**
(`KMomentumUnit.NEWTON_SECOND`)

النوع: **وحدة مركّبة**

الدفع `J = F · t` هو الزخم الذي تنقله قوّة خلال الزمن الذي تؤثّر فيه. من حيث البُعد، *هو*
[زخم](momentum.md): `1 N·s = 1 kg·m/s`. لذا لا تُدخل KUnit مجموعة وحدات ثانية له — الدفع **قراءة**
لمجموعة الزخم، تُعبَّر عنها بالرمز البرمجي `newtonSeconds`. توثّق هذه الصفحة تلك القراءة؛ أمّا المجموعة نفسها فتُوصف في
صفحة [الزخم](momentum.md).

!!! note "مجموعة واحدة، قراءتان"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` تُساوي `true`. اختيار رمز لا يغيّر إلّا طريقة قراءة القيمة، لا
ماهيّتها أبدًا. استخدم `newtonSeconds` عندما تفكّر بمنطق "قوّة × زمن"، و
`kilogramMetersPerSecond` عندما تفكّر بمنطق "كتلة × سرعة".

## الوحدات المسمّاة

| الوحدة                 | الرمز     |              الرمز البرمجي | 1 وحدة بـ kg·m/s |
|------------------------|-----------|---------------------------:|-----------------:|
| نيوتن ثانية            | `N*s`     |            `newtonSeconds` |              1.0 |
| كيلوغرام متر لكل ثانية | `kg*m/s`  |  `kilogramMetersPerSecond` |              1.0 |
| غرام سنتيمتر لكل ثانية | `g*cm/s`  | `gramCentimetersPerSecond` |             1e-5 |
| رطل-قدم لكل ثانية      | `lb*ft/s` |       `poundFeetPerSecond` |       ≈ 0.138255 |

تتوفّر صيغ ببادئة لكلّ رمز (`kilo.newtonSeconds` = kN·s، `milli.newtonSeconds` = mN·s).

## حساب الدفع

| التعبير           | نوع النتيجة             | المعنى                   |
|-------------------|-------------------------|--------------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`              |
| `time * force`    | `KMomentumUnitInstance` | نفسه، تبادلي             |
| `impulse / time`  | `KForceUnitInstance`    | متوسّط القوة `F = J / t`  |
| `impulse / force` | `KTimeUnitInstance`     | زمن التأثير `t = J / F`  |
| `impulse / mass`  | `KSpeedUnitInstance`    | تغيّر السرعة `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0 (البُعد نفسه)
```

## مثال واقعي: احتراق مرحلة صاروخ

يُنتج محرّك صاروخ نموذجيّ دفعًا متوسّطًا مقداره 12 نيوتن لمدّة 1.6 ثانية. أيّ دفع كلّي يُنتجه، وأيّ تغيّر في السرعة
يُحدثه ذلك لصاروخ كتلته 0.8 كغ؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## تنسيق `toString`

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s" (الوحدة الأساسية للمجموعة)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف Unicode المرتفعة
(`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل ضرب بأُسّس سالبة، تُدرَج
الصيغتان المكافئتان في Kotlin.

| الرياضيات    | Kotlin                                   | المعنى                         |
|--------------|------------------------------------------|--------------------------------|
| `N·s`        | `newtonSeconds`                          | الدفع (رمز مسمّى لمجموعة الزخم) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | الكمّية نفسها بالأبعاد الأساسية |
| `J = F · t`  | `force * time`                           | التفكيك المحكوم بالنوع         |
| `F = J / t`  | `impulse / time`                         | محلولة لمتوسّط القوة            |
| `Δv = J / m` | `impulse / mass`                         | تغيّر سرعة كتلة                 |
| `kN·s`       | `kilo.newtonSeconds`                     | دفع ببادئة                     |
