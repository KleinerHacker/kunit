# كثافة التيار

الحزمة: `org.pcsoft.framework.kunit.currentdensity`
الوحدة الأساسية: **أمبير لكل متر مربع** (`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`)

النوع: **وحدة مركّبة**

كثافة التيار وحدة **مركّبة**: التركيب `current · length⁻²` (`A/m²`) — التيار الكهربائي لكل مقطع عرضي
للموصل. يغلّف `KCurrentDensityUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين —
`KElectricCurrentUnit.BASE` (أمبير) بالأس `+1` و`KDistanceUnit.BASE` (متر) بالأس `-2`. يُخزَّن كلا
المكوّنين بوحدتيهما الأساسية في المجموعة، لذا فالقيمة هي مباشرةً القراءة بوحدة A/m².

## إنشاء كثافة تيار

لا تملك كثافة التيار **أي رموز مسمّاة** ولا بادئات خاصة بها: كل صياغة نسبة (`A/m²`، `A/mm²`، …).
تُنشأ كتعبير أو عبر عامل `current / area` المكتوب بنوعه صريحًا، وتُقرأ باستخدام `into` مقابل تعبير كهذا:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## تفكيكات متعددة

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `current / area` | `KCurrentDensityUnitInstance` | التعريف `J = I / A` |
| `current/length²` | عبر `.toCurrentDensity()` | التعبير الأصلي القياسي `A·m⁻²` |

تُعيد الصيغة المكتوبة بنوعها صريحًا كثافة تيار مباشرة. أما التعبير الأصلي بالكامل فيبقى
`KMixedUnitInstance` عامًّا ويُضيَّق عبر `toCurrentDensity()` (الذي يتعرّف فقط على الصيغة القياسية
ويرمي `IllegalStateException` خلاف ذلك). كلا المسارين متساويا القيمة.

تربط العمليات العكسية بين التيار والمساحة وكثافة التيار:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `currentDensity * area` | `KElectricCurrentUnitInstance` | `I = J · A` (تبادلي) |
| `current / currentDensity` | `KAreaUnitInstance` | `A = I / J` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.currentdensity.*

// مثال واقعي - اختيار حجم السلك: مرور 16 A عبر سلك نحاسي 2.5 mm² يعطي 6.4 A/mm².
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// محلولًا لإيجاد التيار الذي يمكن أن يحمله مقطع عرضي معيّن عند تلك الكثافة:
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance، 25.6 A

// نفس الكثافة كتعبير أصلي A·m⁻²:
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف يونيكود العلوية (`²`، `⁻²`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `A/m²` | `amperes / (meters pow 2)` | كثافة التيار، الوحدة الأساسية (صيغة الكسر) |
| `A·m⁻²` | `amperes * (meters pow -2)` | نفس كثافة التيار كحاصل ضرب خالص |
| `I / A` | `(16 of amperes) / crossSection` | كثافة التيار من التيار والمساحة |
| `A/mm²` | `amperes / (milli.meters pow 2)` | كثافة التيار بالوحدة الشائعة في الأسلاك |
