# الجرعة المكافئة (سيفرت)

الحزمة: `org.pcsoft.framework.kunit.thermo.specificenergy`
الوحدة الأساسية: **جول لكل كيلوغرام**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

النوع: **وحدة مركّبة (constructed unit)**

الجرعة المكافئة `H` ترجّح [الجرعة الممتصة](absorbed-dose.ar.md) بعامل ترجيح إشعاعي **عديم البُعد**
`w_R`، الذي يأخذ في الحسبان مدى ضرر نوع معيّن من الإشعاع: `H = w_R · D`. وحدتها هي **السيفرت**،
ولأن `w_R` عديم البُعد، فإن `1 Sv = 1 J/kg` — وهو نفس بُعد الغراي.

## لماذا لا يملك السيفرت نوعًا خاصًا به

يُنمذج KUnit الجرعة المكافئة باستخدام `KSpecificEnergyUnitInstance`، وهو نفس نوع الغراي
والطاقة النوعية. والسبب هو عقد التعرّف على الصيغة في هذه المكتبة:

* كل مجموعة موحّدة لها **صيغة قانونية واحدة** للبُعد الأساسي، و
* تتعرّف `toX()` بدقة على تلك الصيغة فقط.

يشترك السيفرت والغراي والطاقة النوعية جميعًا في الصيغة القياسية `length² · time⁻²`. وجود عدة
أنواع لصيغة قياسية واحدة سيجعل التعبير الأصلي غامضًا، ولن تكون أيّ إجابة أصحّ من الأخرى. نوع واحد
يحافظ على أن تكون عملية التحويل ذهابًا وإيابًا محددة بدقة.

!!! warning "تطبيق عامل الترجيح مسؤوليتك"
    لأن `w_R` عديم البُعد، لا يستطيع KUnit التمييز بين الغراي والسيفرت. ضرب الجرعة الممتصة في
    عامل الترجيح هو مجرد عملية ضرب عددية عادية — لن تقوم المكتبة بذلك نيابةً عنك، ولن تمنعك من
    خلط القراءتين. سمِّ قيمك وفقًا لذلك.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## مثال من الواقع — رحلة طيران وسنة من الخلفية الإشعاعية

الخلفية الإشعاعية الطبيعية تبلغ نحو **2.4 mSv** سنويًا؛ وتضيف رحلة عبر الأطلسي حوالي 0.05 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## انظر أيضًا

* [الجرعة الممتصة](absorbed-dose.ar.md) — الغراي غير المرجّح.
* [الطاقة النوعية](specific-energy.ar.md) — النوع الأساسي.
* [معدل الجرعة](dose-rate.ar.md) — الجرعة لكل وحدة زمن، وتشمل تهجئات السيفرت.
* [التعرّض](exposure.ar.md) — جرعة التأيّن المعتمدة على الشحنة.
