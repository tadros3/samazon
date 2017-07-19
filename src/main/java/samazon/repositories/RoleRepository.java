package samazon.repositories;

import org.springframework.data.repository.CrudRepository;
import samazon.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByRole(String role);
}