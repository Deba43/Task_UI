package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.entity.Task;

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
    public String createTasks(@ModelAttribute Task task, Model model) {
        ResponseEntity<Task> response = restTemplate.postForEntity(
                BASE_URL + "/addTask", task, Task.class);
        model.addAttribute("message", "Task added successfully: " + response.getBody().getTitle());
        return "addTask";

    }

    @GetMapping("/viewAllTasks")
    public String displayTasks(@ModelAttribute Task task, Model model) { 

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

    @GetMapping("/searchTasks")
    public String searchTasks(@ModelAttribute Task task, Model model) { // SearchAllTasks

        String url = BASE_URL + "/viewAllTasks";

        ResponseEntity<List<Task>> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Task>>() {
                });
        List<Task> tasks = response.getBody();

        model.addAttribute("tasks", tasks);

        return "searchTasks";
    }
}