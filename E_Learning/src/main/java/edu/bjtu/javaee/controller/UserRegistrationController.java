package edu.bjtu.javaee.controller;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import edu.bjtu.javaee.domain.User;
import edu.bjtu.javaee.service.UserService;
import edu.bjtu.javaee.controller.dto.UserRegistrationDto;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Tag(name = "UserRegistrationController", description = "the UserRegistration API")
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @Operation(summary = "show registration page", description = "", tags = { "user registration" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "page not found"),
            @ApiResponse(responseCode = "200", description = "OK") })
    @GetMapping
    public ModelAndView showRegistrationForm(Model model) {
        ModelAndView mv = new ModelAndView("registration");
        mv.addObject(model);
        return mv;
    }

    @Operation(summary = "submit user information", description = "", tags = { "user registration" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "page not found"),
            @ApiResponse(responseCode = "200", description = "OK") })
    @PostMapping
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto, BindingResult result,Model model)
    {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null){
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()){
            ModelAndView mv = new ModelAndView("registration");
            mv.addObject(model);
            return mv;
        }

        userService.save(userDto);
        ModelAndView mv = new ModelAndView("redirect:/registration?success");
        mv.addObject(model);
        return mv;
    }

}
