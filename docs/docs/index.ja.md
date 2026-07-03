# kunit

**kunit** は、単なる数値ではなく物理単位で計算できるようにする Kotlin フレームワークです(Java からも利用可能)。
メートル、マイル、平方メートルを単純な `Double` 値として扱い、すべての呼び出し元が同じ単位を使っていることを
期待する代わりに、kunit は値とともに単位情報を保持し、変換・乗算・次元管理を代わりに行います。

## なぜ kunit なのか

物理量を裸の数値として扱うのはエラーの元です。変換せずにメートルとマイルを誤って加算したり、面積と長さを
加算してしまうことは容易に起こります。kunit は単位を型の一部にすることでこの問題を解決します。

- **型安全な演算。** 異なる単位グループや指数間の `+`、`-` は、誤った数値を静かに返す代わりに
  `IllegalStateException` を投げます。
- **自動変換。** `5.meters() + 3.miles()` はそのまま動作します - 両方のオペランドは内部的に正規化されるため、
  結合前に手動で単位を変換する必要はありません。
- **自由な乗算・除算。** 単位の乗算・除算は*常に*許可されており、結果の物理次元(指数)を自動的に追跡します。
  例: `長さ * 長さ` は面積になります。
- **すべての `Number` 型をサポート。** `Int`、`Long`、`Float`、`Double`、その他任意の `Number` 型から
  値を構築できます。内部では常に `Double` に正規化されます。
- **完全な SI 接頭辞テーブル**、Quetta(Q) から Quecto(q) まで、任意の単位と汎用的に組み合わせ可能です。
- **名前付きの特殊単位**(ヘクタール、リットル、エーカーなど)を、便利なグループ・指数依存の入出力ターゲットとして
  提供し、基礎となる生の指数表現を置き換えることはありません。

## 中核となる概念

kunit は2つの中心的な型を中心に構築されています。

- **`KUnitInstance`** - *混合単位*(Mischeinheit): `Double` の基本値と、それぞれ整数の指数と対になった
  1つ以上の `KUnit`(例: 速度の `m^1 * s^-1`)。これは他のすべてを支える汎用エンジンです。
- **`KUnit`** - 単位グループに属する単一の「純粋な」単位(例: メートルは長さグループに属する)。具体的な
  単位グループは `enum class ... : KUnit`(例: `KLengthUnit`)としてモデル化されます。

すべての単位グループは、さらに**ラッパークラス**(例: `KLengthUnitInstance`)を提供します。これは単一の
単位グループに限定された `KUnitInstance` をカプセル化し、常にそのグループの基本単位に正規化されます。
これはほとんどの場合に使用する型です - 現在提供されている単位については[定義済み単位](units/length.md)を、
汎用の `KUnitInstance` エンジンを直接使用すべき場合については[混合単位](mixed-units.md)を参照してください。

新しい物理量(質量や時間など)のサポートを追加したい場合は、[カスタム単位の追加](custom-units.md)の
ステップバイステップの説明を参照してください。

## クイックスタート

モジュールを依存関係として追加する(またはプロジェクト/ソースセットとして含める)、そして必要な単位グループの
語彙をインポートします。

### 長さ

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// 任意の Number 型から純粋な長さの値を作成
val distance = 5.meters()
val trip = 10.miles()

// 演算子: 同じグループ・同じ指数内での自動変換
val total = distance + trip          // KLengthUnitInstance、メートルに正規化
val diff = trip - distance

// 比較
val isFarther = trip > distance      // true

// 特定の単位で値を読み取る
println(total.valueIn(KUnitPrefix.KILO with meters)) // 例: 21.0467...
println(total.valueIn(yards))                         // 例: 23018.4...

// 純粋な単位の乗算・除算は混合単位(KUnitInstance)を生成する
val area = distance.toKUnitInstance() * trip.toKUnitInstance()

// 面積(指数2)と体積(指数3)のための特殊単位
val plot = 3.hectares()
println(plot.valueIn(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters()
println(tank.valueIn(KLengthDerivedUnit.US_GALLON))
```

### SI 接頭辞

```kotlin
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.length.meters
import org.pcsoft.framework.kunit.length.toKLengthUnit

// "5 kilo meters" -> KPrefixBuilder -> KUnitInstance -> KLengthUnitInstance
val fiveKm = (5 kilo meters).toKUnitInstance().toKLengthUnit()
println(fiveKm.value) // 5000.0(メートルに正規化)
```

### 混合単位

```kotlin
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

// 混合単位を手動で構成する例、平方メートル(長さ^1 * 長さ^1)
val speed = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // 指数が加算される -> 長さ^2
```

## チェックアウトとビルド

```bash
git clone <repository-url>
cd kunit
```

kunit は Gradle を使用します(ラッパーがリポジトリに含まれているため、ローカルに Gradle をインストールする
必要はありません):

```bash
# ビルド
./gradlew build          # Windows: gradlew.bat build

# テストのみ実行
./gradlew test            # Windows: gradlew.bat test
```

toolchain 25 を解決できる JDK が必要です(`foojay-resolver` プラグインが必要に応じて自動的にダウンロードします)。
