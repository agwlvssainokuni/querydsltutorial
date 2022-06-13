クエリの書き方チュートリアル
============================

使用するスキーマ
================
## テーブル
### 投稿者 author

| # | 論理名     | 物理名      | 型            |
|--:|:-----------|:------------|:--------------|
|  1| ID         | id          | BIGINT        |
|  2| ログインID | login_id    | VARCHAR(100)  |
|  3| 投稿者名   | name        | VARCHAR(100)  |

### TODO todo

| # | 論理名     | 物理名      | 型            |
|--:|:-----------|:------------|:--------------|
|  1| ID         | id          | BIGINT        |
|  2| 投稿者     | posted_by   | VARCHAR(100)  |
|  3| 投稿日時   | posted_at   | TIMESTAMP     |
|  4| 期日       | due_dt      | DATE          |
|  5| 完了日時   | done_at     | TIMESTAMP     |
|  6| 完了フラグ | done_flg    | INTEGER       |
|  7| TODO内容   | description | VARCHAR(1000) |

### 共通カラム

| # | 論理名           | 物理名       | 型        |
|--:|:-----------------|:-------------|:----------|
|  1| 更新日時         | updated_at   | TIMESTAMP |
|  2| 作成日時         | created_at   | TIMESTAMP |
|  3| ロックバージョン | lock_version | INTEGER   |
|  4| 削除フラグ       | deleted_flg  | INTEGER   |

## DBアクセス用クラス
GradleからANTタスクを実行して「Querydsl SQL用メタデータ」を作成します。
具体的には、下記のコマンドを発行します。

```bash:コマンドライン
$ ./gradlew clean codegen
```


SELECT文
========

## 1. 基本的なAPIの呼出し方
結果データをどのような形態で取り出すかにより、「Tupleとして取出す」「Beanとして取出す(QBeanを使う)」の2種類の呼出し方があります。これらの違いは「データの取出し方の指定の仕方の違い (および、取出し方に依存するに抽出するカラムの指定の仕方の違い)」です。
以下、それぞれの呼出しを例示します。

### 1.1 Tupleとして取出す
```Java
/* 抽出条件を組み立てる。 */
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(a.all()).fetch();
```

### 1.2 Beanとして取出す
```Java
/* 抽出条件を組み立てる。 */
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<BAuthor> list = query.select(a).fetch();
```

## 2. SELECT句の書き方
### 2.1 照会するカラムを指定する
#### 2.1.1 カラムを絞って照会する
```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        a.loginId,
        a.name)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.login_id,
    a.name
FROM
    author AS a
```

#### 2.1.2 全てのカラムを照会する
テーブルのメタデータの`all()`メソッドを使用してください。

```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.all())
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.login_id,
    a.name,
    a.updated_at,
    a.created_at,
    a.lock_version,
    a.deleted_flg
FROM
    author AS a
```

#### 2.1.3 アスタリスク「*」を指定して照会する
`Wildcard.all`(Wildcardクラスのstaticフィールド)を使用してください。ただし、この場合、得られるデータはObject[](Objectの配列)型となります。カラム名で値を参照することができないため、積極的な理由がない限りは、`Wildcard.all`の使用は避けてください。
基本的に、全カラムを照会する場合は、アスタリスクではなくテーブルのメタデータの`all()`メソッドを使用してください。

```Java
QAuthor a = new QAuthor("a");

SQLQuery<?> query = queryFactory.from(a);
List<Object[]> list = query.select(
        Wildcard.all)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    *
FROM
    author AS a
```

#### 2.1.4 カラムにエイリアス(別名)を付与する
カラムのメタデータの`as(エイリアス名)`メソッドを使用します。

```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id.as("alias1"),
        a.loginId.as("alias2"),
        a.name.as("alias3"))
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id        AS alias1,
    a.login_id  AS alias2,
    a.name      AS alias3
FROM
    author AS a
```

### 2.2 定数値を指定する
#### 2.2.1 定数値をカラムとして照会する
`Expressions.constant(定数値)`メソッドを使用します。

```Java
Expression<Integer> const1 = Expressions.constant(123456789);
Expression<String> const2 = Expressions.constant("CONST TEXT");

QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        const1,
        const2)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    123456789,
    'CONST TEXT'
FROM
    author AS a
```

#### 2.2.2 定数値のカラムにエイリアス(別名)を付与する
`Expressions.as(定数カラムオブジェクト, エイリアス名)`メソッドを使用してください。
(カラムにエイリアス(別名)を付与する場合にも使えますが、カラムのエイリアスは前述の通りカラムのメタデータの`as(エイリアス名)`メソッドを使用してください)

