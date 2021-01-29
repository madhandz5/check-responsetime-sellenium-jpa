package co.suggesty.pageloadtimecheck.check;

import co.suggesty.pageloadtimecheck.webpage.WebPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
}
