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

            String redP = calc.getRedPrincipal().getIpv4() == null ? "" : calc.getRedPrincipal().getIpv4();
            HttpSession session = request.getSession();
            session.setAttribute("red", redP);
            session.setAttribute("subredes", calc.imprimirHosts());
            session.setAttribute("resultados", calc.verResultados());

            response.sendRedirect("index.jsp");
            
        } catch (Exception e) {
            // Manejar errores
            System.out.println("Error al procesar la solicitud: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error en los datos proporcionados");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>



}
