package com.lgcms.consulting.repository;

import com.lgcms.consulting.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
