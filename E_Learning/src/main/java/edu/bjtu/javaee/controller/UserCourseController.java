package edu.bjtu.javaee.controller;

import edu.bjtu.javaee.domain.UserCourse;
import edu.bjtu.javaee.exception.ResourceNotFoundException;
import edu.bjtu.javaee.service.UserCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@Tag(name = "UserCourseController", description = "the UserCourseController API")
public class UserCourseController {

    private final int ROW_PER_PAGE = 8;

    @Autowired
    private UserCourseService usercourseService;

    @Operation(summary = "list all usercourses", description = "", tags = { "usercourse" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "courses not found"),
            @ApiResponse(responseCode = "200", description = "OK") })
    @RequestMapping(value = "/user/courses",method = RequestMethod.GET)
    public ModelAndView getCourseSelection(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber)
    {
        List<UserCourse> userCourses = usercourseService.findAll(pageNumber, ROW_PER_PAGE);
        ModelAndView mv = new ModelAndView( "usercourse-list");
        mv.addObject(model);
        long count = usercourseService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("userCourses", userCourses);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);

        return mv;
    }

    @Operation(summary = "Find course by ID", description = "Returns a single course with its Hateoas", tags = { "usercourse" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(schema = @Schema(implementation = UserCourse.class))),
            @ApiResponse(responseCode = "404", description = "course not found") })
    @RequestMapping(value = "/usercourses/{usercourseId}",method = RequestMethod.GET,produces= MediaTypes.HAL_JSON_VALUE)
    public UserCourse getCourseById(Model model, @PathVariable long usercourseId) {
        UserCourse userCourse = null;
        try {
            userCourse = usercourseService.findById(usercourseId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Course not found");
        }
        model.addAttribute("usercourse", userCourse);
        return userCourse;
        //return "usercourse";
    }

    @Operation(summary = "download courseware", description = "", tags = { "usercourse" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "courseware not found"),
            @ApiResponse(responseCode = "200", description = "OK") })
    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void downloadCourseWare(Model model)
    {
       /* ModelAndView mv = new ModelAndView( "downloadcourse");
        mv.addObject(model);
        return mv;

        */
    }
}
