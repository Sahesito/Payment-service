package sahe.com.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sahe.com.paymentservice.dto.PaymentRequest;
import sahe.com.paymentservice.dto.PaymentResponse;
import sahe.com.paymentservice.dto.PaymentStatusUpdateRequest;
import sahe.com.paymentservice.model.Payment;
import sahe.com.paymentservice.repository.PaymentRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // Obtener todos los pagos
    public List<PaymentResponse> getAllPayments() {
        log.info("Getting all payments");
        return paymentRepository.findAll()
                .stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    // Obtener pago x id
    public PaymentResponse getPaymentById(Long id) {
        log.info("Getting payment by id: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return new PaymentResponse(payment);
    }

    // Obtener pagos por orden
    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        log.info("Getting payments by order id: {}", orderId);
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    // Obtener pagos por usuario
    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        log.info("Getting payments by user id: {}", userId);
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    // Obtener pago por estado
    public List<PaymentResponse> getPaymentsByStatus(Payment.PaymentStatus status) {
        log.info("Getting payments by status: {}", status);
        return paymentRepository.findByStatus(status)
                .stream()
                .map(PaymentResponse::new)
                .collect(Collectors.toList());
    }

    // Crear pago
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Creating payment for order: {}", request.getOrderId());

        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setDescription(request.getDescription());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setTransactionId("TXN-" + UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment created with id: {}", savedPayment.getId());
        processPayment(savedPayment);

        return new PaymentResponse(savedPayment);
    }

    // Actualizar
    @Transactional
    public PaymentResponse updatePaymentStatus(Long id, PaymentStatusUpdateRequest request) {
        log.info("Updating payment {} status to: {}", id, request.getStatus());

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        payment.setStatus(request.getStatus());
        Payment updatedPayment = paymentRepository.save(payment);

        log.info("Payment status updated successfully");
        return new PaymentResponse(updatedPayment);
    }

    // ========== ELIMINAR PAGO ==========
    @Transactional
    public void deletePayment(Long id) {
        log.info("Deleting payment: {}", id);

        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found with id: " + id);
        }

        paymentRepository.deleteById(id);
        log.info("Payment deleted successfully");
    }

    // ========== PROCESAR PAGO (Simulación) ==========
    private void processPayment(Payment payment) {
        // En producción, aquí se haría la llamada a la pasarela de pago
        // Por ahora, simulamos que el pago siempre es exitoso
        log.info("Processing payment for transaction: {}", payment.getTransactionId());

        try {
            // Simular delay de procesamiento
            Thread.sleep(1000);

            // Marcar como completado
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            log.info("Payment processed successfully: {}", payment.getTransactionId());
        } catch (Exception e) {
            log.error("Payment processing failed: {}", e.getMessage());
            payment.setStatus(Payment.PaymentStatus.FAILED);
            paymentRepository.save(payment);
        }
    }
}
