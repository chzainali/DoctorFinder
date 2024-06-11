package com.dev.doctorfinder.home;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dev.doctorfinder.R;
import com.dev.doctorfinder.databinding.FragmentBookAppointmentBinding;
import com.dev.doctorfinder.home.adapter.BookingAdaptor;
import com.dev.doctorfinder.home.model.BookingModel;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookAppointmentFragment extends Fragment {

    private FragmentBookAppointmentBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DoctorsModel doctor;
    FirebaseAuth firebaseAuth;

    public BookAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentBookAppointmentBinding.inflate(getLayoutInflater(),container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("appointments");
        firebaseAuth=FirebaseAuth.getInstance();

        doctor= (DoctorsModel) getArguments().getSerializable("data");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker(binding.tvTime);
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog=new ProgressDialog(requireContext());
                dialog.setMessage("Adding Appointmnet");
                dialog.show();


                String id=databaseReference.push().getKey();
                String docId=doctor.getId();
                String userId=firebaseAuth.getCurrentUser().getUid();
                String date=binding.tvTime.getText().toString();;
                String title=binding.etTitle.getText().toString();
                String details=binding.etDetails.getText().toString();
                String userName=firebaseAuth.getCurrentUser().getEmail();

                if(!date.isEmpty() && !title.isEmpty() && !details.isEmpty()) {
                    BookingModel model=new BookingModel(id,userId,docId,formatTime(date).toString(),doctor.getName(),title,"Pending",details,userName);
                    databaseReference.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                Toast.makeText(requireContext(), "Appointment added", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else{
                    Toast.makeText(requireActivity(), "Error some Fields are missing", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void ShowDatePicker(TextView textView) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mMonth = monthOfYear + 1;
                        mYear = year;
                        showTimePicker(textView);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showTimePicker(TextView textView) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;
                        textView.setText(mDay + "/" + mMonth + "/" + mYear + " " + mHour + ":" + mMinute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private Long formatTime(String date) {
        long millis = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        try {
            Date d = f.parse(date);
            millis = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }
}