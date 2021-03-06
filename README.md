# springcloud-workspace

spring cloud的简单demo工程，尝试所有服务docker化，目前服务都是部署在单机的docker内，挂在同一个bridge docker network上，实现hostname直接通信，可以很方便模拟出集群的效果。

configuration-service：配置中心服务，这里利用git作为backend，本地额外挂了一个gitlab的docker镜像来模拟统一git服务维护配置信息。由于spring-web的服务启动需要繁杂的配置信息，将配置信息与服务启动解耦，可以更灵活地支持动态增删服务节点，同时配置信息代码化管理，也符合devops部署工程化的思路。

坑1：低版本spring cloud对于列表格式的配置数据会报错缺少合适converter转换配置项，是因为config client请求的accept mediatype设置问题，升级版本后可解。

坑2：阿里的镜像可能拉不到新版的spring cloud，需要去掉镜像配置再试。

坑3：eureka server实现了onApplicationEvent响应Environment配置变动事件，不需要@RefreshScope，但是默认只响应eureka.client.region、eureka.client.service-url.*、eureka.client.availability-zones.*配置项变动刷新peer节点集，不支持驼峰命名的eureka.client.serviceUrl.defaultZone。

eureka-service：简单eureka服务+docker化+单机高可用部署，提供集群内服务注册和发现的能力，eureka server的高可用其实是简单多对多replicate的架构，每个服务启动时就需要通过配置确定同一个zone的replica节点列表。eureka server集群的一致性保证机制待考，如果是简单的单点写入+广播那一致性就有点弱了。

注意config和eureka可以相互依赖，这里选择优先启动gitlab和config服务，然后再启动eureka server集群，eureka集群利用config获取zone replica节点的配置信息，然后config再注册到eureka上。

producer、ribbon-client：模拟简单的微服务，实现eureka服务注册、ribbon负载均衡的简单demo。
