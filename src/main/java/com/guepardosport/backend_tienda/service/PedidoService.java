package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.dto.*;
import com.guepardosport.backend_tienda.entity.*;
import com.guepardosport.backend_tienda.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final BigDecimal TASA_IVA = new BigDecimal("0.12"); // IVA Guatemala 12%
    private static final BigDecimal COSTO_ENVIO_FIJO = new BigDecimal("25.00");
    private static final BigDecimal MONTO_ENVIO_GRATIS = new BigDecimal("300.00");

    @Autowired private FacturaRepository facturaRepository;
    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private PrendaVarianteRepository prendaVarianteRepository;
    @Autowired private CuponService cuponService;
    @Autowired private CuponRepository cuponRepository;
    @Autowired private FacturaService facturaService;
    @Autowired private EmailService emailService;
    @Autowired private RecurrenteService recurrenteService;

    @Transactional
    public PedidoResponseDTO crearPedido(CheckoutRequestDTO dto) {

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }
        if (!dto.getMetodoPago().equals("EN_LINEA") && !dto.getMetodoPago().equals("CONTRA_ENTREGA")) {
            throw new RuntimeException("Método de pago inválido");
        }

        Pedido pedido = new Pedido();
        pedido.setNombreContacto(dto.getNombreContacto());
        pedido.setCorreoContacto(dto.getCorreoContacto());
        pedido.setDireccionEnvio(dto.getDireccionEnvio());
        pedido.setMetodoPago(dto.getMetodoPago());

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal ivaProductos = BigDecimal.ZERO;

        // 1. Revalidar stock, armar detalle Y calcular el IVA de cada línea individualmente
        for (CheckoutItemDTO item : dto.getItems()) {
            PrendaVariante variante = prendaVarianteRepository.findById(item.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada: " + item.getVarianteId()));

            if (variante.getStock() < item.getCantidad()) {
                throw new RuntimeException("Sin stock suficiente para talla " + variante.getTalla()
                        + " (disponible: " + variante.getStock() + ")");
            }

            variante.setStock(variante.getStock() - item.getCantidad());
            prendaVarianteRepository.save(variante);

            BigDecimal precioUnitario = variante.getPrendaColor().getPrenda().getPrecioBase();
            BigDecimal subtotalLinea = precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad()));
            BigDecimal ivaLinea = subtotalLinea.multiply(TASA_IVA).setScale(2, RoundingMode.HALF_UP);

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setVariante(variante);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setIvaLinea(ivaLinea);
            detalles.add(detalle);

            subtotal = subtotal.add(subtotalLinea);
            ivaProductos = ivaProductos.add(ivaLinea);
        }

        // 2. Aplicar cupón (opcional, solo uno por pedido)
        BigDecimal descuento = BigDecimal.ZERO;
        Cupon cuponAplicado = null;
        if (dto.getCodigoCupon() != null && !dto.getCodigoCupon().isBlank()) {
            cuponAplicado = cuponService.validarCuponParaUso(dto.getCodigoCupon(), subtotal);

            if (cuponAplicado.getTipoDescuento().equals("PORCENTAJE")) {
                descuento = subtotal.multiply(cuponAplicado.getValor())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            } else {
                descuento = cuponAplicado.getValor();
            }
            if (descuento.compareTo(subtotal) > 0) descuento = subtotal;

            cuponAplicado.setUsosActuales(cuponAplicado.getUsosActuales() + 1);
            cuponRepository.save(cuponAplicado);
            pedido.setCupon(cuponAplicado);
        }

        // 3. Costo de envío (gratis si supera el monto mínimo)
        BigDecimal baseParaEnvio = subtotal.subtract(descuento);
        BigDecimal costoEnvio = baseParaEnvio.compareTo(MONTO_ENVIO_GRATIS) >= 0 ? BigDecimal.ZERO : COSTO_ENVIO_FIJO;

        BigDecimal iva = ivaProductos; // Solo se calcula IVA sobre los productos, no sobre el envío

        BigDecimal total = subtotal.subtract(descuento).add(costoEnvio).add(iva);

        pedido.setSubtotal(subtotal);
        pedido.setDescuento(descuento);
        pedido.setCostoEnvio(costoEnvio);
        pedido.setIva(iva);
        pedido.setTotal(total);
        pedido.setDetalles(detalles);

        Pedido guardado = pedidoRepository.save(pedido);
        facturaService.generarFactura(guardado, dto.getNit(), dto.getNombreFacturacion(), dto.getDireccionFiscal());

        PedidoResponseDTO respuesta = convertirADTO(guardado);

        if (dto.getMetodoPago().equals("EN_LINEA")) {
            guardado.setEstadoPago("PENDIENTE_PAGO_EN_LINEA");
            String urlCheckout = recurrenteService.crearCheckout(
                    guardado.getId(), guardado.getTotal(), "Pedido Guepardo Sport #" + guardado.getId()
            );
            guardado.setCheckoutIdPasarela(urlCheckout);
            pedidoRepository.save(guardado);
            respuesta.setUrlPago(urlCheckout);
        } else {
            emailService.enviarConfirmacionPedido(guardado);
        }

        return respuesta;
    }

    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public PedidoResponseDTO obtenerPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        return convertirADTO(pedido);
    }

    // CU-11: admin actualiza estado logístico
    public PedidoResponseDTO actualizarEstadoLogistico(Long id, String nuevoEstado) {
        validarEstadoLogistico(nuevoEstado);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        pedido.setEstadoLogistico(nuevoEstado);
        return convertirADTO(pedidoRepository.save(pedido));
    }

    // CU-12: admin marca como pagado un pedido contra entrega
    public PedidoResponseDTO marcarComoPagado(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
        pedido.setEstadoPago("PAGADO");
        Pedido guardado = pedidoRepository.save(pedido);

        Factura factura = facturaRepository.findByPedidoId(id).orElse(null);
        if (factura != null) {
            emailService.enviarFactura(guardado, factura);
        }

        return convertirADTO(guardado);
    }

    // CU-15: cliente cancela, solo si aún no fue despachado
    @Transactional
    public PedidoResponseDTO cancelar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));

        if (!pedido.getEstadoLogistico().equals("RECIBIDO")) {
            throw new RuntimeException("El pedido ya fue despachado y no se puede cancelar");
        }

        for (DetallePedido detalle : pedido.getDetalles()) {
            PrendaVariante variante = detalle.getVariante();
            variante.setStock(variante.getStock() + detalle.getCantidad());
            prendaVarianteRepository.save(variante);
        }

        pedido.setEstadoLogistico("CANCELADO");
        return convertirADTO(pedidoRepository.save(pedido));
    }

    private PedidoResponseDTO convertirADTO(Pedido pedido) {
        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setId(pedido.getId());
        dto.setNombreContacto(pedido.getNombreContacto());
        dto.setCorreoContacto(pedido.getCorreoContacto());
        dto.setDireccionEnvio(pedido.getDireccionEnvio());
        dto.setMetodoPago(pedido.getMetodoPago());
        dto.setEstadoPago(pedido.getEstadoPago());
        dto.setEstadoLogistico(pedido.getEstadoLogistico());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setDescuento(pedido.getDescuento());
        dto.setCostoEnvio(pedido.getCostoEnvio());
        dto.setIva(pedido.getIva());
        dto.setTotal(pedido.getTotal());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setEmpresaMensajeria(pedido.getEmpresaMensajeria());
        dto.setNumeroRastreo(pedido.getNumeroRastreo());

        if (pedido.getDetalles() != null) {
            dto.setDetalles(pedido.getDetalles().stream().map(d -> {
                DetallePedidoDTO dDto = new DetallePedidoDTO();
                dDto.setPrenda(d.getVariante().getPrendaColor().getPrenda().getNombre());
                dDto.setColor(d.getVariante().getPrendaColor().getColor());
                dDto.setTalla(d.getVariante().getTalla());
                dDto.setCantidad(d.getCantidad());
                dDto.setPrecioUnitario(d.getPrecioUnitario());
                dDto.setIvaLinea(d.getIvaLinea());
                return dDto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    private static final List<String> ESTADOS_LOGISTICOS_VALIDOS = List.of(
            "RECIBIDO", "EN_PREPARACION", "ENVIADO", "ENTREGADO", "CANCELADO"
    );

    private void validarEstadoLogistico(String estado) {
        if (!ESTADOS_LOGISTICOS_VALIDOS.contains(estado)) {
            throw new RuntimeException("Estado logístico inválido. Valores permitidos: " + ESTADOS_LOGISTICOS_VALIDOS);
        }
    }

    public PedidoResponseDTO actualizarRastreo(Long id, ActualizarRastreoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));

        pedido.setEmpresaMensajeria(dto.getEmpresaMensajeria());
        pedido.setNumeroRastreo(dto.getNumeroRastreo());
        pedido.setEstadoLogistico("ENVIADO");

        Pedido guardado = pedidoRepository.save(pedido);
        emailService.enviarNumeroRastreo(guardado);

        return convertirADTO(guardado);
    }
}