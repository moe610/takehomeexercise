package com.fetch.takehomeexercise.service;

import com.fetch.takehomeexercise.dao.Receipt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.ceil;

@Service
public class ReceiptService {

    private final Map<String, Receipt> receiptStorage = new ConcurrentHashMap<>();

    public String processReceipt(Receipt request) {
        var receipt = Receipt.builder()
                .retailer(request.getRetailer())
                .purchaseDate(request.getPurchaseDate())
                .purchaseTime(request.getPurchaseTime())
                .items(request.getItems())
                .total(request.getTotal())
                .build();

        receiptStorage.put(receipt.getId(), receipt);
        return receipt.getId();
    }

    public Integer getPoints(String receiptId) {
        Receipt receipt = receiptStorage.get(receiptId);
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt not found for ID: " + receiptId);
        }

        return calculatePoints(receipt);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        for (char c : receipt.getRetailer().toCharArray()) {
            if (Character.isDigit(c) || Character.isLetter(c)) {
                points++;
            }
        }

        int decimalIndex = receipt.getTotal().indexOf('.');
        String fractionalPart = receipt.getTotal().substring(decimalIndex + 1, decimalIndex + 3);
        if ("00".equals(fractionalPart)) {
            points += 50;
        }

        if (Double.parseDouble(receipt.getTotal()) % 0.25 == 0){
            points += 25;
        }

        int itemCount = receipt.getItems().size();
        if (itemCount > 1){
            int totalItems = itemCount/2;
            points += (totalItems * 5);
        }

        for (int i = 0; i < receipt.getItems().size(); i++) {
            String shortDescription = receipt.getItems().get(i).getShortDescription().trim();
            if (shortDescription.length() % 3 == 0){
                points += (int) ceil(Double.parseDouble(receipt.getItems().get(i).getPrice()) * 0.2);
            }
        }

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate receiptDate = LocalDate.parse(receipt.getPurchaseDate().toString(),df);
        int receiptDayOfMonth = receiptDate.getDayOfMonth();
        if(receiptDayOfMonth % 2 != 0){
            points += 6;
        }

        DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
        LocalTime receiptTime = LocalTime.parse(receipt.getPurchaseTime().toString(), tf);
        LocalTime startTime = LocalTime.of(14,0);
        LocalTime EndTime = LocalTime.of(16,0);
        if (receiptTime.isAfter(startTime) && receiptTime.isBefore(EndTime)) {
            points += 10;
        }

        return points;
    }
}
