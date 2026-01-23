package JavaConstructorOverloading;

public class Constructor {
    String language;

    // constructor with no parameter
    Constructor(){
        this.language = "Java";
    }

    // constructor with a single parameter
    Constructor(String language){
        this.language = language;
    }

    public void getName(){
        System.out.println("Programming Language: " + this.language);
    }
}
