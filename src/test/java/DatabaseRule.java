import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DatabaseRule extends ExternalResource{

  @Override
  protected void before() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/recipe_box_test", null, null);
  }

  @Override
    protected void after() {
    try(Connection con = DB.sql2o.open()) {
      String deleteRecipesQuery = "DELETE FROM recipes *;";
      String deleteTagsQuery = "DELETE FROM tags *;";
      String deleteIngredientsQuery = "DELETE FROM ingredients *;";
      String deleteJoinsQuery = "DELETE FROM tags_recipes *;";
      con.createQuery(deleteJoinsQuery).executeUpdate();
      con.createQuery(deleteRecipesQuery).executeUpdate();
      con.createQuery(deleteTagsQuery).executeUpdate();
      con.createQuery(deleteIngredientsQuery).executeUpdate();

    }
  }

}
