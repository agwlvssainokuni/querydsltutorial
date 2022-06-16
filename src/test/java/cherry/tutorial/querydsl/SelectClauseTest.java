/*
 * Copyright 2022 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.tutorial.querydsl;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

import cherry.tutorial.querydsl.db.QAuthor;
import cherry.tutorial.querydsl.db.QTodo;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class SelectClauseTest {

    private final Logger logger = LoggerFactory.getLogger(SelectClauseTest.class);

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec020101_カラムを絞って照会する() {
        logger.info("2.1.1 カラムを絞って照会する");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.id,
                a.loginId,
                a.name)
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            String valName = tuple.get(a.name);
            LocalDateTime valUpdatedAt = tuple.get(a.updatedAt);
            LocalDateTime valCreatedAt = tuple.get(a.createdAt);
            Integer valLockVersion = tuple.get(a.lockVersion);
            logger.info(
                    "{}: loginId={}, name={}, updatedAt={}, createdAt={}, lockVersion={}",
                    valId, valLoginId, valName, valUpdatedAt, valCreatedAt, valLockVersion);
        }
    }

    @Test
    public void sec020102_全てのカラムを照会する() {
        logger.info("2.1.2 全てのカラムを照会する");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.all())
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            String valName = tuple.get(a.name);
            LocalDateTime valUpdatedAt = tuple.get(a.updatedAt);
            LocalDateTime valCreatedAt = tuple.get(a.createdAt);
            Integer valLockVersion = tuple.get(a.lockVersion);
            logger.info(
                    "{}: loginId={}, name={}, updatedAt={}, createdAt={}, lockVersion={}",
                    valId, valLoginId, valName, valUpdatedAt, valCreatedAt, valLockVersion);
        }
    }

    @Test
    public void sec020103_アスタリスクを指定して照会する() {
        logger.info("2.1.3 アスタリスク「*」を指定して照会する");

        QAuthor a = new QAuthor("a");

        SQLQuery<?> query = queryFactory.from(a);
        List<Object[]> list = query.select(
                Wildcard.all)
                .fetch();

        for (Object[] tuple : list) {
            Long valId = (Long) tuple[0];
            String valLoginId = (String) tuple[1];
            String valName = (String) tuple[2];
            LocalDateTime valUpdatedAt = (LocalDateTime) tuple[3];
            LocalDateTime valCreatedAt = (LocalDateTime) tuple[4];
            Integer valLockVersion = (Integer) tuple[5];
            logger.info(
                    "{}: loginId={}, name={}, updatedAt={}, createdAt={}, lockVersion={}",
                    valId, valLoginId, valName, valUpdatedAt, valCreatedAt, valLockVersion);
        }
    }

    @Test
    public void sec020104_カラムにエイリアスを付与する() {
        logger.info("2.1.4 カラムにエイリアス(別名)を付与する");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.id.as("alias1"),
                a.loginId.as("alias2"),
                a.name.as("alias3"))
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id.as("alias1"));
            String valLoginId = tuple.get(a.loginId.as("alias2"));
            String valName = tuple.get(a.name.as("alias3"));
            logger.info(
                    "{}: loginId={}, name={}",
                    valId, valLoginId, valName);
            assertNull(tuple.get(a.id));
            assertNull(tuple.get(a.loginId));
            assertNull(tuple.get(a.name));
        }
    }

    @Test
    public void sec020201_定数値をカラムとして照会する() {
        logger.info("2.2.1 定数値をカラムとして照会する");

        Expression<Integer> const1 = Expressions.constant(123456789);
        Expression<String> const2 = Expressions.constant("CONST TEXT");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.id,
                const1,
                const2)
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Integer valConst1 = tuple.get(const1);
            String valConst2 = tuple.get(const2);
            logger.info(
                    "{}: const1={}, const2={}",
                    valId, valConst1, valConst2);
        }
    }

    @Test
    public void sec020202_定数値のカラムにエイリアスを付与する() {
        logger.info("2.2.2 定数値のカラムにエイリアス(別名)を付与する");

        Expression<Integer> const1 = Expressions.constant(123456789);
        Expression<String> const2 = Expressions.constant("CONST TEXT");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.id,
                Expressions.as(const1, "alias1"),
                Expressions.as(const2, "alias2"))
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Integer valConst1 = tuple.get(Expressions.as(const1, "alias1"));
            String valConst2 = tuple.get(Expressions.as(const2, "alias2"));
            logger.info(
                    "{}: const1={}, const2={}",
                    valId, valConst1, valConst2);
            assertNull(tuple.get(const1));
            assertNull(tuple.get(const2));
        }
    }

    @Test
    public void sec020301_カラムに対する算術計算_加減乗除() {
        logger.info("2.3.1 加減乗除");

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

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Long valAdd = tuple.get(a.id.add(2L));
            Long valSubtract = tuple.get(a.id.subtract(2L));
            Long valMultiply = tuple.get(a.id.multiply(2L));
            Long valDivide = tuple.get(a.id.divide(2L));
            Long valMod = tuple.get(a.id.mod(2L));
            logger.info(
                    "{}: +2={}, -2={}, *2={}, /2={}, %2={}",
                    valId, valAdd, valSubtract, valMultiply, valDivide, valMod);
        }
    }

    @Test
    public void sec020302_カラムに対する算術計算_計算順序() {
        logger.info("2.3.2 計算順序");

        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);
        List<Tuple> list = query.select(
                a.id,
                a.id.add(2L).multiply(2L),
                a.id.multiply(2L).add(2L),
                a.id.add(2L).multiply(2L).subtract(2L).divide(2L),
                a.id.add(2L).multiply(a.id.subtract(2L)))
                .fetch();

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Long val1 = tuple.get(a.id.add(2L).multiply(2L));
            Long val2 = tuple.get(a.id.multiply(2L).add(2L));
            Long val3 = tuple.get(a.id.add(2L).multiply(2L).subtract(2L)
                    .divide(2L));
            Long val4 = tuple.get(a.id.add(2L).multiply(a.id.subtract(2L)));
            logger.info(
                    "{}: (id+2)*2={}, id*2+2={}, ((id+2)*2-2)/2={}, (id+2)*(id-2)={}",
                    valId, val1, val2, val3, val4);
        }
    }

    @Test
    public void sec020401_カラムに対する関数適用() {
        logger.info("2.4.1 関数適用");

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

        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            String valName = tuple.get(a.name);
            Integer valLength = tuple.get(a.loginId.length());
            String valConcat = tuple.get(a.loginId.concat(a.name));
            String valLpad = tuple.get(StringExpressions.lpad(a.loginId, 10, 'X'));
            logger.info(
                    "{}: loginId={}, name={}, LENGTH(loginId)={}, CONCAT(loginId, name)={}, LPAD(loginId, 10, X)={}",
                    valId, valLoginId, valName, valLength, valConcat, valLpad);
        }
    }

    @Test
    public void sec020408_カラムに対する関数適用_集約関数() {
        logger.info("2.4.8 集約関数(インスタンスメソッド)");

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

        for (Tuple tuple : list) {
            String valPostedBy = tuple.get(a.postedBy);
            Long valCount = tuple.get(a.id.count());
            Long valSum = tuple.get(a.id.sum());
            LocalDateTime valMinPostedAt = tuple.get(a.postedAt.min());
            LocalDateTime valMaxPostedAt = tuple.get(a.postedAt.max());
            logger.info(
                    "{}: COUNT(id)={}, SUM(id)={}, MIN(postedAt)={}, MAX(postedAt)={}",
                    valPostedBy, valCount, valSum, valMinPostedAt, valMaxPostedAt);
        }
    }

    @Test
    public void sec020501_CASE式を指定する_単純CASE式() {
        logger.info("2.5.1 単純CASE式");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            String valDoneDesc = tuple.get(doneDesc);
            logger.info(
                    "{}: doneFlg={}, doneDesc(CASE)={}",
                    valId, valDoneFlg, valDoneDesc);
        }
    }

    @Test
    public void sec020502_CASE式を指定する_検索CASE式() {
        logger.info("2.5.2 検索CASE式");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            LocalDate valDueDt = tuple.get(a.dueDt);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            String valDoneDesc = tuple.get(doneDesc);
            logger.info(
                    "{}: dueDt={}, doneFlg={}, doneDesc(CASE)={}",
                    valId, valDueDt, valDoneFlg, valDoneDesc);
        }
    }

    @Test
    public void sec0206_スカラサブクエリを指定する() {
        logger.info("2.6 スカラサブクエリを指定する");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        SQLQuery<?> query = queryFactory.from(a);

        /* スカラサブクエリを組立てる。 */
        QAuthor b = new QAuthor("b");
        SubQueryExpression<String> posterName = SQLExpressions
                .select(b.name).distinct()
                .from(b)
                .where(
                        b.loginId.eq(a.postedBy));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy,
                posterName)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            String valPosterName = tuple.get(posterName);
            logger.info(
                    "{}: postedBy={}, posterName={}",
                    valId, valPostedBy, valPosterName);
        }
    }

}
