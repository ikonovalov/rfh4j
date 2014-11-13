package ru.codeunited.wmq.restapi;

import ru.codeunited.wmq.ExecutionContext;
import ru.codeunited.wmq.commands.*;
import ru.codeunited.wmq.messaging.WMQDefaultConnectionFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.11.14.
 */
@Path("/mqm")
public class MQSerivce extends ContextAwareService {

    @GET
    @Path("/{qmanager}/{channel}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response mqm(@PathParam("qmanager") String qmName, @PathParam("channel") String channel) throws MissedParameterException, CommandGeneralException {
        final ExecutionContext context = getContext()
                .putOption("qmanager", qmName)
                .putOption("channel", channel);
        final Command connect = new ConnectCommand(new WMQDefaultConnectionFactory()).setContext(context);
        connect.execute();

        final Command disconnect = new DisconnectCommand().setContext(context);
        disconnect.execute();
        return Response.ok(new Message("hello")).build();
    }

}
