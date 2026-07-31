# الانتشارية الحرارية

الحزمة: `org.pcsoft.framework.kunit.common.diffusivity`
الوحدة الأساسية: **متر مربع لكل ثانية** (`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

النوع: **وحدة مركّبة**

الانتشارية الحرارية `α` تُبيّن *سرعة* انتشار تغيّر درجة الحرارة عبر مادّة ما — بخلاف
[الموصلية الحرارية](thermal-conductivity.md)، التي تُبيّن *كمّية* الحرارة المتدفّقة في الحالة المستقرّة. الوحدة: `m²/s`.
تُعرَّف كالتالي

```
α = λ / (ρ · c_p)
```

يغلّف `KDiffusivityUnitInstance` كائن `KMixedUnitInstance` مكوّن من حدّين بالضبط بالصيغة القياسية `distance² · time⁻¹`
(`m²·s⁻¹`)، مطبَّعًا دائمًا إلى m²/s.

!!! note "مجموعة واحدة، مجالان"
تعيش المجموعة في `common.diffusivity` لأنّ الكمّية ذاتها موثّقة في مجالين: بصفتها **الانتشارية الحرارية** هنا،
وبصفتها [اللزوجة الحركية](../mechanics/kinematic-viscosity.md)
`ν = η / ρ` في الميكانيكا. تشترك القراءتان في مفردات `KDiffusivityUnit`، بما في ذلك كتابتَي الستوكس التقليديتين.

## الوحدات المسمّاة

| الوحدة                | الرمز   |                الرمز البرمجي | 1 وحدة بـ m²/s |
|-----------------------|---------|-----------------------------:|---------------:|
| متر مربع لكل ثانية    | `m²/s`  |      `squareMetersPerSecond` |            1.0 |
| ملّيمتر مربع لكل ثانية | `mm²/s` | `squareMillimetersPerSecond` |           1e-6 |
| قدم مربّع لكل ساعة     | `ft²/h` |          `squareFeetPerHour` |   ≈ 2.58064e-5 |

تسرد جداول المواد قيمة `α` بوحدة mm²/s، وهي بالضبط `micro.squareMetersPerSecond`. تدعم جميع الوحدات نطاق بادئات النظام
الدولي الكامل.

## قيم نموذجية

| المادّة        |            α |
|---------------|-------------:|
| النحاس        |  ≈ 116 mm²/s |
| الفولاذ       |   ≈ 14 mm²/s |
| الزجاج        | ≈ 0.34 mm²/s |
| الماء         | ≈ 0.14 mm²/s |
| الصوف المعدني |  ≈ 1.2 mm²/s |

## مثال واقعي: مدى سرعة توازن النحاس

للنحاس λ = 401 W/ (m·K)، وρ = 8960 kg/m³، وc_p = 385 J/ (kg·K).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// المعكوس: استرجاع الموصلية من الانتشارية
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## الحساب باستخدام الوحدات المجاورة

العلاقة المُعرِّفة **ثلاثية** (`α = λ / (ρ · c_p)`)، لذا وبخلاف كل مجموعة أخرى هنا لا يمكن تمثيلها بمعامل ثنائي واحد دون
ابتكار نوع وسيط للسعة الحرارية الحجمية `ρ · c_p` (J/ (m³·K))، وهو ما لا تُمثّله هذه المكتبة. لذا تُعرَض العلاقة كدوالّ
مسمّاة ومحكومة بالنوع بشكل صارم:

| الدالّة                                                               | نوع النتيجة                         | المعنى              |
|----------------------------------------------------------------------|-------------------------------------|---------------------|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KDiffusivityUnitInstance`          | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance`  | `λ = α · ρ · c_p`   |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance`              | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

تصبّ الدوال الأربع جميعها في نفس المصنع المُطبِّع كأيّ تفكيك آخر.

## التفكيكات

كلا التفكيكين يُنتجان نفس النسخة المحكومة بالنوع والمتساوية القيمة.

| التفكيك              | الصيغة                                  | النتيجة                    |
|----------------------|-----------------------------------------|----------------------------|
| `λ / (ρ · c_p)`      | دالّة مكتوبة بنوع صريح `diffusivityWith` | `KDiffusivityUnitInstance` |
| `distance² · time⁻¹` | تعبير أصلي + `toDiffusivity()`          | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K)، ρ = 1 kg/m³، c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native // true - كلاهما 1.0 m²/s
```

## العمليات

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## تنسيق toString

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الوحدة ومكوّناتها رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس بحروف
يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر. وحيثما أمكن كتابة الكمّية ككسر وكحاصل ضرب بأسس سالبة،
تُذكر الصيغتان المتكافئتان في Kotlin.

| الرياضيات           | Kotlin                                                  | المعنى                               |
|---------------------|---------------------------------------------------------|--------------------------------------|
| `m²/s`              | `squareMetersPerSecond`                                 | الانتشارية الحرارية، الوحدة الأساسية |
| `m²·s⁻¹`            | `(meters pow 2) / seconds`                              | نفس الكمّية بالأبعاد الأساسية         |
| `mm²/s`             | `squareMillimetersPerSecond`                            | ملّيمتر مربع لكل ثانية (جداول المواد) |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)`           | العلاقة المُعرِّفة                      |
| `λ = α · ρ · c_p`   | `alpha.conductivityWith(density, heat)`                 | الموصلية من الانتشارية               |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)`                 | الكثافة من الانتشارية                |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | السعة الحرارية النوعية من الانتشارية |
