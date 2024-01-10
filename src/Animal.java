import java.awt.Color;

public class Animal extends Boid{
    // Variables. Some ma not be used in this case
    String name;
    float elvPreference, seperaion, cohesion, alignment, visualRange;
    float speed, size, hunger;
    Animal[] predators, prey;

    Animal(double x, double y, double dx, double dy){
        super(x,y,dx,dy);
    }

    // Add predator to predator arr
    public void AddPredator(Animal predator){
        Animal[] temp = new Animal[predators.length +1];
        for(int i=0; i<predators.length; i++){
            temp[i] = predators[i];
        }
        temp[temp.length-1] = predator;

        predators = temp;
    }
    // Add new prey to prey arr
    public void AddPrey(Animal newPrey){
        Animal[] temp = new Animal[prey.length +1];
        for(int i=0; i<prey.length; i++){
            temp[i] = prey[i];
        }
        temp[temp.length-1] = newPrey;

        prey = temp;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public float getElvPreference() {
        return elvPreference;
    }


    public void setElvPreference(float elvPreference) {
        this.elvPreference = elvPreference;
    }


    public float getSeperaion() {
        return seperaion;
    }


    public void setSeperaion(float seperaion) {
        this.seperaion = seperaion;
    }


    public float getCohesion() {
        return cohesion;
    }


    public void setCohesion(float cohesion) {
        this.cohesion = cohesion;
    }


    public float getAlignment() {
        return alignment;
    }


    public void setAlignment(float alignment) {
        this.alignment = alignment;
    }


    public float getVisualRange() {
        return visualRange;
    }


    public void setVisualRange(float visualRange) {
        this.visualRange = visualRange;
    }

    public float getSpeed() {
        return speed;
    }


    public void setSpeed(float speed) {
        this.speed = speed;
    }


    public float getSize() {
        return size;
    }


    public void setSize(float size) {
        this.size = size;
    }


    public float getHunger() {
        return hunger;
    }


    public void setHunger(float hunger) {
        this.hunger = hunger;
    }
}
