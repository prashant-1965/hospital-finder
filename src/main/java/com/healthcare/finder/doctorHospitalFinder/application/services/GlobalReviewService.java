package com.healthcare.finder.doctorHospitalFinder.application.services;

import com.healthcare.finder.doctorHospitalFinder.application.dto.GlobalReviewRegisterDto;
import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;

import java.util.List;

public interface GlobalReviewService {
    List<Top10RattingCommentProjection> getTop10RecentGlobalReviews();
    String addGlobalReview(GlobalReviewRegisterDto globalReviewRegisterDto);
}
