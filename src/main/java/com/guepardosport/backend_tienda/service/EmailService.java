package com.guepardosport.backend_tienda.service;

import com.guepardosport.backend_tienda.entity.Cliente;
import com.guepardosport.backend_tienda.entity.Factura;
import com.guepardosport.backend_tienda.entity.Pedido;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.remitente}")
    private String remitente;

    // CU-05: correo de bienvenida al registrarse
    public void enviarBienvenida(Cliente cliente) {
        String asunto = "¡Bienvenido a Guepardo Sports, " + cliente.getNombre() + "!";
        String cuerpo = """
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
                <h2 style="color:#B8722E;">Guepardo Sport</h2>
                <p>Hola %s,</p>
                <p>Tu cuenta se creó correctamente. Ya puedes iniciar sesión y disfrutar del catálogo completo de ropa deportiva.</p>
                <p style="margin-top:24px; color:#888; font-size:12px;">Si tú no creaste esta cuenta, ignora este correo.</p>
            </div>
            """.formatted(cliente.getNombre());

        enviarHtml(cliente.getCorreo(), asunto, cuerpo);
    }

    // CU-07: confirmación de pedido justo al hacer el checkout
    public void enviarConfirmacionPedido(Pedido pedido) {
        String asunto = "Confirmación de tu pedido #" + pedido.getId() + " - Guepardo Sports";

        StringBuilder items = new StringBuilder();
        for (var detalle : pedido.getDetalles()) {
            items.append("""
                <tr>
                    <td style="padding:6px 0;">%s (%s, talla %s)</td>
                    <td style="text-align:center;">%d</td>
                    <td style="text-align:right;">Q%.2f</td>
                </tr>
                """.formatted(
                    detalle.getVariante().getPrendaColor().getPrenda().getNombre(),
                    detalle.getVariante().getPrendaColor().getColor(),
                    detalle.getVariante().getTalla(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()))
            ));
        }

        String cuerpo = """
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
                <h2 style="color:#B8722E;">¡Gracias por tu compra!</h2>
                <p>Pedido <strong>#%d</strong> — %s</p>
                <table style="width:100%%; border-collapse:collapse; margin:16px 0;">
                    <thead>
                        <tr style="border-bottom:1px solid #ddd; text-align:left;">
                            <th>Producto</th><th>Cant.</th><th style="text-align:right;">Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>%s</tbody>
                </table>
                         <p>Descuento: -Q%.2f</p>
                         <p>Envío: Q%.2f</p>
                         <p style="font-size:18px;"><strong>Total: Q%.2f</strong></p>
                         <p style="color:#888; font-size:12px;">IVA incluido (Q%.2f)</p>
                <p>Método de pago: %s</p>
                <p>Dirección de envío: %s</p>
                <p style="margin-top:24px; color:#888; font-size:12px;">Te avisaremos cuando tu pedido sea despachado.</p>
            </div>
            """.formatted(
                pedido.getId(), pedido.getFechaCreacion().toLocalDate(),
                items.toString(),
                pedido.getDescuento(), pedido.getCostoEnvio(),
                pedido.getTotal(), pedido.getIva(),
                pedido.getMetodoPago().equals("EN_LINEA") ? "Pago en línea" : "Contra entrega",
                pedido.getDireccionEnvio()
        );

        enviarHtml(pedido.getCorreoContacto(), asunto, cuerpo);
    }

    // CU-12: factura, cuando el admin marca el pedido como pagado
    public void enviarFactura(Pedido pedido, Factura factura) {
        String asunto = "Factura " + factura.getNumeroFactura() + " - Guepardo Sport";

        String cuerpo = """
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
                <h2 style="color:#B8722E;">Factura de tu compra</h2>
                <p>Número de factura: <strong>%s</strong></p>
                <p>NIT: %s</p>
                <p>Nombre: %s</p>
                <p>Fecha de emisión: %s</p>
                <hr>
                            <p style="font-size:18px;"><strong>Total: Q%.2f</strong></p>
                            <p style="color:#888; font-size:12px;">IVA incluido (Q%.2f)</p>
                <p style="margin-top:16px; color:#888; font-size:12px;">
                    Estado de certificación FEL: %s.
                    %s
                </p>
            </div>
            """.formatted(
                factura.getNumeroFactura(), factura.getNit(), factura.getNombreFacturacion(),
                factura.getFechaEmision().toLocalDate(),
                factura.getTotal(), factura.getIva(),
                factura.getEstadoFel(),
                factura.getEstadoFel().equals("PENDIENTE_CERTIFICACION")
                        ? "Esta factura será certificada ante el SAT en cuanto el proceso esté activo; conserva este correo mientras tanto."
                        : "Documento certificado ante el SAT."
        );

        enviarHtml(pedido.getCorreoContacto(), asunto, cuerpo);
    }

    // CU-11: número de rastreo, cuando el admin despacha el pedido
    public void enviarNumeroRastreo(Pedido pedido) {
        String asunto = "Tu pedido #" + pedido.getId() + " va en camino - Guepardo Sport";

        String cuerpo = """
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
                <h2 style="color:#B8722E;">¡Tu pedido va en camino!</h2>
                <p>Pedido <strong>#%d</strong> fue despachado por <strong>%s</strong>.</p>
                <p>Número de rastreo: <strong style="font-size:18px;">%s</strong></p>
                <p>Puedes usar este número directamente en la página de la empresa de mensajería para ver el estado de tu envío.</p>
                <p>Dirección de entrega: %s</p>
            </div>
            """.formatted(
                pedido.getId(), pedido.getEmpresaMensajeria(), pedido.getNumeroRastreo(),
                pedido.getDireccionEnvio()
        );

        enviarHtml(pedido.getCorreoContacto(), asunto, cuerpo);
    }

    private void enviarHtml(String destinatario, String asunto, String htmlCuerpo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(htmlCuerpo, true);
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            // No queremos que un fallo de correo tumbe la operación principal (registro, pago, etc.)
            System.err.println("Error enviando correo a " + destinatario + ": " + e.getMessage());
        }
    }
    public void enviarRecuperacionPassword(Cliente cliente, String token) {
        String asunto = "Recupera tu contraseña - Guepardo Sport";
        String enlace = "http://localhost:4200/restablecer-password?token=" + token;

        String cuerpo = """
            <div style="font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto;">
                <h2 style="color:#B8722E;">Recupera tu contraseña</h2>
                <p>Hola %s,</p>
                <p>Recibimos una solicitud para restablecer tu contraseña. Haz clic en el siguiente enlace para crear una nueva:</p>
                <p><a href="%s" style="background:#F2A21B; color:#1a1200; padding:10px 20px; text-decoration:none; border-radius:6px; display:inline-block;">Restablecer contraseña</a></p>
                <p style="margin-top:24px; color:#888; font-size:12px;">Este enlace expira en 30 minutos. Si tú no solicitaste esto, ignora este correo.</p>
            </div>
            """.formatted(cliente.getNombre(), enlace);

        enviarHtml(cliente.getCorreo(), asunto, cuerpo);
    }
}