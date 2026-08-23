package com.guepardosport.backend_tienda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class CheckoutRequestDTO {

    @NotEmpty(message = "El carrito no puede estar vacío")
    @Valid
    private List<CheckoutItemDTO> items;

    private String codigoCupon;

    @NotBlank(message = "El nombre de contacto es obligatorio")
    private String nombreContacto;

    @NotBlank(message = "El correo de contacto es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correoContacto;

    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;

    @NotBlank(message = "El método de pago es obligatorio")
    @Pattern(regexp = "EN_LINEA|CONTRA_ENTREGA", message = "Método de pago inválido")
    private String metodoPago;

    private String nit;
    private String nombreFacturacion;
    private String direccionFiscal;

    public List<CheckoutItemDTO> getItems() { return items; }
    public void setItems(List<CheckoutItemDTO> items) { this.items = items; }

    public String getCodigoCupon() { return codigoCupon; }
    public void setCodigoCupon(String codigoCupon) { this.codigoCupon = codigoCupon; }

    public String getNombreContacto() { return nombreContacto; }
    public void setNombreContacto(String nombreContacto) { this.nombreContacto = nombreContacto; }

    public String getCorreoContacto() { return correoContacto; }
    public void setCorreoContacto(String correoContacto) { this.correoContacto = correoContacto; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getNombreFacturacion() { return nombreFacturacion; }
    public void setNombreFacturacion(String nombreFacturacion) { this.nombreFacturacion = nombreFacturacion; }

    public String getDireccionFiscal() { return direccionFiscal; }
    public void setDireccionFiscal(String direccionFiscal) { this.direccionFiscal = direccionFiscal; }
}