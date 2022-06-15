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
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

import cherry.tutorial.querydsl.db.QAuthor;
import cherry.tutorial.querydsl.db.QTodo;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class FromClauseTest {

    private final Logger logger = LoggerFactory.getLogger(FromClauseTest.class);

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec0301_単一表を指定する() {
        logger.info("3.1 単一表を指定する");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        SQLQuery<?> query = queryFactory.from(a);

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            logger.info(
                    "{}: postedBy={}",
                    valId, valPostedBy);
        }
    }

    @Test
    public void sec030201_複数表を指定して結合する_内部結合() {
        logger.info("3.2.1 内部結合");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        QAuthor b = new QAuthor("b");
        SQLQuery<?> query = queryFactory
                .from(a)
                .join(b).on(
                        b.loginId.eq(a.postedBy));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy,
                b.name)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            String valPosterName = tuple.get(b.name);
            logger.info(
                    "{}: postedBy={}, posterName={}",
                    valId, valPostedBy, valPosterName);
        }
    }

    @Test
    public void sec030202_複数表を指定して結合する_左外部結合() {
        logger.info("3.2.2 左外部結合");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        QAuthor b = new QAuthor("b");
        SQLQuery<?> query = queryFactory
                .from(a)
                .leftJoin(b).on(
                        b.loginId.eq(a.postedBy));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy,
                b.name)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            String valPosterName = tuple.get(b.name);
            logger.info(
                    "{}: postedBy={}, posterName={}",
                    valId, valPostedBy, valPosterName);
        }
    }

    @Test
    public void sec030203_複数表を指定して結合する_右外部結合() {
        logger.info("3.2.3 右外部結合");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        QAuthor b = new QAuthor("b");
        SQLQuery<?> query = queryFactory
                .from(a)
                .rightJoin(b)
                .on(
                        b.loginId.eq(a.postedBy));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy,
                b.name)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            String valPosterName = tuple.get(b.name);
            logger.info(
                    "{}: postedBy={}, posterName={}",
                    valId, valPostedBy, valPosterName);
        }
    }

    // @Test
    public void sec030204_複数表を指定して結合する_全外部結合() {
        logger.info("3.2.4 全外部結合");

        /* 抽出条件を組み立てる。 */
        QTodo a = new QTodo("a");
        QAuthor b = new QAuthor("b");
        SQLQuery<?> query = queryFactory
                .from(a)
                .fullJoin(b)
                .on(
                        b.loginId.eq(a.postedBy));

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(
                a.id,
                a.postedBy,
                b.name)
                .fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valPostedBy = tuple.get(a.postedBy);
            String valPosterName = tuple.get(b.name);
            logger.info(
                    "{}: postedBy={}, posterName={}",
                    valId, valPostedBy, valPosterName);
        }
    }

    @Test
    public void sec0303_SELECT文をFROM句に指定する() {
        logger.info("3.3 SELECT文をFROM句に指定する");

        /* FROM句に指定するSELECT文を組み立てる。 */
        QTodo x = new QTodo("x");
        SubQueryExpression<Tuple> internalQuery = SQLExpressions
                .select(
                        x.id.as("a_id"),
                        x.postedBy.as("a_posted_by"),
                        x.postedAt.as("a_posted_at"),
                        x.doneFlg.as("a_done_flg"),
                        x.doneAt.as("a_done_at"))
                .from(x);

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

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(aId);
            String valPostedBy = tuple.get(aPostedBy);
            LocalDateTime valPostedAt = tuple.get(aPostedAt);
            LocalDateTime valDoneAt = tuple.get(aDoneAt);
            logger.info(
                    "{}: postedBy={}, postedAt={}, doneAt={}",
                    valId, valPostedBy, valPostedAt, valDoneAt);
        }
    }

}
