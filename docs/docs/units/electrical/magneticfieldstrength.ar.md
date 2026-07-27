# شدّة المجال المغناطيسي

الحزمة: `org.pcsoft.framework.kunit.magneticfieldstrength`
الوحدة الأساسية: **أمبير لكل متر** (`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

النوع: **وحدة مركّبة**

شدّة المجال المغناطيسي (مجال المغنطة `H`) وحدة **مركّبة**: التركيب `current · length⁻¹` (`A/m`).
يغلّف `KMagneticFieldStrengthUnitInstance` نسخة `KMixedUnitInstance` من حدّين — `KElectricCurrentUnit.BASE`
(أمبير) بالأس `+1` و`KDistanceUnit.BASE` (متر) بالأس `-1`. تُطبَّع القيمة المخزَّنة دائمًا إلى أمبير لكل متر.

صفحات ذات صلة: [التيار الكهربائي](ec.md) و[المسافة](../kinematics/distance.md) هما المجموعتان المكوِّنتان لهذه الوحدة.

## بناء شدّة مجال مغناطيسي

تُبنى شدّة المجال من رمز مسمّى أو من تفكيك (انظر أدناه). تبقى الوحدات المسمّاة كرموز قيمتها 1
(تُستخدم مع `of`/`into`):

| شدّة المجال المغناطيسي | الرمز | الرمز البرمجي | 1 وحدة بـ A/m |
|---|---|---:|---:|
| أمبير لكل متر | `A/m` | `amperesPerMeter` | 1.0 |
| أورستد (CGS-EMU) | `Oe` | `oersteds` | 79.57747154594767 |
| جيلبرت لكل سنتيمتر | `Gb/cm` | `gilbertsPerCentimeter` | 79.57747154594767 |
| أمبير-لفّة لكل بوصة | `At/in` | `ampereTurnsPerInch` | 39.37007874015748 |

تدعم الوحدات المسمّاة سوابق النظام الدولي عبر `KPrefixBuilder` (مثل `kilo.amperesPerMeter` و`milli.oersteds`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## تفكيكات متعدّدة

يمكن الوصول إلى شدّة المجال المغناطيسي عبر عدّة **تفكيكات مكافئة** تُنتج جميعها القيمة نفسها:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | العلاقة التعريفية `H = I / l` |
| `current·length⁻¹` | عبر `.toMagneticFieldStrength()` | التعبير الأصلي القياسي `A·m⁻¹` |

تُعيد صيغة المعامل المحكومة بالنوع شدّة مجال مباشرةً. أمّا التعبير الأصلي الكامل فيبقى `KMixedUnitInstance`
عامًّا ويُضيَّق بـ `toMagneticFieldStrength()` (التي تتعرّف على الصيغة القياسية فقط وترمي
`IllegalStateException` خلاف ذلك). كلا المسارين متساويان في القيمة.

تربط المعاملات العكسية بين التيار والطول وشدّة المجال:

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l` |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | الصيغة التبادلية |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.magneticfieldstrength.*

// مثال واقعي - ملفّ من 500 لفّة يمرّ فيه تيار 2 A على طول 0.25 m:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance، 4000 A/m

// شدّة المجال نفسها بالتعبير الأصلي A·m⁻¹:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## المعاملات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance (يخرج من المجموعة)
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m" (الوحدة الأساسية)
```

## الترميز

يوضّح الجدول أدناه كيفية كتابة هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بأحرف يونيكود علوية (`²`، `³`، `⁻¹`)، ويدلّ `·` على الضرب و`/` على الكسر. وحيثما أمكن كتابة كمّية ككسر وكجداء بأسس سالبة، تُذكر الصيغتان المكافئتان في Kotlin.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `A/m` | `amperesPerMeter` | شدّة المجال المغناطيسي، الوحدة الأساسية (رمز مسمّى) |
| `A/m` | `(amperes pow 1) / (meters pow 1)` | شدّة المجال بوصفها تيار / طول (صيغة الكسر) |
| `A·m⁻¹` | `(amperes pow 1) * (meters pow -1)` | شدّة المجال نفسها كجداء صرف |
| `kA/m` | `kilo.amperesPerMeter` | شدّة مجال بسابقة (كيلوأمبير لكل متر) |
