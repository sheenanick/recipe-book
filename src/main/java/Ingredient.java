import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
//delete method
public class Ingredient{
  private String name;
  private int id;
  private int recipeId;

  public Ingredient(String name, int recipeId){
    this.name = name;
    this.recipeId = recipeId;
  }

  public String getName(){
    return name;
  }

  public int getId(){
    return id;
  }

  public int getRecipeId(){
    return recipeId;
  }

  public void save(){
    try(Connection con = DB.sql2o.open()){
      String sql ="INSERT INTO ingredients (name,recipeid) VALUES (:name,:recipeid)";
      this.id = (int) con.createQuery(sql,true).addParameter("name",this.name).addParameter("recipeid",this.recipeId).executeUpdate().getKey();
    }
  }

  public static List<Ingredient> all(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM ingredients";
      return con.createQuery(sql).executeAndFetch(Ingredient.class);
    }
  }

  public static Ingredient find(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM ingredients WHERE id=:id";
      return con.createQuery(sql).addParameter("id",id).executeAndFetchFirst(Ingredient.class);
    }
  }

  @Override
  public boolean equals(Object otherIngredient){
    if(!(otherIngredient instanceof Ingredient)){
      return false;
    }else{
      Ingredient newIngredient = (Ingredient) otherIngredient;
      return this.getName().equals(newIngredient.getName()) && this.getRecipeId() == newIngredient.getRecipeId();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM ingredients WHERE id = :id";
      con.createQuery(sql).addParameter("id",id).executeUpdate();
    }
  }
}
