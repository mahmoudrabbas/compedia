package com.compedia.services;

import com.compedia.entities.RoleEntity;
import com.compedia.enums.RoleName;
import com.compedia.exceptions.NotFoundException;
import com.compedia.repositories.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    // get Role by name
    public RoleEntity getRoleByName(RoleName roleName){
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new NotFoundException(roleName+": Not Found"));
    }

    // add Role
    public RoleEntity addRole(RoleEntity role){
        return roleRepository.save(role);
    }

    // add many Roles
    public List<RoleEntity> addRoles(List<RoleEntity> roles){
        return roleRepository.saveAll(roles);
    }

    // get all roles
    public List<RoleEntity> getAllRoles(){
        return roleRepository.findAll();
    }

}
