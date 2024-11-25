package com.fetch.takehomeexercise.controllers;

import com.fetch.takehomeexercise.dao.Receipt;
import com.fetch.takehomeexercise.service.ReceiptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/receipts")
public class WebControllers {
    private final ReceiptService receiptService;

    public WebControllers(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> process(@RequestBody @Valid Receipt request) throws Exception {
        return ResponseEntity.ok(receiptService.processReceipt(request));
    }

    @GetMapping("/{receiptId}/points")
    public ResponseEntity<Integer> process(@PathVariable @Valid  String receiptId) {
        return ResponseEntity.ok(receiptService.getPoints(receiptId));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = "The receipt is invalid";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExceptions(Exception ex) {
        String errorMessage = "No receipt found for that id";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
