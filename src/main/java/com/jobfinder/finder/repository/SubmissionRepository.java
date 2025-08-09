package com.jobfinder.finder.repository;

import com.jobfinder.finder.entity.SubmissionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionEntity, String>, JpaSpecificationExecutor<SubmissionEntity> {
  List<SubmissionEntity> findByPostId(Long postId);
}
