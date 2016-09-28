import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.List;

public class TagTest{

    @Rule
    public DatabaseRule database = new DatabaseRule();

    @Test
    public void instantiates_CreatesATag(){
      Tag newTag = new Tag("Thanksgiving");
      assertTrue(newTag instanceof Tag);
    }
    @Test
    public void save_SavesATag_true(){
      Tag newTag = new Tag("Thanksgiving");
      newTag.save();
      assertEquals(Tag.all().get(0),newTag);
    }

    @Test
    public void find_findsATag_Tag(){
      Tag newTag = new Tag("Thanksgiving");
      newTag.save();
      assertTrue(Tag.find(newTag.getId()).equals(newTag));
    }

    @Test
    public void equals_ChecksIfEqual_true(){
      Tag oneTag = new Tag("Thanksgiving");
      oneTag.save();
      Tag anotherTag = new Tag("Thanksgiving");
      anotherTag.save();
      assertTrue(oneTag.equals(anotherTag));
    }

    @Test
    public void all_returnsListOfAllTags(){
      Tag oneTag = new Tag("Thanksgiving");
      oneTag.save();
      Tag anotherTag = new Tag("Thanksgiving");
      anotherTag.save();
      assertEquals(2,Tag.all().size());
    }

    @Test
    public void getRecipes_returnsAllRecipes_List() {
      Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
      newRecipe.save();
      Tag tag = new Tag("Thanksgiving");
      tag.save();
      newRecipe.addTag(tag);
      List savedRecipes = tag.getRecipes();
      assertEquals(1, savedRecipes.size());
    }

    @Test
    public void delete_DeletesATag_true(){
      Tag tag = new Tag("Thanksgiving");
      tag.save();
      tag.delete();
      assertEquals(0,Tag.all().size());
    }

    @Test
    public void delete_DeletesAllTagsAndRecipeAssociations(){
      Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
      newRecipe.save();
      Tag tag = new Tag("Thanksgiving");
      tag.save();
      newRecipe.addTag(tag);
      tag.delete();
      assertEquals(0, newRecipe.getTags().size());
    }

    @Test
    public void removeRecipe_RemovesRecipeFromTag(){
      Recipe newRecipe = new Recipe("pumpkin pie", "mix together and bake");
      newRecipe.save();
      Tag tag = new Tag("Thanksgiving");
      tag.save();
      newRecipe.addTag(tag);
      tag.removeRecipe(newRecipe);
      List savedRecipes = tag.getRecipes();
      assertEquals(0, savedRecipes.size());
    }
}
