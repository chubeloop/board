package com.boardproject.board.service;

import com.boardproject.board.dto.BoardDTO;
import com.boardproject.board.entity.BoardEntity;
import com.boardproject.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional // jpa query 매서드 외의 별도의 추가된 매서드를 실행하는 경우 반드시 붙여야 함
    public void updateHits(Long id) { //다른 매서드들은 기본적으로 jpa에서 제공하는 기본적인 매서드들이라 정의할 필요가 없는데, 이건 정의해야함
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }
        else return null;
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        /*
        jpa에 update 매서드가 정의되어있지 않음. 하지만 save 하나로 update, insert 들 다 할 수 있음.
        처음에 id 값이 없는 상태로 Entity 객체가 넘어온다면 insert, 있다면 update가 됨
         */
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
