package coopci.ddia.third.party.pay;

import org.glassfish.grizzly.http.server.ErrorPageGenerator;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import coopci.ddia.third.party.pay.handlers.CheckOrderHandler;
import coopci.ddia.third.party.pay.handlers.CreateOrderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpServer {
	
	public static int listenPort = 8892;
	Logger logger = LoggerFactory.getLogger(HttpServer.class);
	public void start() throws Exception {
		Engine engine = new Engine();
		engine.init();
		
		final org.glassfish.grizzly.http.server.HttpServer server = new org.glassfish.grizzly.http.server.HttpServer();
		org.glassfish.grizzly.http.server.DefaultErrorPageGenerator defaultEpg = new org.glassfish.grizzly.http.server.DefaultErrorPageGenerator();
		ErrorPageGenerator epg = server.getServerConfiguration().getDefaultErrorPageGenerator();
		
		//NetworkListener nl = new NetworkListener("test-listener", "localhost", 8181);
		//server.addListener(nl);
		//HttpServer server = HttpServer.createSimpleServer();
		server.getServerConfiguration().addHttpHandler(
			new HttpHandler() {
	    	// http://127.0.0.1:8080/time
	        public void service(Request request, Response response) throws Exception {
	            String content = "ddia/virtual-assets";
	            response.setContentType("text/html;charset=utf-8");
	            response.getWriter().write(content);
	        }
	    },
	    "/");
		
		CheckOrderHandler checkOrderHandler = new CheckOrderHandler();
		checkOrderHandler.setEngine(engine);
		server.getServerConfiguration().addHttpHandler(
				checkOrderHandler,
				"/pay/check_order");

		CreateOrderHandler createOrderHandler = new CreateOrderHandler();
		createOrderHandler.setEngine(engine);
		server.getServerConfiguration().addHttpHandler(
				createOrderHandler,
				"/pay/create_order");
		
		try {
			server.removeListener("grizzly");
			
			NetworkListener nl = new NetworkListener("ddia-third-party-pay", "0.0.0.0", listenPort);
			ThreadPoolConfig threadPoolConfig = ThreadPoolConfig
			        .defaultConfig();
			        //.setCorePoolSize(16)
			        //.setMaxPoolSize(64);
			nl.getTransport().setWorkerThreadPoolConfig(threadPoolConfig);
			
			server.addListener(nl);
		    server.start();
		    System.out.println("Press any key to stop the server...");
		    System.in.read();
		} catch (Exception e) {
		    System.err.println(e);
		    // logger.error(arg0);
		}
		return;
		
	}
	
	public static void main(String[] argv) throws Exception {
		HttpServer server = new HttpServer();
		try {
		    server.start();
		} catch (Exception e) {
		    System.err.println(e);
		}
		System.out.println("Press any key to stop the server...");
	    System.in.read();
		return;
	}
}
