/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

/**
 *
 * @author fabian
 */
public class Red {
    private String ipv4;
    private int prefijo;
    private String mascara;
    private String ipv4Binario;
    private String mascaraBinaria;

    public Red(String ipv4, int prefijo) throws Exception {
        this.validarIPv4(ipv4);
        this.validarPrefijo(prefijo);
        this.ipv4 = ipv4;
        this.prefijo = prefijo;
        this.ipv4Binario = Convertir.Binario(ipv4);
        this.mascaraBinaria = Convertir.calcularMascaraBinaria(prefijo);
        this.mascara = Convertir.IPv4(Convertir.formatearBinario(mascaraBinaria));
    }

    public String getIpv4() {
        return ipv4;
    }

    public int getPrefijo() {
        return prefijo;
    }

    public String getMascara() {
        return mascara;
    }

    public String getIpv4Binario() {
        return ipv4Binario;
    }

    public String getMascaraBinaria() {
        return mascaraBinaria;
    }

    private void validarIPv4(String ipv4) throws Exception {
        if (!ipv4.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            throw new Exception("La dirección IP debe tener el formato XXX.XXX.XXX.XXX");
        }
        String[] octetos = ipv4.split("\\.");
        for (String octeto : octetos) {
            int valor = Integer.parseInt(octeto);
            if (valor < 0 || valor > 255) {
                throw new Exception("Cada octeto debe estar entre 0 y 255");
            }
        }
    }

    private void validarPrefijo(int prefijo) throws Exception {
        if (prefijo < 0 || prefijo > 32) {
            throw new Exception("El prefijo debe estar entre 0 y 32");
        }
    }

    public String imprimir() {
        return "Red: " + ipv4 + "/" + prefijo + "\nMáscara: " + mascara + "\nIPv4 en binario: " + ipv4Binario;
    }

}
