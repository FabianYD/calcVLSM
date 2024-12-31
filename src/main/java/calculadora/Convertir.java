/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

/**
 *
 * @author fabian
 */
public class Convertir {
    public static String Binario(String ipv4) {
        String[] octetos = ipv4.split("\\.");
        StringBuilder binario = new StringBuilder();
        for (String octeto : octetos) {
            String octetoBinario = String.format("%8s", Integer.toBinaryString(Integer.parseInt(octeto)))
                    .replace(' ', '0');
            binario.append(octetoBinario);
        }
        return binario.toString();
    }

    public static String IPv4(String binarioFormateado) {
        // Eliminar los puntos si los hay
        String binario = binarioFormateado.replace(".", "");
        StringBuilder ip = new StringBuilder();
        
        // Convertir cada grupo de 8 bits a decimal
        for (int i = 0; i < 32; i += 8) {
            if (i > 0) ip.append(".");
            String octeto = binario.substring(i, i + 8);
            ip.append(Integer.parseInt(octeto, 2));
        }
        return ip.toString();
    }

    public static String IPv4ToBinary(String ipv4) {
        String[] octetos = ipv4.split("\\.");
        StringBuilder binario = new StringBuilder();
        
        for (String octeto : octetos) {
            String octetoBinario = String.format("%8s", Integer.toBinaryString(Integer.parseInt(octeto)))
                    .replace(' ', '0');
            binario.append(octetoBinario);
        }
        
        return binario.toString();
    }

    public static String ipToBinary(String ipv4) {
        return IPv4ToBinary(ipv4);
    }

    public static String calcularMascaraBinaria(int mascara) {
        StringBuilder mascaraBinaria = new StringBuilder();
        for (int i = 0; i < mascara; i++) {
            mascaraBinaria.append("1");
        }
        for (int i = mascara; i < 32; i++) {
            mascaraBinaria.append("0");
        }
        return mascaraBinaria.toString();
    }

    public static String formatearBinario(String binario) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < binario.length(); i++) {
            if (i > 0 && i % 8 == 0) {
                resultado.append(".");
            }
            resultado.append(binario.charAt(i));
        }
        return resultado.toString();
    }

    public static String convertirIPaBinario(String direccionIP) {
        return Binario(direccionIP);
    }
}