```Java
Expression<Integer> const1 = Expressions.constant(123456789);
Expression<String> const2 = Expressions.constant("CONST TEXT");

QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        Expressions.as(const1, "alias1"),
        Expressions.as(const2, "alias2"))
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    123456789       AS alias1,
    'CONST TEXT'    AS alias2
FROM
    author AS a
```

### 2.3 カラム(または定数値)に対する算術計算
#### 2.3.1 加減乗除
数値カラムのメタデータ(`NumberExpression`クラス)に下記の算術計算メソッドが定義されています。これを使用してください。

|  #| 演算子    | メソッド           |
|--:|:----------|:-------------------|
|  1| +(加算)   | `add(カラム)`      |
|  2| -(減算)   | `subtract(カラム)` |
|  3| *(乗算)   | `multiply(カラム)` |
|  4| /(除算)   | `divide(カラム)`   |
|  5| MOD(剰余) | `mod(カラム)`      |

```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        a.id.add(2L),
        a.id.subtract(2L),
        a.id.multiply(2L),
        a.id.divide(2L),
        a.id.mod(2L))
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.id + 2,
    a.id - 2,
    a.id * 2,
    a.id / 2,
    MOD(a.id, 2)
FROM
    author AS a
```

#### 2.3.2 計算順序
複数の計算を組合わせる場合、「計算のメソッドを呼出した順序」に従って計算されます。いわゆる四則演算の優先度には従いませんので注意してください。
例えば、「`メタデータA.add(メタデータB).multiply(2)`」は「`(カラムA + カラムB) * 2`」として計算されます。「`カラムA + (カラムB * 2)`」を計算するには「`メタデータA.add(メタデータB.multiply(2))`」の形で指定してください。

```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        a.id.add(2L).multiply(2L),
        a.id.multiply(2L).add(2L),
        a.id.add(2L).multiply(2L).subtract(2L).divide(2L),
        a.id.add(2L).multiply(a.id.subtract(2L)))
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    (a.id + 2) * 2,
    a.id * 2 + 2,
    ((a.id + 2) * 2 - 2) / 2,
    (a.id + 2) * (a.id - 2)
FROM
    author AS a
```

### 2.4 カラム(または定数値)に対する関数適用
#### 2.4.1 関数適用
SQL関数は、カラムのメタデータのインスタンスメソッド、または、staticメソッドとして定義されています。これらの呼出しによりSQL関数の適用を表現します。

```Java
QAuthor a = new QAuthor("a");
SQLQuery<?> query = queryFactory.from(a);
List<Tuple> list = query.select(
        a.id,
        a.loginId,
        a.name,
        a.loginId.length(),
        a.loginId.concat(a.name),
        StringExpressions.lpad(a.loginId, 10, 'X'))
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.login_id,
    a.name,
    LENGTH(a.login_id),
    CONCAT(a.login_id, a.name),
    LPAD(a.login_id, 10, 'X')
FROM
    author AS a
```

#### 2.4.2 数値関数(インスタンスメソッド)
数値カラムのメタデータ(`NumberExpression`クラス)のインスタンスメソッドとして下記の数値関数が定義されています。これを使用してください。

|  #| 関数              | メソッド   |
|--:|:------------------|:-----------|
|  1| ABS(絶対値)       | `abs()`    |
|  2| 符号反転          | `negate()` |
|  3| SQRT(平方根)      | `sqrt()`   |
|  4| FLOOR(整数切下げ) | `floor()`  |
|  5| CEIL(整数切上げ)  | `ceil()`   |
|  6| ROUND(四捨五入)   | `round()`  |

#### 2.4.3 数値関数(staticメソッド)
`MathExpressions`クラスのstaticメソッドとして下記の数値関数が定義されています。これを使用してください。

|  #| 関数               | メソッド                               |
|--:|:-------------------|:---------------------------------------|
|  1| COS(余弦)          | `cos(カラム)`                          |
|  2| SIN(正弦)          | `sin(カラム)`                          |
|  3| TAN(正接)          | `tan(カラム)`                          |
|  4| COT(余接)          | `cot(カラム)`                          |
|  5| ACOS(余弦の逆関数) | `acos(カラム)`                         |
|  6| ASIN(正弦の逆関数) | `asin(カラム)`                         |
|  7| ATAN(正接の逆関数) | `atan(カラム)`                         |
|  8| COSH(双曲線余弦)   | `cosh(カラム)`                         |
|  9| SINH(双曲線正弦)   | `sinh(カラム)`                         |
| 10| TANH(双曲線正接)   | `tanh(カラム)`                         |
| 11| COTH(双曲線余接)   | `coth(カラム)`                         |
| 12| ラジアン→度        | `degrees(カラム)`                      |
| 13| 度→ラジアン        | `radians(カラム)`                      |
| 14| EXP(指数関数)      | `exp(カラム)`                          |
| 15| LN(自然対数)       | `ln(カラム)`                           |
| 16| LOG(対数)          | `log(カラム, 対数の底)`                |
| 17| POWER(べき乗)      | `power(カラム, べき乗数)`              |
| 18| MAX(最大)          | `max(カラム, カラム)`                  |
| 19| MIN(最小)          | `min(カラム, カラム)`                  |
| 20| RANDOM(乱数)       | `random()`, `random(乱数の種)`         |
| 21| ROUND(四捨五入)    | `round(カラム)`, `round(カラム, 桁数)` |
| 22| SIGN(符号)         | `sign(カラム)`                         |


