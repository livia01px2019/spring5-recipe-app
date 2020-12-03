package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.respositories.CategoryRepository;
import guru.springframework.respositories.RecipeRepository;
import guru.springframework.respositories.UOMRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UOMRepository uomRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UOMRepository uomRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.uomRepository = uomRepository;
    }

    private List<Recipe> listRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

//        UOM
        Optional<UnitOfMeasure> eachUOMOpt = uomRepository.findByDescription("Each");
        if(!eachUOMOpt.isPresent()) {
            throw new RuntimeException("UOM");
        }
        Optional<UnitOfMeasure> tablespoonUOMOpt = uomRepository.findByDescription("Tablespoon");
        if(!tablespoonUOMOpt.isPresent()) {
            throw new RuntimeException("UOM");
        }

        UnitOfMeasure eachUOM = eachUOMOpt.get();
        UnitOfMeasure tablespoonUOM = tablespoonUOMOpt.get();

//        Category
        Optional<Category> americanCategoryOpt = categoryRepository.findByDescription("American");
        if(!americanCategoryOpt.isPresent()) {
            throw new RuntimeException("category");
        }

        Optional<Category> mexicanCategoryOpt = categoryRepository.findByDescription("Mexican");
        if(!mexicanCategoryOpt.isPresent()) {
            throw new RuntimeException("category");
        }

        Category americanCategory = americanCategoryOpt.get();
        Category mexicanCategory = mexicanCategoryOpt.get();

//        guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("just make guac");
        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("this is guac");
        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);

        guacRecipe.getIngredients().add(new Ingredient("avocados", new BigDecimal(2), eachUOM, guacRecipe));
        guacRecipe.getIngredients().add(new Ingredient("onion", new BigDecimal(2), tablespoonUOM, guacRecipe));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        recipes.add(guacRecipe);

        return recipes;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(listRecipes());
    }
}
