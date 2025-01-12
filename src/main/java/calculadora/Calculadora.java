/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fabian
 */
public class Calculadora {
    private Red redPrincipal;
    private List<Host> hosts;
    private List<Subred> subredes;
    private String[][] resultados;
    private Map<Integer, Integer> prefijoDisponible;

    public Calculadora(String direccionBase, int mascaraBase) throws Exception {
        this.redPrincipal = new Red(direccionBase, mascaraBase);
        this.hosts = new ArrayList<>();
        this.subredes = new ArrayList<>();
    }

    public Red getRedPrincipal() {
        return redPrincipal;
    }

    public void agregarHost(String nombre, int numHosts) {
        hosts.add(new Host(nombre, numHosts));
    }

    public List<Subred> getSubredes() {
        return subredes;
    }

    private Subred guardarSubred(String ipv4, int prefijo, String hostAsignado,
            String primeraUsable, String ultimaUsable, String broadcast, Map<Integer, Integer> disponibilidad, int bitsRestantes, int contTab)
            throws Exception {
        // Buscar si existe una subred con la misma IP y prefijo
        for (int i = 0; i < subredes.size(); i++) {
            Subred subred = subredes.get(i);
            if (subred.getIpv4().equals(ipv4) && subred.getPrefijo() == prefijo) {
                // Si la subred existente no tiene nombre y la nueva sí, reemplazarla
                if (subred.getHostAsignado() == null && hostAsignado != null) {
                    Subred aux = new Subred(ipv4, prefijo, hostAsignado,
                    primeraUsable, ultimaUsable, broadcast, bitsRestantes, contTab);
                    subredes.set(i, aux);
                    int auxDisp = disponibilidad.get(prefijo);
                    disponibilidad.replace(prefijo, auxDisp - 1);
                    return aux;
                }
            }
        }
        // Si no existe, agregar la nueva subred
        Subred aux = new Subred(ipv4, prefijo, hostAsignado,
        primeraUsable, ultimaUsable, broadcast, bitsRestantes, contTab);
        subredes.add(aux);
        return aux;
    }

    public void calcular() throws Exception {
        prefijoDisponible = new HashMap<>();
        // Ordenar hosts de mayor a menor
        hosts.sort((a, b) -> b.getNumHost() - a.getNumHost());

        String direccionActual = redPrincipal.getIpv4Binario();
        int prefijoActual = redPrincipal.getPrefijo();
        int contTab = -1;

        // Crear el array de resultados con el tamaño de hosts
        resultados = new String[hosts.size()][6];

        for (int i = 0; i < hosts.size(); i++) {
            Host hostActual = hosts.get(i);
            int bitsHost = hostActual.getPotencia();
            int mascaraSubred = 32 - bitsHost;
            int bitsRestantes = mascaraSubred - prefijoActual;
            int numSubredesPosibles = (int) Math.pow(2, bitsRestantes);

            // agregar mascaraSubred y disponibilidad numSubredesPosibles en MAP
            prefijoDisponible.put(mascaraSubred, numSubredesPosibles - 1);

            // Obtener parte de red y calcular dirección
            String redBinaria = direccionActual.substring(0, mascaraSubred);
            String hostBinario = "0".repeat(32 - mascaraSubred);

            // Dirección de red
            String direccionRedBinaria = redBinaria + hostBinario;
            String direccionRedFormateada = Convertir.formatearBinario(direccionRedBinaria);
            String direccionRed = Convertir.IPv4(direccionRedFormateada);

            // Primera dirección utilizable
            String primeraUsableBinaria = redBinaria + "0".repeat(bitsHost - 1) + "1";
            String primeraUsableFormateada = Convertir.formatearBinario(primeraUsableBinaria);
            String primeraUsable = Convertir.IPv4(primeraUsableFormateada);

            // Última dirección utilizable
            String ultimaUsableBinaria = redBinaria + "1".repeat(bitsHost - 1) + "0";
            String ultimaUsableFormateada = Convertir.formatearBinario(ultimaUsableBinaria);
            String ultimaUsable = Convertir.IPv4(ultimaUsableFormateada);

            // Dirección de broadcast
            String broadcastBinaria = redBinaria + "1".repeat(bitsHost);
            String broadcastFormateada = Convertir.formatearBinario(broadcastBinaria);
            String broadcast = Convertir.IPv4(broadcastFormateada);

            // Guardar resultados
            resultados[i][0] = hostActual.getNombre();
            resultados[i][1] = direccionRed + "/" + mascaraSubred;
            resultados[i][2] = primeraUsable;
            resultados[i][3] = ultimaUsable;
            resultados[i][4] = broadcast;

            // Manejar la subred principal
            contTab = controlaTab(prefijoActual + "", mascaraSubred + "", contTab);
            Subred subredAsig = guardarSubred(direccionRed, mascaraSubred, hostActual.getNombre(),
                    primeraUsable, ultimaUsable, broadcast, prefijoDisponible, bitsRestantes, contTab);
            resultados[i][5] = subredAsig.getMascara();

            // Calcular combinaciones posibles
            if (bitsRestantes > 0) {
                String redBase = direccionActual.substring(0, prefijoActual);

                for (int j = 1; j < numSubredesPosibles; j++) {
                    String subredBinaria = String.format("%" + bitsRestantes + "s",
                            Integer.toBinaryString(j)).replace(' ', '0');
                    String hostsParte = "0".repeat(bitsHost);
                    String direccionCompletaBinaria = redBase + subredBinaria + hostsParte;

                    String direccionFormateada = Convertir.formatearBinario(direccionCompletaBinaria);
                    String direccionIPCombi = Convertir.IPv4(direccionFormateada);
                    // Manejar las subredes combinadas
                    // contTab = controlaTab(prefijoActual + "", mascaraSubred + "", contTab);
                    guardarSubred(direccionIPCombi, mascaraSubred, null,
                            null, null, null, prefijoDisponible, bitsRestantes, contTab);
                }
            }

            // Actualizar para la siguiente iteración
            if (prefijoDisponible.get(mascaraSubred) == -1) {
                prefijoDisponible.remove(mascaraSubred);
                prefijoActual = mascaraSubred - 1;
            } else {
                prefijoActual = mascaraSubred;
            }
            direccionActual = incrementarBinario(broadcastBinaria);
        }

        // Ordenar subredes usando la representación binaria
        subredes.sort((a, b) -> Convertir.Binario(a.getIpv4()).compareTo(Convertir.Binario(b.getIpv4())));
        // return resultados;
    }

