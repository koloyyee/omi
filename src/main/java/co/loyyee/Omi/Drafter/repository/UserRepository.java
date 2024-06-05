package co.loyyee.Omi.Drafter.repository;

import co.loyyee.Omi.Drafter.model.CustomUser;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
	List<CustomUser>  findAll();
	Optional<CustomUser> findByUsername();
	Optional<CustomUser> findByEmail();
	void create(CustomUser user);
	void update(CustomUser user, Long id);
	void delete(Long id);
}
