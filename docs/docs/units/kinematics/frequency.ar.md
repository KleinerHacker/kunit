# التردد

الحزمة: `org.pcsoft.framework.kunit.frequency`
الوحدة الأساسية: **هرتز** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

النوع: **وحدة أصلية**

تُنمذج مجموعة التردد عدد مرّات حدوث شيء لكل وحدة زمن. وهي مجموعة **أصلية أحادية البُعد** و**مقلوب الزمن**
(`1 Hz = 1/s`): يغلّف `KFrequencyUnitInstance` حدّ `KFrequencyUnit.HERTZ` واحدًا، مُطبَّعًا دائمًا إلى
الهرتز.

ولأنّ التردد مقلوب الزمن، فإنّ سلوكه عبر المجموعات مُعرَّف بأنّه **عكس الزمن تمامًا**: الضرب في تردد
يتصرّف كالقسمة على زمن، والقسمة على تردد كالضرب في زمن.

## الوحدات

| الوحدة | قيمة التعداد | الرمز | الرمز البرمجي | 1 وحدة بالهرتز |
|---|---|---|---:|---:|
| هرتز | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| دورة في الثانية | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| إطار في الثانية | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| دورة في الدقيقة | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| نبضة في الدقيقة | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

كل `Token` هو `KFrequencyUnitInstance` قيمته 1 يُستخدم مع `of` (البناء) و`into` (القراءة).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz (kHz عبر بادئة SI)
f.value                      // 2000.0 (مُطبَّع إلى الهرتز)
(3000 of rpm) into hertz     // 50.0  (3000 rpm = 50 Hz)
(50 of hertz) into rpm       // 3000.0
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - : المجموعة نفسها، تحويل تلقائي بين الوحدات
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// المقارنات والمساواة (بحسب القيمة المُطبَّعة بالهرتز)
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### معاملات التقاطع «مقلوب الزمن»

التردد والزمن مقلوبان، فيتّحدان في نتائج محكومة بالنوع:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance، 60 Hz (عدد / زمن = تردد)
val period = 1 / (2 of hertz)        // KTimeUnitInstance، 0.5 s   (عدد / تردد = زمن)
val count = (50 of hertz) * (2 of seconds)   // 100.0 (تردد * زمن = عدد عديم البُعد)

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance، 10 m/s (طول * تردد = سرعة)
(v / (5 of hertz)) into meters       // 2.0 (سرعة / تردد = مسافة)
```

## مثال واقعي: السرعة السطحية لعجلة دوّارة

عجلة محيطها **2 m** تدور بسرعة **5 دورات في الثانية**. ضرب محيطها في تردد الدوران يعطي السرعة السطحية
(سرعة التماس) — `length * frequency = speed`، وهو عكس `length / time = speed` المألوف:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed into meters                 // تُقرأ بـ m/s عبر مجموعة السرعة
surfaceSpeed.value                       // 10.0 m/s
```

## القوى بـ `pow`

ارفع قيمة لقوّة عددية صحيحة بالمعامِل `pow` (لا يملك Kotlin معامِل `^` قابلًا للتحميل الزائد). لمجموعة
التردد يُعيد `pow` وحدة `KMixedUnitInstance` عامّة (لا نوع قوّة مُبعَّد للتردد):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## بادئات SI

يقبل التردد **أي** مقدار، فيمكن دمج كل باني بادئة SI (`quetta` … `quecto`) مع كل وحدة تردد عبر الوصول إلى
الخاصّية. `kilo.hertz` هو kHz، و`mega.hertz` هو MHz، و`giga.hertz` هو GHz.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0 (MHz)
(2_400_000_000 of hertz) into giga.hertz // 2.4 (GHz)
```

## تنسيق `toString`

توجد فقط `toString()` للوحدة الأساسية؛ نسّق وحدة محدّدة عبر `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz" (تمثيل الوحدة الأساسية)
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## الترميز

يوضّح الجدول التالي كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل Kotlin مع KUnit. تُكتب الأُسّس بحروف
Unicode المرتفعة (`²`، `³`، `⁻¹`)، ويرمز `·` إلى الضرب و`/` إلى الكسر. وحيث يمكن كتابة كمّية ككسر وكحاصل
ضرب بأُسّس سالبة، تُدرَج الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `Hz` | `hertz` | التردد، الوحدة الأساسية (هرتز) |
| `kHz` | `kilo.hertz` | كيلوهرتز (بادئة مطبَّقة على الهرتز) |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | تردد من دور (هرتز محكوم بالنوع) |
| `Hz²` | `hertz pow 2` | هرتز تربيعًا (وحدة مختلطة عامّة) |
