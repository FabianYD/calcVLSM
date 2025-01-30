/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calculadora;

/**
 *
 * @author fabian
 */
public class Subred extends Red {
    private String hostAsignado;
    private String primeraUtilizable;
    private String ultimaUtilizable;
    private String broadcast;
    private int bitsRestantes;
    private String tabulacionHtml;

    public Subred(String ipv4, int prefijo, String hostAsignado, String primeraUtilizable, String ultimaUtilizable,
            String broadcast){
        super(ipv4, prefijo);
        this.hostAsignado = hostAsignado;
        this.primeraUtilizable = primeraUtilizable;
        this.ultimaUtilizable = ultimaUtilizable;
        this.broadcast = broadcast;
    }

    public Subred(String ipv4, int prefijo, String hostAsignado, String primeraUtilizable, String ultimaUtilizable,
            String broadcast, int bitsRestantes, int contTab) {
        this(ipv4, prefijo, hostAsignado, primeraUtilizable, ultimaUtilizable, broadcast);
        this.bitsRestantes = bitsRestantes;
        this.tabulacionHtml = "&nbsp;<span>・</span>&nbsp;".repeat(contTab);
    }

    public String getHostAsignado() {
        return hostAsignado;
    }

    public String getPrimeraUtilizable() {
        return primeraUtilizable;
    }

    public String getUltimaUtilizable() {
        return ultimaUtilizable;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public int getBitsRestantes() {
        return bitsRestantes;
    }

    public void setTabulacionHtml(String tabulacionHtml) {
        this.tabulacionHtml = tabulacionHtml;
    }

    public String getTabulacionHtml() {
        return tabulacionHtml;
    }

    @Override
    public String imprimir() {
        String nombre= "\n";
        if(this.hostAsignado != null)
            nombre = " <--> " + this.hostAsignado + "\n";
        return super.imprimir() + nombre;
    }
}
