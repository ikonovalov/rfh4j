package ru.codeunited.wmq;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public class RFH4Jetty {

    private final Server server;

    private final int JETTY_PORT = 8080;

    public RFH4Jetty() {
        server = new Server(JETTY_PORT);
    }

    public void startAndWait() throws Exception {
        final ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "ru.codeunited.wmq.restapi");//Set the package where the services reside
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

        final ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        context.getSessionHandler().addEventListener(new HttpSessionListener() {

            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Session created " + se.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                System.out.println("Session destroyed " + se.getSession().getId());
            }
        });

        context.addServlet(sh, "/*");

        server.start();
        server.join();
    }

    public static void main(String[] args) throws Exception {
        new RFH4Jetty().startAndWait();
    }

    // http://jlunaquiroga.blogspot.ru/2014/01/restful-web-services-with-jetty-and.html
}
