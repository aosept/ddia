# DDIA third-party-pay
第三方支付功能都在这里提供。

这个微服务提供的功能是各个支付通道的支付以及支付订单管理功能。支付成功后的效果由各个  引起付费行为的微服务(以下称为商品微服务) 产生。

设计原则:

1、 商品微服务要提供两个接口，一是产生订单的接口(以下称为 A.1 )，这个接口要返回appid, apptranxid以及各种商品描述，订单总金额等。 二是订单支付结果的处理接口(以下称为 A.2 )。 

2、 这个微服务对每个支付通道提供两个接口: 一是 生成第三方订单 的接口(以下称为 B.1 )， 二是 查询 第三方订单支付状态的接口(以下称为 B.2 ) 。

3、 支付遵循这个流程： 

	gateway 收到客户端的支付请求时:
		gateway调用A.1。
		gateway用A.1得到的结果调用B.1。并把B.1的结果返回给客户端。
		
	客户端调用第三方的user-agent进行支付。
	支付完成后，gateway 收到客户端的订单检查请求时:
		gateway调用B.2。
		gateway用B.2得到结果调用A.2。并把A.2的支付结果返回给客户端。
		
