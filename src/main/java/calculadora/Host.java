/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

/**
 *
 * @author fabian
 */
public class Host {
    private String nombre;
    private int numHost;
    private int potencia;

    public Host(String nombre, int numHost) {
        this.nombre = nombre;
        this.numHost = numHost;
        this.potencia = calcularPotencia();
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public int getNumHost() {
        return numHost;
    }

    public int getPotencia() {
        return potencia;
    }

    public int calcularPotencia() {
        int resultado = 0;
        for (int i = 0; i < 33; i++) {
            int potencia = (int) Math.pow(2, i) - 2;
            if (potencia >= numHost) {
                resultado = i;
                break;
            }
        }
        return resultado;
    }

    public String imprimir() {//para la web representar en tabla
        return "<tr>" +
               "<td>" + nombre + "</td>" +
               "<td>" + numHost + "</td>" +
               "<td>2^" + potencia + "</td>" +
               "</tr>";
    }
}

