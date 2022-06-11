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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplatesRegistry;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;

@Configuration
public class QuerydslConfiguration {

    @Bean
    public SQLQueryFactory sqlQueryFactory(DataSource dataSource, SQLExceptionTranslator translator)
            throws SQLException {
        SQLTemplates templates;
        try (Connection connection = dataSource.getConnection()) {
            SQLTemplatesRegistry registry = new SQLTemplatesRegistry();
            templates = registry.getTemplates(connection.getMetaData());
        }
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator(translator));
        Supplier<Connection> connProvider = new SpringConnectionProvider(dataSource);
        SQLQueryFactory sqf = new SQLQueryFactory(configuration, connProvider);
        return sqf;
    }

}
