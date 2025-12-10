package org.platformv1.platformccv1.controller;

import lombok.RequiredArgsConstructor;
import org.platformv1.platformccv1.dto.AdminUserResponse;
import org.platformv1.platformccv1.dto.ChangeUserRoleRequest;
import org.platformv1.platformccv1.services.AdminUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public List<AdminUserResponse> getAllUsers() {
        return adminUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AdminUserResponse getUser(@PathVariable Long id) {
        return adminUserService.getUserById(id);
    }

    @PatchMapping("/{id}/role")
    public AdminUserResponse changeUserRole(
            @PathVariable Long id,
            @RequestBody ChangeUserRoleRequest request
    ) {
        return adminUserService.changeUserRole(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
    }
}