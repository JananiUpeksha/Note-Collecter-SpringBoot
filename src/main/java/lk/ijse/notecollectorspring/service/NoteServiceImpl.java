package lk.ijse.notecollectorspring.service;

import lk.ijse.notecollectorspring.customStatusCode.SelectedNoteErrorStatus;
import lk.ijse.notecollectorspring.dao.NoteDAO;
import lk.ijse.notecollectorspring.dto.NoteStatus;
import lk.ijse.notecollectorspring.dto.impl.NoteDTO;
import lk.ijse.notecollectorspring.entity.impl.NoteEntity;
import lk.ijse.notecollectorspring.exception.DataPersistException;
import lk.ijse.notecollectorspring.exception.NoteNotFoundException;
import lk.ijse.notecollectorspring.utill.AppUtill;
import lk.ijse.notecollectorspring.utill.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service/*@comonent meta anotate krla thiynwa methnath meke sservice class wla functions thiynwa ethkota thma eya spring wlin mng krnne*/
@Transactional
public class NoteServiceImpl implements NoteService{
    @Autowired
    private NoteDAO noteDAO;
    @Autowired
    private Mapping mapping;
    private static List<NoteDTO> noteDTOList = new ArrayList<>();
    public NoteServiceImpl(){
        noteDTOList.add(new NoteDTO("NOTE-b9a081bb-d861-49d6-a655-02a898231cbb","Title1","desc1",
                "date1","level1","userid1"));
        noteDTOList.add(new NoteDTO("NOTE-b9a081bb-d861-49d6-a655-02a898231ccb","Title2","desc2",
                "date","level2","userid2"));
        noteDTOList.add(new NoteDTO("NOTE-b9a081bb-d861-49d6-a655-02a898231cdb","Title3","desc3",
                "date","level3","userid3"));
        noteDTOList.add(new NoteDTO("NOTE-b9a081bb-d861-49d6-a655-02a898231ceb","Title4","desc4",
                "date","level4","userid4"));
    }
    @Override
    public void saveNote(NoteDTO noteDTO) {
        noteDTO.setNoteId(AppUtill.generateNoteId());
        NoteEntity savedNote = noteDAO.save(mapping.toNoteEntity(noteDTO));
        if(savedNote == null){
            throw new DataPersistException("Note not saved");
        }

    }

    @Override
    public List<NoteDTO> getAllNotes() {
        return mapping.asNoteDTOList( noteDAO.findAll());
    }

    @Override
    public NoteStatus getNote(String noteId) {
        if(noteDAO.existsById(noteId)){
            var selectedUser = noteDAO.getReferenceById(noteId);
            return mapping.toNoteDTO(selectedUser);
        }else {
            return new SelectedNoteErrorStatus(2,"Selected note not found");
        }
    }

    @Override
    public void deleteNote(String noteID) {
        Optional<NoteEntity> foundNote = noteDAO.findById(noteID);
        if (!foundNote.isPresent()){
            throw new NoteNotFoundException("Note not found by id " + noteID);
        }else {
            noteDAO.deleteById(noteID);
        }
    }

    @Override
    public void updateNote(String noteId, NoteDTO noteDTO) {
        Optional<NoteEntity> findNote = noteDAO.findById(noteId);
        if (!findNote.isPresent()){
            throw new NoteNotFoundException("Note not found");
        }else {
            findNote.get().setNoteTitle(noteDTO.getNoteTitle());
            findNote.get().setNoteDesc(noteDTO.getNoteDesc());
            findNote.get().setCreatedDate(noteDTO.getCreatedDate());
            findNote.get().setPriorityLevel(noteDTO.getPriorityLevel());
        }
    }
}
