package co.suggesty.pageloadtimecheck.check;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;
    private final CheckRepository checkRepository;

    @GetMapping(value = "/check/all")
    public String checkResponse(Model model) {
        checkService.timeChecking();
        model.addAttribute("done","Done");
        return "page/check";
    }

    @GetMapping(value = "/check/list")
    public String checkList(Model model
                            ,@RequestParam(defaultValue = "0") int page
                            ,@RequestParam(defaultValue = "10") int size
                            ,@RequestParam(defaultValue = "1") int sort) {

        Pageable pageable;

        if (sort == 1) {
            pageable = PageRequest.of(page, size, Sort.by("checkedAt").descending().and(Sort.by("webPage.pageName").ascending()));
        } else {
            pageable = PageRequest.of(page, size, Sort.by("webPage.pageName").ascending().and(Sort.by("checkedAt").descending()));
        }

        Page<Check> checkList = checkService.getCheckList(pageable);

        int currentPage = checkList.getPageable().getPageNumber() + 1;
        int lastPage = checkList.getTotalPages();
        int blockCnt = 10;

        int blockStart = ((currentPage-1)/blockCnt) * blockCnt+1;
        if (blockStart  <  1) { blockStart = 1; }
        int blockEnd = ((currentPage-1)/blockCnt+1) * blockCnt;
        if (lastPage < blockEnd) { blockEnd = lastPage; }

        model.addAttribute("sort", sort);
        model.addAttribute("checkList", checkList);
        model.addAttribute("startPage", blockStart);
        model.addAttribute("endPage", blockEnd);

        return "page/checkList";
    }

    @GetMapping(value = "/check/download", produces = "application/vnd.ms-excel")
    public String downloadList(Model model) {
        List<Check> xlsxCheckList = checkRepository.findAll();
        model.addAttribute("xlsxCheckList", xlsxCheckList);
        return "xlsxForm";
    }
}
