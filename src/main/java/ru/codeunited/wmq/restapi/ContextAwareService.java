package ru.codeunited.wmq.restapi;

import ru.codeunited.wmq.ExecutionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 07.11.14.
 */
public abstract class ContextAwareService {

    private static final String CTX = "execCtx";

    @Context
    HttpServletRequest request;

    @Context
    UriInfo info;

    ExecutionContext getContext() {
        final HttpSession session = request.getSession(true);
        if (session.getAttribute(CTX) == null) {
            final ExecutionContext context = new RestExecutionContext(info);

            MultivaluedMap<String, String> pathParams = info.getPathParameters();

            session.setAttribute(CTX, context);
        }
        return (ExecutionContext) session.getAttribute(CTX);
    }

    String getSessionId() {
        return request.getSession(true).getId();
    }

}
