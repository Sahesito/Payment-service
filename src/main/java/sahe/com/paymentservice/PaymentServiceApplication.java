package sahe.com.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
        System.out.println("""
            PAYMENT SERVICE - RUNNING
            Port: 8086
            GET /payments - Get all payments
            GET /payments/{id} - Get payment by id
            GET /payments/order/{id} - Get payments by order
            POST /payments - Create payment
            PATCH /payments/{id}/status - Update status
            """);
    }
}

