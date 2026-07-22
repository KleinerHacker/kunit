# إضافة وحدات مخصصة

يشحن kunit اليوم عدّة مجموعات وحدات ([المسافة](units/kinematics/distance.md)،
[الزمن](units/kinematics/time.md)، [التخزين](units/information/storage.md)،
[السرعة](units/kinematics/speed.md)، [معدّل البيانات](units/information/datarate.md))، لكنّ المحرّك
بأكمله (`KUnit`، `KMixedUnitInstance`، فِعلا `of`/`into`، بانيات البادئات) عامّ ومستقلّ عن المجموعة.
إضافة كمّية فيزيائية جديدة تعني اتّباع النمط نفسه. تشرح هذه الصفحة إضافة مجموعة **الكتلة** التوضيحية
(`org.pcsoft.framework.kunit.mass`) — مجموعة بسيطة أحادية البُعد مبنية على غرار مجموعة التخزين.

## 1. أنشئ الحزمة الفرعية وتعداد `KUnit`

تحصل كل مجموعة وحدات على حزمتها الفرعية الخاصّة تحت `org.pcsoft.framework.kunit`، وتُصرَّح وحداتها كـ
`enum class` يُنفّذ `KUnit`. `baseValue` هو معامل التحويل إلى الوحدة الأساسية للمجموعة — والوحدة الأساسية
نفسها لها `baseValue == 1.0`.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** كيلوغرام، وحدة الكتلة الأساسية في SI؛ [baseValue] = 1.0 بالتعريف. */
    KILOGRAM("kg", 1.0),

    /** غرام، 1 g = 0.001 kg. */
    GRAM("g", 0.001),

    /** الرطل الأفوردوبوا الدولي، 1 lb = 0.45359237 kg. */
    POUND("lb", 0.45359237),

    /** الأونصة الأفوردوبوا الدولية، 1 oz = 0.028349523125 kg. */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** الوحدة الأساسية لمجموعة الكتلة؛ تُطبَّع كل القيم الداخلية لـ [KMassUnitInstance] إلى هذه الوحدة. */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. أنشئ صنف الغلاف

يغلّف الغلاف (`KMassUnitInstance`) نسخةَ `KMixedUnitInstance` بـ**التفويض** (`KUnitMeasurable by
instance`) ويُنفّذ `KUnitInstance<KMassUnitInstance>`. يكتب يدويًا فقط الأعضاء الخاصّة بـ `KUnitInstance`
(`plus`/`minus`/`compareTo`) إضافةً إلى تجاوز `scaledBy` (الذي يدعم `of`) و`equals`/`hashCode`/`toString`.
لا يوجد `valueAs`/`toString(target)` — فالقراءة هي فِعل `into` المستقلّ عن المجموعة. انسخ شكل
`KStorageUnitInstance`.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** يدعم `of`: يحجّم القيمة (كيلوغرامات)، ويُعيد النوع نفسه. */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** يبني [KMassUnitInstance] من قيمة مُعبَّر عنها بالفعل بالكيلوغرامات ([KMassUnit.BASE]). */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** يحوّل [KMixedUnitInstance] الكتلة النقيّة إلى [KMassUnitInstance]، مع التطبيع إلى [KMassUnit.BASE]. */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. أضِف رموز القيمة-1 المجرّدة وخصائص باني البادئات

قسّم مفردات الـ DSL إلى ملفّين وفق اصطلاح المشروع: تذهب رموز القيمة-1 المجرّدة إلى
`K...UnitBareValues.kt`، وتذهب امتدادات خصائص باني البادئات إلى `K...UnitExtensions.kt`. معًا يتيحان
للمستدعي كتابة `5 of kilograms` أو `5 of kilo.grams` والقراءة بـ `into`.

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 كيلوغرام ([KMassUnit.KILOGRAM]). */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 غرام ([KMassUnit.GRAM]). */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 رطل ([KMassUnit.POUND]). */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 أونصة ([KMassUnit.OUNCE]). */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt` (الكتلة تقبل أي مقدار، فتُعلَّق الخصائص على الأساس المشترك `KPrefixBuilder`):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** كيلوغرامات ببادئة، مثلًا `kilo.kilograms`. */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** غرامات ببادئة، مثلًا `milli.grams` = 1 mg. */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** أرطال ببادئة. */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** أونصات ببادئة. */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

هذا كل شيء — يمنحك هذا بالفعل `+` و`-` و`*` و`/` والمقارنات وبانيات بادئات SI (`5 of milli.grams`)
والتنقّل ذهابًا وإيابًا عبر `toUnit()`/`toMass()` مجانًا.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance، مُطبَّع إلى الكيلوغرامات
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4. (اختياري) أضِف وحدات خاصّة/مشتقّة

إذا كانت مجموعتك تحوي وحدات مسمّاة شائعة الاستخدام مرتبطة بتحجيم محدّد (مثل الهكتار للمساحة)، فأضِفها
كنُسخ قيمتها 1 مسمّاة — دون الحاجة إلى نوع هدف منفصل:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 طنّ متري (1000 kg). */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. ادمج مع مجموعات أخرى

لأنّ كل شيء ينتهي في النهاية عبر محرّك `KMixedUnitInstance` العامّ، تتركّب مجموعتك الجديدة فورًا مع أي
مجموعة أخرى عبر `*`/`/` — راجع [الوحدات المركبة](mixed-units.md) للقواعد. للحصول على نتيجة عبر-المجموعات
محكومة بالنوع (مثل `mass / volume = density`)، أضِف امتدادات معاملات محكومة بالنوع في
`K...UnitOperators.kt`، على غرار `KSpeedUnitOperators.kt`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// الكثافة = الكتلة / الحجم (KMixedUnitInstance عامّ: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. قائمة تحقّق التسمية والاختبار

- تبدأ كل الأنواع العامّة بحرف `K` (`KMassUnit`، `KMassUnitInstance`، ...)؛ رموز القيمة-1 المجرّدة
  وامتدادات خصائص باني البادئات (`kilograms`، `grams`، ...) مُستثناة وتبقى طبيعية لغويًا.
- غطِّ المجموعة بإجراء اختبار المصفوفة المتقاطعة المُعامَل، المبني عبر `of`/`into` (لا عبر التعداد الخام
  أبدًا): تحويل وحدة → وحدة، طريقة واحدة لكل معامِل ولكل مقارنة على كل زوج وحدات، مصفوفة باني البادئات،
  الحفاظ على النوع في `of`، وحالات خطأ `into` — راجع قسم «إجراء اختبار المصفوفة المتقاطعة المُعامَل» في
  `../../.claude/CLAUDE.md`.
- وثّق كل عضو عامّ بالإنجليزية، بصيغة Markdown، مع أمثلة حيث يكون مفيدًا — خاصّةً المعاملات.
- إذا كانت المجموعة مقيّدة المقدار (مثل التخزين، الذي يرفض البادئات المتناقصة)، فعلّق خصائص وحداتها على
  `KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder` بدلًا من الأساس `KPrefixBuilder`، بحيث تصير
  البادئات غير المسموحة **خطأ تصريف**.
```
