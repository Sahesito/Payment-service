package sahe.com.paymentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull(message = "Se requiere ID de pedido")
    private Long orderId;

    @NotNull(message = "Se requiere identificación de usuario")
    private Long userId;

    @NotNull(message = "Se requiere cantidad")
    @DecimalMin(value = "0.01", message = "El importe debe ser mayor que 0")
    private BigDecimal amount;

    @NotBlank(message = "Se requiere método de pago")
    private String paymentMethod;

    private String description;
}