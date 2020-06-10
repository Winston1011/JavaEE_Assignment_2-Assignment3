package edu.bjtu.javaee.repository;

import edu.bjtu.javaee.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Long>,
        JpaSpecificationExecutor<Course> {

    @Query(value = "select o from Course o where o.name like %:keyword%")
     List<Course> findByKeyWord(@Param("keyword") String keyword);

    @Query(value = "select o from Course o where o.classify like %:branch%")
    List<Course> findByBranch(@Param("branch") String branch);
}
