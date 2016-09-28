//  update
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Tag{
  private String name;
  private int id;

  public Tag(String name){
    this.name = name;
  }

  public String getName(){
    return name;
  }

  public int getId(){
    return id;
  }

  public void save(){
    try(Connection con = DB.sql2o.open()){
      String sql ="INSERT INTO tags (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql,true).addParameter("name",this.name).executeUpdate().getKey();
    }
  }

  public static List<Tag> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM tags";
      return con.createQuery(sql).executeAndFetch(Tag.class);
    }
  }

  public static Tag find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM tags WHERE id=:id";
      return con.createQuery(sql).addParameter("id",id).executeAndFetchFirst(Tag.class);
    }
  }

  @Override
  public boolean equals(Object otherTag){
    if(!(otherTag instanceof Tag)){
      return false;
    }else{
      Tag newTag = (Tag) otherTag;
      return this.getName().equals(newTag.getName());
    }
  }

  public List<Recipe>getRecipes() {
    try(Connection con = DB.sql2o.open()) {
      String joinQuery = "SELECT recipeid FROM tags_recipes WHERE tagid = :tagid";
      List<Integer> recipeIds = con.createQuery(joinQuery)
        .addParameter("tagid", this.getId())
        .executeAndFetch(Integer.class);

      List<Recipe> recipes = new ArrayList<Recipe>();

      for (Integer recipeId : recipeIds) {
        String recipeQuery = "SELECT * FROM recipes WHERE id = :recipeId";
        Recipe recipe = con.createQuery(recipeQuery)
          .addParameter("recipeId", recipeId)
          .executeAndFetchFirst(Recipe.class);
        recipes.add(recipe);
      }
      return recipes;
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM tags WHERE id=:id";
      con.createQuery(sql).addParameter("id",this.id).executeUpdate();
      String joinDeleteQuery = "DELETE FROM tags_recipes WHERE tagid =:tagid";
      con.createQuery(joinDeleteQuery).addParameter("tagid",this.id).executeUpdate();
    }
  }

  public void removeRecipe(Recipe recipe){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM tags_recipes WHERE recipeid = :recipeid AND tagid = :tagid";
      con.createQuery(sql)
        .addParameter("recipeid", recipe.getId())
        .addParameter("tagid", this.getId())
        .executeUpdate();
    }
  }

}
