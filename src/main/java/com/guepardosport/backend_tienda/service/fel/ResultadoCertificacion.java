package com.guepardosport.backend_tienda.service.fel;

public class ResultadoCertificacion {
    private boolean exitoso;
    private String uuidFel;
    private String serieFel;
    private String mensaje;

    public ResultadoCertificacion(boolean exitoso, String uuidFel, String serieFel, String mensaje) {
        this.exitoso = exitoso;
        this.uuidFel = uuidFel;
        this.serieFel = serieFel;
        this.mensaje = mensaje;
    }

    public boolean isExitoso() { return exitoso; }
    public String getUuidFel() { return uuidFel; }
    public String getSerieFel() { return serieFel; }
    public String getMensaje() { return mensaje; }
}