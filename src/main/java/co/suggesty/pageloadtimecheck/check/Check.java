package co.suggesty.pageloadtimecheck.check;

import co.suggesty.pageloadtimecheck.page.Page;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_CHECK")
public class Check {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "page_id")
    private Page pages;

    @Column(name = "loading_time")
    private int time;

    @Column(name = "check_time")
    private LocalDateTime checkedAt;
}