#### 2.4.4 文字列関数(インスタンスメソッド)
文字列カラムのメタデータ(`StringExpression`クラス)のインスタンスメソッドとして下記の文字列関数が定義されています。これを使用してください。

|  #| 関数                  | メソッド                    |
|--:|:----------------------|:----------------------------|
|  1| CONCAT(文字列結合)    | `concat(カラム)`            |
|  2| SUBSTRING(部分文字列) | `substring(カラム, カラム)` |
|  3| LENGTH(文字列長)      | `length()`                  |
|  4| LOWER(小文字変換)     | `lower()`                   |
|  5| UPPER(大文字変換)     | `upper()`                   |
|  6| TRIM(空白文字除去)    | `trim()`                    |


#### 2.4.5 文字列関数(staticメソッド)
`StringExpressions`クラスのstaticメソッドとして下記の文字列関数が定義されています。これを使用してください。

|  #| 関数                    | メソッド                                                         |
|--:|:------------------------|:-----------------------------------------------------------------|
|  1| LTRIM(先頭空白文字除去) | `ltrim(カラム)`                                                  |
|  2| RTRIM(末尾空白文字除去) | `rtrim(カラム)`                                                  |
|  3| LPAD(先頭空白文字追加)  | `lpad(カラム, 長さ(カラム))`, `lpad(カラム, 長さ(カラム), 文字)` |
|  4| RPAD(末尾空白文字追加)  | `rpad(カラム, 長さ(カラム))`, `rpad(カラム, 長さ(カラム), 文字)` |

#### 2.4.6 日時操作関数(インスタンスメソッド)
`DateExpression`クラス、`TimeExpression`クラス、`DateTimeExpression`クラスのインスタンスメソッドとして下記の日時操作関数が定義されています。これを使用してください。

|  #| 関数                         | メソッド        |
|--:|:-----------------------------|:----------------|
|  1| 年                           | `year()`        |
|  2| 月(1-12; JAN-DEC)            | `month()`       |
|  3| 週                           | `week()`        |
|  4| 当年の中の日付(1-366)        | `dayOfYear()`   |
|  5| 当月の中の日付(1-31)         | `dayOfMonth()`  |
|  6| 当週の中の日付(1-7; SUN-SAT) | `dayOfWeek()`   |
|  7| 時(0-23)                     | `hour()`        |
|  8| 分(0-59)                     | `minute()`      |
|  9| 秒(0-59)                     | `second()`      |
| 10| ミリ秒(0-999)                | `milliSecond()` |

#### 2.4.7 日時操作関数(staticメソッド)
`SQLExpressions`クラスのstaticメソッドとして下記の日時操作関数が定義されています。これを使用してください。

|  #| 関数     | メソッド                             |
|--:|:---------|:-------------------------------------|
|  1| 日時加算 | `dateadd(加算部位, カラム, 加算値)`  |
|  2| 日時差分 | `datediff(差分部位, カラム, カラム)` |
|  3| 日時切詰 | `datetrunc(切詰部位, カラム)`        |
|  4| 年加算   | `addYears(カラム, 加算値)`           |
|  5| 月加算   | `addMonths(カラム, 加算値)`          |
|  6| 週加算   | `addWeeks(カラム, 加算値)`           |
|  7| 日加算   | `addDays(カラム, 加算値)`            |
|  8| 時加算   | `addHours(カラム, 加算値)`           |
|  9| 分加算   | `addMinutes(カラム, 加算値)`         |
| 10| 秒加算   | `addSeconds(カラム, 加算値)`         |

また、`DateExpression`クラス、`TimeExpression`クラス、`DateTimeExpression`クラスのstaticメソッドとして下記の日時操作関数が定義されています。これを使用してください。

