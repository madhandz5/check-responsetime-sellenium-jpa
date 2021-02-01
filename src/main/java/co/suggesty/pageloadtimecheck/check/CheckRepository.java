package co.suggesty.pageloadtimecheck.check;

import co.suggesty.pageloadtimecheck.webpage.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CheckRepository extends JpaRepository<Check, Long> {
    @Transactional
    void deleteByWebPage(WebPage webPage);
}
