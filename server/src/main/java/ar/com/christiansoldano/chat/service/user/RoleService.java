package ar.com.christiansoldano.chat.service.user;

import ar.com.christiansoldano.chat.exception.user.RoleNotFoundException;
import ar.com.christiansoldano.chat.model.user.Role;
import ar.com.christiansoldano.chat.repository.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role '%s' was not found", name)));
    }
}
