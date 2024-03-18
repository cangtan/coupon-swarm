# SpringCloud实战-优惠券系统

## template(优惠券模板)模块



**核心一：**根据运营人员设定的条件构造优惠券模板

如：名称、logo、分类、产品线、数量、规则

生成优惠券模板最核心的思想是异步+优惠券模板

优惠券必须要有数量限制且优惠券（分发）必须要有优惠券码

优惠券码有18位，有两个要求

- 不可重复
- 有一定的识别性

产品线 + 类型（前4位）

日期随机（中间6位）

0~9的随机数字（后八位）

**核心二：**给优惠券模板生成”优惠券码“并保存到redis当中（list

优惠券码是现在一个服务当中预先生成的，并放到Redis当中

这样做可以解决

简单的解决优惠券码的一致性问题

不会造成优惠券码的超发

不需要考虑分发优惠券时的配额问题

（如果采用动态生成，每个实例自己生成，则需要其他措施来保障优惠券不会超发，【每个实例配额，并且处理好负载均衡等等

**核心三：**清理过期的优惠券模板

优惠券模板规定有使用期限，有两种过期策略：

1.template模块自己的定期清理策略

2.使用template模块的其他模块自己校验（因为策略1存在延迟

（定期策略+惰性策略

## distribution(优惠券分发)模块

**核心一：**根据用户id和优惠券状态查找用户优惠券记录

1.本系统不涉及用户模块， 因此用户相关均为mock

2.优惠券有3类状态：可用的，已使用的，过期的（未被使用的

3.用户数据都存储于Redis当中

4.除获取用户的优惠券之外，还有延迟的过期处理策略

用户查询优惠券-> 服务会先将优惠券信息缓存到Redis当中，当存在过期的优惠券时，将会将过期的优惠券信息发送到Kafka，Kafka再写回到MySQL



**核心二：用户领取优惠券**

优惠券模板从template模块处获取（熔断兜底策略）

根据优惠券的领取限制，比对当前用户所拥有的优惠券，做出判断

从Redis当中获取优惠券码

优惠券写入MySQL和Redis

**核心三：**结算（核销）优惠券

1.校验优惠券是否是合法的（属于当前用户&&可用

2.利用settlement模块（结算模块）计算结算数据

3.如果是核销，需要写回数据库

结算模块是指通过优惠券计算优惠金额，并非使用了该优惠券

而核销表示使用了优惠券（消息发送给kafka写回数据库）

结算（核销优惠券金额）

## settlement(结算)模块

核心一：根据优惠券类型结算优惠券

优惠券是分类的，不同的分类有不同的计算方法（满减，折扣，立减

不同的优惠券可以组合，所以也需要有不同的计算方法（组合券

## 缓存设计

一：优惠券码

优惠券系统使用Redis做缓存，所以都是KV类型

Key需要有意义且不能与原有Key冲突

优惠券码需要永久存在，不设过期时间

Value类型：list

Key: coupon_template_code+后缀（模板id）

二：用户优惠券码

用户优惠券信息根据状态分为三类就（未使用，已使用，已过期）

用户数据保存在MySQL中，且数据量大，不适合长期驻留再Redis中，需要设置过期时间

Key: 

user_coupon_usable + 模板id

user_coupon_used + 模板id

user_coupon_expired+ 模板id

Value: hash （key:优惠券id：value优惠券信息

## 架构设计



# Kafaka

~~~bash
#启动zookeeper
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
#kafka server
bin/kafka-server-start.sh config/server.properties
~~~

创建topic

~~~bash
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
~~~

查询topic list

~~~bash
bin/kafka-topics.sh --list --zookeeper localhost:2181
~~~



