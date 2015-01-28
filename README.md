# db-transaction

Spring默认Transactional事物管理机制不支持多条SQL语句回滚。

只要能保持多条SQL语句用一个connection，就可以做到一次全部回滚。但是一条链接只能对一个数据库，所以此事物不支持跨数据库。
