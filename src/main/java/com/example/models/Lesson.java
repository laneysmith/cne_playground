package com.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.ListView.class)
    private final Long id;

    @JsonView(Views.ListView.class)
    private final String title;

    @Column(columnDefinition = "date")
    @JsonSerialize(using=CustomDateSerializer.class)
    @JsonView(Views.SingleLessonView.class)
    private final DateTime deliveredOn;

    public Lesson(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("deliveredOn") DateTime deliveredOn) {
        this.id = id;
        this.title = title;
        this.deliveredOn = deliveredOn;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public DateTime getDeliveredOn() {
        return deliveredOn;
    }
}