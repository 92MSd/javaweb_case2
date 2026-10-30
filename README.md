# 学生选课系统 JDBC 实验（实验2）

基于 **JDBC + Maven** 的学生选课系统基础信息管理项目。采用 **entity + dao** 分层，对 `user`（用户）、`major`（专业）、`classinfo`（班级）、`teacher`（教师）四组表完成 **CRUD 操作与单元测试**，覆盖**存储过程调用**与**事务操作**。

## 一、开发环境配置

| 项目 | 说明 |
|---|---|
| 操作系统 | Windows 10+ |
| JDK | 21（pom.xml 中 maven.compiler.source/target=21） |
| IDE | IntelliJ IDEA（内置 Maven） |
| 数据库 | MySQL 8.0（本机 3306 端口） |
| 构建工具 | Maven（通过 IDEA 内置 Maven 运行） |
| JDBC 驱动 | mysql-connector-java 8.0.23 |
| 单元测试 | JUnit Jupiter 5.x |
| Web 依赖 | jakarta.servlet-api 6.1.0（provided）、Jersey 4.0.0-M2（本项目无页面，仅供容器部署） |

### 数据库连接配置

位于 `src/main/java/com/example/demo2/DBConnection.java`：

- 驱动：`com.mysql.cj.jdbc.Driver`
- URL：`jdbc:mysql://localhost:3306/javawebdb?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true`
- 用户名 / 密码：`root` / `666888`（按自己环境修改）

## 二、数据库设计

数据库名：`javawebdb`（MySQL 8）。共 4 张表：

### 1. user 表（复用实验1的表结构）

| 字段 | 类型 | 约束 |
|---|---|---|
| id | int | 主键，自增 |
| username | varchar(50) | 非空 |
| password | varchar(50) | 非空 |
| sex | varchar(10) | 可空 |
| age | int | 可空 |
| birthday | varchar(20) | 可空 |
| type | varchar(20) | 非空（学生 / 教师 / 管理员） |

### 2. major 表（专业）

| 字段 | 类型 | 约束 |
|---|---|---|
| specno | varchar(10) | 主键 |
| specname | varchar(20) | 非空 |
| createyear | char(4) | 可空 |

### 3. classinfo 表（班级）

| 字段 | 类型 | 约束 |
|---|---|---|
| classname | varchar(20) | 主键 |
| specno | varchar(10) | 外键 `fk_classinfo_spec` → major.specno |

### 4. teacher 表（教师）

| 字段 | 类型 | 约束 |
|---|---|---|
| tno | varchar(10) | 主键（兼作登录用户名） |
| tname | varchar(20) | 非空 |
| tdept | varchar(20) | 可空 |
| tschool | varchar(50) | 可空 |

### 存储过程 sp_delete_major（删除专业，专业下存在班级时禁止删除）

```sql
DROP PROCEDURE IF EXISTS sp_delete_major;
CREATE PROCEDURE sp_delete_major(IN p_specno VARCHAR(10))
BEGIN
  IF EXISTS (SELECT 1 FROM classinfo WHERE specno = p_specno) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '该专业下还有班级，不能删除';
  END IF;
  DELETE FROM major WHERE specno = p_specno;
END
```

## 三、项目结构

```
demo2
├── pom.xml                          # Maven 配置（JDBC 驱动 + JUnit 依赖）
└── src
    ├── main
    │   ├── java
    │   │   ├── com/example/demo2/DBConnection.java   # 数据库连接公共类
    │   │   ├── dao/                                  # 数据访问层
    │   │   │   ├── UserDao.java      # 用户 CRUD + 登录校验 + 模糊查询
    │   │   │   ├── MajorDao.java     # 专业 CRUD + 调用存储过程 sp_delete_major
    │   │   │   ├── ClassDao.java     # 班级 CRUD（外键约束）
    │   │   │   └── TeacherDao.java   # 教师 CRUD + 事务（同时插入教师账号）
    │   │   └── entity/               # 实体层（与表字段一一对应）
    │   │       ├── User.java
    │   │       ├── Major.java
    │   │       ├── Classinfo.java
    │   │       └── Teacher.java
    │   └── resources/META-INF/beans.xml
    └── test/java                     # 单元测试（JUnit 5）
        ├── UserDaoTest.java          # 5 个测试
        ├── MajorDaoTest.java         # 6 个测试（含存储过程、有班级时删除专业抛异常）
        ├── ClassDaoTest.java         # 3 个测试
        └── TeacherDaoTest.java       # 5 个测试（含事务回滚验证）
```

## 四、运行测试说明

1. **准备数据库**：在 MySQL 中创建 `javawebdb`，导入 `user`、`major`、`classinfo`、`teacher` 四张表的结构和模拟数据，并执行上文 SQL 创建存储过程 `sp_delete_major`。
2. **修改连接配置**：按本机实际账号密码修改 `DBConnection.java`。
3. **打开项目**：IDEA 打开 `demo2` 目录，等待 Maven 自动下载依赖（或点击 Maven 面板刷新按钮）。
4. **运行测试**：打开 `src/test/java` 下的 4 个测试类，分别右键 → **Run**。全部测试通过（绿色）即表示四组 CRUD、存储过程调用与事务操作均正确实现。

## 五、实现要点

- 使用 `PreparedStatement` 预编译 SQL，避免 SQL 注入。
- 使用 try-with-resources 自动关闭 `Connection / PreparedStatement / ResultSet`。
- 删除专业通过 `CallableStatement` 调用存储过程 `sp_delete_major`，专业下存在班级时抛出异常。
- 插入教师时在同一事务中完成「插入教师 + 插入教师账号（默认密码 123）」，任一步失败整体回滚。
