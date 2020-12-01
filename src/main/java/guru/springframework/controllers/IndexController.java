package guru.springframework.controllers;

import guru.springframework.domain.Category;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.respositories.CategoryRepository;
import guru.springframework.respositories.UOMRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class IndexController {
    
    private CategoryRepository categoryRepository;
    private UOMRepository uomRepository;

    public IndexController(CategoryRepository categoryRepository, UOMRepository uomRepository) {
        this.categoryRepository = categoryRepository;
        this.uomRepository = uomRepository;
    }
    
    @RequestMapping({"", "/"})
    public String getIndexPage() {
        Optional<Category> categoryOptional = categoryRepository.findByDescription("American");
        Optional<UnitOfMeasure> unitOfMeasureOptional = uomRepository.findByDescription("Teaspoon");

        System.out.println("Cat id is: " + categoryOptional.get().getId());
        System.out.println("UOM id is: " + unitOfMeasureOptional.get().getId());

        return "index";
    }
}
