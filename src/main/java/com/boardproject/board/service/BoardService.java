package com.boardproject.board.service;

import com.boardproject.board.dto.BoardDTO;
import com.boardproject.board.entity.BoardEntity;
import com.boardproject.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1; // 몇 페이지를 볼건지, page index가 0부터 시작하기 때문
        int pageLimit = 3;  // 한 페이지에 보여줄 글 갯수
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        // Page 객체로 받았기 때문에 쓸 수 있는 매서드들
        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board ->
                new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreationTime()));
                //board 변수로 하나씩 꺼내서 DTO객체로 옮겨담음 (매서드들도 같이 옮겨줌)
        return boardDTOS;
    }
}
