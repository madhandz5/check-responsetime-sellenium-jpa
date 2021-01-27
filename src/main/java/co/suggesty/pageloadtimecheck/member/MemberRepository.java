package co.suggesty.pageloadtimecheck.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Member findByEmail(String email);

    Member findByName(String name);
}
