package com.healthcare.finder.doctorHospitalFinder.application.projection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Top10RattingCommentProjection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String userName;
    private double rating;
    private String comments;
}
