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

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.Union;

import cherry.tutorial.querydsl.db.QAuthor;
import cherry.tutorial.querydsl.db.QTodo;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class AdvancedUsageTest {

    private final Logger logger = LoggerFactory.getLogger(AdvancedUsageTest.class);

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec0501_GROUPBY() {

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
            Timestamp valMinPostedAt = tuple.get(a.postedAt.min());
            Timestamp valMaxPostedAt = tuple.get(a.postedAt.max());
            logger.info(
                    "{}: COUNT(id)={}, SUM(id)={}, MIN(postedAt)={}, MAX(postedAt)={}",
                    valPostedBy, valCount, valSum, valMinPostedAt, valMaxPostedAt);
        }
    }

    @Test
    public void sec0502_HAVING() {

        QTodo a = new QTodo("a");
        Timestamp basedtm = new Timestamp(
                LocalDateTime.of(2015, 2, 1, 0, 0, 0)
                        .toEpochSecond(ZoneId.systemDefault().getRules()
                                .getOffset(Instant.now()))
                        * 1000);
        SQLQuery<?> query = queryFactory.from(a);
        query.groupBy(a.postedBy);
        query.having(
                a.id.count().gt(1),
                a.postedAt.max().lt(basedtm));
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
            Timestamp valMinPostedAt = tuple.get(a.postedAt.min());
            Timestamp valMaxPostedAt = tuple.get(a.postedAt.max());
            logger.info(
                    "{}: COUNT(id)={}, SUM(id)={}, MIN(postedAt)={}, MAX(postedAt)={}",
                    valPostedBy, valCount, valSum, valMinPostedAt, valMaxPostedAt);
        }
    }

    @Test
    public void sec0503_ORDERBY() {

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

        for (Tuple tuple : list) {
            String valPostedBy = tuple.get(a.postedBy);
            Long valCount = tuple.get(a.id.count());
            Long valSum = tuple.get(a.id.sum());
            Timestamp valMinPostedAt = tuple.get(a.postedAt.min());
            Timestamp valMaxPostedAt = tuple.get(a.postedAt.max());
            logger.info(
                    "{}: COUNT(id)={}, SUM(id)={}, MIN(postedAt)={}, MAX(postedAt)={}",
                    valPostedBy, valCount, valSum, valMinPostedAt, valMaxPostedAt);
        }
    }

    @Test
    public void sec0504_UNION() {

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
        @SuppressWarnings("unchecked")
        Union<Tuple> query = queryFactory.query().union(queryA, queryB);

        /* クエリを発行する。 */
        List<Tuple> list = query.fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valName = tuple.get(a.postedBy);
            logger.info(
                    "{}: name={}",
                    valId, valName);
        }
    }

}
