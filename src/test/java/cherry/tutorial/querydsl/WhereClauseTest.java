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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

import cherry.tutorial.querydsl.db.QAuthor;
import cherry.tutorial.querydsl.db.QTodo;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class WhereClauseTest {

    private final Logger logger = LoggerFactory.getLogger(WhereClauseTest.class);

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec040101_抽出条件の記述方法_単一条件() {
        logger.info("4.1.1 単一条件");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Timestamp valPostedAt = tuple.get(a.postedAt);
            Date valDueDt = tuple.get(a.dueDt);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            Timestamp valDoneAt = tuple.get(a.doneAt);
            logger.info(
                    "{}: postedAt={}, dueDt={}, doneFlg={}, doneAt={}",
                    valId, valPostedAt, valDueDt, valDoneFlg, valDoneAt);
        }
    }

    @Test
    public void sec040102_抽出条件の記述方法_複合条件() {
        logger.info("4.1.2 複合条件");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Timestamp valPostedAt = tuple.get(a.postedAt);
            Date valDueDt = tuple.get(a.dueDt);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            Timestamp valDoneAt = tuple.get(a.doneAt);
            logger.info(
                    "{}: postedAt={}, dueDt={}, doneFlg={}, doneAt={}",
                    valId, valPostedAt, valDueDt, valDoneFlg, valDoneAt);
        }
    }

    @Test
    public void sec040103_抽出条件の記述方法_条件の組合せ_1() {
        logger.info("4.1.3 条件の組合せ");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        Date basedt = new Date(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60 * 1000);
        SQLQuery<?> query = queryFactory
                .from(a)
                .where(
                        a.doneFlg.eq(1)
                                .or(a.dueDt.goe(basedt))
                                .and(a.doneAt.isNull()));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedAt,
                a.dueDt,
                a.doneFlg,
                a.doneAt)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Timestamp valPostedAt = tuple.get(a.postedAt);
            Date valDueDt = tuple.get(a.dueDt);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            Timestamp valDoneAt = tuple.get(a.doneAt);
            logger.info(
                    "{}: postedAt={}, dueDt={}, doneFlg={}, doneAt={}",
                    valId, valPostedAt, valDueDt, valDoneFlg, valDoneAt);
        }
    }

    @Test
    public void sec040103_抽出条件の記述方法_条件の組合せ_2() {
        logger.info("4.1.3 条件の組合せ");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        Date basedt = new Date(LocalDate.of(2015, 2, 1).toEpochDay() * 24 * 60 * 60 * 1000);
        SQLQuery<?> query = queryFactory
                .from(a)
                .where(
                        a.doneFlg.eq(1)
                                .or(a.dueDt.goe(basedt)
                                        .and(a.doneAt.isNull())));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedAt,
                a.dueDt,
                a.doneFlg,
                a.doneAt)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            Timestamp valPostedAt = tuple.get(a.postedAt);
            Date valDueDt = tuple.get(a.dueDt);
            Integer valDoneFlg = tuple.get(a.doneFlg);
            Timestamp valDoneAt = tuple.get(a.doneAt);
            logger.info(
                    "{}: postedAt={}, dueDt={}, doneFlg={}, doneAt={}",
                    valId, valPostedAt, valDueDt, valDoneFlg, valDoneAt);
        }
    }

    @Test
    public void sec040205_条件式_IN() {
        logger.info("4.2.5 IN");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            logger.info(
                    "{}: loginId={}",
                    valId, valLoginId);
        }
    }

    @Test
    public void sec040206_条件式_EXISTS() {
        logger.info("4.2.6 EXISTS");

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            logger.info(
                    "{}: loginId={}",
                    valId, valLoginId);
        }
    }

}
