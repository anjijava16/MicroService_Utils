package com.meetup.msa.productservice.dto;

import lombok.Data;

/**
 * Created by cveerapaneni on 7/1/17.
 */
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
}
