# تدفّق الحرارة

الحزمة: `org.pcsoft.framework.kunit.common.power`
الوحدة الأساسية: **واط** (`KPowerUnit.BASE == KPowerUnit.WATT`)

النوع: **وحدة مركّبة**

تدفّق الحرارة `Q̇` (يُسمّى أيضًا القدرة الحرارية أو معدّل الحرارة) هو كمّية الحرارة المنقولة لكل وحدة
زمن: `W`. وهو **مطابق من الناحية البُعدية والفيزيائية لـ[القدرة](power.md)** — طاقة لكل زمن — لذا
يُنمذجه KUnit بـ`KPowerUnitInstance`.

## لماذا لا يملك تدفّق الحرارة نوعًا خاصًا به

تدفّق الحرارة ليس كمّية منفصلة، بل هو قدرة تصادف أن تكون حرارية. توجد صيغة قياسية واحدة بالضبط
`mass¹ · distance² · time⁻³`، ونوع ثانٍ فوقها سيجعل `toPower()` غامضة دون إضافة أي فيزياء جديدة. سواء
كان الواط يصف محرّكًا كهربائيًا أو ليزرًا أو مشعّعًا فهذا سياق، وليس بُعدًا.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // mechanical power
val radiator = 1500 of watts    // heat flow
// both are KPowerUnitInstance
```

## مثال واقعي: مشعّع تدفئة

يعمل مشعّع بقدرة اسمية 1500 W لمدّة 4 ساعات. كم من الطاقة يسلّم، وما كثافة التدفّق الحراري التي
ينتجها عبر سطحه البالغ 0.6 m²؟

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ (= 6 kWh)

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## أين يظهر تدفّق الحرارة في هذا المجال

| التعبير | نوع النتيجة | المعنى |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | تدفّق الحرارة من الحرارة ÷ المدّة |
| `power * time` | `KEnergyUnitInstance` | الحرارة المسلَّمة عبر مدّة |
| `power / area` | `KHeatFluxDensityUnitInstance` | [كثافة التدفّق الحراري](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance` | إجمالي تدفّق الحرارة عبر سطح |

خسارة الحرارة عبر جدار هي السلسلة الكلاسيكية: يعطي
[معامل انتقال الحرارة](heat-transfer-coefficient.md) مضروبًا في فرق درجة حرارة
[كثافة التدفّق الحراري](heat-flux-density.md)، وضرب ذلك في المساحة يعطي تدفّق الحرارة بالواط.

## انظر أيضًا

* [القدرة](power.md) — النوع الذي يشاركه تدفّق الحرارة، مع جدول الوحدات الكامل وجميع التفكيكات
  وسطح المعاملات الكامل
* [كثافة التدفّق الحراري](heat-flux-density.md) — تدفّق الحرارة لكل وحدة مساحة
* [معامل انتقال الحرارة](heat-transfer-coefficient.md) — كثافة التدفّق الحراري لكل كلفن
* [الطاقة](energy.md) — تدفّق الحرارة متكاملًا عبر الزمن

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الكمّية رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس
بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `W` | `watts` | تدفّق الحرارة، الوحدة الأساسية (مشتركة مع القدرة) |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | نفس الكمّية بالأبعاد الأساسية |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime` | تدفّق الحرارة من الحرارة ÷ المدّة |
| `Q = Q̇ · t` | `radiator * runtime` | الحرارة من تدفّق الحرارة × المدّة |
| `q̇ = Q̇ / A` | `radiator / surface` | كثافة التدفّق الحراري من تدفّق الحرارة ÷ المساحة |
