package co.suggesty.pageloadtimecheck.page;

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
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    private LocalDateTime create_time;


    private String page_name;

    @OneToMany(mappedBy = "pages")
    private List<Check> checks;

    private boolean is_use;
}
