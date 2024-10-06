package lk.ijse.notecollectorspring.dao;

import lk.ijse.notecollectorspring.entity.impl.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteDAO extends JpaRepository<NoteEntity,String> {
}
