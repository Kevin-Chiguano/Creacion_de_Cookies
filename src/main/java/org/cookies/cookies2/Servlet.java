package org.cookies.cookies2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener el nombre de usuario del formulario
        String username = req.getParameter("username");
        if (username == null || username.isEmpty()) {
            username = "invitado";
        }

        // Crear un identificador único basado en el nombre de usuario
        String userId = "user_" + username;
        boolean nuevoUsu = true;

        // Vamos a obtener el arreglo de las cookies
        Cookie[] cookies = req.getCookies();

        // Vamos a validar si existe o no la cookie de usuario
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(userId)) {
                    nuevoUsu = false;
                    break;
                }
            }
        }

        String mensaje;
        if (nuevoUsu) {
            Cookie userCookie = new Cookie(userId, "visited");
            resp.addCookie(userCookie);

            Cookie visitCountCookie = new Cookie("visitCount_" + userId, "1");
            resp.addCookie(visitCountCookie);

            mensaje = "<h2>Bienvenido por primera vez, " + username + "!</h2>"
                    + "<p>Esperamos que disfrutes de tu visita.</p>";
        } else {
            // Manejar la cookie de conteo de visitas específica para el usuario
            Cookie visitCountCookie = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visitCount_" + userId)) {
                    visitCountCookie = cookie;
                    break;
                }
            }

            int visitCount = 0;
            if (visitCountCookie == null) {
                visitCountCookie = new Cookie("visitCount_" + userId, "1");
                visitCount = 1;
            } else {
                visitCount = Integer.parseInt(visitCountCookie.getValue());
                visitCount++;
                visitCountCookie.setValue(Integer.toString(visitCount));
            }
            resp.addCookie(visitCountCookie);

            mensaje = "<h2>Bienvenido de nuevo, " + username + "!</h2>"
                    + "<p>Has visitado nuestro sitio " + visitCount + " veces.</p>";
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Respuesta del Servlet</title>");
        out.println("<style>");
        out.println(".container {");
        out.println("  display: flex;");
        out.println("  justify-content: center;");
        out.println("  align-items: center;");
        out.println("  height: 100vh;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println(mensaje);
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
