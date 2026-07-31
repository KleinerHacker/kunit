# الشحنة الكهربائية

الحزمة: `org.pcsoft.framework.kunit.electric.charge`
الوحدة الأساسية: **كولوم** (`KChargeUnit.BASE == KChargeUnit.COULOMB`)

النوع: **وحدة مركبة**

الشحنة الكهربائية وحدة **مركبة**: التركيب `current · time` (`A·s`). يغلّف `KChargeUnitInstance` كائن
`KMixedUnitInstance` مكوّن من حدّين — `KElectricCurrentUnit.BASE` (أمبير) بالأس `+1` و
`KTimeUnit.BASE` (ثانية) بالأس `+1`. القيمة المخزّنة تُطبَّع دائمًا إلى الكولوم، بغضّ النظر عن الوحدة المسمّاة أو
البادئة أو تركيبة التيار/الزمن التي أُنشئت منها.

## إنشاء شحنة

تُنشأ الشحنة برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| الشحنة              | الرمز   |       الرمز البرمجي | 1 وحدة بالكولوم |
|---------------------|---------|--------------------:|----------------:|
| كولوم               | `C`     |          `coulombs` |             1.0 |
| أمبير ثانية         | `As`    |     `ampereSeconds` |             1.0 |
| أمبير ساعة          | `Ah`    |       `ampereHours` |          3600.0 |
| أبكولوم (CGS-EMU)   | `abC`   |        `abcoulombs` |            10.0 |
| ستاتكولوم (CGS-ESU) | `statC` |      `statcoulombs` |    3.335641e-10 |
| فاراداي             | `F_c`   |          `faradays` |       96485.332 |
| الشحنة الأولية      | `e`     | `elementaryCharges` | 1.602176634e-19 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`kilo.coulombs`، `milli.coulombs`،
`milli.ampereHours`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## تفكيكات متعددة

يمكن الوصول إلى الشحنة عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج شحنة متساوية القيمة:

| التعبير               | نوع النتيجة           | المعنى                                     |
|-----------------------|-----------------------|--------------------------------------------|
| `current * time`      | `KChargeUnitInstance` | التعريف `Q = I · t`                        |
| `time * current`      | `KChargeUnitInstance` | الصيغة التبادلية لـ `Q = I · t`            |
| `current / frequency` | `KChargeUnitInstance` | صيغة الزمن العكسي `Q = I / f` (`1/Hz = s`) |
| `current·time`        | عبر `.toCharge()`     | التعبير الأصلي القياسي `A·s`               |

تُعيد الصيغ المكتوبة بأنواع صريحة شحنةً مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toCharge()` (الذي يتعرّف فقط على الصيغة القياسية — حدّ `KElectricCurrentUnit` واحد بالأس `+1` وحدّ
`KTimeUnit` واحد بالأس `+1` — ويرمي `IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الشحنة والتيار والزمن:

| التعبير              | نوع النتيجة                    | المعنى                          |
|----------------------|--------------------------------|---------------------------------|
| `charge / time`      | `KElectricCurrentUnitInstance` | `I = Q / t`                     |
| `charge / current`   | `KTimeUnitInstance`            | `t = Q / I`                     |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f` (صيغة الزمن العكسي) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.*

// مثال واقعي - سعة البطارية: خلية 2000 mAh تخزّن 7200 C.
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance، 7200 C

// كم تدوم عند سحب ثابت مقداره 250 mA؟
battery / (0.25 of amperes)               // KTimeUnitInstance، 28800 s (8 ساعات)

// الشحنة نفسها من التفكيك المكتوب بنوعه ومن التعبير الأصلي A·s:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance، 7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## الفيض الكهربائي

يساوي **الفيض الكهربائي** `Ψ` عبر سطح مغلق الشحنة المحصورة فيه (قانون غاوس، `Ψ = Q`). لذا فهو **مطابق بُعديًا** للشحنة
ويُقاس بالكولوم أيضًا. تُنمذجه KUnit بهذه المجموعة وبالرمز `C`؛ ولا يوجد رمز برمجي منفصل ولا نوع منفصل:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.*

// كرة تحصر 2 µC تحمل فيضًا كهربائيًا مقداره 2 µC.
val psi = 2 of micro.coulombs
psi into micro.coulombs        // 2.0
```

عند قسمته على مساحة، يُعطي الفيض [كثافة الفيض الكهربائي](electricfluxdensity.md) `D = Ψ / A`.

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

(470 of coulombs).toString()   // "470.0 C" (الوحدة الأساسية)
(1 of ampereHours).toString()  // "3600.0 C" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات | Kotlin              | المعنى                                                |
|-----------|---------------------|-------------------------------------------------------|
| `C`       | `coulombs`          | الشحنة، الوحدة الأساسية (رمز مسمّى، كولوم)             |
| `A·s`     | `amperes * seconds` | الشحنة كتيار·زمن (صيغة الضرب)                         |
| `A/Hz`    | `amperes / hertz`   | الشحنة نفسها مكتوبة كتيار مقسوم على تردد (`1/Hz = s`) |
| `mAh`     | `milli.ampereHours` | شحنة ببادئة (مللي أمبير ساعة، سعة البطارية)           |

## انظر أيضًا

- [التيار الكهربائي](ec.md) — عامل التيار في تركيب الشحنة
- [الجهد الكهربائي](voltage.md) — فرق الجهد
- [المقاومة](resistance.md) — قانون أوم يكمل مجموعة الكهرباء
