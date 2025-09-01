package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Top10RattingCommentProjection {
    private String userName;
    private double rating;
    private String comments;
}
