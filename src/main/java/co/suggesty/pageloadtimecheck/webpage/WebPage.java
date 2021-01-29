package co.suggesty.pageloadtimecheck.webpage;

import co.suggesty.pageloadtimecheck.check.Check;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_PAGE")
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    private LocalDateTime create_time;

    private String pageName;

    @OneToMany(mappedBy = "webPage")
    private List<Check> checks;

    private boolean is_use;
}
