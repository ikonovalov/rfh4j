package ru.codeunited.wmq;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.codeunited.wmq.handlers.FrontHandler;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public class RFH4Jetty {

    public static void main(String[] args) throws Exception {
        final Server server = new Server();
        final ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

        final HandlerCollection handlers = new HandlerCollection();

        ContextHandler contextStaticHandler = new ContextHandler();
        contextStaticHandler.setContextPath("/static");

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(".");
        contextStaticHandler.setHandler(resourceHandler);

        final FrontHandler frontHandler = new FrontHandler();

        handlers.setHandlers(new Handler[]{contextStaticHandler, frontHandler});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}
