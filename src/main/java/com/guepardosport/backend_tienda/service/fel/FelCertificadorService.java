package com.guepardosport.backend_tienda.service.fel;

import com.guepardosport.backend_tienda.entity.Factura;

public interface FelCertificadorService {

    /**
     * Intenta certificar la factura ante el proveedor FEL/SAT.
     * La implementación real (cuando exista el proveedor certificador)
     * debe: enviar el XML/JSON correspondiente, recibir el UUID certificado,
     * y actualizar la factura con ese resultado.
     */
    ResultadoCertificacion certificar(Factura factura);
}