|  #| 関数                        | メソッド                                                  |
|--:|:----------------------------|:----------------------------------------------------------|
|  1| CURRENT_DATE(現在日付)      | `DateExpression.currentDate(LocalDate.class)`             |
|  2| CURRENT_TIME(現在時刻)      | `TimeExpression.currentTime(LocalTime.class)`             |
|  3| CURRENT_TIMESTAMP(現在日時) | `DateTimeExpression.currentDateTime(LocalDateTime.class)` |

#### 2.4.8 集約関数(インスタンスメソッド)
カラムのメタデータのインスタンスメソッドとして下記の集約関数が定義されています。これを使用してください。

|  #| 関数        | メソッド  |
|--:|:------------|:----------|
|  1| COUNT(件数) | `count()` |
|  2| SUM(合計)   | `sum()`   |
|  3| AVG(平均)   | `avg()`   |
|  4| MAX(最大)   | `max()`   |
|  5| MIN(最小)   | `min()`   |

```Java
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);
query.groupBy(a.postedBy);
List<Tuple> list = query.select(
        a.postedBy,
        a.id.count(),
        a.id.sum(),
        a.postedAt.min(),
        a.postedAt.max())
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.posted_by,
    COUNT(a.id),
    SUM(a.id),
    MIN(a.posted_at),
    MAX(a.posted_at)
FROM
    todo AS a
GROUP BY
    a.posted_by
```

### 2.5 CASE式を指定する
#### 2.5.1 単純CASE式
カラムのメタデータのインスタンスメソッドを使用してCASE式を組み立てください。
具体的には、`メタデータ.when(条件値).then(結果値)...when(条件値).then(結果値).otherwise(結果値)`を使用してCASE式を組み立てます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);

/* CASE式を組立てる。 */
Expression<String> doneDesc = a.doneFlg
        .when(0).then("未実施")
        .when(1).then("実施済")
        .otherwise("不定");

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.doneFlg,
        doneDesc)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.done_flg,
    CASE a.done_flg
        WHEN 0 THEN '未実施'
        WHEN 1 THEN '実施済'
        ELSE '不定'
    END
FROM
    todo AS a
```

#### 2.5.2 検索CASE式
`CaseBuilder`クラスを使用してCASE式を組み立ててください。
具体的には、`Expressions.cases()`メソッドにより`CaseBuilder`インスタンスを生成し、`Expressions.cases().when(条件式).then(結果値)...when(条件式).then(結果値).otherwise(結果値)`の形でCASE式を組み立てます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);

/* CASE式を組立てる。 */
Expression<String> doneDesc = Expressions.cases()
        .when(a.doneFlg.eq(1)).then("実施済")
        .when(a.dueDt.lt(LocalDate.of(2015, 2, 1))).then("未実施(期限内)")
        .otherwise("未実施(期限切)");

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.dueDt,
        a.doneFlg,
        doneDesc)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.due_dt,
    a.done_flg,
    CASE
        WHEN a.done_flg = 1 THEN '実施済'
        WHEN a.due_dt < '2015-02-01' THEN '未実施(期限内)'
        ELSE '未実施(期限切)'
    END
FROM
    todo AS a
```


### 2.6 スカラサブクエリを指定する
`SQLExpressions`クラスを使用して`SubQueryExpression`を作成しサブクエリを組み立てください。`SubQueryExpression`の使い方は`SQLQuery`に準じます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);

/* スカラサブクエリを組立てる。 */
QAuthor b = new QAuthor("b");
SubQueryExpression<String> posterName = SQLExpressions
        .select(b.name).distinct()
        .from(b)
        .where(
                b.loginId.eq(a.postedBy),
                b.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy,
        posterName)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by,
    (
        SELECT
            b.name
        FROM
            author AS b
        WHERE
            b.login_id = a.posted_by
            AND
            b.deleted_flg = 0
    )
FROM
    todo AS a
```

## 3. FROM句の書き方
### 3.1 単一表を指定する
照会対象のテーブルを`SQLQueryFactory.from(テーブルのメタデータ)`の形で指定してください。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by
FROM
    todo AS a
```

### 3.2 複数表を指定して結合する
#### 3.2.1 内部結合
照会対象のテーブルと結合条件を`SQLQueryFactory.from(テーブルのメタデータ).join(テーブルのメタデータ).on(結合条件)`の形で指定してください。
また、取出すカラムを「`テーブル変数.カラムのメタデータ`」の形で指定してください。これによりテーブルをまたがり値を取出すことができます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
QAuthor b = new QAuthor("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .join(b).on(
                b.loginId.eq(a.postedBy),
                b.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy,
        b.name)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by,
    b.name
