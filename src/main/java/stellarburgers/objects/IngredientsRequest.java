package stellarburgers.objects;

public class IngredientsRequest {
    private String[] ingredients;

    public IngredientsRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsRequest() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
