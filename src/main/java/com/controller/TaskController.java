package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        model.addAttribute("task",new Task());
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
    public String displayTasks(@ModelAttribute Task task, Model model) { //getAllTasks
    	
    	String url = BASE_URL + "/viewAllTasks";
    	
    	ResponseEntity<List<Task>> response = restTemplate.exchange(
    			url,
    			org.springframework.http.HttpMethod.GET,
    			null,
    			new ParameterizedTypeReference<List<Task>>() {}
    			);
    	List<Task> tasks =response.getBody();
    	
    	

    	model.addAttribute("tasks",tasks);
    	
        return "viewTasks"; 
    }
    
    
    //Get task By location
    @GetMapping("/getTaskByLocationPage")
    public String getTaskBylocationPage() { //Search Task By Loacation Page
    	
        return "getTaskByLocation"; 
    }
    
      @GetMapping("/getTaskByLocation/{location}")
      public String searchByLocation(@PathVariable String location, Model model) {
          String apiUrl = "http://localhost:7777/getTaskByLocation/" + location;
          ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

          if (response.getStatusCode() == HttpStatus.OK) {
              List<Task> tasks = Arrays.asList(response.getBody());
              model.addAttribute("tasks", tasks);
          } else {
              model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found
             
          }


            return "getTaskByLocation"; // Thymeleaf template name
        }
      
      
      //Get task By Category
      
      @GetMapping("/getTaskByCategoryPage")
      public String getTaskBycategoryPage() { //Tasks By category Page
      	
          return "getTaskByCategory"; 
      }
      
        @GetMapping("/getTaskByCategory/{category}")
        public String searchByCategory(@PathVariable String category, Model model) {
            String apiUrl = "http://localhost:7777/getTaskByCategory/" + category;
            ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Task> tasks = Arrays.asList(response.getBody());
                model.addAttribute("tasks", tasks);
            } else {
                model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found
               
            }


              return "getTaskByCategory"; // Thymeleaf template name
          }
      
      //seaech By Title
        
        @GetMapping("/getTaskByTitlePage")
        public String getTaskBytitlePage() { //Tasks By Title
        	
            return "getTaskByTitle"; 
        }
        
          @GetMapping("/getTaskByTitle/{title}")
          public String searchByTitle(@PathVariable String title, Model model) {
              String apiUrl = "http://localhost:7777/getTaskByName/" + title;   
              ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

              if (response.getStatusCode() == HttpStatus.OK) {
                  List<Task> tasks = Arrays.asList(response.getBody());
                  model.addAttribute("tasks", tasks);
              } else {
                  model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found
                 
              }

                return "getTaskByTitle"; // Thymeleaf template name
            }
        
          //Search By Date
          
          @GetMapping("/getTaskByDatePage")
          public String getTaskBydate() { //SearchAllTasks
          	
              return "getTaskByDate"; 
          }
          
            @GetMapping("/getTaskByDate/{date}")
            public String searchByDate(@PathVariable String date, Model model) {
                String apiUrl = "http://localhost:7777/getTaskByDate/" + date;
                ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    List<Task> tasks = Arrays.asList(response.getBody());
                    model.addAttribute("tasks", tasks);
                } else {
                    model.addAttribute("tasks", new ArrayList<>()); // Empty list if no tasks found
                   
                }


                  return "getTaskByDate"; // Thymeleaf template name
              }  

}