import com.baidu.duer.dcs.CommonUtil;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetWorkTest {
    public static void main(String[] args) throws Exception{

        System.out.println(CommonUtil.findMAC());
//        Enumeration<NetworkInterface> cards = NetworkInterface.getNetworkInterfaces();
//        while (cards.hasMoreElements()){
//            NetworkInterface card = cards.nextElement();
//            if(card.getHardwareAddress()==null) {
//                continue;
//            }
//            System.out.println("=================================");
//            System.out.println(new BigInteger(card.getHardwareAddress()).toString(16));
//
//            System.out.println(card.getName());
//            System.out.println(card.getDisplayName());
//
//            System.out.println( "card.isLoopback()"+card.isLoopback());
//            System.out.println( "card.isVirtual()"+card.isVirtual());
//
//        }
    }
//    public static String findMAC(){
//        String[] cardNames={"eth0","eth1","wlan0","wlan1","wlan2"};
//        for (String cardName : cardNames) {
//            NetworkInterface card=findNetworkInterface(cardName);
//            if(card!=null){
//                try {
//                    byte[] addr = card.getHardwareAddress();
//                    if(addr!=null){
//                        String mac=new BigInteger(addr).toString(16);
//                        if(mac.length()==11){
//                            mac="0"+mac;
//                        }
//                        return mac;
//                    }
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return null;
//    }
//    public static NetworkInterface findNetworkInterface(String name){
//        try {
//            Enumeration<NetworkInterface> cards = NetworkInterface.getNetworkInterfaces();
//            while (cards.hasMoreElements()){
//                NetworkInterface card = cards.nextElement();
//                if(name.equals(card.getName())){
//                    return card;
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
