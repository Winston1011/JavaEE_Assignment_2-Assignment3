package edu.bjtu.javaee.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
@Tag(name = "MainController", description = "the Main API")
public class MainController {

  /*  @GetMapping("/")
    public String root() {
        return "index";
    }*/

    @Value("${msg.title}")
    private String title;

   /* @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("title", title);
        return "index";
    }
*/
   @Operation(summary = "login", description = "Returns a login page ", tags = { "main" })
   @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "page not found") })
    @GetMapping("/login")
    public ModelAndView login(Model model) {
       ModelAndView mv = new ModelAndView("login");
       mv.addObject(model);
       return mv;
    }

    @Operation(summary = "admin user", description = "Returns page in terms of oauth", tags = { "main" })
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "page not found") })
    @GetMapping("/user")
    public ModelAndView userIndex(Model model) {
        model.addAttribute("title", title);
        ModelAndView mv = new ModelAndView("index");
        mv.addObject(model);
        return mv;
    }

    @Operation(summary = "teacher user", description = "Returns page in terms of oauth", tags = { "main" })
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "page not found") })
    @GetMapping("/teacher")
    public ModelAndView teacherIndex(Model model) {
        ModelAndView mv = new ModelAndView("teacher");
        mv.addObject(model);
        return mv;
    }

    @Operation(summary = "student user", description = "Returns page in terms of oauth", tags = { "main" })
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "page not found") })
    @GetMapping("/student")
    public ModelAndView studentIndex(Model model) {
        ModelAndView mv = new ModelAndView("student");
        mv.addObject(model);
        return mv;
    }

    @Operation(summary = "invalidhandle", description = "Returns an error page ", tags = { "main" })
    @GetMapping("/invalidsession")
    public ModelAndView invalidsession(Model model) {
        ModelAndView mv = new ModelAndView("invalidsession");
        mv.addObject(model);
        return mv;
    }

    @Operation(summary = "websocket chat page", tags = { "main" })
    @GetMapping("/chat")
    public ModelAndView websocketchat(Model model) {
        ModelAndView mv = new ModelAndView("client");
        mv.addObject(model);
        return mv;
    }

    @GetMapping("/live/home")
    public ModelAndView videostream(Model model) {
        ModelAndView mv = new ModelAndView("play");
        mv.addObject(model);
        return mv;
    }
}
