# الجرعة الممتصة (غراي)

الحزمة: `org.pcsoft.framework.kunit.thermo.specificenergy`
الوحدة الأساسية: **جول لكل كيلوغرام**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

النوع: **وحدة مركّبة (constructed unit)**

الجرعة الممتصة `D` هي طاقة الإشعاع المؤيّن المودعة لكل وحدة كتلة: `D = E / m`. وحدتها هي
**الغراي**، و`1 Gy = 1 J/kg` — وهي **مطابقة من حيث البُعد** لـ[الطاقة النوعية](specific-energy.ar.md).

## لماذا لا يملك الغراي نوعًا خاصًا به

يتعمّد KUnit نمذجة الجرعة الممتصة باستخدام `KSpecificEnergyUnitInstance` بدلاً من نوع منفصل
`KAbsorbedDoseUnitInstance`. والسبب هو عقد التعرّف على الصيغة في هذه المكتبة:

* كل مجموعة موحّدة لها **صيغة قانونية واحدة** للبُعد الأساسي، و
* تتعرّف `toX()` بدقة على تلك الصيغة فقط.

تشترك الجرعة الممتصة والطاقة النوعية في الصيغة القياسية `length² · time⁻²`. وجود نوعين لصيغة قياسية
واحدة سيجعل التعبير الأصلي غامضًا — فكلٌ من `toSpecificEnergy()` ودالة افتراضية `toAbsorbedDose()`
ستطابق نفس الوحدة المختلطة، ولن تكون أيّ إجابة أصحّ من الأخرى. نوع واحد يحافظ على أن تكون عملية
التحويل ذهابًا وإيابًا محددة بدقة.

لذا فإن التمييز بينهما هو مسألة *الاسم الذي تختاره لمتغيّرك*، وليس مسألة النوع الذي تمنحك إياه
المكتبة — تمامًا كما في الفيزياء، حيث الغراي **هو** جول لكل كيلوغرام.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## مثال من الواقع — تصوير الصدر بالأشعة السينية

يودع تصوير الصدر بالأشعة السينية تقريبًا **0.1 mGy**. فما هي إجمالي الطاقة المكافئة لذلك في
جسم يزن 70 كجم، وكيف تُقارَن بجرعة سنة كاملة من الخلفية الإشعاعية الطبيعية (≈ 2.4 mGy)؟

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## انظر أيضًا

* [الطاقة النوعية](specific-energy.ar.md) — نفس النوع، مقروءًا كثافة طاقة.
* [الجرعة المكافئة](dose-equivalent.ar.md) — السيفرت، المرجّح للتأثير البيولوجي.
* [معدل الجرعة](dose-rate.ar.md) — الجرعة لكل وحدة زمن، والتي **تملك** نوعها الخاص.
* [التعرّض](exposure.ar.md) — جرعة التأيّن المعتمدة على الشحنة.
