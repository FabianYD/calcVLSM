/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mundonetrh.calculadoravlsm.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import calculadora.Calculadora;

/**
 *
 * @author fabian
 */
@WebServlet(name = "SvRed", urlPatterns = {"/SvRed"})
public class SvRed extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Limpiar la sesión existente
        HttpSession session = request.getSession();
        session.invalidate();
        session = request.getSession(true);

        try {
            // Obtener dirección base y máscara
            String direccionBase = request.getParameter("direccionBase");
            int mascaraBase = Integer.parseInt(request.getParameter("mascaraBase"));
            String[] nombresHost = request.getParameterValues("nombreHost[]");
            String[] numHost = request.getParameterValues("numHosts[]");
            Calculadora calc = new Calculadora(direccionBase, mascaraBase);

            for (int i = 0; i < numHost.length; i++) {
                calc.agregarHost(nombresHost[i], Integer.parseInt(numHost[i]));
            }

            calc.calcular();

            // Guardar los resultados en la sesión
            String redP = calc.getRedPrincipal().getIpv4() + "/" + calc.getRedPrincipal().getPrefijo();
            session.setAttribute("red", redP);
            session.setAttribute("subredes", calc.imprimirSubredes());
            session.setAttribute("resultados", calc.verResultados());
            session.setAttribute("hosts", calc.imprimirHosts());

            response.sendRedirect("index.jsp");
            
        } catch (IllegalArgumentException e) {
            // Guardar el mensaje de error en la sesión
            session.setAttribute("error", e.getMessage());
            // Redirigir a la página principal
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>



}
