package com.udb.bankapirest.service;

import com.udb.bankapirest.model.BankAccount;
import com.udb.bankapirest.model.Transaction;
import com.udb.bankapirest.model.User;
import com.udb.bankapirest.repository.BankAccountRepository;
import com.udb.bankapirest.repository.TransactionRepository;
import com.udb.bankapirest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountService {

    @Autowired private UserRepository userRepository;

    @Autowired private BankAccountRepository bankAccountRepository;

    @Autowired private TransactionRepository transactionRepository;

    // Buscar cuentas por DUI
    public List<BankAccount> getAccountsByDui(String dui) {
        User user = userRepository.findByDui(dui)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DUI: " + dui));
        return bankAccountRepository.findByUserId(user.getId());
    }

    // Abonar efectivo
    public BankAccount depositToAccount(Long accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        account.setBalance(account.getBalance().add(amount));
        bankAccountRepository.save(account);

        // Registrar la transacción
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(Transaction.TransactionType.deposito);
        transactionRepository.save(transaction);

        return account;
    }

    // Retirar efectivo
    public BankAccount withdrawFromAccount(Long accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Fondos insuficientes");
        }

        account.setBalance(account.getBalance().subtract(amount));
        bankAccountRepository.save(account);

        // Registrar la transacción
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(Transaction.TransactionType.retiro);
        transactionRepository.save(transaction);

        return account;
    }
}
