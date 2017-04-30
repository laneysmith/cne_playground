package com.example;

import com.example.models.Lesson;
import com.example.models.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private final LessonRepository repository;

    public LessonsController(LessonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    @JsonView(Views.ListView.class)
    public ResponseEntity<Iterable<Lesson>> all() {
        return new ResponseEntity<>(this.repository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.SingleLessonView.class)
    public ResponseEntity<Lesson> lesson(@PathVariable long id) {
        return new ResponseEntity<>(this.repository.findOne(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Lesson lesson) {
        this.repository.save(lesson);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLessonById(@PathVariable Long id) {
        this.repository.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
