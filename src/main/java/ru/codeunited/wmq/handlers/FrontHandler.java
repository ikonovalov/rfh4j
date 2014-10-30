package ru.codeunited.wmq.handlers;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import ru.codeunited.wmq.cli.CLIFactory;
import ru.codeunited.wmq.cli.ConsoleWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 30.10.14.
 */
public class FrontHandler extends AbstractHandler {

    @Override
    public void handle(String s, Request request, HttpServletRequest baseRequest, HttpServletResponse response) throws IOException, ServletException {
        final PrintWriter servletWriter = response.getWriter();
        final ConsoleWriter console = new ConsoleWriter(servletWriter);
        console.setNextLineMarkerL("<br>");
        console.writeln("<b>RFH4Jetty</b>");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        CLIFactory.showHelp(servletWriter);
        servletWriter.flush();
        request.setHandled(true);
    }
}
