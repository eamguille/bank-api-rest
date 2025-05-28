package com.udb.bankapirest.controller;

import com.udb.bankapirest.model.BankAccount;
import com.udb.bankapirest.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    // 1. Obtener lista de cuentas por DUI
    @GetMapping("/cuentas/{dui}")
    public ResponseEntity<List<BankAccount>> getAccountsByDui(@PathVariable String dui) {
        List<BankAccount> accounts = bankAccountService.getAccountsByDui(dui);
        return ResponseEntity.ok(accounts);
    }

    // 2. Abonar efectivo a una cuenta
    @PostMapping("/abonarefectivo")
    public ResponseEntity<BankAccount> depositCash(@RequestBody Map<String, String> payload) {
        try {
            Long accountId = Long.parseLong(payload.get("accountId"));
            BigDecimal amount = new BigDecimal(payload.get("amount"));
            BankAccount updatedAccount = bankAccountService.depositToAccount(accountId, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 3. Retirar efectivo de una cuenta
    @PostMapping("/retirarefectivo")
    public ResponseEntity<BankAccount> withdrawCash(@RequestBody Map<String, String> payload) {
        try {
            Long accountId = Long.parseLong(payload.get("accountId"));
            BigDecimal amount = new BigDecimal(payload.get("amount"));
            BankAccount updatedAccount = bankAccountService.withdrawFromAccount(accountId, amount);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
