package com.edgar.vaultpoc.application.usecase;
import com.edgar.vaultpoc.domain.model.Payment;import com.edgar.vaultpoc.domain.ports.PaymentRepositoryPort;import org.springframework.stereotype.Service;import java.util.Optional;import java.util.UUID;
@Service public class ReadPaymentUseCase{private final PaymentRepositoryPort repository;public ReadPaymentUseCase(PaymentRepositoryPort repository){this.repository=repository;}public Optional<Payment> execute(UUID id){return repository.findById(id);}}
