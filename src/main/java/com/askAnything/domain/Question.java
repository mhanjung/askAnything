package com.askAnything.domain;

import javax.persistence.ForeignKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.askAnything.LocalDateTimeConverter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Question {
  @Id
  @GeneratedValue
  @JsonProperty
  private Long id;
  
  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
  @JsonProperty
  private User writer;
  
  @JsonProperty
  private String title;
  
  @Lob
  @JsonProperty
  private String contents;
  
  @JsonProperty
  private Integer countOfAnswer = 0;
  
  @Convert(converter = LocalDateTimeConverter.class)
  private LocalDateTime createDate;
  
  @OneToMany(mappedBy="question")
  @OrderBy("id DESC")
  private List<Answer> answers;
  
  public Question(){}
  
  public Question(User writer, String title, String contents) {
    super();
    this.writer = writer;
    this.title = title;
    this.contents = contents;
    this.createDate = LocalDateTime.now();
  }
  
  public String getFormattedCreateDate(){
    if(createDate == null){
      return "";
    }
    return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH:mm:ss"));
  }

  public void update(String title, String contents) {
    this.title = title;
    this.contents = contents;
  }

  public boolean isSameWriter(User loginUser) {
    return this.writer.equals(loginUser);
  }


  public void addAnswer() {
    this.countOfAnswer += 1;
  }
  
  public void deleteAnswer(){
    this.countOfAnswer -= 1;
  }
}
