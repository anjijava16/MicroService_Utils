package com.meetup.msa.productservice.dto;

import lombok.Data;

/**
 * Created by cveerapaneni on 7/1/17.
 */
@Data
public class ProductWithRatingDto extends ProductDto {
    private Float rating;
}
