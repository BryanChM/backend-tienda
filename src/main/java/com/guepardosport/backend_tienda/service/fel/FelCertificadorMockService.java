package com.guepardosport.backend_tienda.service.fel;

import com.guepardosport.backend_tienda.entity.Factura;
import org.springframework.stereotype.Service;

/**
 * Implementación temporal mientras no hay proveedor certificador contratado.
 * No se conecta a ningún servicio externo — simplemente deja la factura
 * marcada como pendiente, para que el sistema funcione de extremo a extremo
 * sin bloquear el proyecto.
 *
 * CUANDO SE CONTRATE UN PROVEEDOR FEL (ej. Megaprint, Digifact, INFILE, etc.):
 * 1. Crear una nueva clase, ej. FelCertificadorDigifactService, que implemente
 *    FelCertificadorService con la integración real (llamada HTTP al proveedor).
 * 2. Anotar esa nueva clase con @Service y @Primary (o quitar @Service de este mock).
 * 3. Eliminar o desactivar esta clase (FelCertificadorMockService).
 * No hace falta tocar FacturaService ni el resto del sistema.
 */
@Service
public class FelCertificadorMockService implements FelCertificadorService {

    @Override
    public ResultadoCertificacion certificar(Factura factura) {
        // Sin proveedor conectado todavía: no se certifica, queda pendiente.
        return new ResultadoCertificacion(
                false,
                null,
                null,
                "Proveedor FEL no configurado todavía. Factura generada en modo local."
        );
    }
}