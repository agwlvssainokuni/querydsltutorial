-- Project Name : QuerydslTutorial
-- Date/Time    : 2022/06/16 22:22:06
-- Author       : agwlvssainokuni
-- RDBMS Type   : MySQL
-- Application  : A5:SQL Mk-2

-- 投稿者
CREATE TABLE author (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID'
  , login_id VARCHAR(100) NOT NULL COMMENT 'ログインID'
  , name VARCHAR(100) NOT NULL COMMENT '投稿者名'
  , updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL COMMENT '更新日時'
  , created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '作成日時'
  , lock_version INT DEFAULT 0 NOT NULL COMMENT 'ロックバージョン'
  , CONSTRAINT author_PKC PRIMARY KEY (id)
) COMMENT '投稿者' ;

CREATE INDEX author_IX1
  ON author(login_id);

-- TODO
CREATE TABLE todo (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID'
  , posted_by VARCHAR(100) NOT NULL COMMENT '投稿者'
  , posted_at DATETIME NOT NULL COMMENT '投稿日時'
  , due_dt DATE NOT NULL COMMENT '期日'
  , done_at DATETIME COMMENT '完了日時'
  , done_flg INT DEFAULT 0 NOT NULL COMMENT '完了フラグ'
  , description VARCHAR(1000) NOT NULL COMMENT 'TODO内容'
  , updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) NOT NULL COMMENT '更新日時'
  , created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) NOT NULL COMMENT '作成日時'
  , lock_version INT DEFAULT 0 NOT NULL COMMENT 'ロックバージョン'
  , CONSTRAINT todo_PKC PRIMARY KEY (id)
) COMMENT 'TODO' ;

CREATE INDEX todo_IX1
  ON todo(posted_by);

