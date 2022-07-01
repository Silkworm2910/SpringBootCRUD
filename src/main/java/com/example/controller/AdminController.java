package com.example.controller;

import com.example.model.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.allUsers());
        return "user-list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.FindUserByID(id));
        return "user-info";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.findAll());
        return "user-create";
    }

    @PostMapping("/new")
    public String create(@Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "rolesNames") String[] rolesNames) {
        if (bindingResult.hasErrors()) {
            return "user-create";
        }
        userService.saveUser(user, rolesNames);
        return "redirect:/admin";
    }

    @GetMapping("/new/default")
    public String createdDefault() {
        return "redirect:/admin";
    }

    @PostMapping("/new/default")
    public String createDefault() {
        userService.createDefaultUser();
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.FindUserByID(id));
        model.addAttribute("roles", roleService.findAll());
        return "user-update";
    }

    @PatchMapping("/edit")
    public String update(@Valid User user,
                         BindingResult bindingResult,
                         @RequestParam(value = "rolesNames") String[] rolesNames) {
        if (bindingResult.hasErrors()) {
            return "user-update";
        }
        userService.updateUser(user, rolesNames);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}