FROM
    todo AS a
    JOIN author AS b
    ON
        b.login_id = a.posted_by
        AND
        b.deleted_flg = 0
```

#### 3.2.2 左外部結合
照会対象のテーブルと結合条件を`SQLQueryFactory.from(テーブルのメタデータ).leftJoin(テーブルのメタデータ).on(結合条件)`の形で指定してください(内部結合とは`leftJoin()`メソッドを使用する点が異なります)。
また、取出すカラムを「`テーブル変数.カラムのメタデータ`」の形で指定してください。これによりテーブルをまたがり値を取出すことができます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
QAuthor b = new QAuthor("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .leftJoin(b).on(
                b.loginId.eq(a.postedBy),
                b.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy,
        b.name)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by,
    b.name
FROM
    todo AS a
    LEFT JOIN author AS b
    ON
        b.login_id = a.posted_by
        AND
        b.deleted_flg = 0
```

#### 3.2.3 右外部結合
照会対象のテーブルと結合条件を`SQLQueryFactory.from(テーブルのメタデータ).rightJoin(テーブルのメタデータ).on(結合条件)`の形で指定してください(内部結合とは`rightJoin()`メソッドを使用する点が異なります)。
また、取出すカラムを「`テーブル変数.カラムのメタデータ`」の形で指定してください。これによりテーブルをまたがり値を取出すことができます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
QAuthor b = new QAuthor("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .rightJoin(b)
        .on(
                b.loginId.eq(a.postedBy),
                b.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy,
        b.name)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by,
    b.name
FROM
    todo AS a
    RIGHT JOIN author AS b
    ON
        b.login_id = a.posted_by
        AND
        b.deleted_flg = 0
```

#### 3.2.4 全外部結合
照会対象のテーブルと結合条件を`SQLQueryFactory.from(テーブルのメタデータ).fullJoin(テーブルのメタデータ).on(結合条件)`の形で指定してください(内部結合とは`fullJoin()`メソッドを使用する点が異なります)。
また、取出すカラムを「`テーブル変数.カラムのメタデータ`」の形で指定してください。これによりテーブルをまたがり値を取出すことができます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
QAuthor b = new QAuthor("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .fullJoin(b)
        .on(
                b.loginId.eq(a.postedBy),
                b.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedBy,
        b.name)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_by,
    b.name
FROM
    todo AS a
    FULL JOIN author AS b
    ON
        b.login_id = a.posted_by
        AND
        b.deleted_flg = 0
```

### 3.3 SELECT文をFROM句に指定する
以下の3つの手順で指定してください。

*	`SQLExpressions`クラスを使用して、FROM句に指定するSELECT文(`SubQueryExpression`)を組み立てる。
*	`Expressions`クラスのメソッド(`path()`, `stringPath()`, `numberPath()`, `datePath()`, `timePath()`, `dateTimePath()`)を使用して、外側のSELECT文で取り出すカラムを指定するためのパス(メタデータ)を組み立てる。
*	外側のSELECT文の抽出条件を組み立てる。

```Java
/* FROM句に指定するSELECT文を組み立てる。 */
QTodo x = new QTodo("x");
SubQueryExpression<Tuple> internalQuery = SQLExpressions
        .select(
                x.id.as("a_id"),
                x.postedBy.as("a_posted_by"),
                x.postedAt.as("a_posted_at"),
                x.doneFlg.as("a_done_flg"),
                x.doneAt.as("a_done_at"))
        .from(x)
        .where(
                x.deletedFlg.eq(0));

/* 外側のSELECT文で取り出すカラムを指定するためのパス(メタデータ)を組み立てる。 */
SimplePath<Tuple> a = Expressions.path(Tuple.class, "a");
NumberPath<Long> aId = Expressions.numberPath(
        Long.class, a, "a_id");
StringPath aPostedBy = Expressions.stringPath(
        a, "a_posted_by");
DateTimePath<LocalDateTime> aPostedAt = Expressions.dateTimePath(
        LocalDateTime.class, a, "a_posted_at");
NumberPath<Integer> aDoneFlg = Expressions.numberPath(
        Integer.class, a, "a_done_flg");
DateTimePath<LocalDateTime> aDoneAt = Expressions.dateTimePath(
        LocalDateTime.class, a, "a_done_at");

/* 外側のSELECT文の抽出条件を組み立てる。 */
SQLQuery<?> query = queryFactory
        .from(internalQuery, a);
query.where(
        aDoneFlg.eq(1));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        aId,
        aPostedBy,
        aPostedAt,
        aDoneAt)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.a_id,
    a.a_posted_by,
    a.a_posted_at,
    a.a_done_at
FROM
    (
        SELECT
            x.id        AS a_id,
            x.posted_by AS a_posted_by,
            x.posted_at AS a_posted_at,
            x.done_flg  AS a_done_flg,
            x.done_at   AS a_done_at
        FROM
            todo AS x
        WHERE
            x.deleted_flg = 0
    ) AS a
WHERE
    a.a_done_flg = 1
```

## 4. WHERE句の書き方
### 4.1 抽出条件の記述方法
`SQLQuery.where(条件式)`メソッドを使用して抽出条件を指定してください。
なお、`SubQueryExpression`クラスにも同じく`where(条件式)`メソッドがあります。使い方は同じですので、ここでは`SubQueryExpression`クラスについて説明することとします。

#### 4.1.1 単一条件

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(
                a.deletedFlg.eq(0));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedAt,
        a.dueDt,
        a.doneFlg,
        a.doneAt)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_at,
    a.due_dt,
    a.done_flg,
    a.done_at
