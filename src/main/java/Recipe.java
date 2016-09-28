//update  removeTag getIngredients
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
  private int id;
  private String name;
  private String instructions;

  public Recipe(String name, String instructions) {
    this.name = name;
    this.instructions = instructions;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }


  public String getInstructions() {
    return instructions;
  }

  public void addTag(Tag tag){
    try(Connection con = DB.sql2o.open()){
      String sql ="INSERT INTO tags_recipes (tagid, recipeid) VALUES (:tagid,:recipeid)";
      con.createQuery(sql).addParameter("tagid",tag.getId()).addParameter("recipeid", this.getId()).executeUpdate();
    }
  }

  public List<Tag> getTags(){
    try(Connection con = DB.sql2o.open()){
      String joinQuery = "SELECT tagid FROM tags_recipes WHERE recipeid = :recipeid";
      List<Integer> tagIds = con.createQuery(joinQuery).addParameter("recipeid",this.getId()).executeAndFetch(Integer.class);

      List<Tag> allTags = new ArrayList<Tag>();

      for(Integer tag : tagIds){
        String tagQuery = "SELECT * FROM tags WHERE id=:tagid";
        Tag theTag = con.createQuery(tagQuery).addParameter("tagid",tag).executeAndFetchFirst(Tag.class);
        allTags.add(theTag);
      }
      return allTags;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM recipes WHERE id = :id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
      String joinDeleteQuery = "DELETE FROM tags_recipes WHERE recipeid = :recipeid";
      con.createQuery(joinDeleteQuery)
        .addParameter("recipeid", this.id)
        .executeUpdate();
    }
  }

  public void removeTag(Tag tag){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM tags_recipes WHERE tagid = :tagid AND recipeid = :recipeid";
      con.createQuery(sql).addParameter("tagid",tag.getId()).addParameter("recipeid",this.getId()).executeUpdate();
    }
  }













  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO recipes (name, instructions) VALUES (:name, :instructions)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("instructions", this.instructions)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Recipe> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes";
      return con.createQuery(sql).executeAndFetch(Recipe.class);
    }
  }

  public static Recipe find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM recipes WHERE id = :id";
      return con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Recipe.class);
    }
  }

  @Override
  public boolean equals(Object otherRecipe) {
    if(!(otherRecipe instanceof Recipe)) {
      return false;
    } else {
      Recipe newRecipe = (Recipe) otherRecipe;
      return this.getName().equals(newRecipe.getName()) &&
        this.getInstructions().equals(newRecipe.getInstructions());
    }
  }
}
