# التدفق المغناطيسي

الحزمة: `org.pcsoft.framework.kunit.electric.magneticflux`
الوحدة الأساسية: **ويبر** (`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`)

النوع: **وحدة مركّبة**

التدفق المغناطيسي وحدة **مركّبة**: التركيب `mass · length² · time⁻² · current⁻¹`
(`kg·m²·s⁻²·A⁻¹`). يغلّف `KMagneticFluxUnitInstance` كائن `KMixedUnitInstance` مكوّن من أربعة حدود —
`KMassUnit.BASE` (غرام) بالأس `+1`، و`KDistanceUnit.BASE` (متر) بالأس `+2`، و`KTimeUnit.BASE` (ثانية)
بالأس `-2`، و`KElectricCurrentUnit.BASE` (أمبير) بالأس `-1`. ولأن مكوّن الكتلة في المكتبة مطبَّع إلى **غرامات** (وليس
كيلوغرامات)، يُقسَم الناتج القياسي على 1000 للوصول إلى الويبر؛ وتُطبَّع القيمة المخزّنة دائمًا إلى الويبر.

## إنشاء تدفق مغناطيسي

يُنشأ التدفق برمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز بقيمة 1 (تُستخدم مع
`of`/`into`):

| التدفق المغناطيسي | الرمز  | الرمز البرمجي |        1 وحدة بالويبر |
|-------------------|--------|--------------:|----------------------:|
| ويبر              | `Wb`   |      `webers` |                   1.0 |
| ماكسويل (CGS-EMU) | `Mx`   |    `maxwells` |                1.0e-8 |
| قطب الوحدة        | `pole` |   `unitPoles` | 1.2566370614359173e-7 |

تدعم الوحدات المسمّاة بادئات النظام الدولي عبر `KPrefixBuilder` (`milli.webers`، `micro.webers`،
`kilo.maxwells`، …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## تفكيكات متعددة

يمكن الوصول إلى التدفق المغناطيسي عبر عدة **تفكيكات مكافئة**، وجميعها تُنتج تدفقًا متساوي القيمة:

| التعبير                        | نوع النتيجة                 | المعنى                                                               |
|--------------------------------|-----------------------------|----------------------------------------------------------------------|
| `voltage * time`               | `KMagneticFluxUnitInstance` | قانون فاراداي للحث `Φ = U · t` (تبادلي)                              |
| `voltage / frequency`          | `KMagneticFluxUnitInstance` | صيغة الزمن العكسي (`V/Hz = V·s`)                                     |
| `inductance * current`         | `KMagneticFluxUnitInstance` | `Φ = L · I` (انظر [المحاثة](inductance.md))                          |
| `fluxDensity * area`           | `KMagneticFluxUnitInstance` | `Φ = B · A` (انظر [كثافة التدفق المغناطيسي](magneticfluxdensity.md)) |
| `mass·length²/(time²·current)` | عبر `.toMagneticFlux()`     | التعبير الأصلي القياسي `kg·m²·s⁻²·A⁻¹`                               |

تُعيد الصيغ المكتوبة بأنواع صريحة تدفقًا مباشرة. أما التعبير الأصلي بالكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق عبر `toMagneticFlux()` (الذي يتعرّف فقط على الصيغة القياسية ويرمي
`IllegalStateException` خلاف ذلك). جميع المسارات متساوية القيمة.

تربط العمليات العكسية بين الجهد والزمن والتدفق:

| التعبير            | نوع النتيجة            | المعنى                    |
|--------------------|------------------------|---------------------------|
| `flux / time`      | `KVoltageUnitInstance` | الجهد المستحث `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | نظير صيغة الزمن العكسي    |
| `flux / voltage`   | `KTimeUnitInstance`    | `t = Φ / U`               |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.magneticflux.*

// مثال واقعي - ملف إشعال: تدفق قلب مقداره 20 mWb ينهار خلال 4 ms يحرّض 5 V.
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance، 5 V

// قانون الحث محلولًا لإيجاد التدفق:
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance، 2 Wb

// نفس التدفق من التردد، وكتعبير أصلي kg·m²·s⁻²·A⁻¹:
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

(20 of webers).toString()     // "20.0 Wb" (الوحدة الأساسية)
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `⁻¹`)، ويرمز `·` للضرب و `/` للكسر. وحيثما أمكن كتابة الكمية ككسر وكحاصل ضرب بأسس سالبة، تُذكر
الصيغتان المتكافئتان في Kotlin.

| الرياضيات       | Kotlin                                                                | المعنى                                              |
|-----------------|-----------------------------------------------------------------------|-----------------------------------------------------|
| `Wb`            | `webers`                                                              | التدفق المغناطيسي، الوحدة الأساسية (رمز مسمّى، ويبر) |
| `V·s`           | `(10 of volts) * (0.2 of seconds)`                                    | التدفق كجهد·زمن (قانون الحث)                        |
| `kg·m²/(s²·A)`  | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | التدفق ككتلة·طول² / (زمن²·تيار) (صيغة الكسر)        |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)`   | نفس التدفق كحاصل ضرب خالص                           |
| `mWb`           | `milli.webers`                                                        | تدفق ببادئة (ميلي ويبر)                             |
