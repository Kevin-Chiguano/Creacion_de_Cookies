/*Autor: Kevin Chiguano
* Fecha: 27/05/2024
* Descripcion: crear Cookies con el uso de servlet
* Version:1.0
* */
package org.cookies.cookies2;

// Importaciones necesarias para el servlet
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Anotación que define la URL de acceso para el servlet
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener el nombre de usuario del formulario
        String username = req.getParameter("username");
        if (username == null || username.isEmpty()) {
            // Si el nombre de usuario no está presente, se usa "invitado"
            username = "invitado";
        }

        // Crear un identificador único basado en el nombre de usuario
        String userId = "user_" + username;
        boolean nuevoUsu = true;

        // Obtener el arreglo de las cookies enviadas por el cliente
        Cookie[] cookies = req.getCookies();

        // Validar si ya existe una cookie de usuario
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(userId)) {
                    // Si se encuentra la cookie, el usuario no es nuevo
                    nuevoUsu = false;
                    break;
                }
            }
        }

        String mensaje;
        if (nuevoUsu) {
            // Si el usuario es nuevo, crear una cookie para el usuario
            Cookie userCookie = new Cookie(userId, "visited");
            resp.addCookie(userCookie);

            // Crear una cookie para contar las visitas del usuario
            Cookie visitCountCookie = new Cookie("visitCount_" + userId, "1");
            resp.addCookie(visitCountCookie);

            // Mensaje de bienvenida para el nuevo usuario
            mensaje = "<h2>Bienvenido por primera vez, " + username + "!</h2>"
                    + "<p>Esperamos que disfrutes de tu visita.</p>";
        } else {
            // Si el usuario ya ha visitado el sitio, manejar la cookie de conteo de visitas
            Cookie visitCountCookie = null;
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("visitCount_" + userId)) {
                    visitCountCookie = cookie;
                    break;
                }
            }

            int visitCount = 0;
            if (visitCountCookie == null) {
                // Si no existe la cookie de conteo de visitas, crear una nueva con valor 1
                visitCountCookie = new Cookie("visitCount_" + userId, "1");
                visitCount = 1;
            } else {
                // Si ya existe la cookie, incrementar el contador de visitas
                visitCount = Integer.parseInt(visitCountCookie.getValue());
                visitCount++;
                visitCountCookie.setValue(Integer.toString(visitCount));
            }
            resp.addCookie(visitCountCookie);

            // Mensaje de bienvenida para el usuario recurrente
            mensaje = "<h2>Bienvenido de nuevo, " + username + "!</h2>"
                    + "<p>Has visitado nuestro sitio " + visitCount + " veces.</p>";
        }

        // Configurar el tipo de contenido de la respuesta
        resp.setContentType("text/html;charset=UTF-8");
        // Obtener el escritor para enviar la respuesta al cliente
        PrintWriter out = resp.getWriter();
        // Construir la respuesta HTML
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Respuesta del Servlet</title>");
        out.println("<style>");
        // Estilo CSS para centrar el contenido en la página
        out.println(".container {");
        out.println("  display: flex;");
        out.println("  justify-content: center;");
        out.println("  align-items: center;");
        out.println("  height: 100vh;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        // Contenedor para el mensaje
        out.println("<div class=\"container\">");
        out.println(mensaje);
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }
}
