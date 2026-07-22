# فرق درجة الحرارة

الحزمة: `org.pcsoft.framework.kunit.temperature`
الوحدة الأساسية: **كلفن** (`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

النوع: **وحدة أصلية**

*فرق* درجة الحرارة هو الفترة بين درجتَي حرارة — كمّية **خطّية**، على النقيض من مجموعة
[درجة الحرارة](temperature.md) المطلقة الأفينية. لا يحمل **أي إزاحة** (فقط مقياس الكلفن)، فيتصرّف كمجموعة
وحدات عادية ويمرّ عبر المحرّك العامّ دون تغيير.

فيزيائيًا هذا هو سبب أنّ طرح درجتَي حرارة مطلقتين يعطي كلفن لا درجة حرارة: `30 °C − 10 °C = 20 ΔK`، لا
`20 °C`. وفرق قدره `20 ΔK` يساوي عدديًا فرقًا قدره `20 °C` على أي حال (حجم الخطوة نفسه)، فتقدّم المجموعة
عمدًا **الكلفن فقط** و**بلا بادئات**.

## الوحدات

| الوحدة | قيمة التعداد | الرمز | من/إلى كلفن |
|---|---|---|---|
| كلفن | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | هويّة |

!!! note "الرمز `ΔK`، لا `K`"
    يُطبَع فرق درجة الحرارة بالرمز **`ΔK`** (مثلًا `"20.0 ΔK"`)، متمايزًا عمدًا عن الكلفن المطلق (`K`).
    كلاهما البُعد نفسه (كلفن) لكنّهما كمّيتان مختلفتان — نقطة أفينية مقابل فترة خطّية. في
    [وحدة مختلطة](../../mixed-units.md) يكون `m·K` (مطلق) و`m·ΔK` (فرق) **ليسا** الوحدة نفسها، وليسا
    متساويين ولا قابلين للجمع؛ ويجعل الرمز المميّز ذلك مرئيًا بنظرة واحدة.

## البناء

لا يُبنى الفرق بالفعل العامّ `of` (المحجوز للكمّيات المطلقة). بل يُنتَج إمّا بـ**طرح درجتَي حرارة مطلقتين**
أو **صراحةً** عبر مصنع `KTemperatureDifference.ofKelvin(…)` — مُوضِّحًا نيّة «هذه فترة»:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // صريح، مساوٍ لـ d1
d1.value                                      // 20.0 (كلفن)
```

## المعاملات

`+`/`-`/المقارنة هي المعاملات الخطّية العادية من النوع نفسه (فرق زائد فرق هو فرق):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

وبما أنّه خطّي، يمكن أيضًا **تحجيم الفرق برقم مجرّد** (خلافًا لدرجة الحرارة المطلقة)، مع الحفاظ على نوعه:

```kotlin
import org.pcsoft.framework.kunit.times

val doubled = KTemperatureDifference.ofKelvin(5) * 2 // KTemperatureDifferenceUnitInstance: 10 ΔK
```

يمكن جمع فرق إلى درجة حرارة مطلقة أو طرحه منها ليعطي درجة حرارة مطلقة ثانيةً (راجع
[درجة الحرارة](temperature.md)):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## الدمج مع وحدات أخرى

ضرب فرق أو قسمته بمجموعة أخرى يُنتج `KMixedUnitInstance` عامًّا:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية (كلفن):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. يحمل الفرق مقياس الكلفن فقط (بلا
إزاحة) ويُبنى صراحةً، لا بالفعل العامّ `of` أبدًا.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | فترة درجة حرارة، الوحدة الأساسية (كلفن) |
| `30 °C − 10 °C` | `(30 of celsius) - (10 of celsius)` | فرق من درجتَي حرارة مطلقتين |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | مجموع فرقين |
