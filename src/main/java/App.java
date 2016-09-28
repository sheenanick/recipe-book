import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;


public class App{
    public static void main(String[] args) {
      staticFileLocation("/public");
      String layout = "templates/layout.vtl";

      get("/", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("recipes", Recipe.all());
        model.put("tags", Tag.all());
        model.put("template", "templates/index.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        String name = request.queryParams("recipe");
        String instructions = request.queryParams("instructions");
        Recipe recipe = new Recipe(name, instructions);
        recipe.save();
        response.redirect("/");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      get("/recipe/:recipeid", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("recipe", Recipe.find(Integer.parseInt(request.params(":recipeid"))));
        model.put("template", "templates/recipe.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      post("/recipe/:recipeid", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Ingredient newIngredient = new Ingredient(request.queryParams("ingredient"),Integer.parseInt(request.params(":recipeid")));
        newIngredient.save();
        model.put("recipe", Recipe.find(Integer.parseInt(request.params(":recipeid"))));
        model.put("template", "templates/recipe.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());

      post("/recipe/:recipeid/added-tag", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        String tagName = request.queryParams("tag");
        boolean found = false;
        for(Tag tag : Tag.all()){
          if(tagName.equals(tag.getName())){
            Recipe.find(Integer.parseInt(request.params(":recipeid"))).addTag(tag);
            found = true;
            break;
          }
        }
        if(!found){
          Tag theTag = new Tag(tagName);
          theTag.save();
          Recipe.find(Integer.parseInt(request.params(":recipeid"))).addTag(theTag);
        }
        model.put("recipe", Recipe.find(Integer.parseInt(request.params(":recipeid"))));
        model.put("template", "templates/recipe.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());
      
      //removes tag from recipe page
      post("/recipe/:id/tag/:tagid/remove-tag", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Recipe.find(Integer.parseInt(request.params(":id"))).removeTag(Tag.find(Integer.parseInt(request.params(":tagid"))));
        model.put("recipe", Recipe.find(Integer.parseInt(request.params(":id"))));
        model.put("template", "templates/recipe.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());
      //tag page
      get("/tag/:tagid", (request, response) -> {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("tag", Tag.find(Integer.parseInt(request.params(":tagid"))));
        model.put("template", "templates/tags.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

      //delete tag
      post("/tag/:tagid/delete-tag", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Tag.find(Integer.parseInt(request.params(":tagid"))).delete();
        model.put("recipes", Recipe.all());
        model.put("tags", Tag.all());
        model.put("template", "templates/index.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());

      //remove recipe from tag
      post("/recipe/:id/tag/:tagid/remove-recipe", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Tag.find(Integer.parseInt(request.params(":tagid"))).removeRecipe(Recipe.find(Integer.parseInt(request.params(":id"))));
        model.put("tag", Tag.find(Integer.parseInt(request.params(":tagid"))));
        model.put("template", "templates/tags.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());

      //deletes recipes
      post("/recipe/:id/delete-recipe", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Recipe.find(Integer.parseInt(request.params(":id"))).delete();
        model.put("recipes", Recipe.all());
        model.put("tags", Tag.all());
        model.put("template", "templates/index.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());

      post("/recipe/:id/ingredient/:ingredientid/deleted-ingredient", (request,response)->{
        Map<String, Object> model = new HashMap<String, Object>();
        Ingredient.find(Integer.parseInt(request.params(":ingredientid"))).delete();
        model.put("recipe", Recipe.find(Integer.parseInt(request.params(":id"))));
        model.put("template", "templates/recipe.vtl");
        return new ModelAndView(model,layout);
      }, new VelocityTemplateEngine());
    }
}
