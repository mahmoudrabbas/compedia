package com.compedia.configs;

import com.compedia.entities.RoleEntity;
import com.compedia.enums.RoleName;
import com.compedia.services.RoleService;
import com.compedia.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class AppStartup implements CommandLineRunner {
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        if(roleService.getAllRoles().isEmpty()){
            RoleEntity userRole = new RoleEntity();
            userRole.setRoleName(RoleName.ROLE_USER);
            RoleEntity adminRole = new RoleEntity();
            adminRole.setRoleName(RoleName.ROLE_ADMIN);
            roleService.addRoles(List.of(adminRole, userRole));
        }
    }
}
