package com.healthcare.finder.doctorHospitalFinder.application.repository;

import com.healthcare.finder.doctorHospitalFinder.application.entity.GlobalReview;
import com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GlobalReviewRepo extends JpaRepository<GlobalReview, Long> {
    @Query("select new com.healthcare.finder.doctorHospitalFinder.application.projection.Top10RattingCommentProjection(" +
    "u.userName,r.rating,r.comments) "+
    "from GlobalReview r join r.user u order by r.createdAt desc")
    List<Top10RattingCommentProjection> findTop10RecentGlobalReviews(Pageable pageable);

    @Query("select gr.comments from GlobalReview gr join gr.user u where u.userEmail = :userEmail")
    List<String> getCommentByUserEmail(@Param("userEmail") String userEmail);
}
