package com.compedia.services;

import com.compedia.entities.RoleEntity;
import com.compedia.enums.RoleName;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // get Role by name
    public RoleEntity getRoleByName(RoleName roleName){
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new NotFoundException(roleName+": Not Found"));
    }


}
