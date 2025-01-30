package calculadora;

/**
 *
 * @author fabian
 */
public class Convertir {
    public static String Binario(String ipv4) throws IllegalArgumentException {

            String[] octetos = ipv4.split("\\.");
            if (octetos.length != 4) {
                throw new IllegalArgumentException("La dirección IP debe tener exactamente 4 octetos");
            }
            
            StringBuilder binario = new StringBuilder();
            for (String octeto : octetos) {
                int valor = Integer.parseInt(octeto);
                if (valor < 0 || valor > 255) {
                    throw new IllegalArgumentException("Cada octeto debe estar entre 0 y 255");
                }
                String octetoBinario = String.format("%8s", Integer.toBinaryString(valor))
                        .replace(' ', '0');
                binario.append(octetoBinario);
            }
            return binario.toString();
    }

    public static String IPv4(String binarioFormateado) throws IllegalArgumentException {

            // Eliminar los puntos si los hay
            String binario = binarioFormateado.replace(".", "");
            if (binario.length() != 32) {
                throw new IllegalArgumentException("El formato binario debe tener 32 bits");
            }
            
            StringBuilder ip = new StringBuilder();
            
            // Convertir cada grupo de 8 bits a decimal
            for (int i = 0; i < 32; i += 8) {
                if (i > 0) ip.append(".");
                String octeto = binario.substring(i, i + 8);
                ip.append(Integer.parseInt(octeto, 2));
            }
            return ip.toString();
    }

    public static String calcularMascaraBinaria(int mascara) throws IllegalArgumentException {
        if (mascara < 0 || mascara > 32) {
            throw new IllegalArgumentException("El prefijo de máscara debe estar entre 0 y 32");
        }
        
        StringBuilder mascaraBinaria = new StringBuilder();
        for (int i = 0; i < mascara; i++) {
            mascaraBinaria.append("1");
        }
        for (int i = mascara; i < 32; i++) {
            mascaraBinaria.append("0");
        }
        return mascaraBinaria.toString();
    }

    public static String formatearBinario(String binario) throws IllegalArgumentException {
        if (binario == null) {
            throw new IllegalArgumentException("El binario no puede ser null");
        }
        
        if (binario.length() != 32) {
            throw new IllegalArgumentException("El formato binario debe tener 32 bits");
        }
        
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < binario.length(); i++) {
            if (i > 0 && i % 8 == 0) {
                resultado.append(".");
            }
            resultado.append(binario.charAt(i));
        }
        return resultado.toString();
    }

    public static String convertirIPaBinario(String direccionIP) throws IllegalArgumentException {
        return Binario(direccionIP);
    }
}
