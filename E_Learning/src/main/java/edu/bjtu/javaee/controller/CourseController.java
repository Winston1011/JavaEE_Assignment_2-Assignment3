package edu.bjtu.javaee.controller;

import edu.bjtu.javaee.domain.Course;
import edu.bjtu.javaee.exception.BadResourceException;
import edu.bjtu.javaee.exception.ResourceAlreadyExistsException;
import edu.bjtu.javaee.exception.ResourceNotFoundException;
import edu.bjtu.javaee.service.CourseService;
import edu.bjtu.javaee.service.UserCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
@Tag(name = "Course", description = "the CourseController API")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CourseController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    @Autowired
    private CourseService courseService;

    @Operation(summary = "list all courses", description = "", tags = { "course" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "courses not found"),
            @ApiResponse(responseCode = "200", description = "OK") })
    @RequestMapping(value = "/courses",method = RequestMethod.GET,produces=MediaTypes.HAL_JSON_VALUE)
    public List<Course> getCourses(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Course> courses = courseService.findAll(pageNumber, ROW_PER_PAGE);
       /* ModelAndView mv = new ModelAndView("course-list");
        mv.addObject(model);
        long count = courseService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("courses", courses);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return mv;
        */
       return courses;
    }

    @Operation(summary = "Find course by ID", description = "Returns a single course with its Hateoas", tags = { "course" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "course not found") })
    @RequestMapping(value = "/courses/id/{courseId}",method = RequestMethod.GET, produces=MediaTypes.HAL_JSON_VALUE)
    public Course getCourseById(Model model, @Parameter(description="Id of the Course to be obtained. Cannot be empty.", required=true)
                                @PathVariable long courseId) {
        Course course = null;
        try {
            course = courseService.findById(courseId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Course not found");
        }
        model.addAttribute("course", course);
        return course;
    }

    @Operation(summary = "Find course by KeyWord,such as c++", description = "Returns a single course with its Hateoas", tags = { "course" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "course not found") })
    @RequestMapping(value = "/courses/keyword/{keyword}",method = RequestMethod.GET, produces=MediaTypes.HAL_JSON_VALUE)
    public List<Course> getCourseByKeyWord(Model model, @Parameter(description="Cannot be empty.", required=true)
    @PathVariable String keyword) {
        List<Course> courses = null;
        try {
            courses = courseService.findByKeyWord(keyword);
        } catch (Exception ex) {
            ex.getCause();
            //model.addAttribute("errorMessage", "Course not found");
        }
        //model.addAttribute("course", course);
        return courses;
        //return "course";//course界面
    }

    @Operation(summary = "Find course branch ,such as programming", description = "Returns courses with its Hateoas", tags = { "course" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "404", description = "course not found") })
    @RequestMapping(value = "/courses/branch/{branch}",method = RequestMethod.GET, produces=MediaTypes.HAL_JSON_VALUE)
    public List<Course> getCourseByBranch(Model model, @Parameter(description="Cannot be empty.", required=true)
    @PathVariable String branch) {
        List<Course> courses = null;
        try {
            courses = courseService.findByBranch(branch);
        } catch (Exception ex) {
            ex.getMessage();
            //model.addAttribute("errorMessage", "Course not found");
        }
        //model.addAttribute("course", course);
        return courses;
        //return "course";//course界面
    }

    @Operation(summary = "Add a new Course", description = "", tags = { "course" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created",
                    content = @Content(schema = @Schema(implementation = Course.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Course already exists") })
    @RequestMapping(value="/courses/add", method = RequestMethod.POST)
    @PreAuthorize("hasRole('Teacher')")
    public Course addCourse(Model model, @ModelAttribute("course") Course course) throws BadResourceException, ResourceAlreadyExistsException {

       /* try {
            Course newCourse = courseService.save(course);
            //ModelAndView mv = new ModelAndView("redirect:/courses/" + String.valueOf(newCourse.getId()));
            mv.addObject(model);
            return mv;
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);

            //model.addAttribute("contact", contact);
            model.addAttribute("add", true);
            ModelAndView mv = new ModelAndView( "course-edit");
            mv.addObject(model);
            return mv;
        }

        */
       return courseService.save(course);
    }

    @Operation(summary = "Edit course page", description = "Returns an editing page", tags = { "course" })
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "invalid input"),
            @ApiResponse(responseCode = "404", description = "page not found") })
    @RequestMapping(value = {"/courses/{courseId}/edit"},method = RequestMethod.POST)
    @PreAuthorize("hasRole('Teacher')")
    public void updateCourse(Model model, @PathVariable long courseId, @ModelAttribute("course") Course course) {
        try {
            course.setId(courseId);
            courseService.update(course);
          //  ModelAndView mv = new ModelAndView( "redirect:/courses/" + String.valueOf(course.getId()));
           // mv.addObject(model);
          //  return mv;
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
         /*   model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);
            ModelAndView mv = new ModelAndView( "course-edit");
            mv.addObject(model);
            return mv;

          */
        }

    }

    @RequestMapping(value = {"/courses/{courseId}/delete"},method = RequestMethod.POST)
    @PreAuthorize("hasRole('Teacher')")
    public void deleteCourseById(Model model, @PathVariable long courseId) {
        try {
            courseService.deleteById(courseId);
            ModelAndView mv = new ModelAndView( "redirect:/courses");
            mv.addObject(model);
        //    return mv;
        } catch (ResourceNotFoundException ex) {
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
       /*     model.addAttribute("errorMessage", errorMessage);
            ModelAndView mv = new ModelAndView( "course");
            mv.addObject(model);
            return mv;

        */
        }
    }

    @Operation(summary = "Register for class", description = "Returns an apply page", tags = { "course" })
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "invalid input"),
            @ApiResponse(responseCode = "404", description = "page not found") })
    @RequestMapping(value = {"/courses/{courseId}/register"},method = RequestMethod.GET)
    @PreAuthorize("hasRole('Student')")
    public ModelAndView registerCourse(Model model, @PathVariable long courseId, @ModelAttribute("course") Course course) {

            ModelAndView mv = new ModelAndView( "course-registration");
            mv.addObject(model);
            return mv;
    }

    @Operation(summary = "Register for class", description = "Returns an apply page", tags = { "course" })
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "invalid input"),
            @ApiResponse(responseCode = "404", description = "page not found") })
    @RequestMapping(value = {"/courses/{courseId}/register"},method = RequestMethod.POST)
    @PreAuthorize("hasRole('Student')")
    public Course registerforCourse(Model model, @PathVariable long courseId, @ModelAttribute("course") Course course) {

        /*ModelAndView mv = new ModelAndView( "course-registration");
        mv.addObject(model);
        return mv;

         */
        return course;
    }

}
