package com.boardproject.board.service;

import com.boardproject.board.dto.BoardDTO;
import com.boardproject.board.entity.BoardEntity;
import com.boardproject.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// DTO -> Entity (Entity Class, Repository로 넘겨줄 때)
// Entity -> DTO (DTO Class, Repository에서 넘겨받을 떄)

@Service
@RequiredArgsConstructor // 생성자 주입을 통해 DI를 할 수 있도록 자동으로 설정
public class BoardService {

    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO){
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity); // save 매서드는 Entity 객체를 매개변수로 해야함
    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(); // DB에서 오는 data는 Entity 형식으로 넘어옴 -> DTO로 변환
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }
}
