package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private UserService userService;

    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        String usernameOfCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(usernameOfCurrentUser);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("list", userService.getAllUsers());
        return "admin-table";
    }

    @GetMapping(value = "/admin/add-user")
    public String successAddedPage(HttpServletRequest request,
                                   @RequestParam("age") int age,
                                   @RequestParam("roles") String roles) {
        User user = new User();
        user.setUsername(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setAge((byte) age);
        Collection<Role> collection =
                Arrays.stream(roles.replaceAll("\\s", "").split(","))
                .map(s -> roleService.findByName(s)).collect(Collectors.toList());
        user.setRoles(collection);
        System.out.println(request.getParameter("roles"));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete-user")
    public String successDeletedPage(@RequestParam(value = "id") Long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/update-user")
    public String successUpdatedPage(HttpServletRequest request,
                                     @RequestParam("idToEdit") Long id,
                                     @RequestParam("ageToEdit") byte age,
                                     @RequestParam("rolesToEdit") String roles) {
        User userFromDB = userService.findById(id);
        userFromDB.setUsername(request.getParameter("emailToEdit"));
        userFromDB.setPassword(request.getParameter("passwordToEdit"));
        userFromDB.setFirstName(request.getParameter("firstNameToEdit"));
        userFromDB.setLastName(request.getParameter("lastNameToEdit"));
        userFromDB.setAge(age);
        Collection<Role> collection =
                Arrays.stream(roles.replaceAll("\\s", "").split(","))
                        .map(s -> roleService.findByName(s)).collect(Collectors.toList());
        userFromDB.setRoles(collection);
        userService.saveUser(userFromDB);
        return "redirect:/admin";
    }
}
