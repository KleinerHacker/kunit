# الإجهاد الميكانيكي ومعامل المرونة

الحزمة: `org.pcsoft.framework.kunit.mechanic.pressure`
الوحدة الأساسية: **باسكال** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

النوع: **وحدة مركّبة**

الإجهاد الميكانيكي `σ = F / A` ومعامل المرونة (يانغ) `E = σ / ε` لهما بالضبط بُعد
[الضغط](pressure.md): `mass · length⁻¹ · time⁻²`. لذا لا تُقدّم KUnit مجموعة وحدات لهما — كلاهما **قراءتان** لمجموعة
الضغط، تُعبَّر عنهما عبر مسمّياتها ذات البادئات. توثّق هذه الصفحة تلك القراءات؛ أمّا المجموعة نفسها فمُوثّقة في
صفحة [الضغط](pressure.md).

!!! note "MPa و N/mm² و GPa مسمّيات ببادئات"
وحدات الإستاتيكا **ليست** رموزًا مخصّصة، لأنّها قابلة للوصول تمامًا:
**MPa = N/mm² = `mega.pascals`** و **GPa = `giga.pascals`**. التعبير
`(1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))` يُنتج نفس قيمة
`1 of mega.pascals` بالضبط.

## جدول القراءات

| القراءة                      | الرمز  | Kotlin         | 1 وحدة بـ Pa |
|------------------------------|--------|----------------|-------------:|
| باسكال                       | `Pa`   | `pascals`      |          1.0 |
| كيلوباسكال                   | `kPa`  | `kilo.pascals` |          1e3 |
| ميغاباسكال = N/mm²           | `MPa`  | `mega.pascals` |          1e6 |
| غيغاباسكال (معاملات المرونة) | `GPa`  | `giga.pascals` |          1e9 |
| قوّة لكل مساحة                | `N/m²` | `force / area` |          1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0 (N/mm² هو الميغاباسكال)
```

## قانون هوك

بالاشتراك مع مجموعة [الانفعال](strain.md)، تحمل مجموعة الضغط طرفَي قانون هوك:

| التعبير                                  | نوع النتيجة             | المعنى                    |
|------------------------------------------|-------------------------|---------------------------|
| `force / area`                           | `KPressureUnitInstance` | الإجهاد `σ = F / A`       |
| `stress / strain`                        | `KPressureUnitInstance` | معامل المرونة `E = σ / ε` |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | الإجهاد `σ = E · ε`       |
| `pressure * area`                        | `KForceUnitInstance`    | القوّة الفاعلة `F = σ · A` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0 (فولاذ)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## مثال واقعي: قضيب شدّ تحت حمل

قضيب شدّ فولاذي بقطر 20 mm (A ≈ 314 mm²) يحمل 60 kN. ما الإجهاد، وهل هو دون إجهاد الخضوع البالغ 235 MPa لفولاذ S235، وكم
يستطيل قضيب طوله 3 m (E = 210 GPa)؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - ضمن إجهاد الخضوع

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E كنسبة صرفة
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa" (وحدة أساسية للمجموعة)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات    | Kotlin                                            | المعنى                           |
|--------------|---------------------------------------------------|----------------------------------|
| `MPa`        | `mega.pascals`                                    | قراءة الإجهاد (= N/mm²)          |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | نفس القراءة كقوّة لكل مساحة       |
| `GPa`        | `giga.pascals`                                    | قراءة معامل المرونة              |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | نفس الكمّية بالأبعاد الأساسية     |
| `σ = F / A`  | `force / area`                                    | الإجهاد من القوّة والمساحة        |
| `E = σ / ε`  | `stress / strain`                                 | قانون هوك، محلولًا من أجل المعامل |
| `σ = E · ε`  | `pressure * strain`                               | قانون هوك، محلولًا من أجل الإجهاد |
