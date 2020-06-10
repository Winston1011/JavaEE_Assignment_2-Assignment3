package edu.bjtu.javaee.service;


import edu.bjtu.javaee.domain.UserCourse;
import edu.bjtu.javaee.exception.BadResourceException;
import edu.bjtu.javaee.exception.ResourceAlreadyExistsException;
import edu.bjtu.javaee.exception.ResourceNotFoundException;
import edu.bjtu.javaee.repository.UserCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCourseService {
    @Autowired
    private UserCourseRepository userCourseRepository;

    private boolean existsById(Long id) {
        return userCourseRepository.existsById(id);
    }

    public List<UserCourse> findAll(int pageNumber, int rowPerPage) {
        List<UserCourse> userCourses = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        userCourseRepository.findAll(sortedByIdAsc).forEach(userCourses::add);
        return userCourses;
    }

    public Long count() {
        return userCourseRepository.count();
    }

    public UserCourse findById(Long id) throws ResourceNotFoundException {
        UserCourse userCourse = userCourseRepository.findById(id).orElse(null);
        if (userCourse==null) {
            throw new ResourceNotFoundException("Cannot find Course with id: " + id);
        }
        else return userCourse;
    }

    public UserCourse save(UserCourse usercourse) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(usercourse.getName())) {
            if (usercourse.getId() != null && existsById(usercourse.getId())) {
                throw new ResourceAlreadyExistsException("Course with id: " + usercourse.getId() + " already exists");
            }
            return userCourseRepository.save(usercourse);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save course");
            exc.addErrorMessage("Course is null or empty");
            throw exc;
        }
    }
}
