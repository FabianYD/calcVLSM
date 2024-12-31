/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabian
 */
public class Calculadora {
    private Red redPrincipal;
    private List<Host> hosts;
    private List<Subred> subredes;
    private String[][] resultados;

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

    private void manejarSubredDuplicada(String ipv4, int prefijo, String hostAsignado, 
            String primeraUsable, String ultimaUsable, String broadcast) throws Exception {
        // Buscar si existe una subred con la misma IP y prefijo
        for (int i = 0; i < subredes.size(); i++) {
            Subred subred = subredes.get(i);
            if (subred.getIpv4().equals(ipv4) && subred.getPrefijo() == prefijo) {
                // Si la subred existente no tiene nombre y la nueva sí, reemplazarla
                if (subred.getHostAsignado() == null && hostAsignado != null) {
                    subredes.set(i, new Subred(ipv4, prefijo, hostAsignado, 
                        primeraUsable, ultimaUsable, broadcast));
                }
                return;
            }
        }
        // Si no existe, agregar la nueva subred
        subredes.add(new Subred(ipv4, prefijo, hostAsignado, 
            primeraUsable, ultimaUsable, broadcast));
    }

    public void calcular() throws Exception {

        // Ordenar hosts de mayor a menor
        hosts.sort((a, b) -> b.getNumHost() - a.getNumHost());
        
        String direccionActual = redPrincipal.getIpv4Binario();
        int prefijoActual = redPrincipal.getPrefijo();
        
        // Crear el array de resultados con el tamaño de hosts
        resultados = new String[hosts.size()][4];
        
        for (int i = 0; i < hosts.size(); i++) {
            Host hostActual = hosts.get(i);
            int bitsHost = hostActual.getPotencia();            
            int mascaraSubred = 32 - bitsHost;
            int bitsRestantes = mascaraSubred - prefijoActual;
            int numSubredesPosibles = (int) Math.pow(2, bitsRestantes);
            
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
            resultados[i][0] = direccionRed+"/"+mascaraSubred;
            resultados[i][1] = primeraUsable;
            resultados[i][2] = ultimaUsable;
            resultados[i][3] = broadcast;
            
            // Manejar la subred principal
            manejarSubredDuplicada(direccionRed, mascaraSubred, hostActual.getNombre(), 
                primeraUsable, ultimaUsable, broadcast);
            
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
                    manejarSubredDuplicada(direccionIPCombi, mascaraSubred, null, 
                        null, null, null);
                }
            }
            
            // Actualizar para la siguiente iteración
            prefijoActual = mascaraSubred;
            direccionActual = incrementarBinario(broadcastBinaria);
        }

        // Ordenar subredes de menor a mayor
        subredes.sort((a, b) -> a.getIpv4().compareTo(b.getIpv4()));
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
            sb.append(subred.imprimir());
            sb.append("\n\n");
        }
        return sb.toString();
    }

    public String imprimirHosts() {
        StringBuilder sb = new StringBuilder();
        for (Host host : hosts) {
            sb.append(host.imprimir());
            sb.append("<br>");
        }
        return sb.toString();
    }

    public String verResultados() {
        if (resultados == null || resultados.length == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='table table-striped'>");
        sb.append("<thead><tr>");
        sb.append("<th>Red</th><th>Primera Utilizable</th><th>Última Utilizable</th><th>Broadcast</th>");
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
