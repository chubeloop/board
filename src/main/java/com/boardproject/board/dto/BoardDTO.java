package com.boardproject.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter // lombok 라이브러리가 제공. 각각 필드의 getter setter 매서드 자동 생성
@Setter
@ToString
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreateTime;
    private LocalDateTime boardUpdateTime;



}
