
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Leo on 2018/5/18.
 * 获取本机真实IP
 */
public class GetRealLocalIP {
    // 用ipconfig /all命令查找本机物理网卡名称
    private static final String NETWORK_CARD_NAME = "Intel(R) Ethernet Connection (2) I219-V";

    /**
     * 获取本机真实IP以及判断Java版本
     * @return 返回List<String>里面是IP地址
     * @throws SocketException 抛出SocketException
     */
    private static List<String> getLocalRealIP() throws SocketException {
        // 判断Java版本
        String java_version = System.getProperty("java.version");
        if (!java_version.contains("1.8.")) {
            throw new UnsupportedClassVersionError("Java版本不正确! (Java version is not correct!)");
        }
        // 获取NetworkInterface的对象
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();

        // 转成Stream对象 (Java8特性)
        // 参考API文档：https://www.ibm.com/developerworks/cn/java/j-lo-java8streamapi/
        Stream<NetworkInterface> networkInterfaceStream = Collections.list(netInterfaces).stream();

        // filter-1 获取所有包含网卡名的对象
        // filter-2 获取非空的网卡名的值(存在空值的情况)
        // map把获取到非空值的IP获取出来
        // collect把map的值写到List中
        List<String> result = networkInterfaceStream.
                filter(ni -> ni.getDisplayName().contains(NETWORK_CARD_NAME)).
                filter(ni -> ni.getInterfaceAddresses().size() != 0).
                map(ni -> ni.getInetAddresses().nextElement().getHostAddress()).
                collect(Collectors.toList());
        System.out.println(result);
        return result;
    }

    public static void main(String[] args) throws SocketException {
        // InetAddress.getLocalHost().getHostAddress() 用左边这种你一辈子都拿不到当前电脑的真实IP, 嘿嘿.
        List<String> res = getLocalRealIP();
        System.out.println(res);
    }
}
