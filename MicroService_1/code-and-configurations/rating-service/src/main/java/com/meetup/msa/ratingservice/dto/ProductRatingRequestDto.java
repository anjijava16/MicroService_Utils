package com.meetup.msa.ratingservice.dto;

import lombok.Data;

/**
 * Created by cveerapaneni on 7/1/17.
 */
@Data
public class ProductRatingRequestDto {
    private Float rating;
    private String comments;
}
