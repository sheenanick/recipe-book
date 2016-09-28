import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class IngredientTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void Ingredient_instantiatesCorrectly_true() {
    Ingredient newIngredient = new Ingredient("pumpkin",1);
    assertTrue(newIngredient instanceof Ingredient);
  }

  @Test
  public void save_savesIngredient_true() {
    Ingredient newIngredient = new Ingredient("pumpkin",1);

    newIngredient.save();
    assertEquals(Ingredient.all().get(0), newIngredient);
  }

  @Test
  public void find_findsIngredient_true() {
    Ingredient newIngredient = new Ingredient("pumpkin",1);
    newIngredient.save();
    assertTrue(Ingredient.find(newIngredient.getId()).equals(newIngredient));
  }

  @Test
  public void all_returnsListOfAllIngredients() {
    Ingredient newIngredient = new Ingredient("pumpkin",1);
    newIngredient.save();
    Ingredient otherIngredient = new Ingredient("turkey",1);
    otherIngredient.save();
    assertEquals(Ingredient.all().size(), 2);
  }

  @Test
  public void equals_ChecksIfEqual_true() {
    Ingredient newIngredient = new Ingredient("pumpkin",1);
    newIngredient.save();
    Ingredient otherIngredient = new Ingredient("pumpkin",1);
    otherIngredient.save();
    assertTrue(newIngredient.equals(otherIngredient));
  }
}
