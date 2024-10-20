package com.kims.cluster;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.ha.session.ClusterSessionListener;
import org.apache.catalina.ha.session.DeltaManager;
import org.apache.catalina.ha.session.JvmRouteBinderValve;
import org.apache.catalina.ha.tcp.ReplicationValve;
import org.apache.catalina.ha.tcp.SimpleTcpCluster;
import org.apache.catalina.tribes.group.GroupChannel;
import org.apache.catalina.tribes.group.interceptors.MessageDispatchInterceptor;
import org.apache.catalina.tribes.group.interceptors.StaticMembershipInterceptor;
import org.apache.catalina.tribes.group.interceptors.TcpFailureDetector;
import org.apache.catalina.tribes.group.interceptors.TcpPingInterceptor;
import org.apache.catalina.tribes.membership.McastService;
import org.apache.catalina.tribes.membership.StaticMember;
import org.apache.catalina.tribes.transport.ReplicationTransmitter;
import org.apache.catalina.tribes.transport.nio.NioReceiver;
import org.apache.catalina.tribes.transport.nio.PooledParallelSender;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/*

출처: https://happy-jjang-a.tistory.com/155 [jjang-a 블로그:티스토리]

*/
//public class TomcatClusterConfig{}

@Configuration
public class TomcatClusterConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(final TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(new TomcatClusterContextCustomizer());
    }


}


class TomcatClusterContextCustomizer implements TomcatContextCustomizer {
	
	private int localClusterMemberPort = 4010;
	private String clusterMembers = "192.168.56.101:4010,192.168.56.102:4010,192.168.56.101:4020,192.168.56.102:4020";
	
    @Override
    public void customize(final Context context) {
    	System.out.println("TomcatClusterContextCustomizer ####");
    	
        context.setDistributable(true); 
        
        DeltaManager manager = new DeltaManager();
        
        manager.setExpireSessionsOnShutdown(false);
        
        manager.setNotifyListenersOnReplication(true);
        
        context.setManager(manager);
        
        configureCluster((Engine) context.getParent().getParent());
    }
    
    private void configureCluster(Engine engine) {
        //cluster 
        SimpleTcpCluster cluster = new SimpleTcpCluster();
        cluster.setChannelStartOptions(3);
        cluster.setChannelSendOptions(8);

        //channel 
        GroupChannel channel = new GroupChannel();

        StaticMembershipInterceptor staticMembershipInterceptor = new StaticMembershipInterceptor();

        /** [WAS1] 설정 기준    */
        // 대상 정보 - was2정보
        StaticMember staticMember = new StaticMember();
        staticMember.setPort(4056);
        staticMember.setSecurePort(-1); // default
        staticMember.setHost("192.168.56.102");
        staticMember.setUniqueId("{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2}");
        staticMembershipInterceptor.addStaticMember(staticMember);

        //receiver(현재 자신의 정보) - was1
        NioReceiver receiver = new NioReceiver();
        receiver.setAddress("192.168.56.101");
        receiver.setMaxThreads(6);
        receiver.setPort(4055);  // was1: 4055, was2: 4056
        channel.setChannelReceiver(receiver);
       

        /** [WAS2] 설정 기준    
        // 대상 정보 - was1정보
        StaticMember staticMember = new StaticMember();
        staticMember.setPort(4055);
        staticMember.setSecurePort(-1); // default
        //staticMember.setHost("172.31.44.193");
        staticMember.setHost("192.168.56.101");
        staticMember.setUniqueId("{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}");
        staticMembershipInterceptor.addStaticMember(staticMember);

         //receiver(현재 자신의 정보) - was2
         NioReceiver receiver = new NioReceiver();
         receiver.setAddress("192.168.56.102");
         receiver.setMaxThreads(6);
         receiver.setPort(4056);  // was1: 4055, was2: 4056
         channel.setChannelReceiver(receiver);
         */

        channel.addInterceptor(staticMembershipInterceptor);

        //sender
        ReplicationTransmitter sender = new ReplicationTransmitter();
        sender.setTransport(new PooledParallelSender());
        channel.setChannelSender(sender);

        //interceptor
        channel.addInterceptor(new TcpPingInterceptor());
        channel.addInterceptor(new TcpFailureDetector());
        channel.addInterceptor(new MessageDispatchInterceptor());
        cluster.addValve(new ReplicationValve());
        cluster.addValve(new JvmRouteBinderValve());
        cluster.setChannel(channel);
        cluster.addClusterListener(new ClusterSessionListener());
        engine.setCluster(cluster);
    } 
    
    
    /*
     * 
     * Multicast 방식
     * 
    private void configureCluster(Engine engine) {
    	System.out.println("TomcatClusterContextCustomizer configureCluster ###"); 
    	
        //cluster
        SimpleTcpCluster cluster = new SimpleTcpCluster();
        cluster.setChannelSendOptions(6);

        //channel
        GroupChannel channel = new GroupChannel();
        
        //membership setting
        McastService mcastService = new McastService();
        mcastService.setAddress("228.0.0.4");
        mcastService.setPort(45564); // TCP&UDP port 오픈 필요
        mcastService.setFrequency(500);
        mcastService.setDropTime(3000);
        channel.setMembershipService(mcastService);

        //receiver
        NioReceiver receiver = new NioReceiver();
        receiver.setAddress("auto");
        receiver.setMaxThreads(6);
        receiver.setPort(5000); // TCP port 오픈 필요
        channel.setChannelReceiver(receiver);

        //sender
        ReplicationTransmitter sender = new ReplicationTransmitter();
        sender.setTransport(new PooledParallelSender());
        channel.setChannelSender(sender);

        //interceptor
        channel.addInterceptor(new TcpPingInterceptor());
        channel.addInterceptor(new TcpFailureDetector());
        channel.addInterceptor(new MessageDispatchInterceptor());
        
        
        
        cluster.addValve(new ReplicationValve());
        cluster.addValve(new JvmRouteBinderValve());
        cluster.setChannel(channel);
        cluster.addClusterListener(new ClusterSessionListener());
        engine.setCluster(cluster);
    }
    
    */
} 
