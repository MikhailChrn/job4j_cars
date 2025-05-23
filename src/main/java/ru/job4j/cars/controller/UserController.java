package ru.job4j.cars.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.UserService;


import java.util.Optional;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegister(Model model) {

        model.addAttribute("zoneDtos", userService.getAllZones());

        return "users/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           Model model) {
        Optional<User> savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("message",
                    "Пользователь с таким логином уже существует");

            return "redirect:/users/register";
        }

        return "/posts/list";
    }

    @GetMapping("/login")
    public String getLoginPage() {

        return "/users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model,
                            HttpServletRequest request) {
        Optional<User> userOptional = userService
                .findByLoginAndPassword(user.getLogin(), user.getPassword());

        if (userOptional.isEmpty()) {
            model.addAttribute("error",
                    "Логин или пароль введены неверно");
            return "redirect:/users/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", userOptional.get());

        return "/posts/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "/posts/list";
    }

}
