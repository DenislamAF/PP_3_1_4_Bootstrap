package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entities.Role;

public interface RoleService {
    Role findByName(String name);

    void saveRole(Role role);
}