FROM
    todo AS a
WHERE
    a.deleted_flg = 0
```

#### 4.1.2 複合条件
`where(条件式)`メソッドを複数回呼出すと、指定された抽出条件が「AND」で結合されます。
また、`where()`メソッドの引数として条件式を複数受渡しても、抽出条件が「AND」で結合されます。即ち、`query.where(条件A).where(条件B);`は、`query.where(条件A, 条件B);`と同じ条件になります。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(
                a.deletedFlg.eq(0))
        .where(
                a.doneFlg.eq(1));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedAt,
        a.dueDt,
        a.doneFlg,
        a.doneAt)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_at,
    a.due_dt,
    a.done_flg,
    a.done_at
FROM
    todo AS a
WHERE
    a.deleted_flg = 0
    AND
    a.done_flg = 1
```

#### 4.1.3 条件の組合せ
条件を組合わせるには、下記のメソッド(`BooleanExpression`のインスタンスメソッド)を使用してください。

|  #| 論理演算子         | メソッド                      |
|--:|:-------------------|:------------------------------|
|  1| AND(論理積)        | `and(条件式)`                 |
|  2| OR(論理和)         | `or(条件式)`                  |
|  3| NOT(論理否定)      | `not()`                       |
|  4| AND (条件 OR ...)  | `andAnyOf(条件式, 条件式...)` |
|  5| OR (条件 AND ...)  | `orAllOf(条件式, 条件式...)`  |


指定した条件は、「上記メソッドを呼出した順序」に従って結合されます。いわゆる論理演算子の優先度には従いませんので注意してください。
例えば、「`条件A.or(条件B).and(条件C)`」は「`(条件A OR 条件B) AND 条件C`」として評価されます。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(
                a.doneFlg.eq(1)
                        .or(a.dueDt.goe(LocalDate.of(2015, 2, 1)))
                        .and(a.doneAt.isNull()));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedAt,
        a.dueDt,
        a.doneFlg,
        a.doneAt)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_at,
    a.due_dt,
    a.done_flg,
    a.done_at
FROM
    todo AS a
WHERE
    (
        a.done_flg = 1
        OR
        a.doe_dt >= '2015-02-01'
    )
    AND
    a.done_at IS NULL
```

「`条件A OR (条件B AND 条件C)`」を指定するには「`条件A.or(条件B.and(条件C))`」の形で指定してください。

```Java
/* 抽出条件を組み立てる。 */
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(
                a.doneFlg.eq(1)
                        .or(a.dueDt.goe(LocalDate.of(2015, 2, 1))
                                .and(a.doneAt.isNull())));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.postedAt,
        a.dueDt,
        a.doneFlg,
        a.doneAt)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.posted_at,
    a.due_dt,
    a.done_flg,
    a.done_at
FROM
    todo AS a
WHERE
    a.done_flg = 1
    OR
    (
        a.doe_dt >= '2015-02-01'
        AND
        a.done_at IS NULL
    )
