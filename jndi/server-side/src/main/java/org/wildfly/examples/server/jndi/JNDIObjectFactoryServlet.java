package org.wildfly.examples.server.jndi;

import com.mongodb.MongoClient;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "JNDIObjectFactoryServlet", urlPatterns = {"/jndiMongo"})
public class JNDIObjectFactoryServlet extends HttpServlet {

    @Resource(lookup = "java:jboss/mongoclient")
    private MongoClient mc;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        for (String dbName: mc.listDatabaseNames()) {
            sb.append("dbName: ").append(dbName).append("\n");
        }
        PrintWriter out = resp.getWriter();
        out.println(sb.toString());
        out.flush();
    }
}