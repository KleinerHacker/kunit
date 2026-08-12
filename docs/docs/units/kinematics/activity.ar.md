# النشاط الإشعاعي (بيكريل)

الحزمة: `org.pcsoft.framework.kunit.kinematic.frequency`
الوحدة الأساسية: **هرتز** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

النوع: **وحدة أصلية**

النشاط الإشعاعي `A` لعينة مشعة هو عدد الاضمحلالات النووية في الثانية. وحدته هي **البيكريل**، حيث
`1 Bq = 1 s⁻¹` — وهي **مطابقة بعديًا** [للتردد](frequency.ar.md).

## لماذا لا يملك البيكريل نوعه الخاص

يُنمذج KUnit النشاط الإشعاعي عمدًا باستخدام `KFrequencyUnitInstance` بدلاً من نوع منفصل
`KActivityUnitInstance`. والسبب يكمن في عقد التعرّف على الصيغة في هذه المكتبة:

* كل مجموعة موحّدة تملك صيغة قانونية **واحدة** للبعد الأساسي المُطبَّع، و
* تتعرّف `toX()` على تلك الصيغة بالتحديد.

يشترك النشاط الإشعاعي والتردد في نفس الصيغة المُطبَّعة `time⁻¹`. وجود نوعين لصيغة مُطبَّعة واحدة سيجعل
التعبير الأصلي غامضًا — إذ ستُطابق `toFrequency()` وسيتطابق `toActivity()` الافتراضية نفس الوحدة المختلطة،
دون أن تكون إحداهما أصح من الأخرى. النوع الواحد يحافظ على حتمية التحويل ذهابًا وإيابًا.

الفرق مسألة *كيف تسمّي متغيّرك*: التردد يعدّ دورات متكررة، والنشاط الإشعاعي يعدّ اضمحلالات عشوائية، لكن
كلاهما "أحداث في الثانية".

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // تُقرأ كـ 37 GBq — غرام واحد من الراديوم
activity into mega.hertz             // 37 000.0

// الاضمحلالات في دقيقة واحدة
val decays = activity * (60 of seconds)   // عدد بلا وحدة
decays                                     // 2.22e12
```

!!! note "الكوري"
    الوحدة التاريخية هي الكوري، حيث 1 Ci = 3.7 × 10¹⁰ Bq. لا يوجد لها رمز خاص بها؛ اكتبها كـ
    `37 of giga.hertz` أو أنشئ ثابتك الخاص.

## مثال من الواقع — مصدر كاشف الدخان

يحتوي كاشف الدخان المنزلي على حوالي **30 kBq** من الأمريسيوم-241:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// الاضمحلالات خلال يوم واحد
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## انظر أيضًا

* [التردد](frequency.ar.md) — نفس النوع، يُقرأ كمعدّل دوري.
* [معدل الجرعة](../thermodynamics/dose-rate.ar.md) — الجرعة التي يوصّلها المصدر لكل وحدة زمن.
* [الجرعة الممتصة](../thermodynamics/absorbed-dose.ar.md) — الجرعة القائمة على الطاقة.
