package test.lomboktest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.lomboktest.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

