package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping(value = "/admin")
    public String adminPage(Model model) {
        model.addAttribute("list", userService.getAllUsers());
        return "admin-table";
    }

    @GetMapping(value = "/admin/add-user")
    public String addUserPage() {
        return "add-user";
    }

    @GetMapping(value = "/admin/successfully-added")
    public String successAddedPage(HttpServletRequest request,
                                   @RequestParam("age") byte age,
                                   @RequestParam("roles") String roles) {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setAge(age);
        user.setEmail(request.getParameter("email"));
        Collection<Role> collection =
                Arrays.stream(roles.replaceAll("\\s", "").split(","))
                .map(s -> roleService.findByName(s)).collect(Collectors.toList());
        user.setRoles(collection);
        userService.saveUser(user);
        return "successfully-added";
    }

    @GetMapping(value = "/admin/delete-user")
    public String deleteUserPage(Model model) {
        model.addAttribute("list", userService.getAllUsers());
        return "delete-user";
    }

    @GetMapping(value = "/admin/successfully-deleted")
    public String successDeletedPage(@RequestParam(value = "id") Long id) {
        userService.removeUser(id);
        return "successfully-deleted";
    }

    @GetMapping(value = "/admin/update-user")
    public String updateUserPage(Model model) {
        model.addAttribute("list", userService.getAllUsers());
        return "update-user";
    }

    @GetMapping(value = "/admin/successfully-updated")
    public String successUpdatedPage(HttpServletRequest request,
                                     @RequestParam("id") Long id,
                                     @RequestParam("age") byte age,
                                     @RequestParam("roles") String roles) {
        User userFromDB = userService.findById(id);
        userFromDB.setUsername(request.getParameter("username"));
        userFromDB.setPassword(request.getParameter("password"));
        userFromDB.setFirstName(request.getParameter("firstName"));
        userFromDB.setLastName(request.getParameter("lastName"));
        userFromDB.setAge(age);
        userFromDB.setEmail(request.getParameter("email"));
        Collection<Role> collection =
                Arrays.stream(roles.replaceAll("\\s", "").split(","))
                        .map(s -> roleService.findByName(s)).collect(Collectors.toList());
        userFromDB.setRoles(collection);
        userService.saveUser(userFromDB);
        return "successfully-updated";
    }
}
