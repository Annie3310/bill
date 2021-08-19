# 账单

该项目是基于 Spring Boot 的账单接口, 提供多用户, 多账户.

可以使用快捷指令来做该接口的客户端, 具体功能在接口描述中.

## 已知问题

- 因为使用 JWT 做身份验证, 所以修改密码后原 Token 还可以继续使用 (可能并不是问题)

## 使用说明

- 因为没有前端, 所以无法做验证码, 无法防止无限请求注册和登录接口, 故关闭注册接口
- 限制每天每用户访问 `get_token (登录)` 接口的次数为 15 (每个 Token 2 小时过期)

## 用户认证

**!! 需要在请求头中添加**

获取 token 见 `user 接口 --> get_token`

| 字段名|用途 |
| :---:|:---: |
| token | 验证 |

## bill 接口

接口 URL: localhost:8080/bill/

### income (Post)

入账接口

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---:|:---:|:---:|:---: |
| money | 金额 | 是 | 不能为负数, 且只能有 2 位小数 |
| account | 账户 | 是 | 必须是用户已有的账户 |
| description | 说明 | 否 | 对该笔记录的说明 |

### expense (Post)

支出接口 (实际与入账调用的是同一个 dao 层方法, 只是 type (0: 支出, 1: 收入) 不同)

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| money | 金额 | 是 | 不能为负数, 且只能有 2 位小数 |
| account | 账户 | 是 | 必须是用户已有的账户 |
| description | 说明 | 否 | 对该笔记录的说明 |

### transfer (Post)

转账接口, 因该接口是面向单用户的接口, 所以只能在自己的账号中对对自己的多个账户进行转账.

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| money | 金额 | 是 | 不能为负数, 且只能有 2 位小数 |
| from | 转出账户 | 是 | 转账的支出账户, 不能与 to 账户名相同 |
| to | 转入账户 | 是 | 转账的收入账户, 不能与 from 账户名相同 |
| description | 说明 | 否 | 对该笔记录的说明 |

### sum (Get)

获取当前总资产的详情

### filter (Post)

多条件模糊查找

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| startDate | 查找的起始时间 | 否 | yyyy-MM-dd, 非此格式会报错 |
| endDate | 查找的结束时间 | 否 | yyyy-MM-dd, 非此格式会报错 |
| account | 账户 | 否 | 必须是用户已有的账户 |
| uuid | 单条记录查找, 使用该字段时, 应该尽量只使用这一个字段 | 否 | 无 |
| greaterThan | 金额大于某值 | 否 | 不能为负数, 且只能有 2 位小数 |
| lessThan | 金额小于某值 | 否 | 不能为负数, 且只能有 2 位小数 |

### rollback (Put)

回滚该账户的最后一次交易记录

## Account 接口

接口 URL: localhost:8080/account

### (Get)

获取所有当前用户的账户

### (Post)

为当前用户添加一个账户

| 参数名  |   说明   | 是否必填 |   值规范   |
| :-----: | :------: | :------: | :--------: |
|  name   |  账户名  |    是    |     无     |
| balance | 账户余额 |    否    | 不能为负数, 且只能有 2 位小数 |

### (Delete)

为当前用户删除一个账户

| 参数名 |  说明  | 是否必填 | 值规范 |
| :----: | :----: | :------: | :----: |
|  name  | 账户名 |    是    |   无   |

### (Update)

更新账户信息

| 参数名  |   说明   |        是否必填        |   值规范   |
| :-----: | :------: | :--------------------: | :--------: |
| oldName | 旧账户名 |           是           |     必须是用户已有的账户     |
| newName | 新账户名 | 是 (如果 balance 为空) |     无     |
| balance | 账户余额 | 是 (如果 newName 为空) | 不能为负数, 且只能有 2 位小数 |

## user 接口

接口 URL: localhost:8080/user/

### get_token (Post)

用户认证, 获取 token (有效期 2 小时)

|  参数名  |   说明   | 是否必填 | 值规范 |
| :------: | :------: | :------: | :----: |
|    userId    | 用户 ID  |    是    |   无   |
| password | 用户密码 |    是    |   大于 8 位, 小于 20 位, 不可以有空格   |

### register (Post)

注册一个账号

|  参数名  |  说明  | 是否必填 | 值规范 |
| :------: | :----: | :------: | :----: |
|   name   | 用户名 |    是    |   无   |
| password |  密码  |    是    |   大于 8 位, 小于 20 位, 不可以有空格 |

### password (Put)

更改密码

|  参数名  |  说明  | 是否必填 | 值规范 |
| :------: | :----: | :------: | :----: |
| oldPassword | 旧密码 |    是    |   大于 8 位, 小于 20 位, 不可以有空格 |
| newPassword | 新密码 |    是    |   大于 8 位, 小于 20 位, 不可以有空格 |

### name (Put)

更改用户名

| 参数名 |  说明  | 是否必填 | 值规范 |
| :----: | :----: | :------: | :----: |
|  name  | 用户名 |    是    |   无   |
|  password  | 密码 |    是    |   大于 8 位, 小于 20 位, 不可以有空格   |

## 建表语句

account 表 (账户表)

```sql
CREATE TABLE `account`
(
    `id`      int           NOT NULL AUTO_INCREMENT COMMENT '自增 ID',
    `name`    varchar(20)   NOT NULL COMMENT '账户名',
    `balance` double(12, 2) NOT NULL DEFAULT '0.00' COMMENT '余额',
    `user_id` varchar(255)  NOT NULL COMMENT '用户 ID',
    PRIMARY KEY (`id`),
    KEY `Idx_userId` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 55
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
```

bill 表 (账单表)

```sql
CREATE TABLE `bill`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `uuid`        varchar(129) NOT NULL COMMENT '查询用唯一 uuid',
    `type`        tinyint      NOT NULL DEFAULT '0' COMMENT '账单类型',
    `money`       double       NOT NULL COMMENT '金额',
    `account`     varchar(20)           DEFAULT 'wechat' COMMENT '账单账户',
    `description` varchar(100) NOT NULL DEFAULT '' COMMENT '账单备注',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更改时间',
    `deleted`     tinyint(1)   NOT NULL DEFAULT '0' COMMENT '是否已删除',
    `user_id`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `bill_uuid_index` (`uuid`),
    KEY `bill__userId_index` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 151
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
```

user 表 (用户表)

```sql
CREATE TABLE `user`
(
    `id`       bigint       NOT NULL AUTO_INCREMENT,
    `user_id`  varchar(255) NOT NULL COMMENT '用户 id',
    `name`     varchar(20)  NOT NULL COMMENT '用户姓名',
    `password` varchar(255) NOT NULL,
    `salt`     bigint       NOT NULL COMMENT 'salt 值',
    PRIMARY KEY (`id`),
    KEY `Idx_userId` (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 24
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
```