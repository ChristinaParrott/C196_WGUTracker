package com.example.wgutracker.utilities;

import com.example.wgutracker.database.NoteEntity;

import java.util.ArrayList;
import java.util.List;

public class SampleNoteData {
    private static final String NOTE_NAME_1 = "Sample Note 1";
    private static final String NOTE_NAME_2 = "Sample Note 2";
    private static final String NOTE_NAME_3 = "Sample Note 3";

    private static final String NOTE_TEXT_1 = "cursus turpis massa tincidunt duicursus turpis massa tincidunt dui";
    private static final String NOTE_TEXT_2 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Erat pellentesque adipiscing commodo elit at imperdiet. Suscipit tellus mauris a diam maecenas sed. Tortor consequat id porta nibh venenatis. Nisl nunc mi ipsum faucibus vitae aliquet nec ullamcorper.";
    private static final String NOTE_TEXT_3 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Quam vulputate dignissim suspendisse in est ante.";


    public static List<NoteEntity> getNotes(){
        List<NoteEntity> notes = new ArrayList<>();
        notes.add(new NoteEntity(NOTE_NAME_1,NOTE_TEXT_1, 1));
        notes.add(new NoteEntity(NOTE_NAME_2,NOTE_TEXT_2, 2));
        notes.add(new NoteEntity(NOTE_NAME_3,NOTE_TEXT_3, 3));
        return notes;
    }
}
