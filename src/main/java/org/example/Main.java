package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Experiment with Hibernate!");
        System.out.println("Currently, database has users, profiles, and hobbies :)");

        System.out.print("If you know the user id, enter it: ");
        Long userId = Long.valueOf(scanner.nextLine());

        System.out.print("If you know the user first name, enter it (comma-seperated): ");
        String name = scanner.nextLine();

        try (EntityManagerFactory emf = Persistence.createEntityManagerFactory("experiment")) {
            EntityManager entityManager = getEntityManager(emf);
            UserRepository repository = new UserRepository(entityManager);
            repository.findAll(userId, name).forEach(user -> {
                System.out.println("User Info");
                System.out.println("User: " + user);

                System.out.println("Profile Info");
                System.out.println("Profile: " + user.getProfile());

                System.out.println("Hobby Info");
                System.out.println("Hobbies: " + user.getHobbies());
            });
            entityManager.close();
        }
    }

    private static EntityManager getEntityManager(EntityManagerFactory emf) {
        return emf.createEntityManager();
    }
}