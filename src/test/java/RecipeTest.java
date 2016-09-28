import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class RecipeTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void recipe_instantiatesCorrectly_true() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    assertTrue(newRecipe instanceof Recipe);
  }

  @Test
  public void save_savesRecipe_true() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    assertEquals(Recipe.all().get(0), newRecipe);
  }

  @Test
  public void find_findsRecipe_true() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    assertTrue(Recipe.find(newRecipe.getId()).equals(newRecipe));
  }

  @Test
  public void all_returnsListOfAllRecipes() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    Recipe otherRecipe = new Recipe("turkey",  "put in oven");
    otherRecipe.save();
    assertEquals(Recipe.all().size(), 2);
  }

  @Test
  public void equals_ChecksIfEqual_true() {
    Recipe newRecipe = new Recipe("turkey",  "put in oven");
    newRecipe.save();
    Recipe otherRecipe = new Recipe("turkey",  "put in oven");
    otherRecipe.save();
    assertTrue(newRecipe.equals(otherRecipe));
  }

  @Test
  public void addTag_AddsTagToRecipe(){
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    Tag tag = new Tag("Thanksgiving");
    tag.save();
    newRecipe.addTag(tag);
    Tag savedTag = newRecipe.getTags().get(0);
    assertTrue(tag.equals(savedTag));
  }

  @Test
  public void getTags_returnsAllTags(){
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    Tag tag = new Tag("Thanksgiving");
    tag.save();
    newRecipe.addTag(tag);
    List savedTags = newRecipe.getTags();
    assertEquals(savedTags.size(),1);
  }

  @Test
  public void delete_DeletesARecipe_true() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    newRecipe.delete();
    assertEquals(0, Recipe.all().size());
  }

  @Test
  public void delete_DeletesAllTagsAndRecipeAssociations() {
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    Tag tag = new Tag("Thanksgiving");
    tag.save();
    newRecipe.addTag(tag);
    newRecipe.delete();
    assertEquals(0, tag.getRecipes().size());
  }

  @Test
  public void removeTag_RemovesTagFromRecipe(){
    Tag testTag = new Tag("Thanksgiving");
    testTag.save();
    Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
    newRecipe.save();
    newRecipe.addTag(testTag); //we added this
    newRecipe.removeTag(testTag);
    List savedTags = newRecipe.getTags();
    assertEquals(0, savedTags.size());
  }
}
