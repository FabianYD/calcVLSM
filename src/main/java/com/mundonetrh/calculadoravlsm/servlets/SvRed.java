/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.mundonetrh.calculadoravlsm.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import calculadora.Red;

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

            // Obtener arrays de nombres y números de hosts
            String[] nombresHost = request.getParameterValues("nombreHost[]");
            String[] numHostsStr = request.getParameterValues("numHosts[]");

            Red red = new Red(direccionBase, mascaraBase);
            HttpSession session = request.getSession();
            session.setAttribute("red", red.imprimir());
            // session.setAttribute("nombresHost", nombresHost);
            // session.setAttribute("numHostsStr", numHostsStr);
            response.sendRedirect("index.jsp");

            // Convertir array de números de hosts a enteros
            /* int[] numHosts = new int[numHostsStr.length];
            for (int i = 0; i < numHostsStr.length; i++) {
                numHosts[i] = Integer.parseInt(numHostsStr[i]);
            }

            // Imprimir los datos recibidos (para depuración)
            System.out.println("Dirección Base: " + direccionBase);
            System.out.println("Máscara Base: " + mascaraBase);
            System.out.println("\nHosts recibidos:");
            for (int i = 0; i < nombresHost.length; i++) {
                System.out.println("LAN: " + nombresHost[i] + ", Hosts requeridos: " + numHosts[i]);
            } */

            // Aquí puedes crear tu objeto Calculadora y procesar los datos
            // Calculadora calc = new Calculadora(direccionBase, mascaraBase);
            // for (int i = 0; i < nombresHost.length; i++) {
            //     calc.agregarHost(nombresHost[i], numHosts[i]);
            // }
            // Subred[] resultado = calc.calcular();

            // TODO: Enviar el resultado a una página JSP para mostrarlo
            
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
