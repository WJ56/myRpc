package com.jia.register;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

public class NacosRegister {
    // Nacos 服务端地址
    private static final String serverAddr = "localhost:8848";

    // 创建 Nacos NamingService 实例
    private static NamingService namingService;

    static {
        try {
            namingService = NacosFactory.createNamingService(serverAddr);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register(String interfaceName, Instance instance){
        // 服务名

        // 可以设置更多的实例信息，例如权重、元数据等
        // instance.setWeight(1.0);
        // instance.setMetadata(...);

        try {
            // 向 Nacos 注册服务实例
            namingService.registerInstance(interfaceName, instance);
            // 注册成功后，您的服务实例将会在 Nacos 上可见
            System.out.println("服务注册成功");
        } catch (NacosException e) {
            System.err.println("服务注册失败");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Instance get(String interfaceName){
        // 获取所有健康的服务实例列表
        List<Instance> instances = null;
        try {
            System.out.println(interfaceName);
            instances = namingService.selectInstances(interfaceName, true);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }

        // 可以根据需要，实现自己的负载均衡算法选择一个实例
        // 这里我们简单地取第一个实例
        if (!instances.isEmpty()) {
            Instance instance = instances.get(0); // 或使用负载均衡策略选择一个实例
            System.out.println("发现服务：" + instance.getIp() + ":" + instance.getPort());
            return instance;
            // 此时你可以使用该实例的 IP 和端口进行 RPC 调用
        } else {
            System.out.println("未发现可用服务实例");
        }
        return null;
    }
}
