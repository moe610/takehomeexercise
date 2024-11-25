package com.fetch.takehomeexercise.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@Data
@Builder
public class Receipt {
    @JsonProperty("id")
    @Pattern(regexp = "^\\S+$")
    private String id;

    @NotBlank
    @Pattern(regexp = "^[\\w\\s\\-&]+$")
    private String retailer;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime purchaseTime;

    @NotNull
    @Size(min = 1, message = "There must be at least one item")
    private List<@Valid Items> items;

    @NotBlank
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String total;

    public static ReceiptBuilder builder() {
        return new ReceiptBuilder().id(UUID.randomUUID().toString());
    }
}
