package com.boardproject.board.repository;

import com.boardproject.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    //update db_codingrecipe set board_hits = board_hits +1 where id=?  ==> native query
    @Modifying // update나 delete 같은 query를 실행할 시 반드시 붙여야 함
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id =:id")
    void updateHits(@Param("id") Long id);
}
