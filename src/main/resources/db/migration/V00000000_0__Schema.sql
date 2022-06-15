-- Project Name : QuerydslTutorial
-- Date/Time    : 2022/06/15 19:16:00
-- Author       : agwlvssainokuni
-- RDBMS Type   : IBM Db2
-- Application  : A5:SQL Mk-2

-- 投稿者
CREATE TABLE author (
  id BIGINT NOT NULL AUTO_INCREMENT
  , login_id VARCHAR(100) NOT NULL
  , name VARCHAR(100) NOT NULL
  , updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL
  , created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL
  , lock_version INTEGER DEFAULT 0 NOT NULL
  , CONSTRAINT author_PKC PRIMARY KEY (id)
) ;

CREATE INDEX author_IX1
  ON author(login_id);

-- TODO
CREATE TABLE todo (
  id BIGINT NOT NULL AUTO_INCREMENT
  , posted_by VARCHAR(100) NOT NULL
  , posted_at TIMESTAMP NOT NULL
  , due_dt DATE NOT NULL
  , done_at TIMESTAMP
  , done_flg INTEGER DEFAULT 0 NOT NULL
  , description VARCHAR(1000) NOT NULL
  , updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL
  , created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL
  , lock_version INTEGER DEFAULT 0 NOT NULL
  , CONSTRAINT todo_PKC PRIMARY KEY (id)
) ;

CREATE INDEX todo_IX1
  ON todo(posted_by);

COMMENT ON TABLE author IS '投稿者';
COMMENT ON COLUMN author.id IS 'ID';
COMMENT ON COLUMN author.login_id IS 'ログインID';
COMMENT ON COLUMN author.name IS '投稿者名';
COMMENT ON COLUMN author.updated_at IS '更新日時';
COMMENT ON COLUMN author.created_at IS '作成日時';
COMMENT ON COLUMN author.lock_version IS 'ロックバージョン';

COMMENT ON TABLE todo IS 'TODO';
COMMENT ON COLUMN todo.id IS 'ID';
COMMENT ON COLUMN todo.posted_by IS '投稿者';
COMMENT ON COLUMN todo.posted_at IS '投稿日時';
COMMENT ON COLUMN todo.due_dt IS '期日';
COMMENT ON COLUMN todo.done_at IS '完了日時';
COMMENT ON COLUMN todo.done_flg IS '完了フラグ';
COMMENT ON COLUMN todo.description IS 'TODO内容';
COMMENT ON COLUMN todo.updated_at IS '更新日時';
COMMENT ON COLUMN todo.created_at IS '作成日時';
COMMENT ON COLUMN todo.lock_version IS 'ロックバージョン';

