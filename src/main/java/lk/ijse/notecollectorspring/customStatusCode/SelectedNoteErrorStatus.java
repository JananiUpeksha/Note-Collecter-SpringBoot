package lk.ijse.notecollectorspring.customStatusCode;

import lk.ijse.notecollectorspring.dto.NoteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SelectedNoteErrorStatus implements NoteStatus {
    private int statusCode;
    private String statusMessage;
}
