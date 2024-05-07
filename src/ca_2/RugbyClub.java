/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca_2;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class RugbyClub {

    enum Option {
        SORT, SEARCH, ADD_PLAYER, GENERATE_RANDOM_PLAYER, EXIT
    }

    enum Coach {
        HEAD_COACH, ASSISTANT_COACH, SCRUM_COACH
    }

    enum Team {
        A_SQUAD, B_SQUAD, UNDER_13_SQUAD
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> people = new ArrayList<>();
        boolean fileExists = false;

        while (!fileExists) {
            System.out.print("Please enter the filename to read: ");
            String filename = scanner.nextLine();
            File file = new File(filename);
            if (file.exists() && !file.isDirectory()) {
                people = readFromFile(filename);
                fileExists = true;
            } else {
                System.out.println("File does not exist. Please try again.");
            }
        }

        while (true) {
            System.out.println("Please select an option from the following:");
            System.out.println("SORT(1) SEARCH(2) ADD_PLAYER(3) GENERATE_RANDOM_PLAYER(4) EXIT(5)");

            int choice = Integer.parseInt(scanner.nextLine());
            Option selectedOption = Option.values()[choice - 1];

            switch (selectedOption) {
                case SORT:
                    sortDummyList(people);
                    // Print the sorted list
                    printSortedArray(people);
                    break;
                case SEARCH:
                    System.out.print("Enter search query: ");
                    String query = scanner.nextLine();
                    searchList(people, query);
                    break;
                case ADD_PLAYER:
                    addNewPlayer(scanner, people);
                    break;
                case GENERATE_RANDOM_PLAYER:
                    generateRandomPlayers(people , 5);

                    break;
                case EXIT:
                    saveToFile(people);
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private static void sortDummyList(ArrayList<String> people) {
        // Check if the list is empty or has only one element
        if (people.size() <= 1) {
            return; // Base case: No need to sort if list has 0 or 1 element
        }
        ArrayList<String> dataCopy = new ArrayList<>(people); // Create a copy of the original list

        // Divide the list into two halves
        int mid = dataCopy.size() / 2;
        ArrayList<String> leftHalf = new ArrayList<>(dataCopy.subList(0, mid));
        ArrayList<String> rightHalf = new ArrayList<>(dataCopy.subList(mid, dataCopy.size()));

        // Recursively sort the two halves
        sortDummyList(leftHalf);
        sortDummyList(rightHalf);

        // Merge the sorted halves
        merge(people, leftHalf, rightHalf); // Merge into the original list



    }



    private static void merge(ArrayList<String> people, ArrayList<String> leftHalf, ArrayList<String> rightHalf) {
        int leftIndex = 0, rightIndex = 0, mergeIndex = 0;
        ArrayList<String> merged = new ArrayList<>(leftHalf.size() + rightHalf.size());

        // Compare elements from left and right halves and merge them in sorted order
        while (leftIndex < leftHalf.size() && rightIndex < rightHalf.size()) {
            if (leftHalf.get(leftIndex).compareToIgnoreCase(rightHalf.get(rightIndex)) < 0) {
                merged.add(leftHalf.get(leftIndex++));
            } else {
                merged.add(rightHalf.get(rightIndex++));
            }
        }

        // Copy remaining elements from left half, if any
        while (leftIndex < leftHalf.size()) {
            merged.add(leftHalf.get(leftIndex++));
        }

        // Copy remaining elements from right half, if any
        while (rightIndex < rightHalf.size()) {
            merged.add(rightHalf.get(rightIndex++));
        }

        // Copy the sorted elements back to the original list
        for (String element : merged) {
            people.set(mergeIndex++, element);
        }

    }

    private static void searchList(ArrayList<String> people, String query) {
        ArrayList<String> dataCopy = new ArrayList<>(people); // Create a copy of the original list
        // Sort the people array
        Collections.sort(dataCopy);

        // Perform Binary Search
        int low = 0;
        int high = dataCopy.size() - 1;
        boolean found = false;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            String person = dataCopy.get(mid);

            if (person.toLowerCase().contains(query.toLowerCase())) {
                System.out.println("Found: " + person);
                found = true;
                break;
            } else if (person.compareToIgnoreCase(query) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (!found) {
            System.out.println("No matching player found for query: " + query);
        }
    }

    private static void addNewPlayer(Scanner scanner, ArrayList<String> people) {
        String name;
        boolean validName;
        do {
            System.out.print("Please input the Player Name: ");
            name = scanner.nextLine();
            validName = name.matches("[a-zA-Z\\s]+"); // Check if name contains only alphabets and white spaces
            if (!validName) {
                System.out.println("Invalid name. Please enter alphabetic characters and white spaces only.");
            }
        } while (!validName);

        // Validate Coach Choice
        System.out.println("Please select from the following Coach Staff:");
        for (Coach coach : Coach.values()) {
            System.out.println(coach.ordinal() + 1 + ". " + coach.name().replace('_', ' '));
        }
        int coachIndex;
        do {
            System.out.print("Enter the number corresponding to the Coach: ");
            coachIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } while (coachIndex < 0 || coachIndex >= Coach.values().length);
        Coach coach = Coach.values()[coachIndex];

        // Validate Team Choice
        System.out.println("Please select the Teams:");
        for (Team team : Team.values()) {
            System.out.println(team.ordinal() + 1 + ". " + team.name().replace('_', ' '));
        }
        int teamIndex;
        do {
            System.out.print("Enter the number corresponding to the Team: ");
            teamIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } while (teamIndex < 0 || teamIndex >= Team.values().length);
        Team team = Team.values()[teamIndex];

        // Add new player to the list
        String newPlayer = name + ", Coach: " + coach.name().replace('_', ' ') + ", Team: " + team.name().replace('_', ' ');
        people.add(newPlayer);
        System.out.println("New player added: " + newPlayer);
    }


    private static ArrayList<String> readFromFile(String filename) {
        ArrayList<String> people = new ArrayList<>();
        try {
            File file = new File(filename);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                people.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
        }
        return people;
    }

    private static void saveToFile(ArrayList<String> people) {
        try {
            FileWriter writer = new FileWriter("ClubForm.txt");
            for (String person : people) {
                writer.write(person + "\n");
            }
            writer.close();
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving data to file.");
        }
    }



    private static void generateRandomPlayers(ArrayList<String> people, int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            // Generate random name
            String name = generateRandomName();

            // Generate random Coach type
            Coach coach = generateRandomCoach();

            // Generate random Team
            Team team = generateRandomTeam();

            // Create player data string
            String playerData = name + ", Coach: " + coach.name().replace('_', ' ') + ", Team: " + team.name().replace('_', ' ');

            // Add player data to the array
            people.add(playerData);
            System.out.println("Player # 0" + (i + 1));
            System.out.println(playerData);

        }

    }

    private static String generateRandomName() {
        // List of possible first names
        String[] firstNames = {"John", "Jane", "David", "Emily", "Michael", "Emma", "Chris", "Sophia"};

        // List of possible last names
        String[] lastNames = {"Smith", "Johnson", "Brown", "Wilson", "Taylor", "Anderson", "Clark", "White"};

        Random random = new Random();

        // Randomly select a first and last name
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        // Concatenate the first and last name
        return firstName + " " + lastName;
    }

    private static Coach generateRandomCoach() {
        // Generate a random index for Coach enumeration
        int index = new Random().nextInt(Coach.values().length);
        return Coach.values()[index];
    }

    private static Team generateRandomTeam() {
        // Generate a random index for Team enumeration
        int index = new Random().nextInt(Team.values().length);
        return Team.values()[index];
    }


    public static void printSortedArray(ArrayList<String> people){
        // Print the first 20 elements of the sorted list

        for (int i = 0; i < Math.min(20, people.size()); i++) {
            System.out.println(people.get(i));
        }

    }

}
