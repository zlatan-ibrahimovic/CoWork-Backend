package edu.stage.backend.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Test {
    private String name;
    private int age;

    // Méthode main pour tester les getters et setters
    public static void main(String[] args) {
        Test test = new Test("Alice", 25);
        System.out.println(test.getName()); // Devrait afficher "Alice"
        System.out.println(test.getAge()); // Devrait afficher "25"

        // Modification des valeurs via setters
        test.setName("Bob");
        test.setAge(30);

        System.out.println(test.getName()); // Devrait afficher "Bob"
        System.out.println(test.getAge()); // Devrait afficher "30"

        // Vérification du toString()
        System.out.println(test); // Devrait afficher "Test(name=Bob, age=30)"
    }
}
