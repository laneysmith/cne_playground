package com.example;

import com.example.models.Lesson;
import com.google.gson.GsonBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;
import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LessonsController.class)
public class LessonsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    LessonRepository lessonRepository;

    private long id;
    private String title;
    private String formattedDate;
    private Lesson lesson;
    private String lessonJson;

    @Before
    public void setUp() throws Exception {
        id = new Random().nextLong();
        title = "Some lesson";
        DateTime date = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        formattedDate = date.toString(fmt);
        lesson = new Lesson(id, title, date);

        lessonJson = new GsonBuilder()
                .create()
                .toJson(lesson);
    }

    @Test
    public void getAllLessons() throws Exception {
        when(this.lessonRepository.findAll()).thenReturn(Collections.singletonList(lesson));

        MockHttpServletRequestBuilder request = get("/lessons")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", equalTo(id)))
                .andExpect(jsonPath("$[0].title", equalTo(title)));
    }

    @Test
    public void getLessonById() throws Exception {
        when(this.lessonRepository.findOne(id)).thenReturn(lesson);

        MockHttpServletRequestBuilder request = get(String.format("/lessons/%d", id))
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id)))
                .andExpect(jsonPath("$.title", equalTo(title)))
                .andExpect(jsonPath("$.deliveredOn", equalTo(formattedDate)));
    }

    @Test
    public void postNewLesson() throws Exception {
        MockHttpServletRequestBuilder request = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(lessonJson);
        this.mvc.perform(request)
                .andExpect(status().isCreated());

        verify(this.lessonRepository).save(any(Lesson.class));
    }

    @Test
    public void deleteLessonById() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(lessonJson);
        this.mvc.perform(postRequest)
                .andExpect(status().isCreated());

        verify(this.lessonRepository).save(any(Lesson.class));
        when(this.lessonRepository.findOne(id)).thenReturn(lesson);

        MockHttpServletRequestBuilder deleteRequest = delete("/lessons/{id}", id);
        this.mvc.perform(deleteRequest)
                .andExpect(status().isNoContent());

        verify(this.lessonRepository).save(any(Lesson.class));
        when(this.lessonRepository.findOne(id)).thenReturn(null);
    }
}