```


### 4.2 条件式
#### 4.2.1 比較演算子
カラムのメタデータのインスタンスメソッドとして下記の比較演算子が定義されています。これを使用してください。

|  #| 比較演算子               | メソッド                                   |
|--:|:-------------------------|:-------------------------------------------|
|  1| =(等値)                  | `eq(式)`                                   |
|  2| <(小なり)                | `lt(式)`                                   |
|  3| <=(小なりイコール)       | `loe(式)`                                  |
|  4| >(大なり)                | `gt(式)`                                   |
|  5| >=(大なりイコール)       | `goe(式)`                                  |
|  6| =  ALL(...), =  ANY(...) | `eqAll(サブクエリ)`,  `eqAny(サブクエリ)`  |
|  7| <  ALL(...), =  ANY(...) | `ltAll(サブクエリ)`,  `ltAny(サブクエリ)`  |
|  8| <= ALL(...), <= ANY(...) | `loeALl(サブクエリ)`, `loeAny(サブクエリ)` |
|  9| >  ALL(...), >  ANY(...) | `gtALl(サブクエリ)`,  `gtAny(サブクエリ)`  |
| 10| >= ALL(...), >= ANY(...) | `goeAll(サブクエリ)`, `goeAny(サブクエリ)` |


#### 4.2.2 LIKE
カラムのメタデータのインスタンスメソッドとして下記の述語が定義されています。これを使用してください。

|  #| 述語               | メソッド      |
|--:|:-------------------|:--------------|
|  1| LIKE(部分一致)     | `like(式)`    |
|  2| NOT LIKE(部分一致) | `notLike(式)` |

#### 4.2.3 IS NULL
カラムのメタデータのインスタンスメソッドとして下記の述語が定義されています。これを使用してください。

|  #| 述語                    | メソッド      |
|--:|:------------------------|:--------------|
|  1| IS NULL(NULL判定)       | `isNull()`    |
|  2| IS NOT NULL(非NULL判定) | `isNotNull()` |

#### 4.2.4 BETWEEN
カラムのメタデータのインスタンスメソッドとして下記の述語が定義されています。これを使用してください。

|  #| 述語                  | メソッド             |
|--:|:----------------------|:---------------------|
|  1| BETWEEN(区間)         | `between(式, 式)`    |
|  2| NOT BETWEEN(区間判定) | `notBetween(式, 式)` |

#### 4.2.5 IN
カラムのメタデータのインスタンスメソッドとして下記の述語が定義されています。これを使用してください。

|  #| 述語             | メソッド                            |
|--:|:-----------------|:------------------------------------|
|  1| IN(含む)         | `in(式...)`, `in(サブクエリ)`       |
|  2| NOT IN(含まない) | `notIn(式...)`, `notIn(サブクエリ)` |

```Java
/* 抽出条件を組み立てる。 */
QAuthor a = new QAuthor("a");
QTodo b = new QTodo("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(
                a.loginId.in(SQLExpressions
                        .select(b.postedBy)
                        .from(b)
                        .where(b.doneFlg.eq(1))));

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.loginId)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.login_id
FROM
    author AS a
WHERE
    a.login_id IN (
        SELECT
            b.posted_by
        FROM
            todo AS b
        WHERE
            b.done_flg = 1
    )
```

#### 4.2.6 EXISTS
サブクエリ`SQLSubQuery`のインスタンスメソッドとして下記の述語が定義されています。これを使用してください。

|  #| 述語                   | メソッド      |
|--:|:-----------------------|:--------------|
|  1| EXISTS(存在する)       | `exists()`    |
|  2| NOT EXISTS(存在しない) | `notExists()` |

```Java
/* 抽出条件を組み立てる。 */
QAuthor a = new QAuthor("a");
QTodo b = new QTodo("b");
SQLQuery<?> query = queryFactory
        .from(a)
        .where(SQLExpressions
                .select(Expressions.constant(1))
                .from(b).where(b.doneFlg.eq(1))
                .where(b.postedBy.eq(a.loginId))
                .exists());

/* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
List<Tuple> list = query.select(
        a.id,
        a.loginId)
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.id,
    a.login_id
FROM
    author AS a
WHERE
    EXISTS (
        SELECT
            1
        FROM
            todo AS b
        WHERE
            b.done_flg = 1
            AND
            b.posted_by = a.login_id
    )
```

## 5. その他の書き方
### 5.1 GROUP BY句
`SQLSQuery`のインスタンスメソッドとして下記の述語が定義されています。これを使用してください。
なお、`SubQueryExpression`クラスにも同じメソッドが定義されています。使い方は同じです。

|  #| 句       | メソッド                        |
|--:|:---------|:--------------------------------|
|  1| GROUP BY | `groupBy(式)`, `groupBy(式...)` |

`groupBy()`メソッドを複数回呼出すと、指定された式が順に集約の条件として追加されます。また、`groupBy()`メソッドの引数として式を複数受渡しても、指定された式が順に集約の条件として追加されます。すなわち、`query.groupBy(式A).groupBy(式B);`は、`query.groupBy(式A, 式B);`と同じ条件になります。

```Java
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);
query.groupBy(a.postedBy);
List<Tuple> list = query.select(
        a.postedBy,
        a.id.count(),
        a.id.sum(),
        a.postedAt.min(),
        a.postedAt.max())
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.posted_by,
    COUNT(a.id),
    SUM(a.id),
    MIN(a.posted_at),
    MAX(a.posted_at)
FROM
    todo AS a
GROUP BY
    a.posted_by
```

### 5.2 HAVING句
`SQLSQuery`のインスタンスメソッドとして下記の述語が定義されています。これを使用してください。
なお、`SubQueryExpression`クラスにも同じメソッドが定義されています。使い方は同じです。

|  #| 句       | メソッド                        |
|--:|:---------|:--------------------------------|
|  1| HAVING | `having(条件式)`, `having(条件式...)` |

`having(条件式)`メソッドを複数回呼出すと、指定された抽出条件が「AND」で結合されます。また、`having()`メソッドの引数として式を複数受渡しても、抽出条件が「AND」で結合されます。即ち、`query.having(条件式A).having(条件式B);`は、`query.having(条件式A, 条件式B);`と同じ条件になります。

```Java
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);
query.groupBy(a.postedBy);
query.having(
        a.id.count().gt(1),
        a.postedAt.max().lt(LocalDateTime.of(2015, 2, 1, 0, 0, 0)));
List<Tuple> list = query.select(
        a.postedBy,
        a.id.count(),
        a.id.sum(),
        a.postedAt.min(),
        a.postedAt.max())
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.posted_by,
    COUNT(a.id),
    SUM(a.id),
    MIN(a.posted_at),
    MAX(a.posted_at)
FROM
    todo AS a
GROUP BY
    a.posted_by
HAVING
    COUNT(a.id) > 1
    AND
    MAX(a.posted_at) < '2015-02-01'
```

### 5.3 ORDER BY句
`SQLSQuery`のインスタンスメソッドとして下記の述語が定義されています。これを使用してください。引数として受渡す順序式は、カラムのメタデータや式のインスタンスメソッド「`asc()`または`desc()`」の返却値として得られます。これはSQLの「`ASC`(昇順)または`DESC`(降順)」に相当します。SQLでは`ASC`, `DESC`を省略できますが(省略時は昇順)、Querydslでは省略できませんので必ず明示してください。
なお、`SubQueryExpression`クラスにも同じメソッドが定義されています。使い方は同じです。

|  #| 句       | メソッド                        |
|--:|:---------|:--------------------------------|
|  1| ORDER BY | `orderBy(順序式)`, `orderBy(順序式...)` |

`orderBy()`メソッドを複数回呼出すと、指定された式が順に並べ替えの条件として追加されます。また、`orderBy()`メソッドの引数として式を複数受渡しても、指定された式が順に並べ替えの条件として追加されます。すなわち、`query.orderBy(式A).orderBy(式B);`は、`query.orderBy(式A, 式B);`と同じ条件になります。

```Java
QTodo a = new QTodo("a");
SQLQuery<?> query = queryFactory.from(a);
query.groupBy(a.postedBy);
query.orderBy(a.id.count().asc());
List<Tuple> list = query.select(
        a.postedBy,
        a.id.count(),
        a.id.sum(),
        a.postedAt.min(),
        a.postedAt.max())
        .fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
SELECT
    a.posted_by,
    COUNT(a.id),
    SUM(a.id),
    MIN(a.posted_at),
    MAX(a.posted_at)
FROM
    todo AS a
GROUP BY
    a.posted_by
ORDER BY
    COUNT(a.id) ASC
```

### 5.4 UNION句
以下の2つの手順で指定してください。

*	`SubQueryExpression`クラスを使用して、UNION句に指定するSELECT文を組み立てる。
*	`SQLQuery.union(サブクエリ...)`メソッド(または`unionAll()`メソッド)を使用してUNION文を組み立てる。


```Java
/* UNION句でつなげるSELECT文を組み立てる。 */
QTodo a = new QTodo("a");
SubQueryExpression<Tuple> queryA = SQLExpressions
        .select(
                a.id,
                a.postedBy)
        .from(a);

QAuthor b = new QAuthor("b");
SubQueryExpression<Tuple> queryB = SQLExpressions
        .select(
                b.id,
                b.loginId)
        .from(b);

/* UNIONを組み立てる。 */
Union<Tuple> query = queryFactory.query().union(queryA, queryB);

/* クエリを発行する。 */
List<Tuple> list = query.fetch();
```

上記Javaコードは下記SQLに相当します。

```SQL
(
    SELECT
        a.id,
        a.posted_by
    FROM
        todo AS a
)
UNION
(
    SELECT
        b.id,
        b.login_id
    FROM
        author AS b
)
```

INSERT文
========

UPDATE文
========

DELETE文
========



以上。


```Java
```

上記Javaコードは下記SQLに相当します。

```SQL
```