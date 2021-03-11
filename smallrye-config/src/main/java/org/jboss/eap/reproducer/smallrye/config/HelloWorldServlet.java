package org.jboss.eap.reproducer.smallrye.config;

import io.smallrye.config.SmallRyeConfig;
import io.smallrye.config.SmallRyeConfigBuilder;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloWorldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SmallRyeConfigBuilder builder = new SmallRyeConfigBuilder();
        builder.addDefaultSources()
                .forClassLoader(getClass().getClassLoader());
        SmallRyeConfig config = builder.build();
        StringBuilder sb = new StringBuilder("\n");
        for (String name: config.getPropertyNames()) {
            sb.append(name)
                    .append(" == ")
                    .append(config.getRawValue(name))
                    .append("\n");
        }
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<h1>Hello World</h1>");
        writer.println(sb.toString());
        writer.close();
    }

}
