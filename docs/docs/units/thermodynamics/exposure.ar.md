# التعرّض (جرعة التأيّن)

الحزمة: `org.pcsoft.framework.kunit.electric.specificcharge`
الوحدة الأساسية: **كولوم لكل كيلوغرام**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

النوع: **وحدة مركّبة (constructed unit)**

التعرّض `X` — **جرعة التأيّن** الكلاسيكية — يقيس الإشعاع المؤيّن بالشحنة الكهربائية التي يحرّرها
لكل وحدة كتلة من الهواء: `X = Q / m`، بوحدة `C/kg`. وحدته التاريخية هي **الرونتغن**
(1 R = 2.58 × 10⁻⁴ C/kg).

بُعده هو `current · time · mass⁻¹` — وهو **مطابق تمامًا** لـ
[الشحنة النوعية](../electrical/specificcharge.ar.md) لجسيم ما. يُنمذج KUnit مجموعة واحدة
لكلتا القراءتين؛ والتعرّض هو إحداهما. توثّق هذه الصفحة تلك القراءة.

## لماذا لا يملك التعرّض نوعًا خاصًا به

يتعمّد KUnit نمذجة التعرّض باستخدام `KSpecificChargeUnitInstance` بدلاً من نوع منفصل
`KExposureUnitInstance`. والسبب هو عقد التعرّف على الصيغة في هذه المكتبة:

* كل مجموعة موحّدة لها **صيغة قانونية واحدة** للبُعد الأساسي، و
* تتعرّف `toX()` بدقة على تلك الصيغة فقط.

يشترك التعرّض والشحنة النوعية في الصيغة القياسية `current¹ · time¹ · mass⁻¹`. وجود نوعين لصيغة
قياسية واحدة سيجعل التعبير الأصلي غامضًا — فكلٌ من `toSpecificCharge()` ودالة افتراضية
`toExposure()` ستطابق نفس الوحدة المختلطة، ولن تكون أيّ إجابة أصحّ من الأخرى. نوع واحد يحافظ على
أن تكون عملية التحويل ذهابًا وإيابًا محددة بدقة.

لذا فإن التمييز بينهما هو مسألة *الاسم الذي تختاره لمتغيّرك*، وليس مسألة النوع الذي تمنحك إياه
المكتبة — تمامًا كما في الفيزياء، حيث تُكتب كلتاهما بوحدة C/kg.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## مثال من الواقع — قراءة من مقياس جرعة قديم

يعرض مقياس جرعة قلمي **200 mR** بعد نوبة عمل. مُحوَّلة إلى النظام الدولي وإلى الشحنة المُحرَّرة
في 1 كجم من الهواء الذي عُيّرت الغرفة عليه:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## انظر أيضًا

* [الشحنة النوعية](../electrical/specificcharge.ar.md) — نفس النوع، مقروءًا كخاصية جسيم.
* [الجرعة الممتصة](absorbed-dose.ar.md) و[الجرعة المكافئة](dose-equivalent.ar.md) — الجرعات
  القائمة على الطاقة.
* [معدل الجرعة](dose-rate.ar.md) — الجرعة لكل وحدة زمن.
