package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public String createTask(@RequestParam Long org_id, Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("org_id", org_id);
        return "addTask";
    }

    @PostMapping("/addTask")
    public String createTasks(@ModelAttribute Task task, @RequestParam Long org_id, Model model) {
        try {

            ResponseEntity<Task> response = restTemplate.postForEntity(
                    BASE_URL + "/Organization/{org_id}/addTask",
                    task,
                    Task.class,
                    org_id);
            model.addAttribute("message", "Task added successfully: " + response.getBody().getTitle());
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "addTask";
    }

    @GetMapping("/viewAllTasks")
    public String displayTasks(@ModelAttribute Task task, Model model) {
        try {
            String url = BASE_URL + "/viewAllTasks";
            ResponseEntity<List<Task>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Task>>() {
                    });
            List<Task> tasks = response.getBody();
            model.addAttribute("tasks", tasks);
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "viewTasks";
    }

    @GetMapping("/getTaskByLocationPage")
    public String getTaskBylocationPage() {
        return "getTaskByLocation";
    }

    @GetMapping("/getTaskByLocation/{location}")
    public String searchByLocation(@PathVariable String location, Model model) {
        try {
            String apiUrl = BASE_URL + "/getTaskByLocation/" + location;
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>());
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "getTaskByLocation";
    }

    @GetMapping("/getTaskByCategoryPage")
    public String getTaskBycategoryPage() {
        return "getTaskByCategory";
    }

    @GetMapping("/getTaskByCategory/{category}")
    public String searchByCategory(@PathVariable String category, Model model) {
        try {
            String apiUrl = BASE_URL + "/getTaskByCategory/" + category;
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>());
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "getTaskByCategory";
    }

    @GetMapping("/getTaskByTitlePage")
    public String getTaskBytitlePage() {
        return "getTaskByTitle";
    }

    @GetMapping("/getTaskByTitle/{title}")
    public String searchByTitle(@PathVariable String title, Model model) {
        try {
            String apiUrl = BASE_URL + "/getTaskByName/" + title;
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>());
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "getTaskByTitle";
    }

    @GetMapping("/getTaskByDatePage")
    public String getTaskBydate() {
        return "getTaskByDate";
    }

    @GetMapping("/getTaskByDate/{date}")
    public String searchByDate(@PathVariable String date, Model model) {
        try {
            String apiUrl = BASE_URL + "/getTaskByDate/" + date;
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>());
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "getTaskByDate";
    }

    @GetMapping("/editTask/{id}")
    public String getupdateTaskPage(Model model, @PathVariable Long id) {
        try {
            String apiUrl = BASE_URL + "/getTaskById/" + id;
            ResponseEntity<Task> response = restTemplate.getForEntity(apiUrl, Task.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("task", response.getBody());
            } else {
                model.addAttribute("errorMessage", "Task not found");
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "updateTasks";
    }

    @PutMapping("/updateTask/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute Task task, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Task> requestEntity = new HttpEntity<>(task, headers);
            ResponseEntity<Task> response = restTemplate.exchange(
                    BASE_URL + "/update/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    Task.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("updatedtask", "Task Updated Successfully");
            } else {
                model.addAttribute("error", "Update failed.");
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "updateTasks";
    }

    private void handleException(HttpClientErrorException e, Model model) {
        try {
            Map<String, String> errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {
                    });
            model.addAttribute("errorMessage", errors.get("message"));
        } catch (JsonProcessingException ex) {
            model.addAttribute("errorMessage", "An error occurred while processing the request.");
        }
    }
}