package ru.codeunited.wmq.restapi;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 06.11.14.
 */
@Path("/ping")
public class PingService extends ContextAwareService {

    @GET
    @Path("time")
    public String time() {
        return String.valueOf(System.currentTimeMillis()) + " SessionID: [" + getSessionId() + "]";
    }
}
