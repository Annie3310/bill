# 账单
该项目是基于 Spring Boot 的账单接口, 提供多用户与多账户功能.

## TODO

- 规范密码格式
- 修改账户相关的操作时, 需要重新提供 ID 和密码

### 用户认证

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
| money | 金额 | 是 | 必须是数字类型, 否则会报错 |
| account | 账户 | 是 | 必须是用户已有的账户, 否则会报错 |
| description | 说明 | 否 | 对该笔记录的说明 |

### expense (Post)
支出接口 (实际与入账调用的是同一个 dao 层方法, 只是 type (0: 支出, 1: 收入) 不同)

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| money | 金额 | 是 | 必须是数字类型, 否则会报错 |
| account | 账户 | 是 | 必须是用户已有的账户, 否则会报错 |
| description | 说明 | 否 | 对该笔记录的说明 |

### transfer (Post)
转账接口, 因该接口是面向单用户的接口, 所以只能在自己的账号中对对自己的多个账户进行转账.

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| money | 金额 | 是 | 必须是数字类型, 否则会报错 |
| from | 转出账户 | 是 | 转账的支出账户, 不能与 to 账户名相同, 否则会报错 |
| to | 转入账户 | 是 | 转账的收入账户, 不能与 from 账户名相同, 否则会报错 |
| description | 说明 | 否 | 对该笔记录的说明 |

### sum (Get)
获取当前总资产的详情

### filter (Get)
多条件模糊查找

| 参数名 | 说明 | 是否必填 | 值规范 |
| :---: | :---: | :---: | :---: |
| startDate | 查找的起始时间 | 否 | yyyy-MM-dd, 非此格式会报错 |
| endDate | 查找的结束时间 | 否 | yyyy-MM-dd, 非此格式会报错 |
| account | 账户 | 否 | 必须是用户已有的账户, 否则会报错 |
| uuid | 单条记录查找, 使用该字段时, 应该尽量只使用这一个字段 | 否 | 无 |
| greaterThan | 金额大于某值 | 否 | 必须是数字类型, 否则会报错 |
| lessThan | 金额小于某值 | 否 | 必须是数字类型, 否则会报错 |

## Account 接口

接口 URL: localhost:8080/account

### (Get)

获取所有当前用户的账户

### (Post)

为当前用户添加一个账户

| 参数名  |   说明   | 是否必填 |   值规范   |
| :-----: | :------: | :------: | :--------: |
|  name   |  账户名  |    是    |     无     |
| balance | 账户余额 |    否    | 只能是数字 |

### (Delete)

为当前用户删除一个账户

| 参数名 |  说明  | 是否必填 | 值规范 |
| :----: | :----: | :------: | :----: |
|  name  | 账户名 |    是    |   无   |

### (Update)

更新账户信息

| 参数名  |   说明   |        是否必填        |   值规范   |
| :-----: | :------: | :--------------------: | :--------: |
| oldName | 旧账户名 |           是           |     无     |
| newName | 新账户名 | 是 (如果 balance 为空) |     无     |
| balance | 账户余额 | 是 (如果 newName 为空) | 只能是数字 |

## user 接口

接口 URL: localhost:8080/user/

### get_token (Get)

用户认证, 获取 token (有效期 1 个月)

|  参数名  |   说明   | 是否必填 | 值规范 |
| :------: | :------: | :------: | :----: |
|    id    | 用户 ID  |    是    |   无   |
| password | 用户密码 |    是    |   无   |

### register (Post)

注册一个账号

|  参数名  |  说明  | 是否必填 | 值规范 |
| :------: | :----: | :------: | :----: |
|   name   | 用户名 |    是    |   无   |
| password |  密码  |    是    |   无   |

### password (Put)

更改密码

|  参数名  |  说明  | 是否必填 | 值规范 |
| :------: | :----: | :------: | :----: |
| password | 新密码 |    是    |   无   |

### name (Put)

更改用户名

| 参数名 |  说明  | 是否必填 | 值规范 |
| :----: | :----: | :------: | :----: |
|  name  | 用户名 |    是    |   无   |

