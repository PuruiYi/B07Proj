package com.example.sport_events_scheduler;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEventDialogFragment extends DialogFragment {

    private Remote remote;
    private String venue;
    private EditText nameText, capacityText;
    private TextView timeText, dateText, startText, endText;
    private Button addBtn;
    private ImageButton closeBtn;
    private int tv_year, tv_month, tv_day, start_hour, start_min, end_hour, end_min;

    public AddEventDialogFragment(String venue) {
        this.venue = venue;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogActivity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_event, null);
        // Components.
        remote = new Remote();
        nameText = view.findViewById(R.id.nameVenue);
        capacityText = view.findViewById(R.id.capacityVenue);
        timeText = view.findViewById(R.id.event_time);
        dateText = view.findViewById(R.id.dateVenue);
        startText = view.findViewById(R.id.startVenue);
        endText = view.findViewById(R.id.endVenue);
        addBtn = view.findViewById(R.id.addButton);
        closeBtn = view.findViewById(R.id.addEventCloseBtn);
        //
        datePickerSetup();
        timePickerSetup();
        // Builder setup.
        builder.setView(view);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (eventRequest())
                   dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void datePickerSetup() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dateText.setError(null);
                        tv_year = year;
                        tv_month = month;
                        tv_day = day;
                        calendar.set(year, month, day);
                        dateText.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    }

                },year,month,day);
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis() - 1000);
                datePicker.show();
            }
        });
    }
    private void timePickerSetup() {
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear previous inputs.
                timeText.setText("");
                startText.setText("");
                endText.setText("");
                timeText.setError(null);

                TimePickerDialog startTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeText.setError(null);

                        Calendar calendar = Calendar.getInstance();
                        start_hour = hour;
                        start_min = minute;
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        if (calendar.before(Calendar.getInstance())) {
                            timeText.setError("The start time has passed.");
                        }
                        else {
                            String startTime = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                            startText.setText(startTime);
                            timeText.setText(startTime);
                        }
                        timeText.setText(timeText.getText().toString() + " - ");
                    }
                },Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
                startTimePicker.setTitle("Select a start time");




                startTimePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (timeText.getError() == null) {
                            TimePickerDialog endTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                    timeText.setError(null);

                                    Calendar end = Calendar.getInstance();
                                    Calendar start = Calendar.getInstance();
                                    start.set(Calendar.HOUR_OF_DAY, start_hour);
                                    start.set(Calendar.MINUTE, start_min);

                                    end_hour = hour;
                                    end_min = minute;
                                    end.set(Calendar.HOUR_OF_DAY, hour);
                                    end.set(Calendar.MINUTE, minute);
                                    if (end.before(start) || (start_hour == end_hour && start_min == end_min))
                                        timeText.setError("The end time should not be earlier.");
                                    else {
                                        String startTime = timeText.getText().toString();
                                        String endTime = new SimpleDateFormat("HH:mm").format(end.getTime());
                                        endText.setText(endTime);
                                        timeText.setText(startTime + endTime);
                                    }
                                }
                            },start_hour, start_min, true);
                            endTimePicker.setTitle("Select a end time");
                            endTimePicker.show();
                            Toast.makeText(getActivity(), "Time to select a end time", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                startTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        timeText.setError("Select start time action cancelled.");
                    }
                });
                startTimePicker.show();
            }
        });

    }

    private boolean eventRequest() {

        /** Event info. */
        String name = nameText.getText().toString();
        String capacity = capacityText.getText().toString();
        String date = dateText.getText().toString();
        String start = startText.getText().toString();
        String end = endText.getText().toString();
        /** Validate Inputs. */
        if (!isValidEvent(name, capacity, date, start, end))
            return false;
        /** Clear errors. */
        nameText.setError(null);
        capacityText.setError(null);
        dateText.setError(null);
        timeText.setError(null);
        startText.setError(null);
        endText.setError(null);
        /** Reference to the path. */
        DatabaseReference ref = remote.getPendingEventRef().push();
        String id = ref.getKey();
        /** SportEvent Object. */
        Event event = new Event(id, name, Integer.parseInt(capacity), 0, date, start, end, venue);
        /** Push data into the database. */
        ref.setValue(event);
        /** Clear Inputs. */
        nameText.setText("");
        capacityText.setText("");
        dateText.setText("");
        timeText.setText("");
        startText.setText("");
        endText.setText("");
        return true;
    }

    /** Validate event information provided by users. */
    private boolean isValidEvent(String name, String capacity, String date, String start, String end) {
        Pattern pattern = Pattern.compile("\\s*");
        Pattern time = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]");
        Matcher nameM = pattern.matcher(name);
        Matcher capacityM = pattern.matcher(capacity);
        Matcher dateM = pattern.matcher(date);
        Matcher startM = time.matcher(start);
        Matcher endM = time.matcher(end);
        if (nameM.matches()) {
            nameText.setError("Name cannot be empty.");
            nameText.requestFocus();
            return false;
        }
        if (capacityM.matches() || Integer.parseInt(capacity) <= 0) {
            capacityText.setError("Capacity should be > 0.");
            capacityText.requestFocus();
            return false;
        }
        if (dateM.matches()) {
            dateText.setError("Date cannot be empty.");
            dateText.requestFocus();
            return false;
        }
        if (!startM.matches()) {
            timeText.setError("Invalid start time.");
            timeText.requestFocus();
            return false;
        }
        if (!endM.matches()) {
            timeText.setError("Invalid end time.");
            timeText.requestFocus();
            return false;
        }
        return true;
    }

}
