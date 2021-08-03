# 账单
该项目是基于 Spring Boot 的账单接口, 提供多用户与多账户功能.
## Bill API 接口请求参数
baseURL: localhost:xxxx/bill/?
### 用户认证
// TODO 用户相关功能, 如添加用户, 修改密码等

**!! 需要在请求头中添加**

| 字段名|用途 |
| :---:|:---: |
| id | 用户 id  |
| password | 用户密码  |
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

## Bill API 接口返回参数
### income
请求
```json
{
    "account":"alipay",
    "money":1
}
```
返回
```json
{
    "code": "00000",
    "message": "收入成功从alipay收入了1.0元",
    "result": 1
}
```
### expense
请求
```json
{
    "account":"alipay",
    "money":1
}
```
返回
```json
{
    "code": "00000",
    "message": "支出成功从alipay支出了1.0元",
    "result": 1
}
```
### transfer
请求
```json
{
    "from":"alipay",
    "to":"wechat",
    "description": "测试",
    "money":1
}
```
返回
```json
{
    "code": "00000",
    "message": "转账成功, 从 alipay 转出 1.0 元到 wechat : 测试",
    "result": null
}
```
### sum
返回
```json
{
    "code": "00000",
    "message": "获取总合成功",
    "result": {
        "details": {
            "icbc_c": 0.0,
            "alipay": 0.0,
            "ccb": 0.0,
            "wechat": 0.0,
            "cmb": 0.0,
            "icbc": 0.0,
            "cmb_c": 0.0,
            "crash": 0.0
        },
        "sum": 0.0
    }
}
```
### filter
请求
```json
{
    "startDate":"2021-07-29",
    "endDate":"2021-07-29"
}
```
返回
```json
{
    "code": "00000",
    "message": "筛选成功, 共18条",
    "result": [
        {
            "id": null,
            "uuid": "xxx",
            "money": 10.0,
            "description": "测试",
            "account": "alipay",
            "type": 1,
            "createTime": "2021-07-29 16:11:56",
            "updateTime": "2021-07-29 16:11:56",
            "deleted": 0
        }
        // ... 省略
    ]
}
```
