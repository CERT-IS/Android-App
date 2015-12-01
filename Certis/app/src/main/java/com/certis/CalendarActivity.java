package com.certis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class CalendarActivity extends NavigationDrawerActivity
{
    private TextView toolbar_title;

    /* return Read Json */
    ArrayList<String> arrayList;

    /* dialog.xml */
    EditText editSchedule;
    DatePicker datePicker;

    /* Modify */
    int mYear, mMonth, mDay;

    /* main.xml */
    CalendarView calendarView;
    ImageButton scheduleWrite;

    /* ListView */
    ListView scheduleListview;
    ArrayAdapter<String> Adapter;

    /* dialog -> db */
    int year, month, day;
    String content;

    /* view */
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_calendar, main_frame);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(getString(R.string.sub_calender));

        // Connect DB Set up
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Write Schedule
        scheduleWrite = (ImageButton) findViewById(R.id.ScheduleWrite);
        scheduleWrite.bringToFront();

        scheduleWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Insert_Schedule();
            }
        });

        //Init Year, Month, Day, Content
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        //ShowScheduleListView
        ShowScheduleList(year, month+1, day);

        /* Set calendar view */
        HashSet<Date> events = new HashSet<>();
        events.add(new Date());

        calendarView = ((CalendarView)findViewById(R.id.calendar_view));
        calendarView.updateCalendar(events);

        // assign event handler
        calendarView.setEventHandler(new CalendarView.EventHandler()
        {
            @Override
            public void onDayLongPress(Date date)
            {
                DateFormat yyyy = new SimpleDateFormat("yyyy");
                int dY = Integer.parseInt(yyyy.format(date));

                DateFormat mm = new SimpleDateFormat("MM");
                int dM = Integer.parseInt(mm.format(date));

                DateFormat dd = new SimpleDateFormat("dd");
                int dD = Integer.parseInt(dd.format(date));

                Modify_Schedule(dY,dM,dD);
            }
        });
    }

    public void ShowScheduleList(int year, int month, int day) {
        arrayList = new SelectSchedule().executeREAD(year, month);
        SetListView();
    }

    public void SetListView() {
        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        scheduleListview = (ListView) findViewById(R.id.ScheduleList);
        scheduleListview.setAdapter(Adapter);

        scheduleListview.setDivider(new ColorDrawable(0x99000000));
        scheduleListview.setDividerHeight(1);
    }

    public void Insert_Schedule(){
        //Create dialog
        AlertDialog.Builder dlg = new AlertDialog.Builder(CalendarActivity.this);

        //Decorate dialog
        dlg.setTitle("Cert-Is 일정 추가");
        dlg.setIcon(R.drawable.icon);

        //Dialog XML inflate
        dialogView = (View) View.inflate(CalendarActivity.this, R.layout.dialog_write, null);
        dlg.setView(dialogView);

        //After Infalte, create instance
        editSchedule = (EditText) dialogView.findViewById(R.id.EditSchedule);
        datePicker = (DatePicker) dialogView.findViewById(R.id.DatePicker);

        //Dialog button
        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content = editSchedule.getText().toString();

                new InsertSchedule(year, month + 1, day, content);
                ShowScheduleList(year, month + 1, day);
            }
        });

        //Dialog button
        dlg.setNegativeButton("취소", null);

        //Dialog show
        dlg.show();

        //Change Date
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int Year, int MonthOfYear, int DayOfMonth) {
                year = Year;
                month = MonthOfYear;
                day = DayOfMonth;
            }
        });
    }

    public void Modify_Schedule(int y, int m, int d) {
        mYear = y; mMonth = m; mDay = d;

        //Create dialog
        AlertDialog.Builder dlg = new AlertDialog.Builder(CalendarActivity.this);

        //Decorate dialog
        dlg.setTitle("Cert-Is 일정 수정 및 삭제");
        dlg.setIcon(R.drawable.icon);

        //Dialog XML inflate
        dialogView = (View) View.inflate(CalendarActivity.this, R.layout.dialog_modify, null);
        dlg.setView(dialogView);

        //After Infalte, create instance
        editSchedule = (EditText) dialogView.findViewById(R.id.EditSchedule);

        //Init content
        SelectSchedule getContent = new SelectSchedule();
        content = getContent.GetContent(mYear, mMonth, mDay);
        editSchedule.setText(content);

        //Dialog button
        dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content = editSchedule.getText().toString();
                new ModifySchedule(mYear, mMonth, mDay, content);
                ShowScheduleList(year, month + 1, day);
            }
        });

        //Dialog button
        dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeleteSchedule(mYear, mMonth, mDay);
                ShowScheduleList(year, month + 1, day);
            }
        });

        //Dialog show
        dlg.show();
    }
}
