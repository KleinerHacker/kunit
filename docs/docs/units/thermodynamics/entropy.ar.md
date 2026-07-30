# الإنتروبيا

الحزمة: `org.pcsoft.framework.kunit.thermo.heatcapacity`
الوحدة الأساسية: **جول لكل كلفن** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

النوع: **وحدة مركّبة**

تقيس الإنتروبيا `S` مدى تشتّت الطاقة في نظام ما. وحدتها `J/K` — **مطابقة في البُعد** لـ
[السعة الحرارية](heat-capacity.md).

## لماذا لا تملك الإنتروبيا نوعًا خاصًا بها

يُنمذج KUnit الإنتروبيا عمدًا باستخدام `KHeatCapacityUnitInstance` بدلًا من نوع منفصل
`KEntropyUnitInstance`. والسبب هو عقد التعرّف على الصيغة في هذه المكتبة:

* لكل مجموعة قياسية **صيغة قياسية واحدة** من الأبعاد الأساسية، و
* تتعرّف `toX()` تحديدًا على تلك الصيغة.

تشترك الإنتروبيا والسعة الحرارية في نفس الصيغة القياسية `mass¹ · distance² · time⁻² · temperature⁻¹`.
ولو وُجد نوعان لنفس الصيغة القياسية لأصبح التعبير الأصلي غامضًا — إذ ستتطابق كل من `toHeatCapacity()`
وافتراضية `toEntropy()` مع نفس الوحدة المختلطة، دون أن تكون إحداهما أصحّ من الأخرى. نوع واحد يُبقي
دورة التحويل حتمية.

لذا فالتمييز بين الكمّيتين هو مسألة *ما تسمّي به متغيّرك*، وليس *نوع* ما تعيده لك المكتبة — تمامًا
كما هو الحال في ترميز الفيزياء، حيث تُكتب كلتاهما بـ J/K.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// both are KHeatCapacityUnitInstance
```

## مثال واقعي: انصهار الجليد

يمتصّ انصهار 1 kg من الجليد عند 273.15 K مقدارًا من الحرارة الكامنة قدره 334 kJ. تغيّر الإنتروبيا هو
`ΔS = Q / T`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // as an interval from absolute zero

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance, in J/K
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// The reverse: how much heat does that entropy change carry at the melting point?
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "درجة حرارة مطلقة في `ΔS = Q / T`"
    تُقسَم الإنتروبيا على درجة حرارة **مطلقة**، لكنّ حاصلات القسمة في هذه المكتبة تستخدم مجموعة
    *فرق* درجة الحرارة (`KTemperatureDifferenceUnit`) — إذ لا يمكن لمقياس أفيني أن يظهر في المقام.
    عبّر عن قراءة الكلفن المطلقة كفترة من الصفر المطلق، كما في المثال أعلاه:
    `KTemperatureDifference.ofKelvin(273.15)`. وفي مقياس الكلفن يتطابق الاثنان عدديًا، وهذا بالضبط سبب
    اعتماد الديناميكا الحرارية على مقياس الكلفن.

## انظر أيضًا

* [السعة الحرارية](heat-capacity.md) — النوع الذي تشاركه الإنتروبيا، مع جدول الوحدات الكامل وجميع
  التفكيكات وسطح المعاملات الكامل
* [السعة الحرارية المولية](molar-heat-capacity.md) — الصيغة لكل مول (إنتروبيا مولية)
* [السعة الحرارية النوعية](specific-heat-capacity.md) — الصيغة لكل كيلوغرام (إنتروبيا نوعية)
* [الطاقة](energy.md) — بسط `ΔS = Q / T`

## الترميز

يبيّن الجدول أدناه كيف تُكتب هذه الكمّية رياضيًا مقابل كتابتها في Kotlin باستخدام KUnit. تُكتب الأسس
بحروف يونيكود العلوية (`²`، `³`، `⁻¹`)، ويرمز `·` للضرب و`/` للكسر.

| الرياضيات | Kotlin | المعنى |
|---|---|---|
| `J/K` | `joulesPerKelvin` | الإنتروبيا، الوحدة الأساسية (مشتركة مع السعة الحرارية) |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | نفس الكمّية بالأبعاد الأساسية |
| `ΔS = Q / T` | `latentHeat / meltingPoint` | تغيّر الإنتروبيا من الحرارة ÷ درجة الحرارة |
| `Q = ΔS · T` | `entropyChange * meltingPoint` | الحرارة من تغيّر الإنتروبيا × درجة الحرارة |