    private String incrementarBinario(String binario) {
        char[] bits = binario.toCharArray();
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i] == '0') {
                bits[i] = '1';
                break;
            }
            bits[i] = '0';
        }
        return new String(bits);
    }

    public String imprimirSubredes() {
        StringBuilder sb = new StringBuilder();
        for (Subred subred : subredes) {
            try {
                String binario = Convertir.Binario(subred.getIpv4());
                int prefijo = subred.getPrefijo();
                int bitsRestantes = subred.getBitsRestantes();
                
                // Dividir el binario en tres partes
                int inicioRed = 0;
                int finRed = prefijo - bitsRestantes;  // Fin de la parte de red principal
                int finRestantes = prefijo;            // Fin de los bits restantes
                
                String redPart = binario.substring(inicioRed, finRed);
                String restantesPart = binario.substring(finRed, finRestantes);
                String hostPart = binario.substring(finRestantes);
                
                sb.append("<div class='subred-line'>");
                sb.append(subred.getTabulacionHtml());
                // Parte de red principal (azul)
                sb.append("<span style='background-color: #e3f2fd'>").append(redPart).append("</span>");
                // Bits restantes (rojo claro)
                sb.append("<span style='background-color: #ffebee'> ").append(restantesPart).append(" </span>");
                // Parte de host (verde claro)
                sb.append("<span style='background-color: #f1f8e9'>").append(hostPart).append("</span>");
                
                // Agregar la IP decimal y máscara
                sb.append("<b> <-> </b>").append(subred.getIpv4()).append("/").append(prefijo);
                
                // Agregar el nombre del host si existe
                if (subred.getHostAsignado() != null) {
                    sb.append("<b> ==> </b><span class='badge bg-primary' style='font-size: 0.9em;'>")
                      .append(subred.getHostAsignado())
                      .append("</span>");
                }
                
                sb.append("</div><br>");
            } catch (Exception e) {
                System.err.println("Error al procesar subred: " + e.getMessage());
            }
        }
        return sb.toString();
    }

    private int controlaTab(String prefijoActual, String mascaraSubRed, int cont) {
        int prefijo = Integer.parseInt(prefijoActual);
        int mascara = Integer.parseInt(mascaraSubRed);
        
        if (prefijo < mascara) {
            // Si el prefijo es menor, aumentamos el nivel de tabulación
            return cont + 1;
        } else if (prefijo > mascara) {
            // Si el prefijo es mayor, disminuimos el nivel de tabulación
            return Math.max(0, cont - 1); // Evitamos números negativos
        }
        // Si son iguales, mantenemos el mismo nivel
        return cont;
    }

    public String imprimirHosts() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table table-striped'>");
        sb.append("<thead><tr>");
        sb.append("<th>Nombre</th><th>Hosts</th><th>Potencia</th>");
        sb.append("</tr></thead><tbody>");

        for (Host host : hosts) {
            sb.append(host.imprimir());
        }

        sb.append("</tbody></table>");
        return sb.toString();
    }

    public String verResultados() {
        if (resultados == null || resultados.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table table-striped'>");
        sb.append("<thead><tr>");
        sb.append("<th>Nombre</th><th>Red</th><th>Primera Utilizable</th><th>Última Utilizable</th><th>Broadcast</th><th>Mascara</th>");
        sb.append("</tr></thead><tbody>");

        for (String[] row : resultados) {
            if (row != null) {
                sb.append("<tr>");
                for (String cell : row) {
                    sb.append("<td>").append(cell != null ? cell : "").append("</td>");
                }
                sb.append("</tr>");
            }
        }

        sb.append("</tbody></table>");
        return sb.toString();
    }
}
