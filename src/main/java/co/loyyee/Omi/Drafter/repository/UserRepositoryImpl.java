package co.loyyee.Omi.Drafter.repository;

import co.loyyee.Omi.Drafter.model.CustomUser;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{

	@Autowired
	@Qualifier("drafterJdbcClient")
	private JdbcClient jdbcClient;
	
	public UserRepositoryImpl() {
	}
	
	@Override
	public List<CustomUser> findAll() {
		String sql = "SELECT * from users";
		return jdbcClient.sql(sql).query(CustomUser.class).list();
	}
	
	@Override
	public Optional<CustomUser> findByUsername() {
		return Optional.empty();
	}
	
	@Override
	public Optional<CustomUser> findByEmail() {
		return Optional.empty();
	}
	
	@Override
	public void create(CustomUser user) {
	
	}
	
	@Override
	public void update(CustomUser user, Long id) {
	
	}
	
	@Override
	public void delete(Long id) {
	
	}
}
