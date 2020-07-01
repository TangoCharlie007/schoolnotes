package com.sam.schoolnotes.utils;

import com.sam.schoolnotes.database.NoteEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SampleDataProvider {
    private static final String SAMPLE_TEXT_1 = "A Simple Note";
    private static final String SAMPLE_TEXT_2 = "A Simple Note with a \n Line feed";
    private static final String SAMPLE_TEXT_3 = "A Simple Note Here, you can read the aptitude questions and answers for your " +
            "interview and entrance exams preparation.";


    private static Date getDate(int diffAmount){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MILLISECOND,diffAmount);

        return calendar.getTime();
    }

    public static List<NoteEntity> getSampleData()
    {
        List<NoteEntity> noteList = new ArrayList<>();

        noteList.add(new NoteEntity(getDate(0),SAMPLE_TEXT_1));
        noteList.add(new NoteEntity(getDate(-1),SAMPLE_TEXT_2));
        noteList.add(new NoteEntity(getDate(-2),SAMPLE_TEXT_3));

        return noteList;
    }
}
