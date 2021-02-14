package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/*
You can simply run this app to complete this exercise.
 */
public class Application {

    private static final Path FILE_NAME = Path.of("../token.txt");

    public static void main(String[] args) throws IOException {
        String token = Files.readString(FILE_NAME, StandardCharsets.UTF_8);
        if(token == null || token.length() != 66) {
            System.err.println("Token was not found. Make sure you have a token.txt in the root of your exercises folder, with subfolders for each exercise.");
            System.exit(-1);
        }

        final String exerciseName = Path.of("").toAbsolutePath().getFileName().toString();
        if(exerciseName == null || !exerciseName.contains("_")) {
            System.err.println("Could not verify exercise name. Make sure the folder name matches the name used by the github repository.");
            System.exit(-1);
        }

        final String apiUrl = "https://deep-dive-java-course.herokuapp.com/api/exercise/" + exerciseName;

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(new HashMap<>()), headers);
        ResponseEntity<String> result = restTemplate.postForEntity(apiUrl, entity, String.class);
        if(result.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("Submission completed and accepted");
        } else {
            System.err.println("Submitted but refused: " + result.getStatusCode());
            System.exit(-1);
        }
    }
}
