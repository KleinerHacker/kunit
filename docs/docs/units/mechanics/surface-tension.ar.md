# التوتّر السطحي

الحزمة: `org.pcsoft.framework.kunit.mechanic.lineforce`
الوحدة الأساسية: **نيوتن لكل متر** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

النوع: **وحدة مركّبة**

التوتّر السطحي `σ` هو الطاقة اللازمة لخلق وحدة من السطح الجديد، أو بصورة معادلة القوّة الفاعلة لكلّ وحدة طول على طول خطّ
تماسّ: `1 J/m² = 1 N/m`. بُعده هو `mass · time⁻²`.

هذا هو بالضبط بُعد **القوّة لكلّ طول**، الذي تشترك فيه [الصلابة](stiffness.md). لذا تُنمذج KUnit مجموعة محايدة واحدة،
`lineforce`، لكلتا القراءتين؛ والتوتّر السطحي إحداهما. توثّق هذه الصفحة تلك القراءة.

!!! note "مجموعة واحدة، قراءتان"
`KLineForceUnitInstance` هو النوع المشترك. لا شيء يميّز التوتّر السطحي عن معامل الزنبرك سوى الاسم الذي تُطلقه عليه —
سُمّيت المجموعة تسمية محايدة حتّى لا تدّعي إحدى القراءتين اسم الأخرى.

## الوحدات المسمّاة

| الوحدة           | الرمز    |          الرمز البرمجي | 1 وحدة بـ N/m |
|------------------|----------|-----------------------:|--------------:|
| نيوتن لكل متر    | `N/m`    |      `newtonsPerMeter` |           1.0 |
| داين لكل سنتيمتر | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |
| نيوتن لكل ملّيمتر | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| رطل-قوّة لكل بوصة | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| كيلوبوند لكل متر | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |

عادةً ما يُذكَر التوتّر السطحي بوحدة mN/m أو مكافئها العددي dyn/cm: الماء عند 25 °C نحو 72 mN/m = 72 dyn/cm. الملّينيوتن
لكل متر هو الصيغة ببادئة `milli.newtonsPerMeter`.

## التفكيكات

| الصيغة       | Kotlin                                                  | نوع النتيجة              |
|--------------|---------------------------------------------------------|--------------------------|
| طاقة / مساحة | `energy / area`                                         | `KLineForceUnitInstance` |
| قوّة / طول    | `force / length`                                        | `KLineForceUnitInstance` |
| تعبير أصلي   | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - كلاهما 2 N/m
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## الحساب باستخدام الوحدات الأساسية

| التعبير                                    | نوع النتيجة              | المعنى                 |
|--------------------------------------------|--------------------------|------------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`            |
| `lineforce * area`, `area * lineforce`     | `KEnergyUnitInstance`    | طاقة السطح `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`            |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`            |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | `F = σ · l`            |

## مثال واقعي: طاقة خلق غشاء صابوني

نفخ غشاء صابوني مساحته 0.05 m² (سطحان، σ ≈ 25 mN/m لكلّ سطح). كم تكلّف ذلك من طاقة، وأيّ قوّة يبذلها الغشاء على سلك طوله
10 cm؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m" (وحدة أساسية)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات   | Kotlin                          | المعنى                         |
|-------------|---------------------------------|--------------------------------|
| `N/m`       | `newtonsPerMeter`               | التوتّر السطحي، الوحدة الأساسية |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | نفس الكمّية بالأبعاد الأساسية   |
| `mN/m`      | `milli.newtonsPerMeter`         | قراءة التوتّر السطحي اليومية    |
| `dyn/cm`    | `dynesPerCentimeter`            | قراءة CGS (= 1 mN/m)           |
| `σ = W / A` | `energy / area`                 | التفكيك أ                      |
| `σ = F / l` | `force / length`                | التفكيك ب                      |
| `W = σ · A` | `lineforce * area`              | طاقة السطح                     |
