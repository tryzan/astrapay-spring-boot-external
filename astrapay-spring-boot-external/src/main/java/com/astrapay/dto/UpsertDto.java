package com.astrapay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpsertDto {

    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String description;


}