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
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class InsertClauseTest {

    @Autowired
    private SQLQueryFactory queryFactory;

    @Test
    public void sec0601_レコード作成() {

        BAuthor before = new BAuthor();
        before.setLoginId("user100");
        before.setName("投稿者１００");

        Long id = queryFactory
                .insert(QAuthor.author)
                .populate(before)
                .executeWithKey(QAuthor.author.id);
        assertNotNull(id);

        BAuthor after = queryFactory
                .selectFrom(QAuthor.author)
                .where(QAuthor.author.id.eq(id))
                .fetchFirst();
        assertEquals("user100", after.getLoginId());
        assertEquals("投稿者１００", after.getName());
    }

}
