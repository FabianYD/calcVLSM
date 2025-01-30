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

    public Red(String ipv4, int prefijo) throws IllegalArgumentException {
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

    private void validarIPv4(String ipv4) throws IllegalArgumentException {
        if (ipv4 == null || ipv4.trim().isEmpty()) {
            throw new IllegalArgumentException("La dirección IP no puede estar vacía");
        }

        if (!ipv4.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            throw new IllegalArgumentException("La dirección IP debe tener el formato XXX.XXX.XXX.XXX");
        }

        String[] octetos = ipv4.split("\\.");
        if (octetos[0].equals("255")) {
            throw new IllegalArgumentException("La dirección IP no puede comenzar con 255, esto normalmente se usa para máscaras de red");
        }

        for (String octeto : octetos) {
            int valor = Integer.parseInt(octeto);
            if (valor < 0 || valor > 255) {
                throw new IllegalArgumentException("Cada octeto debe estar entre 0 y 255");
            }
        }
        
        // Validaciones de direcciones especiales
        if (ipv4.equals("0.0.0.0")) {
            throw new IllegalArgumentException("No se permite la dirección 0.0.0.0 (ruta por defecto)");
        }
        if (ipv4.equals("127.0.0.1")) {
            throw new IllegalArgumentException("No se permite la dirección 127.0.0.1 (localhost)");
        }
    }

    private void validarPrefijo(int prefijo) throws IllegalArgumentException {
        if (prefijo < 8 || prefijo > 32) {
            throw new IllegalArgumentException("El prefijo debe estar entre 8 y 32");
        }
    }

    public String imprimir() {
        try {
            return Convertir.formatearBinario(ipv4Binario) + " -> " + ipv4 + "/" + prefijo;
        } catch (IllegalArgumentException e) {
            return "Error al formatear la red: " + e.getMessage();
        }
    }
}
