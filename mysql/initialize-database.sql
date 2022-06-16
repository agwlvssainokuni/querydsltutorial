-- (1) チュートリアル用DB作成
CREATE DATABASE tutorial
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_as_cs
;

-- (2) チュートリアル用DBユーザ作成
CREATE USER tutorial
IDENTIFIED BY 'password'
;

-- (3) チュートリアル用DBユーザにチュートリアル用DBの利用権限を付与
GRANT ALL ON tutorial.*
TO tutorial
;

-- (4) チュートリアル用DBユーザにパフォーマンスダッシュボード類の参照権限を付与
GRANT SELECT ON performance_schema.*
TO tutorial
;

GRANT SELECT, EXECUTE ON sys.*
TO tutorial
;

GRANT PROCESS ON *.*
TO tutorial
;
