package lk.ijse.notecollectorspring.controller;

import lk.ijse.notecollectorspring.customStatusCode.SelectedNoteErrorStatus;
import lk.ijse.notecollectorspring.dto.NoteStatus;
import lk.ijse.notecollectorspring.dto.impl.NoteDTO;
import lk.ijse.notecollectorspring.exception.DataPersistException;
import lk.ijse.notecollectorspring.exception.NoteNotFoundException;
import lk.ijse.notecollectorspring.service.NoteService;
import lk.ijse.notecollectorspring.utill.RegexProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController//api end point ekak kiyla
@RequestMapping("api/v1/notes")//api/-api end point ekak kiyla      v1/ - version maru weddi wens krnwa      /note - url eka resolve wena class eke name
public class NoteController  {
    @Autowired /*Interface ekak inject kranna*/
    private NoteService noteService;/*run time ekedi impl class eka provid krnwa*/

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)/*json ob eka java ob ekakata map krnna one*/
    public ResponseEntity<Void> saveNote(@RequestBody NoteDTO noteDTO){
        /*var noteServiceImpl = new NoteServiceImpl(); Methnadi gththe impl eka end point athule hduwama eka endpoint eken ekata ai hdnna ne*/
        /*noteServiceImpl.saveNote(noteDTO);argument eka meka methna thma me welawe pass krna actual valu eka thiyenne*/
            try {
                noteService.saveNote(noteDTO);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }catch (DataPersistException e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }catch (Exception e){
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }
    @GetMapping(value = "/{noteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public NoteStatus getSelectedNote(@PathVariable("noteId") String noteId){
        String regexForUserID = "^NOTE-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserID);
        var regexMatcher = regexPattern.matcher(noteId);
        if (!regexMatcher.matches()) {
            return new SelectedNoteErrorStatus(1,"Note ID is not valid");
        }
        return noteService.getNote(noteId);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<NoteDTO> getAllNote(){
        return noteService.getAllNotes();
    }
    @DeleteMapping(value = "/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable("noteId") String noteId){
        /*String regexForUserID = "^NOTE-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserID);
        var regexMatcher = regexPattern.matcher(noteId);
        try {
            if (!regexMatcher.matches()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            noteService.deleteNote(noteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoteNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }*/
        try {
            if (!RegexProcess.noteIdMatcher(noteId)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            noteService.deleteNote(noteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoteNotFoundException e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping(value = "/{noteId}")
    public ResponseEntity<Void> updateNote(@PathVariable("noteId") String noteId,@RequestBody NoteDTO updateNoteDTO){
        String regexForUserID = "^NOTE-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForUserID);
        var regexMatcher = regexPattern.matcher(noteId);
        try {
            if (!regexMatcher.matches() || updateNoteDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            noteService.updateNote(noteId, updateNoteDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoteNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
