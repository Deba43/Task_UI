package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.entity.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
public class TaskController {

    private final String BASE_URL = "http://localhost:7777";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/createTasks")
    public String createTask(Model model) {
        model.addAttribute("task", new Task());
        return "addTask";
    }

    @PostMapping("/addTask")
    public String createTasks(@ModelAttribute Task task, BindingResult result, Model model) {

        try {
            ResponseEntity<Task> response = restTemplate.postForEntity(
                    BASE_URL + "/addTask", task, Task.class);
            model.addAttribute("message", "Task added successfully: " + response.getBody().getTitle());
        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }
        return "addTask";

    }

    @GetMapping("/viewAllTasks")
    public String displayTasks(@ModelAttribute Task task, Model model) { // getAllTasks

        String url = BASE_URL + "/viewAllTasks";

        ResponseEntity<List<Task>> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {
                });
        List<Task> tasks = response.getBody();

        model.addAttribute("tasks", tasks);

        return "viewTasks";
    }

    // Get task By location
    @GetMapping("/getTaskByLocationPage")
    public String getTaskBylocationPage() { // Search Task By Loacation Page

        return "getTaskByLocation";
    }

    @GetMapping("/getTaskByLocation/{location}")
    public String searchByLocation(@PathVariable String location, BindingResult result, Model model) {
        String apiUrl = "http://localhost:7777/getTaskByLocation/" + location;

        try {
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found

            }
        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }

        return "getTaskByLocation"; // Thymeleaf template name
    }

    // Get task By Category

    @GetMapping("/getTaskByCategoryPage")
    public String getTaskBycategoryPage() { // Tasks By category Page

        return "getTaskByCategory";
    }

    @GetMapping("/getTaskByCategory/{category}")
    public String searchByCategory(@PathVariable String category, BindingResult result, Model model) {
        String apiUrl = "http://localhost:7777/getTaskByCategory/" + category;

        try {
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found

            }

        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }

        return "getTaskByCategory"; // Thymeleaf template name
    }

    // seaech By Title

    @GetMapping("/getTaskByTitlePage")
    public String getTaskBytitlePage() { // Tasks By Title

        return "getTaskByTitle";
    }

    @GetMapping("/getTaskByTitle/{title}")
    public String searchByTitle(@PathVariable String title, BindingResult result, Model model) {
        String apiUrl = "http://localhost:7777/getTaskByName/" + title;
        try {
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found

            }
        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }

        return "getTaskByTitle"; // Thymeleaf template name
    }

    

    @GetMapping("/getTaskByDatePage")
    public String getTaskBydate() { // SearchAllTasks

        return "getTaskByDate";
    }

    @GetMapping("/getTaskByDate/{date}")
    public String searchByDate(@PathVariable String date, BindingResult result, Model model) {
        String apiUrl = "http://localhost:7777/getTaskByDate/" + date;
        try {
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found

            }
        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }

        return "getTaskByDate"; // Thymeleaf template name
    }

    @GetMapping("/editTask/{id}")
    public String getupdateTaskPage(Model model, BindingResult result, @PathVariable Long id) { // SearchAllTasks
        String apiUrl = "http://localhost:7777/getTaskById/" + id;
        try {
            ResponseEntity<Task> response = restTemplate.getForEntity(apiUrl, Task.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                model.addAttribute("task", response.getBody());
            } else {
                System.out.print("Task Not Found");

            }
        } catch (HttpClientErrorException e) {

            Map<String, String> errors = null;
            try {
                errors = new ObjectMapper().readValue(
                        e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                        });
            } catch (JsonMappingException e1) {

                e1.printStackTrace();
            } catch (JsonProcessingException e1) {

                e1.printStackTrace();
            }

            for (Map.Entry<String, String> entryset : errors.entrySet()) {
                String field = entryset.getKey();
                String errorMsg = entryset.getValue();
                result.rejectValue(field, "", errorMsg);
            }

        }
        return "updateTasks";
    }

    @PostMapping("/updateTask/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute Task task, Model model) {

        restTemplate.put(BASE_URL + "/update/" + id, task);

        return "updateTasks";
    }

}