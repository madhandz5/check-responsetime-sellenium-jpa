package co.suggesty.pageloadtimecheck.webpage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPageRepository extends JpaRepository<WebPage, Long> {
}
