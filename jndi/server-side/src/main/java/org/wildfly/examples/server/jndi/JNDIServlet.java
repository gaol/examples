package org.wildfly.examples.server.jndi;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "jndiBindServlet", urlPatterns = {"/jndiBind"})
public class JNDIServlet extends HttpServlet {

    private InitialContext context;

    @Override
    public void init() throws ServletException {
        try {
            this.context = new InitialContext();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize the InitialContext", e);
        }
    }

    @Override
    public void destroy() {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("jndi-name");
        if (name == null) {
            name = "default-jndi-name";
        }
        final String jndiName = "java:jboss/exported/" + name;
        String jndiValue = req.getParameter("jndi-value");
        if (jndiValue == null) {
            jndiValue = "default-jndi-value";
        }
        try {
            context.bind(jndiName, jndiValue);
            System.out.println("jndiName: " + jndiName + " was bound to: " + jndiValue);
        } catch (NamingException e) {
            throw new ServletException("Failed to bind JNDI", e);
        }
        PrintWriter out = resp.getWriter();
        out.println("jndiName: " + jndiName + " was bound to: " + jndiValue);
        out.flush();
    }
}