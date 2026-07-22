# KUnit

**KUnit** إطار عمل بلغة Kotlin (قابل للاستخدام من Java أيضًا) للحساب بالوحدات الفيزيائية بدلًا من الأرقام
المجرّدة. فبدلًا من تتبّع الأمتار أو الأميال أو الأمتار المربّعة كقيم `Double` بسيطة على أمل أن تتّفق كل
مواضع الاستدعاء على الوحدة، يحمل `kunit` الوحدة إلى جانب القيمة ويتولّى عنك التحويل والضرب ومسك الدفاتر
البُعدي.

## لماذا KUnit؟

العمل بالأرقام الخام للكمّيات الفيزيائية عرضة للأخطاء: من السهل جمع أمتار إلى أميال دون تحويل، أو جمع
مساحة إلى طول عن غير قصد. يحلّ kunit ذلك بجعل الوحدة جزءًا من النوع:

- **فعلان، `of` و`into`.** تُبنى القيمة بـ `number of <unit>` (`5 of meters`)، وتُقرأ بـ
  `value into <unit>` (`v into kilo.meters`). الرقم والوحدة مفصولان بصرامة.
- **حساب آمن نوعيًا.** يؤدّي `+` و`-` بين مجموعات وحدات أو أُسّس غير متوافقة إلى إطلاق
  `IllegalStateException` بدلًا من إنتاج رقم خاطئ بصمت.
- **تحويل تلقائي.** `(5 of meters) + (3 of miles)` يعمل ببساطة — يُطبَّع المعاملان داخليًا، فلا تحتاج
  أبدًا إلى تحويل الوحدات يدويًا قبل دمجها.
- **ضرب وقسمة حرّان.** ضرب الوحدات أو قسمتها *مسموح دائمًا* ويتتبّع تلقائيًا البُعد الفيزيائي الناتج
  (الأُسّ)، مثلًا `length * length` يصير مساحة.
- **دعم كامل لـ `Number`.** أنشئ القيم من `Int` و`Long` و`Float` و`Double` وأي نوع `Number` آخر؛ يُطبَّع
  كل شيء إلى `Double` داخليًا.
- **جدول بادئات SI الكامل**، من كويتا (Q) إلى كويكتو (q)، كبانيات بادئات (`kilo.meters`،
  `milli.seconds`) مع سياسة بادئات لكل وحدة مفروضة عند التصريف.
- **وحدات خاصّة مسمّاة** (كالهكتار واللتر والفدّان) كرموز قيمتها 1 تُستخدم مع `of`/`into`.

## المفاهيم الأساسية

بُني kunit حول نوعين مركزيين:

- **`KMixedUnitInstance`** — *وحدة مختلطة*: قيمة أساس `Double` إضافة إلى وحدة `KUnit` واحدة أو أكثر، كلّ
  منها مقترنة بأُسّ صحيح (مثلًا `m^1 * s^-1` لسرعة). هذا هو المحرّك العامّ الذي يشغّل كل شيء آخر.
- **`KUnit`** — وحدة «نقيّة» واحدة تنتمي إلى مجموعة وحدات (مثلًا المتر ينتمي إلى مجموعة الطول). تُنمذَج
  مجموعات الوحدات المحدّدة كـ `enum class ... : KUnit` (مثلًا `KDistanceUnit`).

توفّر كل مجموعة وحدات إضافةً إلى ذلك **صنف غلاف** (مثلًا `KLengthUnitInstance`) يغلّف
`KMixedUnitInstance` مقيّدًا بمجموعة وحدات واحدة، مُطبَّعًا دائمًا إلى الوحدة الأساسية لتلك المجموعة. هذا
هو النوع الذي ستستخدمه غالبًا — راجع [الوحدات المعرّفة مسبقًا](units/kinematics/distance.md) للوحدات
المشحونة اليوم، و[الوحدات المركبة](mixed-units.md) لمعرفة متى وكيف تنزل مباشرةً إلى محرّك
`KMixedUnitInstance` العامّ.

إذا أردت إضافة دعم لكمّية فيزيائية جديدة (مثلًا الكتلة أو الزمن)، فراجع
[إضافة وحدات مخصصة](custom-units.md) لجولة كاملة خطوة بخطوة.

!!! note "كائنات الوحدات غير قابلة للتغيير"
    كل قيمة وحدة — محرّك `KMixedUnitInstance` وكذلك كل غلاف «نقيّ» مثل `KLengthUnitInstance` أو
    `KTimeUnitInstance` — **غير قابلة للتغيير**. لا تُعدّل أي عملية نسخةً قائمة أبدًا؛ فالمعاملات
    (`+`، `-`، `*`، `/`) والتحويلات تُعيد دائمًا كائنًا **جديدًا**، تاركةً المعاملات كما هي. هذا يجعل قيم
    الوحدات آمنة للمشاركة بحرّية ولاستخدامها كمفاتيح أو ثوابت.

## بداية سريعة

أضِف الوحدة كتبعية (أو ضمّنها كمشروع/مجموعة مصادر) واستورد مفردات مجموعة الوحدات التي تحتاجها.

### الطول

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// ابنِ قيم طول نقيّة بـ `of` من أي نوع Number
val distance = 5 of meters
val trip = 10 of miles

// المعاملات: تحويل تلقائي ضمن المجموعة والأُسّ نفسه
val total = distance + trip          // KLengthUnitInstance، مُطبَّع إلى الأمتار
val diff = trip - distance

// المقارنات
val isFarther = trip > distance      // true

// اقرأ القيمة بوحدة محدّدة بـ `into`
println(total into kilo.meters)      // مثلًا 21.0467...
println(total into yards)            // مثلًا 23018.4...

// ضرب أطوال نقيّة يبني مساحة محكومة بالنوع
val area = distance * trip           // KAreaUnitInstance

// وحدات خاصّة مسمّاة للمساحة (أُسّ 2) والحجم (أُسّ 3)
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### بادئات SI

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0 (مُطبَّع إلى الأمتار)
```

### الوحدات المختلطة / المركّبة

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds

// كوّن تعبير وحدة من قوالب قيمتها 1 وحجّمه بـ `of`
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance، m·s⁻²
```

## السحب والبناء

```bash
git clone <repository-url>
cd kunit
```

يستخدم kunit أداة Gradle (المُغلِّف مُضمَّن في المستودع، لا حاجة لتثبيت Gradle محليًا):

```bash
# البناء
./gradlew build          # Windows: gradlew.bat build

# تشغيل الاختبارات فقط
./gradlew test            # Windows: gradlew.bat test
```

يلزم توفّر JDK قادر على حلّ سلسلة الأدوات 25 (يُنزّله المكوّن الإضافي `foojay-resolver` تلقائيًا عند
الحاجة).
