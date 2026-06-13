package com.edgar.vaultpoc.domain.ports;
import com.edgar.vaultpoc.domain.model.Payment;
import java.util.Optional;
import java.util.UUID;
public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
}
