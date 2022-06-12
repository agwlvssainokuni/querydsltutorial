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
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;

import cherry.tutorial.querydsl.db.BAuthor;
import cherry.tutorial.querydsl.db.QAuthor;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class BasicUsageTest {

    private final Logger logger = LoggerFactory.getLogger(BasicUsageTest.class);

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec0101_Tupleとして取出す() {
        logger.info("1.1 Tupleとして取出す");

        /* 抽出条件を組み立てる。 */
        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<Tuple> list = query.select(a.all()).fetch();

        /* クエリの結果を表示する。 */
        for (Tuple tuple : list) {
            Long valId = tuple.get(a.id);
            String valLoginId = tuple.get(a.loginId);
            String valName = tuple.get(a.name);
            LocalDateTime valUpdatedAt = tuple.get(a.updatedAt);
            LocalDateTime valCreatedAt = tuple.get(a.createdAt);
            Integer valLockVersion = tuple.get(a.lockVersion);
            Integer valDeletedFlg = tuple.get(a.deletedFlg);
            logger.info(
                    "{}: loginId={}, name={}, updatedAt={}, createdAt={}, lockVersion={}, deletedFlg={}",
                    valId, valLoginId, valName, valUpdatedAt, valCreatedAt, valLockVersion, valDeletedFlg);
        }
    }

    @Test
    public void sec0102_Beanとして取出す() {
        logger.info("1.2 Beanとして取出す");

        /* 抽出条件を組み立てる。 */
        QAuthor a = new QAuthor("a");
        SQLQuery<?> query = queryFactory.from(a);

        /* 取出すカラムとデータの取出し方を指定してクエリを発行する。 */
        List<BAuthor> list = query.select(a).fetch();

        /* クエリの結果を表示する。 */
        for (BAuthor entity : list) {
            Long valId = entity.getId();
            String valLoginId = entity.getLoginId();
            String valName = entity.getName();
            LocalDateTime valUpdatedAt = entity.getUpdatedAt();
            LocalDateTime valCreatedAt = entity.getCreatedAt();
            Integer valLockVersion = entity.getLockVersion();
            Integer valDeletedFlg = entity.getDeletedFlg();
            logger.info(
                    "{}: loginId={}, name={}, updatedAt={}, createdAt={}, lockVersion={}, deletedFlg={}",
                    valId, valLoginId, valName, valUpdatedAt, valCreatedAt, valLockVersion, valDeletedFlg);
        }
    }

}
