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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.querydsl.sql.SQLQueryFactory;

import cherry.tutorial.querydsl.db.BAuthor;
import cherry.tutorial.querydsl.db.QAuthor;

@ExtendWith(SpringExtension.class)
@JdbcTest
@ContextConfiguration(classes = QuerydslConfiguration.class)
public class UpdateClauseTest {

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec0701_レコード更新_単一レコード() {

        BAuthor author = new BAuthor();
        author.setLoginId("user100");
        author.setName("投稿者１００");

        Long id = queryFactory
                .insert(QAuthor.author)
                .populate(author)
                .executeWithKey(QAuthor.author.id);
        assertNotNull(id);

        List<BAuthor> before = queryFactory
                .selectFrom(QAuthor.author)
                .fetch();

        author.setLoginId("user101");
        author.setName("投稿者１０１");
        queryFactory
                .update(QAuthor.author)
                .populate(author)
                .where(QAuthor.author.id.eq(id))
                .execute();

        List<BAuthor> after = queryFactory
                .selectFrom(QAuthor.author)
                .fetch();

        for (BAuthor b : before) {
            BAuthor a = after.stream().filter(e -> e.getId().equals(b.getId())).findFirst().get();
            if (b.getId().equals(id)) {
                assertNotEquals(b.getLoginId(), a.getLoginId());
                assertNotEquals(b.getName(), a.getName());
                assertEquals(author.getLoginId(), a.getLoginId());
                assertEquals(author.getName(), a.getName());
            } else {
                assertEquals(b.getLoginId(), a.getLoginId());
                assertEquals(b.getName(), a.getName());
            }
        }
    }

    @Test
    public void sec0702_レコード更新_複数レコード() {

        List<BAuthor> before = queryFactory
                .selectFrom(QAuthor.author)
                .fetch();

        queryFactory
                .update(QAuthor.author)
                .set(QAuthor.author.loginId, QAuthor.author.loginId.concat("a"))
                .set(QAuthor.author.name, QAuthor.author.name.concat(QAuthor.author.name))
                .where(QAuthor.author.id.mod(2L).eq(1L))
                .execute();

        List<BAuthor> after = queryFactory
                .selectFrom(QAuthor.author)
                .fetch();

        for (BAuthor b : before) {
            BAuthor a = after.stream().filter(e -> e.getId().equals(b.getId())).findFirst().get();
            if (b.getId().longValue() % 2L == 1L) {
                assertNotEquals(b.getLoginId(), a.getLoginId());
                assertNotEquals(b.getName(), a.getName());
                assertEquals(b.getLoginId() + "a", a.getLoginId());
                assertEquals(b.getName() + b.getName(), a.getName());
            } else {
                assertEquals(b.getLoginId(), a.getLoginId());
                assertEquals(b.getName(), a.getName());
            }
        }
    }

}
