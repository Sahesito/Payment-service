package sahe.com.paymentservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sahe.com.paymentservice.model.Payment;

@Data
public class PaymentStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private Payment.PaymentStatus status;
}
